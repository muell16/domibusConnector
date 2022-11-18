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

        DC5Ebms evidenceTriggerMsgDetails = evidenceTriggerMsg.getEbmsData();

        //set correct action for evidence message
        evidenceTriggerMsgDetails.setAction(confirmationCreatorService.createEvidenceAction(confirmation.getEvidenceType()));
        //set correct service derived from business msg
        evidenceTriggerMsgDetails.setService(businessMsgDetails.getService());

        //set correct original sender / final recipient
//        evidenceTriggerMsgDetails.setOriginalSender(businessMsgDetails.getFinalRecipient());
//        evidenceTriggerMsgDetails.setFinalRecipient(businessMsgDetails.getOriginalSender());
        //set correct from/to party
//        evidenceTriggerMsgDetails.setFromParty(businessMsgDetails.getToParty());
//        evidenceTriggerMsgDetails.setToParty(businessMsgDetails.getFromParty());

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
