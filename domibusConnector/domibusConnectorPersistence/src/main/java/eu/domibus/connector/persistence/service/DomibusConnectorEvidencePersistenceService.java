package eu.domibus.connector.persistence.service;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.persistence.service.exceptions.PersistenceException;
import org.springframework.lang.NonNull;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface DomibusConnectorEvidencePersistenceService {



    /**
     * Creates the evidence in storage
     *
     * @param message - the message related to the evidence
     * @param evidence - the evidence as byte[]
     * @param evidenceType - the type of the evidence
     * @param transport - the message the evidence is transported with
     */
    void persistEvidenceForMessageIntoDatabase(@NotNull DomibusConnectorMessage message,
                                               byte[] evidence,
                                               @NotNull DomibusConnectorEvidenceType evidenceType,
                                               DomibusConnectorMessage.DomibusConnectorMessageId transport);


    default void persistEvidenceForMessageIntoDatabase(DomibusConnectorMessage originalMesssage, DomibusConnectorMessageConfirmation messageConfirmation, DomibusConnectorMessage.DomibusConnectorMessageId transportId) {
        persistEvidenceForMessageIntoDatabase(originalMesssage, messageConfirmation.getEvidence(), messageConfirmation.getEvidenceType(), transportId);
    }



    /**
     * Sets the evidence as delivered to GW
     * @param transport - the connector message id, which has transported the message
     */
    void setEvidenceDeliveredToGateway(@NotNull  DomibusConnectorMessage.DomibusConnectorMessageId transport);


    /**
     * Sets the evidence as delivered to national system
     * @param transport - the connector message id, of the message which has transported the evidence
     */
    void setEvidenceDeliveredToNationalSystem(@NotNull  DomibusConnectorMessage.DomibusConnectorMessageId transport);


}
