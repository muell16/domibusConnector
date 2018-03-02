
package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.StartBackendOnly;
import eu.domibus.connector.backend.TestBackendContext;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.persistence.service.BackendClientInfoPersistenceService;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessagesType;
import eu.domibus.connector.domain.transition.testutil.TransitionCreator;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.impl.BigDataWithMessagePersistenceService;
import org.junit.*;
import test.eu.domibus.connector.backend.ws.linktest.client.BackendClientPushWebServiceConfiguration;
import test.eu.domibus.connector.backend.ws.linktest.client.CommonBackendClient;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;

import static org.assertj.core.api.Assertions.*;

import eu.domibus.connector.ws.backend.webservice.EmptyRequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.UUID;

/**
 *
 * This test class starts backend SpringApplicationContext on random web port
 *  and also start clients alice and bob on random web ports
 *
 *  Tests messages flow
 *      from controller to backendClient
 *  and
 *      from backendClient to controller
 * 
 * so message tests between the ports can be done...
 *  colorizing the outputs from the different spring contexts is not working!
 * 
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
public class BackendLinkWsTestMessageFlowITCase {

    private final static Logger LOGGER = LoggerFactory.getLogger(BackendLinkWsTestMessageFlowITCase.class);
    
    static ConfigurableApplicationContext backendApplicationContext;
    
    static ApplicationContext bobApplicationContext;
    
    static ApplicationContext aliceApplicationContext;

    private List<DomibusConnectorMessage> toControllerSubmittedMessages;

    @BeforeClass
    public static void beforeClass() {
        setUpBackend();
        
        bobApplicationContext = setUpClientBob(getBackendServiceWsAddress(backendApplicationContext));
        aliceApplicationContext = setUpClientAlice(getBackendServiceWsAddress(backendApplicationContext));
        
        LOGGER.info("SERVER ADDRESS " +  getBackendServiceWsAddress(backendApplicationContext));
        LOGGER.info("CLIENT BOB ADDRESS " + getBackendClientPushServiceAddress(bobApplicationContext));
                
    }

    @AfterClass
    public static void afterClass() throws InterruptedException {
//        Thread.sleep(60000);
    }
    
    //setup and start connector backend
    private static void setUpBackend() {
        MDC.put("COLOR", "GREEN");
        String dbName = UUID.randomUUID().toString().substring(0,10);
        String[] backendProfiles = new String[] {"test", "db_h2"};
        String[] backendProperties = new String[] {"server.port=0",
                "logging.config=classpath:log4j2-test.xml",
                "liquibase.change-log=classpath:/backend/database/testdata/init-db.xml",
                "spring.h2.console.enabled=true",
                "spring.h2.console.path=/h2-console"
                //"liquibase.enabled=true",
//                "spring.datasource.url=jdbc:h2:mem:" + dbName
        };

        backendApplicationContext = StartBackendOnly.startUpSpringApplication(backendProfiles, backendProperties);

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
        System.out.println("CLIENT ALICE ADDRESS: " + getBackendClientPushServiceAddress(aliceApplicationContext));
        System.out.println("wait 3 seconds....");
        Thread.sleep(3000);

        this.toControllerSubmittedMessages = (List<DomibusConnectorMessage>) backendApplicationContext.getBean(TestBackendContext.SUBMITTED_MESSAGES_LIST_BEAN_NAME);
        toControllerSubmittedMessages.clear();

        BackendClientInfoPersistenceService backendClientInfoPersistenceService = backendApplicationContext.getBean(BackendClientInfoPersistenceService.class);

        //configure backendClient alice in DB
        DomibusConnectorBackendClientInfo alice = backendClientInfoPersistenceService.getBackendClientInfoByName("alice");
        assertThat(alice).as("alice must not be null!").isNotNull();
        alice.setBackendPushAddress(getBackendClientPushServiceAddress(aliceApplicationContext));
        alice.setDefaultBackend(true);
        backendClientInfoPersistenceService.save(alice);

        //configure backendClient bob in DB
        DomibusConnectorBackendClientInfo bob = backendClientInfoPersistenceService.getBackendClientInfoByName("bob");
        assertThat(bob).as("bob must not be null!").isNotNull();
        bob.setBackendPushAddress(null);
        assertThat(bob.isPushBackend()).isFalse();
        backendClientInfoPersistenceService.save(bob);
    }
    
    @After
    public void tearDown() throws InterruptedException {
        System.out.println("###########################AFTER TEST#####################################");
        System.out.println("wait 3 seconds....");
        Thread.sleep(3000);
    }
    
    @Test
    public void testSubmitMessageToBackendService() throws InterruptedException {

        //send message from bob to connector
        DomibusConnectorBackendWebService bobClientEndpoint = bobApplicationContext.getBean("backendClient", DomibusConnectorBackendWebService.class);
        
        DomibusConnectorMessageType msg = TransitionCreator.createMessage();
        DomibsConnectorAcknowledgementType submitMessage = bobClientEndpoint.submitMessage(msg);
        assertThat(submitMessage).isNotNull();
      
    }

    @Test
    public void testRequestMessages() {
        DomibusConnectorBackendWebService bobClientEndpoint = bobApplicationContext.getBean("backendClient", DomibusConnectorBackendWebService.class);

        EmptyRequestType emptyRequest = new EmptyRequestType();

        DomibusConnectorMessagesType domibusConnectorMessagesType = bobClientEndpoint.requestMessages(emptyRequest);

        assertThat(domibusConnectorMessagesType.getMessages()).hasSize(0);
    }

    /**
     * test complete message flow in backend, when controller hands over
     * message to backend for backend delivery, message is directly pushed
     * to backend client alice
     */
    @Test
    public void testSendMessageFromConnectorToBackend_withPush() {
        DomibusConnectorMessage epoMessage = DomainEntityCreator.createEpoMessage();
        epoMessage.setConnectorMessageId("msgid1");
        epoMessage.getMessageDetails().setRefToMessageId(null);
        epoMessage.getMessageDetails().setBackendMessageId(null);

        BackendClientInfoPersistenceService backendClientInfoPersistenceService = backendApplicationContext.getBean(BackendClientInfoPersistenceService.class);
        DomibusConnectorMessagePersistenceService persistenceService = backendApplicationContext.getBean(DomibusConnectorMessagePersistenceService.class);
        BigDataWithMessagePersistenceService bigDataPersistence = backendApplicationContext.getBean(BigDataWithMessagePersistenceService.class);

        persistenceService.persistMessageIntoDatabase(epoMessage, DomibusConnectorMessageDirection.GW_TO_NAT);
        bigDataPersistence.persistAllBigFilesFromMessage(epoMessage);

        //start message processing in backendLinkModule
        DomibusConnectorBackendDeliveryService messageToBackend = backendApplicationContext.getBean(DomibusConnectorBackendDeliveryService.class);

        messageToBackend.deliverMessageToBackend(epoMessage);

        List<DomibusConnectorMessageType> toAlicePushedMessages =
                (List<DomibusConnectorMessageType>) aliceApplicationContext.getBean(BackendClientPushWebServiceConfiguration.PUSH_DELIVERED_MESSAGES_LIST_BEAN_NAME);

        assertThat(toAlicePushedMessages).hasSize(1);
    }

    /**
     * test complete message flow in backend, when controller hands over
     * message to backend for backend delivery, backend is pulling message...
     */
    @Test
    public void testSendMessageFromConnectorToBackend_withPull() throws InterruptedException {
        DomibusConnectorMessage message = DomainEntityCreator.createMessage();
        message.setConnectorMessageId("msgid2");
        message.getMessageDetails().setService(new DomibusConnectorService("BOB", "service_type"));
        message.getMessageDetails().setEbmsMessageId("ebms_23");
        message.getMessageDetails().setRefToMessageId(null);
        message.getMessageDetails().setBackendMessageId(null);
        message.getMessageDetails().setConversationId(null);

        BackendClientInfoPersistenceService backendClientInfoPersistenceService = backendApplicationContext.getBean(BackendClientInfoPersistenceService.class);
        DomibusConnectorMessagePersistenceService persistenceService = backendApplicationContext.getBean(DomibusConnectorMessagePersistenceService.class);
        BigDataWithMessagePersistenceService bigDataPersistence = backendApplicationContext.getBean(BigDataWithMessagePersistenceService.class);

        persistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.GW_TO_NAT);
        bigDataPersistence.persistAllBigFilesFromMessage(message);

        //start message processing in backendLinkModule
        DomibusConnectorBackendDeliveryService messageToBackend = backendApplicationContext.getBean(DomibusConnectorBackendDeliveryService.class);
        messageToBackend.deliverMessageToBackend(message); //message should be on queue


        Thread.sleep(3000); //to be sure wait a little bit before calling backendService

        //ok client bob is now trying to fetch message
        DomibusConnectorBackendWebService bobClientEndpoint = bobApplicationContext.getBean("backendClient", DomibusConnectorBackendWebService.class);
        EmptyRequestType emptyRequest = new EmptyRequestType();
        DomibusConnectorMessagesType domibusConnectorMessagesType = bobClientEndpoint.requestMessages(emptyRequest);
        assertThat(domibusConnectorMessagesType.getMessages()).hasSize(1);

    }

}