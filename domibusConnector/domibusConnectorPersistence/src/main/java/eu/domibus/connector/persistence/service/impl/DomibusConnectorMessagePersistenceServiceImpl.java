package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.persistence.dao.DomibusConnectorEvidenceDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageInfoDao;
import eu.domibus.connector.persistence.model.*;
import eu.domibus.connector.persistence.model.enums.MessageDirection;
import eu.domibus.connector.persistence.service.DomibusConnectorEvidencePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.PersistenceException;
import eu.domibus.connector.persistence.service.impl.helper.EvidenceTypeMapper;
import eu.domibus.connector.persistence.service.impl.helper.MessageDirectionMapper;
import eu.domibus.connector.persistence.service.impl.helper.MsgContentPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Service("persistenceService")
public class DomibusConnectorMessagePersistenceServiceImpl implements DomibusConnectorMessagePersistenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorMessagePersistenceServiceImpl.class);

    private DomibusConnectorMessageDao messageDao;
    private DomibusConnectorEvidenceDao evidenceDao;
    private DomibusConnectorMessageInfoDao messageInfoDao;
    private MsgContentPersistenceService msgContentService;
    private InternalEvidencePersistenceService evidencePersistenceService;

    /*
     * DAO SETTER  
     */
    @Autowired
    public void setMessageDao(DomibusConnectorMessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Autowired
    public void setEvidenceDao(DomibusConnectorEvidenceDao evidenceDao) {
        this.evidenceDao = evidenceDao;
    }

    @Autowired
    public void setMessageInfoDao(DomibusConnectorMessageInfoDao messageInfoDao) {
        this.messageInfoDao = messageInfoDao;
    }

    @Autowired
    public void setMsgContentService(MsgContentPersistenceService msgContService) {
        this.msgContentService = msgContService;
    }

    @Autowired
    public void setEvidencePersistenceService(InternalEvidencePersistenceService evidencePersistenceService) {
        this.evidencePersistenceService = evidencePersistenceService;
    }

    /*
    * END DAO SETTER
     */

    @Override
    public DomibusConnectorMessage findMessageByConnectorMessageId(String connectorMessageId) {
        PDomibusConnectorMessage dbMessage = messageDao.findOneByConnectorMessageId(connectorMessageId);
        DomibusConnectorMessage message = mapMessageToDomain(dbMessage);
        return message;
    }

    @Override
    public boolean checkMessageConfirmedOrRejected(DomibusConnectorMessage message) {
        PDomibusConnectorMessage dbMessage = this.findMessageByMessage(message);
        return this.messageDao.checkMessageConfirmedOrRejected(dbMessage.getId());        
    }

    @Override
    public boolean checkMessageRejected(DomibusConnectorMessage message) {
        PDomibusConnectorMessage dbMessage = this.findMessageByMessage(message);
        return this.messageDao.checkMessageRejected(dbMessage.getId());
    }

    @Override
    public boolean checkMessageConfirmed(DomibusConnectorMessage message) {
        PDomibusConnectorMessage dbMessage = this.findMessageByMessage(message);
        return this.messageDao.checkMessageConfirmed(dbMessage.getId());        
    }

    @Override
    @Transactional(readOnly = false)
    public DomibusConnectorMessage persistMessageIntoDatabase(DomibusConnectorMessage message, DomibusConnectorMessageDirection direction) throws PersistenceException {
        if (message.getMessageDetails() == null) {
            throw new IllegalArgumentException("MessageDetails (getMessageDetails()) are not allowed to be null in message!");
        }
        if (message.getConnectorMessageId() == null) {
            throw new IllegalArgumentException("connectorMessageId (getConnectorMessageId()) must be set!");
        }
        if (DomainModelHelper.isEvidenceMessage(message)) {
            LOGGER.debug("#persistMessageIntoDatabase: messge is an evidence message, persisting as evidence!");
            message = evidencePersistenceService.persistAsEvidence(message);
            return message;
        }

        LOGGER.trace("#persistMessageIntoDatabase: Persist message [{}] with direction [{}] into storage", message, direction);
        PDomibusConnectorMessage dbMessage = new PDomibusConnectorMessage();

        dbMessage.setDirection(MessageDirectionMapper.mapFromDomainToPersistence(direction));
        dbMessage.setConversationId(message.getMessageDetails().getConversationId());
        dbMessage.setEbmsMessageId(message.getMessageDetails().getEbmsMessageId());
        dbMessage.setBackendMessageId(message.getMessageDetails().getBackendMessageId());
        dbMessage.setConnectorMessageId(message.getConnectorMessageId());
        dbMessage.setBackendName(message.getMessageDetails().getConnectorBackendClientName());

        try {
            LOGGER.trace("#persistMessageIntoDatabase: Saving message [{}] into storage", dbMessage);
            dbMessage = messageDao.save(dbMessage);
        } catch (DuplicateKeyException cve) {
            String error = String.format("Message already persisted! The domibusConnectorMessageId [%s] already exist.",
                    dbMessage.getConnectorMessageId());
            LOGGER.error(error);
            throw new PersistenceException(error, cve);
        }

        PDomibusConnectorMessageInfo dbMessageInfo = new PDomibusConnectorMessageInfo();
        dbMessageInfo.setMessage(dbMessage);
        dbMessageInfo.setCreated(new Date());
        dbMessageInfo.setUpdated(new Date());
        mapMessageDetailsToDbMessageInfoPersistence(message.getMessageDetails(), dbMessageInfo);

        this.msgContentService.storeMsgContent(message);

        try {
            dbMessageInfo = this.messageInfoDao.save(dbMessageInfo);
        } catch (Exception e) {
            LOGGER.error("Exception occured", e);
            throw new PersistenceException("Could not persist message info into database. ", e);
        }

        dbMessage.setMessageInfo(dbMessageInfo);
        
        return message;
    }



    /**
     * Tries to find a message by domibusConnectorMessageId
     *
     * @param message - the message to which the storage data should be found,
     * message need a domibusConnectorMessageId set
     * @return the found message or null
     */
    PDomibusConnectorMessage findMessageByMessage(@Nonnull DomibusConnectorMessage message) {
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
    public DomibusConnectorMessage mergeMessageWithDatabase(DomibusConnectorMessage message) throws PersistenceException {

        PDomibusConnectorMessage dbMessage = findMessageByMessage(message);
        if (dbMessage == null) {
            String error = String.format("No db message found for domain message %s in storage!%n"
                    + "Can only merge a message wich has already been persisted", message);
            LOGGER.error(error + "\nThrowing exception!");
            throw new PersistenceException(error);
        }


        dbMessage.setConversationId(message.getMessageDetails().getConversationId());
        dbMessage.setEbmsMessageId(message.getMessageDetails().getEbmsMessageId());
        dbMessage.setBackendMessageId(message.getMessageDetails().getBackendMessageId());
        dbMessage.setConnectorMessageId(message.getConnectorMessageId());
        dbMessage.setBackendName(message.getMessageDetails().getConnectorBackendClientName());


        PDomibusConnectorMessageInfo messageInfo = dbMessage.getMessageInfo();
        if (messageInfo == null) {
            messageInfo = new PDomibusConnectorMessageInfo();
            dbMessage.setMessageInfo(messageInfo);
        }

        DomibusConnectorMessageDetails messageDetails = message.getMessageDetails();

        if (messageDetails != null) {
            mapMessageDetailsToDbMessageInfoPersistence(message.getMessageDetails(), messageInfo);
            messageInfoDao.save(messageInfo);
        }

        this.msgContentService.storeMsgContent(message);

        this.messageDao.save(dbMessage);
        
        return message;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Transactional
    public void setDeliveredToGateway(DomibusConnectorMessage message) {
        LOGGER.trace("#setDeliveredToGateway: with message [{}]", message);
        PDomibusConnectorMessage dbMessage;
        if (DomainModelHelper.isEvidenceMessage(message)) {
            DomibusConnectorMessageConfirmation confirmation = message.getMessageConfirmations().get(0);
            dbMessage = findByRefToMsg(message);
            LOGGER.trace("#setDeliveredToGateway: set evidence with type [{}] of message db id [{}] as delivered to gateway",
                    confirmation.getEvidenceType(), dbMessage.getId());
            evidenceDao.setDeliveredToGateway(dbMessage, EvidenceTypeMapper.mapEvidenceTypeFromDomainToDb(confirmation.getEvidenceType()));
        } else {
            dbMessage = messageDao.findOneByConnectorMessageId(message.getConnectorMessageId());
            LOGGER.trace("#setDeliveredToGateway: set connectorId [{}] as delivered in db", message.getConnectorMessageId());
            messageDao.setMessageDeliveredToGateway(dbMessage);
            message.getMessageConfirmations().forEach( (c) -> {
                LOGGER.trace("#setDeliveredToGateway: also set with message {[{}] delivered evidence [{}] to status delivered", message, c);
                evidenceDao.setDeliveredToGateway(dbMessage, EvidenceTypeMapper.mapEvidenceTypeFromDomainToDb(c.getEvidenceType()));
            });
        }

    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Transactional
    public void setMessageDeliveredToNationalSystem(DomibusConnectorMessage message) {
        PDomibusConnectorMessage dbMessage;
        LOGGER.trace("#setMessageDeliveredToNationalSystem: with message [{}]", message);
        if (DomainModelHelper.isEvidenceMessage(message)) {
            DomibusConnectorMessageConfirmation confirmation = message.getMessageConfirmations().get(0);
            dbMessage = findByRefToMsg(message);
            LOGGER.trace("#setMessageDeliveredToNationalSystem: set evidence with type [{}] of message db id [{}] as delivered to national system",
                    confirmation.getEvidenceType(), dbMessage.getId());
            evidenceDao.setDeliveredToBackend(dbMessage, EvidenceTypeMapper.mapEvidenceTypeFromDomainToDb(confirmation.getEvidenceType()));
        } else {
            LOGGER.trace("#setMessageDeliveredToNationalSystem: set connectorId [{}] as delivered in db", message.getConnectorMessageId());
            dbMessage = messageDao.findOneByConnectorMessageId(message.getConnectorMessageId());
            messageDao.setMessageDeliveredToBackend(dbMessage);
            message.getMessageConfirmations().forEach( (c) -> {
                LOGGER.trace("#setMessageDeliveredToNationalSystem: set with message [{}] transported evidence [{}] to status delivered to backend",
                        dbMessage, c);
                evidenceDao.setDeliveredToBackend(dbMessage, EvidenceTypeMapper.mapEvidenceTypeFromDomainToDb(c.getEvidenceType()));
            });
        }
    }

    PDomibusConnectorMessage findByRefToMsg(@Nonnull DomibusConnectorMessage msg) {
        String refToMessageId = msg.getMessageDetails().getRefToMessageId();
        LOGGER.trace("#findByRefToMsg: find message by reference [{}] (should be ebmsId or NationalId)");
        PDomibusConnectorMessage refMsg = messageDao.findOneByEbmsMessageIdOrBackendMessageId(refToMessageId);
        return refMsg;
    }


    @Override
    @Transactional(readOnly = true)
    public DomibusConnectorMessage findMessageByNationalId(String nationalMessageId) {
        PDomibusConnectorMessage dbMessage = messageDao.findOneByBackendMessageId(nationalMessageId);

        DomibusConnectorMessage message = mapMessageToDomain(dbMessage);

        return message;

    }

    @Override
    @Transactional(readOnly = true)
    public DomibusConnectorMessage findMessageByEbmsId(String ebmsMessageId) {
        PDomibusConnectorMessage dbMessage = messageDao.findOneByEbmsMessageId(ebmsMessageId);

        DomibusConnectorMessage message = mapMessageToDomain(dbMessage);

        return message;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomibusConnectorMessage> findMessagesByConversationId(String conversationId) {
        List<PDomibusConnectorMessage> dbMessages = messageDao.findByConversationId(conversationId);
        return mapDBMessagesToDTO(dbMessages);
    }

    private List<DomibusConnectorMessage> mapDBMessagesToDTO(List<PDomibusConnectorMessage> dbMessages) {
        if (dbMessages != null && !dbMessages.isEmpty()) {
            List<DomibusConnectorMessage> messages = new ArrayList<>(dbMessages.size());
            for (PDomibusConnectorMessage dbMessage : dbMessages) {
                messages.add(mapMessageToDomain(dbMessage));
            }
            return messages;
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomibusConnectorMessage> findOutgoingUnconfirmedMessages() {
        List<PDomibusConnectorMessage> dbMessages = messageDao.findOutgoingUnconfirmedMessages();
        return mapDBMessagesToDTO(dbMessages);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomibusConnectorMessage> findOutgoingMessagesNotRejectedAndWithoutDelivery() {
        List<PDomibusConnectorMessage> dbMessages = messageDao.findOutgoingMessagesNotRejectedAndWithoutDelivery();
        return mapDBMessagesToDTO(dbMessages);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomibusConnectorMessage> findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD() {
        List<PDomibusConnectorMessage> dbMessages = messageDao.findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD();
        return mapDBMessagesToDTO(dbMessages);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomibusConnectorMessage> findIncomingUnconfirmedMessages() {
        List<PDomibusConnectorMessage> dbMessages = messageDao.findIncomingUnconfirmedMessages();
        return mapDBMessagesToDTO(dbMessages);
    }

    @Override
    @Transactional
    public void confirmMessage(DomibusConnectorMessage message) {
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
            throw new PersistenceException("message not confirmed!");
        } else {
            throw new IllegalStateException("Multiple messages confirmed! This should not happen! Maybe DB corrupted? Duplicate IDs?");
        }
    }

    @Override
    @Transactional
    public void rejectMessage(DomibusConnectorMessage message) {
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
            throw new PersistenceException("message not confirmed!");
        } else {
            throw new IllegalStateException("Multiple messages marked as rejected! This should not happen! Maybe DB corrupted? Duplicate IDs?");
        }

    }

    /**
     * Maps database messages (PDomibusConnectorMessage) to the according
     * representation in Domain layer (Message)
     *
     * @param dbMessage - the database message
     * @return - the mapped message
     */
    @Nullable
    DomibusConnectorMessage mapMessageToDomain(PDomibusConnectorMessage dbMessage) {
        if (dbMessage == null) {
            return null;
        }
        DomibusConnectorMessageBuilder messageBuilder = DomibusConnectorMessageBuilder.createBuilder();
        DomibusConnectorMessageDetails details = new DomibusConnectorMessageDetails();
        details.setEbmsMessageId(dbMessage.getEbmsMessageId());
        details.setBackendMessageId(dbMessage.getBackendMessageId());
        details.setConversationId(dbMessage.getConversationId());
        details.setConnectorBackendClientName(dbMessage.getBackendName());
        PDomibusConnectorMessageInfo messageInfo = dbMessage.getMessageInfo();
        //details.setRefToMessageId(dbMessage.get);

        if (messageInfo != null) {

            DomibusConnectorAction action = ActionMapper.mapActionToDomain(messageInfo.getAction());
            details.setAction(action);

            DomibusConnectorService service = ServiceMapper.mapServiceToDomain(messageInfo.getService());
            details.setService(service);

            details.setFinalRecipient(messageInfo.getFinalRecipient());
            details.setOriginalSender(messageInfo.getOriginalSender());

            DomibusConnectorParty fromParty = PartyMapper.mapPartyToDomain(messageInfo.getFrom());
            details.setFromParty(fromParty);
            DomibusConnectorParty toParty = PartyMapper.mapPartyToDomain(messageInfo.getTo());
            details.setToParty(toParty);
        }
        messageBuilder.setMessageDetails(details);
        messageBuilder.setConnectorMessageId(dbMessage.getConnectorMessageId());

        this.msgContentService.loadMsgContent(messageBuilder, dbMessage);

        DomibusConnectorMessage message = messageBuilder.build();
        return message;
    }

    void mapMessageDetailsToDbMessageInfoPersistence(DomibusConnectorMessageDetails messageDetails, PDomibusConnectorMessageInfo dbMessageInfo) {
        PDomibusConnectorAction persistenceAction = ActionMapper.mapActionToPersistence(messageDetails.getAction());
        dbMessageInfo.setAction(persistenceAction);

        PDomibusConnectorService persistenceService = ServiceMapper.mapServiceToPersistence(messageDetails.getService());
        dbMessageInfo.setService(persistenceService);

        dbMessageInfo.setFinalRecipient(messageDetails.getFinalRecipient());
        dbMessageInfo.setOriginalSender(messageDetails.getOriginalSender());

        PDomibusConnectorParty from = PartyMapper.mapPartyToPersistence(messageDetails.getFromParty());
        dbMessageInfo.setFrom(from);
        PDomibusConnectorParty to = PartyMapper.mapPartyToPersistence(messageDetails.getToParty());
        dbMessageInfo.setTo(to);
    }

}
