package eu.domibus.connector.gwc;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceException;

import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import backend.ecodex.org._1_1.BackendInterface;
import backend.ecodex.org._1_1.DownloadMessageFault;
import backend.ecodex.org._1_1.DownloadMessageRequest;
import backend.ecodex.org._1_1.DownloadMessageResponse;
import backend.ecodex.org._1_1.ErrorLogEntry;
import backend.ecodex.org._1_1.ErrorLogEntryArray;
import backend.ecodex.org._1_1.GetErrorsRequest;
import backend.ecodex.org._1_1.GetStatusRequest;
import backend.ecodex.org._1_1.ListPendingMessagesResponse;
import backend.ecodex.org._1_1.MessageStatus;
import backend.ecodex.org._1_1.SendRequest;
import backend.ecodex.org._1_1.SendResponse;
import eu.domibus.connector.common.gwc.DomibusConnectorGatewayWebserviceClient;
import eu.domibus.connector.common.gwc.DomibusConnectorGatewayWebserviceClientException;
import eu.domibus.connector.domain.Message;

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

//    @Override
//    public String[] listPendingMessages() throws DomibusConnectorGatewayWebserviceClientException {
//    	List<String> pendingMessageIds = null;
//    	
//    	pendingMessageIds = listPendingMessagesInternal(pendingMessageIds);
//        return pendingMessageIds.toArray(new String[pendingMessageIds.size()]);
//    }
//
//
//    @Override
//    public Message downloadMessage(String messageId) throws DomibusConnectorGatewayWebserviceClientException {
//
//        Message message = downloadMessageInternal(messageId);
//
//        return message;
//    }

    @Override
    public void getMessageErrorsFromGateway(Message message) throws DomibusConnectorGatewayWebserviceClientException {

        GetErrorsRequest getErrorsRequest = new GetErrorsRequest();
        getErrorsRequest.setMessageID(message.getMessageDetails().getEbmsMessageId());
        ErrorLogEntryArray messageErrors = null;

        try {
            messageErrors = gatewayBackendWebservice.getMessageErrors(getErrorsRequest);
        } catch (Exception e) {
            LOGGER.error("getMessageErrorsFromGateway failed: ", e);
            throw new DomibusConnectorGatewayWebserviceClientException(e);
        }

        if (messageErrors != null && !CollectionUtils.isEmpty(messageErrors.getItem())) {
            String source = null;
            try {
                source = ClassUtils
                        .getQualifiedMethodName(this.getClass().getMethod("getMessageErrorsFromGateway", Message.class));
            } catch (Exception e) {
                LOGGER.error("Exception trying to get Method name.", e);
                source = "DomibusConnectorGatewayWebserviceClientImpl";
            }

            for (ErrorLogEntry messageError : messageErrors.getItem()) {
                commonMessageHelper.persistMessageError(message, CommonMessageHelper.DOMIBUS_MESSAGE_ERROR,
                        messageError.getErrorDetail(), source);
            }
        }
    }

    @Override
    public String getMessageStatusOnGateway(Message message) throws DomibusConnectorGatewayWebserviceClientException {

        GetStatusRequest getStatusRequest = new GetStatusRequest();
        getStatusRequest.setMessageID(message.getMessageDetails().getEbmsMessageId());
        try {
            MessageStatus messageStatus = gatewayBackendWebservice.getMessageStatus(getStatusRequest);
            if (messageStatus != null) {
                return messageStatus.value();
            }
        } catch (Exception e) {
            LOGGER.error("getMessageStatusOnGateway failed: ", e);
            throw new DomibusConnectorGatewayWebserviceClientException(e);
        }

        return null;
    }

	@Override
	public Collection<Message> requestPendingMessages() throws DomibusConnectorGatewayWebserviceClientException {
		LOGGER.debug("started... ");
		List<String> pendingMessageIds = null;
		pendingMessageIds = listPendingMessagesInternal(pendingMessageIds);
        
        Collection<Message> pendingMessages = new ArrayList<Message>();
        if(!CollectionUtils.isEmpty(pendingMessageIds)){
        	LOGGER.debug("Received {} messageIds by listPendingMessages.", pendingMessageIds.size());
        	for(String messageId:pendingMessageIds){
        		LOGGER.debug("Trying to download message with ID {}", messageId);
        		Message message = downloadMessageInternal(messageId);
                pendingMessages.add(message);
                LOGGER.debug("Added Message with ID {} to collection.", messageId);
        	}
        	return pendingMessages;
        }
		return null;
	}

	private Message downloadMessageInternal(String messageId) throws DomibusConnectorGatewayWebserviceClientException {
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

		//if (LOGGER.isDebugEnabled()) {
			try {
				String headerString = commonMessageHelper
						.printXML(ebMSHeader.value, UserMessage.class, Messaging.class);
				LOGGER.debug(headerString);
			} catch (JAXBException e1) {
				LOGGER.error(e1.getMessage());
			} catch (IOException e1) {
				LOGGER.error(e1.getMessage());
			}
		//}

		if (response.value == null 
				//|| response.value.getBodyload() == null
				) {
		    LOGGER.info("Message {} contains no payload!", request.getMessageID());
		    throw new DomibusConnectorGatewayWebserviceClientException("Message " + request.getMessageID()
		            + " contains no bodyload!");
		}

		Message message = downloadMessageHelper.convertDownloadIntoMessage(response, ebMSHeader);
		return message;
	}

	private List<String> listPendingMessagesInternal(List<String> pendingMessageIds)
			throws DomibusConnectorGatewayWebserviceClientException {
		LOGGER.debug("started... ");
		try {
			ListPendingMessagesResponse response = gatewayBackendWebservice.listPendingMessages(commonMessageHelper
					.createEmptyListPendingMessagesRequest());
			
			LOGGER.debug(response.getMessageID().toString());
			pendingMessageIds = response.getMessageID();
		} catch (Exception e) {
			if (e instanceof WebServiceException) {
				if (e.getCause() instanceof ConnectException) {
					throw new DomibusConnectorGatewayWebserviceClientException(
							"The corresponding gateway cannot be reached!");
				}
			}
			throw new DomibusConnectorGatewayWebserviceClientException("Could not execute! ", e);
		}
		return pendingMessageIds;
	}
}
