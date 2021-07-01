package eu.domibus.connector.persistence.service;

import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.persistence.service.exceptions.IncorrectResultSizeException;

import java.util.Optional;

/**
 * This service offers access to the current p mode configuration
 *  of a message lane
 *  and also provides methods to change the current p-mode set
 *  of a message lane
 */
public interface DomibusConnectorPModeService {

    /**
     * Will check if the current p-Mode set for this message lane contains
     * the requested action. Only the action name will be looked up
     * if any matching action is found it will be returned
     * @param lane - the MessageLaneConfiguration which is asked
     * @param action - the action
     * @return the domibusConnectorService
     *          empty Optional if no service was found
     *          the domibusConnectorService where all attributes are filled
     */
    Optional<DomibusConnectorAction> getConfiguredSingle(DomibusConnectorMessageLane.MessageLaneId lane, DomibusConnectorAction action);

    /**
     * Will check if the current p-Mode set for this message lane contains
     * the requested service. Only the attribute service will be looked up
     * if any matching service is found it will be returned
     * @param lane - the MessageLaneConfiguration which is asked
     * @param domibusConnectorService the service
     * @return the domibusConnectorService
     *          empty Optional if no service was found
     *          the domibusConnectorService where all attributes are filled
     */
    Optional<DomibusConnectorService> getConfiguredSingle(DomibusConnectorMessageLane.MessageLaneId lane, DomibusConnectorService domibusConnectorService);

    /**
     * Will check if the current p-Mode set for this message lane contains
     * the requested party. If a DomibusConnectorParty property is null
     * it will be ignored on the search
     *  This means if only the partyId is set on the provided party, then only the partyId will be compared
     *
     *
     * @param lane - the MessageLaneConfiguration which is asked
     * @param domibusConnectorParty the DomibusConnectorParty from the configuration
     *                              where all properties are filled
     * @return the DomibusConnectorParty
     *          empty Optional if no matching party was found
     *          the domibusConnectorService where all attributes are filled
     *
     * @throws  IncorrectResultSizeException if more than one Party was found
     */
    Optional<DomibusConnectorParty> getConfiguredSingle(DomibusConnectorMessageLane.MessageLaneId lane, DomibusConnectorParty domibusConnectorParty) throws IncorrectResultSizeException;

    /**
     *
     * @param lane - the MessageLaneConfiguration which is changed
     * @param connectorPModeSet - this PModeSet will become the new current pModeSet
     */
    void updatePModeConfigurationSet(DomibusConnectorMessageLane.MessageLaneId lane, DomibusConnectorPModeSet connectorPModeSet);

    /**
     * @param lane - the MessageLaneConfiguration which is changed
     * @return  the current PModeSet of the given MessageLane
     */
    Optional<DomibusConnectorPModeSet> getCurrentPModeSet(DomibusConnectorMessageLane.MessageLaneId lane);

}
