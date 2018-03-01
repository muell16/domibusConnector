package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.backend.persistence.service.BackendClientInfoPersistenceService;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;


@Service
public class DeliverMessageFromControllerToBackendService  implements DomibusConnectorBackendDeliveryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeliverMessageFromControllerToBackendService.class);


    BackendClientInfoPersistenceService backendClientInfoPersistenceService;

    @Nullable PushMessageToBackendClient pushMessageToBackendClient;

    MessageToBackendClientWaitQueue waitQueue;

    //SETTER
    @Autowired
    public void setBackendClientInfoPersistenceService(BackendClientInfoPersistenceService backendClientInfoPersistenceService) {
        this.backendClientInfoPersistenceService = backendClientInfoPersistenceService;
    }

    @Autowired(required = false) //can be null if push is not activated
    public void setPushMessageToBackendClient(PushMessageToBackendClient pushMessageToBackendClient) {
        this.pushMessageToBackendClient = pushMessageToBackendClient;
    }

    @Autowired
    public void setWaitQueue(MessageToBackendClientWaitQueue waitQueue) {
        this.waitQueue = waitQueue;
    }

    @Override
    public void deliverMessageToBackend(DomibusConnectorMessage message) throws DomibusConnectorControllerException {
        LOGGER.debug("#deliverMessageToBackend: message [{}]", message);
        DomibusConnectorBackendMessage backendMessage = new DomibusConnectorBackendMessage();
        backendMessage.setDomibusConnectorMessage(message);

        LOGGER.debug("#deliverMessageToBackend: determine correct backendClient");
        DomibusConnectorBackendClientInfo backendClientInfoByServiceName =
                backendClientInfoPersistenceService.getBackendClientInfoByServiceName(message.getMessageDetails().getService());
        backendMessage.setBackendClientInfo(backendClientInfoByServiceName);


        LOGGER.debug("#deliverMessageToBackend: decide message [{}] is push message", backendMessage);
        if (backendMessage.getBackendClientInfo().isPushBackend()) {
            if (pushMessageToBackendClient == null) { throw new IllegalStateException("The client is configured as push client, but no push service is available!"); }
            LOGGER.debug("#deliverMessageToBackend: pushing message to backend client");
            pushMessageToBackendClient.push(backendMessage);
        } else {
            LOGGER.debug("#deliverMessageToBackend: putting message on waitQueue for pull");
            waitQueue.putMessageInWaitingQueue(backendMessage);
        }

    }
}
