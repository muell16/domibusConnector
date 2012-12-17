package eu.ecodex.connector.common.db.service;

import eu.ecodex.connector.common.enums.ECodexEvidenceType;
import eu.ecodex.connector.common.enums.ECodexMessageDirection;
import eu.ecodex.connector.common.message.Message;

public interface ECodexConnectorPersistenceService {

    void persistMessageIntoDatabase(Message message, ECodexMessageDirection direction);

    void mergeMessageWithDatabase(Message message);

    void persistEvidenceForMessageIntoDatabase(Message message, byte[] evidence, ECodexEvidenceType evidenceType);

    Message findMessageByNationalId(String nationalMessageId);

    Message findMessageByEbmsId(String ebmsMessageId);
}
