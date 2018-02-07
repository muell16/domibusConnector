
package eu.domibus.connector.ws.backend.link.impl;

import eu.domibus.connector.backend.StartBackendOnly;
import eu.domibus.connector.backend.TestBackendContext;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.testutil.TransitionCreator;
import eu.domibus.connector.ws.backend.linktest.client.CommonBackendClient;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import org.junit.runner.RunWith;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {TestBackendContext.class})
//@TestPropertySource(locations = {"application.properties"},
//        properties = { "connector.backend.internal.wait-queue.name=waitqueue"}
//)
public class PushMessageViaWsToBackendClientImplITCase {

    
//    @LocalPort
    private int localPort;
    
    

    @Test
    public void testSomeMethod() {
        
        //start backend link
        MDC.put("COLOR", "GREEN");
        String[] backendProfiles = new String[] {};
        String[] backendProperties = new String[] {"server.port=0", "logging.config=classpath:log4j2-test.xml"};
        ConfigurableApplicationContext backendApplicationContext = StartBackendOnly.startUpSpringApplication(backendProfiles, backendProperties);
        
        //local.server.port of backend link
        String serverPort = backendApplicationContext.getEnvironment().getProperty("local.server.port");
        
        String backendServerAddress = "http://localhost:"+ serverPort + "/services/backend";
        
        System.out.println("Server Adress is:" + backendServerAddress);
        
        MDC.put("COLOR", "BLUE");
        //start client bob
        //ws-backendclient-server
        String[] profiles = new String[] {"ws-backendclient-server", "ws-backendclient-client"};
        String[] properties = new String[] {"ws.backendclient.name=bob", "ws.backendclient.cn=bob", "server.port=0", "connector.backend.ws.address="+backendServerAddress };
        ApplicationContext bobApplicationContext = CommonBackendClient.startSpringApplication(profiles, properties);
        //TODO: get web port!
        
        //send message from bob to connector
        DomibusConnectorBackendWebService bobClientEndpoint = bobApplicationContext.getBean("backendClient", DomibusConnectorBackendWebService.class);
        
        DomibusConnectorMessageType msg = TransitionCreator.createMessage();
        DomibsConnectorAcknowledgementType submitMessage = bobClientEndpoint.submitMessage(msg);
        assertThat(submitMessage).isNotNull();



        //TODO: push test message to bob
        //TODO: check rcv messages in bob
        //TODO: start client alice        
        //TODO: push test message to alice
        //TODO: check alice
        
    }

}