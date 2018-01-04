package eu.domibus.connector.persistence.service.impl;

//import eu.domibus.connector.domain.Action;
//import eu.domibus.connector.domain.Message;
//import eu.domibus.connector.domain.MessageConfirmation;
//import eu.domibus.connector.domain.MessageDetails;
//import eu.domibus.connector.domain.MessageError;
//import eu.domibus.connector.domain.model.Party;
//import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
//import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DetachedSignatureMimeType;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.persistence.dao.DomibusConnectorActionDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorEvidenceDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageErrorDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageInfoDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorPartyDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorServiceDao;
//import eu.domibus.connector.domain.Service;
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
import eu.domibus.connector.persistence.model.PersistedMessageContent;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.persistence.service.PersistenceException;
import java.util.Date;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.exception.ExceptionUtils;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.CollectionUtils;

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
    @Transactional(readOnly = false)  //TODO: rollback for PersistenceException?
    public void persistMessageIntoDatabase(eu.domibus.connector.domain.model.DomibusConnectorMessage message, eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection direction) throws PersistenceException {
//        if (message.getDbMessageId() !=  null) {
//            throw new IllegalArgumentException("Cannot persist (create) a message with an already set db message id! Use mergeMessageIntoDatabase instead!");
//        }
        if (message.getMessageDetails() == null) {
            throw new IllegalArgumentException("MessageDetails (getMessageDetails()) are not allowed to be null in message!");
        }
        DomibusConnectorMessage dbMessage = new DomibusConnectorMessage();

        dbMessage.setDirection(eu.domibus.connector.persistence.model.enums.MessageDirection.valueOf(direction.name()));
                
        dbMessage.setConversationId(message.getMessageDetails().getConversationId());
        dbMessage.setEbmsMessageId(message.getMessageDetails().getEbmsMessageId());
        dbMessage.setBackendMessageId(message.getMessageDetails().getBackendMessageId());

        try {
            dbMessage = messageDao.save(dbMessage);
        } catch (DuplicateKeyException cve) {
            LOGGER.error("Duplicate key exception occured! The nationalMessageId [{}] or the ebmsMessageId [{}] already exist.", 
                    dbMessage.getBackendMessageId(), dbMessage.getEbmsMessageId());
//            throw new PersistenceException(
//                    "Could not persist message into database. Most likely the nationalMessageId or the ebmsMessageId already exist. ",
//                    cve);
        }

        DomibusConnectorMessageInfo dbMessageInfo = new DomibusConnectorMessageInfo();
        dbMessageInfo.setMessage(dbMessage);       
        dbMessageInfo.setCreated(new Date());
        dbMessageInfo.setUpdated(new Date());
        mapMessageDetailsToDbMessageInfoPersistence(message.getMessageDetails(), dbMessageInfo);
        
       
        try {
            dbMessageInfo = this.messageInfoDao.save(dbMessageInfo);
        } catch (Exception e) {
            LOGGER.error("Exception occured", e);
            throw new PersistenceException("Could not persist message info into database. ", e);
        }

        dbMessage.setMessageInfo(dbMessageInfo);       
//        message.setDbMessageId(dbMessage.getId());        
//        message.getMessageDetails().setDbMessageId(dbMessage.getId());
    }

    void mapMessageDetailsToDbMessageInfoPersistence(eu.domibus.connector.domain.model.DomibusConnectorMessageDetails messageDetails, DomibusConnectorMessageInfo dbMessageInfo) {
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
    
    /**
     * Tries to find a message by ebmsId or backendId
     * @param message
     * @return 
     */
    DomibusConnectorMessage findMessageByMessage(eu.domibus.connector.domain.model.DomibusConnectorMessage message) {
        String ebmsMessageId = message.getMessageDetails().getEbmsMessageId();
        String backendId = message.getMessageDetails().getBackendMessageId();
        DomibusConnectorMessage dbMessage = null;
        if (ebmsMessageId != null) {
            dbMessage = messageDao.findOneByEbmsMessageId(ebmsMessageId);
        }
        if (dbMessage == null && backendId != null) {
            dbMessage = messageDao.findOneByBackendMessageId(backendId);
        }        
        LOGGER.warn("No message found with ebmsId [{}] neither with backendMessageId [{}]", ebmsMessageId, backendId);
        return dbMessage;
    }
    
    
    /**
     * Only updates 
     *  - action
     *  - service
     *  - fromParty
     *  - toParty
     *  - finalRecipient
     *  - originalRecipient 
     *  of the provided message details
     * @param message - the message
     * @throws PersistenceException 
     */
    @Override
    @Transactional
    public void mergeMessageWithDatabase(eu.domibus.connector.domain.model.DomibusConnectorMessage message) throws PersistenceException {
//        if (message.getDbMessageId() == null) {
//            throw new IllegalArgumentException("Can only merge a message which has already been persisted!");
//        }
        DomibusConnectorMessage dbMessage = findMessageByMessage(message);
        if (dbMessage == null) {
            String error = String.format("No db message found for domain message %s in storage!", message);
            LOGGER.error(error + "\nThrowing exception!");
            throw new PersistenceException(error);
        }
        
        dbMessage.getMessageInfo();
        
        DomibusConnectorMessageInfo messageInfo = dbMessage.getMessageInfo();
        if (messageInfo == null) {
            messageInfo = new DomibusConnectorMessageInfo();
            dbMessage.setMessageInfo(messageInfo);
        }

        eu.domibus.connector.domain.model.DomibusConnectorMessageDetails messageDetails = message.getMessageDetails();
        
        if (messageDetails != null) {
            mapMessageDetailsToDbMessageInfoPersistence(message.getMessageDetails(), messageInfo);            
            messageInfoDao.save(messageInfo);
        }

        messageDao.save(dbMessage);
    }

    @Override
    @Transactional
    public void setMessageDeliveredToGateway(eu.domibus.connector.domain.model.DomibusConnectorMessage message) {
        DomibusConnectorMessage dbMessage = findMessageByMessage(message);
        messageDao.setMessageDeliveredToGateway(dbMessage.getId());
    }

    @Override
    @Transactional
    public void setMessageDeliveredToNationalSystem(eu.domibus.connector.domain.model.DomibusConnectorMessage message) {
        DomibusConnectorMessage dbMessage = findMessageByMessage(message);
        messageDao.setMessageDeliveredToBackend(dbMessage.getId());
    }

    @Override
    @Transactional
    public void setEvidenceDeliveredToGateway(@Nonnull eu.domibus.connector.domain.model.DomibusConnectorMessage message, @Nonnull eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType evidenceType) throws PersistenceException {
        //messageDao.mergeMessage(message.getDbMessage());
        this.mergeMessageWithDatabase(message);
        DomibusConnectorMessage dbMessage = findMessageByMessage(message);
        List<DomibusConnectorEvidence> evidences = evidenceDao.findEvidencesForMessage(dbMessage.getId());
        DomibusConnectorEvidence dbEvidence = findEvidence(evidences, evidenceType);
        if (dbEvidence != null) {
            evidenceDao.setDeliveredToGateway(dbEvidence.getId());
            //dbEvidence.setDeliveredToGateway(new Date());
            //evidenceDao.save(dbEvidence);
        }
    }

    @Override
    @Transactional
    public void setEvidenceDeliveredToNationalSystem(@Nonnull eu.domibus.connector.domain.model.DomibusConnectorMessage message, @Nonnull eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType evidenceType) throws PersistenceException {
//        messageDao.mergeMessage(message.getDbMessage());
        this.mergeMessageWithDatabase(message);
        DomibusConnectorMessage dbMessage = findMessageByMessage(message);
        List<DomibusConnectorEvidence> evidences = evidenceDao.findEvidencesForMessage(dbMessage.getId());
        DomibusConnectorEvidence dbEvidence = findEvidence(evidences, evidenceType);
        if (dbEvidence != null) {
//            dbEvidence.setDeliveredToNationalSystem(new Date());
//            evidenceDao.mergeEvidence(dbEvidence);
            evidenceDao.setDeliveredToBackend(dbEvidence.getId());
        }
    }

    private @Nullable DomibusConnectorEvidence findEvidence(@Nonnull List<DomibusConnectorEvidence> evidences, @Nonnull eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType evidenceType) {
        for (DomibusConnectorEvidence evidence : evidences) {
            if (evidence.getType().name().equals(evidenceType.name())) {
                return evidence;
            }
        }       
        return null;
    }

    @Override
    @Transactional
    public void persistEvidenceForMessageIntoDatabase(eu.domibus.connector.domain.model.DomibusConnectorMessage message, byte[] evidence, eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType evidenceType) {   
        DomibusConnectorMessage dbMessage = findMessageByMessage(message);
        if (dbMessage == null) {
            throw new IllegalArgumentException("The dbMessage must exist in db!");
        }

        DomibusConnectorEvidence dbEvidence = new DomibusConnectorEvidence();
        
        //DomibusConnectorMessage dbMessage = messageDao.findOne(message.getDbMessageId());
        if (dbMessage == null) {
            throw new IllegalStateException(String.format("The provided message [%d] does not exist in storage!", message));
        }
        
        dbEvidence.setMessage(dbMessage);
        dbEvidence.setEvidence(new String(evidence));
        dbEvidence.setType(eu.domibus.connector.persistence.model.enums.EvidenceType.valueOf(evidenceType.name()));
        dbEvidence.setDeliveredToGateway(null);
        dbEvidence.setDeliveredToNationalSystem(null);
        evidenceDao.save(dbEvidence);
    }

    @Override
    @Transactional(readOnly = true)
    public eu.domibus.connector.domain.model.DomibusConnectorMessage findMessageByNationalId(String nationalMessageId) {
        DomibusConnectorMessage dbMessage = messageDao.findOneByBackendMessageId(nationalMessageId);

        eu.domibus.connector.domain.model.DomibusConnectorMessage message = mapMessageToDomain(dbMessage);

        return message;

    }

    @Override
    @Transactional(readOnly = true)
    public eu.domibus.connector.domain.model.DomibusConnectorMessage findMessageByEbmsId(String ebmsMessageId) {
        DomibusConnectorMessage dbMessage = messageDao.findOneByEbmsMessageId(ebmsMessageId);

        eu.domibus.connector.domain.model.DomibusConnectorMessage message = mapMessageToDomain(dbMessage);

        return message;
    }

    @Override
    @Transactional(readOnly = true)
    public List<eu.domibus.connector.domain.model.DomibusConnectorMessage> findMessagesByConversationId(String conversationId) {
        List<DomibusConnectorMessage> dbMessages = messageDao.findByConversationId(conversationId);
        return mapDBMessagesToDTO(dbMessages);
    }

	private List<eu.domibus.connector.domain.model.DomibusConnectorMessage> mapDBMessagesToDTO(List<DomibusConnectorMessage> dbMessages) {        
		if (dbMessages != null && !dbMessages.isEmpty()) {
            List<eu.domibus.connector.domain.model.DomibusConnectorMessage> messages = new ArrayList<>(dbMessages.size());
            for (DomibusConnectorMessage dbMessage : dbMessages) {
                messages.add(mapMessageToDomain(dbMessage));
            }
            return messages;
        }
        return new ArrayList<>();
	}

    @Override
    @Transactional(readOnly = true)
    public List<eu.domibus.connector.domain.model.DomibusConnectorMessage> findOutgoingUnconfirmedMessages() {
        List<DomibusConnectorMessage> dbMessages = messageDao.findOutgoingUnconfirmedMessages();
        return mapDBMessagesToDTO(dbMessages);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<eu.domibus.connector.domain.model.DomibusConnectorMessage> findOutgoingMessagesNotRejectedAndWithoutDelivery() {
        List<DomibusConnectorMessage> dbMessages = messageDao.findOutgoingMessagesNotRejectedAndWithoutDelivery();
        return mapDBMessagesToDTO(dbMessages);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<eu.domibus.connector.domain.model.DomibusConnectorMessage> findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD() {
        List<DomibusConnectorMessage> dbMessages = messageDao.findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD();
        return mapDBMessagesToDTO(dbMessages);
    }

    @Override
    @Transactional(readOnly = true)
    public List<eu.domibus.connector.domain.model.DomibusConnectorMessage> findIncomingUnconfirmedMessages() {
        List<DomibusConnectorMessage> dbMessages = messageDao.findIncomingUnconfirmedMessages();
        return mapDBMessagesToDTO(dbMessages);
    }

    @Override
    @Transactional
    public void confirmMessage(eu.domibus.connector.domain.model.DomibusConnectorMessage message) {
        if (message == null) {
            throw new IllegalArgumentException("Argument message must be not null! Cannot confirm null!");
        }
        DomibusConnectorMessage dbMessage = this.findMessageByMessage(message);
        if (dbMessage == null) {
            throw new IllegalArgumentException("Message must be already persisted to database! Call persistMessageIntoDatabase first");
        }
        int confirmed = messageDao.confirmMessage(dbMessage.getId());
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
    public void rejectMessage(eu.domibus.connector.domain.model.DomibusConnectorMessage message) {
        if (message == null) {
            throw new IllegalArgumentException("Argument message must be not null! Cannot reject null!");
        }
        DomibusConnectorMessage dbMessage = this.findMessageByMessage(message);
        if (dbMessage == null) {
            throw new IllegalArgumentException("Message must be already persisted to database! Call persistMessageIntoDatabase message first!");
        }
        int rejected = messageDao.rejectMessage(dbMessage.getId());
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
    private eu.domibus.connector.domain.model.DomibusConnectorMessage mapMessageToDomain(DomibusConnectorMessage dbMessage) {
        if (dbMessage == null) {
            return null;
        }
        eu.domibus.connector.domain.model.DomibusConnectorMessageDetails details = new eu.domibus.connector.domain.model.DomibusConnectorMessageDetails();
        //details.setDbMessageId(dbMessage.getId());
        details.setEbmsMessageId(dbMessage.getEbmsMessageId());
        //details.setBackendMessageId(dbMessage.getBackendMessageId());
        details.setBackendMessageId(dbMessage.getBackendMessageId());
        details.setConversationId(dbMessage.getConversationId());
        DomibusConnectorMessageInfo messageInfo = dbMessage.getMessageInfo();
        details.setRefToMessageId(RELAY_REMMD_FAILURE);
        
        
        if (messageInfo != null) {
            eu.domibus.connector.domain.model.DomibusConnectorAction action = this.mapActionToDomain(messageInfo.getAction());            
            details.setAction(action);
            
            eu.domibus.connector.domain.model.DomibusConnectorService service = this.mapServiceToDomain(messageInfo.getService());
            details.setService(service);
            
            details.setFinalRecipient(messageInfo.getFinalRecipient());
            details.setOriginalSender(messageInfo.getOriginalSender());
            
            eu.domibus.connector.domain.model.DomibusConnectorParty fromParty = this.mapPartyToDomain(messageInfo.getFrom());
            details.setFromParty(fromParty);
            eu.domibus.connector.domain.model.DomibusConnectorParty toParty = this.mapPartyToDomain(messageInfo.getTo());
            details.setToParty(toParty);
        }
        
        List<DomibusConnectorMessageConfirmation> evidences = null;
        if (dbMessage.getEvidences() != null) {
            evidences = dbMessage.getEvidences().stream()
                    .map( e -> mapDbEvidenceToMessageConfirmation(e) ).collect(Collectors.toList());
        }
        
        
        PersistedMessageContent dbMessageContent = dbMessage.getMessageContent();
        if (dbMessageContent != null) {
            //if message content is not null:
           
             eu.domibus.connector.domain.model.DetachedSignature detachedSignature = 
                     new eu.domibus.connector.domain.model.DetachedSignature(
                             dbMessageContent.getDetachedSignature(),
                             dbMessageContent.getDetachedSignatureName(),
                             DetachedSignatureMimeType.valueOf(dbMessageContent.getDetachedSignatureMimeType())                             
                     );
            
            
            eu.domibus.connector.domain.model.DomibusConnectorMessageDocument doc = 
                    new eu.domibus.connector.domain.model.DomibusConnectorMessageDocument(
                            dbMessageContent.getDocument(),
                            dbMessageContent.getDocumentName(),
                            detachedSignature
                    );
            
            eu.domibus.connector.domain.model.DomibusConnectorMessageContent messageContent = new eu.domibus.connector.domain.model.DomibusConnectorMessageContent();            
            
            messageContent.setXmlContent(dbMessageContent.getXmlContent());
            messageContent.setDocument(doc);
            
            //final DomibusConnectorMessageDetails messageDetails, final DomibusConnectorMessageContent messageContent
            eu.domibus.connector.domain.model.DomibusConnectorMessage message = 
                new eu.domibus.connector.domain.model.DomibusConnectorMessage(
                        details, 
                        messageContent
                );
            message.getMessageConfirmations().addAll(evidences);
            return message;
        } else if (evidences != null && !evidences.isEmpty()) {
            //if message content is null maybe its an evidence message try to load this way:
            DomibusConnectorMessageConfirmation evidence = evidences.remove(0); //try with first evidence
            
            eu.domibus.connector.domain.model.DomibusConnectorMessage message = 
                new eu.domibus.connector.domain.model.DomibusConnectorMessage(
                        details, 
                        evidence
                );
                        
            message.getMessageConfirmations().addAll(evidences);
            return message;                        
        } 
        //message is neither an message with content or an evidence message, illegal state throw exception
        String error = String.format("Should not end up here! Message content or evidence for message with db id [%s] cannot be found! "
                + "This message is inconsistent in database!", dbMessage.getId());
        LOGGER.error(error);
        throw new RuntimeException(error);
    }

    private eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation mapDbEvidenceToMessageConfirmation(DomibusConnectorEvidence dbEvidence) {
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
    public void persistMessageError(eu.domibus.connector.domain.model.DomibusConnectorMessageError messageError) {
        DomibusConnectorMessageError dbError = new DomibusConnectorMessageError();
//        dbError.setMessage(messageError.getMessage().getDbMessage());
        dbError.setErrorMessage(messageError.getText());
        dbError.setDetailedText(messageError.getDetails());
        dbError.setErrorSource(messageError.getSource());

        this.messageErrorDao.save(dbError);
    }

    @Override
    @Transactional
    public void persistMessageErrorFromException(eu.domibus.connector.domain.model.DomibusConnectorMessage message, Throwable ex, Class<?> source) {   
        if (message == null) {
            throw new IllegalArgumentException("message is not allowed to be null!");
        }
        if (ex == null) {
            throw new IllegalArgumentException("Message Error cannot be stored as there is no exception given!");
        }
        if (source == null) {
            throw new IllegalArgumentException(
                    "Message Error cannot be stored as the Class object given as source is null!");
        }
        DomibusConnectorMessage dbMessage = this.findMessageByMessage(message);
        if (message == null || dbMessage == null) {
            LOGGER.error("persistMessageErrorFromException: failed because ");
            throw new RuntimeException (
                    "Message Error cannot be stored as the message object, or its database reference is null!");
        }
        if (dbMessage == null) {
            throw new RuntimeException(
                String.format("No entry for message [%d] has been found in database! Persist message first!", message));
        }

        DomibusConnectorMessageError error = new DomibusConnectorMessageError();
        error.setErrorMessage(ex.getMessage());
        error.setDetailedText(ExceptionUtils.getStackTrace(ex));        
        error.setErrorSource(source.getName());
        error.setMessage(dbMessage);

        this.messageErrorDao.save(error);
    }

    @Override
    public List<eu.domibus.connector.domain.model.DomibusConnectorMessageError> getMessageErrors(eu.domibus.connector.domain.model.DomibusConnectorMessage message) throws Exception {
        DomibusConnectorMessage dbMessage = this.findMessageByMessage(message);
        if (dbMessage == null) {
            //no message reference
            return new ArrayList<>();
        }
        List<DomibusConnectorMessageError> dbErrorsForMessage = this.messageErrorDao.findByMessage(dbMessage.getId());
        if (!CollectionUtils.isEmpty(dbErrorsForMessage)) {
            List<eu.domibus.connector.domain.model.DomibusConnectorMessageError> messageErrors = new ArrayList<>(dbErrorsForMessage.size());

            for (DomibusConnectorMessageError dbMsgError : dbErrorsForMessage) {
                eu.domibus.connector.domain.model.DomibusConnectorMessageError msgError = 
                        new eu.domibus.connector.domain.model.DomibusConnectorMessageError(
                                dbMsgError.getErrorMessage(),
                                dbMsgError.getDetailedText(),
                                dbMsgError.getErrorSource()
                        );
//                msgError.setMessage(message);
//                msgError.setText(dbMsgError.getErrorMessage());
//                if (dbMsgError.getDetailedText() != null)
//                    msgError.setDetails(dbMsgError.getDetailedText());
//                msgError.setSource(dbMsgError.getErrorSource());

                messageErrors.add(msgError);
            }

            return messageErrors;
        }
        return new ArrayList<>();
    }

    @Override
    public eu.domibus.connector.domain.model.DomibusConnectorAction getAction(String action) {
        
        DomibusConnectorAction findOne = actionDao.findOne(action);

        return mapActionToDomain(findOne);
    }

    eu.domibus.connector.domain.model.DomibusConnectorAction mapActionToDomain(DomibusConnectorAction persistenceAction) {
        if (persistenceAction != null) {
            eu.domibus.connector.domain.model.DomibusConnectorAction action = 
                    new eu.domibus.connector.domain.model.DomibusConnectorAction(
                            persistenceAction.getAction(),
                            persistenceAction.isDocumentRequired()
                    );
//            BeanUtils.copyProperties(persistenceAction, action);
            return action;
        }
        return null;
    }
    
    DomibusConnectorAction mapActionToPersistence(eu.domibus.connector.domain.model.DomibusConnectorAction action) {
        if (action != null) {
            DomibusConnectorAction persistenceAction = new DomibusConnectorAction();
            BeanUtils.copyProperties(action, persistenceAction);        
            return persistenceAction;
        }
        return null;
    }
    
    eu.domibus.connector.domain.model.DomibusConnectorService mapServiceToDomain(DomibusConnectorService persistenceService) {
        if (persistenceService != null) {
            eu.domibus.connector.domain.model.DomibusConnectorService service = 
                    new eu.domibus.connector.domain.model.DomibusConnectorService(
                            persistenceService.getService(),
                            persistenceService.getServiceType()
                    );
//            BeanUtils.copyProperties(persistenceService, service);
            return service;
        }
        return null;
    }
    
    DomibusConnectorService mapServiceToPersistence(eu.domibus.connector.domain.model.DomibusConnectorService service) {
        if (service != null) {
            DomibusConnectorService persistenceService = new DomibusConnectorService();
            BeanUtils.copyProperties(service, persistenceService);
            return persistenceService;
        }
        return null;
    }
    
    
    @Override
    public eu.domibus.connector.domain.model.DomibusConnectorService getService(String service) {
        DomibusConnectorService srv = serviceDao.findOne(service);
        return mapServiceToDomain(srv);        
    }

    eu.domibus.connector.domain.model.DomibusConnectorParty mapPartyToDomain(DomibusConnectorParty persistenceParty) {
        if (persistenceParty != null) {
            eu.domibus.connector.domain.model.DomibusConnectorParty p = 
                    new eu.domibus.connector.domain.model.DomibusConnectorParty(
                            persistenceParty.getPartyId(),
                            persistenceParty.getPartyIdType(),
                            persistenceParty.getRole()
                    );
            //BeanUtils.copyProperties(persistenceParty, p);
            return p;
        }
        return null;
    }
    
    DomibusConnectorParty mapPartyToPersistence(eu.domibus.connector.domain.model.DomibusConnectorParty party) {
        if (party != null) {
            DomibusConnectorParty persistenceParty = new DomibusConnectorParty();
            BeanUtils.copyProperties(party, persistenceParty);
            return persistenceParty;
        }
        return null;
    }
    
    @Override
    public eu.domibus.connector.domain.model.DomibusConnectorParty getParty(String partyId, String role) {
        DomibusConnectorPartyPK pk = new DomibusConnectorPartyPK(partyId, role);        
        DomibusConnectorParty party = partyDao.findOne(pk);
        return mapPartyToDomain(party);
    }

    @Override
    public eu.domibus.connector.domain.model.DomibusConnectorParty getPartyByPartyId(String partyId) {
//        //return partyDao.getPartyByPartyId(partyId);
//        //TODO
//        return new Party();
        DomibusConnectorParty party = partyDao.findOneByPartyId(partyId);
        return mapPartyToDomain(party);
    }

    @Override
    public eu.domibus.connector.domain.model.DomibusConnectorAction getRelayREMMDAcceptanceRejectionAction() {
        return getAction(RELAY_REMMD_ACCEPTANCE_REJECTION);
    }

    @Override
    public eu.domibus.connector.domain.model.DomibusConnectorAction getRelayREMMDFailure() {
        return getAction(RELAY_REMMD_FAILURE);
    }

    @Override
    public eu.domibus.connector.domain.model.DomibusConnectorAction getDeliveryNonDeliveryToRecipientAction() {
        return getAction(DELIVERY_NON_DELIVERY_TO_RECIPIENT);
    }

    @Override
    public eu.domibus.connector.domain.model.DomibusConnectorAction getRetrievalNonRetrievalToRecipientAction() {
        return getAction(RETRIEVAL_NON_RETRIEVAL_TO_RECIPIENT);
    }

   





}
