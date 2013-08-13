package eu.ecodex.connector.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.controller.exception.ECodexConnectorControllerException;
import eu.ecodex.connector.controller.message.EvidenceService;
import eu.ecodex.connector.controller.message.MessageService;
import eu.ecodex.connector.gwc.ECodexConnectorGatewayWebserviceClient;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;

public class ECodexConnectorIncomingController implements ECodexConnectorController {

    static Logger LOGGER = LoggerFactory.getLogger(ECodexConnectorIncomingController.class);

    private ECodexConnectorGatewayWebserviceClient gatewayWebserviceClient;
    private MessageService incomingMessageService;
    private EvidenceService incomingEvidenceService;

    public void setGatewayWebserviceClient(ECodexConnectorGatewayWebserviceClient gatewayWebserviceClient) {
        this.gatewayWebserviceClient = gatewayWebserviceClient;
    }

    public void setIncomingMessageService(MessageService incomingMessageService) {
        this.incomingMessageService = incomingMessageService;
    }

    public void setIncomingEvidenceService(EvidenceService incomingEvidenceService) {
        this.incomingEvidenceService = incomingEvidenceService;
    }

    @Override
    public void handleMessages() throws ECodexConnectorControllerException {
        LOGGER.debug("Started handle gateway Messages!");

        String[] messageIDs = null;
        try {
            messageIDs = gatewayWebserviceClient.listPendingMessages();
        } catch (ECodexConnectorGatewayWebserviceClientException e) {
            throw new ECodexConnectorControllerException(e);
        }

        if (messageIDs != null && messageIDs.length > 0) {
            LOGGER.info("Found {} incoming messages on gateway to handle...", messageIDs.length);
            for (String messageId : messageIDs) {
                try {
                    handleMessage(messageId);
                } catch (ECodexConnectorControllerException e) {
                    LOGGER.error("Error handling message with id " + messageId, e);
                }
            }
        } else {
            LOGGER.debug("No pending messages on gateway!");
        }
    }

    private void handleMessage(String messageId) throws ECodexConnectorControllerException {
        Message message = null;
        try {
            message = gatewayWebserviceClient.downloadMessage(messageId);
        } catch (ECodexConnectorGatewayWebserviceClientException e) {
            throw new ECodexConnectorControllerException("Error downloading message from the gateway!", e);
        }

        if (isMessageEvidence(message)) {
            incomingEvidenceService.handleEvidence(message);
        } else {
            try {
                incomingMessageService.handleMessage(message);
            } catch (ECodexConnectorControllerException e) {
                LOGGER.error("Error handling message with id " + messageId, e);
            }
        }
    }

    @Override
    public void handleEvidences() throws ECodexConnectorControllerException {
        // TODO Auto-generated method stub

    }

    private boolean isMessageEvidence(Message message) {
        return message.getMessageDetails().getAction().getAction().equals("RelayREMMDAcceptanceRejection")
                || message.getMessageDetails().getAction().getAction().equals("DeliveryNonDeliveryToRecipient")
                || message.getMessageDetails().getAction().getAction().equals("RetrievalNonRetrievalToRecipient");
    }

}
