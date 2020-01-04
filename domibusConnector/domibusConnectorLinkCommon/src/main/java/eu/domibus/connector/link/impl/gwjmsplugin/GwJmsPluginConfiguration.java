package eu.domibus.connector.link.impl.gwjmsplugin;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(GwJmsPluginConfiguration.GW_JMS_PLUGIN_PROFILE)
@Configuration
@EnableConfigurationProperties(GwJmsPluginConfigurationProperties.class)
@ComponentScan
public class GwJmsPluginConfiguration {

    public static final String GW_JMS_PLUGIN_PROFILE = "link.gwjmsplugin";




}
