package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.Action;
import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.MessageConfirmation;
import eu.domibus.connector.domain.MessageError;
import eu.domibus.connector.domain.Party;
import eu.domibus.connector.domain.enums.EvidenceType;
import eu.domibus.connector.domain.enums.MessageDirection;
import eu.domibus.connector.persistence.dao.DomibusConnectorActionDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorEvidenceDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageErrorDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageInfoDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorPartyDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorServiceDao;
import eu.domibus.connector.domain.Service;
import eu.domibus.connector.persistence.model.DomibusConnectorAction;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import eu.domibus.connector.persistence.model.DomibusConnectorEvidence;
import eu.domibus.connector.persistence.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.model.DomibusConnectorMessageError;
import eu.domibus.connector.persistence.model.DomibusConnectorMessageInfo;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;

@org.springframework.stereotype.Service("persistenceService")
public class DomibusConnectorPersistenceServiceImpl implements DomibusConnectorPersistenceService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorPersistenceServiceImpl.class);
    
    private static final String RETRIEVAL_NON_RETRIEVAL_TO_RECIPIENT = "RetrievalNonRetrievalToRecipient";
    private static final String DELIVERY_NON_DELIVERY_TO_RECIPIENT = "DeliveryNonDeliveryToRecipient";
    private static final String RELAY_REMMD_FAILURE = "RelayREMMDFailure";
    private static final String RELAY_REMMD_ACCEPTANCE_REJECTION = "RelayREMMDAcceptanceRejection";

    @Autowired
    private DomibusConnectorMessageDao messageDao;
    
    @Autowired
    private DomibusConnectorEvidenceDao evidenceDao;
    
    @Autowired
    private DomibusConnectorActionDao actionDao;
    
    @Autowired
    private DomibusConnectorServiceDao serviceDao;
    
