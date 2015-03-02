package eu.ecodex.connector.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.security.container.ECodexSecurityContainer;
import eu.ecodex.connector.security.exception.ECodexConnectorSecurityException;

public class ECodexConnectorSecurityToolkitImpl implements ECodexConnectorSecurityToolkit {

    static Logger LOGGER = LoggerFactory.getLogger(ECodexConnectorSecurityToolkitImpl.class);

    ECodexSecurityContainer securityContainer;

    public void setSecurityContainer(ECodexSecurityContainer securityContainer) {
        this.securityContainer = securityContainer;
    }

    @Override
    public void buildContainer(Message message) throws ECodexConnectorSecurityException {
        securityContainer.createContainer(message);
    }

    @Override
    public void validateContainer(Message message) throws ECodexConnectorSecurityException {
        securityContainer.recieveContainerContents(message);
    }

}
