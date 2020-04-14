package eu.domibus.connector.domain.configuration;

import eu.domibus.connector.domain.model.DomibusConnectorAction;


public class MessageLaneEvidenceActionServiceConfigurationProperties {

    private EvidenceServiceAction relayREEMDAcceptance = new EvidenceServiceAction("RelayREMMDAcceptanceRejection", null, null);
    private EvidenceServiceAction relayREEMDRejection = new EvidenceServiceAction("RelayREMMDAcceptanceRejection", null, null);
    private EvidenceServiceAction relayREMMDFailure = new EvidenceServiceAction("RelayREMMDFailure", null, null);
    private EvidenceServiceAction deliveryNonDelivery = new EvidenceServiceAction("DeliveryNonDeliveryToRecipient", null, null);
    private EvidenceServiceAction retrievalNonRetrieval = new EvidenceServiceAction("RetrievalNonRetrievalToRecipient", null, null);

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

    public EvidenceServiceAction getDeliveryNonDelivery() {
        return deliveryNonDelivery;
    }

    public void setDeliveryNonDelivery(EvidenceServiceAction deliveryNonDelivery) {
        this.deliveryNonDelivery = deliveryNonDelivery;
    }

    public EvidenceServiceAction getRetrievalNonRetrieval() {
        return retrievalNonRetrieval;
    }

    public void setRetrievalNonRetrieval(EvidenceServiceAction retrievalNonRetrieval) {
        this.retrievalNonRetrieval = retrievalNonRetrieval;
    }

    public static class EvidenceServiceAction {
        private String action;
        private String service;
        private String serviceType;

        public EvidenceServiceAction() {}

        public EvidenceServiceAction(String action, String service, String serviceType) {
            this.action = action;
            this.service = service;
            this.serviceType = serviceType;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getServiceType() {
            return serviceType;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }
    }

}
