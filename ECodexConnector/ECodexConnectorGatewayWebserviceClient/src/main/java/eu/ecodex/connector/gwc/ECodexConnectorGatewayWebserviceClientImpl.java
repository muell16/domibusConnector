package eu.ecodex.connector.gwc;

import java.net.ConnectException;

import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceException;

import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;

import backend.ecodex.org._1_0.BackendInterface;
import backend.ecodex.org._1_0.DownloadMessageFault;
import backend.ecodex.org._1_0.DownloadMessageRequest;
import backend.ecodex.org._1_0.DownloadMessageResponse;
import backend.ecodex.org._1_0.ListPendingMessagesResponse;
import backend.ecodex.org._1_0.SendRequest;
import backend.ecodex.org._1_0.SendResponse;

import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;

public class ECodexConnectorGatewayWebserviceClientImpl implements ECodexConnectorGatewayWebserviceClient {

    org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ECodexConnectorGatewayWebserviceClientImpl.class);

    private static final String VERSION_1_0 = "1.0";
    private static final String VERSION_1_1 = "1.1";

    private eu.ecodex.connector.gwc._1_0.GatewayWebserviceClient gatewayWebserviceClient_1_0;
    private eu.ecodex.connector.gwc._1_1.GatewayWebserviceClient gatewayWebserviceClient_1_1;
    private String gatewayBackendWebserviceVersion;

    public void setGatewayWebserviceClient_1_0(
            eu.ecodex.connector.gwc._1_0.GatewayWebserviceClient gatewayWebserviceClient_1_0) {
        this.gatewayWebserviceClient_1_0 = gatewayWebserviceClient_1_0;
    }

    public void setGatewayWebserviceClient_1_1(
            eu.ecodex.connector.gwc._1_1.GatewayWebserviceClient gatewayWebserviceClient_1_1) {
        this.gatewayWebserviceClient_1_1 = gatewayWebserviceClient_1_1;
    }

    public void setGatewayBackendWebserviceVersion(String gatewayBackendWebserviceVersion) {
        this.gatewayBackendWebserviceVersion = gatewayBackendWebserviceVersion;
    }

    @Override
    public void sendMessageWithReference(Message message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendMessage(Message message) throws ECodexConnectorGatewayWebserviceClientException {
         if (gatewayBackendWebserviceVersion.equals(VERSION_1_0)) {
            gatewayWebserviceClient_1_0.sendMessage(message);
        } else if (gatewayBackendWebserviceVersion.equals(VERSION_1_1)) {
            gatewayWebserviceClient_1_1.sendMessage(message);
        } else {
            throw new ECodexConnectorGatewayWebserviceClientException(
                    "Unknown or invalid version entry for gateway.backend.webservice.version: "
                            + gatewayBackendWebserviceVersion);
        }
    }

    @Override
    public String[] listPendingMessages() throws ECodexConnectorGatewayWebserviceClientException {
        String[] messageIds = null;
        if (gatewayBackendWebserviceVersion.equals(VERSION_1_0)) {
            messageIds = gatewayWebserviceClient_1_0.listPendingMessages();
        } else if (gatewayBackendWebserviceVersion.equals(VERSION_1_1)) {
            messageIds = gatewayWebserviceClient_1_1.listPendingMessages();
        } else {
            throw new ECodexConnectorGatewayWebserviceClientException(
                    "Unknown or invalid version entry for gateway.backend.webservice.version: "
                            + gatewayBackendWebserviceVersion);
        }
        return messageIds;
    }

    @Override
    public Message downloadMessage(String messageId) throws ECodexConnectorGatewayWebserviceClientException {
        Message message = null;
        if (gatewayBackendWebserviceVersion.equals(VERSION_1_0)) {
            message = gatewayWebserviceClient_1_0.downloadMessage(messageId);
        } else if (gatewayBackendWebserviceVersion.equals(VERSION_1_1)) {
            message = gatewayWebserviceClient_1_1.downloadMessage(messageId);
        } else {
            throw new ECodexConnectorGatewayWebserviceClientException(
                    "Unknown or invalid version entry for gateway.backend.webservice.version: "
                            + gatewayBackendWebserviceVersion);
        }
        return message;
    }

}
