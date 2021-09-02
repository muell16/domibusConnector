package eu.domibus.connector.config.c2ctests;

import eu.domibus.connector.domain.configuration.EvidenceActionServiceConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * This configuration properties are for defining the connector to connector
 * test action and service
 *
 * If the connector receives a message with the configured service and action
 * the message will not be delivered to the gateway
 *
 *
 */
@ConfigurationProperties(prefix=ConnectorTestConfigurationProperties.PREFIX)
@Component
public class ConnectorTestConfigurationProperties {

    public static final String PREFIX = "c2ctests";

    private boolean enabled = true;

    /**
     * should a delivery evidence sent
     */
    private boolean respondWithDeliveryEvidence = true;


    @Valid
    private EvidenceActionServiceConfigurationProperties.AS4Service service = null;

    @Valid
    @NotNull
    private EvidenceActionServiceConfigurationProperties.AS4Action action = new EvidenceActionServiceConfigurationProperties.AS4Action("Connector-TEST");

    public EvidenceActionServiceConfigurationProperties.AS4Service getService() {
        return service;
    }

    public void setService(EvidenceActionServiceConfigurationProperties.AS4Service service) {
        this.service = service;
    }

    public EvidenceActionServiceConfigurationProperties.AS4Action getAction() {
        return action;
    }

    public void setAction(EvidenceActionServiceConfigurationProperties.AS4Action action) {
        this.action = action;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isRespondWithDeliveryEvidence() {
        return respondWithDeliveryEvidence;
    }

    public void setRespondWithDeliveryEvidence(boolean respondWithDeliveryEvidence) {
        this.respondWithDeliveryEvidence = respondWithDeliveryEvidence;
    }
}