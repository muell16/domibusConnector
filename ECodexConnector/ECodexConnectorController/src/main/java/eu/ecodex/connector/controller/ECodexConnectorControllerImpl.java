package eu.ecodex.connector.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.common.ECodexConnectorProperties;
import eu.ecodex.connector.common.EncryptedDocumentPackage;
import eu.ecodex.connector.common.exception.ImplementationMissingException;
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

        EncryptedDocumentPackage[] messages = null;
        try {
            messages = nationalBackendClient.requestAllPendingMessages();
        } catch (ECodexConnectorNationalBackendClientException e1) {
            e1.printStackTrace();
        } catch (ImplementationMissingException e) {
            e.printStackTrace();
        }

        if (messages != null && messages.length > 0) {

            for (EncryptedDocumentPackage message : messages) {

                byte[] content = null;

                if (connectorProperties.isUseContentMapper()) {
                    try {
                        content = contentMapper.mapNationalToInternational(message.getContent());
                    } catch (ECodexConnectorContentMapperException e) {
                        e.printStackTrace();
                    } catch (ImplementationMissingException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    gatewayWebserviceClient.sendMessage(content);
                } catch (ECodexConnectorGatewayWebserviceClientException e) {
                    e.printStackTrace();
                }
            }
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
