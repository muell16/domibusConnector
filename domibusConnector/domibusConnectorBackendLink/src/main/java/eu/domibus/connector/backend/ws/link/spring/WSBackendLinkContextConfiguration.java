
package eu.domibus.connector.backend.ws.link.spring;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.backend.persistence.service.BackendClientInfoPersistenceService;
import eu.domibus.connector.backend.ws.link.impl.DomibusConnectorWsBackendImpl;
import eu.domibus.connector.backend.ws.link.impl.PushMessageToBackendClient;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.backend.ws.link.impl.MessageToBackendClientWaitQueue;
import eu.domibus.connector.backend.ws.link.impl.ToBackendClientJmsBasedWaitQueue;
import eu.domibus.connector.lib.spring.configuration.CxfTrustKeyStoreConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import eu.domibus.connector.link.common.DefaultWsCallbackHandler;
import eu.domibus.connector.link.common.WsPolicyLoader;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWSService;
import eu.domibus.connector.ws.gateway.delivery.webservice.DomibusConnectorGatewayDeliveryWebService;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.ws.policy.WSPolicyFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;

import org.springframework.jms.annotation.EnableJms;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import javax.xml.ws.soap.MTOMFeature;
import java.util.Properties;

/**
 * Configure the backend link web services
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Configuration
@EnableJms
@PropertySource("classpath:/eu/domibus/connector/backend/config/backend-default-config.properties")
@Profile("backendlink-ws")
public class WSBackendLinkContextConfiguration {

    private static final Logger LOGGER = LogManager.getLogger(WSBackendLinkContextConfiguration.class);

    public static final String BACKEND_POLICY_LOADER = "backendPolicyLoader";

    @Autowired
    WSBackendLinkConfigurationProperties configurationProperties;

    @Bean
    @Qualifier(BACKEND_POLICY_LOADER)
    public WsPolicyLoader wsPolicyLoader() {
        WsPolicyLoader wsPolicyLoader = new WsPolicyLoader(configurationProperties.getWsPolicy());
        return wsPolicyLoader;
    }

    @ConditionalOnMissingBean(name = "defaultCallbackHandler")
    @Bean("defaultCallbackHandler")
    public DefaultWsCallbackHandler defaultCallbackHandler() {
        return new DefaultWsCallbackHandler();
    }

    @Autowired
    @Qualifier(DomibusConnectorWsBackendImpl.BEAN_NAME)
    DomibusConnectorWsBackendImpl domibusConnectorWsBackendImpl;

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
