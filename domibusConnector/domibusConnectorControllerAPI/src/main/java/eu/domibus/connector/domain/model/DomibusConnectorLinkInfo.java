package eu.domibus.connector.domain.model;

import eu.domibus.connector.domain.enums.LinkType;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DomibusConnectorLinkInfo {

    private String linkName;

    private String description;

    private boolean enabled;

    private LinkType linkType;

    private DomibusConnectorLinkConfiguration linkConfiguration;

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
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

    public void setLinkConfiguration(DomibusConnectorLinkConfiguration linkConfiguration) {
        this.linkConfiguration = linkConfiguration;
    }
}
