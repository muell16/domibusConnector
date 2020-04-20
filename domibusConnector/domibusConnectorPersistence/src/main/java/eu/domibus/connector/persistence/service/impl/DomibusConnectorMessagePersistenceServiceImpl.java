package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.persistence.dao.DomibusConnectorEvidenceDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.model.*;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.exceptions.PersistenceException;
import eu.domibus.connector.persistence.service.impl.helper.MessageDirectionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
@org.springframework.stereotype.Service("persistenceService")
public class DomibusConnectorMessagePersistenceServiceImpl implements DomibusConnectorMessagePersistenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorMessagePersistenceServiceImpl.class);

    private DomibusConnectorMessageDao messageDao;
    private DomibusConnectorEvidenceDao evidenceDao;
    private MsgContentPersistenceService msgContentService;
    private InternalEvidencePersistenceService evidencePersistenceService;

    private InternalMessageInfoPersistenceServiceImpl internalMessageInfoPersistenceService;

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
    public void setInternalMessageInfoPersistenceService(InternalMessageInfoPersistenceServiceImpl internalMessageInfoPersistenceService) {
        this.internalMessageInfoPersistenceService = internalMessageInfoPersistenceService;
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
        return mapMessageToDomain(dbMessage);
    }

    @Override
    public Optional<DomibusConnectorMessage> findMessageByEbmsIdAndDirection(String ebmsMessageId, DomibusConnectorMessageDirection messageDirection) {
        return messageDao.findOneByEbmsMessageIdAndDirectionTarget(ebmsMessageId, messageDirection.getTarget())
               .map(this::mapMessageToDomain);
    }


    @Override
    public Optional<DomibusConnectorMessage> findMessageByNationalIdAndDirection(String nationalMessageId, DomibusConnectorMessageDirection messageDirection) {
        return messageDao.findOneByBackendMessageIdAndDirectionTarget(nationalMessageId, messageDirection.getTarget())
                .map(this::mapMessageToDomain);
    }

    @Override
    public Optional<DomibusConnectorMessage> findMessageByEbmsIdOrBackendIdAndDirection(String messageId, DomibusConnectorMessageDirection messageDirection) {
        return messageDao.findOneByEbmsMessageIdOrBackendMessageIdAndDirectionTarget(messageId, messageDirection.getTarget())
                .map(this::mapMessageToDomain);
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
    @Transactional
    public DomibusConnectorMessage persistMessageIntoDatabase(@Nonnull DomibusConnectorMessage message, @Nonnull DomibusConnectorMessageDirection direction) throws PersistenceException {
        if (message.getMessageDetails() == null) {
            throw new IllegalArgumentException("MessageDetails (getMessageDetails()) are not allowed to be null in message!");
        }
        if (message.getConnectorMessageId() == null) {
            throw new IllegalArgumentException("connectorMessageId (getConnectorMessageId()) must be set!");
        }
        if (DomainModelHelper.isEvidenceMessage(message)) {
            LOGGER.debug("#persistMessageIntoDatabase: messge is an evidence message, persisting as evidence!");
            message.getMessageDetails().setDirection(direction);
            message = evidencePersistenceService.persistAsEvidence(message);
            return message;
        }
        message.getMessageDetails().setDirection(direction);

        LOGGER.trace("#persistMessageIntoDatabase: Persist message [{}] with direction [{}] into storage", message, direction);
        PDomibusConnectorMessage dbMessage = new PDomibusConnectorMessage();

        dbMessage.setDirectionSource(direction.getSource());
        dbMessage.setDirectionTarget(direction.getTarget());

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

        this.msgContentService.storeMsgContent(message);
        this.internalMessageInfoPersistenceService.persistMessageInfo(message, dbMessage);

        //TODO: store errors!

        this.persistMessageConfirmationsOfMessage(dbMessage, message.getMessageConfirmations());

        return message;
    }


    void persistMessageConfirmationsOfMessage(final PDomibusConnectorMessage dbMessage, final List<DomibusConnectorMessageConfirmation> confirmations) {
        confirmations.stream().forEach(
                c -> this.evidencePersistenceService.persistToMessage(dbMessage, c)
        );
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
    public DomibusConnectorMessage mergeMessageWithDatabase(@Nonnull DomibusConnectorMessage message) throws PersistenceException {
        if (DomainModelHelper.isEvidenceMessage(message)) {
            LOGGER.debug("#mergeMessageWithDatabase: message is an evidence message, doing nothing!");
            return message;
        }
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
            this.internalMessageInfoPersistenceService.mergeMessageInfo(message, dbMessage);
//            mapMessageDetailsToDbMessageInfoPersistence(message.getMessageDetails(), messageInfo);
//            messageInfoDao.save(messageInfo);
        }

        this.msgContentService.storeMsgContent(message);

        this.messageDao.save(dbMessage);

        //mapMessageInfoIntoMessageDetails(dbMessage, messageDetails);

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
            evidenceDao.setDeliveredToGateway(message.getConnectorMessageId());
        } else {
            dbMessage = messageDao.findOneByConnectorMessageId(message.getConnectorMessageId());
            LOGGER.trace("#setDeliveredToGateway: set connectorId [{}] as delivered in db", message.getConnectorMessageId());
            messageDao.setMessageDeliveredToGateway(dbMessage);
            evidenceDao.setDeliveredToGateway(message.getConnectorMessageId()); //also set the evidences transported with this message as delivered!
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
//            DomibusConnectorMessageConfirmation confirmation = message.getMessageConfirmations().get(0);
//            dbMessage = findByRefToMsg(message);
//            LOGGER.trace("#setMessageDeliveredToNationalSystem: set evidence with type [{}] of message db id [{}] as delivered to national system",
//                    confirmation.getEvidenceType(), dbMessage.getId());
            evidenceDao.setDeliveredToBackend(message.getConnectorMessageId());
        } else {
            LOGGER.trace("#setMessageDeliveredToNationalSystem: set connectorId [{}] as delivered in db", message.getConnectorMessageId());
            dbMessage = messageDao.findOneByConnectorMessageId(message.getConnectorMessageId());
            messageDao.setMessageDeliveredToBackend(dbMessage);
            evidenceDao.setDeliveredToBackend(message.getConnectorMessageId()); //also set the evidences transported with this message as delivered!
        }
    }

    @Override
    @Transactional
    public DomibusConnectorMessage updateMessageDetails(DomibusConnectorMessage message) {
        PDomibusConnectorMessage messageByMessage = this.findMessageByMessage(message);
        this.internalMessageInfoPersistenceService.mergeMessageInfo(message, messageByMessage);
        return message;
    }

