package eu.domibus.connector.domain.model;

import java.util.Properties;

public class DomibusConnectorLinkConfiguration {

    private String linkImpl;

    private Properties properties = new Properties();

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
}
