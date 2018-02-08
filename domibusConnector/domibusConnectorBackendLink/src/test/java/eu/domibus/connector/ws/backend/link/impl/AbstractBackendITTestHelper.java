
package eu.domibus.connector.ws.backend.link.impl;

import eu.domibus.connector.backend.StartBackendOnly;
import eu.domibus.connector.ws.backend.linktest.client.CommonBackendClient;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * This test class starts backend SpringApplicationContext on random web port
 *  and also start clients alice and bob on random web ports
 * 
 * so message tests between the ports can be done...
 *  colorizing the outputs from the different spring contexts is not working!
 * 
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
public class AbstractBackendITTestHelper {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractBackendITTestHelper.class);
    
    static ConfigurableApplicationContext backendApplicationContext;
    
    static ApplicationContext bobApplicationContext;
    
    static ApplicationContext aliceApplicationContext;
    
    @BeforeClass
    public static void beforeClass() {
        setUpBackend();
        
        bobApplicationContext = setUpClientBob(getBackendServiceWsAddress(backendApplicationContext));
        aliceApplicationContext = setUpClientAlice(getBackendServiceWsAddress(backendApplicationContext));
        
        LOGGER.info("SERVER ADDRESS " +  getBackendServiceWsAddress(backendApplicationContext));
        LOGGER.info("CLIENT BOB ADDRESS " + getBackendClientPushServiceAddress(bobApplicationContext));
                
    }
    
    //setup and start connector backend
    private static void setUpBackend() {
        MDC.put("COLOR", "GREEN");
        String[] backendProfiles = new String[] {};
        String[] backendProperties = new String[] {"server.port=0", "logging.config=classpath:log4j2-test.xml"};
        backendApplicationContext = StartBackendOnly.startUpSpringApplication(backendProfiles, backendProperties);
        
        //local.server.port of backend link
        //String serverPort = backendApplicationContext.getEnvironment().getProperty("local.server.port");
        
        //backendServerAddress = "http://localhost:"+ serverPort + "/services/backend";
        
        LOGGER.debug("backendServiceStarted....");
        MDC.remove("COLOR");        
    }

    
    //setup and start application context for client bob
    private static ApplicationContext setUpClientBob(String backendAddress) {
        MDC.put("COLOR", "BLUE");
        String[] profiles = new String[]{"ws-backendclient-server", "ws-backendclient-client"};
        String[] properties = new String[]{"ws.backendclient.name=bob", "ws.backendclient.cn=bob", "server.port=0", "connector.backend.ws.address=" + backendAddress};
        ApplicationContext ctx = CommonBackendClient.startSpringApplication(profiles, properties);
        MDC.remove("COLOR");
        return ctx;
    }
    
    private static ApplicationContext setUpClientAlice(String backendAddress) {
        MDC.put("COLOR", "CYAN");
        String[] profiles = new String[]{"ws-backendclient-server", "ws-backendclient-client"};
        String[] properties = new String[]{"ws.backendclient.name=alice", "ws.backendclient.cn=alice", "server.port=0", "connector.backend.ws.address=" + backendAddress};
        ApplicationContext ctx = CommonBackendClient.startSpringApplication(profiles, properties);
        MDC.remove("COLOR");
        return ctx;
    }
    
    protected static String getBackendClientPushServiceAddress(ApplicationContext appContext) {
        String serverPort = appContext.getEnvironment().getProperty("local.server.port");
        return "http://localhost:" + serverPort + "/services/backendDelivery";
    }
    
    protected static String getBackendServiceWsAddress(ApplicationContext appContext) {
        String serverPort = appContext.getEnvironment().getProperty("local.server.port");
        return "http://localhost:" + serverPort + "/services/backend";
    }
    
    @Before
    public void setUp() throws InterruptedException {
        System.out.println("###########################RUN TEST#######################################");
        System.out.println("SERVER ADDRESS: " +  getBackendServiceWsAddress(backendApplicationContext));
        System.out.println("CLIENT BOB ADDRESS: " + getBackendClientPushServiceAddress(bobApplicationContext));
        System.out.println("wait 3 seconds....");
        Thread.sleep(3000);
    }
    
    @After
    public void tearDown() throws InterruptedException {
        System.out.println("###########################AFTER TEST#####################################");
        System.out.println("wait 3 seconds....");
        Thread.sleep(3000);
    }
    
//    @Test
////    @Ignore //This test shuold be moved to other class
//    public void testSubmitMessageToBackendService() throws InterruptedException {
//
//        //send message from bob to connector
//        DomibusConnectorBackendWebService bobClientEndpoint = bobApplicationContext.getBean("backendClient", DomibusConnectorBackendWebService.class);
//        
//        DomibusConnectorMessageType msg = TransitionCreator.createMessage();
//        DomibsConnectorAcknowledgementType submitMessage = bobClientEndpoint.submitMessage(msg);
//        assertThat(submitMessage).isNotNull();
//      
//    }
    
    

}