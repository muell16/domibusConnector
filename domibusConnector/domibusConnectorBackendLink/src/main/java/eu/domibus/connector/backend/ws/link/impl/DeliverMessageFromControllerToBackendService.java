package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.backend.persistence.service.BackendClientInfoPersistenceService;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;


@Service
public class DeliverMessageFromControllerToBackendService  implements DomibusConnectorBackendDeliveryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeliverMessageFromControllerToBackendService.class);


    private BackendClientInfoPersistenceService backendClientInfoPersistenceService;

    @Nullable
    private PushMessageToBackendClient pushMessageToBackendClient;

    private MessageToBackendClientWaitQueue waitQueue;

    private DomibusConnectorMessagePersistenceService messagePersistenceService;


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
    public void setMessagePersistenceService(DomibusConnectorMessagePersistenceService messagePersistenceService) {
        this.messagePersistenceService = messagePersistenceService;
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


        DomibusConnectorBackendClientInfo backendClientInfo = getBackendClientForMessage(message);
        backendMessage.setBackendClientInfo(backendClientInfo);


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

    /**
     * Determine correct backend by following strategy
     *
     *  1) RefToMessageId:      The message is related (usually its a evidence message) to a previous message. The relation is expressed by
     *                          referencing the previous messageId in the refToMessageId property.
     *                          Load this previous message and set the backendClient to the same as the previous message.
     *
     *  2) ConversationId:      The message is related to a previous message. The relation is expressed by the same conversationId
     *                          Load this previous message and set the backendClient to the same as the previous message.
     *
     *  3) Service:             Lookup the backendConfiguration and find the correct backendClient by the service name.
     *
     *  4) DefaultBackend:      Lookup the backendConfiguration and find the default backend.
     *
     *  5) ERROR:               If none of the previous conditions has matched a error will be thrown. No backend client found to process this message!
     *
     * @param msg
     * @return
     */
    @Nonnull DomibusConnectorBackendClientInfo getBackendClientForMessage(DomibusConnectorMessage msg) {
        LOGGER.debug("#getBackendClientForMessage: determine correct backendClient");

        DomibusConnectorBackendClientInfo backendClientInfo;

        // 1 RefToMessageId
        backendClientInfo = getBackendClientInfoByRefToMessageIdOrReturnNull(msg.getMessageDetails().getRefToMessageId());
        if (backendClientInfo != null) {
            return backendClientInfo;
        }

        // 2 ConversationId
        backendClientInfo = getBackendClientInfoByConversationIdOrReturnNull(msg.getMessageDetails().getConversationId());
        if (backendClientInfo != null) {
            return backendClientInfo;
        }

        // 3 Service
        DomibusConnectorService service = msg.getMessageDetails().getService();
        backendClientInfo =
                backendClientInfoPersistenceService.getEnabledBackendClientInfoByService(service);
        if (backendClientInfo != null) {
            return backendClientInfo;
        }

        // 4 defaultBackend
        backendClientInfo = backendClientInfoPersistenceService.getDefaultBackendClientInfo();
        if (backendClientInfo != null) {
            return backendClientInfo;
        }

        //5 error
        throw new RuntimeException(String.format("No backend found to handle message [%s]", msg));
    }

    private DomibusConnectorBackendClientInfo getBackendClientInfoByRefToMessageIdOrReturnNull(String refToMessageId) {
        if (refToMessageId != null) {
            LOGGER.trace("#getBackendClientInfoByRefToMessageIdOrReturnNull: try to find message by refToMessageId [{}]", refToMessageId);
            DomibusConnectorMessage referencedMessage = messagePersistenceService.findMessageByConnectorMessageId(refToMessageId);
            if (referencedMessage == null) {
                throw new IllegalStateException(String.format("Referenced message id=[%s] does not exist!", refToMessageId));
            }
            String connectorBackendClientName = referencedMessage.getMessageDetails().getConnectorBackendClientName();
            DomibusConnectorBackendClientInfo backendClientInfoByServiceName = backendClientInfoPersistenceService.getEnabledBackendClientInfoByName(connectorBackendClientName);
            return backendClientInfoByServiceName;
        } else {
            LOGGER.debug("#getBackendClientInfoByRefToMessageIdOrReturnNull: refToMessageId is null returning null!");
            return null;
        }
    }

    private DomibusConnectorBackendClientInfo getBackendClientInfoByConversationIdOrReturnNull(String conversationId) {
        if (conversationId != null) {
            LOGGER.trace("#getBackendClientInfoByConversationIdOrReturnNull: try to find message by conversationId");
            List<DomibusConnectorMessage> messagesByConversationId = messagePersistenceService.findMessagesByConversationId(conversationId);
            if (messagesByConversationId.size() > 0) {
                String connectorBackendClientName = messagesByConversationId.get(0).getMessageDetails().getConnectorBackendClientName();
                DomibusConnectorBackendClientInfo backendClientInfoByServiceName = backendClientInfoPersistenceService.getEnabledBackendClientInfoByName(connectorBackendClientName);
                return backendClientInfoByServiceName;
            } else {
                LOGGER.debug("#getBackendClientInfoByConversationIdOrReturnNull: no messages found for conversationId [{}]", conversationId);
                return null;
            }
        } else {
            LOGGER.debug("#getBackendClientInfoByConversationIdOrReturnNull: conversationId is null returning null!");
            return null;
        }
    }
}
