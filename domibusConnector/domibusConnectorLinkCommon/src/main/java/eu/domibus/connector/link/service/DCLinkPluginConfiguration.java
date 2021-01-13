package eu.domibus.connector.link.service;


import eu.domibus.connector.link.impl.gwwspullplugin.DCGatewayPullPlugin;
import eu.domibus.connector.link.impl.wsbackendplugin.WsBackendPlugin;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import static eu.domibus.connector.link.service.DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME;

@Configuration
@Profile(LINK_PLUGIN_PROFILE_NAME)
@EnableConfigurationProperties(DCLinkPluginConfigurationProperties.class)
@Import(DomibusConnectorLinkCreatorConfigurationService.class)
public class DCLinkPluginConfiguration {

    public static final String LINK_PLUGIN_PROFILE_NAME = "linkplugins";

    @Bean
    public DCPluginBasedGatewaySubmissionService dcPluginBasedGatewaySubmissionService() {
        return new DCPluginBasedGatewaySubmissionService();
    }

    @Bean
    public DCPluginBasedBackendDeliveryService dcPluginBasedBackendDeliveryService() {
        return new DCPluginBasedBackendDeliveryService();
    }

    @Bean
    public DCActiveLinkManagerService dcActiveLinkManagerService() {
        return new DCActiveLinkManagerService();
    }

    @Bean
    @Profile("plugin-" + DCGatewayPullPlugin.IMPL_NAME)
    public DCGatewayPullPlugin dcGatewayPullPlugin() {
        return new DCGatewayPullPlugin();
    }

    @Bean
    @Profile("plugin-" + WsBackendPlugin.IMPL_NAME)
    public WsBackendPlugin DCWsBackendPlugin() {
        return new WsBackendPlugin();
    }

}
