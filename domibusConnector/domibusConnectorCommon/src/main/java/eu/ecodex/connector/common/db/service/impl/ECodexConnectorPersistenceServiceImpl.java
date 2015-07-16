package eu.ecodex.connector.common.db.service.impl;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.lob.ClobImpl;
import org.springframework.transaction.annotation.Transactional;

import eu.ecodex.connector.common.db.dao.ECodexActionDao;
import eu.ecodex.connector.common.db.dao.ECodexEvidenceDao;
import eu.ecodex.connector.common.db.dao.ECodexMessageDao;
import eu.ecodex.connector.common.db.dao.ECodexMessageInfoDao;
import eu.ecodex.connector.common.db.dao.ECodexPartyDao;
import eu.ecodex.connector.common.db.dao.ECodexServiceDao;
import eu.ecodex.connector.common.db.model.ECodexAction;
import eu.ecodex.connector.common.db.model.ECodexEvidence;
import eu.ecodex.connector.common.db.model.ECodexMessage;
import eu.ecodex.connector.common.db.model.ECodexMessageInfo;
import eu.ecodex.connector.common.db.model.ECodexParty;
import eu.ecodex.connector.common.db.model.ECodexService;
import eu.ecodex.connector.common.db.service.ECodexConnectorPersistenceService;
import eu.ecodex.connector.common.enums.ECodexEvidenceType;
import eu.ecodex.connector.common.enums.ECodexMessageDirection;
import eu.ecodex.connector.common.exception.PersistenceException;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.common.message.MessageDetails;

public class ECodexConnectorPersistenceServiceImpl implements ECodexConnectorPersistenceService {

    private static final String RETRIEVAL_NON_RETRIEVAL_TO_RECIPIENT = "RetrievalNonRetrievalToRecipient";
    private static final String DELIVERY_NON_DELIVERY_TO_RECIPIENT = "DeliveryNonDeliveryToRecipient";
    private static final String RELAY_REMMD_FAILURE = "RelayREMMDFailure";
    private static final String RELAY_REMMD_ACCEPTANCE_REJECTION = "RelayREMMDAcceptanceRejection";

    private ECodexMessageDao messageDao;
    private ECodexEvidenceDao evidenceDao;
    private ECodexActionDao actionDao;
    private ECodexServiceDao serviceDao;
    private ECodexPartyDao partyDao;
    private ECodexMessageInfoDao messageInfoDao;

