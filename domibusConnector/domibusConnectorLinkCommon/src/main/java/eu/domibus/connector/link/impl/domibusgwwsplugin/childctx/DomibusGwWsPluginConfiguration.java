package eu.domibus.connector.link.impl.domibusgwwsplugin.childctx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(DomibusGwWsPluginConfiguration.DOMIBUS_GATEWAY_WS_PLUGIN_PROFILE)
@EnableConfigurationProperties(DomibusGwWsPluginConfigurationProperties.class)
public class DomibusGwWsPluginConfiguration {

    public static final String DOMIBUS_GATEWAY_WS_PLUGIN_PROFILE = "link.domibusgatewaywsplugin";

    @Autowired
    DomibusGwWsPluginConfigurationProperties config;

    @Bean
    DomibusGwWsPluginBackendInterfaceFactory domibusGwWsPluginBackendInterfaceFactory() {
        return new DomibusGwWsPluginBackendInterfaceFactory();
    }

    @Bean
    DomibusGwWsPluginPullFromGw gwWsPluginPullFromGw() {
        return new DomibusGwWsPluginPullFromGw();
    }

    @Bean
    DomibusGwWsPluginSubmitToLink gwWsPluginSubmitToLink() {
        return new DomibusGwWsPluginSubmitToLink();
    }

}
