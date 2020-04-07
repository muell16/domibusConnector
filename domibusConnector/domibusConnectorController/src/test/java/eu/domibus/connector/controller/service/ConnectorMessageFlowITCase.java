
package eu.domibus.connector.controller.service;


import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.test.util.ITCaseTestContext;
import eu.domibus.connector.controller.test.util.LoadStoreMessageFromPath;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageConfirmationBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageDetailsBuilder;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
 *
 *  Check Action, Service of EvidenceMessages, is not necessary because
 *  this checks are done by the EvidenceBuilder checks
 *
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@SpringBootTest(classes = {ITCaseTestContext.class})
@Sql(scripts = "/testdata.sql") //adds testdata to database like domibus-blue party
@ActiveProfiles({"ITCaseTestContext", STORAGE_DB_PROFILE_NAME, "test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ConnectorMessageFlowITCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectorMessageFlowITCase.class);

    public static final Duration TEST_TIMEOUT = Duration.ofSeconds(20);

    public static String TEST_FILE_RESULTS_DIR_PROPERTY_NAME = "test.file.results";
    private File testResultsFolder;
    private String testDateAsString;

    @Autowired
    DataSource ds;

    @Autowired
    ITCaseTestContext.DomibusConnectorGatewaySubmissionServiceInterceptor domibusConnectorGatewaySubmissionServiceInterceptor;

    @Autowired
    ITCaseTestContext.DomibusConnectorBackendDeliveryServiceInterceptor backendInterceptor;

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

        //clear gw submission interceptor mock
        Mockito.reset(domibusConnectorGatewaySubmissionServiceInterceptor);
        //clear backend interceptor mock
        Mockito.reset(backendInterceptor);

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
    public void testReceiveMessageFromGw(TestInfo testInfo) throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {

        String EBMS_ID = "e23";
        String CONNECTOR_MESSAGE_ID = testInfo.getDisplayName();
        String MSG_FOLDER = "msg2";

        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {

            DomibusConnectorMessage testMessage = deliverMessageFromGw(MSG_FOLDER, EBMS_ID, CONNECTOR_MESSAGE_ID);

            LOGGER.info("message with confirmations: [{}]", testMessage.getMessageConfirmations());

            DomibusConnectorMessage take = toBackendDeliveredMessages.take(); //wait until a message is put into queue
            assertThat(toBackendDeliveredMessages).hasSize(0); //queue should be empty!
            assertThat(take).isNotNull();

            DomibusConnectorMessage relayRemmdEvidenceMsg = toGwDeliveredMessages.take();
            assertThat(relayRemmdEvidenceMsg.getMessageConfirmations().get(0).getEvidenceType())
                    .as("RelayREMMD acceptance message")
                    .isEqualTo(DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE);
            DomibusConnectorMessageDetails relayRemmdEvidenceMsgDetails = relayRemmdEvidenceMsg.getMessageDetails();
            assertThat(relayRemmdEvidenceMsgDetails.getRefToMessageId())
                    .as("refToMessageId must be set to the original BusinessMessage EBMS ID")
                    .isEqualTo(EBMS_ID);
            assertThat(relayRemmdEvidenceMsgDetails.getConversationId())
                    .as("Conversation ID must be the same as the business message")
                    .isEqualTo(testMessage.getMessageDetails().getConversationId());
            assertThat(relayRemmdEvidenceMsgDetails.getFromParty())
                    .as("Parties must be switched")
                    .isEqualTo(testMessage.getMessageDetails().getToParty());
            assertThat(relayRemmdEvidenceMsgDetails.getToParty())
                    .as("Parties must be switched")
                    .isEqualTo(testMessage.getMessageDetails().getFromParty());


            //message status confirmed
            DomibusConnectorMessage persistedMessage = messagePersistenceService.findMessageByConnectorMessageId(CONNECTOR_MESSAGE_ID);
            assertThat(messagePersistenceService.checkMessageConfirmedOrRejected(persistedMessage))
                    .as("Message is currently neither confirmed nor rejected")
                    .isFalse();


        });
    }

    /**
     * RCV message from GW
     *
     *   -) Backend must have received MSG
     *   -) GW must have received RELAY_REMMD_ACCEPTANCE
     *
     *   -) test responds with DELIVERY Trigger
     *
     *   -) GW must have received DELIVERY_EVIDENCE
     *
     *   -) tests responds with RETRIEVAL_TRIGGER
     *
     *   -) GW must have received RETRIEVAL_EVIDENCE
     *
     */
    @Test
    public void testReceiveMessageFromGw_respondWithDelivery(TestInfo testInfo) throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {

        String EBMS_ID = "EBMS_" + testInfo.getDisplayName();
        String CONNECTOR_MESSAGE_ID = testInfo.getDisplayName();
        String MSG_FOLDER = "msg2";

        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {

            DomibusConnectorMessage testMessage = deliverMessageFromGw(MSG_FOLDER, EBMS_ID, CONNECTOR_MESSAGE_ID);

            LOGGER.info("message with confirmations: [{}]", testMessage.getMessageConfirmations());

            DomibusConnectorMessage take = toBackendDeliveredMessages.take(); //wait until a message is put into queue
            assertThat(toBackendDeliveredMessages).hasSize(0); //queue should be empty!
            assertThat(take).isNotNull();

            DomibusConnectorMessage relayRemmdEvidenceMsg = toGwDeliveredMessages.take();

            DomibusConnectorMessage deliveryTriggerMessage = DomibusConnectorMessageBuilder
                    .createBuilder()
                    .setConnectorMessageId(CONNECTOR_MESSAGE_ID + "_ev1")
                    .addConfirmation(DomibusConnectorMessageConfirmationBuilder
                            .createBuilder()
                            .setEvidenceType(DomibusConnectorEvidenceType.DELIVERY)
                            .setEvidence(new byte[0])
                            .build()
                    )
                    .setMessageDetails(DomibusConnectorMessageDetailsBuilder
                            .create()
                            .withRefToMessageId(EBMS_ID) // <-- wird verwendet um die original nachricht zu finden
                            .withEbmsMessageId(null) //
                            .withAction("")
                            .withService("", "")
                            .withBackendMessageId("")
                            .withConversationId("")
                            .withFromParty(DomainEntityCreator.createPartyAT()) //hier auch leer!
                            .withToParty(DomainEntityCreator.createPartyDE()) //hier auch leer!
                            .withFinalRecipient("")
                            .withOriginalSender("")
                            .build())
                    .build();
            backendSubmissionService.submitToController(deliveryTriggerMessage);


            DomibusConnectorMessage deliveryEvidenceMessage = toGwDeliveredMessages.take();
            assertThat(deliveryEvidenceMessage.getMessageConfirmations().get(0).getEvidenceType())
                    .as("Message must be evidence message of type Delivery")
                    .isEqualTo(DomibusConnectorEvidenceType.DELIVERY);
            assertThat(deliveryEvidenceMessage.getMessageConfirmations().get(0).getEvidence())
                    .as("Generated evidence must be longer than 100 bytes! Ensure that there was really a evidence generated!")
                    .hasSizeGreaterThan(100);
            DomibusConnectorMessageDetails deliveryEvidenceMessageDetails = deliveryEvidenceMessage.getMessageDetails();
            assertThat(deliveryEvidenceMessageDetails.getRefToMessageId()).isEqualTo(EBMS_ID);
            assertThat(deliveryEvidenceMessageDetails.getFromParty())
                    .as("Parties must be switched")
                    .isEqualTo(DomainEntityCreator.createPartyDE());
            assertThat(deliveryEvidenceMessageDetails.getToParty())
                    .as("Parties must be switched")
                    .isEqualTo(DomainEntityCreator.createPartyAT());

            DomibusConnectorMessage messageByConnectorMessageId = messagePersistenceService.findMessageByConnectorMessageId(CONNECTOR_MESSAGE_ID);
            assertThat(messagePersistenceService.checkMessageConfirmed(messageByConnectorMessageId))
                    .as("Message must be in confirmed state")
                    .isTrue();


        });
    }

    //TODO:!!
    //@Test
    public void testReceiveMessageFromGw_respondWithNonDelivery(TestInfo testInfo) throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {

    }


    /**
     * RCV message from GW
     *  but cannot verify ASIC-S container
     *
     *
     *   -) GW must have received RELAY_REMMD_ACCEPTANCE
     *   -) GW must have received NON_DELIVERY
     *   -) From and to Party are switched, within the evidence messages
     *   -) refToMessageId of the evidence messages are the EBMS id of the RCV message
     *
     *
     */
    @Test
    public void testReceiveMessageFromGw_CertificateFailure(TestInfo testInfo) throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {

        String EBMS_ID = "e25";
        String CONNECTOR_MESSAGE_ID = testInfo.getDisplayName();
        String MSG_FOLDER = "msg3";

        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {

            DomibusConnectorMessage testMessage = deliverMessageFromGw(MSG_FOLDER, EBMS_ID, CONNECTOR_MESSAGE_ID);
            LOGGER.info("message with confirmations: [{}]", testMessage.getMessageConfirmations());

            //wait for evidence messages delivered to gw
            List<DomibusConnectorMessage> toGwDeliveredMessages = Stream.of(this.toGwDeliveredMessages.take(), this.toGwDeliveredMessages.take()).collect(Collectors.toList());

            assertThat(toGwDeliveredMessages)
                    .extracting(m -> m.getMessageConfirmations().get(0).getEvidenceType())
                    .as("First a RelayRemmdAcceptance message is transported back to gw, then a NonDelivery")
                    .containsOnly(DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE, DomibusConnectorEvidenceType.NON_DELIVERY);

            assertThat(toGwDeliveredMessages)
                    .extracting(m -> m.getMessageDetails().getRefToMessageId())
                    .as("Evidence Messages transported back to GW must have as refToMessageId the EBMS id")
                    .containsOnly(EBMS_ID, EBMS_ID);

            assertThat(toGwDeliveredMessages)
                    .extracting(m -> m.getMessageDetails().getFromParty())
                    .as("From Party must be switched")
                    .containsOnly(DomainEntityCreator.createPartyDE(), DomainEntityCreator.createPartyDE());

            assertThat(toGwDeliveredMessages)
                    .extracting(m -> m.getMessageDetails().getToParty())
                    .as("To Party must be switched")
                    .containsOnly(DomainEntityCreator.createPartyAT(), DomainEntityCreator.createPartyAT());

            assertThat(toBackendDeliveredMessages)
                    .as("no messages should have been delivered to backend")
                    .hasSize(0); //queue should be empty!
        });
    }


    /**
     * RCV message from GW
     *  but cannot deliver to backend
     *
     *
     *   -) GW must have received RELAY_REMMD_ACCEPTANCE
     *   -) GW must have received NON_DELIVERY
     *
     */
    @Test
    public void testReceiveMessageFromGw_backendDeliveryFailure(TestInfo testInfo) throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {

        String EBMS_ID = "e24";
        String CONNECTOR_MESSAGE_ID = testInfo.getDisplayName();
        String MSG_FOLDER = "msg2";

        //syntetic error on deliver message to backend
        Mockito.doThrow(new RuntimeException("error"))
                .when(backendInterceptor)
                .deliveryToBackend(Mockito.any(DomibusConnectorMessage.class));

        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {

            DomibusConnectorMessage testMessage = deliverMessageFromGw(MSG_FOLDER, EBMS_ID, CONNECTOR_MESSAGE_ID);

            LOGGER.info("message with confirmations: [{}]", testMessage.getMessageConfirmations());

            //wait for evidence messages delivered to gw
            List<DomibusConnectorMessage> collect = Stream.of(toGwDeliveredMessages.take(), toGwDeliveredMessages.take()).collect(Collectors.toList());

            assertThat(collect)
                    .extracting(m -> m.getMessageConfirmations().get(0).getEvidenceType())
                    .containsOnly(DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE, DomibusConnectorEvidenceType.NON_DELIVERY);

            assertThat(toBackendDeliveredMessages).as("no messages should be delivered to backend").hasSize(0); //queue should be empty!
        });
    }




    private DomibusConnectorMessage deliverMessageFromGw(String msgFolder, String EBMS_ID, String CONNECTOR_MESSAGE_ID) throws IOException {
        DomibusConnectorMessage testMessage = LoadStoreMessageFromPath.loadMessageFrom(new ClassPathResource("/testmessages/"+ msgFolder +"/"));
        assertThat(testMessage).isNotNull();
        testMessage.getMessageDetails().setFinalRecipient("final recipient");
        testMessage.getMessageDetails().setOriginalSender("original sender");
        testMessage.getMessageDetails().setEbmsMessageId(EBMS_ID);
        testMessage.getMessageDetails().setBackendMessageId(null);
        testMessage.setConnectorMessageId(CONNECTOR_MESSAGE_ID);
        gatewaySubmissionService.deliverMessageFromGatewayToController(testMessage);
        return testMessage;
    }


    /**
     * Send message from Backend to GW
     *
     *   -) Backend must have received SUBMISSION_ACCEPTANCE
     *   -) GW must have received Business MSG with SUBMISSION_ACCEPTANCE and 2 attachments ASICS-S, tokenXml
     *
     */
    @Test
    public void sendMessageFromBackend(TestInfo testInfo) {
        String EBMS_ID = null;
        String CONNECTOR_MESSAGE_ID = testInfo.getDisplayName();
        String BACKEND_MESSAGE_ID = "n1";
        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {
            DomibusConnectorMessage submittedMessage = submitMessage(EBMS_ID, CONNECTOR_MESSAGE_ID, BACKEND_MESSAGE_ID);


            DomibusConnectorMessage take = toGwDeliveredMessages.take(); //wait until a message is put into queue

            assertThat(take).as("Gw must RCV message").isNotNull();

            assertThat(take.getMessageConfirmations()).as("submission acceptance evidence must be a part of message").hasSize(1); //SUBMISSION_ACCEPTANCE
            assertThat(take.getMessageConfirmations().get(0).getEvidenceType())
                    .as("evidence must be of type submission acceptance")
                    .isEqualTo(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE);

            //ASIC-S + token XML
            assertThat(take.getMessageAttachments()).hasSize(2);
            assertThat(take.getMessageAttachments()).extracting(a -> a.getIdentifier()).containsOnly("ASIC-S", "tokenXML");
            assertThat(take.getMessageContent().getXmlContent()).isNotNull(); //business XML


            //check sent message in DB
            DomibusConnectorMessage loadedMsg = messagePersistenceService.findMessageByConnectorMessageId(CONNECTOR_MESSAGE_ID);
            assertThat(loadedMsg.getMessageDetails().getEbmsMessageId()).isNotBlank();


            DomibusConnectorMessage toBackendEvidence = toBackendDeliveredMessages.take();
            assertThat(toBackendEvidence).isNotNull();
            DomibusConnectorMessageDetails toBackendEvidenceMsgDetails = toBackendEvidence.getMessageDetails();
            assertThat(toBackendEvidence.getMessageConfirmations().get(0).getEvidenceType())
                    .isEqualTo(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE);
            assertThat(toBackendEvidence.getMessageConfirmations().get(0).getEvidence())
                    .as("Generated evidence must be longer than 100 bytes - make sure this way a evidence has been generated")
                    .hasSizeGreaterThan(100);

            assertThat(toBackendEvidenceMsgDetails.getRefToMessageId())
                    .as("To backend back transported evidence message must use backend message id for refToMessageId")
                    .isEqualTo(BACKEND_MESSAGE_ID);
            assertThat(toBackendEvidenceMsgDetails.getFromParty())
                    .as("Parties must be switched")
                    .isEqualTo(submittedMessage.getMessageDetails().getToParty());

            assertThat(toBackendEvidenceMsgDetails.getToParty())
                    .as("Parties must be switched")
                    .isEqualTo(submittedMessage.getMessageDetails().getFromParty());




            //TODO:  evidence Antworten erzeugen und zurückschicken - mit leeren XML

        });
    }


    /**
     * Send message from Backend to GW and RCV evidences for the message
     *
     *  PRE
     *   -) Backend has received SUBMISSION_ACCEPTANCE
     *   -) GW must has received Business MSG with SUBMISSION_ACCEPTANCE and 2 attachments ASICS-S, tokenXml
     *
     *   DO:
     *   -) Generate evidence RELAY_REMMD_ACCEPTANCE
     *
     *  ASSERT:
     *   -) backend has received RELAY_REMMD_ACCEPTANCE
     *
     */
    @Test
    public void sendMessageFromBackend_rcvEvidences(TestInfo testInfo) {
        String EBMS_ID = null;
        String CONNECTOR_MESSAGE_ID = testInfo.getDisplayName();
        String BACKEND_MESSAGE_ID = "backend_" + testInfo.getDisplayName();
        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {
            DomibusConnectorMessage domibusConnectorMessage = submitMessage(EBMS_ID, CONNECTOR_MESSAGE_ID, BACKEND_MESSAGE_ID);

            DomibusConnectorMessage take = toGwDeliveredMessages.take(); //wait until a message is put into queue
            String newEbmsId = take.getMessageDetails().getEbmsMessageId();

            DomibusConnectorMessage toBackendEvidence = toBackendDeliveredMessages.take();
            assertThat(toBackendEvidence).isNotNull();

            //DO
            DomibusConnectorMessage relayRemmdAcceptanceEvidenceForMessage = DomainEntityCreator.createRelayRemmdAcceptanceEvidenceForMessage(domibusConnectorMessage);
            relayRemmdAcceptanceEvidenceForMessage.getMessageDetails().setRefToMessageId(newEbmsId);
            relayRemmdAcceptanceEvidenceForMessage.getMessageDetails().setEbmsMessageId(testInfo.getDisplayName() + "_remote_2");
            this.gatewaySubmissionService.deliverMessageFromGatewayToController(relayRemmdAcceptanceEvidenceForMessage);

            //ASSERT
            DomibusConnectorMessage relayReemdEvidenceMsg = toBackendDeliveredMessages.take();
            assertThat(relayReemdEvidenceMsg)
                    .isNotNull();


        });
    }

    /**
     * Send message from Backend to GW and RCV evidences for the message
     *      but first receive a negative confirmation and afterwards a positive confirmation
     *
     *  PRE
     *   -) Backend has received SUBMISSION_ACCEPTANCE
     *   -) GW must has received Business MSG with SUBMISSION_ACCEPTANCE and 2 attachments ASICS-S, tokenXml
     *
     *   DO:
     *   -) Generate evidence RELAY_REMMD_REJECTION
     *   -) Generate evidence DELIVERY
     *
     *  ASSERT:
     *   -) backend has received RELAY_REMMD_REJECTION
     *   -) backend has NOT received any more confirmations
     *   -) message state in connector is rejected!
     *
     */
    @Test
    public void sendMessageFromBackend_rcvEvidencesPosThenNegative(TestInfo testInfo) {
        String EBMS_ID = null;
        String CONNECTOR_MESSAGE_ID = testInfo.getDisplayName();
        String BACKEND_MESSAGE_ID = "backend_" + testInfo.getDisplayName();
        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {
            DomibusConnectorMessage domibusConnectorMessage = submitMessage(EBMS_ID, CONNECTOR_MESSAGE_ID, BACKEND_MESSAGE_ID);

            DomibusConnectorMessage take = toGwDeliveredMessages.take(); //wait until a message is put into queue
            String newEbmsId = take.getMessageDetails().getEbmsMessageId();

            DomibusConnectorMessage toBackendEvidence = toBackendDeliveredMessages.take();
            assertThat(toBackendEvidence).isNotNull();

            //DO
            DomibusConnectorMessage relayRemmdAcceptanceEvidenceForMessage = DomainEntityCreator.createRelayRemmdAcceptanceEvidenceForMessage(domibusConnectorMessage);
            relayRemmdAcceptanceEvidenceForMessage.getMessageDetails().setRefToMessageId(newEbmsId);
            relayRemmdAcceptanceEvidenceForMessage.getMessageDetails().setEbmsMessageId(testInfo.getDisplayName() + "_remote_2");
            this.gatewaySubmissionService.deliverMessageFromGatewayToController(relayRemmdAcceptanceEvidenceForMessage);

            //ASSERT
            DomibusConnectorMessage relayReemdEvidenceMsg = toBackendDeliveredMessages.take();
            assertThat(relayReemdEvidenceMsg)
                    .isNotNull();


        });
    }


    //TODO: ASIC-S container fehler, SUBMISSION_REJECTION

    /**
     * Send message from Backend to GW
     *
     *   -) Backend must have received SUBMISSION_REJECTION
     *   -) GW receives nothing
     *
     */
    @Test
    public void sendMessageFromBackend_submitToGwFails(TestInfo testInfo) throws DomibusConnectorGatewaySubmissionException {

        //syntetic error on submitting message...
        Mockito.doThrow(new DomibusConnectorGatewaySubmissionException("error"))
                .when(domibusConnectorGatewaySubmissionServiceInterceptor)
                .submitToGateway(Mockito.any(DomibusConnectorMessage.class));

        String EBMS_ID = null;
        String CONNECTOR_MESSAGE_ID = testInfo.getDisplayName();
        String BACKEND_MESSAGE_ID = "n2";
        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {
            submitMessage(EBMS_ID, CONNECTOR_MESSAGE_ID, BACKEND_MESSAGE_ID);

            DomibusConnectorMessage toBackendEvidence = toBackendDeliveredMessages.take();
            assertThat(toBackendEvidence).as("Backend must RCV a Submission Rejection").isNotNull();
            assertThat(toBackendEvidence.getMessageConfirmations()).hasSize(1);
            assertThat(toBackendEvidence.getMessageConfirmations().get(0).getEvidenceType())
                    .as("Backend must RCV a Submission Rejection")
                    .isEqualTo(DomibusConnectorEvidenceType.SUBMISSION_REJECTION);

            //check attribute refToMessageId
            assertThat(toBackendEvidence.getMessageDetails().getRefToMessageId())
                    .as("refToMessageId must match backend message id")
                    .isEqualTo(BACKEND_MESSAGE_ID);

            //parties, services, action..


        });
    }

    //empfang negativ evidences NON_DELIVERY, NON_RETRIEVAL

    //DELIVERY kommt nach einer RELAY_REMMD_REJECTION
    //result Logging event ins Business Log KEINE nachricht ans Backend!


    private DomibusConnectorMessage submitMessage(String ebmsId, String connectorMessageId, String backendMessageId) {
        DomibusConnectorMessageBuilder msgBuilder = DomibusConnectorMessageBuilder.createBuilder();
        DomibusConnectorMessage msg = msgBuilder.setMessageContent(DomainEntityCreator.createMessageContentWithDocumentWithNoSignature())
                .setConnectorMessageId(connectorMessageId)
                .setMessageDetails(DomibusConnectorMessageDetailsBuilder
                        .create()
                        .withEbmsMessageId(ebmsId)
                        .withAction("action1")
                        .withService("service1", "servicetype")
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
        return msg;
    }
}
