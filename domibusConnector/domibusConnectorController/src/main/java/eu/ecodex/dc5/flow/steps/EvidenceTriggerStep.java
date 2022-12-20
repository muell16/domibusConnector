package eu.ecodex.dc5.flow.steps;

import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.ecodex.dc5.message.ConfirmationCreatorService;
import eu.ecodex.dc5.message.FindBusinessMessageByMsgId;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.ecodex.dc5.flow.api.Step;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DC5Confirmation;
import eu.ecodex.dc5.message.model.DC5Ebms;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.tools.logging.LoggingMarker;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Processes an evidence trigger message
 *  the evidence trigger only contains a refToMessageId
 *  all other AS4 attributes must be read from the business message
 *
 */
@Component
@RequiredArgsConstructor
public class EvidenceTriggerStep {

    private static final Logger LOGGER = LogManager.getLogger(EvidenceTriggerStep.class);

    private final FindBusinessMessageByMsgId findBusinessMessageByMsgId;
    private final ConfirmationCreatorService confirmationCreatorService;
    private final DC5MessageRepo messageRepo;


    @Step(name = "EvidenceTriggerStep")
    public DC5Message executeStep(DC5Message evidenceTriggerMsg) {

        //is evidence triggering allowed
        isEvidenceTriggeringAllowed(evidenceTriggerMsg);

        //only incoming evidence messages are looked up
        DC5Message businessMsg = findBusinessMessageByMsgId.findBusinessMessageByIdAndDirection(evidenceTriggerMsg, DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND);
        DC5Ebms businessMsgDetails = businessMsg.getEbmsData();

        DomibusConnectorEvidenceType evidenceType = getEvidenceType(evidenceTriggerMsg);

        //create evidence
        DC5Confirmation confirmation = confirmationCreatorService.createConfirmation(evidenceType, businessMsg, DomibusConnectorRejectionReason.OTHER, "");
        LOGGER.info(LoggingMarker.Log4jMarker.BUSINESS_LOG, "Successfully created evidence [{}] for evidence trigger", evidenceType);

        //set generated evidence into the trigger message
//        ConfirmationCreatorService.DomibusConnectorMessageConfirmationWrapper evidenceWrapper = confirmation.build();
        evidenceTriggerMsg.getTransportedMessageConfirmations().get(0)
                .setEvidence(confirmation.getEvidence());

        evidenceTriggerMsg.setRefToConnectorMessageId(businessMsg.getConnectorMessageId());
        evidenceTriggerMsg.setMessageLaneId(businessMsg.getMessageLaneId());

        DC5Ebms evidenceTriggerMsgDetails = evidenceTriggerMsg.getEbmsData();
        if (evidenceTriggerMsgDetails == null) {
            evidenceTriggerMsgDetails = new DC5Ebms();
            evidenceTriggerMsg.setEbmsData(evidenceTriggerMsgDetails);
            evidenceTriggerMsg = messageRepo.save(evidenceTriggerMsg);
            evidenceTriggerMsgDetails = evidenceTriggerMsg.getEbmsData();
        }

        evidenceTriggerMsgDetails.setRefToEbmsMessageId(businessMsgDetails.getEbmsMessageId());

        //set correct action for evidence message
        evidenceTriggerMsgDetails.setAction(confirmationCreatorService.createEvidenceAction(confirmation.getEvidenceType()));
        //set correct service derived from business msg
        evidenceTriggerMsgDetails.setService(businessMsgDetails.getService().toBuilder().build());

        //change sender / receiver
        evidenceTriggerMsgDetails.setGatewayAddress(businessMsgDetails.getGatewayAddress().toBuilder().build());
        evidenceTriggerMsgDetails.setBackendAddress(businessMsgDetails.getBackendAddress().toBuilder().build());
        evidenceTriggerMsgDetails.setResponderRole(businessMsgDetails.getResponderRole().toBuilder().build());
        evidenceTriggerMsgDetails.setInitiatorRole(businessMsgDetails.getInitiatorRole().toBuilder().build());
//        //preserve ebms roles
//        evidenceTriggerMsgDetails.getBackendAddress().setRole(businessMsgDetails.getBackendAddress().getRole().toBuilder().build());
//        evidenceTriggerMsgDetails.getGatewayAddress().setRole(businessMsgDetails.getGatewayAddress().getRole().toBuilder().build());

        return evidenceTriggerMsg;
    }

    private void isEvidenceTriggeringAllowed(DC5Message evidenceTriggerMsg) {
        if (!DomainModelHelper.isEvidenceTriggerMessage(evidenceTriggerMsg)) {
            throwException(evidenceTriggerMsg, "The message is not an evidence trigger message!");
        }
        MessageTargetSource source = evidenceTriggerMsg.getDirection().getSource();
        if (source != MessageTargetSource.BACKEND) {
            throwException(evidenceTriggerMsg, "Only backend can generate trigger messages");
        }
    }

    private void throwException(DC5Message evidenceTriggerMsg, String s) {
        throw new DomibusConnectorMessageException(evidenceTriggerMsg, EvidenceTriggerStep.class, s);
    }

    private DomibusConnectorEvidenceType getEvidenceType(DC5Message evidenceTriggerMsg) {
        if (!DomainModelHelper.isEvidenceTriggerMessage(evidenceTriggerMsg)) {
            throw new DomibusConnectorMessageException(evidenceTriggerMsg, EvidenceTriggerStep.class, "The message is not an evidence trigger msg!");
        }
        DC5Confirmation msgConfirmation = evidenceTriggerMsg.getTransportedMessageConfirmations().get(0);
        return msgConfirmation.getEvidenceType();
    }


}