//    private void updateMessageDetailsIntoDb(PDomibusConnectorMessage dbMessage, DomibusConnectorMessage message) {
//
//    }


//    PDomibusConnectorMessage findByRefToMsg(@Nonnull DomibusConnectorMessage msg) {
//        String refToMessageId = msg.getMessageDetails().getRefToMessageId();
//        DomibusConnectorMessageDirection direction = msg.getMessageDetails().getDirection();
//
////        PMessageDirection pMessageDirection = MessageDirectionMapper.mapFromDomainToPersistence(direction);
//
//        LOGGER.trace("#findByRefToMsg: find message by reference [{}] for directionTarget [{}] (should be ebmsId or NationalId)", refToMessageId, direction);
//        return messageDao.findOneByEbmsMessageIdOrBackendMessageIdAndDirectionTarget(refToMessageId, direction.getTarget()).get();
//    }


//    @Override
//    @Transactional(readOnly = true)
//    public DomibusConnectorMessage findMessageByNationalId(String nationalMessageId) {
//        PDomibusConnectorMessage dbMessage = messageDao.findOneByBackendMessageId(nationalMessageId);
//        return mapMessageToDomain(dbMessage);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public DomibusConnectorMessage findMessageByEbmsId(String ebmsMessageId) {
//        PDomibusConnectorMessage dbMessage = messageDao.findOneByEbmsMessageId(ebmsMessageId);
//        return mapMessageToDomain(dbMessage);
//    }

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
        details.setDeliveredToBackend(dbMessage.getDeliveredToNationalSystem());
        details.setDeliveredToGateway(dbMessage.getDeliveredToGateway());

        details.setDirection(MessageDirectionMapper.mapFromPersistenceToDomain(dbMessage.getDirectionSource(), dbMessage.getDirectionTarget()));



        details.setFailed(dbMessage.getRejected());

        //mapMessageInfoIntoMessageDetails(dbMessage, details);
        this.internalMessageInfoPersistenceService.mapMessageInfoIntoMessageDetails(dbMessage, details);

        messageBuilder.setMessageDetails(details);
        messageBuilder.setConnectorMessageId(dbMessage.getConnectorMessageId());

        this.msgContentService.loadMessagePayloads(messageBuilder, dbMessage);

        List<DomibusConnectorMessageConfirmation> confirmations = dbMessage.getEvidences().stream()
                .map(MessageConfirmationMapper::mapFromDbToDomain)
                .collect(Collectors.toList());
        messageBuilder.addConfirmations(confirmations);

        return messageBuilder.build();
    }



}
