package eu.domibus.connector.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.domibus.connector.common.NationalImplementationFactory;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;

public class DomibusConnectorSecurityToolkitFactory extends NationalImplementationFactory {

    static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorSecurityToolkitFactory.class);

    public DomibusConnectorSecurityToolkit createDomibusConnectorSecurityToolkit()
            throws DomibusConnectorSecurityException {

        if (implementationClassName == null || implementationClassName.isEmpty()) {
            return new DomibusConnectorSecurityToolkitImpl();
        }
        try {
            DomibusConnectorSecurityToolkit client = (DomibusConnectorSecurityToolkit) super
                    .createNationalImplementation();
            return client;
        } catch (Exception e) {
            LOGGER.error(
                    "Could not create an instance for DomibusConnectorSecurityToolkit implementation with full qualified class name "
                            + implementationClassName, e);
            return new DomibusConnectorSecurityToolkitImpl();
        }
    }

}
