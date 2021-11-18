package eu.domibus.connector.link.service;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(DCLinkPluginConfigurationProperties.class)
@Import(DomibusConnectorLinkCreatorConfigurationService.class)
public class DCLinkPluginConfiguration {

    public static final String LINK_PLUGIN_PROFILE_NAME = "linkplugins";

//    @Bean
//    public DCPluginBasedGatewaySubmissionService dcPluginBasedGatewaySubmissionService() {
//        return new DCPluginBasedGatewaySubmissionService();
//    }
//
//    @Bean
//    public DCPluginBasedBackendDeliveryService dcPluginBasedBackendDeliveryService() {
//        return new DCPluginBasedBackendDeliveryService();
//    }

//    @Bean
//    @Profile("plugin-" + DCGatewayPullPlugin.IMPL_NAME)
//    public DCGatewayPullPlugin dcGatewayPullPlugin() {
//        return new DCGatewayPullPlugin();
//    }
//
//    @Bean
//    @Profile("plugin-" + WsBackendPlugin.IMPL_NAME)
//    public WsBackendPlugin DCWsBackendPlugin() {
//        return new WsBackendPlugin();
//    }
//
//    @Bean
//    @Profile("plugin-" + WsGatewayPlugin.IMPL_NAME)
//    public WsGatewayPlugin wsGatewayPlugin() {
//        return new WsGatewayPlugin();
//    }

//    @Bean
//    @Profile("plugin-" + DomibusGwWsPlugin.IMPL_NAME)
//    public DomibusGwWsPlugin domibusGwWsPlugin() {
//        return new DomibusGwWsPlugin();
//    }

}
