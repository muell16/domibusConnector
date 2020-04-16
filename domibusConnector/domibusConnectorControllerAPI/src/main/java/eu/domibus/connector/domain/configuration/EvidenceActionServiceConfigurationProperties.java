package eu.domibus.connector.domain.configuration;

import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * This property class is meant to be loaded over the
 * {@link eu.domibus.connector.common.service.ConfigurationPropertyLoaderService} so also
 * MessageLane specific properties are taken into account
 */
@ConfigurationProperties(prefix = "connector.confirmation-messages")
public class EvidenceActionServiceConfigurationProperties {

    @NestedConfigurationProperty
    private EvidenceServiceAction relayREEMDAcceptance = new EvidenceServiceAction(new EvidenceAction("RelayREMMDAcceptanceRejection"), null);
    @NestedConfigurationProperty
    private EvidenceServiceAction relayREEMDRejection = new EvidenceServiceAction(new EvidenceAction("RelayREMMDAcceptanceRejection"), null);
    @NestedConfigurationProperty
    private EvidenceServiceAction relayREMMDFailure = new EvidenceServiceAction(new EvidenceAction("RelayREMMDFailure"), null);
    @NestedConfigurationProperty
    private EvidenceServiceAction delivery = new EvidenceServiceAction(new EvidenceAction("DeliveryNonDeliveryToRecipient"), null);
    @NestedConfigurationProperty
    private EvidenceServiceAction nonDelivery = new EvidenceServiceAction(new EvidenceAction("DeliveryNonDeliveryToRecipient"), null);
    @NestedConfigurationProperty
    private EvidenceServiceAction nonRetrieval = new EvidenceServiceAction(new EvidenceAction("RetrievalNonRetrievalToRecipient"), null);
    @NestedConfigurationProperty
    private EvidenceServiceAction retrieval = new EvidenceServiceAction(new EvidenceAction("RetrievalNonRetrievalToRecipient"), null);
    @NestedConfigurationProperty
    private EvidenceServiceAction submissionAcceptance = new EvidenceServiceAction(new EvidenceAction("SubmissionAcceptanceRejection"), null);
    @NestedConfigurationProperty
    private EvidenceServiceAction submissionRejection = new EvidenceServiceAction(new EvidenceAction("SubmissionAcceptanceRejection"), null);

    public EvidenceServiceAction getSubmissionAcceptance() {
        return submissionAcceptance;
    }

    public void setSubmissionAcceptance(EvidenceServiceAction submissionAcceptance) {
        this.submissionAcceptance = submissionAcceptance;
    }

    public EvidenceServiceAction getSubmissionRejection() {
        return submissionRejection;
    }

    public void setSubmissionRejection(EvidenceServiceAction submissionRejection) {
        this.submissionRejection = submissionRejection;
    }

    public EvidenceServiceAction getRelayREEMDAcceptance() {
        return relayREEMDAcceptance;
    }

    public void setRelayREEMDAcceptance(EvidenceServiceAction relayREEMDAcceptance) {
        this.relayREEMDAcceptance = relayREEMDAcceptance;
    }

    public EvidenceServiceAction getRelayREEMDRejection() {
        return relayREEMDRejection;
    }

    public void setRelayREEMDRejection(EvidenceServiceAction relayREEMDRejection) {
        this.relayREEMDRejection = relayREEMDRejection;
    }

    public EvidenceServiceAction getRelayREMMDFailure() {
        return relayREMMDFailure;
    }

    public void setRelayREMMDFailure(EvidenceServiceAction relayREMMDFailure) {
        this.relayREMMDFailure = relayREMMDFailure;
    }

    public EvidenceServiceAction getDelivery() {
        return delivery;
    }

    public void setDelivery(EvidenceServiceAction delivery) {
        this.delivery = delivery;
    }

    public EvidenceServiceAction getNonDelivery() {
        return nonDelivery;
    }

    public void setNonDelivery(EvidenceServiceAction nonDelivery) {
        this.nonDelivery = nonDelivery;
    }

    public EvidenceServiceAction getNonRetrieval() {
        return nonRetrieval;
    }

    public void setNonRetrieval(EvidenceServiceAction nonRetrieval) {
        this.nonRetrieval = nonRetrieval;
    }

    public EvidenceServiceAction getRetrieval() {
        return retrieval;
    }

    public void setRetrieval(EvidenceServiceAction retrieval) {
        this.retrieval = retrieval;
    }

    public static class EvidenceServiceAction {
        @Valid
        private EvidenceAction action;
        @Valid
        @NestedConfigurationProperty
        private EvidenceService service;

        public EvidenceServiceAction() {}

        public EvidenceServiceAction(EvidenceAction action, EvidenceService service) {
            this.action = action;
            this.service = service;
        }

        public EvidenceAction getAction() {
            return action;
        }

        public void setAction(EvidenceAction action) {
            this.action = action;
        }

        public EvidenceService getService() {
            return service;
        }

        public void setService(EvidenceService service) {
            this.service = service;
        }

        @Nullable
        public DomibusConnectorAction getConnectorAction() {
            if (this.action == null) {
                return null;
            }
            return this.action.getConnectorAction();
        }

        @Nullable
        public DomibusConnectorService getConnectorService() {
            if (this.service == null) {
                return null;
            }
            return this.getService().getConnectorService();
        }

    }

    public static class EvidenceAction {
        @NotBlank
        private String action;

        public EvidenceAction() {}

        public EvidenceAction(String action) {
            this.action = action;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public DomibusConnectorAction getConnectorAction() {
            return new DomibusConnectorAction(this.action);
        }
    }

    public static class EvidenceService {
        @NotBlank
        private String name;
        @NotBlank
        private String serviceType;

        public EvidenceService() {

        }

        public EvidenceService(String name, String serviceType) {
            this.name = name;
            this.serviceType = serviceType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getServiceType() {
            return serviceType;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }

        public DomibusConnectorService getConnectorService() {
            return new DomibusConnectorService(this.name, this.serviceType);
        }
    }

}
