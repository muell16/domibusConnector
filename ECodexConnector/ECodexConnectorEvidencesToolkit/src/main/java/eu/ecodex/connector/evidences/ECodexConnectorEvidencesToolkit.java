package eu.ecodex.connector.evidences;

import org.etsi.uri._02640.v2.REMEvidenceType;

import eu.ecodex.connector.evidences.exception.EvidencesToolkitException;
import eu.ecodex.connector.evidences.type.RejectionReason;

/**
 * Interface to publish methods for creation of eCodex Evidence Messages.
 * 
 * @author riederb
 * 
 */
public interface ECodexConnectorEvidencesToolkit {

    byte[] createSubmissionAcceptance(String nationalMessageId, byte[] originalMessage, String senderAddress,
            String recipientAddress) throws EvidencesToolkitException;

    byte[] createSubmissionRejection(RejectionReason rejectionReason, String nationalMessageId, byte[] originalMessage,
            String senderAddress, String recipientAddress) throws EvidencesToolkitException;

    byte[] createDeliveryEvidence(REMEvidenceType previousEvidence) throws EvidencesToolkitException;

    byte[] createNonDeliveryEvidence(RejectionReason rejectionReason, REMEvidenceType previousEvidence)
            throws EvidencesToolkitException;

    byte[] createRetrievalEvidence(REMEvidenceType previousEvidence) throws EvidencesToolkitException;

    byte[] createNonRetrievalEvidence(RejectionReason rejectionReason, REMEvidenceType previousEvidence)
            throws EvidencesToolkitException;

}
