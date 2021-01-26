package eu.domibus.connector.persistence.service;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageId;

import javax.validation.constraints.NotNull;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface DomibusConnectorEvidencePersistenceService {


//
//    /**
//     * Creates the evidence in storage
//     *
//     * @param message - the message related to the evidence
//     * @param evidence - the evidence as byte[]
//     * @param evidenceType - the type of the evidence
//     * @param transport - the message id the evidence is transported within
//     */
//    @Deprecated
//    void persistEvidenceForMessageIntoDatabase(@NotNull DomibusConnectorMessage message,
//                                               byte[] evidence,
//                                               @NotNull DomibusConnectorEvidenceType evidenceType,
//                                               DomibusConnectorMessageId transport);
//
//
//    @Deprecated
//    default void persistEvidenceForMessageIntoDatabase(DomibusConnectorMessage originalMesssage, DomibusConnectorMessageConfirmation messageConfirmation, DomibusConnectorMessageId transportId) {
//        persistEvidenceForMessageIntoDatabase(originalMesssage, messageConfirmation.getEvidence(), messageConfirmation.getEvidenceType(), transportId);
//    }

//    /**
//     *
//     * @param evidenceMessage - the evidence message (message which contains the evidence(s))
//     * @param businessMessage - the message the evidence is meant for, must be a business message
//     * @return
//     */
//    DomibusConnectorMessage persistEvidenceMessageForBusinessMessage(DomibusConnectorMessage evidenceMessage,
//            DomibusConnectorMessage businessMessage);
//
//    DomibusConnectorMessage persistEvidenceMessageForBusinessMessage(DomibusConnectorMessage evidenceMessage,
//                                                                     DomibusConnectorMessageId businessMessage);
//

//    void persistEvidenceMessageToBusinessMessage(DomibusConnectorMessageId connectorMessageId, DomibusConnectorMessageConfirmation confirmation);

    DomibusConnectorMessage persistEvidenceMessageToBusinessMessage(DomibusConnectorMessage businessMessage, DomibusConnectorMessageId transportId, DomibusConnectorMessageConfirmation confirmation);
}
