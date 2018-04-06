package eu.domibus.connector.security;


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.security.container.DomibusSecurityContainer;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;
import eu.domibus.connector.security.validation.DomibusConnectorTechnicalValidationServiceFactory;
import org.springframework.transaction.annotation.Transactional;

@Component("domibusConnectorSecurityToolkitDefaultImpl")
public class DomibusConnectorSecurityToolkitDefaultImpl implements DomibusConnectorSecurityToolkit {

    static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorSecurityToolkitDefaultImpl.class);

    @Resource(name="domibusConnectorSecurityContainer")
    DomibusSecurityContainer securityContainer;
    
    @Resource
    DomibusConnectorTechnicalValidationServiceFactory technicalValidationServiceFactory;

    public void setSecurityContainer(DomibusSecurityContainer securityContainer) {
        this.securityContainer = securityContainer;
    }

    @Override
    @Transactional
    public DomibusConnectorMessage buildContainer(DomibusConnectorMessage message) throws DomibusConnectorSecurityException {
        message = securityContainer.createContainer(message);
    	return message;
    }

    @Override
    @Transactional
    public DomibusConnectorMessage validateContainer(DomibusConnectorMessage message) throws DomibusConnectorSecurityException {
        securityContainer.recieveContainerContents(message);
        return message;
    }

}
