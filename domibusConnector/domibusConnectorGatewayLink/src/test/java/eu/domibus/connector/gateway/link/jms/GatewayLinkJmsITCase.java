package eu.domibus.connector.gateway.link.jms;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.testutil.TransitionCreator;
import eu.domibus.connector.gateway.link.StartupGwLinkOnly;
import eu.domibus.connector.gateway.link.jms.helper.TestGatewayToDeliveryServiceClient;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.SocketUtils;
import test.eu.domibus.connector.gateway.link.testgw.TestGW;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GatewayJmsLinkITCase {

    static String GW_LINK_SERVER_ADDRESS;
    static String TEST_GW_SERVER_ADDRESS;

    private static ConfigurableApplicationContext GW_LINK_APPLICATION_CONTEXT;
    private static ConfigurableApplicationContext TEST_GW_APPLICATION_CONTEXT;

    private static String BROKER_URL;

    private ActiveMQConnectionFactory connectionFactory;


    @BeforeClass
    public static void prepareEnv() throws Exception {
        //TODO: set up/prepare jms server


        startBroker();

        int portTestGW = SocketUtils.findAvailableTcpPort();
        int portGWLink = SocketUtils.findAvailableTcpPort();

        GW_LINK_SERVER_ADDRESS = String.format("http://localhost:%d/services/domibusConnectorDeliveryWebservice", portGWLink);
        TEST_GW_SERVER_ADDRESS = String.format("http://localhost:%d/services/submission", portTestGW);

        //start gw link
        GW_LINK_APPLICATION_CONTEXT =
                StartupGwLinkOnly.startGwContext(
                        new String[]{"gwlink-jms"},
                        new String[]{
                                "connector.gatewaylink.jms.broker-url=" + BROKER_URL,
                                "server.port=" + portGWLink,
                                "connector.gatewaylink.ws.submission-endpoint-address=" + TEST_GW_SERVER_ADDRESS
                        });
        System.out.println(String.format("GW LINK APPLICATION CONTEXT STARTED with address [%s]", GW_LINK_SERVER_ADDRESS));


    }

    private static void startBroker() throws Exception {
        BrokerService broker = new BrokerService();
        broker.setBrokerName("testbroker");
        broker.setUseShutdownHook(false);

        broker.setDataDirectory("target/activemq-data/");

        int freeBrokerPort = SocketUtils.findAvailableTcpPort();
        BROKER_URL = "tcp://localhost:" + freeBrokerPort;

        broker.addConnector(BROKER_URL);
        broker.start();

    }


    @Before
    public void setUp() {
        //TODO: configure connection factory of broker URL
        this.connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(BROKER_URL);
    }


    @Test
    public void testJmsConnection() throws InterruptedException {


        List<DomibusConnectorMessage> fromGwReceivedMessagesList = StartupGwLinkOnly.getFromGwReceivedMessagesList(GW_LINK_APPLICATION_CONTEXT);

        Thread.sleep(1000L);

        TestGatewayToDeliveryServiceClient testClient = new TestGatewayToDeliveryServiceClient(this.connectionFactory);
        DomibusConnectorMessageType testmessage = TransitionCreator.createMessage();
        testClient.deliverMessge(testmessage);

        Thread.sleep(1000L);

        assertThat(fromGwReceivedMessagesList).hasSize(1);
        //int size = fromGwReceivedMessagesList.size();
        //System.out.println("SIZE IS " + size);

    }


//    @Test
    public void justSendSomething() {
        TestGatewayToDeliveryServiceClient testClient = new TestGatewayToDeliveryServiceClient(this.connectionFactory);
        DomibusConnectorMessageType testmessage = TransitionCreator.createMessage();
        testClient.deliverMessge(testmessage);
    }

}
