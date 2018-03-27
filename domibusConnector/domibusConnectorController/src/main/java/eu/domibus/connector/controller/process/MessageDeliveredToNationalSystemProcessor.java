package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.controller.service.DomibusConnectorGatewayDeliveryService;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.evidences.DomibusConnectorEvidencesToolkit;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Responsible for setting message as delivered to national system
 *  *) sets the correct state in persistence
 *  *) creates the correct evidences and sends them
 *
 */
@Component
public class MessageDeliveredToNationalSystemProcessor implements DomibusConnectorMessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDeliveredToNationalSystemProcessor.class);

    @Autowired
    private DomibusConnectorMessagePersistenceService messagePersistenceService;

    @Autowired
    private DomibusConnectorGatewaySubmissionService gwSubmissionService;

    @Autowired
    private DomibusConnectorBackendDeliveryService backendDeliveryService;

    @Autowired
    private CreateConfirmationMessageService confirmationMessageService;

    @Override
    public void processMessage(DomibusConnectorMessage message) {
        messagePersistenceService.setMessageDeliveredToNationalSystem(message);

        DomibusConnectorMessage deliveryConfirmationMessage = confirmationMessageService
                .createConfirmationMessageBuilder(message, DomibusConnectorEvidenceType.DELIVERY)
                .save()
                .build();

        try {
            gwSubmissionService.submitToGateway(deliveryConfirmationMessage);
        } catch (DomibusConnectorGatewaySubmissionException e) {
            LOGGER.error("Error while sending Evidence message [{}] to GW", deliveryConfirmationMessage);
        }

        backendDeliveryService.deliverMessageToBackend(deliveryConfirmationMessage);

    }

}
