package eu.ecodex.connector.evidences;

import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.evidences.exception.ECodexConnectorEvidencesToolkitException;
import eu.ecodex.connector.evidences.type.RejectionReason;

/**
 * Interface to publish methods for creation of eCodex Evidence Messages.
 * 
 * @author riederb
 * 
 */
public interface ECodexConnectorEvidencesToolkit {

    byte[] createSubmissionAcceptance(Message message, String hash) throws ECodexConnectorEvidencesToolkitException;

    byte[] createSubmissionRejection(RejectionReason rejectionReason, Message message, String hash)
            throws ECodexConnectorEvidencesToolkitException;

    MessageConfirmation createRelayREMMDAcceptance(Message message) throws ECodexConnectorEvidencesToolkitException;

    MessageConfirmation createRelayREMMDRejection(RejectionReason rejectionReason, Message message)
            throws ECodexConnectorEvidencesToolkitException;

    MessageConfirmation createRelayREMMDFailure(RejectionReason rejectionReason, Message message)
            throws ECodexConnectorEvidencesToolkitException;

    MessageConfirmation createDeliveryEvidence(Message message) throws ECodexConnectorEvidencesToolkitException;

    MessageConfirmation createNonDeliveryEvidence(RejectionReason rejectionReason, Message message)
            throws ECodexConnectorEvidencesToolkitException;

    MessageConfirmation createNonDeliveryEvidence(RejectionReason rejectionReason, Message message, String errorDetails)
            throws ECodexConnectorEvidencesToolkitException;

    MessageConfirmation createRetrievalEvidence(Message message) throws ECodexConnectorEvidencesToolkitException;

    MessageConfirmation createNonRetrievalEvidence(RejectionReason rejectionReason, Message message)
            throws ECodexConnectorEvidencesToolkitException;

    MessageConfirmation createNonRetrievalEvidence(RejectionReason rejectionReason, Message message, String errorDetails)
            throws ECodexConnectorEvidencesToolkitException;

    byte[] createSubmissionRejection(RejectionReason rejectionReason, Message message, String hash, String errorDetails)
            throws ECodexConnectorEvidencesToolkitException;

    MessageConfirmation createRelayREMMDRejection(RejectionReason rejectionReason, Message message, String errorDetails)
            throws ECodexConnectorEvidencesToolkitException;

    MessageConfirmation createRelayREMMDFailure(RejectionReason rejectionReason, Message message, String errorDetails)
            throws ECodexConnectorEvidencesToolkitException;

}
