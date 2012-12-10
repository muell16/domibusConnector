package eu.ecodex.connector.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.common.exception.ImplementationMissingException;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.controller.exception.ECodexConnectorControllerException;
import eu.ecodex.connector.controller.message.MessageService;
import eu.ecodex.connector.nbc.ECodexConnectorNationalBackendClient;
import eu.ecodex.connector.nbc.exception.ECodexConnectorNationalBackendClientException;

public class ECodexConnectorOutgoingController implements ECodexConnectorController {

    static Logger LOGGER = LoggerFactory.getLogger(ECodexConnectorOutgoingController.class);

    private ECodexConnectorNationalBackendClient nationalBackendClient;
    private MessageService outgoingMessageService;

    public void setNationalBackendClient(ECodexConnectorNationalBackendClient nationalBackendClient) {
        this.nationalBackendClient = nationalBackendClient;
    }

    public void setOutgoingMessageService(MessageService outgoingMessageService) {
        this.outgoingMessageService = outgoingMessageService;
    }

    @Override
    public void handleMessages() throws ECodexConnectorControllerException {
        LOGGER.info("Started to handle outgoing messages from national system to gateway!");

        String[] messages = null;
        try {
            messages = nationalBackendClient.requestMessagesUnsent();
        } catch (ECodexConnectorNationalBackendClientException nbce) {
            throw new ECodexConnectorControllerException(
                    "Exception while trying to get messages list from national system. ", nbce);
        } catch (ImplementationMissingException ime) {
            throw new ECodexConnectorControllerException(
                    "Exception while trying to get messages list from national system. ", ime);
        }

        if (messages != null && messages.length > 0) {

            for (String messageId : messages) {

                try {
                    outgoingMessageService.handleMessage(messageId);
                } catch (ECodexConnectorControllerException ce) {
                    LOGGER.error(ce.getMessage());
                    throw ce;
                }
            }
        } else {
            LOGGER.info("There are no unsent outgoing messages on national system!");
        }
    }

    @Override
    public void handleEvidences() throws ECodexConnectorControllerException {
        LOGGER.info("Started to check national implementation for pending confirmations!");

        MessageConfirmation[] confirmations = null;
        try {
            confirmations = nationalBackendClient.requestConfirmations();
        } catch (ECodexConnectorNationalBackendClientException e) {
            throw new ECodexConnectorControllerException(
                    "Exception while trying to get confirmations from national system. ", e);
        } catch (ImplementationMissingException e) {
            throw new ECodexConnectorControllerException(
                    "Exception while trying to get confirmations from national system. ", e);
        }

        if (confirmations != null && confirmations.length > 0) {

            for (MessageConfirmation confirmation : confirmations) {

                try {
                    handleConfirmation(confirmation);
                } catch (ECodexConnectorControllerException ce) {
                    LOGGER.error(ce.getMessage());
                    throw ce;
                }
            }
        }
    }

    private void handleConfirmation(MessageConfirmation confirmation) throws ECodexConnectorControllerException {
        // TODO Auto-generated method stub

    }

}
