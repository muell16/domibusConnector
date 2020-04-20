
package eu.domibus.connector.persistence.service;

import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.LargeFileReference;

import javax.annotation.Nonnull;

/**
 * Takes a message and persists all big data of this message into
 * storage
 * OR
 * Takes a message and activates all references {@link LargeFileReference}
 * for streaming from storage
 * 
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 * 
 */
public interface DomibusConnectorMessageContentManager {


    /**
     * this method should remove all messages from storage which is related to this
     * message
     * @param message - the message
     */
    void cleanForMessage(DomibusConnectorMessage message);
}
