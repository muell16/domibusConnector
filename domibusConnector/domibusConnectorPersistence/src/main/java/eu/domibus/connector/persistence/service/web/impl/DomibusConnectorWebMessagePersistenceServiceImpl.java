package eu.domibus.connector.persistence.service.web.impl;

import java.util.Iterator;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessageInfo;
import eu.domibus.connector.persistence.service.impl.DomibusConnectorMessagePersistenceServiceImpl;
import eu.domibus.connector.persistence.service.web.DomibusConnectorWebMessagePersistenceService;
import eu.domibus.connector.web.dto.WebMessage;

@org.springframework.stereotype.Service("webMessagePersistenceService")
public class DomibusConnectorWebMessagePersistenceServiceImpl implements DomibusConnectorWebMessagePersistenceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorMessagePersistenceServiceImpl.class);

    private DomibusConnectorMessageDao messageDao;
    
    /*
     * DAO SETTER  
     */
    @Autowired
    public void setMessageDao(DomibusConnectorMessageDao messageDao) {
        this.messageDao = messageDao;
    }
    
	@Override
	public LinkedList<WebMessage> getAllMessages() {
		Iterable<PDomibusConnectorMessage> allMessages = messageDao.findAll();
		return mapDbMessagesToWebMessages(allMessages);
	}
	
	private LinkedList<WebMessage> mapDbMessagesToWebMessages(Iterable<PDomibusConnectorMessage> messages){
		LinkedList<WebMessage> webMessages = new LinkedList<WebMessage>();
		Iterator<PDomibusConnectorMessage> msgIt = messages.iterator();
		while(msgIt.hasNext()) {
			PDomibusConnectorMessage pMessage = msgIt.next();
			
			WebMessage message = mapDbMessageToWebMessage(pMessage);
			
			webMessages.addLast(message);
		}
		
		return webMessages;
	}

	private WebMessage mapDbMessageToWebMessage(PDomibusConnectorMessage pMessage) {
		WebMessage message = new WebMessage();
		
		message.setConnectorMessageId(pMessage.getConnectorMessageId());
		message.setBackendMessageId(pMessage.getBackendMessageId());
		message.setEbmsMessageId(pMessage.getEbmsMessageId());
		message.setConversationId(pMessage.getConversationId());
		message.setBackendClient(pMessage.getBackendName());
		message.setDeliveredToBackend(pMessage.getDeliveredToNationalSystem());
		message.setDeliveredToGateway(pMessage.getDeliveredToGateway());
		message.setDirection(pMessage.getDirection().name());
		message.setConfirmed(pMessage.getConfirmed());
		message.setRejected(pMessage.getRejected());
		message.setCreated(pMessage.getCreated());
		
		PDomibusConnectorMessageInfo pMessageInfo = pMessage.getMessageInfo();
		message.setAction(pMessageInfo.getAction().getAction());
		message.setService(pMessageInfo.getService().getService());
		message.setOriginalSender(pMessageInfo.getOriginalSender());
		message.setFinalRecipient(pMessageInfo.getFinalRecipient());
		message.setFromPartyId(pMessageInfo.getFrom().getPartyId());
		message.setToPartyId(pMessageInfo.getTo().getPartyId());
		
		
		return message;
	}

}
