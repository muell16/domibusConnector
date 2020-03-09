package eu.domibus.connector.link.service;


import eu.ecodex.utils.configuration.api.annotation.ConfigurationDescription;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationLabel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static eu.domibus.connector.link.service.DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME;

@Component
@Profile(LINK_PLUGIN_PROFILE_NAME)
@ConfigurationProperties(prefix = "connector.link")
public class DCLinkPluginConfigurationProperties {

    @ConfigurationLabel("Link Autostart")
    @ConfigurationDescription("Should the links be autostarted on connector start?, default is true")
    private boolean autostart = true;

    public boolean isAutostart() {
        return autostart;
    }

    public void setAutostart(boolean autostart) {
        this.autostart = autostart;
    }
}
