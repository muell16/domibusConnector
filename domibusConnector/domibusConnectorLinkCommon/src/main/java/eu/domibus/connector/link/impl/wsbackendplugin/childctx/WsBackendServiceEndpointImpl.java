
package eu.domibus.connector.link.impl.wsbackendplugin.childctx;

import eu.domibus.connector.controller.exception.DomibusConnectorBackendDeliveryException;
import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformerService;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessagesType;
import eu.domibus.connector.link.api.ActiveLinkPartnerManager;
import eu.domibus.connector.link.impl.wsplugin.DCWsEndpointAuthentication;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import eu.domibus.connector.ws.backend.webservice.EmptyRequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import java.security.Principal;
import java.util.Optional;

/**
 * Handles transmitting messages (push/pull) from and to backendClients over webservice
 * pushing messages to backendClients are handled in different service
 */
//@Profile(WS_BACKEND_LINK_PROFILE)
//@Service(BEAN_NAME)
//@Qualifier(BEAN_NAME)
public class WsBackendServiceEndpointImpl implements DomibusConnectorBackendWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WsBackendServiceEndpointImpl.class);

    private WebServiceContext webServiceContext;


//    @Autowired
//    DCWsEndpointAuthentication endpointAuthenticator;

    @Autowired
    SubmitToConnector submitToConnector;

    @Autowired
    DomibusConnectorDomainMessageTransformerService transformerService;

    @Autowired
    WsActiveLinkPartnerManager WsActiveLinkPartnerManager;

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
            ActiveLinkPartnerManager backendClientInfoByName = null;
//            backendClientInfoByName = endpointAuthenticator.checkBackendClient(webServiceContext).get();

            DomibusConnectorMessage msg = transformerService.transformTransitionToDomain(submitMessageRequest);
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


    private Optional<ActiveLinkPartnerManager> checkBackendClient() throws DomibusConnectorBackendDeliveryException {
        if (this.webServiceContext == null) {
            throw new RuntimeException("No webServiceContext found");
        }
        Principal userPrincipal = webServiceContext.getUserPrincipal();
        String backendName = userPrincipal == null ? null : userPrincipal.getName();
        if (userPrincipal == null || backendName == null) {
            String error = String.format("checkBackendClient: Cannot handle request because userPrincipal is [%s] the userName is [%s]. Cannot identify backend!", userPrincipal, backendName);
            LOGGER.error("#checkBackendClient: Throwing Exception: {}", error);
            throw new DomibusConnectorBackendDeliveryException(error);
        }

        backendName = backendName.toLowerCase();
        return null;
//        backendClientInfoByName = activeLink.getActiveLinkPartner(new DomibusConnectorLinkPartner.LinkPartnerName(backendName)); //.orElse(null);

//        backendClientInfoByName = backendClientInfoPersistenceService.getEnabledBackendClientInfoByName(backendName);

//        if (backendClientInfoByName == null) {
//
//            String error = String.format("#checkBackendClient: No backend with name [%s] configured on connector!", backendName);
//            //should be marked deprecated the removal of leading cn=
//            backendName = backendName.toLowerCase();
//            backendName = "cn=".equals(backendName.substring(0, 3)) ? backendName.replaceFirst("cn=", "") : backendName;
//            //replace leading "cn=" with "" so common name cn=alice becomes alice
//
//            LOGGER.warn("#checkBackendClient: {}, Looking for 4.0.x compatible connector backend naming [{}]", error, backendName);
////            backendClientInfoByName = activeLink.getActiveLinkPartner(new DomibusConnectorLinkPartner.LinkPartnerName(backendName));
//        }
//        if (backendClientInfoByName == null) {
//            String error = String.format("#checkBackendClient: No link partner with name [%s] configured on connector!\n" +
//                    "Connector takes the FQDN of the certificate and starts looking if a active link partner with this name is found\n" +
//                    "Connector always converts the fqdn of the certificate to lower case letters!", backendName);
//            LOGGER.error("#checkBackendClient: Throwing Exception: {}", error);
//            throw new DomibusConnectorBackendDeliveryException(error);
//        }
//
//        return backendClientInfoByName;
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
