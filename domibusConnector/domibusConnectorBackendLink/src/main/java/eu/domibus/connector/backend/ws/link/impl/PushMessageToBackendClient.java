
package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface PushMessageToBackendClient {

    public void push(DomibusConnectorBackendMessage backendMessage);

//    public void push(String connectorMessageId, String backendClientName);

}
