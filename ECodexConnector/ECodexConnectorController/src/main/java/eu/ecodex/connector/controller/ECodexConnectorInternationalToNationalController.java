package eu.ecodex.connector.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.controller.exception.ECodexConnectorControllerException;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;

public class ECodexConnectorInternationalToNationalController extends ECodexConnectorControllerImpl {

    static Logger LOGGER = LoggerFactory.getLogger(ECodexConnectorInternationalToNationalController.class);

    @Override
    public void handleMessages() throws ECodexConnectorControllerException {
        LOGGER.debug("Started handle gateway Messages!");

        try {
            String[] messageIDs = gatewayWebserviceClient.listPendingMessages();
            for (String messageId : messageIDs) {
                gatewayWebserviceClient.downloadMessage(messageId);
            }
        } catch (ECodexConnectorGatewayWebserviceClientException e) {
            e.printStackTrace();
        }
    }

}
