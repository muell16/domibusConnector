
package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformerService;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessagesType;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import eu.domibus.connector.ws.backend.webservice.EmptyRequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 * Handles transmitting messages (push/pull) from and to backendClients over webservice
 * pushing messages to backendClients are handled in different service
 */
//@Profile(WS_BACKEND_LINK_PROFILE)
//@Service(BEAN_NAME)
//@Qualifier(BEAN_NAME)
public class DCWsBackendServiceEndpointImpl implements DomibusConnectorBackendWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DCWsBackendServiceEndpointImpl.class);

    private WebServiceContext webServiceContext;


    @Autowired
    DCWsEndpointAuthentication endpointAuthenticator;

    @Autowired
    SubmitToConnector submitToConnector;



    @Resource
    public void setWsContext(WebServiceContext webServiceContext) {
        this.webServiceContext = webServiceContext;
    }

    @Override
    public DomibusConnectorMessagesType requestMessages(EmptyRequestType requestMessagesRequest) {
//        DomibusConnectorBackendClientInfo backendClientInfoByName = null;
//        try {
//            backendClientInfoByName = checkBackendClient();
//            DomibusConnectorMessagesType retrieveWaitingMessagesFromQueue = retrieveWaitingMessagesFromQueue(backendClientInfoByName);
//            return retrieveWaitingMessagesFromQueue;
//        } catch (DomibusConnectorBackendDeliveryException e) {
//            LOGGER.error("Exception caught retrieving Messages from the backend queue!", e);
//            return null;
//        }
        //TODO: return all pending messages...
        return new DomibusConnectorMessagesType();
    }

//    @Transactional
//    public DomibusConnectorMessagesType retrieveWaitingMessagesFromQueue(DomibusConnectorBackendClientInfo backendInfo) throws DomibusConnectorBackendDeliveryException {
//        DomibusConnectorMessagesType messagesType = new DomibusConnectorMessagesType();
//        MessageContext mContext = webServiceContext.getMessageContext();
//        if (mContext == null) {
//            throw new RuntimeException("Retrieved MessageContext mContext from WebServiceContext is null!");
//        }
//        WrappedMessageContext wmc = (WrappedMessageContext)mContext;
//        Message m = wmc.getWrappedMessage();
//        if (m == null) {
//            throw new RuntimeException("Retrieved Message m from WrappedMessageContext is null!");
//        }
//
////        //TODO: put interceptor for message deletion in here!
////        try {
////            List<DomibusConnectorMessage> messageIds = messageToBackendClientWaitQueue.getConnectorMessageIdForBackend(backendInfo.getBackendName());
////            messageIds.stream()
////                    .forEach((message) -> {
////                        messagesType.getMessages().add(transformDomibusConnectorMessageToTransitionMessage(message));
////                        m.getInterceptorChain().add(new ProcessMessageAfterDeliveredToBackendInterceptor(message));
////
////                    });
////        } catch (Exception e) {
////            throw new DomibusConnectorBackendDeliveryException("Exception caught retrieving messages from backend queue!", e);
////        }
//        return messagesType;
//    }
//
//    private DomibusConnectorMessageType transformDomibusConnectorMessageToTransitionMessage(DomibusConnectorMessage message) {
////        DomibusConnectorMessage processedMessage = backendSubmissionService.processMessageBeforeDeliverToBackend(message);
//        return DomibusConnectorDomainMessageTransformer.transformDomainToTransition(message);
//    }


    @Override
    public DomibsConnectorAcknowledgementType submitMessage(DomibusConnectorMessageType submitMessageRequest) {
        DomibsConnectorAcknowledgementType answer = new DomibsConnectorAcknowledgementType();
        try {
            LOGGER.debug("#submitMessage: message: [{}]", submitMessageRequest);
            ActiveLinkPartner backendClientInfoByName = null;
            backendClientInfoByName = endpointAuthenticator.checkBackendClient(webServiceContext);

            DomibusConnectorMessage msg = DomibusConnectorDomainMessageTransformerService.transformTransitionToDomain(submitMessageRequest);
            msg.getMessageDetails().setConnectorBackendClientName(backendClientInfoByName.getLinkPartnerName().getLinkName());
            LOGGER.debug("#submitMessage: setConnectorBackendClientName to [{}]", backendClientInfoByName.getLinkPartnerName().getLinkName());


            submitToConnector.submitToConnector(msg, backendClientInfoByName.getLinkPartner());


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





//    private class ProcessMessageAfterDeliveredToBackendInterceptor extends AbstractPhaseInterceptor<Message> {
//
//        private final DomibusConnectorMessage connectorMessage;
//
//        ProcessMessageAfterDeliveredToBackendInterceptor(DomibusConnectorMessage connectorMessage) {
//            super(Phase.POST_INVOKE);
//            this.connectorMessage = connectorMessage;
//        }
//
//        @Override
//        public void handleMessage(Message message) throws Fault {
//            LOGGER.trace("ProcessMessageAfterDeliveredToBackendInterceptor: handleMessage: invoking backendSubmissionService.processMessageAfterDeliveredToBackend");
////            backendSubmissionService.processMessageAfterDeliveredToBackend(connectorMessage);
//        }
//    }

}
