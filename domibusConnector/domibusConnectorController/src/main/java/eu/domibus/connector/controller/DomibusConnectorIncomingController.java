package eu.domibus.connector.controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.domibus.connector.common.gwc.DomibusConnectorGatewayWebserviceClient;
import eu.domibus.connector.common.gwc.DomibusConnectorGatewayWebserviceClientException;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.controller.service.EvidenceService;
import eu.domibus.connector.controller.service.MessageService;
import eu.domibus.connector.domain.Message;

public class DomibusConnectorIncomingController implements DomibusConnectorController {

    static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorIncomingController.class);

    private DomibusConnectorGatewayWebserviceClient gatewayWebserviceClient;
    private MessageService incomingMessageService;
    private EvidenceService incomingEvidenceService;

    public void setGatewayWebserviceClient(DomibusConnectorGatewayWebserviceClient gatewayWebserviceClient) {
        this.gatewayWebserviceClient = gatewayWebserviceClient;
    }

    public void setIncomingMessageService(MessageService incomingMessageService) {
        this.incomingMessageService = incomingMessageService;
    }

    public void setIncomingEvidenceService(EvidenceService incomingEvidenceService) {
        this.incomingEvidenceService = incomingEvidenceService;
    }

    @Override
    public void execute() throws DomibusConnectorControllerException {
        LOGGER.debug("Job for handling incoming messages triggered.");
        Date start = new Date();

        Collection<Message> messages = null;
        try {
            messages = gatewayWebserviceClient.requestPendingMessages();
        } catch (DomibusConnectorGatewayWebserviceClientException e) {
            throw new DomibusConnectorControllerException(e);
        }

        if (!CollectionUtils.isEmpty(messages)) {
            LOGGER.info("Found {} incoming messages on gateway to handle...", messages.size());
            for (Message message : messages) {
                try {
                    handleMessage(message);
                } catch (DomibusConnectorControllerException e) {
                    LOGGER.error("Error handling message with id " + message.getMessageDetails().getEbmsMessageId(), e);
                }
            }
        } else {
            LOGGER.debug("No pending messages on gateway!");
        }

        LOGGER.debug("Job for handling incoming messages finished in {} ms.",
                (System.currentTimeMillis() - start.getTime()));
    }

    private void handleMessage(Message message) throws DomibusConnectorControllerException {
        

        if (isMessageEvidence(message)) {
            try {
                incomingEvidenceService.handleEvidence(message);
            } catch (DomibusConnectorMessageException | DomibusConnectorControllerException e) {
                LOGGER.error("Error handling message with id " + message.getMessageDetails().getEbmsMessageId(), e);
            }
        } else {
            try {
                incomingMessageService.handleMessage(message);
            } catch (DomibusConnectorControllerException | DomibusConnectorMessageException e) {
                LOGGER.error("Error handling message with id " + message.getMessageDetails().getEbmsMessageId(), e);
            }
        }
    }

    private boolean isMessageEvidence(Message message) {
        return message.getMessageDetails().getAction().getAction().equals("RelayREMMDAcceptanceRejection")
                || message.getMessageDetails().getAction().getAction().equals("DeliveryNonDeliveryToRecipient")
                || message.getMessageDetails().getAction().getAction().equals("RetrievalNonRetrievalToRecipient");
    }

}
