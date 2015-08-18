package eu.domibus.connector.evidences;

import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.common.message.MessageConfirmation;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.evidences.type.RejectionReason;

/**
 * Interface to publish methods for creation of eCodex Evidence Messages.
 * 
 * @author riederb
 * 
 */
public interface DomibusConnectorEvidencesToolkit {

    byte[] createSubmissionAcceptance(Message message, String hash) throws DomibusConnectorEvidencesToolkitException;

    byte[] createSubmissionRejection(RejectionReason rejectionReason, Message message, String hash)
            throws DomibusConnectorEvidencesToolkitException;

    MessageConfirmation createRelayREMMDAcceptance(Message message) throws DomibusConnectorEvidencesToolkitException;

    MessageConfirmation createRelayREMMDRejection(RejectionReason rejectionReason, Message message)
            throws DomibusConnectorEvidencesToolkitException;

    MessageConfirmation createRelayREMMDFailure(RejectionReason rejectionReason, Message message)
            throws DomibusConnectorEvidencesToolkitException;

    MessageConfirmation createDeliveryEvidence(Message message) throws DomibusConnectorEvidencesToolkitException;

    MessageConfirmation createNonDeliveryEvidence(RejectionReason rejectionReason, Message message)
            throws DomibusConnectorEvidencesToolkitException;

    MessageConfirmation createNonDeliveryEvidence(RejectionReason rejectionReason, Message message, String errorDetails)
            throws DomibusConnectorEvidencesToolkitException;

    MessageConfirmation createRetrievalEvidence(Message message) throws DomibusConnectorEvidencesToolkitException;

    MessageConfirmation createNonRetrievalEvidence(RejectionReason rejectionReason, Message message)
            throws DomibusConnectorEvidencesToolkitException;

    MessageConfirmation createNonRetrievalEvidence(RejectionReason rejectionReason, Message message, String errorDetails)
            throws DomibusConnectorEvidencesToolkitException;

    byte[] createSubmissionRejection(RejectionReason rejectionReason, Message message, String hash, String errorDetails)
            throws DomibusConnectorEvidencesToolkitException;

    MessageConfirmation createRelayREMMDRejection(RejectionReason rejectionReason, Message message, String errorDetails)
            throws DomibusConnectorEvidencesToolkitException;

    MessageConfirmation createRelayREMMDFailure(RejectionReason rejectionReason, Message message, String errorDetails)
            throws DomibusConnectorEvidencesToolkitException;

}
