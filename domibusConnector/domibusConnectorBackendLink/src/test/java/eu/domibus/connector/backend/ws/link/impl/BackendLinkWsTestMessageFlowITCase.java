
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
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessageContentManager;
import eu.domibus.connector.testdata.TransitionCreator;
import org.junit.jupiter.api.*;
import test.eu.domibus.connector.backend.ws.linktest.client.BackendClientPushWebServiceConfiguration;
import test.eu.domibus.connector.backend.ws.linktest.client.CommonBackendClient;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;

import static eu.domibus.connector.persistence.spring.PersistenceProfiles.STORAGE_DB_PROFILE_NAME;
import static org.assertj.core.api.Assertions.*;

import eu.domibus.connector.ws.backend.webservice.EmptyRequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

/**
 * This test class starts backend SpringApplicationContext on random web port
 * and also start clients alice and bob on random web ports
 * <p>
 * Tests messages flow
 * from controller to backendClient
 * and
 * from backendClient to controller
 * <p>
 * so message tests between the ports can be done...
 * colorizing the outputs from the different spring contexts is not working!
 */
public class BackendLinkWsTestMessageFlowITCase {

    private final static Logger LOGGER = LoggerFactory.getLogger(BackendLinkWsTestMessageFlowITCase.class);

    static ConfigurableApplicationContext backendApplicationContext;
    static ApplicationContext aliceApplicationContext;
    static ApplicationContext bobApplicationContext;
    static ApplicationContext catrinaApplicationcontext;

    private BlockingQueue<DomibusConnectorMessage> toControllerSubmittedMessages;

    @BeforeAll
    public static void beforeClass() {
        backendApplicationContext = setUpBackend();

        bobApplicationContext = setUpClientBob(getBackendServiceWsAddress(backendApplicationContext));
        aliceApplicationContext = setUpClientAlice(getBackendServiceWsAddress(backendApplicationContext));
//        catrinaApplicationcontext = setUpClientCatrina(getBackendServiceWsAddress(backendApplicationContext));

        LOGGER.info("SERVER ADDRESS " + getBackendServiceWsAddress(backendApplicationContext));
        LOGGER.info("CLIENT BOB ADDRESS " + getBackendClientPushServiceAddress(bobApplicationContext));

    }

    @AfterAll
    public static void afterClass() throws InterruptedException {
//        Thread.sleep(120000);
    }

    //setup and start connector backend
    private static ConfigurableApplicationContext setUpBackend() {
        MDC.put("COLOR", "GREEN");
        String dbName = UUID.randomUUID().toString().substring(0, 10); //use random db name to avoid reusing db between test runs
        String[] backendProfiles = new String[]{"db_h2", "backendlink-ws", STORAGE_DB_PROFILE_NAME};
        String[] backendProperties = new String[]{"server.port=0",
                "spring.datasource.url=jdbc:h2:mem:" + dbName,
                "logging.config=classpath:log4j2-test.xml",
                "spring.liquibase.change-log=classpath:/db/changelog/test/testdata.xml",
                "spring.h2.console.enabled=true",
                "spring.h2.console.path=/h2-console",
                "spring.liquibase.enabled=true",
//                "connector.persistence.big-data-impl-class=eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceJpaImpl",
                "connector.backend.ws.key.store.path=classpath:/connector.jks"
        };

        ConfigurableApplicationContext ctx = StartBackendOnly.startUpSpringApplication(backendProfiles, backendProperties);
        System.out.println("backend context started...");
        LOGGER.info("backendServiceStarted....");
        MDC.remove("COLOR");
        return ctx;
    }


    //setup and start application context for client alice
    private static ApplicationContext setUpClientAlice(String backendAddress) {
        MDC.put("COLOR", "CYAN");
        String[] profiles = new String[]{"ws-backendclient-server", "ws-backendclient-client"};
        String[] properties = new String[]{
                "spring.main.allow-bean-definition-overriding=true",
                "ws.backendclient.name=alice",
                "ws.backendclient.cn=alice",
                "server.port=0",
                "connector.backend.ws.address=" + backendAddress};
        ApplicationContext ctx = CommonBackendClient.startSpringApplication(profiles, properties);
        MDC.remove("COLOR");
        return ctx;
    }

    //setup and start application context for client bob
    private static ApplicationContext setUpClientBob(String backendAddress) {
        MDC.put("COLOR", "BLUE");
        String[] profiles = new String[]{"ws-backendclient-server", "ws-backendclient-client"};
        String[] properties = new String[]{
                "spring.main.allow-bean-definition-overriding=true",
                "ws.backendclient.name=bob",
                "ws.backendclient.cn=bob",
                "server.port=0",
                "connector.backend.ws.address=" + backendAddress};
        ApplicationContext ctx = CommonBackendClient.startSpringApplication(profiles, properties);
        MDC.remove("COLOR");
        return ctx;
    }

