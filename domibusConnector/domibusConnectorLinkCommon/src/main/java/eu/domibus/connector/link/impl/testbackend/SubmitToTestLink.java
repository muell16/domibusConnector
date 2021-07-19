package eu.domibus.connector.link.impl.testbackend;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.controller.service.TransportStateService;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageConfirmationBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageDetailsBuilder;
import eu.domibus.connector.link.service.SubmitToLinkPartner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static eu.domibus.connector.link.service.DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME;

@Profile("plugin-" + TestbackendPlugin.IMPL_NAME)
@Component
public class SubmitToTestLink implements SubmitToLinkPartner {

    private static final Logger LOGGER = LogManager.getLogger(SubmitToTestLink.class);

    private final SubmitToConnector submitToConnector;
    private final TransportStateService transportStateService;
    private final DomibusConnectorMessageIdGenerator messageIdGenerator;

    private boolean enabled;
    private DomibusConnectorLinkPartner linkPartner;

    public SubmitToTestLink(SubmitToConnector submitToConnector,
                            TransportStateService transportStateService,
                            DomibusConnectorMessageIdGenerator messageIdGenerator) {
        this.submitToConnector = submitToConnector;
        this.transportStateService = transportStateService;
        this.messageIdGenerator = messageIdGenerator;
    }

    @Override
    public void submitToLink(DomibusConnectorMessage message, DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) throws DomibusConnectorSubmitToLinkException {

        if (this.enabled) {
            String ebmsMessageId = message.getMessageDetails().getEbmsMessageId();

            DomibusConnectorMessage deliveryConfirmation = DomibusConnectorMessageBuilder.createBuilder()
                    .setMessageDetails(
                            DomibusConnectorMessageDetailsBuilder.create()
                                    .withRefToMessageId(ebmsMessageId)                                              //set ref to message id to ebms id
                                    .build()
                    )
                    .setConnectorMessageId(messageIdGenerator.generateDomibusConnectorMessageId())
                    .setMessageLaneId(message.getMessageLaneId())
                    .addTransportedConfirmations(DomibusConnectorMessageConfirmationBuilder.createBuilder()     //append evidence trigger of type DELIVERY
                            .setEvidenceType(DomibusConnectorEvidenceType.DELIVERY)
                            .setEvidence(new byte[0])
                            .build())
                    .build();

            submitToConnector.submitToConnector(deliveryConfirmation, linkPartnerName, LinkType.BACKEND);       //submit trigger message to connector
            TransportStateService.TransportId transportFor = transportStateService.createTransportFor(message, linkPartnerName);
            TransportStateService.DomibusConnectorTransportState state = new TransportStateService.DomibusConnectorTransportState();
            state.setConnectorTransportId(transportFor);
            state.setLinkPartner(linkPartner);
            state.setRemoteMessageId("Testbackend_" + LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            state.setStatus(TransportState.ACCEPTED);
            transportStateService.updateTransportStatus(state);
            LOGGER.info("Generated Delivery evidence trigger message for connector test message with EBMS ID [{}]", message.getConnectorMessageId());
        } else {
            LOGGER.warn("Test message received, but test backend is not enabled! No response will be sent!");
        }


    }

    public void setDomibusConnectorLinkPartner(DomibusConnectorLinkPartner linkPartner) {
        this.linkPartner = linkPartner;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
