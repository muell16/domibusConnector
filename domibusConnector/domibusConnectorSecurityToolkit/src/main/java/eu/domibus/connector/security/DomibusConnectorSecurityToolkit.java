package eu.domibus.connector.security;


import eu.ecodex.dc5.message.model.DC5Message;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;

/**
 * Interface with methods to invoke WP4 functionality.
 * 
 * @author riederb
 * 
 */
public interface DomibusConnectorSecurityToolkit {

    DC5Message validateContainer(DC5Message message) throws DomibusConnectorSecurityException;

    DC5Message buildContainer(DC5Message message) throws DomibusConnectorSecurityException;

}
