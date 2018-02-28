
package eu.domibus.connector.ws.backend.link.spring;

import eu.domibus.connector.backend.persistence.service.BackendClientInfoPersistenceService;
import eu.domibus.connector.backend.ws.helper.WsPolicyLoader;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessagesType;
import eu.domibus.connector.domain.transition.testutil.TransitionCreator;
import eu.domibus.connector.ws.backend.linktest.client.CommonBackendClient;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import eu.domibus.connector.ws.backend.webservice.EmptyRequestType;
import java.security.Security;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
@RunWith(SpringRunner.class)
@Import(WSBackendLinkContextConfigurationITCase.TestConfiguration.class)
@SpringBootTest(properties= {"server.port=0"}, webEnvironment = WebEnvironment.RANDOM_PORT)
public class WSBackendLinkContextConfigurationITCase {

    private final static Logger LOGGER = LoggerFactory.getLogger(WSBackendLinkContextConfigurationITCase.class);
    
    @SpringBootApplication(scanBasePackages={"eu.domibus.connector.ws.backend.link.spring", }, 
            scanBasePackageClasses= {WsPolicyLoader.class},
            exclude = {
        DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})    
    public static class TestConfiguration {     
        
        @Bean
        BackendClientInfoPersistenceService backendClientInfoPersistenceService() {
            BackendClientInfoPersistenceService mock = Mockito.mock(BackendClientInfoPersistenceService.class);
            return mock;
        }
        
        
        @Bean("connectorBackendImpl")
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
    
    @BeforeClass
    public static void beforeClass() {
        Security.setProperty("crypto.policy", "unlimited");
    }
    
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
        
    @Before
    public void setUp() {
    }
    
    
    /**
     * builds a test client via new spring ctx and calls the server
     */
    @Test
    public void testCallBackendService_submitMessage() {
        String[] springProps = new String[] {       
            "ws.backendclient.name=bob",
            "connector.backend.ws.address=http://localhost:" + port + "/services/backend"
        };
        String[] springProfiles = new String[] {"ws-backend-client"};
        
        
        ApplicationContext clientCtx = CommonBackendClient.startSpringApplication(springProfiles, springProps);
               
        DomibusConnectorBackendWebService domibusConnectorBackendWebService = clientCtx.getBean("backendClient", DomibusConnectorBackendWebService.class);

        DomibusConnectorMessageType msg = TransitionCreator.createMessage();
        DomibsConnectorAcknowledgementType response = domibusConnectorBackendWebService.submitMessage(msg);
        System.out.println("RESPONSE result: " + response.isResult());
        
        assertThat(response).isNotNull();
        assertThat(response.isResult()).isTrue();
        
    }

    
}

