package eu.domibus.connector.domain.model;

import eu.domibus.connector.domain.enums.ConfigurationSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DC5BusinessDomain {

    public static final String DEFAULT_LANE_NAME = "defaultBusinessDomain";

    private BusinessDomainId id;

    private String description;

    private boolean enabled;

    private Map<String, String> properties = new HashMap<>();

    private ConfigurationSource configurationSource; // TODO: must be read-only if props are not from db, beause cant change .properties.

    public static DC5BusinessDomain getDefaultBusinessDomain() {
        DC5BusinessDomain defaultMessageLane = new DC5BusinessDomain();
        defaultMessageLane.setId(new BusinessDomainId(DEFAULT_LANE_NAME));
        defaultMessageLane.setDescription("default message lane");
        defaultMessageLane.setProperties(new HashMap<>());
        defaultMessageLane.setEnabled(true);
        return defaultMessageLane;
    }

    public static BusinessDomainId getDefaultBusinessDomainId() {
        return getDefaultBusinessDomain().getId();
    }

    public BusinessDomainId getId() {
        return id;
    }

    public void setId(BusinessDomainId id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public ConfigurationSource getConfigurationSource() {
        return configurationSource;
    }

    public void setConfigurationSource(ConfigurationSource configurationSource) {
        this.configurationSource = configurationSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DC5BusinessDomain)) return false;

        DC5BusinessDomain that = (DC5BusinessDomain) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public static class BusinessDomainId {

//        public BusinessDomainId() {}

        public BusinessDomainId(String id) {
            if (StringUtils.isBlank(id)) {
                throw new IllegalArgumentException(String.format("id of business domain is not allowed to be blank or null, but it is [%s]!", id));
            }
            this.businessDomainId = id;
        }

        @NotBlank
        @NotNull
        @NonNull
        private final String businessDomainId;

        public static BusinessDomainId of(String s) {
            return new BusinessDomainId(s);
        }

        public String getBusinessDomainId() {
            return businessDomainId;
        }

        @Override
        public String toString() {
            return String.format("BusinessDomainId: [%s]", this.businessDomainId);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BusinessDomainId)) return false;

            BusinessDomainId that = (BusinessDomainId) o;

            return businessDomainId != null ? businessDomainId.equals(that.businessDomainId) : that.businessDomainId == null;
        }

        @Override
        public int hashCode() {
            return businessDomainId != null ? businessDomainId.hashCode() : 0;
        }
    }

    @Override
    public String toString() {
        return Objects.toString(this.id);
    }

}
