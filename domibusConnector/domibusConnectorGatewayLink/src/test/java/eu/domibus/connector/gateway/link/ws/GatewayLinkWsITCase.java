package eu.domibus.connector.gateway.link.ws;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.gateway.link.StartupGwLinkOnly;
import eu.domibus.connector.testdata.TransitionCreator;
import eu.domibus.connector.ws.gateway.delivery.webservice.DomibusConnectorGatewayDeliveryWebService;
import eu.domibus.connector.ws.gateway.submission.webservice.DomibusConnectorGatewaySubmissionWebService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.SocketUtils;
import test.eu.domibus.connector.gateway.link.testgw.TestGW;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
public class GatewayLinkWsITCase {


    static String GW_LINK_SERVER_ADDRESS;
    static String TEST_GW_SERVER_ADDRESS;

    private static ConfigurableApplicationContext GW_LINK_APPLICATION_CONTEXT;
    private static ConfigurableApplicationContext TEST_GW_APPLICATION_CONTEXT;

    @BeforeAll
    public static void beforeClass() {

        int portTestGW = SocketUtils.findAvailableTcpPort();
        int portGWLink = SocketUtils.findAvailableTcpPort();

        GW_LINK_SERVER_ADDRESS = String.format("http://localhost:%d/services/domibusConnectorDeliveryWebservice", portGWLink);
        TEST_GW_SERVER_ADDRESS = String.format("http://localhost:%d/services/submission", portTestGW);

        //start gw link
        GW_LINK_APPLICATION_CONTEXT =
                StartupGwLinkOnly.startGwContext(
                        new String[]{"gwlink-ws"},
                        new String[]{
                                "server.port=" + portGWLink,
                                "connector.gatewaylink.ws.submission-endpoint-address=" + TEST_GW_SERVER_ADDRESS
                        });
        System.out.println(String.format("GW LINK APPLICATION CONTEXT STARTED with address [%s]", GW_LINK_SERVER_ADDRESS));


        TEST_GW_APPLICATION_CONTEXT = TestGW.startContext(new String[]{
                "server.port=" + portTestGW,
                "connector.delivery.endpoint.publishAddress=" + GW_LINK_SERVER_ADDRESS,
                "spring.main.allow-bean-definition-overriding=true",
                "connector.delivery.endpoint.backendPublishAddress=" + GW_LINK_SERVER_ADDRESS,
        });
        System.out.println(String.format("TEST GW APPLICATION CONTEXT STARTED with address [%s]", TEST_GW_SERVER_ADDRESS));
    }

    @BeforeEach
    public void setUp() {
        System.out.println(String.format("GW LINK APPLICATION CONTEXT STARTED with address [%s]", GW_LINK_SERVER_ADDRESS));
        System.out.println(String.format("TEST GW APPLICATION CONTEXT STARTED with address [%s]", TEST_GW_SERVER_ADDRESS));
    }


    @Test
    public void testSendMessageFromGwToGwLink() throws InterruptedException {
        DomibusConnectorGatewayDeliveryWebService connectorDeliveryClient = TestGW.getConnectorDeliveryClient(TEST_GW_APPLICATION_CONTEXT);
        DomibusConnectorMessageType message = TransitionCreator.createMessage();

        connectorDeliveryClient.deliverMessage(message);

        List<DomibusConnectorMessage> fromGwReceivedMessagesList = StartupGwLinkOnly.getFromGwReceivedMessagesList(GW_LINK_APPLICATION_CONTEXT);
        assertThat(fromGwReceivedMessagesList).hasSize(1);
    }

    @Test
    public void testSendMessageFromGwLinkToGw() {
        DomibusConnectorGatewaySubmissionWebService submissionWebServiceClient = StartupGwLinkOnly.getDomibusConnectorGatewaySubmissionWebServiceClient(GW_LINK_APPLICATION_CONTEXT);
        DomibusConnectorMessageType message = TransitionCreator.createMessage();

        submissionWebServiceClient.submitMessage(message);

        LinkedBlockingQueue<DomibusConnectorMessageType> toGwSubmittedMessages = TestGW.getToGwSubmittedMessages(TEST_GW_APPLICATION_CONTEXT);
        assertThat(toGwSubmittedMessages).hasSize(1);
    }

}
