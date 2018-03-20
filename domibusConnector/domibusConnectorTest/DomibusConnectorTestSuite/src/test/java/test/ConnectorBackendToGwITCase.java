package test;

import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.starter.ConnectorStarter;
import eu.domibus.connector.testdata.LoadStoreTransitionMessage;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import eu.domibus.connector.ws.gateway.delivery.webservice.DomibusConnectorGatewayDeliveryWebService;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.SocketUtils;
import test.eu.domibus.connector.backend.ws.linktest.client.CommonBackendClient;
import test.eu.domibus.connector.gateway.link.testgw.TestGW;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

public class ConnectorBackendToGwITCase {

    private static ConfigurableApplicationContext CONNECTOR_APPLICATION_CONTEXT;
    private static ConfigurableApplicationContext TEST_GW_APPLICATION_CONTEXT;
    private static ConfigurableApplicationContext BACKEND_BOB_APPLICATION_CONTEXT;
    private static ConfigurableApplicationContext BACKEND_ALICE_APPLICATION_CONTEXT;
    private static ConfigurableApplicationContext BACKEND_CATRINA_APPLICATION_CONTEXT;

    private static String CONNECTOR_GWLINK_SERVER_ADDRESS;
    private static String TESTGW_SERVER_ADDRESS;
    private static String CONNECTOR_BACKENDLINK_SERVER_ADDRESS;
    private static String BOB_BACKEND_SERVER_ADDRESS;
    private static String ALICE_BACKEND_SERVER_ADDRESS;
    private static DataSource DATA_SOURCE;


    @BeforeClass
    public static void beforeClass() throws SQLException {

        int portTestGW = SocketUtils.findAvailableTcpPort();
        int portConnector = SocketUtils.findAvailableTcpPort();

        int portBobBackend = SocketUtils.findAvailableTcpPort();
        int portAliceBackend = SocketUtils.findAvailableTcpPort();


        //configure server addresses
        //push for gw
        CONNECTOR_GWLINK_SERVER_ADDRESS = String.format("http://localhost:%d/services/domibusConnectorDeliveryWebservice", portConnector);
        //push/pull for backend
        CONNECTOR_BACKENDLINK_SERVER_ADDRESS = String.format("http://localhost:%d/services/backend", portConnector);
        //configure TEST GW address
        TESTGW_SERVER_ADDRESS = String.format("http://localhost:%d/services/submission", portTestGW);
        //BOB backend address
        BOB_BACKEND_SERVER_ADDRESS = String.format("http://localhost:%d/services/backendDelivery", portBobBackend);
        //ALICE backend address
        ALICE_BACKEND_SERVER_ADDRESS = String.format("http://localhost:%d/services/backendDelivery", portAliceBackend);



        //start controller
        String[] args = new String[] {
                "--liquibase.change-log=classpath:/endtoendtest/database/testdata/init-db.xml",
                "--liquibase.enabled=true",
                "--server.port=" + portConnector,
                "--gateway.submission.endpoint.address=" + TESTGW_SERVER_ADDRESS,
                "--gateway.delivery.service.ssl.key.password=12345",
                "--gateway.delivery.service.ssl.keystore.store=store.jks",
                "--gateway.delivery.service.ssl.keystore.password=12345",
                "--gateway.delivery.service.ssl.truststore.store=store.jks",
                "--gateway.delivery.service.ssl.truststore.password=12345",
                "--debug"
        };
        System.out.println("START CONNECTOR CONTEXT");
        CONNECTOR_APPLICATION_CONTEXT = ConnectorStarter.runSpringApplication(args);
        printDashBlock();

        //TODO: prepare testdata....load testdata in db
        DATA_SOURCE = CONNECTOR_APPLICATION_CONTEXT.getBean(DataSource.class);

        setPushBackendUrl("bob", BOB_BACKEND_SERVER_ADDRESS);

        //start clients....
        //alice push backend
        System.out.println("START ALICE CONTEXT");
        BACKEND_ALICE_APPLICATION_CONTEXT = CommonBackendClient.startSpringApplication(new String[]{
                "--spring.profiles.active=" + CommonBackendClient.START_WEBSERVICE_PROFILE,
                "--spring.application.name=alice",
                "--server.port=" + portAliceBackend,
                "--" + CommonBackendClient.PROPERTY_BACKENDCLIENT_NAME + "=alice",
                "--" + CommonBackendClient.PROPERTY_BACKENDCLIENT_KEY_ALIAS + "=alice",
                "--" + CommonBackendClient.PROPERTY_BACKENDCLIENT_KEY_PASSWORD + "=test",
                "--" + CommonBackendClient.PROPERTY_CONNECTOR_BACKEND_ADDRESS + "=" + CONNECTOR_BACKENDLINK_SERVER_ADDRESS});
        //bob push backend
        System.out.println("START BOB CONTEXT");
        BACKEND_BOB_APPLICATION_CONTEXT = CommonBackendClient.startSpringApplication(new String[]{
                        "--spring.profiles.active=" + CommonBackendClient.START_WEBSERVICE_PROFILE,
                        "--spring.application.name=bob",
                        "--server.port=" + portBobBackend,
                        "--" + CommonBackendClient.PROPERTY_BACKENDCLIENT_NAME + "=bob",
                        "--" + CommonBackendClient.PROPERTY_BACKENDCLIENT_KEY_ALIAS + "=bob",
                        "--" + CommonBackendClient.PROPERTY_BACKENDCLIENT_KEY_PASSWORD + "=test",
                        "--" + CommonBackendClient.PROPERTY_CONNECTOR_BACKEND_ADDRESS + "=" + CONNECTOR_BACKENDLINK_SERVER_ADDRESS});
        //catrina //pull backend!
        System.out.println("START CATRINA CONTEXT");
        BACKEND_CATRINA_APPLICATION_CONTEXT = CommonBackendClient.startSpringApplication(new String[]{
                "--spring.main.web-application-type=none",
                "--spring.main.web-environment=false",
                "--" + CommonBackendClient.PROPERTY_BACKENDCLIENT_NAME + "=catrina",
                "--" + CommonBackendClient.PROPERTY_BACKENDCLIENT_KEY_ALIAS + "=catrina",
                "--" + CommonBackendClient.PROPERTY_BACKENDCLIENT_KEY_PASSWORD + "=test",
                "--" + CommonBackendClient.PROPERTY_CONNECTOR_BACKEND_ADDRESS + "=" + CONNECTOR_BACKENDLINK_SERVER_ADDRESS});

        //start gw....
        System.out.println("START TEST GW CONTEXT");
        TEST_GW_APPLICATION_CONTEXT = TestGW.startContextWithArgs(new String[]{
                "--server.port=" + portTestGW,
                "--connector.delivery.endpoint.address=" + CONNECTOR_GWLINK_SERVER_ADDRESS,
                "--gateway.delivery.service.ssl.key.password=12345",
                "--liquibase.enabled=false"
        });
        printDashBlock();
        System.out.println("ALL CONTEXTs are STARTED....beginn testing...");

    }

