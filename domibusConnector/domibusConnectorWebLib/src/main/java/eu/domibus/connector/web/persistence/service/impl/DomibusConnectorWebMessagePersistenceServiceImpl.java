package eu.domibus.connector.web.persistence.service.impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
//import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorEvidence;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessageInfo;
import eu.domibus.connector.persistence.service.impl.DomibusConnectorMessagePersistenceServiceImpl;
import eu.domibus.connector.web.dto.WebMessage;
import eu.domibus.connector.web.dto.WebMessageDetail;
import eu.domibus.connector.web.dto.WebMessageEvidence;
import eu.domibus.connector.web.persistence.service.DomibusConnectorWebMessagePersistenceService;

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
	public Optional<WebMessage> getMessageByConnectorId(String connectorMessageId) {
		PDomibusConnectorMessage dbMessage = messageDao.findOneByConnectorMessageId(connectorMessageId);
		return mapDbMessageToWebMessage(dbMessage);
	}
	
	@Override
	public Optional<WebMessage> getOutgoingMessageByBackendMessageId(String backendMessageId){
		PDomibusConnectorMessage dbMessage = messageDao.findOneByBackendMessageIdAndDirectionTarget(backendMessageId, MessageTargetSource.GATEWAY).get();
        return mapDbMessageToWebMessage(dbMessage);
	}
	
	@Override
	public Optional<WebMessage> getIncomingMessageByEbmsMessageId(String ebmsMessageId){
		PDomibusConnectorMessage dbMessage = messageDao.findOneByEbmsMessageIdAndDirectionTarget(ebmsMessageId, MessageTargetSource.BACKEND).get();
        return mapDbMessageToWebMessage(dbMessage);
	}
	
	@Override
    @Transactional(readOnly = true)
    public Optional<WebMessage>  findMessageByNationalId(String nationalMessageId, DomibusConnectorMessageDirection direction) {
        PDomibusConnectorMessage dbMessage = messageDao.findOneByBackendMessageIdAndDirectionTarget(nationalMessageId, direction.getTarget()).get();
        return mapDbMessageToWebMessage(dbMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WebMessage>  findMessageByEbmsId(String ebmsMessageId, DomibusConnectorMessageDirection direction) {
        PDomibusConnectorMessage dbMessage = messageDao.findOneByEbmsMessageIdAndDirectionTarget(ebmsMessageId, direction.getTarget()).get();
        return mapDbMessageToWebMessage(dbMessage);
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
		
		LOGGER.debug("Returned {} results.", all.getSize());

		return all.map(c -> new DBMessageToWebMessageConverter().convert(c));

	}


	private Example<PDomibusConnectorMessage> getpDomibusConnectorMessageExample(Example<WebMessage> example) {
    	if (example == null) {
    		throw new IllegalArgumentException("Example cannot be null!");
		}
//		WebMessage probe = example.getProbe();

		PDomibusConnectorMessage dbMsg = new PDomibusConnectorMessage();
//		PDomibusConnectorMessageInfo dbMsgInfo = new PDomibusConnectorMessageInfo();

//		dbMsg.setBackendName(probe.getBackendClient());
//		dbMsg.setDeliveredToNationalSystem(probe.getDeliveredToBackend());
		
//		if(!StringUtils.isEmpty(probe.getFinalRecipient())) {
//			dbMsgInfo.setFinalRecipient(probe.getFinalRecipient());
//			dbMsg.setMessageInfo(dbMsgInfo);
//		}
//		
//		if(!StringUtils.isEmpty(probe.getOriginalSender())) {
//			dbMsgInfo.setOriginalSender(probe.getOriginalSender());
//			dbMsg.setMessageInfo(dbMsgInfo);
//		}
//
//		if (!StringUtils.isEmpty(probe.getAction())) {
//			PDomibusConnectorAction dbAction = new PDomibusConnectorAction();
//			dbAction.setAction(probe.getAction());
//			dbMsgInfo.setAction(dbAction);
//			dbMsg.setMessageInfo(dbMsgInfo);
//		}
//		if (!StringUtils.isEmpty(probe.getService())) {
//			PDomibusConnectorService dbService = new PDomibusConnectorService();
//			dbService.setService(probe.getService());
//			dbMsgInfo.setService(dbService);
//			dbMsg.setMessageInfo(dbMsgInfo);
//		}
//		if (!StringUtils.isEmpty(probe.getToPartyId())) {
//			PDomibusConnectorParty dbParty = new PDomibusConnectorParty();
//			dbParty.setPartyId(probe.getToPartyId());
//			dbMsgInfo.setTo(dbParty);
//			dbMsg.setMessageInfo(dbMsgInfo);
//		}
//		if (!StringUtils.isEmpty(probe.getFromPartyId())) {
//			PDomibusConnectorParty dbParty = new PDomibusConnectorParty();
//			dbParty.setPartyId(probe.getFromPartyId());
//			dbMsgInfo.setFrom(dbParty);
//			dbMsg.setMessageInfo(dbMsgInfo);
//		}

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
			message.setConversationId(pMessage.getConversationId());
			message.setBackendName(pMessage.getBackendName());
			message.setDirectionSource(pMessage.getDirectionSource()!=null?pMessage.getDirectionSource().getDbName():null);
			message.setDirectionTarget(pMessage.getDirectionTarget()!=null?pMessage.getDirectionTarget().getDbName():null);
			message.setDeliveredToNationalSystem(pMessage.getDeliveredToNationalSystem()!=null?ZonedDateTime.ofInstant(pMessage.getDeliveredToNationalSystem().toInstant(), ZoneId.systemDefault()):null);
			message.setDeliveredToGateway(pMessage.getDeliveredToGateway()!=null?ZonedDateTime.ofInstant(pMessage.getDeliveredToGateway().toInstant(), ZoneId.systemDefault()):null);
			message.setCreated(pMessage.getCreated()!=null?ZonedDateTime.ofInstant(pMessage.getCreated().toInstant(), ZoneId.systemDefault()):null);
			message.setConfirmed(pMessage.getConfirmed()!=null?ZonedDateTime.ofInstant(pMessage.getConfirmed().toInstant(), ZoneId.systemDefault()):null);
			message.setRejected(pMessage.getRejected()!=null?ZonedDateTime.ofInstant(pMessage.getRejected().toInstant(), ZoneId.systemDefault()):null);

			PDomibusConnectorMessageInfo pMessageInfo = pMessage.getMessageInfo();
			if(pMessageInfo!=null) {

				message.setMessageInfo(mapDbMessageInfoToWebMessageDetail(pMessageInfo));
				
			}
			
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
		
		private WebMessageDetail mapDbMessageInfoToWebMessageDetail(PDomibusConnectorMessageInfo pMessageInfo) {
	    	WebMessageDetail messageDetail = new WebMessageDetail();
			
			messageDetail.setOriginalSender(pMessageInfo.getOriginalSender());
			messageDetail.setFinalRecipient(pMessageInfo.getFinalRecipient());
			
			if (pMessageInfo.getAction()!=null) {
				messageDetail.setAction(messageDetail.new Action(pMessageInfo.getAction().getAction()));
			}
			
			if(pMessageInfo.getService()!=null) {
				messageDetail.setService(messageDetail.new Service(pMessageInfo.getService().getService(), pMessageInfo.getService().getServiceType()));
			}
			
			if(pMessageInfo.getFrom()!=null) {
				messageDetail.setFrom(messageDetail.new Party(pMessageInfo.getFrom().getPartyId(),pMessageInfo.getFrom().getPartyIdType()));
			}
			
			if(pMessageInfo.getTo()!=null) {
				messageDetail.setTo(messageDetail.new Party(pMessageInfo.getTo().getPartyId(),pMessageInfo.getTo().getPartyIdType()));
			}
			
			
			
			
			return messageDetail;
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

	private Optional<WebMessage> mapDbMessageToWebMessage (PDomibusConnectorMessage pMessage){
		if (pMessage == null) {
    		return Optional.empty();
		}
			
		WebMessage message = new DBMessageToWebMessageConverter().convert(pMessage);
		
		return Optional.of(message);
	}
	
//	private Optional<WebMessageDetail> mapDbMessageToWebMessageDetail(PDomibusConnectorMessageInfo pMessageInfo) {
//    	if (pMessageInfo == null) {
//    		return Optional.empty();
//		}
//		WebMessageDetail messageDetail = new WebMessageDetail();
//		
//		messageDetail.setConnectorMessageId(pMessage.getConnectorMessageId());
//		messageDetail.setBackendMessageId(pMessage.getBackendMessageId());
//		messageDetail.setEbmsMessageId(pMessage.getEbmsMessageId());
//		messageDetail.setConversationId(pMessage.getConversationId());
//		messageDetail.setBackendName(pMessage.getBackendName());
//		messageDetail.setDeliveredToNationalSystem(pMessage.getDeliveredToNationalSystem());
//		messageDetail.setDeliveredToGateway(pMessage.getDeliveredToGateway());
//		DomibusConnectorMessageDirection domibusConnectorMessageDirection = DomibusConnectorMessageDirection.fromMessageTargetSource(pMessage.getDirectionSource(), pMessage.getDirectionTarget());
//		messageDetail.setDirection(domibusConnectorMessageDirection.toString());
//		messageDetail.setConfirmed(pMessage.getConfirmed());
//		messageDetail.setRejected(pMessage.getRejected());
//		messageDetail.setCreated(pMessage.getCreated());
//		
//		PDomibusConnectorMessageInfo pMessageInfo = pMessage.getMessageInfo();
//		messageDetail.setAction(pMessageInfo.getAction().getAction());
//		messageDetail.setService(pMessageInfo.getService().getService());
//		messageDetail.setOriginalSender(pMessageInfo.getOriginalSender());
//		messageDetail.setFinalRecipient(pMessageInfo.getFinalRecipient());
//		messageDetail.setFromPartyId(pMessageInfo.getFrom().getPartyId());
//		messageDetail.setToPartyId(pMessageInfo.getTo().getPartyId());
//		
//		if(!CollectionUtils.isEmpty(pMessage.getEvidences())) {
//			for(PDomibusConnectorEvidence dbEvidence:pMessage.getEvidences()) {
//				WebMessageEvidence evidence = new WebMessageEvidence();
//				evidence.setEvidenceType(dbEvidence.getType().name());
//				evidence.setDeliveredToGateway(dbEvidence.getDeliveredToGateway());
//				evidence.setDeliveredToBackend(dbEvidence.getDeliveredToNationalSystem());
//				messageDetail.getEvidences().add(evidence);
//			}
//		}
//		
//		return Optional.of(messageDetail);
//	}


}
