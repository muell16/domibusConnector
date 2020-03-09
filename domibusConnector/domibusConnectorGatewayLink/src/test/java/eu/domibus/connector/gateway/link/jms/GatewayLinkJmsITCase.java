package eu.domibus.connector.gateway.link.jms;

import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageResponseType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.gateway.link.StartupGwLinkOnly;
import eu.domibus.connector.gateway.link.jms.helper.TestGatewayToDeliveryServiceClient;
import eu.domibus.connector.testdata.TransitionCreator;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.SocketUtils;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
public class GatewayLinkJmsITCase {

    static String GW_LINK_SERVER_ADDRESS;
    static String TEST_GW_SERVER_ADDRESS;

    private static ConfigurableApplicationContext GW_LINK_APPLICATION_CONTEXT;
    private static ConfigurableApplicationContext TEST_GW_APPLICATION_CONTEXT;

    private static String BROKER_URL;

    private ActiveMQConnectionFactory connectionFactory;
    private List<DomibusConnectorMessage> fromGwReceivedMessagesList;
    private LinkedBlockingQueue<TransportStatusService.DomibusConnectorTransportState> setTransportState;


    @BeforeAll
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


    @BeforeEach
    public void setUp() {
        //TODO: configure connection factory of broker URL
        this.connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(BROKER_URL);

        fromGwReceivedMessagesList = StartupGwLinkOnly.getFromGwReceivedMessagesList(GW_LINK_APPLICATION_CONTEXT);
        fromGwReceivedMessagesList.clear();

        setTransportState = StartupGwLinkOnly.getSetTransportStates(GW_LINK_APPLICATION_CONTEXT);
        setTransportState.clear();
    }


    @Disabled
    @Test
    public void testDeliverMessageToConnector() throws InterruptedException {

        TestGatewayToDeliveryServiceClient testClient = new TestGatewayToDeliveryServiceClient(this.connectionFactory);
        DomibusConnectorMessageType testmessage = TransitionCreator.createMessage();
//        testClient.deliverMessage(testmessage);

        Thread.sleep(1000L);
        assertThat(fromGwReceivedMessagesList).hasSize(1);
    }


    @Disabled
    @Test
    public void testDeliverResponseToConnector() throws InterruptedException {

        TestGatewayToDeliveryServiceClient testClient = new TestGatewayToDeliveryServiceClient(this.connectionFactory);
        DomibusConnectorMessageResponseType response = TransitionCreator.createResponse();
//        testClient.deliverResponse(response);


        TransportStatusService.DomibusConnectorTransportState state = setTransportState.take();
        assertThat(state).isNotNull();



    }



    @Disabled
    @Test
    public void sendMessageToGateway() {
//        TestGatewayToDeliveryServiceClient testClient = new TestGatewayToDeliveryServiceClient(this.connectionFactory);
////        DomibusConnectorMessageType testmessage = TransitionCreator.createMessage();
////        testClient.deliverMessage(testmessage);


    }

}
