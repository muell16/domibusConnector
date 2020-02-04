package eu.domibus.connector.link.service;

import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.link.impl.gwjmsplugin.GwJmsPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import test.eu.domibus.connector.link.LinkTestContext;

import javax.jms.MapMessage;
import java.time.Duration;
import java.util.Properties;

import static eu.domibus.connector.link.service.DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = LinkTestContext.class)
@ActiveProfiles({"test", "jms-test", LINK_PLUGIN_PROFILE_NAME})
@Configuration
@EnableJms
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DomibusConnectorActiveLinkTest {

    private static final Logger LOGGER = LogManager.getLogger(DomibusConnectorActiveLinkTest.class);

    @Autowired
    private DCActiveLinkManagerService linkManager;

    @Autowired
    private JmsTemplate jmsTemplate;

    @BeforeAll
    public static void beforeAll() {
//        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

    }

    @Test
    @Order(1)
    void testLinkSetup() {
        SubmitToLink firstLINK = linkManager.getSubmitToLinkPartner("firstLINK");
        assertThat(firstLINK).isNotNull();
    }

    @Test
    @Order(10)
    void addLink() {
        Assertions.assertTimeout(Duration.ofSeconds(10), () -> {
            DomibusConnectorLinkPartner linkPartner = new DomibusConnectorLinkPartner();
            String linkName = "JMS-GW-PLUGIN";


            linkPartner.setLinkPartnerName(new DomibusConnectorLinkPartner.LinkPartnerName(linkName));
            linkPartner.setDescription("A description for this link...");
            linkPartner.setEnabled(true);
            linkPartner.setLinkType(LinkType.GATEWAY);
            Properties props = new Properties();
            props.put("link.gwjmsplugin.put-attachment-in-queue", "true");
//            props.put("link.gwjmsplugin.put-attachment-in-queue", "true");

            props.put("link.gwjmsplugin.to-connector", "toconnector");
            props.put("link.gwjmsplugin.to-domibus-gateway", "contogw");
            props.put("link.gwjmsplugin.username", "username");
            props.put("link.gwjmsplugin.password", "password");
            props.put("link.gwjmsplugin.in-queue", "contogw");
            props.put("link.gwjmsplugin.reply-queue", "replyfromgwtocon");
            props.put("link.gwjmsplugin.out-queue", "gwtocon");
            props.put("link.gwjmsplugin.error-notify-producer-queue", "errornotifyproducer");
            props.put("link.gwjmsplugin.error-notify-conusmer-queue", "errornotifyconsumer");


            DomibusConnectorLinkConfiguration linkConfig = new DomibusConnectorLinkConfiguration();
            linkConfig.setLinkImpl(GwJmsPlugin.LINK_IMPL_NAME);
            linkConfig.setProperties(props);
            linkConfig.setConfigName(new DomibusConnectorLinkConfiguration.LinkConfigName("config1"));
            linkPartner.setLinkConfiguration(linkConfig);

            linkManager.activateLinkPartner(linkPartner);

            SubmitToLink link = linkManager.getSubmitToLinkPartner(linkName);

            DomibusConnectorMessage sentMessage = DomainEntityCreator.createMessage();
            assertThat(link).isNotNull();
            link.submitToLink(sentMessage, linkPartner.getLinkPartnerName());


            jmsTemplate.setReceiveTimeout(5);
            MapMessage contogw = (MapMessage) jmsTemplate.receive("contogw");

            LOGGER.debug("rcv message is: [{}]", contogw);

            assertThat(contogw).isNotNull();
            assertThat(contogw.getStringProperty("conversationId")).isEqualTo(sentMessage.getMessageDetails().getConversationId());

            //TODO: check attachments...
        });

    }

    @Test
    @Order(20)
    void getLink() {
        String linkName = "JMS-GW-PLUGIN";
        SubmitToLink link = linkManager.getSubmitToLinkPartner(linkName);

        assertThat(link).isNotNull();
    }


}