    private static ApplicationContext setUpClientCatrina(String backendAddress) {
//        MDC.put("COLOR", "CYAN");
        String[] profiles = new String[]{"ws-backendclient-server", "ws-backendclient-client"};
        String[] properties = new String[]{
                "ws.backendclient.name=catrina",
                "ws.backendclient.cn=catrina",
                "server.port=0",
                "connector.backend.ws.address=" + backendAddress};
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

    @BeforeEach
    public void setUp() throws InterruptedException {
        System.out.println("###########################RUN TEST#######################################");
        System.out.println("SERVER ADDRESS: " + getBackendServiceWsAddress(backendApplicationContext));
        System.out.println("CLIENT BOB ADDRESS: " + getBackendClientPushServiceAddress(bobApplicationContext));
//        System.out.println("CLIENT ALICE ADDRESS: " + getBackendClientPushServiceAddress(aliceApplicationContext));
        System.out.println("wait 3 seconds....");
        Thread.sleep(3000);

        this.toControllerSubmittedMessages = (BlockingQueue<DomibusConnectorMessage>) backendApplicationContext.getBean(TestBackendContext.SUBMITTED_MESSAGES_LIST_BEAN_NAME);
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

//    @Test
//    public void testSetupBackend() {
//        ConfigurableApplicationContext ctx = setUpBackend();
//
//        BackendClientInfoPersistenceService backendClientInfoPersistenceService = ctx.getBean(BackendClientInfoPersistenceService.class);
//
//        DomibusConnectorBackendClientInfo bob = backendClientInfoPersistenceService.getBackendClientInfoByName("bob");
//        assertThat(bob).as("bob must not be null!").isNotNull();
//        bob.setBackendPushAddress(null);
//        assertThat(bob.isPushBackend()).isFalse();
//        backendClientInfoPersistenceService.save(bob);
//
//        System.out.println("SERVER ADDRESS: " + getBackendServiceWsAddress(ctx));
//    }


    @AfterEach
    public void tearDown() throws InterruptedException {
        System.out.println("###########################AFTER TEST#####################################");
        System.out.println("wait 3 seconds....");
        Thread.sleep(3000);
    }

    @Test
    public void testSubmitMessageToBackendService() throws InterruptedException {

        //send message from bob to connector
        DomibusConnectorBackendWebService bobClientEndpoint = bobApplicationContext.getBean("backendClient", DomibusConnectorBackendWebService.class);

        DomibusConnectorMessageType msg = TransitionCreator.createEpoMessage();
        DomibsConnectorAcknowledgementType submitMessage = bobClientEndpoint.submitMessage(msg);
        assertThat(submitMessage).isNotNull();

    }

    @Test
    public void testRequestMessages_asAlice() {
        DomibusConnectorBackendWebService bobClientEndpoint = aliceApplicationContext.getBean("backendClient", DomibusConnectorBackendWebService.class);
        EmptyRequestType emptyRequest = new EmptyRequestType();
        DomibusConnectorMessagesType domibusConnectorMessagesType = bobClientEndpoint.requestMessages(emptyRequest);
        assertThat(domibusConnectorMessagesType.getMessages()).hasSize(0);
    }


    @Test
    public void testRequestMessages_asBob() {
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
        epoMessage.getMessageDetails().setService(new DomibusConnectorService("ALICE", "service_type"));

        BackendClientInfoPersistenceService backendClientInfoPersistenceService = backendApplicationContext.getBean(BackendClientInfoPersistenceService.class);
        DomibusConnectorMessagePersistenceService persistenceService = backendApplicationContext.getBean(DomibusConnectorMessagePersistenceService.class);
        DomibusConnectorMessageContentManager bigDataPersistence = backendApplicationContext.getBean(DomibusConnectorMessageContentManager.class);

        persistenceService.persistMessageIntoDatabase(epoMessage, DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND);
//        bigDataPersistence.persistAllBigFilesFromMessage(epoMessage);

        //start message processing in backendLinkModule
        DomibusConnectorBackendDeliveryService messageToBackend = backendApplicationContext.getBean(DomibusConnectorBackendDeliveryService.class);

        messageToBackend.deliverMessageToBackend(epoMessage);

        BlockingQueue<DomibusConnectorMessageType> toAlicePushedMessages =
                (BlockingQueue<DomibusConnectorMessageType>) aliceApplicationContext.getBean(BackendClientPushWebServiceConfiguration.PUSH_DELIVERED_MESSAGES_LIST_BEAN_NAME);

        assertThat(toAlicePushedMessages).hasSize(1);
    }

    /**
     * test complete message flow in backend, when controller hands over
     * message to backend for backend delivery, backend is pulling message...
     */
    //@Ignore("not working because embedded broker is closed before queue is queried")
    @Test
    public void testSendMessageFromConnectorToBackend_withPull() throws InterruptedException {
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(20), () -> {
            DomibusConnectorMessage message = DomainEntityCreator.createMessage();
            message.setConnectorMessageId("msgid2");
            genMsg(message);


            //Thread.sleep(3000); //to be sure wait a little bit before calling backendService

            //ok client bob is now trying to fetch message
            DomibusConnectorBackendWebService bobClientEndpoint = bobApplicationContext.getBean("backendClient", DomibusConnectorBackendWebService.class);

            DomibusConnectorMessagesType domibusConnectorMessagesType;
            do {
                EmptyRequestType emptyRequest = new EmptyRequestType();
                domibusConnectorMessagesType = bobClientEndpoint.requestMessages(emptyRequest);
                Thread.sleep(500);
            } while (domibusConnectorMessagesType != null && domibusConnectorMessagesType.getMessages().size() == 0);

            assertThat(domibusConnectorMessagesType.getMessages()).hasSize(1);

        });
    }

    /**
     * test complete message flow in backend, when controller hands over
     * message to backend for backend delivery, backend is pulling message...
     */
    //@Ignore("not working because embedded broker is closed before queue is queried")
    @Test
    public void testSendMessageFromConnectorToBackend_withPull2EvidenceMsg() throws InterruptedException {
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(20), () -> {
            DomibusConnectorMessage msg1 = DomainEntityCreator.createMessage();
            msg1.setConnectorMessageId("msgid45");
            genMsg(msg1);

            DomibusConnectorMessage msg2 = DomainEntityCreator.createMessage();
            msg2.setConnectorMessageId("msgid56");
            genMsg(msg2);

//            Thread.sleep(3000); //to be sure wait a little bit before calling backendService

            //ok client bob is now trying to fetch message
            DomibusConnectorBackendWebService bobClientEndpoint = bobApplicationContext.getBean("backendClient", DomibusConnectorBackendWebService.class);

            DomibusConnectorMessagesType domibusConnectorMessagesType;
            do {
                EmptyRequestType emptyRequest = new EmptyRequestType();
                domibusConnectorMessagesType = bobClientEndpoint.requestMessages(emptyRequest);
                Thread.sleep(500);
            } while (domibusConnectorMessagesType != null && domibusConnectorMessagesType.getMessages().size() == 0);

            assertThat(domibusConnectorMessagesType.getMessages()).hasSize(2);

            DomibusConnectorMessagePersistenceService p = backendApplicationContext.getBean(DomibusConnectorMessagePersistenceService.class);

            DomibusConnectorMessage msgid45 = p.findMessageByConnectorMessageId("msgid45");
            assertThat(msgid45.getMessageDetails().getDeliveredToBackend()).isNotNull();

            DomibusConnectorMessage msgid56 = p.findMessageByConnectorMessageId("msgid56");
            assertThat(msgid56.getMessageDetails().getDeliveredToBackend()).isNotNull();
        });
    }

    private void genMsg(DomibusConnectorMessage message) {
        message.getMessageDetails().setService(new DomibusConnectorService("BOB", "service_type"));
        message.getMessageDetails().setEbmsMessageId("ebms_23");
        message.getMessageDetails().setRefToMessageId(null);
        message.getMessageDetails().setBackendMessageId(null);
        message.getMessageDetails().setConversationId(null);

        BackendClientInfoPersistenceService backendClientInfoPersistenceService = backendApplicationContext.getBean(BackendClientInfoPersistenceService.class);
        DomibusConnectorMessagePersistenceService persistenceService = backendApplicationContext.getBean(DomibusConnectorMessagePersistenceService.class);
        DomibusConnectorMessageContentManager bigDataPersistence = backendApplicationContext.getBean(DomibusConnectorMessageContentManager.class);

        persistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND);
//            bigDataPersistence.persistAllBigFilesFromMessage(message);

        //start message processing in backendLinkModule
        DomibusConnectorBackendDeliveryService messageToBackend = backendApplicationContext.getBean(DomibusConnectorBackendDeliveryService.class);
        messageToBackend.deliverMessageToBackend(message); //message should be on queue
    }

}