    public void setMessageDao(ECodexMessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public void setEvidenceDao(ECodexEvidenceDao evidenceDao) {
        this.evidenceDao = evidenceDao;
    }

    public void setActionDao(ECodexActionDao actionDao) {
        this.actionDao = actionDao;
    }

    public void setServiceDao(ECodexServiceDao serviceDao) {
        this.serviceDao = serviceDao;
    }

    public void setPartyDao(ECodexPartyDao partyDao) {
        this.partyDao = partyDao;
    }

    public void setMessageInfoDao(ECodexMessageInfoDao messageInfoDao) {
        this.messageInfoDao = messageInfoDao;
    }

    @Override
    @Transactional
    public void persistMessageIntoDatabase(Message message, ECodexMessageDirection direction)
            throws PersistenceException {
        ECodexMessage dbMessage = new ECodexMessage();

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

        ECodexMessageInfo dbMessageInfo = new ECodexMessageInfo();
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

        ECodexMessageInfo messageInfo = message.getDbMessage().getMessageInfo();

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
        ECodexMessage dbMessage = messageDao.mergeMessage(message.getDbMessage());
        dbMessage.setDeliveredToGateway(new Date());
        dbMessage = messageDao.mergeMessage(dbMessage);
        message.setDbMessage(dbMessage);
    }

    @Override
    @Transactional
    public void setMessageDeliveredToNationalSystem(Message message) {
        ECodexMessage dbMessage = messageDao.mergeMessage(message.getDbMessage());
        dbMessage.setDeliveredToNationalSystem(new Date());
        dbMessage = messageDao.mergeMessage(dbMessage);
        message.setDbMessage(dbMessage);
    }

    @Override
    @Transactional
    public void setEvidenceDeliveredToGateway(Message message, ECodexEvidenceType evidenceType) {
        messageDao.mergeMessage(message.getDbMessage());
        List<ECodexEvidence> evidences = evidenceDao.findEvidencesForMessage(message.getDbMessage());
        ECodexEvidence dbEvidence = findEvidence(evidences, evidenceType);
        if (dbEvidence != null) {
            dbEvidence.setDeliveredToGateway(new Date());
            evidenceDao.mergeEvidence(dbEvidence);
        }
    }

    @Override
    @Transactional
    public void setEvidenceDeliveredToNationalSystem(Message message, ECodexEvidenceType evidenceType) {
        messageDao.mergeMessage(message.getDbMessage());
        List<ECodexEvidence> evidences = evidenceDao.findEvidencesForMessage(message.getDbMessage());
        ECodexEvidence dbEvidence = findEvidence(evidences, evidenceType);
        if (dbEvidence != null) {
            dbEvidence.setDeliveredToNationalSystem(new Date());
            evidenceDao.mergeEvidence(dbEvidence);
        }
    }

    private ECodexEvidence findEvidence(List<ECodexEvidence> evidences, ECodexEvidenceType evidenceType) {
        if (evidences != null) {
            for (ECodexEvidence evidence : evidences) {
                if (evidence.getType().equals(evidenceType)) {
                    return evidence;
                }
            }
        }
        return null;
    }

    @Override
    @Transactional
    public void persistEvidenceForMessageIntoDatabase(Message message, byte[] evidence, ECodexEvidenceType evidenceType) {
        ECodexEvidence dbEvidence = new ECodexEvidence();

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
        ECodexMessage dbMessage = messageDao.findMessageByNationalId(nationalMessageId);

        Message message = mapDbMessageToMessage(dbMessage);

        return message;

    }

    @Override
    @Transactional(readOnly = true)
    public Message findMessageByEbmsId(String ebmsMessageId) {
        ECodexMessage dbMessage = messageDao.findMessageByEbmsId(ebmsMessageId);

        Message message = mapDbMessageToMessage(dbMessage);

        return message;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> findMessagesByConversationId(String conversationId) {
        List<ECodexMessage> dbMessages = messageDao.findMessagesByConversationId(conversationId);
        if (dbMessages != null && !dbMessages.isEmpty()) {
            List<Message> messages = new ArrayList<Message>(dbMessages.size());
            for (ECodexMessage dbMessage : dbMessages) {
                messages.add(mapDbMessageToMessage(dbMessage));
            }
            return messages;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> findOutgoingUnconfirmedMessages() {
        List<ECodexMessage> dbMessages = messageDao.findOutgoingUnconfirmedMessages();
        if (dbMessages != null && !dbMessages.isEmpty()) {
            List<Message> unconfirmedMessages = new ArrayList<Message>(dbMessages.size());
            for (ECodexMessage dbMessage : dbMessages) {
                unconfirmedMessages.add(mapDbMessageToMessage(dbMessage));
            }
            return unconfirmedMessages;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> findIncomingUnconfirmedMessages() {
        List<ECodexMessage> dbMessages = messageDao.findIncomingUnconfirmedMessages();
        if (dbMessages != null && !dbMessages.isEmpty()) {
            List<Message> unconfirmedMessages = new ArrayList<Message>(dbMessages.size());
            for (ECodexMessage dbMessage : dbMessages) {
                unconfirmedMessages.add(mapDbMessageToMessage(dbMessage));
            }
            return unconfirmedMessages;
        }
        return null;
    }

    @Override
    @Transactional
    public void confirmMessage(Message message) {
        ECodexMessage newDbMessage = messageDao.confirmMessage(message.getDbMessage());
        message.setDbMessage(newDbMessage);
    }

    @Override
    @Transactional
    public void rejectMessage(Message message) {
        ECodexMessage newDbMessage = messageDao.rejectMessage(message.getDbMessage());
        message.setDbMessage(newDbMessage);
    }

    private Message mapDbMessageToMessage(ECodexMessage dbMessage) {
        MessageDetails details = new MessageDetails();
        details.setDbMessageId(dbMessage.getId());
        details.setEbmsMessageId(dbMessage.getEbmsMessageId());
        details.setNationalMessageId(dbMessage.getNationalMessageId());
        details.setConversationId(dbMessage.getConversationId());
        ECodexMessageInfo messageInfo = dbMessage.getMessageInfo();
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

        for (ECodexEvidence dbEvidence : dbMessage.getEvidences()) {
            try {
                MessageConfirmation confirmation = mapDbEvidenceToMessageConfirmation(dbEvidence);
                message.addConfirmation(confirmation);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return message;
    }

    private MessageConfirmation mapDbEvidenceToMessageConfirmation(ECodexEvidence dbEvidence) throws Exception {
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
    public ECodexAction getAction(String action) {
        return actionDao.getAction(action);
    }

    @Override
    public ECodexService getService(String service) {
        return serviceDao.getService(service);
    }

    @Override
    public ECodexParty getParty(String partyId, String role) {
        return partyDao.getParty(partyId, role);
    }

    @Override
    public ECodexParty getPartyByPartyId(String partyId) {
        return partyDao.getPartyByPartyId(partyId);
    }

    @Override
    public ECodexAction getRelayREMMDAcceptanceRejectionAction() {
        return getAction(RELAY_REMMD_ACCEPTANCE_REJECTION);
    }

    @Override
    public ECodexAction getRelayREMMDFailure() {
        return getAction(RELAY_REMMD_FAILURE);
    }

    @Override
    public ECodexAction getDeliveryNonDeliveryToRecipientAction() {
        return getAction(DELIVERY_NON_DELIVERY_TO_RECIPIENT);
    }

    @Override
    public ECodexAction getRetrievalNonRetrievalToRecipientAction() {
        return getAction(RETRIEVAL_NON_RETRIEVAL_TO_RECIPIENT);
    }

}
