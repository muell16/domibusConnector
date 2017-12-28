package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.Action;
import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.MessageConfirmation;
import eu.domibus.connector.domain.MessageDetails;
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
import eu.domibus.connector.persistence.model.DomibusConnectorParty;
import eu.domibus.connector.persistence.model.DomibusConnectorPartyPK;
import eu.domibus.connector.persistence.model.DomibusConnectorService;
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
    
    @Autowired
    private DomibusConnectorPartyDao partyDao;
    
    @Autowired
    private DomibusConnectorMessageInfoDao messageInfoDao;
    
    @Autowired
    private DomibusConnectorMessageErrorDao messageErrorDao;

    /*
     * DAO SETTER  
     */
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
    /*
    * END DAO SETTER
    */
    
    
    @Override
    @Transactional
    public void persistMessageIntoDatabase(Message message, MessageDirection direction) {
        if (message.getDbMessageId() !=  null) {
            throw new IllegalArgumentException("Cannot persist (create) a message with an already set db message id! Use mergeMessageIntoDatabase instead!");
        }
        if (message.getMessageDetails() == null) {
            throw new IllegalArgumentException("MessageDetails (getMessageDetails()) are not allowed to be null in message!");
        }
        DomibusConnectorMessage dbMessage = new DomibusConnectorMessage();

        dbMessage.setDirection(
        eu.domibus.connector.persistence.model.enums.MessageDirection.valueOf(direction.name()));
                
        dbMessage.setConversationId(message.getMessageDetails().getConversationId());
        dbMessage.setEbmsMessageId(message.getMessageDetails().getEbmsMessageId());
        dbMessage.setNationalMessageId(message.getMessageDetails().getNationalMessageId());

        try {
            dbMessage = messageDao.save(dbMessage);
        } catch (DuplicateKeyException cve) {
            LOGGER.error("Duplicate key exception occured! The nationalMessageId [{}] or the ebmsMessageId [{}] already exist.", 
                    dbMessage.getNationalMessageId(), dbMessage.getEbmsMessageId());
//            throw new PersistenceException(
//                    "Could not persist message into database. Most likely the nationalMessageId or the ebmsMessageId already exist. ",
//                    cve);
        }

        DomibusConnectorMessageInfo dbMessageInfo = new DomibusConnectorMessageInfo();
        dbMessageInfo.setMessage(dbMessage);
        
        mapMessageDetailsToDbMessageInfoPersistence(message.getMessageDetails(), dbMessageInfo);
        
       
        try {
            this.messageInfoDao.save(dbMessageInfo);
        } catch (Exception e) {
            //throw new PersistenceException("Could not persist message info into database. ", e);
        }

        dbMessage.setMessageInfo(dbMessageInfo);

        message.setDbMessageId(dbMessage.getId());
        message.getMessageDetails().setDbMessageId(dbMessage.getId());
    }

    void mapMessageDetailsToDbMessageInfoPersistence(MessageDetails messageDetails, DomibusConnectorMessageInfo dbMessageInfo) {
        DomibusConnectorAction persistenceAction = this.mapActionToPersistence(messageDetails.getAction());
        dbMessageInfo.setAction(persistenceAction);
                
        DomibusConnectorService persistenceService = this.mapServiceToPersistence(messageDetails.getService());
        dbMessageInfo.setService(persistenceService);
        
        dbMessageInfo.setFinalRecipient(messageDetails.getFinalRecipient());
        dbMessageInfo.setOriginalSender(messageDetails.getOriginalSender());
        
        DomibusConnectorParty from = this.mapPartyToPersistence(messageDetails.getFromParty());        
        dbMessageInfo.setFrom(from);
        DomibusConnectorParty to = this.mapPartyToPersistence(messageDetails.getToParty());
        dbMessageInfo.setTo(to);
    }
    
    @Override
    @Transactional
    public void mergeMessageWithDatabase(Message message) {
        DomibusConnectorMessage dbMessage = messageDao.findOne(message.getDbMessageId());
        
        dbMessage.getMessageInfo();
        
        DomibusConnectorMessageInfo messageInfo = dbMessage.getMessageInfo();
        if (messageInfo == null) {
            messageInfo = new DomibusConnectorMessageInfo();
            dbMessage.setMessageInfo(messageInfo);
        }

        MessageDetails messageDetails = message.getMessageDetails();
        
        if (messageDetails != null) {
            mapMessageDetailsToDbMessageInfoPersistence(message.getMessageDetails(), messageInfo);            
            messageInfoDao.save(messageInfo);
        }

        messageDao.save(dbMessage);
    }

    @Override
    @Transactional
    public void setMessageDeliveredToGateway(Message message) {
        messageDao.setMessageDeliveredToGateway(message.getDbMessageId());
    }

    @Override
    @Transactional
    public void setMessageDeliveredToNationalSystem(Message message) {
        messageDao.setMessageDeliveredToBackend(message.getDbMessageId());
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

        Message message = mapMessageToDomain(dbMessage);

        return message;

    }

    @Override
    @Transactional(readOnly = true)
    public Message findMessageByEbmsId(String ebmsMessageId) {
        DomibusConnectorMessage dbMessage = messageDao.findOneByEbmsMessageId(ebmsMessageId);

        Message message = mapMessageToDomain(dbMessage);

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
            List<Message> messages = new ArrayList<>(dbMessages.size());
            for (DomibusConnectorMessage dbMessage : dbMessages) {
                messages.add(mapMessageToDomain(dbMessage));
            }
            return messages;
        }
        return new ArrayList<>();
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
        if (message == null) {
            throw new IllegalArgumentException("Argument message must be not null! Cannot confirm null!");
        }
        if (message.getDbMessageId() == null) {
            throw new IllegalArgumentException("Message must provide a db message id! But message.getDbMessageId was null!");
        }
        int confirmed = messageDao.confirmMessage(message.getDbMessageId());
        if (confirmed == 1) {
            LOGGER.debug("Message {} successfully confirmed in db", message);
        } else if (confirmed < 1) {            
            throw new RuntimeException("message not confirmed!");
        } else {
            throw new IllegalStateException("Multiple messages confirmed! This should not happen! Maybe DB corrupted? Duplicate IDs?");
        }
        
        
    }

    @Override
    @Transactional
    public void rejectMessage(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Argument message must be not null! Cannot reject null!");
        }
        if (message.getDbMessageId() == null) {
            throw new IllegalArgumentException("Message must provide a db message id! But message.getDbMessageId was null!");
        }
        int rejected = messageDao.rejectMessage(message.getDbMessageId());;
        if (rejected == 1) {
            LOGGER.debug("Message {} successfully marked as rejected in persistence", message);
        } else if (rejected < 1) {            
            throw new RuntimeException("message not confirmed!");
        } else {
            throw new IllegalStateException("Multiple messages marked as rejected! This should not happen! Maybe DB corrupted? Duplicate IDs?");
        }
        
    }

    /**
     * Maps database messages (DomibusConnectorMessage) to the
     * according representation in Domain layer (Message)
     * @param dbMessage - the database message
     * @return - the mapped message
     */
    private Message mapMessageToDomain(DomibusConnectorMessage dbMessage) {
        if (dbMessage == null) {
            return null;
        }
        MessageDetails details = new MessageDetails();
        details.setDbMessageId(dbMessage.getId());
        details.setEbmsMessageId(dbMessage.getEbmsMessageId());
        details.setNationalMessageId(dbMessage.getNationalMessageId());
        details.setConversationId(dbMessage.getConversationId());
        DomibusConnectorMessageInfo messageInfo = dbMessage.getMessageInfo();
        
        if (messageInfo != null) {
            Action action = this.mapActionToDomain(messageInfo.getAction());            
            details.setAction(action);
            
            Service service = this.mapServiceToDomain(messageInfo.getService());
            details.setService(service);
            
            details.setFinalRecipient(messageInfo.getFinalRecipient());
            details.setOriginalSender(messageInfo.getOriginalSender());
            
            Party fromParty = this.mapPartyToDomain(messageInfo.getFrom());
            details.setFromParty(fromParty);
            Party toParty = this.mapPartyToDomain(messageInfo.getTo());
            details.setToParty(toParty);
        }

        Message message = new Message(details);
        message.setDbMessageId(dbMessage.getId());

        for (DomibusConnectorEvidence dbEvidence : dbMessage.getEvidences()) {
            try {
                MessageConfirmation confirmation = mapDbEvidenceToMessageConfirmation(dbEvidence);
                message.addConfirmation(confirmation);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return message;
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
    
    DomibusConnectorAction mapActionToPersistence(Action action) {
        if (action != null) {
            DomibusConnectorAction persistenceAction = new DomibusConnectorAction();
            BeanUtils.copyProperties(action, persistenceAction);
            return persistenceAction;
        }
        return null;
    }
    
    Service mapServiceToDomain(DomibusConnectorService persistenceService) {
        if (persistenceService != null) {
            Service service = new Service();
            BeanUtils.copyProperties(persistenceService, service);
            return service;
        }
        return null;
    }
    
    DomibusConnectorService mapServiceToPersistence(Service service) {
        if (service != null) {
            DomibusConnectorService persistenceService = new DomibusConnectorService();
            BeanUtils.copyProperties(service, persistenceService);
            return persistenceService;
        }
        return null;
    }
    
    
    @Override
    public Service getService(String service) {
        DomibusConnectorService srv = serviceDao.findOne(service);
        return mapServiceToDomain(srv);        
    }

    Party mapPartyToDomain(DomibusConnectorParty persistenceParty) {
        if (persistenceParty != null) {
            Party p = new Party();
            BeanUtils.copyProperties(persistenceParty, p);
            return p;
        }
        return null;
    }
    
    DomibusConnectorParty mapPartyToPersistence(Party party) {
        if (party != null) {
            DomibusConnectorParty persistenceParty = new DomibusConnectorParty();
            BeanUtils.copyProperties(party, persistenceParty);
            return persistenceParty;
        }
        return null;
    }
    
    @Override
    public Party getParty(String partyId, String role) {
        DomibusConnectorPartyPK pk = new DomibusConnectorPartyPK(partyId, role);        
        DomibusConnectorParty party = partyDao.findOne(pk);
        return mapPartyToDomain(party);
    }

    @Override
    public Party getPartyByPartyId(String partyId) {
//        //return partyDao.getPartyByPartyId(partyId);
//        //TODO
//        return new Party();
        DomibusConnectorParty party = partyDao.findOneByPartyId(partyId);
        return mapPartyToDomain(party);
    }

    @Override
    public Action getRelayREMMDAcceptanceRejectionAction() {
        return getAction(RELAY_REMMD_ACCEPTANCE_REJECTION);
    }

    @Override
    public Action getRelayREMMDFailure() {
        return getAction(RELAY_REMMD_FAILURE);
    }

    @Override
    public Action getDeliveryNonDeliveryToRecipientAction() {
        return getAction(DELIVERY_NON_DELIVERY_TO_RECIPIENT);
    }

    @Override
    public Action getRetrievalNonRetrievalToRecipientAction() {
        return getAction(RETRIEVAL_NON_RETRIEVAL_TO_RECIPIENT);
    }

   





}
