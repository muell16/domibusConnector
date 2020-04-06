
package eu.domibus.connector.controller.service;


import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.test.util.ITCaseTestContext;
import eu.domibus.connector.controller.test.util.LoadStoreMessageFromPath;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageDetailsBuilder;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;

import static eu.domibus.connector.persistence.spring.PersistenceProfiles.STORAGE_DB_PROFILE_NAME;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the message flow in the connector
 * with persistence
 * with security lib
 * with evidence lib
 * <p>
 * WITHOUT
 * backendlink
 * gatewaylink
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
//@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ITCaseTestContext.class})
//@ContextConfiguration
//@TestPropertySource("classpath:config/application-test.properties")
@Sql(scripts = "/testdata.sql") //adds testdata to database like domibus-blue party
@ActiveProfiles({"ITCaseTestContext", STORAGE_DB_PROFILE_NAME, "test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ConnectorMessageFlowITCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectorMessageFlowITCase.class);

    public static String TEST_FILE_RESULTS_DIR_PROPERTY_NAME = "test.file.results";
    private File testResultsFolder;
    private String testDateAsString;

    @Autowired
    DataSource ds;

    @Autowired
    ITCaseTestContext.DomibusConnectorGatewaySubmissionServiceInterceptor domibusConnectorGatewaySubmissionServiceInterceptor;

    @Autowired
    @Qualifier(ITCaseTestContext.TO_GW_DELIVERD_MESSAGES_LIST_BEAN_NAME)
    BlockingQueue<DomibusConnectorMessage> toGwDeliveredMessages;

    @Autowired
    @Qualifier(ITCaseTestContext.TO_BACKEND_DELIVERD_MESSAGES_LIST_BEAN_NAME)
    BlockingQueue<DomibusConnectorMessage> toBackendDeliveredMessages;

    @Autowired
    DomibusConnectorGatewayDeliveryService gatewaySubmissionService;

    @Autowired
    DomibusConnectorBackendSubmissionService backendSubmissionService;

    @Autowired
    DomibusConnectorMessagePersistenceService messagePersistenceService;

    private String testDir;

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        testDir = System.getenv().getOrDefault(TEST_FILE_RESULTS_DIR_PROPERTY_NAME, "./target/testfileresults/");
        testDir = testDir + "/" + ConnectorMessageFlowITCase.class.getSimpleName() + "/" + testInfo.getDisplayName();
        testResultsFolder = new File(testDir);
        testResultsFolder.mkdirs();

        DateFormatter simpleDateFormatter = new DateFormatter();
        simpleDateFormatter.setPattern("yyyy-MM-dd-hh-mm");
        testDateAsString = simpleDateFormatter.print(new Date(), Locale.ENGLISH);

        //clear gw submission interceptor...
        Mockito.reset(domibusConnectorGatewaySubmissionServiceInterceptor);

        //clear delivery lists
        toGwDeliveredMessages.clear();
        toBackendDeliveredMessages.clear();
    }


    /**
     * RCV message from GW
     *
     *   -) Backend must have received MSG
     *   -) GW must have received RELAY_REMMD_ACCEPTANCE
     *
     */
    @Test
//    @Disabled("new certificates does not match testmessage")
    public void testReceiveMessageFromGw() throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(20), () -> {
            DomibusConnectorMessage testMessage = LoadStoreMessageFromPath.loadMessageFrom(new ClassPathResource("/testmessages/msg2/"));

            assertThat(testMessage).isNotNull();
            testMessage.getMessageDetails().setFinalRecipient("final recipient");
            testMessage.getMessageDetails().setOriginalSender("original sender");
            testMessage.getMessageDetails().setEbmsMessageId("ebms1");
            testMessage.getMessageDetails().setBackendMessageId(null);
            testMessage.setConnectorMessageId("g1");

//            testMessage = messagePersistenceService.persistMessageIntoDatabase(testMessage, DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND);

            LOGGER.info("message with confirmations: [{}]", testMessage.getMessageConfirmations());

            gatewaySubmissionService.deliverMessageFromGatewayToController(testMessage);

            DomibusConnectorMessage take = toBackendDeliveredMessages.take(); //wait until a message is put into queue
            assertThat(toBackendDeliveredMessages).hasSize(0); //queue should be empty!
            assertThat(take).isNotNull();

            //TODO: check database!
        });
    }


    /**
     * Send message from Backend to GW
     *
     *   -) Backend must have received SUBMISSION_ACCEPTANCE
     *   -) GW must have received Business MSG with SUBMISSION_ACCEPTANCE and 2 attachments ASICS-S, tokenXml
     *
     */
    @Test
    public void sendMessageFromBackend() {
        String EBMS_ID = null;
        String CONNECTOR_MESSAGE_ID = "t1";
        String BACKEND_MESSAGE_ID = "n1";
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(2000), () -> {
            submitMessage(EBMS_ID, CONNECTOR_MESSAGE_ID, BACKEND_MESSAGE_ID);


            DomibusConnectorMessage take = toGwDeliveredMessages.take(); //wait until a message is put into queue


            assertThat(toBackendDeliveredMessages.isEmpty()).isTrue();
            assertThat(take).as("Gw must RCV message").isNotNull();

            assertThat(take.getMessageConfirmations()).as("submission acceptance evidence must be a part of message").hasSize(1); //SUBMISSION_ACCEPTANCE
            assertThat(take.getMessageConfirmations().get(0).getEvidenceType())
                    .as("evidence must be of type submission acceptance")
                    .isEqualTo(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE);

            //ASIC-S + token XML
            assertThat(take.getMessageAttachments()).hasSize(2);
            assertThat(take.getMessageAttachments()).extracting(a -> a.getIdentifier()).containsOnly("ASIC-S", "tokenXML");

//            LoadStoreMessageFromPath.storeMessageTo(new FileSystemResource(testDir), take);

            //check sent message in DB
            DomibusConnectorMessage loadedMsg = messagePersistenceService.findMessageByConnectorMessageId(CONNECTOR_MESSAGE_ID);
            assertThat(loadedMsg.getMessageDetails().getEbmsMessageId()).isNotBlank();


            DomibusConnectorMessage toBackendEvidence = toBackendDeliveredMessages.take();
            assertThat(toBackendEvidence).isNotNull();

        });
    }

    /**
     * Send message from Backend to GW
     *
     *   -) Backend must have received SUBMISSION_REJECTION
     *   -) GW receives nothing
     *
     */
    @Test
    public void sendMessageFromBackend_submitToGwFails() throws DomibusConnectorGatewaySubmissionException {

        //syntetic error on submitting message...
        Mockito.doThrow(new DomibusConnectorGatewaySubmissionException("error"))
                .when(domibusConnectorGatewaySubmissionServiceInterceptor)
                .submitToGateway(Mockito.any(DomibusConnectorMessage.class));

        String EBMS_ID = null;
        String CONNECTOR_MESSAGE_ID = "t2";
        String BACKEND_MESSAGE_ID = "n2";
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(2000), () -> {
            submitMessage(EBMS_ID, CONNECTOR_MESSAGE_ID, BACKEND_MESSAGE_ID);


//            DomibusConnectorMessage take = toGwDeliveredMessages.take(); //wait until a message is put into queue
//
//
//            assertThat(toBackendDeliveredMessages.isEmpty()).isTrue();
//            assertThat(take).as("Gw must RCV message").isNotNull();
//
//            assertThat(take.getMessageConfirmations()).as("submission acceptance evidence must be a part of message").hasSize(1); //SUBMISSION_ACCEPTANCE
//            assertThat(take.getMessageConfirmations().get(0).getEvidenceType())
//                    .as("evidence must be of type submission rejection")
//                    .isEqualTo(DomibusConnectorEvidenceType.SUBMISSION_REJECTION);
//
//            //ASIC-S + token XML
//            assertThat(take.getMessageAttachments()).hasSize(2);
//            assertThat(take.getMessageAttachments()).extracting(a -> a.getIdentifier()).containsOnly("ASIC-S", "tokenXML");
//
//            //check sent message in DB
//            DomibusConnectorMessage loadedMsg = messagePersistenceService.findMessageByConnectorMessageId(CONNECTOR_MESSAGE_ID);
//            assertThat(loadedMsg.getMessageDetails().getEbmsMessageId()).isNotBlank();


            DomibusConnectorMessage toBackendEvidence = toBackendDeliveredMessages.take();
            assertThat(toBackendEvidence).as("Backend must RCV a Submission Rejection").isNotNull();
            assertThat(toBackendEvidence.getMessageConfirmations()).hasSize(1);
            assertThat(toBackendEvidence.getMessageConfirmations().get(0).getEvidenceType())
                    .as("Backend must RCV a Submission Rejection")
                    .isEqualTo(DomibusConnectorEvidenceType.SUBMISSION_REJECTION);

        });
    }

    private void submitMessage(String ebmsId, String connectorMessageId, String backendMessageId) {
        DomibusConnectorMessageBuilder msgBuilder = DomibusConnectorMessageBuilder.createBuilder();
        DomibusConnectorMessage msg = msgBuilder.setMessageContent(DomainEntityCreator.createMessageContentWithDocumentWithNoSignature())
                .setConnectorMessageId(connectorMessageId)
                .setMessageDetails(DomibusConnectorMessageDetailsBuilder
                        .create()
                        .withEbmsMessageId(ebmsId)
                        .withAction("action1")
                        .withService("service1", "servicetype")
                        .withBackendMessageId("backend1")
                        .withConversationId("conv1")
                        .withBackendMessageId(backendMessageId)
                        .withFromParty(DomainEntityCreator.createPartyAT())
                        .withToParty(DomainEntityCreator.createPartyDE())
                        .withFinalRecipient("final")
                        .withOriginalSender("original")
                        .build()
                ).build();

        msg = messagePersistenceService.persistMessageIntoDatabase(msg, DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);

        backendSubmissionService.submitToController(msg);
    }
}
