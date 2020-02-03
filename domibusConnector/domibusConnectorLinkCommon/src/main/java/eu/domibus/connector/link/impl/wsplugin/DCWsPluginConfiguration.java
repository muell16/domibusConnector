package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.link.common.DefaultWsCallbackHandler;
import eu.domibus.connector.link.common.WsPolicyLoader;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWSService;
import eu.domibus.connector.ws.gateway.delivery.webservice.DomibusConnectorGatewayDeliveryWSService;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.ws.policy.WSPolicyFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile(DCWsPluginConfiguration.DC_WS_PLUGIN_PROFILE_NAME)
@ComponentScan(basePackageClasses = DCWsPluginConfiguration.class)
public class DCWsPluginConfiguration {

    private static final Logger LOGGER = LogManager.getLogger(DCWsPluginConfiguration.class);

    public static final String DC_WS_PLUGIN_PROFILE_NAME = "link.wsplugin";
    public static final String DC_WS_BACKEND_PLUGIN_PROFILE_NAME = "link.wsbackendplugin";
    public static final String DC_WS_GATEWAY_PLUGIN_PROFILE_NAME = "link.wsgatewayplugin";


    public static final String POLICY_LOADER_NAME = "policyLoader";

    @Autowired
    DCWsPluginConfigurationProperties configurationProperties;

    @Autowired
    DomibusConnectorLinkConfiguration linkConfiguration;

    @Autowired
    SpringBus springBus;

    @Bean
    DCWsActiveLink dcWsActiveLink() {
        return new DCWsActiveLink();
    }

    @Bean
    DCWsEndpointAuthentication dcWsEndpointAuthentication() {
        return new DCWsEndpointAuthentication();
    }

    @Bean
    DCWsClientWebServiceClientFactory wsClientWebServiceClientFactory() {
        return new DCWsClientWebServiceClientFactory();
    }

    @ConditionalOnMissingBean(name = "defaultCallbackHandler")
    @Bean("defaultCallbackHandler")
    public DefaultWsCallbackHandler defaultCallbackHandler() {
        return new DefaultWsCallbackHandler();
    }

    @Bean
    DCWsBackendServiceEndpointImpl wsBackendServiceEndpoint() {
        return new DCWsBackendServiceEndpointImpl();
    }


    @Bean
    @Profile(DC_WS_BACKEND_PLUGIN_PROFILE_NAME)
    EndpointImpl connectorBackendWS() {
        DCWsBackendLinkConfigurationProperties config = (DCWsBackendLinkConfigurationProperties) configurationProperties;
        EndpointImpl endpoint = new EndpointImpl(springBus, wsBackendServiceEndpoint());
        endpoint.setAddress(config.getBackendPublishAddress());
        endpoint.setServiceName(DomibusConnectorBackendWSService.SERVICE);
        endpoint.setEndpointName(DomibusConnectorBackendWSService.DomibusConnectorBackendWebService);
        endpoint.setWsdlLocation("classpath:wsdl/DomibusConnectorBackendWebService.wsdl");

        WSPolicyFeature wsPolicyFeature = wsPolicyLoader().loadPolicyFeature();
        endpoint.getFeatures().add(wsPolicyFeature);

        endpoint.getProperties().put("security.callback-handler", new DefaultWsCallbackHandler());
        endpoint.getProperties().put("security.store.bytes.in.attachment", true);
        endpoint.getProperties().put("security.enable.streaming", true);
        endpoint.getProperties().put("mtom-enabled", true);
        endpoint.getProperties().put("security.encryption.properties", configurationProperties.getWssProperties());
        endpoint.getProperties().put("security.signature.properties", configurationProperties.getWssProperties());
        endpoint.getProperties().put("security.encryption.username", "useReqSigCert");

        endpoint.publish();
        LOGGER.debug("Published WebService {} under {}", DomibusConnectorBackendWSService.class, config.getBackendPublishAddress());
        return endpoint;
    }


    @Bean
    @Profile(DC_WS_GATEWAY_PLUGIN_PROFILE_NAME)
    EndpointImpl connectorGatewaySubmitWS() {
        DCWsGatewayLinkConfigurationProperties config = (DCWsGatewayLinkConfigurationProperties) configurationProperties;
        EndpointImpl endpoint = new EndpointImpl(springBus, wsConnectorDeliveryService());
        endpoint.setAddress(config.getGatewaySubmissionPublishAddress());
        endpoint.setWsdlLocation(DomibusConnectorGatewayDeliveryWSService.WSDL_LOCATION.toString());
        endpoint.setServiceName(DomibusConnectorGatewayDeliveryWSService.SERVICE);
        endpoint.setEndpointName(DomibusConnectorGatewayDeliveryWSService.DomibusConnectorGatewayDeliveryWebService);

        WSPolicyFeature wsPolicyFeature = wsPolicyLoader().loadPolicyFeature();
        endpoint.getFeatures().add(wsPolicyFeature);

        endpoint.getProperties().put("security.callback-handler", new DefaultWsCallbackHandler());
        endpoint.getProperties().put("security.store.bytes.in.attachment", true);
        endpoint.getProperties().put("security.enable.streaming", true);
        endpoint.getProperties().put("mtom-enabled", true);
        endpoint.getProperties().put("security.encryption.properties", configurationProperties.getWssProperties());
        endpoint.getProperties().put("security.signature.properties", configurationProperties.getWssProperties());
        endpoint.getProperties().put("security.encryption.username", "useReqSigCert");

        endpoint.publish();
        LOGGER.debug("Published WebService {} under {}", DomibusConnectorBackendWSService.class, config.getGatewaySubmissionPublishAddress());
        return endpoint;

    }

    @Bean
    @Profile(DC_WS_GATEWAY_PLUGIN_PROFILE_NAME)
    DCGatewayDeliveryEndpointImpl wsConnectorDeliveryService() {
        return new DCGatewayDeliveryEndpointImpl();
    }


    @Bean
    @Qualifier(POLICY_LOADER_NAME)
    public WsPolicyLoader wsPolicyLoader() {
        WsPolicyLoader wsPolicyLoader = new WsPolicyLoader(configurationProperties.getWsPolicy());
        LOGGER.info("Registered wsPolicyLoader [{}]");
        return wsPolicyLoader;
    }


}
