
package eu.domibus.connector.backend.ws.link.spring;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.persistence.service.BackendClientInfoPersistenceService;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessagesType;
import eu.domibus.connector.testdata.TransitionCreator;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import eu.domibus.connector.ws.backend.webservice.EmptyRequestType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import test.eu.domibus.connector.backend.ws.linktest.client.CommonBackendClient;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static test.eu.domibus.connector.backend.ws.linktest.client.CommonBackendClient.PROPERTY_CONNECTOR_BACKEND_ADDRESS;

/**
 *
 */
@ExtendWith(SpringExtension.class)
@Import(WSBackendLinkSendReceiveITCase.TestConfiguration.class)
@SpringBootTest(properties =
        {"server.port=0", "trace=true"},
        webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"backendlink-ws", "WSBackendLinkSendReceiveITCase"})
@Disabled("TODO: fix that test!")
public class WSBackendLinkSendReceiveITCase {

    private final static Logger LOGGER = LoggerFactory.getLogger(WSBackendLinkSendReceiveITCase.class);

    @SpringBootApplication(scanBasePackages = {"eu.domibus.connector.backend.ws.link.spring",},
            exclude = {
                    DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
    @Profile("WSBackendLinkSendReceiveITCase")
    public static class TestConfiguration {

        @Bean("defaultBackendClientInfo")
        public DomibusConnectorBackendClientInfo defaultBackendClientInfo() {
            DomibusConnectorBackendClientInfo backendClientInfo = new DomibusConnectorBackendClientInfo();
            backendClientInfo.setBackendName("bob");
            backendClientInfo.setBackendKeyAlias("bob");
            return backendClientInfo;
        }

        @Bean("defaultCallbackHandler")
        @ConditionalOnMissingBean
        CallbackHandler defaultCallbackHandler() {
            return (Callback[] cbk) -> {
            };
        }


        @Bean
        @ConditionalOnMissingBean
        BackendClientInfoPersistenceService backendClientInfoPersistenceService() {

            BackendClientInfoPersistenceService mock = Mockito.mock(BackendClientInfoPersistenceService.class);
            Mockito.when(mock.getEnabledBackendClientInfoByService(any(DomibusConnectorService.class))).thenReturn(defaultBackendClientInfo());
            return mock;
        }


        @Bean("connectorBackendImpl")
        @ConditionalOnMissingBean
        DomibusConnectorBackendWebService domibusConnectorBackendWebService() {
            return new DomibusConnectorBackendWebService() {
                @Override
                public DomibusConnectorMessagesType requestMessages(EmptyRequestType requestMessagesRequest) {
                    DomibusConnectorMessagesType messages = new DomibusConnectorMessagesType();
                    return messages;
                }

                @Override
                public DomibsConnectorAcknowledgementType submitMessage(DomibusConnectorMessageType submitMessageRequest) {
                    DomibsConnectorAcknowledgementType ack = new DomibsConnectorAcknowledgementType();
                    ack.setResult(true);
                    return ack;
                }
            };
        }

    }

//    @BeforeAll
//    public static void beforeClass() {
//        Security.setProperty("crypto.policy", "unlimited");
//    }

    //mock backend impl
//    @MockBean(name="connectorBackendImpl")
//    DomibusConnectorBackendWebService domibusConnectorBackendWebService;

    @LocalServerPort //use with WebEnvironment.RANDOM_PORT
            int port;

    @Value("${spring.webservices.path}")
    String webservicesPath;

    @Autowired
    DomibusConnectorBackendWebService backendWebService;

    @Autowired
    WSBackendLinkConfigurationProperties backendLinkConfigurationProperties;


    /**
     * builds a test client via new spring ctx and calls the server
     */
    @Test
    public void testCallBackendService_submitMessage() {
        String[] springProps = new String[]{
                "ws.backendclient.name=bob",
                "server.port=0",
                PROPERTY_CONNECTOR_BACKEND_ADDRESS + "=http://localhost:" + port + "/services/backend"
        };
        String[] springProfiles = new String[]{"ws-backend-client"};


        ApplicationContext clientCtx = CommonBackendClient.startSpringApplication(springProfiles, springProps);

        System.out.println("PROPERTY IS : " + clientCtx.getEnvironment().getProperty(PROPERTY_CONNECTOR_BACKEND_ADDRESS));

        DomibusConnectorBackendWebService domibusConnectorBackendWebService = CommonBackendClient.getBackendWebServiceClient(clientCtx); //clientCtx.getBean("backendClient", DomibusConnectorBackendWebService.class);

        DomibusConnectorMessageType msg = TransitionCreator.createMessage();
        DomibsConnectorAcknowledgementType response = domibusConnectorBackendWebService.submitMessage(msg);
        System.out.println("RESPONSE result: " + response.isResult());

        assertThat(response).isNotNull();
        assertThat(response.isResult()).isTrue();
    }

//    @Test
//    public void testSendMessageToBackend() {
//
//    }


}

