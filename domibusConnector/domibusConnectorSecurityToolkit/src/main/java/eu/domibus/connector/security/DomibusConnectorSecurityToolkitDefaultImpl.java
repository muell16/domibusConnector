package eu.domibus.connector.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.security.container.DomibusSecurityContainer;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;
import eu.domibus.connector.security.validation.DomibusConnectorTechnicalValidationServiceFactory;

@Component
public class DomibusConnectorSecurityToolkitDefaultImpl implements DomibusConnectorSecurityToolkit {

    static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorSecurityToolkitDefaultImpl.class);

    private final DomibusSecurityContainer securityContainer;

    public DomibusConnectorSecurityToolkitDefaultImpl(DomibusSecurityContainer securityContainer) {
        this.securityContainer = securityContainer;
    }


    @Override
    public DomibusConnectorMessage buildContainer(DomibusConnectorMessage message) throws DomibusConnectorSecurityException {
        message = securityContainer.createContainer(message);
    	return message;
    }

    @Override
    public DomibusConnectorMessage validateContainer(DomibusConnectorMessage message) throws DomibusConnectorSecurityException {
        securityContainer.recieveContainerContents(message);
        return message;
    }

}
