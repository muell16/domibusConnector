package eu.domibus.connector.common.db.service.impl;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.lob.ClobImpl;
import org.springframework.transaction.annotation.Transactional;

import eu.domibus.connector.common.db.dao.DomibusConnectorActionDao;
import eu.domibus.connector.common.db.dao.DomibusConnectorEvidenceDao;
import eu.domibus.connector.common.db.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.common.db.dao.DomibusConnectorMessageInfoDao;
import eu.domibus.connector.common.db.dao.DomibusConnectorPartyDao;
import eu.domibus.connector.common.db.dao.DomibusConnectorServiceDao;
import eu.domibus.connector.common.db.model.DomibusConnectorAction;
import eu.domibus.connector.common.db.model.DomibusConnectorEvidence;
import eu.domibus.connector.common.db.model.DomibusConnectorMessage;
import eu.domibus.connector.common.db.model.DomibusConnectorMessageInfo;
import eu.domibus.connector.common.db.model.DomibusConnectorParty;
import eu.domibus.connector.common.db.model.DomibusConnectorService;
import eu.domibus.connector.common.db.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.common.enums.EvidenceType;
import eu.domibus.connector.common.enums.MessageDirection;
import eu.domibus.connector.common.exception.PersistenceException;
import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.common.message.MessageConfirmation;
import eu.domibus.connector.common.message.MessageDetails;

public class DomibusConnectorPersistenceServiceImpl implements DomibusConnectorPersistenceService {

    private static final String RETRIEVAL_NON_RETRIEVAL_TO_RECIPIENT = "RetrievalNonRetrievalToRecipient";
    private static final String DELIVERY_NON_DELIVERY_TO_RECIPIENT = "DeliveryNonDeliveryToRecipient";
    private static final String RELAY_REMMD_FAILURE = "RelayREMMDFailure";
    private static final String RELAY_REMMD_ACCEPTANCE_REJECTION = "RelayREMMDAcceptanceRejection";

    private DomibusConnectorMessageDao messageDao;
    private DomibusConnectorEvidenceDao evidenceDao;
    private DomibusConnectorActionDao actionDao;
    private DomibusConnectorServiceDao serviceDao;
    private DomibusConnectorPartyDao partyDao;
    private DomibusConnectorMessageInfoDao messageInfoDao;

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

    @Override
    @Transactional
    public void persistMessageIntoDatabase(Message message, MessageDirection direction)
            throws PersistenceException {
        DomibusConnectorMessage dbMessage = new DomibusConnectorMessage();

        dbMessage.setDirection(direction);
        dbMessage.setConversationId(message.getMessageDetails().getConversationId());
        dbMessage.setEbmsMessageId(message.getMessageDetails().getEbmsMessageId());
        dbMessage.setNationalMessageId(message.getMessageDetails().getNationalMessageId());

        try {
            messageDao.saveNewMessage(dbMessage);
        } catch (Exception cve) {
            throw new PersistenceException(
                    "Could not persist message into database. Most likely the nationalMessageId or the ebmsMessageId already exist. ",
                    cve);
        }

        DomibusConnectorMessageInfo dbMessageInfo = new DomibusConnectorMessageInfo();
        dbMessageInfo.setMessage(dbMessage);
        dbMessageInfo.setAction(message.getMessageDetails().getAction());
        dbMessageInfo.setService(message.getMessageDetails().getService());
        dbMessageInfo.setFinalRecipient(message.getMessageDetails().getFinalRecipient());
        dbMessageInfo.setOriginalSender(message.getMessageDetails().getOriginalSender());
        dbMessageInfo.setFrom(message.getMessageDetails().getFromParty());
        dbMessageInfo.setTo(message.getMessageDetails().getToParty());

        try {
            messageInfoDao.persistMessageInfo(dbMessageInfo);
        } catch (Exception e) {
            throw new PersistenceException("Could not persist message info into database. ", e);
        }

        dbMessage.setMessageInfo(dbMessageInfo);

        message.setDbMessage(dbMessage);
        message.getMessageDetails().setDbMessageId(dbMessage.getId());
    }

