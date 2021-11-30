package eu.domibus.connector.ui.view.areas.configuration.security;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.common.service.ConfigurationPropertyManagerService;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.security.configuration.DCEcodexContainerProperties;
import eu.domibus.connector.ui.utils.binder.SpringBeanValidationBinderFactory;
import eu.domibus.connector.ui.utils.RoleRequired;
import eu.domibus.connector.ui.view.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.ui.view.areas.configuration.TabMetadata;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@UIScope
@Route(value = EcxContainerConfigPanel.ROUTE, layout = ConfigurationLayout.class)
@RoleRequired(role = "ADMIN")
@TabMetadata(title = "ECodex Container Configuration", tabGroup = ConfigurationLayout.TAB_GROUP_NAME)
@Order(6)
public class EcxContainerConfigPanel extends VerticalLayout implements AfterNavigationObserver {

    public static final String ROUTE = "ecxContainer";

    private final ConfigurationPropertyManagerService configurationPropertyManagerService;
    private final SpringBeanValidationBinderFactory springBeanValidationBinderFactory;

    private final Label errorField;
    private final EcxContainerConfigForm ecxContainerConfigForm;
    private Binder<DCEcodexContainerProperties> binder;
    private DCEcodexContainerProperties boundConfigValue;


    public EcxContainerConfigPanel(ConfigurationPropertyManagerService configurationPropertyManagerService,
                                   SpringBeanValidationBinderFactory springBeanValidationBinderFactory,
                                   EcxContainerConfigForm ecxContainerConfigForm) {
        this.configurationPropertyManagerService = configurationPropertyManagerService;
        this.springBeanValidationBinderFactory = springBeanValidationBinderFactory;
        this.ecxContainerConfigForm = ecxContainerConfigForm;

        this.errorField = new Label("");

        initUI();
    }

    private void initUI() {

        VerticalLayout configDiv = new VerticalLayout();

        Button saveChanges = new Button("Save Changes");
        saveChanges.addClickListener(this::saveChangesButtonClicked);
        configDiv.add(saveChanges);

        Button reset = new Button("Reset Changes");
        reset.addClickListener(this::resetButtonClicked);
        configDiv.add(reset);

        configDiv.add(errorField);

        Class<DCEcodexContainerProperties> configurationClazz = DCEcodexContainerProperties.class;

        binder = springBeanValidationBinderFactory.create(configurationClazz);
        binder.setStatusLabel(errorField);

        ecxContainerConfigForm.bindInstanceFields(binder);
        configDiv.add(ecxContainerConfigForm);

        add(configDiv);
    }

    private void resetButtonClicked(ClickEvent<Button> buttonClickEvent) {
        DCEcodexContainerProperties currentConfig = readConfigFromPropertyService();
        binder.readBean(currentConfig); //reset config
    }

    private DCEcodexContainerProperties readConfigFromPropertyService() {
        return configurationPropertyManagerService.loadConfiguration(
                DomibusConnectorBusinessDomain.getDefaultMessageLaneId(),
                DCEcodexContainerProperties.class);
    }

    private void saveChangesButtonClicked(ClickEvent<Button> buttonClickEvent) {

        BinderValidationStatus<DCEcodexContainerProperties> validate = binder.validate();
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
