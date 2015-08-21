package eu.domibus.connector.gwc;

import java.io.IOException;
import java.net.ConnectException;

import javax.xml.bind.JAXBException;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceException;

import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;

import backend.ecodex.org._1_1.BackendInterface;
import backend.ecodex.org._1_1.DownloadMessageFault;
import backend.ecodex.org._1_1.DownloadMessageRequest;
import backend.ecodex.org._1_1.DownloadMessageResponse;
import backend.ecodex.org._1_1.ListPendingMessagesResponse;
import backend.ecodex.org._1_1.SendRequest;
import backend.ecodex.org._1_1.SendResponse;
import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.gwc.exception.DomibusConnectorGatewayWebserviceClientException;
import eu.domibus.connector.gwc.helper.DownloadMessageHelper;
import eu.domibus.connector.gwc.helper.SendMessageHelper;
import eu.domibus.connector.gwc.util.CommonMessageHelper;

public class DomibusConnectorGatewayWebserviceClientImpl implements DomibusConnectorGatewayWebserviceClient {

    org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DomibusConnectorGatewayWebserviceClientImpl.class);

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

    @Override
    public void sendMessage(Message message) throws DomibusConnectorGatewayWebserviceClientException {
        SendRequest request = new SendRequest();
        Messaging ebMSHeaderInfo = new Messaging();
        try {
            sendMessageHelper.buildMessage(request, ebMSHeaderInfo, message);
        } catch (DomibusConnectorGatewayWebserviceClientException e) {
            throw e;
        }

        if (LOGGER.isDebugEnabled()) {
            try {
                String headerString = commonMessageHelper.printXML(ebMSHeaderInfo, UserMessage.class, Messaging.class);
                LOGGER.debug(headerString);
            } catch (JAXBException e1) {
                LOGGER.error(e1.getMessage());
            } catch (IOException e1) {
                LOGGER.error(e1.getMessage());
            }

            LOGGER.debug("--PARTY-- " + "\nFrom PartyId "
                    + ebMSHeaderInfo.getUserMessage().getPartyInfo().getFrom().getPartyId().get(0).getValue()
                    + "\nFrom PartyIdType "
                    + ebMSHeaderInfo.getUserMessage().getPartyInfo().getFrom().getPartyId().get(0).getType()
                    + "\nTo PartyId "
                    + ebMSHeaderInfo.getUserMessage().getPartyInfo().getTo().getPartyId().get(0).getValue()
                    + "\nTo PartyIdType "
                    + ebMSHeaderInfo.getUserMessage().getPartyInfo().getTo().getPartyId().get(0).getType());
        }

        SendResponse response = null;
        try {
            response = gatewayBackendWebservice.sendMessage(request, ebMSHeaderInfo);
        } catch (Exception e) {
            LOGGER.error("sendMessage failed: ", e);
            throw new DomibusConnectorGatewayWebserviceClientException(e);
        }
        if (!commonMessageHelper.isMessageEvidence(message)) {

            sendMessageHelper.extractEbmsMessageIdAndPersistIntoDB(response, message);
        }
    }

    @Override
    public String[] listPendingMessages() throws DomibusConnectorGatewayWebserviceClientException {
        LOGGER.debug("started... ");
        try {
            ListPendingMessagesResponse response = gatewayBackendWebservice.listPendingMessages(commonMessageHelper
                    .createEmptyListPendingMessagesRequest());

            LOGGER.debug(response.getMessageID().toString());
            return response.getMessageID().toArray(new String[response.getMessageID().size()]);
        } catch (Exception e) {
            if (e instanceof WebServiceException) {
                if (e.getCause() instanceof ConnectException) {
                    throw new DomibusConnectorGatewayWebserviceClientException(
                            "The corresponding gateway cannot be reached!");
                }
            }
            throw new DomibusConnectorGatewayWebserviceClientException("Could not execute! ", e);
        }
    }

    @Override
    public Message downloadMessage(String messageId) throws DomibusConnectorGatewayWebserviceClientException {

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

        if (response.value == null || response.value.getBodyload() == null) {
            LOGGER.info("Message {} contains no payload!", request.getMessageID());
            throw new DomibusConnectorGatewayWebserviceClientException("Message " + request.getMessageID()
                    + " contains no bodyload!");
        }

        if (LOGGER.isDebugEnabled()) {
            try {
                String headerString = commonMessageHelper
                        .printXML(ebMSHeader.value, UserMessage.class, Messaging.class);
                LOGGER.debug(headerString);
            } catch (JAXBException e1) {
                LOGGER.error(e1.getMessage());
            } catch (IOException e1) {
                LOGGER.error(e1.getMessage());
            }
        }

        Message message = downloadMessageHelper.convertDownloadIntoMessage(response, ebMSHeader);

        return message;
    }

}
