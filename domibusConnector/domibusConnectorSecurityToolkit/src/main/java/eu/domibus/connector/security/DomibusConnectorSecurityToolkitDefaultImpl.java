package eu.domibus.connector.security;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.security.container.DomibusSecurityContainer;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;

public class DomibusConnectorSecurityToolkitDefaultImpl implements DomibusConnectorSecurityToolkit {

    static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorSecurityToolkitDefaultImpl.class);

    @Resource(name="domibusConnectorSecurityContainer")
    DomibusSecurityContainer securityContainer;

    public void setSecurityContainer(DomibusSecurityContainer securityContainer) {
        this.securityContainer = securityContainer;
    }

    @Override
    public void buildContainer(Message message) throws DomibusConnectorSecurityException {
        securityContainer.createContainer(message);
    }

    @Override
    public void validateContainer(Message message) throws DomibusConnectorSecurityException {
        securityContainer.recieveContainerContents(message);
    }

}
