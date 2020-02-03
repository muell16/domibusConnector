package eu.domibus.connector.link.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static eu.domibus.connector.link.service.DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME;

@Configuration
@Profile(LINK_PLUGIN_PROFILE_NAME)
public class DCLinkPluginConfiguration {

    public static final String LINK_PLUGIN_PROFILE_NAME = "linkplugins";

}
