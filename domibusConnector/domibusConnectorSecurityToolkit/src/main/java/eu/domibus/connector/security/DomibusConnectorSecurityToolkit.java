package eu.domibus.connector.security;


import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;

/**
 * Interface with methods to invoke WP4 functionality.
 * 
 * @author riederb
 * 
 */
public interface DomibusConnectorSecurityToolkit {

    void validateContainer(DomibusConnectorMessage message) throws DomibusConnectorSecurityException;

    void buildContainer(DomibusConnectorMessage message) throws DomibusConnectorSecurityException;

}
