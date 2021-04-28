
package eu.domibus.connector.link.impl.wsbackendplugin.childctx;

import eu.domibus.connector.controller.exception.DomibusConnectorBackendDeliveryException;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.controller.service.TransportStateService;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageId;
import eu.domibus.connector.domain.model.DomibusConnectorTransportStep;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformerService;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessagesType;
import eu.domibus.connector.link.impl.wsbackendplugin.WsBackendPluginActiveLinkPartner;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import eu.domibus.connector.ws.backend.webservice.EmptyRequestType;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Handles transmitting messages (push/pull) from and to backendClients over webservice
 * pushing messages to backendClients are handled in different service
 */
public class WsBackendServiceEndpointImpl implements DomibusConnectorBackendWebService {

    private static final Logger LOGGER = LogManager.getLogger(WsBackendServiceEndpointImpl.class);

    private WebServiceContext webServiceContext;

    @Autowired
    SubmitToConnector submitToConnector;

    @Autowired
    DomibusConnectorDomainMessageTransformerService transformerService;

    @Autowired
    DCMessagePersistenceService messagePersistenceService;

    @Autowired
    DCActiveLinkManagerService linkManager;

    @Autowired
    TransportStateService transportStateService;

    @Autowired
    DomibusConnectorMessageIdGenerator messageIdGenerator;

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
                        .map(DomibusConnectorTransportStep::getTransportedMessage)
                        .filter(Objects::nonNull)
                        .map(msg -> transformerService.transformDomainToTransition(msg))
                        .collect(Collectors.toList());
                getMessagesResponse.getMessages().addAll(collect);

                registerCleanupTransportInterceptor(pendingTransportsForLinkPartner);

            } else {
                LOGGER.warn("No backend found, returning empty DomibusConnectorMessagesType!");
            }
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        LOGGER.debug("#requestMessages returns messages: [{}]", getMessagesResponse.getMessages().size());
        return getMessagesResponse;
    }

    /**
     * registers an interceptor which updates the transport state to ACCEPTED
     * after the SOAP-message has been sent to the client
     *
     * @param pendingTransportsForLinkPartner - all transportIds to set to ACCEPTED
     *
     */
    private void registerCleanupTransportInterceptor(List<DomibusConnectorTransportStep> pendingTransportsForLinkPartner) {
        MessageContext mContext = webServiceContext.getMessageContext();
        WrappedMessageContext wmc = (WrappedMessageContext)mContext;
        ProcessMessageAfterDeliveredToBackendInterceptor interceptor = new ProcessMessageAfterDeliveredToBackendInterceptor(pendingTransportsForLinkPartner.stream().map(DomibusConnectorTransportStep::getTransportId).collect(Collectors.toList()));
        wmc.getWrappedMessage().getInterceptorChain().add(interceptor);
    }


    @Override
    public DomibsConnectorAcknowledgementType submitMessage(DomibusConnectorMessageType submitMessageRequest) {
        DomibusConnectorMessageId domibusConnectorMessageId = messageIdGenerator.generateDomibusConnectorMessageId();
        try (MDC.MDCCloseable conId = MDC.putCloseable(LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_ID_PROPERTY_NAME, domibusConnectorMessageId.getConnectorMessageId())
        ) {
            DomibsConnectorAcknowledgementType answer = new DomibsConnectorAcknowledgementType();
            try {
                LOGGER.debug("#submitMessage: message: [{}]", submitMessageRequest);

                Optional<DomibusConnectorLinkPartner> backendClientInfoByName = checkBackendClient();

                if (backendClientInfoByName.isPresent()) {
                    DomibusConnectorLinkPartner linkPartner = backendClientInfoByName.get();
                    try(MDC.MDCCloseable mdc = MDC.putCloseable(LoggingMDCPropertyNames.MDC_LINK_PARTNER_NAME, linkPartner.getLinkPartnerName().getLinkName())) {

                        DomibusConnectorMessage msg = transformerService.transformTransitionToDomain(submitMessageRequest, domibusConnectorMessageId);
                        msg.getMessageDetails().setConnectorBackendClientName(linkPartner.getLinkPartnerName().getLinkName());
                        LOGGER.debug("#submitMessage: setConnectorBackendClientName to [{}]", linkPartner);

                        submitToConnector.submitToConnector(msg, linkPartner);
                        answer.setResult(true);
                        answer.setMessageId(msg.getConnectorMessageIdAsString());
                    }
                } else {
                    java.lang.String error = java.lang.String.format("The requested backend user is not available on connector!\nCheck server logs for details!");
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
    }

    @Autowired
    WsActiveLinkPartnerManager wsActiveLinkPartnerManager;

    private Optional<DomibusConnectorLinkPartner> checkBackendClient() throws DomibusConnectorBackendDeliveryException {
        if (this.webServiceContext == null) {
            throw new RuntimeException("No webServiceContext found");
        }
        Principal userPrincipal = webServiceContext.getUserPrincipal();
        java.lang.String certificateDn = userPrincipal == null ? null : userPrincipal.getName();
        if (userPrincipal == null || certificateDn == null) {
            java.lang.String error = java.lang.String.format("checkBackendClient: Cannot handle request because userPrincipal is [%s] the userName is [%s]. Cannot identify backend!", userPrincipal, certificateDn);
            LOGGER.error("#checkBackendClient: Throwing Exception: {}", error);
            throw new DomibusConnectorBackendDeliveryException(error);
        }
        Optional<WsBackendPluginActiveLinkPartner> linkPartner =  wsActiveLinkPartnerManager.getDomibusConnectorLinkPartnerByDn(certificateDn);
        if (!linkPartner.isPresent()) {
            LOGGER.warn("No backend with certificate dn [{}] found!", certificateDn);
        } else {
            LOGGER.debug("#checkBackendClient: returning link partner: [{}]", linkPartner.get());
        }
        return linkPartner.map(WsBackendPluginActiveLinkPartner::getLinkPartner);

    }


    private class ProcessMessageAfterDeliveredToBackendInterceptor extends AbstractPhaseInterceptor<Message> {

        private final List<TransportStateService.TransportId> transports;

        ProcessMessageAfterDeliveredToBackendInterceptor(List<TransportStateService.TransportId> transports) {
            super(Phase.POST_INVOKE);
            this.transports = transports;
        }

        @Override
        public void handleMessage(Message message) throws Fault {
            LOGGER.trace("ProcessMessageAfterDeliveredToBackendInterceptor: handleMessage: invoking backendSubmissionService.processMessageAfterDeliveredToBackend");

            transports.stream().forEach(transportId -> {
                TransportStateService.DomibusConnectorTransportState transportState = new TransportStateService.DomibusConnectorTransportState();
                transportState.setStatus(TransportState.ACCEPTED);
                transportStateService.updateTransportToBackendClientStatus(transportId, transportState);
            });
        }
    }

}
