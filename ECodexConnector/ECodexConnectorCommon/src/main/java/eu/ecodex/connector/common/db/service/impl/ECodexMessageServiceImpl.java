package eu.ecodex.connector.common.db.service.impl;

import java.sql.Clob;

import org.hibernate.lob.ClobImpl;

import eu.ecodex.connector.common.db.dao.ECodexEvidenceDao;
import eu.ecodex.connector.common.db.dao.ECodexMessageDao;
import eu.ecodex.connector.common.db.model.ECodexEvidence;
import eu.ecodex.connector.common.db.model.ECodexMessage;
import eu.ecodex.connector.common.db.service.ECodexMessageService;
import eu.ecodex.connector.common.enums.ECodexEvidenceType;
import eu.ecodex.connector.common.enums.ECodexMessageDirection;
import eu.ecodex.connector.common.message.Message;

public class ECodexMessageServiceImpl implements ECodexMessageService {

    private ECodexMessageDao messageDao;
    private ECodexEvidenceDao evidenceDao;

    public void setMessageDao(ECodexMessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public void setEvidenceDao(ECodexEvidenceDao evidenceDao) {
        this.evidenceDao = evidenceDao;
    }

    @Override
    public ECodexMessage createAndPersistDBMessage(Message message, ECodexMessageDirection direction) {
        ECodexMessage dbMessage = new ECodexMessage();

        dbMessage.setDirection(direction);
        dbMessage.setConversationId(message.getMessageDetails().getConversationId());
        dbMessage.setEbmsMessageId(message.getMessageDetails().getEbmsMessageId());
        dbMessage.setNationalMessageId(message.getMessageDetails().getNationalMessageId());

        messageDao.saveNewMessage(dbMessage);

        return dbMessage;
    }

    @Override
    public void mergeDBMessage(ECodexMessage dbMessage) {
        messageDao.mergeMessage(dbMessage);

    }

    public void createAndPersistDBEvidenceForDBMessage(ECodexMessage dbMessage, byte[] evidence,
            ECodexEvidenceType evidenceType) {
        ECodexEvidence dbEvidence = new ECodexEvidence();

        dbEvidence.setMessage(dbMessage);
        Clob cEvidence = new ClobImpl(new String(evidence));
        dbEvidence.setEvidence(cEvidence);
        dbEvidence.setType(evidenceType);

        evidenceDao.saveNewEvidence(dbEvidence);
    }

}
