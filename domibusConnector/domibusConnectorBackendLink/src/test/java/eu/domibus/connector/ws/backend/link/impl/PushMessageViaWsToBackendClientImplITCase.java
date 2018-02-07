
package eu.domibus.connector.ws.backend.link.impl;

import eu.domibus.connector.backend.StartBackendOnly;
import eu.domibus.connector.backend.TestBackendContext;
import eu.domibus.connector.ws.backend.linktest.client.CommonBackendClient;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import org.junit.runner.RunWith;
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
        
        //TODO: start backend link
//        String[] backendProfiles = new String[] {};
//        String[] backendProperties = new String[] {"server.port=-1"};
//        ConfigurableApplicationContext backendApplicationContext = StartBackendOnly.startUpSpringApplication(backendProfiles, backendProperties);
        //TODO: backendApplicationContext get web port...
        
        
        String backendWsAddress = "";
        
        //TODO: start client bob
        //ws-backendclient-server
        String[] profiles = new String[] {"ws-backendclient-server", "ws-backendclient-client"};
        String[] properties = new String[] {"ws.backendclient.name=bob", "ws.backendclient.cn=bob", "server.port=-1", "connector.backend.ws.address="+backendWsAddress };
        ApplicationContext bobApplicationContext = CommonBackendClient.startSpringApplication(profiles, properties);
        //TODO: get web port!
        
        
        //TODO: push test message to bob
        
        //TODO: check rcv messages in bob
        
        
        
        //TODO: start client alice        
        //TODO: push test message to alice
        //TODO: check alice
        
    }

}