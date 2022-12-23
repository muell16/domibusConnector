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
     * @param lane - the MessageLaneConfiguration which is changed
     * @return  the current PModeSet of the given MessageLane
     */
    Optional<DomibusConnectorPModeSet> getCurrentPModeSet(DomibusConnectorBusinessDomain.BusinessDomainId lane);


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
        private List<PModeProcess> businessProcess = new ArrayList<>();
        private List<PModeLeg> legs = new ArrayList<>();

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
        String name;
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
        PModeProcess businessProcess;
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
        String name;
    }

}
