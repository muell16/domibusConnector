package eu.ecodex.connector.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.common.NationalImplementationFactory;
import eu.ecodex.connector.security.exception.ECodexConnectorSecurityException;

public class ECodexConnectorSecurityToolkitFactory extends NationalImplementationFactory {

    static Logger LOGGER = LoggerFactory.getLogger(ECodexConnectorSecurityToolkitFactory.class);

    public ECodexConnectorSecurityToolkit createECodexConnectorSecurityToolkit()
            throws ECodexConnectorSecurityException {

        if (implementationClassName == null || implementationClassName.isEmpty()) {
            return new ECodexConnectorSecurityToolkitImpl();
        }
        try {
            ECodexConnectorSecurityToolkit client = (ECodexConnectorSecurityToolkit) super
                    .createNationalImplementation();
            return client;
        } catch (Exception e) {
            LOGGER.error(
                    "Could not create an instance for ECodexConnectorSecurityToolkit implementation with full qualified class name "
                            + implementationClassName, e);
            return new ECodexConnectorSecurityToolkitImpl();
        }
    }

}