    @Override
    @Transactional
    public void mergeMessageWithDatabase(Message message) {

        messageDao.mergeMessage(message.getDbMessage());

        DomibusConnectorMessageInfo messageInfo = message.getDbMessage().getMessageInfo();

        if (messageInfo != null) {
            messageInfo.setAction(message.getMessageDetails().getAction());
            messageInfo.setService(message.getMessageDetails().getService());
            messageInfo.setFrom(message.getMessageDetails().getFromParty());
            messageInfo.setTo(message.getMessageDetails().getToParty());
            messageInfo.setFinalRecipient(message.getMessageDetails().getFinalRecipient());
            messageInfo.setOriginalSender(message.getMessageDetails().getOriginalSender());

            messageInfoDao.mergeMessageInfo(messageInfo);
        }
    }

    @Override
    @Transactional
    public void setMessageDeliveredToGateway(Message message) {
        DomibusConnectorMessage dbMessage = messageDao.mergeMessage(message.getDbMessage());
        dbMessage.setDeliveredToGateway(new Date());
        dbMessage = messageDao.mergeMessage(dbMessage);
        message.setDbMessage(dbMessage);
    }

    @Override
    @Transactional
    public void setMessageDeliveredToNationalSystem(Message message) {
        DomibusConnectorMessage dbMessage = messageDao.mergeMessage(message.getDbMessage());
        dbMessage.setDeliveredToNationalSystem(new Date());
        dbMessage = messageDao.mergeMessage(dbMessage);
        message.setDbMessage(dbMessage);
    }

    @Override
    @Transactional
    public void setEvidenceDeliveredToGateway(Message message, EvidenceType evidenceType) {
        messageDao.mergeMessage(message.getDbMessage());
        List<DomibusConnectorEvidence> evidences = evidenceDao.findEvidencesForMessage(message.getDbMessage());
        DomibusConnectorEvidence dbEvidence = findEvidence(evidences, evidenceType);
        if (dbEvidence != null) {
            dbEvidence.setDeliveredToGateway(new Date());
            evidenceDao.mergeEvidence(dbEvidence);
        }
    }

    @Override
    @Transactional
    public void setEvidenceDeliveredToNationalSystem(Message message, EvidenceType evidenceType) {
        messageDao.mergeMessage(message.getDbMessage());
        List<DomibusConnectorEvidence> evidences = evidenceDao.findEvidencesForMessage(message.getDbMessage());
        DomibusConnectorEvidence dbEvidence = findEvidence(evidences, evidenceType);
        if (dbEvidence != null) {
            dbEvidence.setDeliveredToNationalSystem(new Date());
            evidenceDao.mergeEvidence(dbEvidence);
        }
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

        dbEvidence.setMessage(message.getDbMessage());
        Clob cEvidence = new ClobImpl(new String(evidence));
        dbEvidence.setEvidence(cEvidence);
        dbEvidence.setType(evidenceType);
        dbEvidence.setDeliveredToGateway(null);
        dbEvidence.setDeliveredToNationalSystem(null);

        evidenceDao.saveNewEvidence(dbEvidence);
    }

    @Override
    @Transactional(readOnly = true)
    public Message findMessageByNationalId(String nationalMessageId) {
        DomibusConnectorMessage dbMessage = messageDao.findMessageByNationalId(nationalMessageId);

        Message message = mapDbMessageToMessage(dbMessage);

        return message;

    }

