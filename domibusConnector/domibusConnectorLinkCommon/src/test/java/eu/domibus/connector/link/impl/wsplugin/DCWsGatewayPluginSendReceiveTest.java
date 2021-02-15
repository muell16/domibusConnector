package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.enums.LinkMode;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformerService;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.link.service.DCActiveLinkManagerService;
import eu.domibus.connector.link.service.LinkPluginQualifier;
import eu.domibus.connector.persistence.testutils.LargeFileProviderMemoryImpl;
import eu.domibus.connector.testdata.TransitionCreator;
import eu.domibus.connector.ws.gateway.delivery.webservice.DomibusConnectorGatewayDeliveryWebService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import test.eu.domibus.connector.link.util.TestGW;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static eu.domibus.connector.link.service.DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;


@SpringBootTest(classes = {DCWsPluginConfiguration.class, DCWsGatewayPluginSendReceiveTest.WsPluginTestContext.class},
        properties = {
//            "debug=true",
            "connector.link.autostart=false",
//                "logging.level.eu.domibus.connector=debug",
                "link.wsgatewayplugin.soap.key-store.path=classpath:/keystores/connector-gwlink-keystore.jks",
                "link.wsgatewayplugin.soap.key-store.password=12345",
                "link.wsgatewayplugin.soap.private-key.alias=connector",
                "link.wsgatewayplugin.soap.private-key.password=12345",
                "link.wsgatewayplugin.soap.trust-store.password=12345",
                "link.wsgatewayplugin.soap.trust-store.path=classpath:/keystores/connector-gwlink-truststore.jks",
        },
webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({
        LINK_PLUGIN_PROFILE_NAME,
        DCWsPluginConfiguration.DC_WS_PLUGIN_PROFILE_NAME,
        DCWsPluginConfiguration.DC_WS_GATEWAY_PLUGIN_PROFILE_NAME,
})
public class DCWsGatewayPluginSendReceiveTest {

    private String gwSubmitAddress;

    @SpringBootApplication(scanBasePackageClasses = DCWsPluginConfiguration.class, exclude = DataSourceAutoConfiguration.class)
    public static class WsPluginTestContext {

        @Bean
        @Qualifier("toConnectorQueue")
        LinkedBlockingQueue<DomibusConnectorMessage> toConnectorMsgQeue() {
            return new LinkedBlockingQueue<>();
        }

        @Bean
        SubmitToConnector submitToConnector() {
            return (message, linkPartner) -> {
                try {
                    toConnectorMsgQeue().put(message);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            };
        }

        @Bean
        @ConditionalOnMissingBean
        public DomibusConnectorDomainMessageTransformerService domibusConnectorDomainMessageTransformerService() {
            return new DomibusConnectorDomainMessageTransformerService(new LargeFileProviderMemoryImpl());
        }


    }

    private static final Logger LOGGER = LogManager.getLogger(DCWsGatewayPluginSendReceiveTest.class);

    boolean init = false;
    DomibusConnectorGatewayDeliveryWebService connectorDeliveryClient;

    private LinkedBlockingQueue<DomibusConnectorMessageType> toGwSubmittedMessages;

    @Autowired
    @Qualifier("toConnectorQueue")
    private LinkedBlockingQueue<DomibusConnectorMessage> toConnectorSubmittedMessages; // = new LinkedBlockingQueue<>();

//    @MockBean
    @Autowired
    SubmitToConnector submitToConnector;

    @MockBean
    DomibusConnectorLinkConfiguration linkConfiguration;

    @MockBean
    DCActiveLinkManagerService dcActiveLinkManagerService;

    @MockBean
    TransportStatusService transportStatusService;

    @LinkPluginQualifier
    @MockBean
    DCWsGatewayPlugin dcWsGatewayPlugin;

    @Autowired
    DCWsSubmitTo dcWsSubmitTo;

    @Autowired
    DCWsActiveLink dcWsActiveLink;

    @LocalServerPort
    int serverPort;




    @BeforeEach
    public void beforeEach() throws IOException {
        init();

        toConnectorSubmittedMessages.clear();
        LOGGER.info("Server port is [{}]", serverPort);


        DomibusConnectorLinkPartner linkPartner = new DomibusConnectorLinkPartner();
        linkPartner.setLinkMode(LinkMode.PUSH);
        linkPartner.setLinkType(LinkType.GATEWAY);
        Properties p = new Properties();
        p.put("push-address", gwSubmitAddress);
        p.put("encryption-alias", "gw");
        linkPartner.setProperties(p);

        Mockito.when(dcActiveLinkManagerService.getActiveLinkPartner(eq(new DomibusConnectorLinkPartner.LinkPartnerName("cn=gw"))))
                .thenReturn(Optional.of(new DCWsActiveLinkPartner(
                        linkPartner,
                        dcWsSubmitTo,
                        dcWsActiveLink
                        )));

    }



    public void init() {
        if (!init) {

            String address = "http://localhost:" + serverPort + "/services/gateway";
            ConfigurableApplicationContext testGwCtx = TestGW.startContext(new String[]{"server.port=0", "connector.address=" + address});
            connectorDeliveryClient = TestGW.getConnectorDeliveryClient(testGwCtx);
            gwSubmitAddress = TestGW.getSubmitAddress(testGwCtx);
            toGwSubmittedMessages = TestGW.getToGwSubmittedMessages(testGwCtx);
        }
    }

    @Test
    public void testSendMessage() throws InterruptedException {

        DomibusConnectorMessage message = DomainEntityCreator.createMessage();
        dcWsSubmitTo.submitToLink(message, new DomibusConnectorLinkPartner.LinkPartnerName("cn=gw"));

        DomibusConnectorMessageType polledMsg = toGwSubmittedMessages.poll(10, TimeUnit.SECONDS);

        assertThat(polledMsg).isNotNull();

    }


    @Test
    public void testReceiveMessage() throws InterruptedException {
        Assertions.assertTimeout(Duration.ofSeconds(15), () -> {


            DomibusConnectorMessageType message = TransitionCreator.createMessage();


            LOGGER.info("Submitting message to connector");
            DomibsConnectorAcknowledgementType domibsConnectorAcknowledgementType = connectorDeliveryClient.deliverMessage(message);

            assertThat(domibsConnectorAcknowledgementType.isResult()).isTrue();


//            assertThat((toConnectorSubmittedMessages)).hasSize(1);

            DomibusConnectorMessage msg = toConnectorSubmittedMessages.poll(10, TimeUnit.SECONDS);
            assertThat(msg).isNotNull();


        });
//        Thread.sleep(100000);

    }



}