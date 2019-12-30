package eu.domibus.connector.link.impl.gwjmsplugin;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(ReceiveFromJmsQueueConfiguration.GW_JMS_PLUGIN_PROFILE)
@Configuration
@EnableConfigurationProperties(ReceiveFromJmsQueueConfigurationProperties.class)
@ComponentScan
public class ReceiveFromJmsQueueConfiguration {

    public static final String GW_JMS_PLUGIN_PROFILE = "link.gwjmsplugin";




}
