
package eu.domibus.connector.backend.ws.link.spring;

import eu.domibus.connector.backend.ws.link.impl.DomibusConnectorWsBackendImpl;
import eu.domibus.connector.link.common.CloseAttachmentInputStreamsInterceptor;
import eu.domibus.connector.link.common.DefaultWsCallbackHandler;
import eu.domibus.connector.link.common.WsPolicyLoader;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWSService;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.ws.policy.WSPolicyFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;

import org.springframework.jms.annotation.EnableJms;

import static eu.domibus.connector.backend.ws.link.spring.WSBackendLinkContextConfiguration.WS_BACKEND_LINK_PROFILE;

/**
 * Configure the backend link web services
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Configuration
@EnableJms
@PropertySource("classpath:/eu/domibus/connector/backend/config/backend-default-config.properties")
@Profile(WS_BACKEND_LINK_PROFILE)
public class WSBackendLinkContextConfiguration {

    private static final Logger LOGGER = LogManager.getLogger(WSBackendLinkContextConfiguration.class);

    public static final String WS_BACKEND_LINK_PROFILE = "backendlink-ws";

    public static final String BACKEND_POLICY_LOADER = "backendPolicyLoader";
    public static final String BACKEND_DELIVERY_CLOSE_INPUT_STREAM_INTERCEPTOR_BEAN = "backendDeliveryCloseInputStreamsInterceptor";

    @Autowired
    WSBackendLinkConfigurationProperties configurationProperties;

    @Autowired
    @Qualifier(DomibusConnectorWsBackendImpl.BEAN_NAME)
    DomibusConnectorWsBackendImpl domibusConnectorWsBackendImpl;

    @Bean
    @Qualifier(BACKEND_POLICY_LOADER)
    public WsPolicyLoader wsPolicyLoader() {
        WsPolicyLoader wsPolicyLoader = new WsPolicyLoader(configurationProperties.getWsPolicy());
        return wsPolicyLoader;
    }

    @Bean(BACKEND_DELIVERY_CLOSE_INPUT_STREAM_INTERCEPTOR_BEAN)
    public CloseAttachmentInputStreamsInterceptor closeAttachmentInputStreamsInterceptor() {
        return new CloseAttachmentInputStreamsInterceptor();
    }

    @ConditionalOnMissingBean(name = "defaultCallbackHandler")
    @Bean("defaultCallbackHandler")
    public DefaultWsCallbackHandler defaultCallbackHandler() {
        return new DefaultWsCallbackHandler();
    }

    @Bean
    EndpointImpl connectorBackendWS() {
        EndpointImpl endpoint = new EndpointImpl(domibusConnectorWsBackendImpl);
        endpoint.setAddress(configurationProperties.getBackendPublishAddress());
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
        LOGGER.debug("Published WebService {} under {}", DomibusConnectorBackendWSService.class, configurationProperties.getBackendPublishAddress());
        return endpoint;
    }

}