//    @Autowired
    private DomibusConnectorPartyDao partyDao;
    
    @Autowired
    private DomibusConnectorMessageInfoDao messageInfoDao;
    
    @Autowired
    private DomibusConnectorMessageErrorDao messageErrorDao;

    public void setMessageDao(DomibusConnectorMessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public void setEvidenceDao(DomibusConnectorEvidenceDao evidenceDao) {
        this.evidenceDao = evidenceDao;
    }

    public void setActionDao(DomibusConnectorActionDao actionDao) {
        this.actionDao = actionDao;
    }

    public void setServiceDao(DomibusConnectorServiceDao serviceDao) {
        this.serviceDao = serviceDao;
    }

    public void setPartyDao(DomibusConnectorPartyDao partyDao) {
        this.partyDao = partyDao;
    }

    public void setMessageInfoDao(DomibusConnectorMessageInfoDao messageInfoDao) {
        this.messageInfoDao = messageInfoDao;
    }

    public void setMessageErrorDao(DomibusConnectorMessageErrorDao messageErrorDao) {
        this.messageErrorDao = messageErrorDao;
    }

    @Override
    @Transactional
    public void persistMessageIntoDatabase(Message message, MessageDirection direction) {
        DomibusConnectorMessage dbMessage = new DomibusConnectorMessage();

        //dbMessage.setDirection(direction);
        dbMessage.setConversationId(message.getMessageDetails().getConversationId());
        dbMessage.setEbmsMessageId(message.getMessageDetails().getEbmsMessageId());
        dbMessage.setNationalMessageId(message.getMessageDetails().getNationalMessageId());

        try {
            messageDao.save(dbMessage);
            //messageDao.saveNewMessage(dbMessage);
        } catch (DuplicateKeyException cve) {
            LOGGER.error("Duplicate key exception occured! The nationalMessageId [{}] or the ebmsMessageId [{}] already exist.", 
                    dbMessage.getNationalMessageId(), dbMessage.getEbmsMessageId());
//            throw new PersistenceException(
//                    "Could not persist message into database. Most likely the nationalMessageId or the ebmsMessageId already exist. ",
//                    cve);
        }

        DomibusConnectorMessageInfo dbMessageInfo = new DomibusConnectorMessageInfo();
        dbMessageInfo.setMessage(dbMessage);
//        dbMessageInfo.setAction(message.getMessageDetails().getAction());
//        dbMessageInfo.setService(message.getMessageDetails().getService());
        dbMessageInfo.setFinalRecipient(message.getMessageDetails().getFinalRecipient());
        dbMessageInfo.setOriginalSender(message.getMessageDetails().getOriginalSender());
//        dbMessageInfo.setFrom(message.getMessageDetails().getFromParty());
//        dbMessageInfo.setTo(message.getMessageDetails().getToParty());

//        try {
//            messageInfoDao.persistMessageInfo(dbMessageInfo);
//        } catch (Exception e) {
//            throw new PersistenceException("Could not persist message info into database. ", e);
//        }

        dbMessage.setMessageInfo(dbMessageInfo);

//        message.setDbMessage(dbMessage);
        message.getMessageDetails().setDbMessageId(dbMessage.getId());
    }

    @Override
    @Transactional
    public void mergeMessageWithDatabase(Message message) {

        //messageDao.mergeMessage(message.getDbMessage());

        DomibusConnectorMessage dbMessage = messageDao.findOne(message.getDbMessageId());
        
        dbMessage.getMessageInfo();
        
//        DomibusConnectorMessageInfo messageInfo = message.getDbMessage().getMessageInfo();
//
//        if (messageInfo != null) {
//            messageInfo.setAction(message.getMessageDetails().getAction());
//            messageInfo.setService(message.getMessageDetails().getService());
//            messageInfo.setFrom(message.getMessageDetails().getFromParty());
//            messageInfo.setTo(message.getMessageDetails().getToParty());
//            messageInfo.setFinalRecipient(message.getMessageDetails().getFinalRecipient());
//            messageInfo.setOriginalSender(message.getMessageDetails().getOriginalSender());
//
//            messageInfoDao.mergeMessageInfo(messageInfo);
//        }
    }

    @Override
    @Transactional
    public void setMessageDeliveredToGateway(Message message) {
//        DomibusConnectorMessage dbMessage = messageDao.mergeMessage(message.getDbMessage());
//        dbMessage.setDeliveredToGateway(new Date());
//        dbMessage = messageDao.mergeMessage(dbMessage);
//        message.setDbMessage(dbMessage);
    }

    @Override
    @Transactional
    public void setMessageDeliveredToNationalSystem(Message message) {
//        DomibusConnectorMessage dbMessage = messageDao.mergeMessage(message.getDbMessage());
//        dbMessage.setDeliveredToNationalSystem(new Date());
//        dbMessage = messageDao.mergeMessage(dbMessage);
//        message.setDbMessage(dbMessage);
    }

    @Override
    @Transactional
    public void setEvidenceDeliveredToGateway(Message message, EvidenceType evidenceType) {
//        messageDao.mergeMessage(message.getDbMessage());
//        List<DomibusConnectorEvidence> evidences = evidenceDao.findEvidencesForMessage(message.getDbMessage());
//        DomibusConnectorEvidence dbEvidence = findEvidence(evidences, evidenceType);
//        if (dbEvidence != null) {
//            dbEvidence.setDeliveredToGateway(new Date());
//            evidenceDao.mergeEvidence(dbEvidence);
//        }
    }

    @Override
    @Transactional
    public void setEvidenceDeliveredToNationalSystem(Message message, EvidenceType evidenceType) {
//        messageDao.mergeMessage(message.getDbMessage());
//        List<DomibusConnectorEvidence> evidences = evidenceDao.findEvidencesForMessage(message.getDbMessage());
//        DomibusConnectorEvidence dbEvidence = findEvidence(evidences, evidenceType);
//        if (dbEvidence != null) {
//            dbEvidence.setDeliveredToNationalSystem(new Date());
//            evidenceDao.mergeEvidence(dbEvidence);
//        }
    }

    private DomibusConnectorEvidence findEvidence(List<DomibusConnectorEvidence> evidences, EvidenceType evidenceType) {
        if (evidences != null) {
            for (DomibusConnectorEvidence evidence : evidences) {
                if (evidence.getType().equals(evidenceType)) {
                    return evidence;
                }
            }
        }
        return null;
    }

    @Override
    @Transactional
    public void persistEvidenceForMessageIntoDatabase(Message message, byte[] evidence, EvidenceType evidenceType) {
        DomibusConnectorEvidence dbEvidence = new DomibusConnectorEvidence();

//        dbEvidence.setMessage(message.getDbMessage());
//        dbEvidence.setEvidence(new String(evidence));
//        dbEvidence.setType(evidenceType);
//        dbEvidence.setDeliveredToGateway(null);
//        dbEvidence.setDeliveredToNationalSystem(null);
//
//        evidenceDao.saveNewEvidence(dbEvidence);
    }

    @Override
    @Transactional(readOnly = true)
    public Message findMessageByNationalId(String nationalMessageId) {
        DomibusConnectorMessage dbMessage = messageDao.findOneByNationalMessageId(nationalMessageId);

        Message message = mapDbMessageToMessage(dbMessage);

        return message;

    }

    @Override
    @Transactional(readOnly = true)
    public Message findMessageByEbmsId(String ebmsMessageId) {
        DomibusConnectorMessage dbMessage = messageDao.findOneByEbmsMessageId(ebmsMessageId);

        Message message = mapDbMessageToMessage(dbMessage);

        return message;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> findMessagesByConversationId(String conversationId) {
        List<DomibusConnectorMessage> dbMessages = messageDao.findByConversationId(conversationId);
        return mapDBMessagesToDTO(dbMessages);
    }

	private List<Message> mapDBMessagesToDTO(List<DomibusConnectorMessage> dbMessages) {
		if (dbMessages != null && !dbMessages.isEmpty()) {
            List<Message> messages = new ArrayList<Message>(dbMessages.size());
            for (DomibusConnectorMessage dbMessage : dbMessages) {
                messages.add(mapDbMessageToMessage(dbMessage));
            }
            return messages;
        }
        return null;
	}

    @Override
    @Transactional(readOnly = true)
    public List<Message> findOutgoingUnconfirmedMessages() {
        List<DomibusConnectorMessage> dbMessages = messageDao.findOutgoingUnconfirmedMessages();
        return mapDBMessagesToDTO(dbMessages);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Message> findOutgoingMessagesNotRejectedAndWithoutDelivery() {
        List<DomibusConnectorMessage> dbMessages = messageDao.findOutgoingMessagesNotRejectedAndWithoutDelivery();
        return mapDBMessagesToDTO(dbMessages);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Message> findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD() {
        List<DomibusConnectorMessage> dbMessages = messageDao.findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD();
        return mapDBMessagesToDTO(dbMessages);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> findIncomingUnconfirmedMessages() {
        List<DomibusConnectorMessage> dbMessages = messageDao.findIncomingUnconfirmedMessages();
        return mapDBMessagesToDTO(dbMessages);
    }

    @Override
    @Transactional
    public void confirmMessage(Message message) {
        messageDao.confirmMessage(message.getDbMessageId());
    }

    @Override
    @Transactional
    public void rejectMessage(Message message) {
        messageDao.rejectMessage(message.getDbMessageId());
    }

    private Message mapDbMessageToMessage(DomibusConnectorMessage dbMessage) {
//        MessageDetails details = new MessageDetails();
//        details.setDbMessageId(dbMessage.getId());
//        details.setEbmsMessageId(dbMessage.getEbmsMessageId());
//        details.setNationalMessageId(dbMessage.getNationalMessageId());
//        details.setConversationId(dbMessage.getConversationId());
//        DomibusConnectorMessageInfo messageInfo = dbMessage.getMessageInfo();
//        if (messageInfo != null) {
//            details.setAction(messageInfo.getAction());
//            details.setService(messageInfo.getService());
//            details.setFinalRecipient(messageInfo.getFinalRecipient());
//            details.setOriginalSender(messageInfo.getOriginalSender());
//            details.setFromParty(messageInfo.getFrom());
//            details.setToParty(messageInfo.getTo());
//        }
//
//        Message message = new Message(details);
//
//        message.setDbMessage(dbMessage);
//
//        for (DomibusConnectorEvidence dbEvidence : dbMessage.getEvidences()) {
//            try {
//                MessageConfirmation confirmation = mapDbEvidenceToMessageConfirmation(dbEvidence);
//                message.addConfirmation(confirmation);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        return message;
        return null;
    }

    private MessageConfirmation mapDbEvidenceToMessageConfirmation(DomibusConnectorEvidence dbEvidence) {
//            throws Exception {
//        MessageConfirmation messageConfirmation = new MessageConfirmation();
//        messageConfirmation.setEvidenceType(dbEvidence.getType());
//        messageConfirmation.setEvidence(dbEvidence.getEvidence().getBytes());
//
//        return messageConfirmation;
        return null;
    }

    @Override
    @Transactional
    public void persistMessageError(MessageError messageError) {
        DomibusConnectorMessageError dbError = new DomibusConnectorMessageError();
//        dbError.setMessage(messageError.getMessage().getDbMessage());
        dbError.setErrorMessage(messageError.getText());
        dbError.setDetailedText(messageError.getDetails());
        dbError.setErrorSource(messageError.getSource());

        this.messageErrorDao.save(dbError);
    }

    @Override
    @Transactional
    public void persistMessageErrorFromException(Message message, Throwable ex, Class<?> source) {
        if (message == null || message.getDbMessageId() == null) {
            throw new RuntimeException (
                    "Message Error cannot be stored as the message object, or its database reference is null!");
        }
        if (ex == null) {
            throw new RuntimeException("Message Error cannot be stored as there is no exception given!");
        }
        if (source == null) {
            throw new RuntimeException(
                    "Message Error cannot be stored as the Class object given as source is null!");
        }
        DomibusConnectorMessageError error = new DomibusConnectorMessageError();
        error.setErrorMessage(ex.getMessage());
        //error.setDetailedText(ExceptionUtils.getStackTrace(ex));
        error.setErrorSource(source.getName());
        //error.setMessage(message.getDbMessage());

        this.messageErrorDao.save(error);
    }

    @Override
    public List<MessageError> getMessageErrors(Message message) throws Exception {
        List<DomibusConnectorMessageError> dbErrorsForMessage = this.messageErrorDao.findByMessage(message.getDbMessageId());
//        if (!CollectionUtils.isEmpty(dbErrorsForMessage)) {
//            List<MessageError> messageErrors = new ArrayList<MessageError>(dbErrorsForMessage.size());
//
//            for (DomibusConnectorMessageError dbMsgError : dbErrorsForMessage) {
//                MessageError msgError = new MessageError();
//                msgError.setMessage(message);
//                msgError.setText(dbMsgError.getErrorMessage());
//                if (dbMsgError.getDetailedText() != null)
//                    msgError.setDetails(dbMsgError.getDetailedText());
//                msgError.setSource(dbMsgError.getErrorSource());
//
//                messageErrors.add(msgError);
//            }
//
//            return messageErrors;
//        }
        return null;
    }

    @Override
    public Action getAction(String action) {
        
        DomibusConnectorAction findOne = actionDao.findOne(action);

        return mapActionToDomain(findOne);
    }

    Action mapActionToDomain(DomibusConnectorAction persistenceAction) {
        if (persistenceAction != null) {
            Action action = new Action();
            BeanUtils.copyProperties(persistenceAction, action);
            return action;
        }
        return null;
    }
    
    
    @Override
    public Service getService(String service) {
        //return serviceDao.getService(service);
        //TODO
        return new Service();
    }

    @Override
    public Party getParty(String partyId, String role) {
        //return partyDao.getParty(partyId, role);
        //TODO        
        return new Party();
    }

    @Override
    public Party getPartyByPartyId(String partyId) {
        //return partyDao.getPartyByPartyId(partyId);
        //TODO
        return new Party();
    }

    @Override
    public Action getRelayREMMDAcceptanceRejectionAction() {
        //return getAction(RELAY_REMMD_ACCEPTANCE_REJECTION);
        return new Action();
    }

    @Override
    public Action getRelayREMMDFailure() {
        //return getAction(RELAY_REMMD_FAILURE);
        return new Action();
    }

    @Override
    public Action getDeliveryNonDeliveryToRecipientAction() {
        //return getAction(DELIVERY_NON_DELIVERY_TO_RECIPIENT);
        return new Action();
    }

    @Override
    public Action getRetrievalNonRetrievalToRecipientAction() {
        //return getAction(RETRIEVAL_NON_RETRIEVAL_TO_RECIPIENT);
        return new Action();
    }

}
