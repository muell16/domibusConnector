package eu.domibus.connector.ui.view.areas.tools.ecxcontainer;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.common.service.CurrentBusinessDomain;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageDetailsBuilder;
import eu.domibus.connector.security.container.service.ECodexContainerFactoryService;
import eu.domibus.connector.ui.utils.RoleRequired;
import eu.domibus.connector.ui.view.areas.configuration.TabMetadata;
import eu.domibus.connector.ui.view.areas.tools.ToolsLayout;
import eu.ecodex.dss.model.BusinessContent;
import eu.ecodex.dss.model.ECodexContainer;
import eu.ecodex.dss.service.ECodexContainerService;
import eu.ecodex.dss.service.ECodexException;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


@Component
@UIScope
@Route(value = ECodexContainerCreatorView.ROUTE, layout = ToolsLayout.class)
@RoleRequired(role = "ADMIN" )
@TabMetadata(title = "Create ECodex Container", tabGroup = ToolsLayout.TAB_GROUP_NAME)
public class ECodexContainerCreatorView extends VerticalLayout {

    private static final Logger LOGGER = LogManager.getLogger(ECodexContainerCreatorView.class);

    public static final String ROUTE = "createEcodexContainer";

    private final ECodexContainerFactoryService eCodexContainerFactoryService;


    public ECodexContainerCreatorView(ECodexContainerFactoryService eCodexContainerFactoryService) {
        this.eCodexContainerFactoryService = eCodexContainerFactoryService;
        this.initUI();
    }

    private void initUI() {

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


                DomibusConnectorMessage theMessage = DomibusConnectorMessageBuilder.createBuilder()
                        .setMessageDetails(DomibusConnectorMessageDetailsBuilder.create()
                                .withOriginalSender("TheOriginalSender")
                                .build())
                        .build();

                ECodexContainerService eCodexContainerService = eCodexContainerFactoryService.createECodexContainerService(theMessage);
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
                LOGGER.warn("Ecodex Exception occured while testing in UI", e);
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

}
