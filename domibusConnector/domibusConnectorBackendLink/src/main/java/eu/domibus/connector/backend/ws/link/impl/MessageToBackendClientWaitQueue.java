
package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.backend.persistence.model.BackendClientInfo;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.Message;
import org.springframework.jms.annotation.JmsListener;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface MessageToBackendClientWaitQueue {

    List<String> getConnectorMessageIdForBackend(String backendName);
        
    void putMessageInWaitingQueue(final DomibusConnectorBackendMessage backendMessage);

}
