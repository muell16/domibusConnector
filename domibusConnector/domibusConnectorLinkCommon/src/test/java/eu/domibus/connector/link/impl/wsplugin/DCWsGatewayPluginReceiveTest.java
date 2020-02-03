package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import test.eu.domibus.connector.link.util.TestGW;
import eu.domibus.connector.testdata.TransitionCreator;
import eu.domibus.connector.ws.gateway.delivery.webservice.DomibusConnectorGatewayDeliveryWebService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import test.eu.domibus.connector.link.util.TestGW;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;


import static eu.domibus.connector.link.LinkTestContext.SUBMIT_TO_CONNECTOR_QUEUE;
import static eu.domibus.connector.link.service.DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = {DCWsGatewayPluginTestContext.class},
        properties = {
        "debug=true",
                "connector.link.autostart=false",
                "link.wsgatewayplugin.soap.key-store.path=classpath:/keystores/connector-gwlink-keystore.jks",
                "link.wsgatewayplugin.soap.key-store.password=12345",
                "link.wsgatewayplugin.soap.private-key.alias=connector",
                "link.wsgatewayplugin.soap.private-key.password=12345",
                "link.wsgatewayplugin.soap.trust-store.password=12345",
                "link.wsgatewayplugin.soap.trust-store.path=classpath:/keystores/connector-gwlink-truststore.jks",

        },
webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({LINK_PLUGIN_PROFILE_NAME,
        "test",
        "ws-test",
        DCWsPluginConfiguration.DC_WS_GATEWAY_PLUGIN_PROFILE_NAME,
        DCWsPluginConfiguration.DC_WS_PLUGIN_PROFILE_NAME})
public class DCWsGatewayPluginReceiveTest {

    private static final Logger LOGGER = LogManager.getLogger(DCWsGatewayPluginReceiveTest.class);

    @Autowired
    @Qualifier(SUBMIT_TO_CONNECTOR_QUEUE)
    public BlockingQueue<DomibusConnectorMessage> toConnectorSubmittedMessages;

    @MockBean
    SubmitToConnector submitToConnector;

    @LocalServerPort
    int serverPort;

    @Autowired
    DCWsPluginConfigurationProperties config;


    @BeforeEach
    public void beforeEach() throws IOException {
        toConnectorSubmittedMessages.clear();
        LOGGER.info("Server port is [{}]", serverPort);
    }

    @BeforeAll
    public static void cleanDirs() throws IOException {

    }


    @Test
    public void testReceiveMessage() throws InterruptedException {
        //TODO: send message...

        DomibusConnectorMessageType message = TransitionCreator.createMessage();


        String address = "http://localhost:" + serverPort + "/services/gateway";

        ConfigurableApplicationContext testGwCtx = TestGW.startContext(new String[]{"connector.address=" + address});
        DomibusConnectorGatewayDeliveryWebService connectorDeliveryClient = TestGW.getConnectorDeliveryClient(testGwCtx);
        DomibsConnectorAcknowledgementType domibsConnectorAcknowledgementType = connectorDeliveryClient.deliverMessage(message);

        assertThat(domibsConnectorAcknowledgementType.isResult()).isTrue();

        assertThat(toConnectorSubmittedMessages.size()).as("must have received 1 message!").isEqualTo(1);


        Thread.sleep(100000);

    }



}