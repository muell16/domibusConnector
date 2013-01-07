package eu.ecodex.connector.common.db.service.impl;

import java.sql.Clob;

import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.lob.ClobImpl;

import eu.ecodex.connector.common.db.dao.ECodexEvidenceDao;
import eu.ecodex.connector.common.db.dao.ECodexMessageDao;
import eu.ecodex.connector.common.db.model.ECodexEvidence;
import eu.ecodex.connector.common.db.model.ECodexMessage;
import eu.ecodex.connector.common.db.service.ECodexConnectorPersistenceService;
import eu.ecodex.connector.common.enums.ECodexEvidenceType;
import eu.ecodex.connector.common.enums.ECodexMessageDirection;
import eu.ecodex.connector.common.exception.PersistenceException;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.common.message.MessageDetails;

public class ECodexConnectorPersistenceServiceImpl implements ECodexConnectorPersistenceService {

    private ECodexMessageDao messageDao;
    private ECodexEvidenceDao evidenceDao;

    public void setMessageDao(ECodexMessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public void setEvidenceDao(ECodexEvidenceDao evidenceDao) {
        this.evidenceDao = evidenceDao;
    }

    @Override
    public void persistMessageIntoDatabase(Message message, ECodexMessageDirection direction)
            throws PersistenceException {
        ECodexMessage dbMessage = new ECodexMessage();

        dbMessage.setDirection(direction);
        dbMessage.setConversationId(message.getMessageDetails().getConversationId());
        dbMessage.setEbmsMessageId(message.getMessageDetails().getEbmsMessageId());
        dbMessage.setNationalMessageId(message.getMessageDetails().getNationalMessageId());

        try {
            messageDao.saveNewMessage(dbMessage);
        } catch (ConstraintViolationException cve) {
            throw new PersistenceException(
                    "Could not persist message into database. Most likely the nationalMessageId or the ebmsMessageId already exist. ",
                    cve);
        }

        message.setDbMessage(dbMessage);
        message.getMessageDetails().setDbMessageId(dbMessage.getId());
    }

    @Override
    public void mergeMessageWithDatabase(Message message) {

        messageDao.mergeMessage(message.getDbMessage());

    }

    @Override
    public void persistEvidenceForMessageIntoDatabase(Message message, byte[] evidence, ECodexEvidenceType evidenceType) {
        ECodexEvidence dbEvidence = new ECodexEvidence();

        dbEvidence.setMessage(message.getDbMessage());
        Clob cEvidence = new ClobImpl(new String(evidence));
        dbEvidence.setEvidence(cEvidence);
        dbEvidence.setType(evidenceType);

        evidenceDao.saveNewEvidence(dbEvidence);
    }

    @Override
    public Message findMessageByNationalId(String nationalMessageId) {
        ECodexMessage dbMessage = messageDao.findMessageByNationalId(nationalMessageId);

        Message message = mapDbMessageToMessage(dbMessage);

        return message;

    }

    @Override
    public Message findMessageByEbmsId(String ebmsMessageId) {
        ECodexMessage dbMessage = messageDao.findMessageByEbmsId(ebmsMessageId);

        Message message = mapDbMessageToMessage(dbMessage);

        return message;
    }

    private Message mapDbMessageToMessage(ECodexMessage dbMessage) {
        MessageDetails details = new MessageDetails();
        details.setDbMessageId(dbMessage.getId());
        details.setEbmsMessageId(dbMessage.getEbmsMessageId());
        details.setNationalMessageId(dbMessage.getNationalMessageId());
        details.setConversationId(dbMessage.getConversationId());

        Message message = new Message(details);

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

}
