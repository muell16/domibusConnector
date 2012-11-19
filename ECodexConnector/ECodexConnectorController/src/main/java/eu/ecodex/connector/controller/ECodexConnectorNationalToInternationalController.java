package eu.ecodex.connector.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.common.exception.ImplementationMissingException;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageContent;
import eu.ecodex.connector.common.message.MessageDetails;
import eu.ecodex.connector.controller.exception.ECodexConnectorControllerException;
import eu.ecodex.connector.evidences.exception.EvidencesToolkitException;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;
import eu.ecodex.connector.mapping.exception.ECodexConnectorContentMapperException;
import eu.ecodex.connector.nbc.exception.ECodexConnectorNationalBackendClientException;

public class ECodexConnectorNationalToInternationalController extends ECodexConnectorControllerImpl {

    static Logger LOGGER = LoggerFactory.getLogger(ECodexConnectorNationalToInternationalController.class);

    @Override
    public void handleMessages() throws ECodexConnectorControllerException {
        LOGGER.debug("Started to check national implementation for pending messages!");

        String[] messages = null;
        try {
            messages = nationalBackendClient.requestMessagesUnsent();
        } catch (ECodexConnectorNationalBackendClientException e1) {
            e1.printStackTrace();
        } catch (ImplementationMissingException e) {
            e.printStackTrace();
        }

        if (messages != null && messages.length > 0) {

            for (String messageId : messages) {

                handleNationalMessage(messageId);
            }
        }
    }

    private void handleNationalMessage(String messageId) throws ECodexConnectorControllerException {

        MessageDetails details = new MessageDetails();
        details.setNationalMessageId(messageId);

        MessageContent content = new MessageContent();

        Message message = new Message(details, content);

        try {
            nationalBackendClient.requestMessage(message);
        } catch (ECodexConnectorNationalBackendClientException e1) {
            e1.printStackTrace();
        } catch (ImplementationMissingException e1) {
            e1.printStackTrace();
        }

        if (connectorProperties.isUseContentMapper()) {
            try {
                byte[] xmlContent = contentMapper.mapNationalToInternational(message.getMessageContent()
                        .getXmlContent());
                message.getMessageContent().setXmlContent(xmlContent);
            } catch (ECodexConnectorContentMapperException e) {
                e.printStackTrace();
            } catch (ImplementationMissingException e) {
                e.printStackTrace();
            }
        }

        if (connectorProperties.isUseSecurityToolkit()) {
            // TODO: Integration of SecurityToolkit to build ASIC-S container
            // and TrustOKToken
            LOGGER.warn("SecurityToolkit not available yet! Must send message unsecure!");
        }

        if (connectorProperties.isUseEvidencesToolkit()) {
            try {
                evidencesToolit.createSubmissionAcceptance(message);
            } catch (EvidencesToolkitException e) {
                e.printStackTrace();
            }
        }

        try {
            gatewayWebserviceClient.sendMessage(message);
        } catch (ECodexConnectorGatewayWebserviceClientException e) {
            throw new ECodexConnectorControllerException("Could not send ECodex Message to Gateway! ", e);
        }
    }

}
