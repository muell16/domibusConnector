
package eu.domibus.connector.persistence.service;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

/**
 * Takes a message and persists all big data of this message into
 * storage
 * OR
 * Takes a message and activates all references {@link eu.domibus.connector.domain.model.DomibusConnectorBigDataReference} 
 * for streaming from storage
 * 
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 * 
 */
public interface DomibusConnectorPersistAllBigDataOfMessageService {

    /**
     * The implementation can assumte that, the message is already persisted
     * with DomibusConnectorMessagePersistenceService also see: {@link eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService#persistMessageIntoDatabase(eu.domibus.connector.domain.model.DomibusConnectorMessage, eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection) }
     * 
     * @param message - the message
     * @return the message with storage references (ready to read)
     */
    DomibusConnectorMessage loadAllBigFilesFromMessage(DomibusConnectorMessage message);

    /**
     * 
     * persist all {@link eu.domibus.connector.domain.model.DomibusConnectorBigDataReference} 
     * into storage
     * 
     * The implementation can assume that, the message was already persisted first 
     * with DomibusConnectorMessagePersistenceService also see: {@link eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService#persistMessageIntoDatabase(eu.domibus.connector.domain.model.DomibusConnectorMessage, eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection) }
     * 
     * @param message the message, with storage references
     * @return the message, with refreshed storage references (ready to read)
     * 
     */
    DomibusConnectorMessage persistAllBigFilesFromMessage(DomibusConnectorMessage message);

}
