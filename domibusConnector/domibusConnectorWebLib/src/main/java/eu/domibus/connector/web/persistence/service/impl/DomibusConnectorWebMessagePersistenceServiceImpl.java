package eu.domibus.connector.web.persistence.service.impl;

import java.util.*;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.web.persistence.service.DomibusConnectorWebMessagePersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorEvidence;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessageInfo;
import eu.domibus.connector.persistence.service.impl.DCMessagePersistenceServiceImpl;

import eu.domibus.connector.web.dto.WebMessage;
import eu.domibus.connector.web.dto.WebMessageDetail;
import eu.domibus.connector.web.dto.WebMessageEvidence;

@Service("webMessagePersistenceService")
public class DomibusConnectorWebMessagePersistenceServiceImpl implements DomibusConnectorWebMessagePersistenceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DCMessagePersistenceServiceImpl.class);

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
		Optional<PDomibusConnectorMessage> dbMessage = messageDao.findOneByConnectorMessageId(connectorMessageId);
		if (dbMessage.isPresent()) {
			return mapDbMessageToWebMessageDetail(dbMessage.get());
		} else {
			return null;
		}
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
		DomibusConnectorMessageDirection domibusConnectorMessageDirection = DomibusConnectorMessageDirection.fromMessageTargetSource(pMessage.getDirectionSource(), pMessage.getDirectionTarget());
		message.setDirection(domibusConnectorMessageDirection.toString());
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
		
		if(!CollectionUtils.isEmpty(pMessage.getRelatedEvidences())) {
			for(PDomibusConnectorEvidence dbEvidence:pMessage.getRelatedEvidences()) {
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
