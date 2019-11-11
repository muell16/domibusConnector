package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.ws.link.spring.WSBackendLinkConfigurationProperties;
import eu.domibus.connector.link.common.WsPolicyLoader;
import eu.domibus.connector.ws.backend.delivery.webservice.DomibusConnectorBackendDeliveryWebService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("TODO: convert to ITCase!")
public class BackendClientWebServiceClientFactoryTest {

    BackendClientWebServiceClientFactory webServiceClientFactory;

    WSBackendLinkConfigurationProperties configurationProperties = new WSBackendLinkConfigurationProperties();


    @BeforeEach
    public void setUp() {
        WsPolicyLoader policyLoader = new WsPolicyLoader(configurationProperties.getWsPolicy());
//        policyLoader.setBackendLinkConfigurationProperties(configurationProperties);

        webServiceClientFactory = new BackendClientWebServiceClientFactory();

        webServiceClientFactory.setBackendLinkConfigurationProperties(configurationProperties);
        webServiceClientFactory.setPolicyUtil(policyLoader);
    }

    @Test
    public void testCreateWsClient() {
        DomibusConnectorBackendClientInfo backendClientInfo = new DomibusConnectorBackendClientInfo();
        backendClientInfo.setBackendName("bob");
        backendClientInfo.setBackendKeyAlias("bob");
        backendClientInfo.setBackendPushAddress("http://localhost:9002/a-push-address");
        DomibusConnectorBackendDeliveryWebService wsClient = webServiceClientFactory.createWsClient(backendClientInfo);

        assertThat(wsClient).isNotNull();
    }


}