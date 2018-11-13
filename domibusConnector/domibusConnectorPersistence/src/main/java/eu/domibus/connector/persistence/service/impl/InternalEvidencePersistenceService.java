package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;

/**
 * for usage in persistence module only!
 */
public interface InternalEvidencePersistenceService {

    DomibusConnectorMessage persistAsEvidence(DomibusConnectorMessage message);

    void persistToMessage(PDomibusConnectorMessage dbMessage, DomibusConnectorMessageConfirmation c);
}
