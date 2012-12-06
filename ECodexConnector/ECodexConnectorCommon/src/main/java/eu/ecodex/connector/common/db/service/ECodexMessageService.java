package eu.ecodex.connector.common.db.service;

import eu.ecodex.connector.common.db.model.ECodexMessage;
import eu.ecodex.connector.common.enums.ECodexEvidenceType;
import eu.ecodex.connector.common.enums.ECodexMessageDirection;
import eu.ecodex.connector.common.message.Message;

public interface ECodexMessageService {

    ECodexMessage createAndPersistDBMessage(Message message, ECodexMessageDirection direction);

    void mergeDBMessage(ECodexMessage dbMessage);

    void createAndPersistDBEvidenceForDBMessage(ECodexMessage dbMessage, byte[] evidence,
            ECodexEvidenceType evidenceType);
}
