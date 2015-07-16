package eu.ecodex.connector.security;

import eu.domibus.connector.common.message.Message;
import eu.ecodex.connector.security.exception.ECodexConnectorSecurityException;

/**
 * Interface with methods to invoke WP4 functionality.
 * 
 * @author riederb
 * 
 */
public interface ECodexConnectorSecurityToolkit {

    void validateContainer(Message message) throws ECodexConnectorSecurityException;

    void buildContainer(Message message) throws ECodexConnectorSecurityException;

}
