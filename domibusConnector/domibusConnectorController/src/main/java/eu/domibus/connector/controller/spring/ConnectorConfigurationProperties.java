package eu.domibus.connector.controller.spring;

import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Validated
@Valid
@ConfigurationProperties(prefix = ConnectorConfigurationProperties.PREFIX)
public class ConnectorConfigurationProperties {

    public static final String PREFIX = "connector";

    public ConnectorConfigurationProperties() {
    }

    /**
     * a random instance name is the default
     */
    @NotNull
    @NotBlank
    private String instanceName = UUID.randomUUID().toString().substring(0,6);

    @NotNull
    DomibusConnectorMessageLane.MessageLaneId defaultBusinessDomainId = DomibusConnectorMessageLane.getDefaultMessageLaneId();

    @NotNull
    Map<String, BusinessDomainConfig> businessDomain = new HashMap<>();

    @Validated
    public static class BusinessDomainConfig {
        @NotNull
        private boolean enabled = true;

        private Map<String, String> properties = new HashMap<>();

        public Map<String, String> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public DomibusConnectorMessageLane.MessageLaneId getDefaultBusinessDomainId() {
        return defaultBusinessDomainId;
    }

    public void setDefaultBusinessDomainId(DomibusConnectorMessageLane.MessageLaneId defaultBusinessDomainId) {
        this.defaultBusinessDomainId = defaultBusinessDomainId;
    }

    public Map<String, BusinessDomainConfig> getBusinessDomain() {
        return businessDomain;
    }

    public void setBusinessDomain(Map<String, BusinessDomainConfig> businessDomain) {
        this.businessDomain = businessDomain;
    }

}
