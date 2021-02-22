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

    /**
     * Persist the confirmation to the business message
     * @param businessMessage - the business Message the confirmation should be associated with
     * @param transportId - the transportId used to transport the confirmation
     * @param confirmation - the confirmation
     * @return
     */
    void persistEvidenceMessageToBusinessMessage(DomibusConnectorMessage businessMessage, DomibusConnectorMessageId transportId, DomibusConnectorMessageConfirmation confirmation);
}
