package eu.domibus.connector.persistence.service;

import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DC5Confirmation;
import eu.ecodex.dc5.message.model.DC5MessageId;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface DomibusConnectorEvidencePersistenceService {

    /**
     * Sets the confirmation as transported to gateway
     *  the confirmation must have already been persisted to the database
     * @param confirmation the confirmation, the dbEvidenceId must not be null!
     */
    void setConfirmationAsTransportedToGateway(DC5Confirmation confirmation);


    /**
     * Sets the confirmation as transported to backend
     *  the confirmation must have already been persisted to the database
     * @param confirmation the confirmation, the dbEvidenceId must not be null!
     */
    void setConfirmationAsTransportedToBackend(DC5Confirmation confirmation);

    /**
     * Persist the confirmation to the business message
     * @param businessMessage - the business Message the confirmation should be associated with
     * @param transportId - the transportId used to transport the confirmation
     * @param confirmation - the confirmation, within the confirmation the databaseId property will be updated
     *                     with the value from DB
     *
     */
    void persistEvidenceMessageToBusinessMessage(DC5Message businessMessage, DC5MessageId transportId, DC5Confirmation confirmation);
}
