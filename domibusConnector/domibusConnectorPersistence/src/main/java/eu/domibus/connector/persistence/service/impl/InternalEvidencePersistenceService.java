package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

/**
 * for usage in persistence module only!
 */
public interface InternalEvidencePersistenceService {

    DomibusConnectorMessage persistAsEvidence(DomibusConnectorMessage message);

}
