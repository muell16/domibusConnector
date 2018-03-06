
package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import java.util.List;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface MessageToBackendClientWaitQueue {

    List<DomibusConnectorMessage> getConnectorMessageIdForBackend(String backendName);
        
    void putMessageInWaitingQueue(final DomibusConnectorBackendMessage backendMessage);

}
