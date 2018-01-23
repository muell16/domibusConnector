package eu.domibus.connector.persistence.service;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import javax.annotation.Nonnull;

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
     */
    void persistEvidenceForMessageIntoDatabase(@Nonnull DomibusConnectorMessage message, @Nonnull byte[] evidence, @Nonnull DomibusConnectorEvidenceType evidenceType);

    /**
     * Merges the message into the storage @see #mergeMessageWithDatabase
     * and updates the delivered to gateway
     * field with the current time of the corresponding
     * evidence (the corresponding evidence is identified by the evidence type)
     *
     * @param message - the message
     * @param evidenceType - the evidenceType
     * @throws eu.domibus.connector.persistence.service.PersistenceException - if the message could not be updated
     * in storage
     *
     *
     */
    void setEvidenceDeliveredToGateway(@Nonnull DomibusConnectorMessage message, @Nonnull DomibusConnectorEvidenceType evidenceType) throws PersistenceException;

    /**
     * Merges the message into the storage @see #mergeMessageWithDatabase
     * and updates the delivered to national_backend
     * field with the current time of the corresponding
     * evidence (the corresponding evidence is identified by the evidence type)
     *
     * @param message - the message
     * @param evidenceType - the evidenceType
     * @throws eu.domibus.connector.persistence.service.PersistenceException - if the message
     * could not be updated in storage
     *
     */
    void setEvidenceDeliveredToNationalSystem(DomibusConnectorMessage message, DomibusConnectorEvidenceType evidenceType) throws PersistenceException;

}
