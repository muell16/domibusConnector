package eu.ecodex.connector.common.db.dao;

import eu.ecodex.connector.common.db.model.ECodexEvidence;

public interface ECodexEvidenceDao {

    void saveNewEvidence(ECodexEvidence evidence);

}
