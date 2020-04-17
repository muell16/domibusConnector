package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.controller.exception.DomibusConnectorBackendException;
import eu.domibus.connector.backend.persistence.service.BackendClientInfoPersistenceService;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static eu.domibus.connector.backend.ws.link.spring.WSBackendLinkContextConfiguration.WS_BACKEND_LINK_PROFILE;


@Profile(WS_BACKEND_LINK_PROFILE)
@Service
public class DeliverMessageFromControllerToBackendService implements DomibusConnectorBackendDeliveryService {

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
        if (message.getConnectorMessageId() == null) {
            throw new DomibusConnectorControllerException("cannot handle message without message id");
        }

        LOGGER.debug("#deliverMessageToBackend: message [{}]", message);
        DomibusConnectorBackendMessage backendMessage = new DomibusConnectorBackendMessage();
        backendMessage.setDomibusConnectorMessage(message);


        DomibusConnectorBackendClientInfo backendClientInfo = getBackendClientForMessage(message);
        backendMessage.setBackendClientInfo(backendClientInfo);
        message.getMessageDetails().setConnectorBackendClientName(backendClientInfo.getBackendName());
        messagePersistenceService.mergeMessageWithDatabase(message);


        LOGGER.debug("#deliverMessageToBackend: decide message [{}] is push message: [{}]", backendMessage, backendMessage.getBackendClientInfo().isPushBackend());
        if (backendMessage.getBackendClientInfo().isPushBackend()) {
            if (pushMessageToBackendClient == null) {
                throw new DomibusConnectorBackendException("The client is configured as push client, but no push service is available!");
            }
            LOGGER.debug("#deliverMessageToBackend: pushing message to backend client");
            pushMessageToBackendClient.push(backendMessage);
        } else {
            LOGGER.debug("#deliverMessageToBackend: putting message on waitQueue for pull");
            waitQueue.putMessageInWaitingQueue(backendMessage);
        }
    }

    /**
     * Determine correct backend by following strategy
     * <p>
     * 0) BackendId:            The message details contains a backendIdName, then this is used!
     * <p>
     * 1) RefToMessageId:      The message is related (usually its a evidence message) to a previous message. The relation is expressed by
     * referencing the ebmsId OR nationalId of the previous message in the refToMessageId property.
     * Load this previous message and set the backendClient to the same as the previous message.
     * <p>
     * 2) ConversationId:      The message is related to a previous message. The relation is expressed by the same conversationId
     * Load this previous message and set the backendClient to the same as the previous message.
     * <p>
     * 3) Service:             Lookup the backendConfiguration and find the correct backendClient by the service name.
     * <p>
     * 4) DefaultBackend:      Lookup the backendConfiguration and find the default backend.
     * <p>
     * 5) ERROR:               If none of the previous conditions has matched a error will be thrown. No backend client found to process this message!
     *
     * @param msg - the processed message
     * @return the determined connectorBackendClientInfo
     */
    @Nonnull
    DomibusConnectorBackendClientInfo getBackendClientForMessage(DomibusConnectorMessage msg) {
        LOGGER.debug("#getBackendClientForMessage: determine correct backendClient");

        DomibusConnectorBackendClientInfo backendClientInfo;

        String backendConnectorClientName = msg.getMessageDetails().getConnectorBackendClientName();
        backendClientInfo = getBackendClientInfoByClientNameOrReturnNull(backendConnectorClientName);
        if (backendClientInfo != null) {
            LOGGER.debug("#getBackendClientForMessage: used by message provided backend name [{}] to determine backend [{}]",
                    backendConnectorClientName, backendClientInfo);
            return backendClientInfo;
        }

        // 1 RefToMessageId
        String refToMessageId = msg.getMessageDetails().getRefToMessageId();
        backendClientInfo = getBackendClientInfoByRefToMessageIdOrReturnNull(refToMessageId);
        if (backendClientInfo != null) {
            LOGGER.debug("#getBackendClientForMessage: used refToMessageId [{}] to determine backend [{}]", refToMessageId, backendClientInfo);
            return backendClientInfo;
        }
        LOGGER.debug("#getBackendClientForMessage: refToMessageId [{}] not used to determine backend ", refToMessageId);


        // 2 ConversationId
        String conversationId = msg.getMessageDetails().getConversationId();
        backendClientInfo = getBackendClientInfoByConversationIdOrReturnNull(conversationId);
        if (backendClientInfo != null) {
            LOGGER.debug("#getBackendClientForMessage: used conversationId [{}] to determine backend [{}]", conversationId, backendClientInfo);
            return backendClientInfo;
        }
        LOGGER.debug("#getBackendClientForMessage: conversationId [{}] not used to determine backend ", conversationId);

        // 3 Service
        DomibusConnectorService service = msg.getMessageDetails().getService();
        backendClientInfo =
                backendClientInfoPersistenceService.getEnabledBackendClientInfoByService(service);
        if (backendClientInfo != null) {
            LOGGER.debug("#getBackendClientForMessage: used service [{}] to determine backend [{}]", service, backendClientInfo);
            return backendClientInfo;
        }
        LOGGER.debug("#getBackendClientForMessage: service [{}] not used to determine backend ", service);

        // 4 defaultBackend
        backendClientInfo = backendClientInfoPersistenceService.getDefaultBackendClientInfo();
        if (backendClientInfo != null) {
            LOGGER.debug("#getBackendClientForMessage: default backend to determine backend [{}]", backendClientInfo);
            return backendClientInfo;
        }

        //5 error
        throw new DomibusConnectorBackendException(String.format("No backend found to handle message [%s]", msg));
    }

    private DomibusConnectorBackendClientInfo getBackendClientInfoByClientNameOrReturnNull(String backendConnectorClientName) {
        DomibusConnectorBackendClientInfo backendClientInfo;
        if (backendConnectorClientName != null) {
            backendClientInfo = backendClientInfoPersistenceService.getBackendClientInfoByName(backendConnectorClientName);
            if (backendClientInfo != null) {
                LOGGER.debug("#getBackendClientInfoByClientNameOrReturnNull: used connectorBackendClientName [{}] in messageDetails to determine backend [{}]!",
                        backendConnectorClientName, backendClientInfo);
                return backendClientInfo;
            }
            LOGGER.debug("#getBackendClientInfoByClientNameOrReturnNull: connectorBackendClientName is not set in messageDetails!");
        }
        LOGGER.debug("#getBackendClientInfoByClientNameOrReturnNull: backendConnectorClientName is null returning null!");
        return null;
    }

    private DomibusConnectorBackendClientInfo getBackendClientInfoByRefToMessageIdOrReturnNull(String refToMessageId) {
        if (refToMessageId != null) {
            LOGGER.trace("#getBackendClientInfoByRefToMessageIdOrReturnNull: try to find message by refToMessageId [{}]", refToMessageId);
            DomibusConnectorMessage referencedMessage = messagePersistenceService
                    .findMessageByEbmsIdAndDirection(refToMessageId, DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY)
                    .orElse(messagePersistenceService
                            .findMessageByNationalIdAndDirection(refToMessageId, DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY)
                            .orElse(null));
            if (referencedMessage == null) {
                referencedMessage = messagePersistenceService
                        .findMessageByEbmsIdAndDirection(refToMessageId, DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND)
                        .orElse(messagePersistenceService
                                .findMessageByNationalIdAndDirection(refToMessageId, DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND)
                                .orElse(null));
            }
            if (referencedMessage == null) {
                throw new IllegalStateException(String.format("Referenced message with ebmsid or nationalBackendId [%s] does not exist!", refToMessageId));
            }
            String connectorBackendClientName = referencedMessage.getMessageDetails().getConnectorBackendClientName();
            return backendClientInfoPersistenceService.getEnabledBackendClientInfoByName(connectorBackendClientName);
        } else {
            LOGGER.debug("#getBackendClientInfoByRefToMessageIdOrReturnNull: refToMessageId is null returning null!");
            return null;
        }
    }

    private DomibusConnectorBackendClientInfo getBackendClientInfoByConversationIdOrReturnNull(String conversationId) {
        if (conversationId == null) {
            LOGGER.debug("#getBackendClientInfoByConversationIdOrReturnNull: conversationId is null returning null!");
            return null;
        }

        LOGGER.trace("#getBackendClientInfoByConversationIdOrReturnNull: try to find message by conversationId");
        List<DomibusConnectorMessage> messagesByConversationId = messagePersistenceService.findMessagesByConversationId(conversationId);
        if (messagesByConversationId.isEmpty()) {
            LOGGER.debug("#getBackendClientInfoByConversationIdOrReturnNull: no messages found for conversationId [{}]", conversationId);
            return null;
        }

        DomibusConnectorMessage message = messagesByConversationId.get(0);
        String connectorBackendClientName = message.getMessageDetails().getConnectorBackendClientName();
        if (connectorBackendClientName == null) {
            LOGGER.warn("Message [{}] has no backendClientName set!", message);
            return null;
        }
        DomibusConnectorBackendClientInfo backendClientInfoByServiceName = backendClientInfoPersistenceService.getEnabledBackendClientInfoByName(connectorBackendClientName);
        return backendClientInfoByServiceName;

    }
}
