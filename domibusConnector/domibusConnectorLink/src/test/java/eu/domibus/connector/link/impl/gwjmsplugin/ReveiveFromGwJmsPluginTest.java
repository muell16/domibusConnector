package eu.domibus.connector.link.impl.gwjmsplugin;

import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.controller.service.TransportStateService;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transition.DomibusConnectorConfirmationType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StreamUtils;

import javax.jms.JMSException;
import javax.jms.Message;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static eu.domibus.connector.domain.enums.TransportState.ACCEPTED;
import static eu.domibus.connector.domain.enums.TransportState.FAILED;
import static eu.domibus.connector.link.impl.gwjmsplugin.GwJmsPluginConstants.ASIC_S_MIMETYPE;
import static org.mockito.ArgumentMatchers.refEq;

@SpringBootTest(classes = {ReveiveFromGwJmsPlugin.class, GwJmsPluginConfigurationProperties.class}

)
@ActiveProfiles({GwJmsPluginConfiguration.GW_JMS_PLUGIN_PROFILE, "test", "jms-test"})
@Disabled
class ReveiveFromGwJmsPluginTest {

    public static final String ATTACHMENT_LOCATION = "./target/testattachments";


    @MockBean
    SubmitToConnector submitToConnector;

    @MockBean
    TransportStateService transportStateService;

    @MockBean
    DomibusConnectorLinkPartner linkPartner;

    @MockBean
    DomibusConnectorLinkConfiguration linkConfiguration;

    @Autowired
    ReveiveFromGwJmsPlugin reveiveFromGwJmsPlugin;

    @Autowired
    GwJmsPluginConfigurationProperties config;




    @BeforeEach
    public void beforeEach() throws IOException {
        config.setAttachmentStorageLocation(Paths.get(ATTACHMENT_LOCATION));
        Path attachmentStorageLocation = config.getAttachmentStorageLocation();
        Files.createDirectories(attachmentStorageLocation);
    }

    @BeforeAll
    public static void cleanDirs() throws IOException {
        FileSystemUtils.deleteRecursively(Paths.get(ATTACHMENT_LOCATION));
    }

    @Test
    void onMessageReceive() throws JMSException, IOException {
        config.setPutAttachmentInQueue(false);
        Message msg = new TestJmsMessage();
        msg.setJMSCorrelationID("jms1");
        msg.setStringProperty("messageType", "incomingMessage");
        msg.setStringProperty("messageId", "EBMS765");
        msg.setStringProperty("action", "action1");
        msg.setStringProperty("conversationId", "conversation1");
        msg.setStringProperty("fromPartyId", "part1");
        msg.setStringProperty("fromPartyIdType", "urn:oasis:names:tc:ebcore:partyid-type:unregistered");
        msg.setStringProperty("fromRole", "GW");
        msg.setStringProperty("toPartyId", "part1");
        msg.setStringProperty("toPartyIdType", "urn:oasis:names:tc:ebcore:partyid-type:unregistered");
        msg.setStringProperty("toRole", "GW");
        msg.setStringProperty("originalSender", "sender");
        msg.setStringProperty("finalRecipient", "finalRecipient");
        msg.setStringProperty("service", "service1");
        msg.setStringProperty("serviceType", "urn:e-codex:services:");
        msg.setStringProperty("protocol", "AS4");
        msg.setStringProperty("refToMessageId", "id2341");
        msg.setStringProperty("agreementRef", "abc");
        msg.setIntProperty("totalNumberOfPayloads", 2);

        msg.setStringProperty("payload_1_description", "messageContent");
        msg.setStringProperty("payload_1_mimeContentId", "12312-das123");
        msg.setStringProperty("payload_1_mimeType", "text/xml");
        msg.setStringProperty("payload_1_fileName", "msg1/file1");


        msg.setStringProperty("payload_2_description", GwJmsPluginConstants.ASIC_S_DESCRIPTION_NAME);
        msg.setStringProperty("payload_2_mimeContentId", "12312-abc");
        msg.setStringProperty("payload_2_mimeType", ASIC_S_MIMETYPE);
        msg.setStringProperty("payload_2_fileName", "msg1/file2");

        Path msg1 = config.getAttachmentStorageLocation().resolve("msg1");
        Files.createDirectories(msg1);

        Path file1 = msg1.resolve("file1");
        try (FileOutputStream fos = new FileOutputStream(file1.toFile()); InputStream is = new ClassPathResource("/examples/Form_A.xml").getInputStream()) {
            StreamUtils.copy(is, fos);
        }

        Path file2 = msg1.resolve("file2");
        try (FileOutputStream fos = new FileOutputStream(file2.toFile()); InputStream is = new ClassPathResource("/examples/asic-s.asics").getInputStream()) {
            StreamUtils.copy(is, fos);
        }

        reveiveFromGwJmsPlugin.onMessage(msg);

        Mockito.verify(submitToConnector, Mockito.times(1))
                .submitToConnector(Mockito.any(DomibusConnectorMessage.class), Mockito.any(DomibusConnectorLinkPartner.class));
    }

