package eu.ecodex.connector.evidences;

import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.evidences.exception.ECodexConnectorEvidencesToolkitException;
import eu.ecodex.connector.evidences.type.RejectionReason;

/**
 * Interface to publish methods for creation of eCodex Evidence Messages.
 * 
 * @author riederb
 * 
 */
public interface ECodexConnectorEvidencesToolkit {

    byte[] createSubmissionAcceptance(Message message, byte[] hash) throws ECodexConnectorEvidencesToolkitException;

    byte[] createSubmissionRejection(RejectionReason rejectionReason, Message message, byte[] hash)
            throws ECodexConnectorEvidencesToolkitException;

    void createRelayREMMDAcceptance(Message message) throws ECodexConnectorEvidencesToolkitException;

    void createRelayREMMDRejection(RejectionReason rejectionReason, Message message) throws ECodexConnectorEvidencesToolkitException;

    void createDeliveryEvidence(Message message) throws ECodexConnectorEvidencesToolkitException;

    void createNonDeliveryEvidence(RejectionReason rejectionReason, Message message) throws ECodexConnectorEvidencesToolkitException;

    void createRetrievalEvidence(Message message) throws ECodexConnectorEvidencesToolkitException;

    void createNonRetrievalEvidence(RejectionReason rejectionReason, Message message) throws ECodexConnectorEvidencesToolkitException;

}
