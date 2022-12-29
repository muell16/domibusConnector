package eu.ecodex.dc5.pmode;

import eu.domibus.connector.domain.model.DC5BusinessDomain;
import eu.domibus.connector.domain.model.DomibusConnectorKeystore;
import lombok.*;

import javax.annotation.CheckForNull;
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
    Optional<DomibusConnectorPModeSet> getCurrentPModeSet(DC5BusinessDomain.BusinessDomainId lane);


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    class DomibusConnectorPModeSet {

        private DC5BusinessDomain.BusinessDomainId businessDomainId;

        //create new class in ConnectorMessageProcessingProperties
        //PMode
        @CheckForNull
        private String description;
        @CheckForNull
        private LocalDateTime createDate;
        @CheckForNull
        private String principal; //SecurityContext.getPrincipal()
        @CheckForNull
        private byte[] pModes;

        @NonNull
        private List<PModeParty> parties = new ArrayList<>();
        @NonNull
        private List<PModeAction> actions = new ArrayList<>();
        @NonNull
        private List<PModeService> services = new ArrayList<>();
        @NonNull
        private List<PModeProcess> businessProcess = new ArrayList<>();
        @NonNull
        private List<PModeLeg> legs = new ArrayList<>();
        @CheckForNull
        private DomibusConnectorKeystore connectorstore;
        @CheckForNull
        private PModeParty homeParty;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public class PModeAction {
        String action;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    class PModeService {
        String service;
        String serviceType;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    class PModeParty {
        String name;
        String partyId;
        String partyIdType;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    class PModeLeg {
        String name;
        PModeAction action;
        PModeService service;
        @ToString.Exclude
        PModeProcess businessProcess;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    class PModeProcess {
        List<PModeLeg> legs;
        List<PModeParty> initiatorParties;
        List<PModeParty> responderParties;
        String responderRole;
        String initiatorRole;
        String name;
    }

}
