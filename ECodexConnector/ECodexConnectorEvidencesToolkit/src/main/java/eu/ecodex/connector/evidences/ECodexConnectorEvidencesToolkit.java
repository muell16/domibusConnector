package eu.ecodex.connector.evidences;

import eu.ecodex.connector.evidences.exception.EvidencesToolkitException;
import eu.ecodex.connector.evidences.jaxb.EvidenceMessage;
import eu.ecodex.connector.evidences.type.EvidenceInput;

/**
 * Interface to publish methods for creation of eCodex Evidence Messages.
 * 
 * @author riederb
 * 
 */
public interface ECodexConnectorEvidencesToolkit {

    /**
     * Method to transform messageState from national backend system into an eCodex evidence message .
     * 
     * @param input. Contains messageId, messageState and comment.
     * @return An JAXB Object which can be easily marshalled into an XML containing the evidence message.
     * @throws EvidencesToolkitException
     */
    EvidenceMessage createEvidenceMessage(EvidenceInput input) throws EvidencesToolkitException;

}
