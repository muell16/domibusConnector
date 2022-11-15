package eu.domibus.connector.evidences;


import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.ecodex.dc5.message.model.DomibusConnectorMessage;
import eu.ecodex.dc5.message.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;

/**
 * Interface to publish methods for creation of eCodex Evidence Messages.
 * 
 * @author riederb
 * 
 */
public interface DomibusConnectorEvidencesToolkit {

	DomibusConnectorMessageConfirmation  createEvidence(DomibusConnectorEvidenceType type, DomibusConnectorMessage message, DomibusConnectorRejectionReason rejectionReason, String details) throws DomibusConnectorEvidencesToolkitException;
	
}