    @Override
    @Transactional(readOnly = true)
    public Message findMessageByEbmsId(String ebmsMessageId) {
        DomibusConnectorMessage dbMessage = messageDao.findMessageByEbmsId(ebmsMessageId);

        Message message = mapDbMessageToMessage(dbMessage);

        return message;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> findMessagesByConversationId(String conversationId) {
        List<DomibusConnectorMessage> dbMessages = messageDao.findMessagesByConversationId(conversationId);
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
        if (dbMessages != null && !dbMessages.isEmpty()) {
            List<Message> unconfirmedMessages = new ArrayList<Message>(dbMessages.size());
            for (DomibusConnectorMessage dbMessage : dbMessages) {
                unconfirmedMessages.add(mapDbMessageToMessage(dbMessage));
            }
            return unconfirmedMessages;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> findIncomingUnconfirmedMessages() {
        List<DomibusConnectorMessage> dbMessages = messageDao.findIncomingUnconfirmedMessages();
        if (dbMessages != null && !dbMessages.isEmpty()) {
            List<Message> unconfirmedMessages = new ArrayList<Message>(dbMessages.size());
            for (DomibusConnectorMessage dbMessage : dbMessages) {
                unconfirmedMessages.add(mapDbMessageToMessage(dbMessage));
            }
            return unconfirmedMessages;
        }
        return null;
    }

    @Override
    @Transactional
    public void confirmMessage(Message message) {
        DomibusConnectorMessage newDbMessage = messageDao.confirmMessage(message.getDbMessage());
        message.setDbMessage(newDbMessage);
    }

    @Override
    @Transactional
    public void rejectMessage(Message message) {
        DomibusConnectorMessage newDbMessage = messageDao.rejectMessage(message.getDbMessage());
        message.setDbMessage(newDbMessage);
    }

    private Message mapDbMessageToMessage(DomibusConnectorMessage dbMessage) {
        MessageDetails details = new MessageDetails();
        details.setDbMessageId(dbMessage.getId());
        details.setEbmsMessageId(dbMessage.getEbmsMessageId());
        details.setNationalMessageId(dbMessage.getNationalMessageId());
        details.setConversationId(dbMessage.getConversationId());
        DomibusConnectorMessageInfo messageInfo = dbMessage.getMessageInfo();
        if (messageInfo != null) {
            details.setAction(messageInfo.getAction());
            details.setService(messageInfo.getService());
            details.setFinalRecipient(messageInfo.getFinalRecipient());
            details.setOriginalSender(messageInfo.getOriginalSender());
            details.setFromParty(messageInfo.getFrom());
            details.setToParty(messageInfo.getTo());
        }

        Message message = new Message(details);

        message.setDbMessage(dbMessage);

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

    private MessageConfirmation mapDbEvidenceToMessageConfirmation(DomibusConnectorEvidence dbEvidence) throws Exception {
        MessageConfirmation messageConfirmation = new MessageConfirmation();
        messageConfirmation.setEvidenceType(dbEvidence.getType());

        String stringClob = null;
        try {
            long i = 1;
            int clobLength = (int) dbEvidence.getEvidence().length();
            stringClob = dbEvidence.getEvidence().getSubString(i, clobLength);
        } catch (Exception e) {
            throw new Exception("Could not parse evidence from database!", e);
        }

        messageConfirmation.setEvidence(stringClob.getBytes());

        return messageConfirmation;
    }

    @Override
    public DomibusConnectorAction getAction(String action) {
        return actionDao.getAction(action);
    }

    @Override
    public DomibusConnectorService getService(String service) {
        return serviceDao.getService(service);
    }

    @Override
    public DomibusConnectorParty getParty(String partyId, String role) {
        return partyDao.getParty(partyId, role);
    }

    @Override
    public DomibusConnectorParty getPartyByPartyId(String partyId) {
        return partyDao.getPartyByPartyId(partyId);
    }

    @Override
    public DomibusConnectorAction getRelayREMMDAcceptanceRejectionAction() {
        return getAction(RELAY_REMMD_ACCEPTANCE_REJECTION);
    }

    @Override
    public DomibusConnectorAction getRelayREMMDFailure() {
        return getAction(RELAY_REMMD_FAILURE);
    }

    @Override
    public DomibusConnectorAction getDeliveryNonDeliveryToRecipientAction() {
        return getAction(DELIVERY_NON_DELIVERY_TO_RECIPIENT);
    }

    @Override
    public DomibusConnectorAction getRetrievalNonRetrievalToRecipientAction() {
        return getAction(RETRIEVAL_NON_RETRIEVAL_TO_RECIPIENT);
    }

}
