package eu.domibus.connector.gpc;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import connector.domibus.eu.domibusconnectorgatewayservice._1.AcknowledgementType;
import connector.domibus.eu.domibusconnectorgatewayservice._1.MessageErrorLogEntriesType;
import connector.domibus.eu.domibusconnectorgatewayservice._1.MessageErrorLogEntryType;
import connector.domibus.eu.domibusconnectorgatewayservice._1.MessageType;
import connector.domibus.eu.domibusconnectorgatewayservice._1.MessagesType;
import connector.domibus.eu.domibusconnectorgatewayservice._1_0.DomibusConnectorGatewayServiceInterface;
import connector.domibus.eu.domibusconnectorgatewayservice._1_0.RequestPendingMessagesFault;
import connector.domibus.eu.domibusconnectorgatewayservice._1_0.SendMessageFault;
import eu.domibus.connector.common.db.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.common.gwc.DomibusConnectorGatewayWebserviceClient;
import eu.domibus.connector.common.gwc.DomibusConnectorGatewayWebserviceClientException;
import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.common.message.MessageError;
import eu.domibus.connector.gpc.transformer.MessageTransformer;

public class DomibusConnectorGatewayPluginClientImpl implements DomibusConnectorGatewayWebserviceClient {
	
	org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DomibusConnectorGatewayPluginClientImpl.class);
	
	public static final String DOMIBUS_MESSAGE_ERROR = "Error to message on domibus gateway side!";
	
	private DomibusConnectorGatewayServiceInterface gatewayService;
	
	@Autowired
	private MessageTransformer messageTransformer;
	
	@Autowired
	private DomibusConnectorPersistenceService persistenceService;

	@Override
	public void sendMessage(Message message) throws DomibusConnectorGatewayWebserviceClientException {
		MessageType sendMessageRequest = messageTransformer.transformFromDTO(message);
		
		AcknowledgementType ack = null;
		try {
			ack = gatewayService.sendMessage(sendMessageRequest);
			if(ack!=null){
				if(ack.isSuccess()){
					String ebmsId = ack.getMessageId();
					persistEbmsMessageIdIntoDatabase(ebmsId, message);
				}else{
					throw new DomibusConnectorGatewayWebserviceClientException("Sending message to gateway was not successful! ");
				}
			}
		} catch (SendMessageFault e) {
			throw new DomibusConnectorGatewayWebserviceClientException("Exception sending message! ", e);
		}

	}

	@Override
	public Collection<Message> requestPendingMessages() throws DomibusConnectorGatewayWebserviceClientException {
		
		MessagesType pendingMessages = null; 
				
		try {
			pendingMessages = gatewayService.requestPendingMessages(new String());
		} catch (RequestPendingMessagesFault e) {
			throw new DomibusConnectorGatewayWebserviceClientException("Exception calling requestPendingMessages! ", e);
		}
		
		if(pendingMessages!=null && !CollectionUtils.isEmpty(pendingMessages.getMessages())){
			Collection<Message> newMessages = new ArrayList<>();
			
			for(MessageType msg: pendingMessages.getMessages()){
				Message connMsg = null;
				try {
					connMsg = messageTransformer.transformToDTO(msg);
				} catch (Exception e) {
					throw new DomibusConnectorGatewayWebserviceClientException("Message could not be transformed! ", e);
				}
				newMessages.add(connMsg);
			}
			
			return newMessages;
		}
		return null;
	}
	
	@Override
	public void getMessageErrorsFromGateway(Message message) throws DomibusConnectorGatewayWebserviceClientException {
		MessageErrorLogEntriesType messageErrors = gatewayService.requestMessageErrors(message.getMessageDetails().getEbmsMessageId());

		
        if (messageErrors != null && !CollectionUtils.isEmpty(messageErrors.getItem())) {
            String source = null;
            try {
                source = ClassUtils
                        .getQualifiedMethodName(this.getClass().getMethod("getMessageErrorsFromGateway", Message.class));
            } catch (Exception e) {
                LOGGER.error("Exception trying to get Method name.", e);
                source = "DomibusConnectorGatewayPluginClientImpl";
            }

            for (MessageErrorLogEntryType messageError : messageErrors.getItem()) {
                persistMessageError(message, DOMIBUS_MESSAGE_ERROR,
                        messageError.getErrorDetail(), source);
            }
        }
	}

	@Override
	public String getMessageStatusOnGateway(Message message) throws DomibusConnectorGatewayWebserviceClientException {
		try {
            String messageStatus = gatewayService.requestMessageStatus(message.getMessageDetails().getEbmsMessageId());
            if (messageStatus != null) {
                return messageStatus;
            }
        } catch (Exception e) {
            LOGGER.error("getMessageStatusOnGateway failed: ", e);
            throw new DomibusConnectorGatewayWebserviceClientException(e);
        }

        return null;
	}
	
	private Element createEmptyRequestPendingMessagesRequest() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        }
        Document document = db.newDocument();
        Element request = document.createElement("requestPendingMessagesRequest");

        return request;
    }
	
	private void persistMessageError(Message message, String text, String details, String source) {

        MessageError messageError = new MessageError();

        messageError.setMessage(message);
        messageError.setText(text);
        messageError.setDetails(details);
        messageError.setSource(source);

        persistenceService.persistMessageError(messageError);
    }
	
	private void persistEbmsMessageIdIntoDatabase(String ebmsMessageId, Message message) {
        if (!ebmsMessageId.isEmpty()) {
            message.getDbMessage().setEbmsMessageId(ebmsMessageId);
            persistenceService.mergeMessageWithDatabase(message);
        }
    }

	public void setGatewayService(DomibusConnectorGatewayServiceInterface gatewayService) {
		this.gatewayService = gatewayService;
	}




}
