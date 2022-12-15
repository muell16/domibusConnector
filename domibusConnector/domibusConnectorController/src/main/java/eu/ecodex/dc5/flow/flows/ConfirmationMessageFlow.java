package eu.ecodex.dc5.flow.flows;

import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.ecodex.dc5.flow.events.MessageReadyForTransportEvent;
import eu.ecodex.dc5.flow.steps.MessageConfirmationStep;
import eu.ecodex.dc5.flow.steps.ValidateMessageConfirmationStep;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.ecodex.dc5.flow.steps.EvidenceTriggerStep;
import eu.ecodex.dc5.message.ConfirmationCreatorService;
import eu.ecodex.dc5.message.FindBusinessMessageByMsgId;
import eu.ecodex.dc5.message.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ConfirmationMessageFlow {


    private final EvidenceTriggerStep evidenceTriggerStep;
    private final FindBusinessMessageByMsgId findBusinessMessageByMsgId;
    private final ValidateMessageConfirmationStep validateMessageConfirmationStep;
    private final MessageConfirmationStep messageConfirmationStep;


    private final ApplicationEventPublisher eventPublisher;

    public void processMessage(DC5Message message) {
        // validate message

        if (MessageModelHelper.isEvidenceTriggerMessage(message)) {
//            confirmationCreatorService.createConfirmation()

            evidenceTriggerStep.executeStep(message);

        }

        DomibusConnectorMessageDirection revertedDirection = DomibusConnectorMessageDirection.revert(message.getDirection());
        DC5Message businessMsg = findBusinessMessageByMsgId.findBusinessMessageByIdAndDirection(message, revertedDirection);



        //set ref to backend message id (backendId of business message)
        BackendMessageId businessMessageBackendId = businessMsg.getBackendData().getBackendMessageId();
        log.debug("Setting refToBackendMessageId to [{}]", businessMessageBackendId);
        if (message.getBackendData() == null) {
            message.setBackendData(DC5BackendData.builder()
                    .build());
        }
        message.getBackendData().setRefToBackendMessageId(businessMessageBackendId);

        //set ref to message id (EBMSID of business message)
        EbmsMessageId businessMessageEbmsId = businessMsg.getEbmsData().getEbmsMessageId();
        log.debug("Setting refToMessageId to [{}]", businessMessageEbmsId);
        message.getEbmsData().setRefToEbmsMessageId(businessMessageEbmsId);



        validateMessageConfirmationStep.executeStep(message);

        List<DC5Confirmation> transportedConfirmations = message.getTransportedMessageConfirmations();
        for (DC5Confirmation c : transportedConfirmations) {
            messageConfirmationStep.processConfirmationForMessage(businessMsg, c);
        }


        MessageReadyForTransportEvent messageReadyForTransportEvent =
                MessageReadyForTransportEvent.of(message.getId(), message.getGatewayLinkName(), message.getTarget());
        eventPublisher.publishEvent(messageReadyForTransportEvent);


//
//            DomibusConnectorMessageConfirmation transportedConfirmation = message.getTransportedMessageConfirmations().get(0);
//
//
//
//            //if business message is rejected, confirmed trigger cleanup routine
//            if (businessMsg.getMessageDetails().getConfirmed() != null || businessMsg.getMessageDetails().getRejected() != null) {
//                cleanupQueue.putOnQueue(businessMsg);
//            }
//
//            submitConfirmationAsEvidenceMessageStep.submitOppositeDirection(message.getConnectorMessageId(), businessMsg, transportedConfirmation);
//
//            if (isEvidenceTrigger && submitConfirmationAsEvidenceMessageStep.isSendCreatedTriggerEvidenceBack(businessMsg.getMessageLaneId())) {
//                //send generated evidence back...this would be the same direction as the business message...with new messageid
//                LOGGER.debug("Sending by trigger created confirmation message back to backend");
//                submitConfirmationAsEvidenceMessageStep.submitSameDirection(null, businessMsg, transportedConfirmation);
//            }
//
//            LOGGER.info(LoggingMarker.Log4jMarker.BUSINESS_LOG, "Successfully processed evidence [{}] in direction [{}] for business message [{}]",
//                    transportedConfirmation.getEvidenceType(),
//                    message.getMessageDetails().getDirection(),
//                    businessMsg.getConnectorMessageId());
//        } catch (DCEvidenceNotRelevantException dcEvidenceNotRelevantException) {
//            LOGGER.warn(LoggingMarker.Log4jMarker.BUSINESS_LOG, dcEvidenceNotRelevantException.getMessage());
//            LOGGER.debug(dcEvidenceNotRelevantException.getMessage(), dcEvidenceNotRelevantException);
//        }


    }


}
