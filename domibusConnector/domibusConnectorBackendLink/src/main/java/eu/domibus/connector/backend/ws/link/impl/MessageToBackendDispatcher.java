
package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.persistence.dao.BackendClientDao;
import eu.domibus.connector.backend.persistence.model.BackendClientInfo;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Determines which backend should receive this message
 * adds this information to the message
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class MessageToBackendDispatcher implements MessageHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageToBackendDispatcher.class);
    
    @Autowired
    BackendClientDao backendClientDao;

    public void setBackendClientDao(BackendClientDao backendClientDao) {
        this.backendClientDao = backendClientDao;
    }

    
    @Override
    public DomibusConnectorMessage handleMessage(DomibusConnectorMessage message) {
        DomibusConnectorService service = message.getMessageDetails().getService();
        List<BackendClientInfo> findByServices_service = backendClientDao.findByServices_service(service.getService());
        
        if (findByServices_service.size() > 1) {
            String error = String.format("There is more than one backend configured for receiving messages with this service [%s]!", service.getService());
            LOGGER.error("Throwing exception because: {}", error);
            throw new IllegalStateException(error);
        } else if (findByServices_service.size() == 0) {
            String error = String.format("There is NO backend configured for receiving messages with this service [%s]!", service.getService());
            LOGGER.error("Throwing exception because: {}", error);
            throw new IllegalStateException(error);
        }
        BackendClientInfo backendClient = findByServices_service.get(0);
        
        message.setConnectorBackendClientName(backendClient.getBackendName());
        
        return message;
    }

}
