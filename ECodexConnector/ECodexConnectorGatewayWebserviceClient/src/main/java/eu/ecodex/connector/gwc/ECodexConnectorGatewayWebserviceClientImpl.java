package eu.ecodex.connector.gwc;

import javax.xml.ws.Holder;

import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;

import backend.ecodex.org.BackendInterface;
import backend.ecodex.org.DownloadMessageFault;
import backend.ecodex.org.DownloadMessageRequest;
import backend.ecodex.org.DownloadMessageResponse;
import backend.ecodex.org.ListPendingMessagesFault;
import backend.ecodex.org.ListPendingMessagesResponse;
import backend.ecodex.org.SendRequest;
import backend.ecodex.org.SendResponse;
import eu.ecodex.connector.common.enums.ActionEnum;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;
import eu.ecodex.connector.gwc.helper.DownloadMessageHelper;
import eu.ecodex.connector.gwc.helper.SendMessageHelper;

public class ECodexConnectorGatewayWebserviceClientImpl implements ECodexConnectorGatewayWebserviceClient {

    org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ECodexConnectorGatewayWebserviceClientImpl.class);

    private BackendInterface gatewayBackendWebservice;
    private DownloadMessageHelper downloadMessageHelper;
    private SendMessageHelper sendMessageHelper;

    public void setGatewayBackendWebservice(BackendInterface gatewayBackendWebservice) {
        this.gatewayBackendWebservice = gatewayBackendWebservice;
    }

    public void setDownloadMessageHelper(DownloadMessageHelper downloadMessageHelper) {
        this.downloadMessageHelper = downloadMessageHelper;
    }

    public void setSendMessageHelper(SendMessageHelper sendMessageHelper) {
        this.sendMessageHelper = sendMessageHelper;
    }

    @Override
    public void sendMessageWithReference(Message message) {
        // TODO Auto-generated method stub

    }

    @Override
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
        if (!(message.getMessageDetails().getAction().equals(ActionEnum.RelayREMMDAcceptanceRejection)
                || message.getMessageDetails().getAction().equals(ActionEnum.DeliveryNonDeliveryToRecipient) || message
                .getMessageDetails().getAction().equals(ActionEnum.RetrievalNonRetrievalToRecipient))) {

            sendMessageHelper.extractEbmsMessageIdAndPersistIntoDB(response, message);
        }
    }

    @Override
    public String[] listPendingMessages() throws ECodexConnectorGatewayWebserviceClientException {
        LOGGER.debug("started... ");
        try {
            ListPendingMessagesResponse response = gatewayBackendWebservice.listPendingMessages(downloadMessageHelper
                    .createEmptyListPendingMessagesRequest());

            LOGGER.debug(response.getMessageID().toString());
            return response.getMessageID().toArray(new String[response.getMessageID().size()]);
        } catch (ListPendingMessagesFault e) {
            throw new ECodexConnectorGatewayWebserviceClientException("Could not execute! ", e);
        } catch (Exception e) {
            throw new ECodexConnectorGatewayWebserviceClientException("Could not execute! ", e);
        }

    }

    @Override
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
