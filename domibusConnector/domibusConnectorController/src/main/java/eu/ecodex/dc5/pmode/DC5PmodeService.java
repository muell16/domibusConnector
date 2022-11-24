package eu.ecodex.dc5.pmode;

import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.persistence.service.exceptions.IncorrectResultSizeException;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This service offers access to the current p mode configuration
 *  of a message lane
 *  and also provides methods to change the current p-mode set
 *  of a message lane
 */
public interface DC5PmodeService {




    /**
     * Will check if the current p-Mode set for this message lane contains
     * the requested action. Only the action name will be looked up
     * if any matching action is found it will be returned.
     * null value acts as a wildcard
     * @param lane - the MessageLaneConfiguration which is asked
     * @param action - the action
     * @return a list of services
     */
    List<PModeAction> findByExample(DomibusConnectorBusinessDomain.BusinessDomainId lane, PModeAction action);

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
    Optional<PModeAction> getConfiguredSingle(DomibusConnectorBusinessDomain.BusinessDomainId lane, PModeAction action);


    /**
     * Will check if the current p-Mode set for this message lane contains
     * the requested service.
     * Ff any matching service is found it will be returned within the list
     * a null value acts as wildcard
     * @param lane - the MessageLaneConfiguration which is asked
     * @param DC5Service the service
     * @return a list of matching services
     *
     */
    List<PModeService> findByExample(DomibusConnectorBusinessDomain.BusinessDomainId lane, PModeService DC5Service);

    /**
     * Will check if the current p-Mode set for this message lane contains
     * the requested service. Only the attribute service will be looked up
     * if any matching service is found it will be returned
     * @param lane - the MessageLaneConfiguration which is asked
     * @param DC5Service the service
     * @return the domibusConnectorService
     *          empty Optional if no service was found
     *          the domibusConnectorService where all attributes are filled
     */
    Optional<PModeService> getConfiguredSingle(DomibusConnectorBusinessDomain.BusinessDomainId lane, PModeService DC5Service);

    /**
     * Will check if the current p-Mode set for this message lane contains
     * the requested party. If a DomibusConnectorParty property is null
     * it will be ignored on the search
     *  This means if only the partyId is set on the provided party, then only the partyId will be compared.
     *
     *
     * @param lane - the MessageLaneConfiguration which is asked
     * @param domibusConnectorParty the DomibusConnectorParty from the configuration
     *                              where all properties are filled
     * @return A list of matching DomibusConnectorParties
     *
     */
    List<PModeParty> findByExample(DomibusConnectorBusinessDomain.BusinessDomainId lane, PModeParty domibusConnectorParty) throws IncorrectResultSizeException;

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
    Optional<PModeParty> getConfiguredSingle(DomibusConnectorBusinessDomain.BusinessDomainId lane, PModeParty domibusConnectorParty) throws IncorrectResultSizeException;

    /**
     *
     * @param connectorPModeSet - this PModeSet will become the new current pModeSet
     */
    void updatePModeConfigurationSet(DomibusConnectorPModeSet connectorPModeSet);

    /**
     * @param lane - the MessageLaneConfiguration which is changed
     * @return  the current PModeSet of the given MessageLane
     */
    Optional<DomibusConnectorPModeSet> getCurrentPModeSet(DomibusConnectorBusinessDomain.BusinessDomainId lane);

	void updateActivePModeSetDescription(DomibusConnectorPModeSet connectorPModeSet);

	List<DomibusConnectorPModeSet> getInactivePModeSets(DomibusConnectorBusinessDomain.BusinessDomainId lane);


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    class DomibusConnectorPModeSet {

        private DomibusConnectorBusinessDomain.BusinessDomainId businessDomainId;

        //create new class in ConnectorMessageProcessingProperties
        //PMode
        private String description;
        private LocalDateTime createDate;
        private String principal; //SecurityContext.getPrincipal()
        private byte[] pModes;

        private List<PModeParty> parties = new ArrayList<>();
        private List<PModeAction> actions = new ArrayList<>();
        private List<PModeService> services = new ArrayList<>();

        private DomibusConnectorKeystore connectorstore;
        private PModeParty homeParty;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class PModeAction {
        String action;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class PModeService {
        String service;
        String serviceType;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class PModeParty {
        String partyId;
        String partyIdType;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class PModeLeg {
        String name;
        PModeAction action;
        PModeService service;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class PModeProcess {
        List<PModeLeg> legs;
        List<PModeParty> initiatorParties;
        List<PModeParty> responderParties;
        String responderRole;
        String initiatorRole;
    }

}
