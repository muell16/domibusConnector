package eu.ecodex.connector.gwc._1_1;

import java.net.ConnectException;

import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceException;

import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;

import backend.ecodex.org._1_1.BackendInterface;
import backend.ecodex.org._1_1.DownloadMessageFault;
import backend.ecodex.org._1_1.DownloadMessageRequest;
import backend.ecodex.org._1_1.DownloadMessageResponse;
import backend.ecodex.org._1_1.ListPendingMessagesResponse;
import backend.ecodex.org._1_1.SendRequest;
import backend.ecodex.org._1_1.SendResponse;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;
import eu.ecodex.connector.gwc.util.CommonMessageHelper;

public class GatewayWebserviceClient {

    org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GatewayWebserviceClient.class);

    private BackendInterface gatewayBackendWebservice;
    private DownloadMessageHelper downloadMessageHelper;
    private SendMessageHelper sendMessageHelper;
    private CommonMessageHelper commonMessageHelper;

    public void setCommonMessageHelper(CommonMessageHelper commonMessageHelper) {
        this.commonMessageHelper = commonMessageHelper;
    }

    public void setGatewayBackendWebservice(BackendInterface gatewayBackendWebservice) {
        this.gatewayBackendWebservice = gatewayBackendWebservice;
    }

    public void setDownloadMessageHelper(DownloadMessageHelper downloadMessageHelper) {
        this.downloadMessageHelper = downloadMessageHelper;
    }

    public void setSendMessageHelper(SendMessageHelper sendMessageHelper) {
        this.sendMessageHelper = sendMessageHelper;
    }

    public void sendMessage(Message message) throws ECodexConnectorGatewayWebserviceClientException {
        SendRequest request = new SendRequest();
        Messaging ebMSHeaderInfo = new Messaging();
        try {
            sendMessageHelper.buildMessage(request, ebMSHeaderInfo, message);
        } catch (ECodexConnectorGatewayWebserviceClientException e) {
            throw e;
        }

        SendResponse response = null;
        try {
            response = gatewayBackendWebservice.sendMessage(request, ebMSHeaderInfo);
        } catch (Exception e) {
            LOGGER.error("sendMessage failed: ", e);
            throw new ECodexConnectorGatewayWebserviceClientException(e);
        }
        if (!commonMessageHelper.isMessageEvidence(message)) {

            sendMessageHelper.extractEbmsMessageIdAndPersistIntoDB(response, message);
        }
    }

    public String[] listPendingMessages() throws ECodexConnectorGatewayWebserviceClientException {
        LOGGER.debug("started... ");
        try {
            ListPendingMessagesResponse response = gatewayBackendWebservice.listPendingMessages(commonMessageHelper
                    .createEmptyListPendingMessagesRequest());

            LOGGER.debug(response.getMessageID().toString());
            return response.getMessageID().toArray(new String[response.getMessageID().size()]);
        } catch (Exception e) {
            if (e instanceof WebServiceException) {
                if (e.getCause() instanceof ConnectException) {
                    throw new ECodexConnectorGatewayWebserviceClientException(
                            "The corresponding gateway cannot be reached!");
                }
            }
            throw new ECodexConnectorGatewayWebserviceClientException("Could not execute! ", e);
        }

    }

    public Message downloadMessage(String messageId) throws ECodexConnectorGatewayWebserviceClientException {

        Holder<DownloadMessageResponse> response = new Holder<DownloadMessageResponse>();
        Holder<Messaging> ebMSHeader = new Holder<Messaging>();

        DownloadMessageRequest request = new DownloadMessageRequest();
        request.setMessageID(messageId);

        try {
            gatewayBackendWebservice.downloadMessage(request, response, ebMSHeader);
            LOGGER.debug("Successfully downloaded message with id [{}]", request.getMessageID());
        } catch (DownloadMessageFault e) {
            LOGGER.error("Could not execute! ", e);
        }

        if (response.value == null || response.value.getPayload() == null || response.value.getPayload().isEmpty()) {
            LOGGER.info("Message {} contains no payload!", request.getMessageID());
            throw new ECodexConnectorGatewayWebserviceClientException("Message " + request.getMessageID()
                    + " contains no payload!");
        }

        Message message = downloadMessageHelper.convertDownloadIntoMessage(response, ebMSHeader);

        return message;
    }
}
