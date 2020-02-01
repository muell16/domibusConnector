package eu.domibus.connector.domain.model;

import eu.domibus.connector.domain.enums.LinkMode;
import eu.domibus.connector.domain.enums.LinkType;
import org.springframework.core.style.ToStringCreator;

import java.util.Objects;
import java.util.Properties;

public class DomibusConnectorLinkPartner {

    private LinkPartnerName linkPartnerName;

    private String description;

    private boolean enabled;

    private LinkMode linkMode;

    private LinkType linkType;

    private Properties properties = new Properties();

    private DomibusConnectorLinkConfiguration linkConfiguration;

    private String configurationSource;

    public LinkPartnerName getLinkPartnerName() {
        return linkPartnerName;
    }

    public void setLinkPartnerName(LinkPartnerName linkPartnerName) {
        this.linkPartnerName = linkPartnerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
    }

    public DomibusConnectorLinkConfiguration getLinkConfiguration() {
        return linkConfiguration;
    }

    public String getConfigurationSource() {
        return configurationSource;
    }

    public void setConfigurationSource(String configurationSource) {
        this.configurationSource = configurationSource;
    }

    public void setLinkConfiguration(DomibusConnectorLinkConfiguration linkConfiguration) {
        this.linkConfiguration = linkConfiguration;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public static class LinkPartnerName {
        private String linkName;

        public LinkPartnerName(String linkName) {
            this.linkName = linkName;
        }

        public String getLinkName() {
            return linkName;
        }

        public void setLinkName(String linkName) {
            this.linkName = linkName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LinkPartnerName that = (LinkPartnerName) o;
            return Objects.equals(linkName, that.linkName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(linkName);
        }

        @Override
        public String toString() {
            return this.linkName;
        }
    }
}
