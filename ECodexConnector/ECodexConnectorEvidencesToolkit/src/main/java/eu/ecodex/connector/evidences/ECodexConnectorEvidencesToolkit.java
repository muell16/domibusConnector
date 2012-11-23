package eu.ecodex.connector.evidences;

import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.evidences.exception.EvidencesToolkitException;
import eu.ecodex.connector.evidences.type.RejectionReason;

/**
 * Interface to publish methods for creation of eCodex Evidence Messages.
 * 
 * @author riederb
 * 
 */
public interface ECodexConnectorEvidencesToolkit {

    void createSubmissionAcceptance(Message message) throws EvidencesToolkitException;

    void createSubmissionRejection(RejectionReason rejectionReason, Message message) throws EvidencesToolkitException;

    void createRelayREMMDAcceptance(Message message) throws EvidencesToolkitException;

    void createRelayREMMDRejection(RejectionReason rejectionReason, Message message) throws EvidencesToolkitException;

    void createDeliveryEvidence(Message message) throws EvidencesToolkitException;

    void createNonDeliveryEvidence(RejectionReason rejectionReason, Message message) throws EvidencesToolkitException;

    void createRetrievalEvidence(Message message) throws EvidencesToolkitException;

    void createNonRetrievalEvidence(RejectionReason rejectionReason, Message message) throws EvidencesToolkitException;

}
