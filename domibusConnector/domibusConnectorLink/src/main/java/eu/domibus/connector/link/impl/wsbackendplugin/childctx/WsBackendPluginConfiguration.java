package eu.domibus.connector.link.impl.wsbackendplugin.childctx;

import eu.domibus.connector.link.common.DefaultWsCallbackHandler;
import eu.domibus.connector.link.common.MerlinPropertiesFactory;
import eu.domibus.connector.link.common.WsPolicyLoader;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWSService;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.ws.policy.WSPolicyFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Properties;


/**
 * Configuration for the spring childContext for
 * the pullGatewayPlugin
 */
@Configuration
@Profile(WsBackendPluginConfiguration.WS_BACKEND_PLUGIN_PROFILE_NAME)
@EnableConfigurationProperties(WsBackendPluginConfigurationProperties.class)
@ComponentScan(basePackageClasses = WsBackendPluginConfiguration.class)
public class WsBackendPluginConfiguration {

    private static final Logger LOGGER = LogManager.getLogger(WsBackendPluginConfiguration.class);
    public static final String WS_BACKEND_PLUGIN_PROFILE_NAME = "link.wsbackendplugin";

    @Autowired
    WsBackendPluginConfigurationProperties configurationProperties;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    SpringBus springBus;

    @Autowired
    MerlinPropertiesFactory merlinPropertiesFactory;

    @Bean
    WsBackendServiceEndpointImpl wsBackendServiceEndpoint() {
        return new WsBackendServiceEndpointImpl();
    }


    @Bean
    WsBackendPluginWebServiceClientFactory wsBackendPluginWebServiceClientFactory() {
        return new WsBackendPluginWebServiceClientFactory();
    }

    @Bean
    WsActiveLinkPartnerManager wsActiveLinkPartnerManager() {
        return new WsActiveLinkPartnerManager();
    }

    @Bean
    WsBackendSubmitTo wsBackendSubmitTo() {
        return new WsBackendSubmitTo();
    }

    @Bean
    EndpointImpl connectorBackendWS() {
        WsBackendPluginConfigurationProperties config = configurationProperties;
        EndpointImpl endpoint = new EndpointImpl(springBus, wsBackendServiceEndpoint());
        endpoint.setAddress(config.getBackendPublishAddress());
        endpoint.setServiceName(DomibusConnectorBackendWSService.SERVICE);
        endpoint.setEndpointName(DomibusConnectorBackendWSService.DomibusConnectorBackendWebService);
        endpoint.setWsdlLocation("classpath:wsdl/DomibusConnectorBackendWebService.wsdl");

        WSPolicyFeature wsPolicyFeature = new WsPolicyLoader(config.getWsPolicy()).loadPolicyFeature();
        endpoint.getFeatures().add(wsPolicyFeature);

        if (configurationProperties.isCxfLoggingEnabled()) {
            endpoint.getFeatures().add(new LoggingFeature());
        }

        endpoint.getProperties().put("security.callback-handler", new DefaultWsCallbackHandler());
        endpoint.getProperties().put("security.store.bytes.in.attachment", true);
        endpoint.getProperties().put("security.enable.streaming", true);
        endpoint.getProperties().put("mtom-enabled", true);

        Properties encSigProperties = merlinPropertiesFactory.mapCertAndStoreConfigPropertiesToMerlinProperties(configurationProperties.getSoap(), "");

        endpoint.getProperties().put("security.encryption.properties", encSigProperties);
        endpoint.getProperties().put("security.signature.properties", encSigProperties);
        endpoint.getProperties().put("security.encryption.username", "useReqSigCert");

        endpoint.publish();
        LOGGER.debug("Published WebService [{}] under [{}]", DomibusConnectorBackendWSService.class, config.getBackendPublishAddress());
        return endpoint;
    }


}
