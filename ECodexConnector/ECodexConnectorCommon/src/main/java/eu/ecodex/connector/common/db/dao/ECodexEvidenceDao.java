package eu.ecodex.connector.common.db.dao;

import java.util.List;

import eu.ecodex.connector.common.db.model.ECodexEvidence;
import eu.ecodex.connector.common.db.model.ECodexMessage;

public interface ECodexEvidenceDao {

    void saveNewEvidence(ECodexEvidence evidence);

    void mergeEvidence(ECodexEvidence evidence);

    List<ECodexEvidence> findEvidencesForMessage(ECodexMessage message);
}
