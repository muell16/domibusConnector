
package eu.domibus.connector.backend.ws.link.impl;

import java.security.Principal;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.controller.exception.DomibusConnectorBackendDeliveryException;
import eu.domibus.connector.backend.persistence.service.BackendClientInfoPersistenceService;
import eu.domibus.connector.backend.service.DomibusConnectorBackendInternalDeliverToController;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformerService;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessagesType;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import eu.domibus.connector.ws.backend.webservice.EmptyRequestType;

import static eu.domibus.connector.backend.ws.link.impl.DomibusConnectorWsBackendImpl.BEAN_NAME;
import static eu.domibus.connector.backend.ws.link.spring.WSBackendLinkContextConfiguration.WS_BACKEND_LINK_PROFILE;

/**
 * Handles transmitting messages (push/pull) from and to backendClients over webservice
 * pushing messages to backendClients are handled in different service: {@link PushMessageViaWsToBackendClientImpl}
 */
@Profile(WS_BACKEND_LINK_PROFILE)
@Service(BEAN_NAME)
@Qualifier(BEAN_NAME)
public class DomibusConnectorWsBackendImpl implements DomibusConnectorBackendWebService {

    public static final String BEAN_NAME = "connectorBackendImpl";

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorWsBackendImpl.class);

    private WebServiceContext webServiceContext;
    private BackendClientInfoPersistenceService backendClientInfoPersistenceService;
    private MessageToBackendClientWaitQueue messageToBackendClientWaitQueue;
    private DomibusConnectorBackendInternalDeliverToController backendSubmissionService;


    //setter
    @Resource
    public void setWsContext(WebServiceContext webServiceContext) {
        this.webServiceContext = webServiceContext;
    }

    @Autowired
    public void setBackendClientInfoPersistenceService(BackendClientInfoPersistenceService backendClientInfoPersistenceService) {
        this.backendClientInfoPersistenceService = backendClientInfoPersistenceService;
    }

    @Autowired
    public void setMessageToBackendClientWaitQueue(MessageToBackendClientWaitQueue messageToBackendClientWaitQueue) {
        this.messageToBackendClientWaitQueue = messageToBackendClientWaitQueue;
    }

    @Autowired
    public void setBackendSubmissionService(DomibusConnectorBackendInternalDeliverToController backendSubmissionService) {
        this.backendSubmissionService = backendSubmissionService;
    }


    @Override
    public DomibusConnectorMessagesType requestMessages(EmptyRequestType requestMessagesRequest) {
        DomibusConnectorBackendClientInfo backendClientInfoByName = null;
        try {
            backendClientInfoByName = checkBackendClient();
            DomibusConnectorMessagesType retrieveWaitingMessagesFromQueue = retrieveWaitingMessagesFromQueue(backendClientInfoByName);
            return retrieveWaitingMessagesFromQueue;
        } catch (DomibusConnectorBackendDeliveryException e) {
            LOGGER.error("Exception caught retrieving Messages from the backend queue!", e);
            return null;
        }
    }

    @Transactional
    public DomibusConnectorMessagesType retrieveWaitingMessagesFromQueue(DomibusConnectorBackendClientInfo backendInfo) throws DomibusConnectorBackendDeliveryException {
        DomibusConnectorMessagesType messagesType = new DomibusConnectorMessagesType();
        MessageContext mContext = webServiceContext.getMessageContext();
        if (mContext == null) {
            throw new RuntimeException("Retrieved MessageContext mContext from WebServiceContext is null!");
        }
        WrappedMessageContext wmc = (WrappedMessageContext)mContext;
        Message m = wmc.getWrappedMessage();
        if (m == null) {
            throw new RuntimeException("Retrieved Message m from WrappedMessageContext is null!");
        }

        try {
            List<DomibusConnectorMessage> messageIds = messageToBackendClientWaitQueue.getConnectorMessageIdForBackend(backendInfo.getBackendName());
            messageIds.stream()
                    .forEach((message) -> {
                        messagesType.getMessages().add(transformDomibusConnectorMessageToTransitionMessage(message));
                        m.getInterceptorChain().add(new ProcessMessageAfterDeliveredToBackendInterceptor(message));

                    });
        } catch (Exception e) {
            throw new DomibusConnectorBackendDeliveryException("Exception caught retrieving messages from backend queue!", e);
        }
        return messagesType;
    }

    private DomibusConnectorMessageType transformDomibusConnectorMessageToTransitionMessage(DomibusConnectorMessage message) {
        DomibusConnectorMessage processedMessage = backendSubmissionService.processMessageBeforeDeliverToBackend(message);
        return DomibusConnectorDomainMessageTransformerService.transformDomainToTransition(processedMessage);
    }


    @Override
    public DomibsConnectorAcknowledgementType submitMessage(DomibusConnectorMessageType submitMessageRequest) {
        DomibsConnectorAcknowledgementType answer = new DomibsConnectorAcknowledgementType();
        try {
            LOGGER.debug("#submitMessage: message: [{}]", submitMessageRequest);
            DomibusConnectorBackendClientInfo backendClientInfoByName = null;
            backendClientInfoByName = checkBackendClient();

            DomibusConnectorMessage msg = DomibusConnectorDomainMessageTransformerService.transformTransitionToDomain(submitMessageRequest);
            msg.getMessageDetails().setConnectorBackendClientName(backendClientInfoByName.getBackendName());
            LOGGER.debug("#submitMessage: setConnectorBackendClientName to [{}]", backendClientInfoByName.getBackendName());

            DomibusConnectorBackendMessage backendMessage = new DomibusConnectorBackendMessage();
            backendMessage.setDomibusConnectorMessage(msg);
            backendMessage.setBackendClientInfo(backendClientInfoByName);

            backendSubmissionService.submitToController(backendMessage);

            answer.setResult(true);
            answer.setMessageId(msg.getConnectorMessageId());

        } catch (Exception e) {
            LOGGER.error("Exception occured during submitMessage from backend", e);
            answer.setResult(false);
            answer.setMessageId(submitMessageRequest.getMessageDetails().getBackendMessageId());
            answer.setResultMessage(e.getMessage());
        }
        return answer;
    }


    private DomibusConnectorBackendClientInfo checkBackendClient() throws DomibusConnectorBackendDeliveryException {
        Principal userPrincipal = webServiceContext.getUserPrincipal();
        String backendName = userPrincipal == null ? null : userPrincipal.getName();
        if (userPrincipal == null || backendName == null) {
            String error = String.format("checkBackendClient: Cannot handle request because userPrincipal is [%s] the userName is [%s]. Cannot identify backend!", userPrincipal, backendName);
            LOGGER.error("#checkBackendClient: Throwing Exception: {}", error);
            throw new DomibusConnectorBackendDeliveryException(error);
        }
        DomibusConnectorBackendClientInfo backendClientInfoByName;

        backendName = backendName.toLowerCase();
        backendClientInfoByName = backendClientInfoPersistenceService.getEnabledBackendClientInfoByName(backendName);

        if (backendClientInfoByName == null) {

            String error = String.format("#checkBackendClient: No backend with name [%s] configured on connector!", backendName);
            //should be marked deprecated the removal of leading cn=
            backendName = backendName.toLowerCase();
            backendName = "cn=".equals(backendName.substring(0, 3)) ? backendName.replaceFirst("cn=", "") : backendName;
            //replace leading "cn=" with "" so common name cn=alice becomes alice

            LOGGER.warn("#checkBackendClient: {}, Looking for 4.0 compatible connector backend naming [{}]", error, backendName);
            backendClientInfoByName = backendClientInfoPersistenceService.getEnabledBackendClientInfoByName(backendName);
        }
        if (backendClientInfoByName == null) {
            String error = String.format("#checkBackendClient: No backend with name [%s] configured on connector!\n" +
                    "Connector takes the FQDN of the certificate and startes looking if in the database a backend with this name is found\n" +
                    "Connector always converts the fqdn of the certificate to lower case letters!", backendName);
            LOGGER.error("#checkBackendClient: Throwing Exception: {}", error);
            throw new DomibusConnectorBackendDeliveryException(error);
        }

        return backendClientInfoByName;
    }


    private class ProcessMessageAfterDeliveredToBackendInterceptor extends AbstractPhaseInterceptor<Message> {

        private final DomibusConnectorMessage connectorMessage;

        ProcessMessageAfterDeliveredToBackendInterceptor(DomibusConnectorMessage connectorMessage) {
            super(Phase.POST_INVOKE);
            this.connectorMessage = connectorMessage;
        }

        @Override
        public void handleMessage(Message message) throws Fault {
            LOGGER.trace("ProcessMessageAfterDeliveredToBackendInterceptor: handleMessage: invoking backendSubmissionService.processMessageAfterDeliveredToBackend");
            backendSubmissionService.processMessageAfterDeliveredToBackend(connectorMessage);
        }
    }

}
