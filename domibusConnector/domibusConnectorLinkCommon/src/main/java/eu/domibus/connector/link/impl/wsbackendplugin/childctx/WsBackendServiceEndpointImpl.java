
package eu.domibus.connector.link.impl.wsbackendplugin.childctx;

import eu.domibus.connector.controller.exception.DomibusConnectorBackendDeliveryException;
import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.controller.service.TransportStateService;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorTransportStep;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformerService;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessagesType;
import eu.domibus.connector.link.impl.wsbackendplugin.WsBackendPluginActiveLinkPartner;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import eu.domibus.connector.ws.backend.webservice.EmptyRequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Handles transmitting messages (push/pull) from and to backendClients over webservice
 * pushing messages to backendClients are handled in different service
 */
public class WsBackendServiceEndpointImpl implements DomibusConnectorBackendWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WsBackendServiceEndpointImpl.class);

    private WebServiceContext webServiceContext;

    @Autowired
    SubmitToConnector submitToConnector;

    @Autowired
    DomibusConnectorDomainMessageTransformerService transformerService;

    @Autowired
    DomibusConnectorMessagePersistenceService messagePersistenceService;

    @Autowired
    DCActiveLinkManagerService linkManager;

    @Autowired
    TransportStateService transportStateService;

    @Resource
    public void setWsContext(WebServiceContext webServiceContext) {
        this.webServiceContext = webServiceContext;
    }

    @Override
    public DomibusConnectorMessagesType requestMessages(EmptyRequestType requestMessagesRequest) {
        DomibusConnectorMessagesType getMessagesResponse = new DomibusConnectorMessagesType();
        try {
            Optional<DomibusConnectorLinkPartner> backendClientInfoByName = checkBackendClient();
            if (backendClientInfoByName.isPresent()) {
                List<DomibusConnectorTransportStep> pendingTransportsForLinkPartner = transportStateService.getPendingTransportsForLinkPartner(backendClientInfoByName.get().getLinkPartnerName());
                List<DomibusConnectorMessageType> collect = pendingTransportsForLinkPartner.stream()
                        .map(step -> step.getMessageId())
                        .map(msgId -> messagePersistenceService.findMessageByConnectorMessageId(msgId.getConnectorMessageId()))
                        .map(msg -> transformerService.transformDomainToTransition(msg))
                        .collect(Collectors.toList());
                getMessagesResponse.getMessages().addAll(collect);

            } else {
                LOGGER.warn("No backend found!");
            }
        } catch (Exception e) {

        }
        return getMessagesResponse;
    }


    @Override
    public DomibsConnectorAcknowledgementType submitMessage(DomibusConnectorMessageType submitMessageRequest) {
        DomibsConnectorAcknowledgementType answer = new DomibsConnectorAcknowledgementType();
        try {
            LOGGER.debug("#submitMessage: message: [{}]", submitMessageRequest);

            Optional<DomibusConnectorLinkPartner> backendClientInfoByName = checkBackendClient();
            if (backendClientInfoByName.isPresent()) {
                DomibusConnectorLinkPartner linkPartner = backendClientInfoByName.get();
                DomibusConnectorMessage msg = transformerService.transformTransitionToDomain(submitMessageRequest);
                msg.getMessageDetails().setConnectorBackendClientName(linkPartner.getLinkPartnerName().getLinkName());
                LOGGER.debug("#submitMessage: setConnectorBackendClientName to [{}]", linkPartner);

                submitToConnector.submitToConnector(msg, linkPartner);
                answer.setResult(true);
                answer.setMessageId(msg.getConnectorMessageId());

            } else {
                String error = String.format("The requested backend user is not available on connector!\nCheck server logs for details!");
                throw new RuntimeException(error);
            }

        } catch (Exception e) {
            LOGGER.error("Exception occured during submitMessage from backend", e);
            answer.setResult(false);
            answer.setMessageId(submitMessageRequest.getMessageDetails().getBackendMessageId());
            answer.setResultMessage(e.getMessage());
        }
        return answer;
    }

    @Autowired
    WsActiveLinkPartnerManager wsActiveLinkPartnerManager;

    private Optional<DomibusConnectorLinkPartner> checkBackendClient() throws DomibusConnectorBackendDeliveryException {
        if (this.webServiceContext == null) {
            throw new RuntimeException("No webServiceContext found");
        }
        Principal userPrincipal = webServiceContext.getUserPrincipal();
        String certificateDn = userPrincipal == null ? null : userPrincipal.getName();
        if (userPrincipal == null || certificateDn == null) {
            String error = String.format("checkBackendClient: Cannot handle request because userPrincipal is [%s] the userName is [%s]. Cannot identify backend!", userPrincipal, certificateDn);
            LOGGER.error("#checkBackendClient: Throwing Exception: {}", error);
            throw new DomibusConnectorBackendDeliveryException(error);
        }
        Optional<WsBackendPluginActiveLinkPartner> linkPartner =  wsActiveLinkPartnerManager.getDomibusConnectorLinkPartnerByDn(certificateDn);
        if (!linkPartner.isPresent()) {
            LOGGER.warn("No backend with certificate dn [{}] found!", certificateDn);
        }
//        certificateDn = certificateDn.toLowerCase();
        return linkPartner.map(WsBackendPluginActiveLinkPartner::getLinkPartner);

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
