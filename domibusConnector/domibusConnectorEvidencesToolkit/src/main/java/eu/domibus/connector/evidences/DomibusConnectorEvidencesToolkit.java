package eu.domibus.connector.evidences;


import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.MessageConfirmation;
import eu.domibus.connector.domain.enums.EvidenceType;
import eu.domibus.connector.domain.enums.RejectionReason;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;

/**
 * Interface to publish methods for creation of eCodex Evidence Messages.
 * 
 * @author riederb
 * 
 */
public interface DomibusConnectorEvidencesToolkit {

	MessageConfirmation createEvidence(EvidenceType type, Message message, RejectionReason rejectionReason, String details) throws DomibusConnectorEvidencesToolkitException;
	
}
