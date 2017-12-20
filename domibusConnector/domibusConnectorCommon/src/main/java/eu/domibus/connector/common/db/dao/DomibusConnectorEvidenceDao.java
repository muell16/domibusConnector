package eu.domibus.connector.common.db.dao;

import java.util.List;

import eu.domibus.connector.persistence.model.DomibusConnectorEvidence;
import eu.domibus.connector.persistence.model.DomibusConnectorMessage;

public interface DomibusConnectorEvidenceDao {

    void saveNewEvidence(DomibusConnectorEvidence evidence);

    void mergeEvidence(DomibusConnectorEvidence evidence);

    List<DomibusConnectorEvidence> findEvidencesForMessage(DomibusConnectorMessage message);
}
