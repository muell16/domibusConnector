package eu.ecodex.dc5.flow.steps;

import eu.domibus.connector.controller.exception.DCEvidenceNotRelevantException;
import eu.domibus.connector.controller.exception.ErrorCode;
import eu.ecodex.dc5.flow.api.Step;
import eu.ecodex.dc5.message.model.*;
import eu.domibus.connector.persistence.model.enums.EvidenceType;
import eu.domibus.connector.persistence.service.exceptions.DuplicateEvidencePersistenceException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * This service is responsible for
 *      -) in the future checking if the provided confirmation is valid
 *      -) storing the confirmation to the according business message into the database
 *      -) sending the confirmation in the other direction as the original message back as
 *      evidence message
 */
@Service
@RequiredArgsConstructor
public class MessageConfirmationStep {

    private static final Logger LOGGER = LogManager.getLogger(MessageConfirmationStep.class);

    private final DC5BusinessMessageStateMachineFactory stateMachineFactory;

    public void processTransportedConfirmations(DC5Message message) {
        if (!message.isBusinessMessage()) {
            throw new IllegalArgumentException("message must be a business message!");
        }
        processConfirmationForMessage(message, message.getTransportedMessageConfirmation());

    }

    /**
     * Handles the confirmation for a business message
     * See class level javadoc for details
     * @param businessMessage - the business message
     * @param confirmation - the confirmation
     */
    @Step(name = "processConfirmationForMessage")
    public void processConfirmationForMessage(DC5Message businessMessage, DC5Confirmation confirmation) {
        try {

            if (businessMessage.getMessageContent() == null) {
                throw new IllegalArgumentException("message must be a business message!");
            }

            this.processConfirmation(confirmation);

            this.confirmRejectMessage(confirmation, businessMessage.getMessageContent());
        } catch (DuplicateEvidencePersistenceException e) {
            throw new DCEvidenceNotRelevantException(ErrorCode.EVIDENCE_IGNORED_DUE_DUPLICATE, e);
        }

    }

    private void processConfirmation(DC5Confirmation confirmation) {
        byte[] evidence = confirmation.getEvidence();
        //TODO: parse evidence...

    }

    /**
     * Sets the correct message state within the database according to the following rules:
     *      a already rejected message cannot become a confirmed message!
     *      all evidences of lower priority are ignored
     *      (this means a RELAY_REMMD_REJECTION cannot overwrite a already processed DELIVERY evidence)
     *      also see {@link EvidenceType#getPriority()}
     * @param confirmation - the evidence Type
     */
    public void confirmRejectMessage(DC5Confirmation confirmation, DC5MessageContent content) {

        DC5BusinessMessageStateMachineFactory.DC5BusinessMessageStateMachine dc5BusinessMessageStateMachine = stateMachineFactory.loadStateMachine(content.getCurrentState());

        DC5BusinessMessageState dc5BusinessMessageState = dc5BusinessMessageStateMachine.publishEvent(DC5BusinessMessageState.BusinessMessageEvents.ofEvidenceType(confirmation.getEvidenceType()), confirmation);
        content.changeCurrentState(dc5BusinessMessageState);

    }

}
