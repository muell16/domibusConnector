package eu.domibus.connector.web.view.areas.configuration.security;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.common.service.ConfigurationPropertyManagerService;
import eu.domibus.connector.common.service.CurrentBusinessDomain;
import eu.domibus.connector.common.spring.BusinessDomainScope;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.dss.service.CommonCertificateVerifierFactory;
import eu.domibus.connector.security.configuration.DCBusinessDocumentValidationConfigurationProperties;
import eu.domibus.connector.security.container.service.ECodexContainerFactoryService;
import eu.domibus.connector.web.ui.binder.SpringBeanValidationBinderFactory;
import eu.domibus.connector.web.utils.RoleRequired;
import eu.domibus.connector.web.view.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.web.view.areas.configuration.TabMetadata;
import eu.ecodex.dss.model.BusinessContent;
import eu.ecodex.dss.model.ECodexContainer;
import eu.ecodex.dss.service.ECodexContainerService;
import eu.ecodex.dss.service.ECodexException;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.validation.Validator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Collectors;

@Component
@UIScope
@Route(value = BusinessDocumentValidationConfigPanel.ROUTE, layout = ConfigurationLayout.class)
@RoleRequired(role = "ADMIN")
@TabMetadata(title = "ECodex Business Document Verification", tabGroup = ConfigurationLayout.TAB_GROUP_NAME)
public class BusinessDocumentValidationConfigPanel extends VerticalLayout implements AfterNavigationObserver {

    public static final String ROUTE = "businessDocumentValidation";

    private final ConfigurationPropertyManagerService configurationPropertyManagerService;
    private final SpringBeanValidationBinderFactory springBeanValidationBinderFactory;
    private final BusinessDocumentValidationConfigForm form;
    private final Label errorField;
    private final javax.validation.Validator javaxValidator;
    private final ECodexContainerFactoryService eCodexContainerFactoryService;

    private Binder<DCBusinessDocumentValidationConfigurationProperties> binder;
    private DCBusinessDocumentValidationConfigurationProperties boundConfigValue;

    public BusinessDocumentValidationConfigPanel(ConfigurationPropertyManagerService configurationPropertyManagerService,
                                                 BusinessDocumentValidationConfigForm form,
                                                 SpringBeanValidationBinderFactory springBeanValidationBinderFactory,
                                                 Validator validator, ECodexContainerFactoryService eCodexContainerFactoryService) {
        this.configurationPropertyManagerService = configurationPropertyManagerService;
        this.springBeanValidationBinderFactory = springBeanValidationBinderFactory;
        this.form = form;
        this.javaxValidator = validator;
        this.eCodexContainerFactoryService = eCodexContainerFactoryService;
        this.errorField = new Label("");

        initUi();
    }

    private void initUi() {

        VerticalLayout configDiv = new VerticalLayout();

        Button saveChanges = new Button("Save Changes");
        saveChanges.addClickListener(this::saveChangesButtonClicked);
        configDiv.add(saveChanges);

        Button reset = new Button("Reset Changes");
        reset.addClickListener(this::resetButtonClicked);
        configDiv.add(reset);

        configDiv.add(errorField);

        Class<DCBusinessDocumentValidationConfigurationProperties> configurationClazz = DCBusinessDocumentValidationConfigurationProperties.class;

        binder = springBeanValidationBinderFactory.create(configurationClazz);
        binder.setStatusLabel(errorField);

        form.bindInstanceFields(binder);
        configDiv.add(form);

        add(configDiv);

        //TODO: put in different Tab!
        VerticalLayout tryDocumentDiv = new VerticalLayout();

        Label l = new Label("Upload any signed document and see the certificate validation result");
        tryDocumentDiv.add(l);

        MemoryBuffer buffer = new MemoryBuffer ();
        Upload upload = new Upload(buffer);
        upload.setMaxFiles(1);
        upload.setId("uploadBusinessDocTest");

        Label uploadResultLabel = new Label("");

        upload.addSucceededListener(event -> {
            try {
                CurrentBusinessDomain.setCurrentBusinessDomain(DomibusConnectorBusinessDomain.getDefaultMessageLaneId());
                String fileName = buffer.getFileName();

                byte[] bytes = StreamUtils.copyToByteArray(buffer.getInputStream());
                DSSDocument document = new InMemoryDocument(bytes, fileName);


                ECodexContainerService eCodexContainerService = eCodexContainerFactoryService.createECodexContainerService(new DomibusConnectorMessage());
                BusinessContent businessContent = new BusinessContent();
                businessContent.setDocument(document);
                ECodexContainer eCodexContainer = eCodexContainerService.create(businessContent);


                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                eCodexContainer.getTokenXML().writeTo(byteArrayOutputStream);

                uploadResultLabel.setText("File " + fileName + " uploaded\n" +
                        "Legal Disclaimer " + eCodexContainer.getToken().getLegalValidationResultDisclaimer() +"\n" +
                        "Legal Trust Level " + eCodexContainer.getToken().getLegalValidationResult().getTrustLevel().getText()
                );

                uploadResultLabel.getStyle().set("color", "green");

                //TODO: make ecodex container downloadable

            } catch (IOException ioe) {
                uploadResultLabel.setText("File upload failed!");
                uploadResultLabel.getStyle().set("color", "red");
            } catch (ECodexException e) {
                e.printStackTrace();
                uploadResultLabel.setText("eCodex processing failed!");
                uploadResultLabel.getStyle().set("color", "red");
            } finally {
                CurrentBusinessDomain.setCurrentBusinessDomain(null);
            }
        });
        upload.addFailedListener(e -> {
            uploadResultLabel.setText("File upload failed!");
            uploadResultLabel.getStyle().set("color", "red");
        });

        tryDocumentDiv.add(upload);
        tryDocumentDiv.add(uploadResultLabel);

        add(tryDocumentDiv);

    }


    private void resetButtonClicked(ClickEvent<Button> buttonClickEvent) {
        DCBusinessDocumentValidationConfigurationProperties currentConfig = readConfigFromPropertyService();
        binder.readBean(currentConfig); //reset config
    }

    private DCBusinessDocumentValidationConfigurationProperties readConfigFromPropertyService() {
        return configurationPropertyManagerService.loadConfiguration(
                DomibusConnectorBusinessDomain.getDefaultMessageLaneId(),
                DCBusinessDocumentValidationConfigurationProperties.class);
    }

    private void saveChangesButtonClicked(ClickEvent<Button> buttonClickEvent) {

        BinderValidationStatus<DCBusinessDocumentValidationConfigurationProperties> validate = binder.validate();
        if (validate.isOk()) {
            try {
                binder.writeBean(this.boundConfigValue);
            } catch (ValidationException e) {
                //should not occur since validate.isOk()
                throw new RuntimeException(e);
            }
            //write config update...
            configurationPropertyManagerService.updateConfiguration(
                    DomibusConnectorBusinessDomain.getDefaultMessageLaneId(),
                    boundConfigValue);
        } else {
            Notification.show("Error, cannot save due:\n" + validate.getBeanValidationErrors()
                    .stream()
                    .map(vr -> vr.getErrorMessage())
                    .collect(Collectors.joining("\n"))
            );
        }

    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        this.boundConfigValue = readConfigFromPropertyService();
        binder.setBean(boundConfigValue); //bind bean
    }

}
