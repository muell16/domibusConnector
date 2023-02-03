package eu.ecodex.dc5.flow.flows;


import eu.domibus.connector.controller.service.SubmitToLinkService;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.enums.TransportState;
import eu.ecodex.dc5.domain.CurrentBusinessDomain;
import eu.ecodex.dc5.events.DC5EventListener;
import eu.ecodex.dc5.flow.common.SubmitConfirmationMsg;
import eu.ecodex.dc5.flow.events.MessageReadyForTransportEvent;
import eu.ecodex.dc5.flow.events.MessageTransportEvent;
import eu.ecodex.dc5.flow.events.OutgoingBusinessMessageTransportEvent;
import eu.ecodex.dc5.message.ConfirmationCreatorService;
import eu.ecodex.dc5.message.model.*;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import eu.ecodex.dc5.process.MessageProcessManager;
import eu.ecodex.dc5.transport.model.DC5TransportRequest;
import eu.ecodex.dc5.transport.model.DC5TransportRequestState;
import eu.ecodex.dc5.transport.repo.DC5TransportRequestRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class TransportMessageFlow {

    private final SubmitToLinkService submitToLinkService;
    private final DC5MessageRepo messageRepo;
    private final DC5TransportRequestRepo dc5TransportRequestRepo;
    private final ConfirmationCreatorService confirmationCreatorService;
    private final ApplicationEventPublisher eventPublisher;
    private final MessageProcessManager messageProcessManager;
    private final SubmitConfirmationMsg submitConfirmationMsg;

    //@DC5EventListener //implicit transactional and resumes implicit current message process
    @EventListener
    public void handleMessageReadyForTransport(MessageReadyForTransportEvent messageReadyForTransportEvent) {

        DC5Message msg = messageReadyForTransportEvent.getMessage();

        DC5TransportRequest transportRequest = new DC5TransportRequest();
        transportRequest.setMessage(msg);
        transportRequest.setLinkType(messageReadyForTransportEvent.getTarget());
        transportRequest.setLinkName(messageReadyForTransportEvent.getLinkName());
        transportRequest.setTransportRequestId(DC5TransportRequest.TransportRequestId.ofRandom());

        transportRequest.changeCurrentState(DC5TransportRequestState.builder()
                    .created(LocalDateTime.now())
                    .transportState(TransportState.PENDING)
                .build());
        dc5TransportRequestRepo.save(transportRequest);
        log.info("Created Transport Request [{}]", transportRequest);

        SubmitToLinkService.SubmitToLinkEvent build = SubmitToLinkService.SubmitToLinkEvent.builder()
                .transportRequestId(transportRequest.getTransportRequestId())
                .linkName(messageReadyForTransportEvent.getLinkName())
                .target(messageReadyForTransportEvent.getTarget())
                .build();

        //TODO: decouple!
        submitToLinkService.submitToLink(build);

    }

//    @DC5EventListener
    //TODO: restore correct Message Process here...
    @EventListener
    @Transactional
    public void handleMessageTransportEvent(MessageTransportEvent messageTransportEvent) {
        try (MessageProcessManager.CloseableMessageProcess msgProcess = messageProcessManager.startProcess()) {

            DC5TransportRequestState.DC5TransportRequestStateBuilder transportStateBuilder = DC5TransportRequestState.builder()
                    .created(LocalDateTime.now());

            TransportState state = messageTransportEvent.getState();
            transportStateBuilder.transportState(state);

            DC5TransportRequest transportRequest = dc5TransportRequestRepo.getById(messageTransportEvent.getTransportId());
            transportRequest.changeCurrentState(transportStateBuilder.build());
            DC5Message transportedMessage = transportRequest.getMessage();
            CurrentBusinessDomain.setCurrentBusinessDomain(transportedMessage.getBusinessDomainId());

            messageTransportEvent.getRemoteTransportId().ifPresent(transportRequest::setRemoteMessageId);
            messageTransportEvent.getTransportSystemId().ifPresent(transportRequest::setTransportSystemId);

            Optional<String> remoteTransportId = messageTransportEvent.getRemoteTransportId();
            if (remoteTransportId.isPresent()) {

                if (transportRequest.getLinkType() == MessageTargetSource.GATEWAY) {
                    //should be ebms id
                    EbmsMessageId ebmsMessageId = EbmsMessageId.ofString(remoteTransportId.get());
                    if (transportRequest.getMessage().getEbmsData() == null) {
                        transportRequest.getMessage().setEbmsData(new DC5Ebms());
                    }
                    transportRequest.getMessage().getEbmsData().setEbmsMessageId(ebmsMessageId);
                } else if (transportRequest.getLinkType() == MessageTargetSource.BACKEND) {
                    //should be backend id
                    BackendMessageId backendMessageId = BackendMessageId.ofString(remoteTransportId.get());
                    if (transportRequest.getMessage().getBackendData() == null) {
                        transportRequest.getMessage().setBackendData(new DC5BackendData());
                    }
                    transportRequest.getMessage().getBackendData().setBackendMessageId(backendMessageId);
                } else {
                    throw new IllegalArgumentException("Target is not set of message transport!");
                }
            }

            if (MessageModelHelper.isOutgoingBusinessMessage(transportedMessage)) {
                eventPublisher.publishEvent(OutgoingBusinessMessageTransportEvent.builder()
                        .transportState(messageTransportEvent.getState())
                        .message(transportRequest.getMessage())
                        .transportRequest(transportRequest)
                        .build());
            }


            if (messageTransportEvent.getState() == TransportState.FAILED) {
                //TODO: make behaviour configureable!!

//                if (MessageModelHelper.isOutgoingBusinessMessage(transportedMessage)) {
//                    //message failed to be transported to gw
//                    DC5Confirmation submissionRejection = confirmationCreatorService.createConfirmation(DomibusConnectorEvidenceType.SUBMISSION_REJECTION,
//                            transportedMessage,
//                            DomibusConnectorRejectionReason.GW_REJECTION,
//                            "");
//
//                    submitConfirmationMsg(transportedMessage, submissionRejection);
//                    //submit trigger message...
//                }

                if (MessageModelHelper.isIncomingBusinessMessage(transportedMessage)) {
                    //message failed to be transported to backend

                    DC5Confirmation nonDelivery = confirmationCreatorService.createConfirmation(DomibusConnectorEvidenceType.NON_DELIVERY,
                            transportedMessage,
                            DomibusConnectorRejectionReason.BACKEND_REJECTION,
                            "");

                    submitConfirmationMsg(transportedMessage, nonDelivery);
                }

                //TRIGGER NON_DELIVERY , SUBMISSION_REJECTION , RELAY_REMMD_FAILURE ?
//            MessageTransportFailedEvent.MessageTransportFailedEventBuilder builder = MessageTransportFailedEvent.builder()
//                    .transportId(messageTransportEvent.getTransportId())
//                    .target(MessageTargetSource.GATEWAY)
            }

            if (messageTransportEvent.getState() == TransportState.ACCEPTED) {


//                if (MessageModelHelper.isOutgoingBusinessMessage(transportedMessage) && processingProperties.isSendGeneratedEvidencesToBackend()) {
//                    DC5Confirmation submissionAcceptance = transportedMessage.getTransportedMessageConfirmations().get(0); //should be submission acceptance
//                    if (submissionAcceptance.getEvidenceType() != DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE) {
//                        throw new IllegalStateException("Cannot continue, the wrong evidence has been packed into outgoing business message!");
//                    }
//                    submitConfirmationMsg(transportedMessage, submissionAcceptance);
//                    //submit trigger message...
//                }
// DOES NOT WORK, cannot create NON_DELIVERY AFTER RELAY_REMMD_REJECTION
//                boolean isOutgoingRelayRemmdRejectionConfirmationMsg = MessageModelHelper.isEvidenceMessage(transportedMessage) && transportedMessage.getTarget() == MessageTargetSource.GATEWAY &&
//                        !transportedMessage.getTransportedMessageConfirmations().isEmpty() &&
//                        transportedMessage.getTransportedMessageConfirmations().get(0).getEvidenceType() == DomibusConnectorEvidenceType.RELAY_REMMD_REJECTION;
//                if (isOutgoingRelayRemmdRejectionConfirmationMsg) {
//                    Optional<DC5Message> businessMsgByRefToMsgId = findBusinessMessageByMsgId.findBusinessMsgByRefToMsgId(transportedMessage);
//                    if (!businessMsgByRefToMsgId.isPresent()) {
//                        throw new IllegalStateException("A confirmation message must be related to a business message!");
//                    }
//                    DC5Confirmation nonDeliveryEvidence = createNonDeliveryEvidence(businessMsgByRefToMsgId.get());
//                    submitConfirmationMsg(businessMsgByRefToMsgId.get(), nonDeliveryEvidence);
//                }


            }

            //TODO: save transport update...

            // update message attributes (backend messageId, ebmsId,
            // send already in message existing submission_acceptance (when submission success)
            // trigger submission_rejection (when submission to gw failed)
            // trigger non_delivery (when transport to backend failed)
        }
    }

    /**
     * Create a evidence message with the given confirmation and send it in
     * opposite direction to given business message
     * @param businessMessage - the given business message
     * @param confirmation - the confirmation
     */
    private void submitConfirmationMsg(DC5Message businessMessage, DC5Confirmation confirmation) {
        submitConfirmationMsg.submitConfirmationMsg(businessMessage, confirmation);
    }

}
