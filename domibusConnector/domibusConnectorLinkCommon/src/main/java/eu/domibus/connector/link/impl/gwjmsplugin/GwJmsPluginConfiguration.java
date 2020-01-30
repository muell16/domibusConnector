package eu.domibus.connector.link.impl.gwjmsplugin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

@Profile(GwJmsPluginConfiguration.GW_JMS_PLUGIN_PROFILE)
@Configuration
@EnableConfigurationProperties(GwJmsPluginConfigurationProperties.class)
@ComponentScan(basePackageClasses = GwJmsPluginConfiguration.class)
public class GwJmsPluginConfiguration {

    public static final String GW_JMS_PLUGIN_PROFILE = "link.gwjmsplugin";

    @Autowired
    DefaultJmsListenerContainerFactory jmsListenerContainerFactory;

    @Autowired
    GwJmsPluginConfigurationProperties configurationProperties;

    @Autowired
    ReveiveFromGwJmsPlugin reveiveFromGatewayJmsPlugin;

    @Bean
    DefaultMessageListenerContainer receiveFromGwJmsPluginMessageListener() {
        SimpleJmsListenerEndpoint endpoint =
                new SimpleJmsListenerEndpoint();
        endpoint.setMessageListener(reveiveFromGatewayJmsPlugin);
        endpoint.setDestination(configurationProperties.getToDomibusGateway());

        return jmsListenerContainerFactory
                .createListenerContainer(endpoint);
    }





}
