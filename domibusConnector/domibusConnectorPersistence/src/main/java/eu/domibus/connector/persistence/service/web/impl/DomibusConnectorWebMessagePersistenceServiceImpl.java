package eu.domibus.connector.persistence.service.web.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorEvidence;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessageInfo;
import eu.domibus.connector.persistence.service.impl.DomibusConnectorMessagePersistenceServiceImpl;
import eu.domibus.connector.persistence.service.web.DomibusConnectorWebMessagePersistenceService;
import eu.domibus.connector.web.dto.WebMessage;
import eu.domibus.connector.web.dto.WebMessageDetail;
import eu.domibus.connector.web.dto.WebMessageEvidence;

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
		Iterable<PDomibusConnectorMessage> allMessages = messageDao.findAllByOrderByCreatedDesc();
		return mapDbMessagesToWebMessages(allMessages);
	}
	
	@Override
	public LinkedList<WebMessage> getMessagesWithinPeriod(Date from, Date to) {
		Iterable<PDomibusConnectorMessage> allMessages = messageDao.findByPeriod(from, to);
		return mapDbMessagesToWebMessages(allMessages);
	}
	
	@Override
	public WebMessageDetail getMessageByConnectorId(String connectorMessageId) {
		PDomibusConnectorMessage dbMessage = messageDao.findOneByConnectorMessageId(connectorMessageId);
		return mapDbMessageToWebMessageDetail(dbMessage);
	}
	
	@Override
    @Transactional(readOnly = true)
    public WebMessageDetail findMessageByNationalId(String nationalMessageId, DomibusConnectorMessageDirection direction) {
        PDomibusConnectorMessage dbMessage = messageDao.findOneByBackendMessageIdAndDirectionTarget(nationalMessageId, direction.getTarget()).get();
        return mapDbMessageToWebMessageDetail(dbMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public WebMessageDetail findMessageByEbmsId(String ebmsMessageId, DomibusConnectorMessageDirection direction) {
        PDomibusConnectorMessage dbMessage = messageDao.findOneByEbmsMessageIdAndDirectionTarget(ebmsMessageId, direction.getTarget()).get();
        return mapDbMessageToWebMessageDetail(dbMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public LinkedList<WebMessage> findMessagesByConversationId(String conversationId) {
        List<PDomibusConnectorMessage> dbMessages = messageDao.findByConversationId(conversationId);
        return mapDbMessagesToWebMessages(dbMessages);
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
		message.setBackendClient(pMessage.getBackendName());
		message.setDeliveredToBackend(pMessage.getDeliveredToNationalSystem());
		message.setDeliveredToGateway(pMessage.getDeliveredToGateway());
		message.setCreated(pMessage.getCreated());
		
		PDomibusConnectorMessageInfo pMessageInfo = pMessage.getMessageInfo();
		message.setAction(pMessageInfo.getAction().getAction());
		message.setService(pMessageInfo.getService().getService());
		message.setFromPartyId(pMessageInfo.getFrom().getPartyId());
		message.setToPartyId(pMessageInfo.getTo().getPartyId());
		
		
		return message;
	}
	
	private WebMessageDetail mapDbMessageToWebMessageDetail(PDomibusConnectorMessage pMessage) {
		WebMessageDetail message = new WebMessageDetail();
		
		message.setConnectorMessageId(pMessage.getConnectorMessageId());
		message.setBackendMessageId(pMessage.getBackendMessageId());
		message.setEbmsMessageId(pMessage.getEbmsMessageId());
		message.setConversationId(pMessage.getConversationId());
		message.setBackendClient(pMessage.getBackendName());
		message.setDeliveredToBackend(pMessage.getDeliveredToNationalSystem());
		message.setDeliveredToGateway(pMessage.getDeliveredToGateway());
//		message.setDirection(pMessage.getDirection().name());
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
		
		if(!CollectionUtils.isEmpty(pMessage.getEvidences())) {
			for(PDomibusConnectorEvidence dbEvidence:pMessage.getEvidences()) {
				WebMessageEvidence evidence = new WebMessageEvidence();
				evidence.setEvidenceType(dbEvidence.getType().name());
				evidence.setDeliveredToGateway(dbEvidence.getDeliveredToGateway());
				evidence.setDeliveredToBackend(dbEvidence.getDeliveredToNationalSystem());
				message.getEvidences().add(evidence);
			}
		}
		
		return message;
	}



}
