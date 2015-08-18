package eu.domibus.connector.security;

import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;

/**
 * Interface with methods to invoke WP4 functionality.
 * 
 * @author riederb
 * 
 */
public interface DomibusConnectorSecurityToolkit {

    void validateContainer(Message message) throws DomibusConnectorSecurityException;

    void buildContainer(Message message) throws DomibusConnectorSecurityException;

}
