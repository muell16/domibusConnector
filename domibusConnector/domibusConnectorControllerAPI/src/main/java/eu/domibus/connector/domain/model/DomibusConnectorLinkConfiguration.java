package eu.domibus.connector.domain.model;

import org.springframework.core.style.ToStringCreator;

import java.util.Objects;
import java.util.Properties;

public class DomibusConnectorLinkConfiguration {

    private LinkConfigName configName;

    private String linkImpl;

    private Properties properties = new Properties();

    private String configurationSource;

    public LinkConfigName getConfigName() {
        return configName;
    }

    public String getConfigurationSource() {
        return configurationSource;
    }

    public void setConfigurationSource(String configurationSource) {
        this.configurationSource = configurationSource;
    }

    public void setConfigName(LinkConfigName configName) {
        this.configName = configName;
    }

    public String getLinkImpl() {
        return linkImpl;
    }

    public void setLinkImpl(String linkImpl) {
        this.linkImpl = linkImpl;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public static class LinkConfigName {
        private String configName;

        public LinkConfigName(String configName) {
            this.configName = configName;
        }

        public String getConfigName() {
            return configName;
        }

        public void setConfigName(String configName) {
            this.configName = configName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LinkConfigName that = (LinkConfigName) o;
            return Objects.equals(configName, that.configName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(configName);
        }

        @Override
        public String toString() {
            return this.configName;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomibusConnectorLinkConfiguration that = (DomibusConnectorLinkConfiguration) o;
        return configName.equals(that.configName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configName);
    }
}