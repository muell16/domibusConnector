
package eu.domibus.connector.ws.backend.link.impl;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface PushMessageToBackendClient {

    public void push(String connectorMessageId, String backendClientName);

}
