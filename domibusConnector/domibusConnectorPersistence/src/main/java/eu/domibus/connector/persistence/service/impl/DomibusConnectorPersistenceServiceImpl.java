package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.persistence.service.impl.loadstore.StoreType;
import eu.domibus.connector.domain.model.DetachedSignatureMimeType;
import eu.domibus.connector.domain.model.DomibusConnectorMessageAttachment;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageContent;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.persistence.dao.DomibusConnectorActionDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorEvidenceDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageErrorDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageInfoDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMsgContDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorPartyDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorServiceDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorAction;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import eu.domibus.connector.persistence.model.PDomibusConnectorEvidence;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessageError;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessageInfo;
import eu.domibus.connector.persistence.model.PDomibusConnectorParty;
import eu.domibus.connector.persistence.model.PDomibusConnectorPartyPK;
import eu.domibus.connector.persistence.model.PDomibusConnectorService;
import eu.domibus.connector.persistence.model.PDomibusConnectorMsgCont;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.persistence.service.PersistenceException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.exception.ExceptionUtils;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

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
    
    @Autowired
    private DomibusConnectorMsgContDao msgContDao;

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
    
    public void setMsgContDao(DomibusConnectorMsgContDao msgContDao) {
        this.msgContDao = msgContDao;
    }
    /*
    * END DAO SETTER
    */
    
    
    @Override
    @Transactional(readOnly = false)
    public void persistMessageIntoDatabase(eu.domibus.connector.domain.model.DomibusConnectorMessage message, eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection direction) throws PersistenceException {
        if (message.getMessageDetails() == null) {
            throw new IllegalArgumentException("MessageDetails (getMessageDetails()) are not allowed to be null in message!");
        }
        PDomibusConnectorMessage dbMessage = new PDomibusConnectorMessage();

        dbMessage.setDirection(eu.domibus.connector.persistence.model.enums.MessageDirection.valueOf(direction.name()));                
        dbMessage.setConversationId(message.getMessageDetails().getConversationId());
        dbMessage.setEbmsMessageId(message.getMessageDetails().getEbmsMessageId());
        dbMessage.setBackendMessageId(message.getMessageDetails().getBackendMessageId());
        dbMessage.setConnectorMessageId(message.getConnectorMessageId());
        
        try {
            dbMessage = messageDao.save(dbMessage);
        } catch (DuplicateKeyException cve) {
            String error = String.format("Message already persisted! The domibusConnectorMessageId [{}] already exist.", 
                    dbMessage.getConnectorMessageId());
            LOGGER.error(error);
            throw new PersistenceException(error, cve);
        }

        PDomibusConnectorMessageInfo dbMessageInfo = new PDomibusConnectorMessageInfo();
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
    }

    void mapMessageDetailsToDbMessageInfoPersistence(eu.domibus.connector.domain.model.DomibusConnectorMessageDetails messageDetails, PDomibusConnectorMessageInfo dbMessageInfo) {
        PDomibusConnectorAction persistenceAction = this.mapActionToPersistence(messageDetails.getAction());
        dbMessageInfo.setAction(persistenceAction);
                
        PDomibusConnectorService persistenceService = this.mapServiceToPersistence(messageDetails.getService());
        dbMessageInfo.setService(persistenceService);
        
        dbMessageInfo.setFinalRecipient(messageDetails.getFinalRecipient());
        dbMessageInfo.setOriginalSender(messageDetails.getOriginalSender());
        
        PDomibusConnectorParty from = this.mapPartyToPersistence(messageDetails.getFromParty());        
        dbMessageInfo.setFrom(from);
        PDomibusConnectorParty to = this.mapPartyToPersistence(messageDetails.getToParty());
        dbMessageInfo.setTo(to);
    }

    
    
    
    void loadMsgContent(final @Nonnull DomibusConnectorMessageBuilder messageBuilder, final PDomibusConnectorMessage dbMessage) throws PersistenceException {
        List<PDomibusConnectorMsgCont> findByMessage = this.msgContDao.findByMessage(dbMessage);
        //map content back
        Optional<PDomibusConnectorMsgCont> findFirst = findByMessage.stream()
                .filter( c -> StoreType.MESSAGE_CONTENT.getDbString().equals(c.getContentType()))
                .findFirst();
        if (findFirst.isPresent()) {
            DomibusConnectorMessageContent messageContent = mapFromMsgCont(findFirst.get(), DomibusConnectorMessageContent.class);
            messageBuilder.setMessageContent(messageContent);
        }
        //map evidence back
        findByMessage.stream()
                .filter( c -> StoreType.MESSAGE_CONFIRMATION.getDbString().equals(c.getContentType()))
                .forEach( c -> {
                    DomibusConnectorMessageConfirmation msgConfirmation = mapFromMsgCont(c, DomibusConnectorMessageConfirmation.class);
                    messageBuilder.addConfirmation(msgConfirmation);
                });
        //map attachments back
        findByMessage.stream()
                .filter( c -> StoreType.MESSAGE_ATTACHMENT.getDbString().equals(c.getContentType()))
                .forEach( c -> {
                    DomibusConnectorMessageAttachment msgAttachment = mapFromMsgCont(c, DomibusConnectorMessageAttachment.class);
                    messageBuilder.addAttachment(msgAttachment);
                });        
    }
    
    /**
     * takes a message and stores all content into the database
     * deletes all old content regarding the message and persists it again in the database
     * 
     * @param message the message
     */
    void storeMsgContent(@Nonnull eu.domibus.connector.domain.model.DomibusConnectorMessage message) throws PersistenceException {
        //handle document
        List<PDomibusConnectorMsgCont> toStoreList = new ArrayList<>();
        DomibusConnectorMessageContent messageContent = message.getMessageContent();
        if (messageContent != null) {
            toStoreList.add(mapToMsgCont(StoreType.MESSAGE_CONTENT, messageContent));
        }
        //handle attachments
        for (DomibusConnectorMessageAttachment attachment : message.getMessageAttachments()) {
            toStoreList.add(mapToMsgCont(StoreType.MESSAGE_ATTACHMENT, attachment));
        }
        //handle confirmations
        for (DomibusConnectorMessageConfirmation c : message.getMessageConfirmations()) {
            toStoreList.add(mapToMsgCont(StoreType.MESSAGE_CONFIRMATION, c));            
        }
        PDomibusConnectorMessage findMessageByMessage = this.findMessageByMessage(message);
        this.msgContDao.deleteByMessage(findMessageByMessage);   //delete old contents
        this.msgContDao.save(toStoreList); //save new contents
    }        
    
    /**
     * Takes a StoreType and a Object
     * maps that object into @see eu.domibus.connector.persistence.model.PDomibusConnectorMsgCont
     * 
     * 
     */
    PDomibusConnectorMsgCont mapToMsgCont(            
            @Nonnull StoreType type, 
            @Nonnull Object object) throws PersistenceException {        
        try {
            PDomibusConnectorMsgCont msgCont = new PDomibusConnectorMsgCont();
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(object);
            byte[] toByteArray = byteOut.toByteArray();
            String md5DigestAsHex = DigestUtils.md5DigestAsHex(toByteArray);
            msgCont.setContentType(type.getDbString());
            msgCont.setChecksum(md5DigestAsHex);
            msgCont.setContent(toByteArray);
            return msgCont;
        } catch (IOException ioe) {
            String error = String.format("storeMsgContent: A error occured during serializing [%s] object [%s] and storing it into database", type, object);
            LOGGER.error(error, ioe);
            throw new PersistenceException(error);
        }        
    }
    
    <T> T mapFromMsgCont(@Nonnull PDomibusConnectorMsgCont msgContent, Class<T> clazz) throws PersistenceException {
        ObjectInputStream inputStream;
        try {
            byte[] byteContent = msgContent.getContent();
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteContent);
            inputStream = new ObjectInputStream(byteInputStream);
            T confirmation = (T) inputStream.readObject();                                    
            return confirmation;            
        } catch (IOException ex) {
            String error = String.format("mapFromMsgCont: IOException occured during reading object out of message content [%s]", msgContent);
            LOGGER.error(error);
            throw new PersistenceException(error, ex);
        } catch (ClassNotFoundException ex) {
            String error = String.format("mapFromMsgCont: Class not found exception occured during reading object out of message content [%s], "
                    + "maybe incompatible updates or corruped database", msgContent);
            LOGGER.error(error);
            throw new PersistenceException(error, ex);
        }
    }
    
    /**
     * Tries to find a message by ebmsId or backendId
     * @param message
     * @return the found message or null
     */
    PDomibusConnectorMessage findMessageByMessage(@Nonnull eu.domibus.connector.domain.model.DomibusConnectorMessage message) {
        String connectorMessageId = message.getConnectorMessageId();
        PDomibusConnectorMessage dbMessage = messageDao.findOneByConnectorMessageId(connectorMessageId);
        if (dbMessage == null) {
            LOGGER.warn("No message found with connector message id [{}] ", connectorMessageId);
        }
        return dbMessage;
    }
    
    
    /**
     * {@inheritDoc }     
     */
    @Override
    @Transactional
    public void mergeMessageWithDatabase(eu.domibus.connector.domain.model.DomibusConnectorMessage message) throws PersistenceException {

        PDomibusConnectorMessage dbMessage = findMessageByMessage(message);
        if (dbMessage == null) {
            String error = String.format("No db message found for domain message %s in storage!\n"
                    + "Can only merge a message wich has already been persisted", message);
            LOGGER.error(error + "\nThrowing exception!");
            throw new PersistenceException(error);
        }
        
        dbMessage.getMessageInfo();
        
        PDomibusConnectorMessageInfo messageInfo = dbMessage.getMessageInfo();
        if (messageInfo == null) {
            messageInfo = new PDomibusConnectorMessageInfo();
            dbMessage.setMessageInfo(messageInfo);
        }

        eu.domibus.connector.domain.model.DomibusConnectorMessageDetails messageDetails = message.getMessageDetails();
        
        if (messageDetails != null) {
            mapMessageDetailsToDbMessageInfoPersistence(message.getMessageDetails(), messageInfo);            
            messageInfoDao.save(messageInfo);
        }

        messageDao.save(dbMessage);
    }

    /**
     * {@inheritDoc }      
     */
    @Override
    @Transactional
    public void setMessageDeliveredToGateway(eu.domibus.connector.domain.model.DomibusConnectorMessage message) {
        PDomibusConnectorMessage dbMessage = findMessageByMessage(message);
        messageDao.setMessageDeliveredToGateway(dbMessage.getId());
    }

    /**
     * {@inheritDoc }      
     */
    @Override
    @Transactional
    public void setMessageDeliveredToNationalSystem(eu.domibus.connector.domain.model.DomibusConnectorMessage message) {
        PDomibusConnectorMessage dbMessage = findMessageByMessage(message);
        messageDao.setMessageDeliveredToBackend(dbMessage.getId());
    }

    /**
     * {@inheritDoc }      
     */
    @Override
    @Transactional
    public void setEvidenceDeliveredToGateway(@Nonnull eu.domibus.connector.domain.model.DomibusConnectorMessage message, @Nonnull eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType evidenceType) throws PersistenceException {
        //messageDao.mergeMessage(message.getDbMessage());
        this.mergeMessageWithDatabase(message);
        PDomibusConnectorMessage dbMessage = findMessageByMessage(message);
        List<PDomibusConnectorEvidence> evidences = evidenceDao.findEvidencesForMessage(dbMessage.getId());
        PDomibusConnectorEvidence dbEvidence = findEvidence(evidences, evidenceType);
        if (dbEvidence != null) {
            evidenceDao.setDeliveredToGateway(dbEvidence.getId());
            //dbEvidence.setDeliveredToGateway(new Date());
            //evidenceDao.save(dbEvidence);
        }
    }

    /**
     * {@inheritDoc }      
     */
    @Override
    @Transactional
    public void setEvidenceDeliveredToNationalSystem(@Nonnull eu.domibus.connector.domain.model.DomibusConnectorMessage message, @Nonnull eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType evidenceType) throws PersistenceException {
//        messageDao.mergeMessage(message.getDbMessage());
        this.mergeMessageWithDatabase(message);
        PDomibusConnectorMessage dbMessage = findMessageByMessage(message);
        List<PDomibusConnectorEvidence> evidences = evidenceDao.findEvidencesForMessage(dbMessage.getId());
        PDomibusConnectorEvidence dbEvidence = findEvidence(evidences, evidenceType);
        if (dbEvidence != null) {
            evidenceDao.setDeliveredToBackend(dbEvidence.getId());
        }
    }

    private @Nullable PDomibusConnectorEvidence findEvidence(@Nonnull List<PDomibusConnectorEvidence> evidences, @Nonnull eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType evidenceType) {
        for (PDomibusConnectorEvidence evidence : evidences) {
            if (evidence.getType().name().equals(evidenceType.name())) {
                return evidence;
            }
        }       
        return null;
    }

    /**
     * {@inheritDoc }      
     */
    @Override
    @Transactional
    public void persistEvidenceForMessageIntoDatabase(eu.domibus.connector.domain.model.DomibusConnectorMessage message, byte[] evidence, eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType evidenceType) {   
        PDomibusConnectorMessage dbMessage = findMessageByMessage(message);
        if (dbMessage == null) {
            throw new IllegalArgumentException("The dbMessage must exist in db!");
        }

        PDomibusConnectorEvidence dbEvidence = new PDomibusConnectorEvidence();
        
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
        PDomibusConnectorMessage dbMessage = messageDao.findOneByBackendMessageId(nationalMessageId);

        eu.domibus.connector.domain.model.DomibusConnectorMessage message = mapMessageToDomain(dbMessage);

        return message;

    }

    @Override
    @Transactional(readOnly = true)
    public eu.domibus.connector.domain.model.DomibusConnectorMessage findMessageByEbmsId(String ebmsMessageId) {
        PDomibusConnectorMessage dbMessage = messageDao.findOneByEbmsMessageId(ebmsMessageId);

        eu.domibus.connector.domain.model.DomibusConnectorMessage message = mapMessageToDomain(dbMessage);

        return message;
    }

    @Override
    @Transactional(readOnly = true)
    public List<eu.domibus.connector.domain.model.DomibusConnectorMessage> findMessagesByConversationId(String conversationId) {
        List<PDomibusConnectorMessage> dbMessages = messageDao.findByConversationId(conversationId);
        return mapDBMessagesToDTO(dbMessages);
    }

	private List<eu.domibus.connector.domain.model.DomibusConnectorMessage> mapDBMessagesToDTO(List<PDomibusConnectorMessage> dbMessages) {        
		if (dbMessages != null && !dbMessages.isEmpty()) {
            List<eu.domibus.connector.domain.model.DomibusConnectorMessage> messages = new ArrayList<>(dbMessages.size());
            for (PDomibusConnectorMessage dbMessage : dbMessages) {
                messages.add(mapMessageToDomain(dbMessage));
            }
            return messages;
        }
        return new ArrayList<>();
	}

    @Override
    @Transactional(readOnly = true)
    public List<eu.domibus.connector.domain.model.DomibusConnectorMessage> findOutgoingUnconfirmedMessages() {
        List<PDomibusConnectorMessage> dbMessages = messageDao.findOutgoingUnconfirmedMessages();
        return mapDBMessagesToDTO(dbMessages);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<eu.domibus.connector.domain.model.DomibusConnectorMessage> findOutgoingMessagesNotRejectedAndWithoutDelivery() {
        List<PDomibusConnectorMessage> dbMessages = messageDao.findOutgoingMessagesNotRejectedAndWithoutDelivery();
        return mapDBMessagesToDTO(dbMessages);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<eu.domibus.connector.domain.model.DomibusConnectorMessage> findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD() {
        List<PDomibusConnectorMessage> dbMessages = messageDao.findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD();
        return mapDBMessagesToDTO(dbMessages);
    }

    @Override
    @Transactional(readOnly = true)
    public List<eu.domibus.connector.domain.model.DomibusConnectorMessage> findIncomingUnconfirmedMessages() {
        List<PDomibusConnectorMessage> dbMessages = messageDao.findIncomingUnconfirmedMessages();
        return mapDBMessagesToDTO(dbMessages);
    }

    @Override
    @Transactional
    public void confirmMessage(eu.domibus.connector.domain.model.DomibusConnectorMessage message) {
        if (message == null) {
            throw new IllegalArgumentException("Argument message must be not null! Cannot confirm null!");
        }
        PDomibusConnectorMessage dbMessage = this.findMessageByMessage(message);
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
        PDomibusConnectorMessage dbMessage = this.findMessageByMessage(message);
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
     * Maps database messages (PDomibusConnectorMessage) to the
 according representation in Domain layer (Message)
     * @param dbMessage - the database message
     * @return - the mapped message
     */
    private eu.domibus.connector.domain.model.DomibusConnectorMessage mapMessageToDomain(PDomibusConnectorMessage dbMessage) {
        if (dbMessage == null) {
            return null;
        }
        DomibusConnectorMessageBuilder messageBuilder = DomibusConnectorMessageBuilder.createBuilder();
        eu.domibus.connector.domain.model.DomibusConnectorMessageDetails details = new eu.domibus.connector.domain.model.DomibusConnectorMessageDetails();
        details.setEbmsMessageId(dbMessage.getEbmsMessageId());
        details.setBackendMessageId(dbMessage.getBackendMessageId());
        details.setConversationId(dbMessage.getConversationId());
        PDomibusConnectorMessageInfo messageInfo = dbMessage.getMessageInfo();
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
        messageBuilder.setMessageDetails(details);
        messageBuilder.setConnectorMessageId(dbMessage.getConnectorMessageId());
        
        

        if (dbMessage.getEvidences() != null) {
            dbMessage.getEvidences().stream()
                    .map( e -> mapDbEvidenceToMessageConfirmation(e))
                    .forEach(e -> messageBuilder.addConfirmation(e));
        }
        
        
        loadMessageContents(dbMessage, messageBuilder);
        
//        //PersistedMessageContent dbMessageContent = dbMessage.getMessageContent();
//        if (dbMessageContent != null) {
//            //if message content is not null:
//           
//             eu.domibus.connector.domain.model.DetachedSignature detachedSignature = 
//                     new eu.domibus.connector.domain.model.DetachedSignature(
//                             dbMessageContent.getDetachedSignature(),
//                             dbMessageContent.getDetachedSignatureName(),
//                             DetachedSignatureMimeType.valueOf(dbMessageContent.getDetachedSignatureMimeType())                             
//                     );
//            
//            
//            eu.domibus.connector.domain.model.DomibusConnectorMessageDocument doc = 
//                    new eu.domibus.connector.domain.model.DomibusConnectorMessageDocument(
//                            dbMessageContent.getDocument(),
//                            dbMessageContent.getDocumentName(),
//                            detachedSignature
//                    );
//            
//            eu.domibus.connector.domain.model.DomibusConnectorMessageContent messageContent = new eu.domibus.connector.domain.model.DomibusConnectorMessageContent();            
//            
//            messageContent.setXmlContent(dbMessageContent.getXmlContent());
//            messageContent.setDocument(doc);
//            
//            //final DomibusConnectorMessageDetails messageDetails, final DomibusConnectorMessageContent messageContent
//            eu.domibus.connector.domain.model.PDomibusConnectorMessage message = 
//                new eu.domibus.connector.domain.model.PDomibusConnectorMessage(
//                        details, 
//                        messageContent
//                );
//            message.getMessageConfirmations().addAll(evidences);
//            return message;
//        } else if (evidences != null && !evidences.isEmpty()) {
//            //if message content is null maybe its an evidence message try to load this way:
//            DomibusConnectorMessageConfirmation evidence = evidences.remove(0); //try with first evidence
//            
//            eu.domibus.connector.domain.model.PDomibusConnectorMessage message = 
//                new eu.domibus.connector.domain.model.PDomibusConnectorMessage(
//                        details, 
//                        evidence
//                );
//                        
//            message.getMessageConfirmations().addAll(evidences);
//            return message;                        
//        } 
        
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = messageBuilder.build();
        return message;
        
//        //message is neither an message with content or an evidence message, illegal state throw exception
//        String error = String.format("Should not end up here! Message content or evidence for message with db id [%s] cannot be found! "
//                + "This message is inconsistent in database!", dbMessage.getId());
//        LOGGER.error(error);
//        throw new RuntimeException(error);
    }

    DomibusConnectorMessageConfirmation mapDbEvidenceToMessageConfirmation(PDomibusConnectorEvidence e) {
        
        return null;
    }
    
    void loadMessageContents(PDomibusConnectorMessage dbMessage, DomibusConnectorMessageBuilder messageBuilder) {
        
    }
    
    


    @Override
    @Transactional
    public void persistMessageError(eu.domibus.connector.domain.model.DomibusConnectorMessageError messageError) {
        PDomibusConnectorMessageError dbError = new PDomibusConnectorMessageError();
        
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
        PDomibusConnectorMessage dbMessage = this.findMessageByMessage(message);
        if (message == null || dbMessage == null) {
            LOGGER.error("persistMessageErrorFromException: failed because ");
            throw new RuntimeException (
                    "Message Error cannot be stored as the message object, or its database reference is null!");
        }
        if (dbMessage == null) {
            throw new RuntimeException(
                String.format("No entry for message [%d] has been found in database! Persist message first!", message));
        }

        PDomibusConnectorMessageError error = new PDomibusConnectorMessageError();
        error.setErrorMessage(ex.getMessage());
        error.setDetailedText(ExceptionUtils.getStackTrace(ex));        
        error.setErrorSource(source.getName());
        error.setMessage(dbMessage);

        this.messageErrorDao.save(error);
    }

    @Override
    public List<eu.domibus.connector.domain.model.DomibusConnectorMessageError> getMessageErrors(eu.domibus.connector.domain.model.DomibusConnectorMessage message) throws PersistenceException {
        PDomibusConnectorMessage dbMessage = this.findMessageByMessage(message);
        if (dbMessage == null) {
            //no message reference
            return new ArrayList<>();
        }
        List<PDomibusConnectorMessageError> dbErrorsForMessage = this.messageErrorDao.findByMessage(dbMessage.getId());
        if (!CollectionUtils.isEmpty(dbErrorsForMessage)) {
            List<eu.domibus.connector.domain.model.DomibusConnectorMessageError> messageErrors = new ArrayList<>(dbErrorsForMessage.size());

            for (PDomibusConnectorMessageError dbMsgError : dbErrorsForMessage) {
                eu.domibus.connector.domain.model.DomibusConnectorMessageError msgError = 
                        new eu.domibus.connector.domain.model.DomibusConnectorMessageError(
                                dbMsgError.getErrorMessage(),
                                dbMsgError.getDetailedText(),
                                dbMsgError.getErrorSource()
                        );
                messageErrors.add(msgError);
            }

            return messageErrors;
        }
        return new ArrayList<>();
    }

    @Override
    public eu.domibus.connector.domain.model.DomibusConnectorAction getAction(String action) {        
        PDomibusConnectorAction findOne = actionDao.findOne(action);
        return mapActionToDomain(findOne);
    }

    eu.domibus.connector.domain.model.DomibusConnectorAction mapActionToDomain(PDomibusConnectorAction persistenceAction) {
        if (persistenceAction != null) {
            eu.domibus.connector.domain.model.DomibusConnectorAction action = 
                    new eu.domibus.connector.domain.model.DomibusConnectorAction(
                            persistenceAction.getAction(),
                            persistenceAction.isDocumentRequired()
                    );
            return action;
        }
        return null;
    }
    
    PDomibusConnectorAction mapActionToPersistence(eu.domibus.connector.domain.model.DomibusConnectorAction action) {
        if (action != null) {
            PDomibusConnectorAction persistenceAction = new PDomibusConnectorAction();
            BeanUtils.copyProperties(action, persistenceAction);        
            return persistenceAction;
        }
        return null;
    }
    
    eu.domibus.connector.domain.model.DomibusConnectorService mapServiceToDomain(PDomibusConnectorService persistenceService) {
        if (persistenceService != null) {
            eu.domibus.connector.domain.model.DomibusConnectorService service = 
                    new eu.domibus.connector.domain.model.DomibusConnectorService(
                            persistenceService.getService(),
                            persistenceService.getServiceType()
                    );
            return service;
        }
        return null;
    }
    
    PDomibusConnectorService mapServiceToPersistence(eu.domibus.connector.domain.model.DomibusConnectorService service) {
        if (service != null) {
            PDomibusConnectorService persistenceService = new PDomibusConnectorService();
            BeanUtils.copyProperties(service, persistenceService);
            return persistenceService;
        }
        return null;
    }
    
    
    @Override
    public eu.domibus.connector.domain.model.DomibusConnectorService getService(String service) {
        PDomibusConnectorService srv = serviceDao.findOne(service);
        return mapServiceToDomain(srv);        
    }

    eu.domibus.connector.domain.model.DomibusConnectorParty mapPartyToDomain(PDomibusConnectorParty persistenceParty) {
        if (persistenceParty != null) {
            eu.domibus.connector.domain.model.DomibusConnectorParty p = 
                    new eu.domibus.connector.domain.model.DomibusConnectorParty(
                            persistenceParty.getPartyId(),
                            persistenceParty.getPartyIdType(),
                            persistenceParty.getRole()
                    );            
            return p;
        }
        return null;
    }
    
    PDomibusConnectorParty mapPartyToPersistence(eu.domibus.connector.domain.model.DomibusConnectorParty party) {
        if (party != null) {
            PDomibusConnectorParty persistenceParty = new PDomibusConnectorParty();
            BeanUtils.copyProperties(party, persistenceParty);
            return persistenceParty;
        }
        return null;
    }
    
    @Override
    public eu.domibus.connector.domain.model.DomibusConnectorParty getParty(String partyId, String role) {
        PDomibusConnectorPartyPK pk = new PDomibusConnectorPartyPK(partyId, role);        
        PDomibusConnectorParty party = partyDao.findOne(pk);
        return mapPartyToDomain(party);
    }

    @Override
    public eu.domibus.connector.domain.model.DomibusConnectorParty getPartyByPartyId(String partyId) {
        PDomibusConnectorParty party = partyDao.findOneByPartyId(partyId);
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