    @Test
    void onMessageReceive_evidenceMessage() throws JMSException, IOException {
        config.setPutAttachmentInQueue(false);
        Message msg = new TestJmsMessage();
        msg.setJMSCorrelationID("jms1");
        msg.setStringProperty("messageType", "incomingMessage");
        msg.setStringProperty("messageId", "EBMS765");
        msg.setStringProperty("action", "action1");
        msg.setStringProperty("conversationId", "conversation1");
        msg.setStringProperty("fromPartyId", "part1");
        msg.setStringProperty("fromPartyIdType", "urn:oasis:names:tc:ebcore:partyid-type:unregistered");
        msg.setStringProperty("fromRole", "GW");
        msg.setStringProperty("toPartyId", "part1");
        msg.setStringProperty("toPartyIdType", "urn:oasis:names:tc:ebcore:partyid-type:unregistered");
        msg.setStringProperty("toRole", "GW");
        msg.setStringProperty("originalSender", "sender");
        msg.setStringProperty("finalRecipient", "finalRecipient");
        msg.setStringProperty("service", "service1");
        msg.setStringProperty("serviceType", "urn:e-codex:services:");
        msg.setStringProperty("protocol", "AS4");
        msg.setStringProperty("refToMessageId", "id2341");
        msg.setStringProperty("agreementRef", "abc");
        msg.setIntProperty("totalNumberOfPayloads", 1);

        msg.setStringProperty("payload_1_description", DomibusConnectorConfirmationType.DELIVERY.value());
        msg.setStringProperty("payload_1_mimeContentId", "12312-das123");
        msg.setStringProperty("payload_1_mimeType", "text/xml");
        msg.setStringProperty("payload_1_fileName", "msg1/file1");


        Path msg1 = config.getAttachmentStorageLocation().resolve("msg1");
        Files.createDirectories(msg1);

        Path file1 = msg1.resolve("file1");
        try (FileOutputStream fos = new FileOutputStream(file1.toFile()); InputStream is = new ClassPathResource("/examples/Form_A.xml").getInputStream()) {
            StreamUtils.copy(is, fos);
        }

//        Path file2 = msg1.resolve("file2");
//        try (FileOutputStream fos = new FileOutputStream(file2.toFile()); InputStream is = new ClassPathResource("/examples/asic-s.asics").getInputStream()) {
//            StreamUtils.copy(is, fos);
//        }

        reveiveFromGwJmsPlugin.onMessage(msg);

        Mockito.verify(submitToConnector, Mockito.times(1))
                .submitToConnector(Mockito.any(DomibusConnectorMessage.class), Mockito.any(DomibusConnectorLinkPartner.class));
    }




    @Test
    void onMessageFailedResponse() throws JMSException {
        Message msg = new TestJmsMessage();
        msg.setJMSCorrelationID("jms1");
        msg.setStringProperty("messageType", "submitResponse");
        msg.setStringProperty("messageId", null);
        msg.setStringProperty("errorDetail", "a error occured...");


        reveiveFromGwJmsPlugin.onMessage(msg);

        TransportStateService.DomibusConnectorTransportState state = new TransportStateService.DomibusConnectorTransportState();
        state.setStatus(FAILED);
        state.setConnectorTransportId(new TransportStateService.TransportId("jms1"));
        state.setRemoteMessageId(null);
        state.setTransportImplId(null);


        Mockito.verify(transportStateService, Mockito.times(1))
                .updateTransportStatus(refEq(state, "messageErrorList"));


    }

    @Test
    void onMessageSuccessResponse() throws JMSException {
        Message msg = new TestJmsMessage();
        msg.setJMSCorrelationID("jms1");
        msg.setStringProperty("messageType", "submitResponse");
        msg.setStringProperty("messageId", "ebms1234");
        msg.setStringProperty("errorDetail", null);


        reveiveFromGwJmsPlugin.onMessage(msg);

        TransportStateService.DomibusConnectorTransportState state = new TransportStateService.DomibusConnectorTransportState();
        state.setStatus(ACCEPTED);
        state.setConnectorTransportId(new TransportStateService.TransportId("jms1"));
        state.setRemoteMessageId("ebms1234");
        state.setTransportImplId(null);


        Mockito.verify(transportStateService, Mockito.times(1))
                .updateTransportStatus(refEq(state, "messageErrorList"));


    }




}