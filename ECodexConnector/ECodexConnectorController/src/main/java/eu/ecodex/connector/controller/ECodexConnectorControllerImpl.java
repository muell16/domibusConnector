package eu.ecodex.connector.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.common.ECodexConnectorProperties;
import eu.ecodex.connector.common.exception.ImplementationMissingException;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageContent;
import eu.ecodex.connector.common.message.MessageDetails;
import eu.ecodex.connector.controller.exception.ECodexConnectorControllerException;
import eu.ecodex.connector.gwc.ECodexConnectorGatewayWebserviceClient;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;
import eu.ecodex.connector.mapping.ECodexConnectorContentMapper;
import eu.ecodex.connector.mapping.exception.ECodexConnectorContentMapperException;
import eu.ecodex.connector.nbc.ECodexConnectorNationalBackendClient;
import eu.ecodex.connector.nbc.exception.ECodexConnectorNationalBackendClientException;

public class ECodexConnectorControllerImpl implements ECodexConnectorController {

    static Logger LOGGER = LoggerFactory.getLogger(ECodexConnectorControllerImpl.class);

    ECodexConnectorProperties connectorProperties;
    ECodexConnectorContentMapper contentMapper;
    ECodexConnectorNationalBackendClient nationalBackendClient;
    ECodexConnectorGatewayWebserviceClient gatewayWebserviceClient;

    public void setConnectorProperties(ECodexConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

    public void setContentMapper(ECodexConnectorContentMapper contentMapper) {
        this.contentMapper = contentMapper;
    }

    public void setNationalBackendClient(ECodexConnectorNationalBackendClient nationalBackendClient) {
        this.nationalBackendClient = nationalBackendClient;
    }

    public void setGatewayWebserviceClient(ECodexConnectorGatewayWebserviceClient gatewayWebserviceClient) {
        this.gatewayWebserviceClient = gatewayWebserviceClient;
    }

    @Override
    public void handleNationalMessages() throws ECodexConnectorControllerException {
        LOGGER.debug("Started handle national Messages!");

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
            } catch (ECodexConnectorContentMapperException e) {
                e.printStackTrace();
            } catch (ImplementationMissingException e) {
                e.printStackTrace();
            }
        }

        try {
            gatewayWebserviceClient.sendMessage(message);
        } catch (ECodexConnectorGatewayWebserviceClientException e) {
            throw new ECodexConnectorControllerException("Could not send ECodex Message to Gateway! ", e);
        }
    }

    @Override
    public void handleGatewayMessages() throws ECodexConnectorControllerException {
        LOGGER.debug("Started handle gateway Messages!");

        try {
            String[] messageIDs = gatewayWebserviceClient.listPendingMessages();
            for (String messageId : messageIDs) {
                gatewayWebserviceClient.downloadMessage(messageId);
            }
        } catch (ECodexConnectorGatewayWebserviceClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
