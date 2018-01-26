package eu.domibus.connector.persistence.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 
 * @deprecated use the more specific interfaces instead:
 *      DomibusConnectorPartyPersistenceService
 *      DomibusConnectorActionPersistenceService
 *      DomibusConnectorEvidencePersistenceService
 *      DomibusConnectorServicePersistenceService
 *      DomibusConnectorMessageErrorPersistenceService
 *      DomibusConnectorMessagePersistenceService
 * 
 */
@Deprecated
@Transactional
public interface DomibusConnectorPersistenceService extends DomibusConnectorPartyPersistenceService, 
        DomibusConnectorActionPersistenceService, 
        DomibusConnectorServicePersistenceService,
        DomibusConnectorEvidencePersistenceService,
        DomibusConnectorMessageErrorPersistenceService, 
        DomibusConnectorMessagePersistenceService {
}
