
package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface MessageHandler {

    public DomibusConnectorMessage handleMessage(DomibusConnectorMessage message);
    
}
