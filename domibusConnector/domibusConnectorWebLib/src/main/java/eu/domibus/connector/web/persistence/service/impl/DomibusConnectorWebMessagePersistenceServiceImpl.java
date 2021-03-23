package eu.domibus.connector.web.persistence.service.impl;

import java.util.*;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.persistence.model.*;
import eu.domibus.connector.web.persistence.service.DomibusConnectorWebMessagePersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.service.impl.DomibusConnectorMessagePersistenceServiceImpl;

import eu.domibus.connector.web.dto.WebMessage;
import eu.domibus.connector.web.dto.WebMessageDetail;
import eu.domibus.connector.web.dto.WebMessageEvidence;
import org.springframework.util.StringUtils;

@Service("webMessagePersistenceService")
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
	public Optional<WebMessageDetail> getMessageByConnectorId(String connectorMessageId) {
		PDomibusConnectorMessage dbMessage = messageDao.findOneByConnectorMessageId(connectorMessageId);
		return mapDbMessageToWebMessageDetail(dbMessage);
	}
	
	@Override
	public Optional<WebMessageDetail> getOutgoingMessageByBackendMessageId(String backendMessageId){
		PDomibusConnectorMessage dbMessage = messageDao.findOneByBackendMessageIdAndDirectionTarget(backendMessageId, MessageTargetSource.GATEWAY).get();
        return mapDbMessageToWebMessageDetail(dbMessage);
	}
	
	@Override
	public Optional<WebMessageDetail> getIncomingMessageByEbmsMessageId(String ebmsMessageId){
		PDomibusConnectorMessage dbMessage = messageDao.findOneByEbmsMessageIdAndDirectionTarget(ebmsMessageId, MessageTargetSource.BACKEND).get();
        return mapDbMessageToWebMessageDetail(dbMessage);
	}
	
	@Override
    @Transactional(readOnly = true)
    public Optional<WebMessageDetail>  findMessageByNationalId(String nationalMessageId, DomibusConnectorMessageDirection direction) {
        PDomibusConnectorMessage dbMessage = messageDao.findOneByBackendMessageIdAndDirectionTarget(nationalMessageId, direction.getTarget()).get();
        return mapDbMessageToWebMessageDetail(dbMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WebMessageDetail>  findMessageByEbmsId(String ebmsMessageId, DomibusConnectorMessageDirection direction) {
        PDomibusConnectorMessage dbMessage = messageDao.findOneByEbmsMessageIdAndDirectionTarget(ebmsMessageId, direction.getTarget()).get();
        return mapDbMessageToWebMessageDetail(dbMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public LinkedList<WebMessage> findMessagesByConversationId(String conversationId) {
        List<PDomibusConnectorMessage> dbMessages = messageDao.findByConversationId(conversationId);
        return mapDbMessagesToWebMessages(dbMessages);
    }

	@Override
	public Page<WebMessage> findAll(Example<WebMessage> example, Pageable pageable) {
		Example<PDomibusConnectorMessage> exampleDbMsg = getpDomibusConnectorMessageExample(example);


		Page<PDomibusConnectorMessage> all = messageDao.findAll(exampleDbMsg, pageable);

		return all.map(c -> new DBMessageToWebMessageConverter().convert(c));

	}


	private Example<PDomibusConnectorMessage> getpDomibusConnectorMessageExample(Example<WebMessage> example) {
    	if (example == null) {
    		throw new IllegalArgumentException("Example cannot be null!");
		}
		WebMessage probe = example.getProbe();

		PDomibusConnectorMessage dbMsg = new PDomibusConnectorMessage();
		PDomibusConnectorMessageInfo dbMsgInfo = new PDomibusConnectorMessageInfo();

		dbMsg.setBackendName(probe.getBackendClient());
		dbMsg.setDeliveredToNationalSystem(probe.getDeliveredToBackend());
		
		if(!StringUtils.isEmpty(probe.getFinalRecipient())) {
			dbMsgInfo.setFinalRecipient(probe.getFinalRecipient());
			dbMsg.setMessageInfo(dbMsgInfo);
		}
		
		if(!StringUtils.isEmpty(probe.getOriginalSender())) {
			dbMsgInfo.setOriginalSender(probe.getOriginalSender());
			dbMsg.setMessageInfo(dbMsgInfo);
		}

		if (!StringUtils.isEmpty(probe.getAction())) {
			PDomibusConnectorAction dbAction = new PDomibusConnectorAction();
			dbAction.setAction(probe.getAction());
			dbMsgInfo.setAction(dbAction);
			dbMsg.setMessageInfo(dbMsgInfo);
		}
		if (!StringUtils.isEmpty(probe.getService())) {
			PDomibusConnectorService dbService = new PDomibusConnectorService();
			dbService.setService(probe.getService());
			dbMsgInfo.setService(dbService);
			dbMsg.setMessageInfo(dbMsgInfo);
		}
		if (!StringUtils.isEmpty(probe.getToPartyId())) {
			PDomibusConnectorParty dbParty = new PDomibusConnectorParty();
			dbParty.setPartyId(probe.getToPartyId());
			dbMsgInfo.setTo(dbParty);
			dbMsg.setMessageInfo(dbMsgInfo);
		}
		if (!StringUtils.isEmpty(probe.getFromPartyId())) {
			PDomibusConnectorParty dbParty = new PDomibusConnectorParty();
			dbParty.setPartyId(probe.getFromPartyId());
			dbMsgInfo.setFrom(dbParty);
			dbMsg.setMessageInfo(dbMsgInfo);
		}

		Example<PDomibusConnectorMessage> exampleDbMsg = Example.of(dbMsg, example.getMatcher());
		return exampleDbMsg;
	}


	private static class DBMessageToWebMessageConverter implements Converter<PDomibusConnectorMessage, WebMessage> {

		@Nullable
		@Override
		public WebMessage convert(PDomibusConnectorMessage pMessage) {
			WebMessage message = new WebMessage();

			message.setConnectorMessageId(pMessage.getConnectorMessageId());
			message.setEbmsMessageId(pMessage.getEbmsMessageId());
			message.setBackendMessageId(pMessage.getBackendMessageId());
			message.setBackendClient(pMessage.getBackendName());
			message.setDeliveredToBackend(pMessage.getDeliveredToNationalSystem());
			message.setDeliveredToGateway(pMessage.getDeliveredToGateway());
			message.setCreated(pMessage.getCreated());

			PDomibusConnectorMessageInfo pMessageInfo = pMessage.getMessageInfo();
			if(pMessageInfo!=null) {
				
			message.setAction(pMessageInfo.getAction()!=null?pMessageInfo.getAction().getAction():"n.a.");
			message.setService(pMessageInfo.getService()!=null?pMessageInfo.getService().getService():"n.a.");
			message.setFromPartyId(pMessageInfo.getFrom()!=null?pMessageInfo.getFrom().getPartyId():"n.a.");
			message.setFromPartyIdType(pMessageInfo.getFrom()!=null?pMessageInfo.getFrom().getPartyIdType():"n.a.");
			message.setToPartyId(pMessageInfo.getTo()!=null?pMessageInfo.getTo().getPartyId():"n.a.");
			message.setToPartyIdType(pMessageInfo.getTo()!=null?pMessageInfo.getTo().getPartyIdType():"n.a.");
			}
			return message;
		}
	}

	@Override
	public long count(Example<WebMessage> example) {
		Example<PDomibusConnectorMessage> pDomibusConnectorMessageExample = getpDomibusConnectorMessageExample(example);
		return messageDao.count(pDomibusConnectorMessageExample);
	}

	private LinkedList<WebMessage> mapDbMessagesToWebMessages(Iterable<PDomibusConnectorMessage> messages){
		LinkedList<WebMessage> webMessages = new LinkedList<WebMessage>();
		Iterator<PDomibusConnectorMessage> msgIt = messages.iterator();
		while(msgIt.hasNext()) {
			PDomibusConnectorMessage pMessage = msgIt.next();

			WebMessage message = new DBMessageToWebMessageConverter().convert(pMessage);
			webMessages.addLast(message);
		}
		
		return webMessages;
	}

	
	private Optional<WebMessageDetail> mapDbMessageToWebMessageDetail(PDomibusConnectorMessage pMessage) {
    	if (pMessage == null) {
    		return Optional.empty();
		}
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
		
		if(!CollectionUtils.isEmpty(pMessage.getEvidences())) {
			for(PDomibusConnectorEvidence dbEvidence:pMessage.getEvidences()) {
				WebMessageEvidence evidence = new WebMessageEvidence();
				evidence.setEvidenceType(dbEvidence.getType().name());
				evidence.setDeliveredToGateway(dbEvidence.getDeliveredToGateway());
				evidence.setDeliveredToBackend(dbEvidence.getDeliveredToNationalSystem());
				message.getEvidences().add(evidence);
			}
		}
		
		return Optional.of(message);
	}


}