    static void setPushBackendUrl(String backendName, String url) throws SQLException {
        Connection connection = DATA_SOURCE.getConnection();
        PreparedStatement stm = connection.prepareStatement("UPDATE DOMIBUS_CONNECTOR_BACKEND_INFO SET BACKEND_PUSH_ADDRESS=? WHERE BACKEND_NAME=?");

        stm.setString(1, url);
        stm.setString(2, backendName);

        stm.executeUpdate();
    }


    BlockingQueue<DomibusConnectorMessageType> toGwSubmittedMessages;
    DomibusConnectorGatewayDeliveryWebService connectorDeliveryClient;

    BlockingQueue<DomibusConnectorMessageType> toBobPushedMessagesList;
    DomibusConnectorBackendWebService bobBackendClient;

    BlockingQueue<DomibusConnectorMessageType> toAlicePushedMessagesList;
    DomibusConnectorBackendWebService aliceBackendClient;

    DomibusConnectorBackendWebService catrinaBackendClient;


    @Before
    public void setUp() {
        System.out.println("connectorGWLink         " + CONNECTOR_GWLINK_SERVER_ADDRESS);
        System.out.println("testgw                  " + TESTGW_SERVER_ADDRESS);
        System.out.println("connector backend link  " + CONNECTOR_BACKENDLINK_SERVER_ADDRESS);
        System.out.println("bob backend             " + BOB_BACKEND_SERVER_ADDRESS);
        System.out.println("alice backend           " + ALICE_BACKEND_SERVER_ADDRESS);


        toGwSubmittedMessages = TestGW.getToGwSubmittedMessages(TEST_GW_APPLICATION_CONTEXT);
        toGwSubmittedMessages.clear();
        connectorDeliveryClient = TestGW.getConnectorDeliveryClient(TEST_GW_APPLICATION_CONTEXT);

        toBobPushedMessagesList = CommonBackendClient.getPushedMessagesList(BACKEND_BOB_APPLICATION_CONTEXT);
        toBobPushedMessagesList.clear();
        bobBackendClient = CommonBackendClient.getBackendWebServiceClient(BACKEND_BOB_APPLICATION_CONTEXT);

        toAlicePushedMessagesList = CommonBackendClient.getPushedMessagesList(BACKEND_ALICE_APPLICATION_CONTEXT);
        toAlicePushedMessagesList.clear();
        aliceBackendClient = CommonBackendClient.getBackendWebServiceClient(BACKEND_ALICE_APPLICATION_CONTEXT);

        catrinaBackendClient = CommonBackendClient.getBackendWebServiceClient(BACKEND_CATRINA_APPLICATION_CONTEXT);


    }

    @After
    public void tearDown() throws InterruptedException {
        Thread.sleep(3000); //wait 3s after a test!
    }



    private static void printDashBlock() {
        for (int i=0; i < 10; i++) {
            System.out.println("#####################");
        }
    }

//    @Test(timeout=10000)
//    public void aTest() throws InterruptedException {
//        //do nohting jus see if we can reach to here...
//    }

   
    @Test(timeout=10000) //("test is failing because testdb is not correct initialized!")
    public void testSendEpoMessageFromBackendBobToGw() throws InterruptedException {

        DomibusConnectorMessageType msg1 = LoadStoreTransitionMessage.loadMessageFrom(new ClassPathResource("endtoendtest/messages/epo_forma_backend_to_gw/"));

        assertThat(msg1).isNotNull();

        DomibsConnectorAcknowledgementType acknowledgementType = bobBackendClient.submitMessage(msg1);

        assertThat(acknowledgementType.isResult()).isTrue();

        DomibusConnectorMessageType msg = toGwSubmittedMessages.take();
        assertThat(msg).isNotNull();

    }


}
