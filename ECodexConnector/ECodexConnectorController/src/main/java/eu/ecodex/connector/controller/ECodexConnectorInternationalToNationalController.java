package eu.ecodex.connector.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.common.exception.ImplementationMissingException;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.controller.exception.ECodexConnectorControllerException;
import eu.ecodex.connector.evidences.exception.EvidencesToolkitException;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;
import eu.ecodex.connector.mapping.exception.ECodexConnectorContentMapperException;
import eu.ecodex.connector.nbc.exception.ECodexConnectorNationalBackendClientException;

public class ECodexConnectorInternationalToNationalController extends ECodexConnectorControllerImpl {

    static Logger LOGGER = LoggerFactory.getLogger(ECodexConnectorInternationalToNationalController.class);

    @Override
    public void handleMessages() throws ECodexConnectorControllerException {
        LOGGER.info("Started handle gateway Messages!");

        String[] messageIDs = null;
        try {
            messageIDs = gatewayWebserviceClient.listPendingMessages();
        } catch (ECodexConnectorGatewayWebserviceClientException e) {
            throw new ECodexConnectorControllerException(e);
        }

        if (messageIDs != null && messageIDs.length > 0) {
            for (String messageId : messageIDs) {
                try {
                    handleGatewayMessage(messageId);
                } catch (ECodexConnectorControllerException e) {
                    LOGGER.error("Error handling message with id " + messageId, e);
                }
            }
        } else {
            LOGGER.info("No pending messages on gateway!");
        }
    }

    private void handleGatewayMessage(String messageId) throws ECodexConnectorControllerException {
        Message message = null;
        try {
            message = gatewayWebserviceClient.downloadMessage(messageId);
        } catch (ECodexConnectorGatewayWebserviceClientException e) {
            throw new ECodexConnectorControllerException("Error downloading message from the gateway!", e);
        }

        if (connectorProperties.isUseSecurityToolkit()) {
            // TODO: Integration of SecurityToolkit to unpack ASIC-S container
            // and TrustOKToken
            LOGGER.warn("SecurityToolkit not available yet! Must send message unsecure!");
        }

        if (connectorProperties.isUseEvidencesToolkit()) {
            try {
                evidencesToolkit.createRelayREMMDAcceptance(message);
            } catch (EvidencesToolkitException e) {
                throw new ECodexConnectorControllerException("Error creating Evidence for message!", e);
            }

            // TODO: MessageDetails building: From and To changed, Service and
            // Action other?
            Message evidenceMessage = new Message(message.getMessageDetails(), message.getConfirmations().get(
                    message.getConfirmations().size() - 1));
            try {
                gatewayWebserviceClient.sendMessage(evidenceMessage);
            } catch (ECodexConnectorGatewayWebserviceClientException e) {
                LOGGER.error("Exception sending RelayREMMD evidence back to sender gateway of message "
                        + message.getMessageDetails().getEbmsMessageId(), e);
            }
        }

        if (connectorProperties.isUseContentMapper()) {
            try {
                byte[] xmlContent = contentMapper.mapInternationalToNational(message.getMessageContent()
                        .getXmlContent());
                message.getMessageContent().setXmlContent(xmlContent);
            } catch (ECodexConnectorContentMapperException e) {
                throw new ECodexConnectorControllerException("Error mapping content of message into national format!",
                        e);
            } catch (ImplementationMissingException e) {
                throw new ECodexConnectorControllerException(e);
            }
        }

        try {
            nationalBackendClient.deliverMessage(message);
        } catch (ECodexConnectorNationalBackendClientException e) {
            throw new ECodexConnectorControllerException("Error delivering message to national backend client!", e);
        } catch (ImplementationMissingException e) {
            throw new ECodexConnectorControllerException(e);
        }
    }

    @Override
    public void handleEvidences() throws ECodexConnectorControllerException {
        // TODO Auto-generated method stub

    }

}
