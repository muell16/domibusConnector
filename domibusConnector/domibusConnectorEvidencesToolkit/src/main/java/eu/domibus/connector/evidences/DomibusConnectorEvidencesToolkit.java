package eu.domibus.connector.evidences;


import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.MessageConfirmation;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;

/**
 * Interface to publish methods for creation of eCodex Evidence Messages.
 * 
 * @author riederb
 * 
 */
public interface DomibusConnectorEvidencesToolkit {

	MessageConfirmation createEvidence(DomibusConnectorEvidenceType type, Message message, DomibusConnectorRejectionReason rejectionReason, String details) throws DomibusConnectorEvidencesToolkitException;
	
}
