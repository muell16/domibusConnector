
package eu.ecodex.dc5.flow.flows;


import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.controller.processor.confirmation.CheckEvidencesTimeoutProcessorImpl;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.controller.service.SubmitToLinkService;
import eu.domibus.connector.controller.test.util.LoadStoreMessageFromPath;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.ecodex.dc5.message.model.*;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType.*;
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
 * <p>
 * <p>
 * Check Action, Service of EvidenceMessages, is not necessary because
 * this checks are done by the EvidenceBuilder checks
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
//@SpringBootTest(classes = {ITCaseTestContext.class},
//        properties = { "connector.controller.evidence.timeoutActive=false", //deactivate the evidence timeout checking timer job during this test
//                "token.issuer.advanced-electronic-system-type=SIGNATURE_BASED",
//                "spring.jta.enabled=true"
//                "logging.level.eu.domibus=TRACE"

//}
//)
//@Commit
//@ActiveProfiles({"ITCaseTestContext", STORAGE_DB_PROFILE_NAME, "test", "flow-test"})
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@Disabled("failing on CI")


@AutoConfigureTestDatabase
@SpringBootTest(classes = {eu.ecodex.dc5.DC5FlowModule.class, ConnectorMessageFlowITCase.MySubmitToLink.class})
@ActiveProfiles({"flow-test"})

@Log4j2
public class ConnectorMessageFlowITCase {


    @Autowired
    private SubmitToConnector submitToConnector;
    private TransactionTemplate txTemplate;

    @Autowired
    private DC5MessageRepo messageRepo;


    @Primary
    @Component
    @Getter
    public static class MySubmitToLink implements SubmitToLinkService {

        @Autowired
        private DC5MessageRepo messageRepo;

        @Override
        public void submitToLink(DC5Message message) throws DomibusConnectorSubmitToLinkException {
            Objects.requireNonNull(message);
            Objects.requireNonNull(message.getConnectorMessageId(), "connector messageId cannot be null!");

            try {
                if (message.getTarget() == MessageTargetSource.GATEWAY) {
                    toGwDeliveredMessages.put(message.getConnectorMessageId());
                } else if (message.getTarget() == MessageTargetSource.BACKEND) {
                    toBackendDeliveredMessages.put(message.getConnectorMessageId());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        @SneakyThrows
        public DC5Message takeToGwMessage() {
            DomibusConnectorMessageId take = toGwDeliveredMessages.take();
            return messageRepo.getByConnectorMessageId(take);
        }

        @SneakyThrows
        public DC5Message takeToBackendMessage() {
            DomibusConnectorMessageId take = toBackendDeliveredMessages.take();
            return messageRepo.getByConnectorMessageId(take);
        }

        BlockingQueue<DomibusConnectorMessageId> toGwDeliveredMessages = new ArrayBlockingQueue<>(50);

        BlockingQueue<DomibusConnectorMessageId> toBackendDeliveredMessages = new ArrayBlockingQueue<>(50);

        public void clear() {
            toBackendDeliveredMessages.clear();
            toGwDeliveredMessages.clear();
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectorMessageFlowITCase.class);

    public static final Duration TEST_TIMEOUT = Duration.ofSeconds(60);

    public static String TEST_FILE_RESULTS_DIR_PROPERTY_NAME = "test.file.results";
    private File testResultsFolder;
    private String testDateAsString;

    @Autowired
    CheckEvidencesTimeoutProcessorImpl checkEvidencesTimeoutProcessor;

    @Autowired
    DataSource ds;

    @Autowired
    PlatformTransactionManager txManager;

    @Autowired
    DomibusConnectorMessageIdGenerator messageIdGenerator;


//    BlockingQueue<DC5Message> toGwDeliveredMessages;
//
//    BlockingQueue<DC5Message> toBackendDeliveredMessages;

//    @Autowired
//    ITCaseTestContext.DomibusConnectorGatewaySubmissionServiceInterceptor domibusConnectorGatewaySubmissionServiceInterceptor;

//    @Autowired
//    ITCaseTestContext.DomibusConnectorBackendDeliveryServiceInterceptor backendInterceptor;

    @Autowired
    MySubmitToLink mySubmitToLink;

//    @Autowired
//    SubmitToConnector submitToConnector;

//    @Autowired
//    DCMessagePersistenceService messagePersistenceService;

//    @Autowired
//    ITCaseTestContext.QueueBasedDomibusConnectorGatewaySubmissionService fromConnectorToGwSubmissionService;
//
//    @Autowired
//    ITCaseTestContext.QueueBasedDomibusConnectorBackendDeliveryService fromConnectorToBackendDeliveryService;

//    @MockBean
//    SubmitToLinkService submitToLinkService;


    private String testDir;

    @AfterEach
    public void clearAfterTest(TestInfo testInfo) throws Exception {
        mySubmitToLink.clear();


    }

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        testDir = System.getenv().getOrDefault(TEST_FILE_RESULTS_DIR_PROPERTY_NAME, "./target/testfileresults/");
        testDir = testDir + "/" + ConnectorMessageFlowITCase.class.getSimpleName() + "/" + testInfo.getDisplayName();
        testResultsFolder = new File(testDir);
        testResultsFolder.mkdirs();

        DateFormatter simpleDateFormatter = new DateFormatter();
        simpleDateFormatter.setPattern("yyyy-MM-dd-hh-mm");
        testDateAsString = simpleDateFormatter.print(new Date(), Locale.ENGLISH);

        this.txTemplate = new TransactionTemplate(txManager);


//        Mockito.when(submitToLinkService.submitToLink(Mockito.any())).thenAnswer(new Answer<Object>() {
//        });

        //clear gw submission interceptor mock
//        Mockito.reset(domibusConnectorGatewaySubmissionServiceInterceptor);
        //clear backend interceptor mock
//        Mockito.reset(backendInterceptor);

        //clear to backend lists
//        fromConnectorToBackendDeliveryService.clearQueue();
//        this.toBackendDeliveredMessages = fromConnectorToBackendDeliveryService.getQueue();
//
//        //clear to gw list
//        fromConnectorToGwSubmissionService.clearQueue();
//        this.toGwDeliveredMessages = fromConnectorToGwSubmissionService.getQueue();


    }


    /**
     * RCV message from GW
     * <p>
     * -) Backend must have received MSG
     * -) GW must have received RELAY_REMMD_ACCEPTANCE
     */
    @Test
    public void testReceiveMessageFromGw(TestInfo testInfo) throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {

        EbmsMessageId EBMS_ID = EbmsMessageId.ofString("e23_2");
        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
        String MSG_FOLDER = "msg2";

        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {

            DC5Message testMessage = createTestMessage(MSG_FOLDER, EBMS_ID, CONNECTOR_MESSAGE_ID);
//            testMessage.getEbmsData().getService().setService("service2");
            submitFromGatewayToController(testMessage);


            LOGGER.info("message with confirmations: [{}]", testMessage.getTransportedMessageConfirmations());

            txTemplate.executeWithoutResult((state) -> {
                DC5Message toBackendDelivered = mySubmitToLink.takeToBackendMessage(); //wait until a message is put into queue
                assertThat(mySubmitToLink.toBackendDeliveredMessages).hasSize(0); //queue should be empty!
                assertThat(toBackendDelivered).isNotNull();
                assertThat(toBackendDelivered.getBackendLinkName())
                        .as("service2 should delivered to backend2")
                        .isEqualTo("backend2");
            });


            txTemplate.executeWithoutResult((state) -> {
                DC5Message relayRemmdEvidenceMsg = mySubmitToLink.takeToGwMessage();
                assertThat(relayRemmdEvidenceMsg.getTransportedMessageConfirmations()).hasSize(1);
                    assertThat(relayRemmdEvidenceMsg.getTransportedMessageConfirmations().get(0).getEvidenceType())
                            .as("RelayREMMD acceptance message")
                            .isEqualTo(DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE);
                    DC5Ebms relayRemmdEvidenceMsgDetails = relayRemmdEvidenceMsg.getEbmsData();
                    assertThat(relayRemmdEvidenceMsgDetails.getRefToEbmsMessageId())
                            .as("refToMessageId must be set to the original BusinessMessage EBMS ID")
                            .isEqualTo(EBMS_ID);
                    assertThat(relayRemmdEvidenceMsgDetails.getConversationId())
                            .as("Conversation ID must be the same as the business message")
                            .isEqualTo(testMessage.getEbmsData().getConversationId());

                    assertThat(testMessage.getEbmsData().getReceiver())
                        .as("Parties must be switched")
                            .usingRecursiveComparison().ignoringFields("role")
                            .isEqualTo(relayRemmdEvidenceMsgDetails.getSender());

                    assertThat(relayRemmdEvidenceMsgDetails.getSender().getRole())
                        .as("Must be initiator")
                            .isEqualTo(DomainEntityCreator.getDefaultInitiatorRole());



//            assertThat(relayRemmdEvidenceMsgDetails.getToParty())
//                    .as("Parties must be switched")
//                    .isEqualTo(DomibusConnectorPartyBuilder.createBuilder().copyPropertiesFrom(testMessage.getEbmsData().getFromParty())
//                            .setRoleType(DomibusConnectorParty.PartyRoleType.RESPONDER)
//                            .build());
            });



//
//
            //message status confirmed
            txTemplate.executeWithoutResult((state) -> {
                DC5Message persistedMessage = messageRepo.getByConnectorMessageId(CONNECTOR_MESSAGE_ID);
                assertThat(persistedMessage.getMessageContent().getCurrentState().getState()).isEqualTo(DC5BusinessMessageState.BusinessMessagesStates.RELAYED);
//                        .as("Message is currently neither confirmed nor rejected")
//                        .isFalse();
            });


        });
    }

//    /**
//     * RCV message from GW
//     * <p>
//     * -) Backend must have received MSG
//     * -) GW must have received RELAY_REMMD_ACCEPTANCE
//     * <p>
//     * -) test responds with DELIVERY Trigger
//     * <p>
//     * -) GW must have received DELIVERY_EVIDENCE
//     * <p>
//     * -) Backend must have rcv DELIVERY_EVIDENCE
//     */
//    @Test
//    @Disabled("unstable on jenkins")
//    public void testReceiveMessageFromGw_respondWithDelivery(TestInfo testInfo) throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {
//
//        EbmsMessageId EBMS_ID = EbmsMessageId.ofString("EBMS_" + testInfo.getDisplayName());
//        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
//        String MSG_FOLDER = "msg2";
//
//        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {
//
//            DC5Message testMessage = deliverMessageFromGw(MSG_FOLDER, EBMS_ID, CONNECTOR_MESSAGE_ID);
//
//            LOGGER.info("message with confirmations: [{}]", testMessage.getTransportedMessageConfirmations());
//
//            DC5Message businessMsg = toBackendDeliveredMessages.poll(10, TimeUnit.SECONDS); //wait until a message is put into queue
//            assertThat(toBackendDeliveredMessages).hasSize(0); //queue should be empty!
//            assertThat(businessMsg).isNotNull();
//
//            DC5Message relayRemmdEvidenceMsg = toGwDeliveredMessages.poll(10, TimeUnit.SECONDS);
//            assertThat(relayRemmdEvidenceMsg).isNotNull();
//
////            DC5Message deliveryTriggerMessage = DomibusConnectorMessageBuilder
////                    .createBuilder()
////                    .setConnectorMessageId(CONNECTOR_MESSAGE_ID + "_ev1")
////                    .addTransportedConfirmations(DomibusConnectorMessageConfirmationBuilder
////                            .createBuilder()
////                            .setEvidenceType(DomibusConnectorEvidenceType.DELIVERY)
////                            .setEvidence(new byte[0])
////                            .build()
////                    )
////                    .setMessageDetails(DomibusConnectorMessageDetailsBuilder
////                            .create()
////                            .withRefToMessageId(businessMsg.getEbmsData().getBackendMessageId()) // <-- is used to find original message
////                            .withEbmsMessageId(null) //
////                            .withAction("")
////                            .withService("", "")
////                            .withBackendMessageId("")
////                            .withConversationId("")
////                            .withFromParty(DomainEntityCreator.createPartyATasInitiator()) //empty
////                            .withToParty(DomainEntityCreator.createPartyDE()) //empty
////                            .withFinalRecipient("")
////                            .withOriginalSender("")
////                            .build())
////                    .build();
////            submitFromBackendToController(deliveryTriggerMessage);
//
//
////            DC5Message deliveryEvidenceMessage = toGwDeliveredMessages.poll(10, TimeUnit.SECONDS);
////            assertThat(deliveryEvidenceMessage)
////                    .extracting(DomainModelHelper::getEvidenceTypeOfEvidenceMessage)
////                    .as("Message must be evidence message of type Delivery")
////                    .isEqualTo(DomibusConnectorEvidenceType.DELIVERY);
////            assertThat(deliveryEvidenceMessage.getTransportedMessageConfirmations().get(0).getEvidence())
////                    .as("Generated evidence must be longer than 100 bytes! Ensure that there was really a evidence generated!")
////                    .hasSizeGreaterThan(100);
////            DC5Ebms deliveryEvidenceMessageDetails = deliveryEvidenceMessage.getEbmsData();
////            assertThat(deliveryEvidenceMessageDetails).isNotNull();
////            assertThat(deliveryEvidenceMessageDetails.getRefToMessageId()).isEqualTo(EBMS_ID);
////            assertThat(deliveryEvidenceMessageDetails.getFromParty())
////                    .as("Parties must be switched")
////                    .isEqualTo(DomainEntityCreator.createPartyDE());
////            assertThat(deliveryEvidenceMessageDetails.getToParty())
////                    .as("Parties must be switched")
////                    .isEqualTo(DomainEntityCreator.createPartyATasInitiator());
////
////            DC5Message deliveryEvidenceToBackendMessage = toBackendDeliveredMessages.poll(10, TimeUnit.SECONDS);
////            assertThat(deliveryEvidenceToBackendMessage)
////                    .isNotNull()
////                    .extracting(DomainModelHelper::getEvidenceTypeOfEvidenceMessage)
////                    .isEqualTo(DomibusConnectorEvidenceType.DELIVERY);
////            assertThat(deliveryEvidenceToBackendMessage.getEbmsData().getRefToMessageId())
////                    .as("The refToMessageId must match the EBMSID of the original message!")
////                    .isEqualTo(businessMsg.getEbmsData().getEbmsMessageId());
////            assertThat(deliveryEvidenceToBackendMessage.getEbmsData().getRefToBackendMessageId())
////                    .as("The backend ref to message id must match the backend message id of the original message!")
////                    .isEqualTo(businessMsg.getEbmsData().getBackendMessageId());
////
////            DC5Message messageByConnectorMessageId = messagePersistenceService.findMessageByConnectorMessageId(CONNECTOR_MESSAGE_ID);
////            assertThat(messagePersistenceService.checkMessageConfirmed(messageByConnectorMessageId))
////                    .as("Message must be in confirmed state")
////                    .isTrue();
//
//
//        });
//    }
//
//
//    /**
//     * RCV message from GW
//     * <p>
//     * -) Backend must have received MSG
//     * -) GW must have received RELAY_REMMD_ACCEPTANCE
//     * <p>
//     * -) test responds with DELIVERY Trigger
//     * -) test responds with a 2nd DELIVERY Trigger
//     * <p>
//     * -) GW must have received only one DELIVERY_EVIDENCE
//     * <p>
//     * -) Backend must have rcv only one DELIVERY_EVIDENCE
//     */
//    @Test
//    @Disabled("test is unstable on jenkins")
//    public void testReceiveMessageFromGw_triggerDeliveryTwice_shouldOnlyRcvOne(TestInfo testInfo) throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {
//
//        EbmsMessageId EBMS_ID = EbmsMessageId.ofString("EBMS_" + testInfo.getDisplayName());
//        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
//        String MSG_FOLDER = "msg2";
//
//        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {
//
//            DC5Message testMessage = deliverMessageFromGw(MSG_FOLDER, EBMS_ID, CONNECTOR_MESSAGE_ID);
//
//            LOGGER.info("message with confirmations: [{}]", testMessage.getTransportedMessageConfirmations());
//
//            DC5Message businessMsg = toBackendDeliveredMessages.poll(10, TimeUnit.SECONDS); //wait until a message is put into queue
//            assertThat(toBackendDeliveredMessages).hasSize(0); //queue should be empty!
//            assertThat(businessMsg).isNotNull();
//
//            DC5Message relayRemmdEvidenceMsg = toGwDeliveredMessages.poll(10, TimeUnit.SECONDS);
//            assertThat(relayRemmdEvidenceMsg).isNotNull();
//
////            DC5Message deliveryTriggerMessage = DomibusConnectorMessageBuilder
////                    .createBuilder()
////                    .setConnectorMessageId(CONNECTOR_MESSAGE_ID + "_ev1")
////                    .addTransportedConfirmations(DomibusConnectorMessageConfirmationBuilder
////                            .createBuilder()
////                            .setEvidenceType(DomibusConnectorEvidenceType.DELIVERY)
////                            .setEvidence(new byte[0])
////                            .build()
////                    )
////                    .setMessageDetails(DomibusConnectorMessageDetailsBuilder
////                            .create()
////                            .withRefToMessageId(businessMsg.getEbmsData().getBackendMessageId()) // <-- wird verwendet um die original nachricht zu finden
////                            .withEbmsMessageId(null) //
////                            .withAction("")
////                            .withService("", "")
////                            .withBackendMessageId("")
////                            .withConversationId("")
////                            .withFromParty(DomainEntityCreator.createPartyATasInitiator()) //hier auch leer!
////                            .withToParty(DomainEntityCreator.createPartyDE()) //hier auch leer!
////                            .withFinalRecipient("")
////                            .withOriginalSender("")
////                            .build())
////                    .build();
////            submitFromBackendToController(deliveryTriggerMessage);
//
////            DC5Message deliveryTriggerMessage2 = DomibusConnectorMessageBuilder
////                    .createBuilder()
////                    .setConnectorMessageId(CONNECTOR_MESSAGE_ID + "_ev1_1")
////                    .addTransportedConfirmations(DomibusConnectorMessageConfirmationBuilder
////                            .createBuilder()
////                            .setEvidenceType(DomibusConnectorEvidenceType.DELIVERY)
////                            .setEvidence(new byte[0])
////                            .build()
////                    )
////                    .setMessageDetails(DomibusConnectorMessageDetailsBuilder
////                            .create()
////                            .withRefToMessageId(businessMsg.getEbmsData().getBackendMessageId()) // <-- wird verwendet um die original nachricht zu finden
////                            .withEbmsMessageId(null) //
////                            .withAction("")
////                            .withService("", "")
////                            .withBackendMessageId("")
////                            .withConversationId("")
////                            .withFromParty(DomainEntityCreator.createPartyATasInitiator()) //hier auch leer!
////                            .withToParty(DomainEntityCreator.createPartyDE()) //hier auch leer!
////                            .withFinalRecipient("")
////                            .withOriginalSender("")
////                            .build())
////                    .build();
////            submitFromBackendToController(deliveryTriggerMessage2);
//
//
//            DC5Message deliveryEvidenceMessage = toGwDeliveredMessages.poll(10, TimeUnit.SECONDS);
//            assertThat(deliveryEvidenceMessage)
//                    .extracting(DomainModelHelper::getEvidenceTypeOfEvidenceMessage)
//                    .as("Message must be evidence message of type Delivery")
//                    .isEqualTo(DomibusConnectorEvidenceType.DELIVERY);
//            assertThat(deliveryEvidenceMessage.getTransportedMessageConfirmations().get(0).getEvidence())
//                    .as("Generated evidence must be longer than 100 bytes! Ensure that there was really a evidence generated!")
//                    .hasSizeGreaterThan(100);
////            DC5Ebms deliveryEvidenceMessageDetails = deliveryEvidenceMessage.getEbmsData();
////            assertThat(deliveryEvidenceMessageDetails).isNotNull();
////            assertThat(deliveryEvidenceMessageDetails.getRefToMessageId()).isEqualTo(EBMS_ID);
////            assertThat(deliveryEvidenceMessageDetails.getFromParty())
////                    .as("Parties must be switched")
////                    .isEqualTo(DomainEntityCreator.createPartyDE());
////            assertThat(deliveryEvidenceMessageDetails.getToParty())
////                    .as("Parties must be switched")
////                    .isEqualTo(DomainEntityCreator.createPartyATasInitiator());
//
//            DC5Message deliveryEvidenceToBackendMessage = toBackendDeliveredMessages.poll(10, TimeUnit.SECONDS);
//            assertThat(deliveryEvidenceToBackendMessage)
//                    .isNotNull()
//                    .extracting(DomainModelHelper::getEvidenceTypeOfEvidenceMessage)
//                    .isEqualTo(DomibusConnectorEvidenceType.DELIVERY);
////            assertThat(deliveryEvidenceToBackendMessage.getEbmsData().getRefToMessageId())
////                    .as("The refToMessageId must match the EBMSID of the original message!")
////                    .isEqualTo(businessMsg.getEbmsData().getEbmsMessageId());
////            assertThat(deliveryEvidenceToBackendMessage.getEbmsData().getRefToBackendMessageId())
////                    .as("The backend ref to message id must match the backend message id of the original message!")
////                    .isEqualTo(businessMsg.getEbmsData().getBackendMessageId());
////            assertThat(deliveryEvidenceToBackendMessage.getEbmsData().getDirection().getTarget())
////                    .isEqualTo(MessageTargetSource.BACKEND);
//
////            DC5Message messageByConnectorMessageId = messagePersistenceService.findMessageByConnectorMessageId(CONNECTOR_MESSAGE_ID);
////            assertThat(messagePersistenceService.checkMessageConfirmed(messageByConnectorMessageId))
////                    .as("Message must be in confirmed state")
////                    .isTrue();
//
//            DC5Message deliveryEvidenceMessage2 = toGwDeliveredMessages.poll(10, TimeUnit.SECONDS);
//            assertThat(deliveryEvidenceMessage2)
//                    .as("No more delivery messages must be transported to GW")
//                    .isNull();
//            DC5Message deliveryEvidenceToBackendMessage2 = toBackendDeliveredMessages.poll(10, TimeUnit.SECONDS);
//            assertThat(deliveryEvidenceToBackendMessage2)
//                    .as("No more delivery messages must be transported to Backend")
//                    .isNull();
//
//        });
//    }
//
//
//    //was ist mit DELIVERY danach NON_RETRIEVAL?
//
//
//    /**
//     * RCV message from GW
//     * <p>
//     * -) Backend must have received MSG
//     * -) GW must have received RELAY_REMMD_ACCEPTANCE
//     * <p>
//     * -) test responds with NON_DELIVERY Trigger
//     * <p>
//     * -) GW must have received NON_DELIVERY_EVIDENCE
//     * <p>
//     * -) Message must be in rejected state
//     */
//    @Test
//    @Disabled
//    public void testReceiveMessageFromGw_respondWithNonDelivery(TestInfo testInfo) throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {
//        EbmsMessageId EBMS_ID = EbmsMessageId.ofString("EBMS_" + testInfo.getDisplayName());
//        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
//        String MSG_FOLDER = "msg2";
//
//        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {
//
//            DC5Message testMessage = deliverMessageFromGw(MSG_FOLDER, EBMS_ID, CONNECTOR_MESSAGE_ID);
//
//            LOGGER.info("message with confirmations: [{}]", testMessage.getTransportedMessageConfirmations());
//
//            DC5Message take = toBackendDeliveredMessages.take(); //wait until a message is put into queue
//            assertThat(toBackendDeliveredMessages).hasSize(0); //queue should be empty!
//            assertThat(take).isNotNull();
//
//            DC5Message relayRemmdEvidenceMsg = toGwDeliveredMessages.take();
//            assertThat(relayRemmdEvidenceMsg)
//                    .as("Evidence must be of type RelayREMMD")
//                    .extracting(DomainModelHelper::getEvidenceTypeOfEvidenceMessage)
//                    .isEqualTo(RELAY_REMMD_ACCEPTANCE);
//
////            DC5Message nonDeliveryTriggerMessage = DomibusConnectorMessageBuilder
////                    .createBuilder()
////                    .setConnectorMessageId(CONNECTOR_MESSAGE_ID + "_ev1")
////                    .addTransportedConfirmations(DomibusConnectorMessageConfirmationBuilder
////                            .createBuilder()
////                            .setEvidenceType(DomibusConnectorEvidenceType.NON_DELIVERY)
////                            .setEvidence(new byte[0])
////                            .build()
////                    )
////                    .setMessageDetails(DomibusConnectorMessageDetailsBuilder
////                            .create()
////                            .withRefToMessageId(EBMS_ID) // <-- wird verwendet um die original nachricht zu finden
////                            .withEbmsMessageId(null) //
////                            .withAction("")
////                            .withService("", "")
////                            .withBackendMessageId("")
////                            .withConversationId("")
////                            .withFromParty(DomainEntityCreator.createPartyATasInitiator()) //hier auch leer!
////                            .withToParty(DomainEntityCreator.createPartyDE()) //hier auch leer!
////                            .withFinalRecipient("")
////                            .withOriginalSender("")
////                            .build())
////                    .build();
////            submitFromBackendToController(nonDeliveryTriggerMessage);
//
//
//            DC5Message deliveryEvidenceMessage = toGwDeliveredMessages.take();
//            assertThat(deliveryEvidenceMessage.getTransportedMessageConfirmations().get(0).getEvidenceType())
//                    .as("Message must be evidence message of type Delivery")
//                    .isEqualTo(DomibusConnectorEvidenceType.NON_DELIVERY);
//            assertThat(deliveryEvidenceMessage.getTransportedMessageConfirmations().get(0).getEvidence())
//                    .as("Generated evidence must be longer than 100 bytes! Ensure that there was really a evidence generated!")
//                    .hasSizeGreaterThan(100);
////            DC5Ebms deliveryEvidenceMessageDetails = deliveryEvidenceMessage.getEbmsData();
////            assertThat(deliveryEvidenceMessageDetails.getRefToMessageId()).isEqualTo(EBMS_ID);
////            assertThat(deliveryEvidenceMessageDetails.getFromParty())
////                    .as("Parties must be switched")
////                    .isEqualToComparingOnlyGivenFields(DomainEntityCreator.createPartyDE(), "partyId", "partyIdType", "role");
////            assertThat(deliveryEvidenceMessageDetails.getToParty())
////                    .as("Parties must be switched")
////                    .isEqualToComparingOnlyGivenFields(DomainEntityCreator.createPartyATasInitiator(), "partyId", "partyIdType", "role");
//
//
////            DC5Message messageByConnectorMessageId = messagePersistenceService.findMessageByConnectorMessageId(CONNECTOR_MESSAGE_ID);
////            assertThat(messagePersistenceService.checkMessageRejected(messageByConnectorMessageId))
////                    .as("Message must be in rejected state")
////                    .isTrue();
//
//            assertThat(toBackendDeliveredMessages.take())
//                    .as("Evidence must be of type NON DELIVERY")
//                    .extracting(DomainModelHelper::getEvidenceTypeOfEvidenceMessage)
//                    .isEqualTo(NON_DELIVERY);
//
//        });
//    }
//
//
//    /**
//     * RCV message from GW
//     * <p>
//     * -) Backend must have received MSG
//     * -) GW must have received RELAY_REMMD_ACCEPTANCE
//     * <p>
//     * -) test responds with DELIVERY Trigger
//     * -) test responds with RETRIEVAL Trigger
//     * <p>
//     * -) GW must have received RETRIEVAL_EVIDENCE
//     * <p>
//     * -) Backend must have RCV DELIVERY Evidence
//     * -) Backend must have RCV RETRIEVAL Evidence
//     * <p>
//     * -) message must be in confirmed state
//     */
//    @Test
//    @Disabled
//    public void testReceiveMessageFromGw_respondWithDeliveryAndRetrieval(TestInfo testInfo) throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {
//        EbmsMessageId EBMS_ID = EbmsMessageId.ofString("EBMS_" + testInfo.getDisplayName());
//        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
//        String MSG_FOLDER = "msg2";
//
//        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {
//
//            DC5Message testMessage = deliverMessageFromGw(MSG_FOLDER, EBMS_ID, CONNECTOR_MESSAGE_ID);
//
////            LOGGER.info("message with confirmations: [{}]", testMessage.getMessageConfirmations());
//
//            DC5Message rcvMsg = toBackendDeliveredMessages.take(); //wait until a message is put into queue
//            assertThat(toBackendDeliveredMessages).hasSize(0); //queue should be empty!
//            assertThat(rcvMsg).isNotNull();
////            assertThat(rcvMsg.getEbmsData().getConnectorBackendClientName()).isEqualTo("default_backend");
////            assertThat(rcvMsg.getEbmsData().getGatewayName()).isEqualTo("test_gw");
//
//            DC5Message relayRemmdEvidenceMsg = toGwDeliveredMessages.take();
//
////            DC5Message deliveryTriggerMessage = DomibusConnectorMessageBuilder
////                    .createBuilder()
////                    .setConnectorMessageId(CONNECTOR_MESSAGE_ID + "_ev1")
////                    .addTransportedConfirmations(DomibusConnectorMessageConfirmationBuilder
////                            .createBuilder()
////                            .setEvidenceType(DomibusConnectorEvidenceType.DELIVERY)
////                            .setEvidence(new byte[0])
////                            .build()
////                    )
////                    .setMessageDetails(DomibusConnectorMessageDetailsBuilder
////                            .create()
////                            .withRefToMessageId(EBMS_ID) // <-- wird verwendet um die original nachricht zu finden
////                            .withEbmsMessageId(null) //
////                            .withAction("")
////                            .withService("", "")
////                            .withBackendMessageId("")
////                            .withConversationId("")
////                            .withFromParty(DomainEntityCreator.createPartyATasInitiator()) //hier auch leer!
////                            .withToParty(DomainEntityCreator.createPartyDE()) //hier auch leer!
////                            .withFinalRecipient("")
////                            .withOriginalSender("")
////                            .build())
////                    .build();
////            submitFromBackendToController(deliveryTriggerMessage);
////            //take delivery from queue
////            DC5Message deliveryEvidenceMessage = toGwDeliveredMessages.take();
//
//
////            DC5Message retrievalTriggerMessage = DomibusConnectorMessageBuilder
////                    .createBuilder()
////                    .setConnectorMessageId(CONNECTOR_MESSAGE_ID + "_ev1")
////                    .addTransportedConfirmations(DomibusConnectorMessageConfirmationBuilder
////                            .createBuilder()
////                            .setEvidenceType(DomibusConnectorEvidenceType.RETRIEVAL)
////                            .setEvidence(new byte[0])
////                            .build()
////                    )
////                    .setMessageDetails(DomibusConnectorMessageDetailsBuilder
////                            .create()
////                            .withRefToMessageId(EBMS_ID) // <-- wird verwendet um die original nachricht zu finden
////                            .withEbmsMessageId(null) //
////                            .withAction("")
////                            .withService("", "")
////                            .withBackendMessageId("")
////                            .withConversationId("")
////                            .withFromParty(DomainEntityCreator.createPartyATasInitiator()) //hier auch leer!
////                            .withToParty(DomainEntityCreator.createPartyDE()) //hier auch leer!
////                            .withFinalRecipient("")
////                            .withOriginalSender("")
////                            .build())
////                    .build();
////            submitFromBackendToController(retrievalTriggerMessage);
//
//
//            //check retrieval msg.
//            DC5Message retrievalMsg = toGwDeliveredMessages.take();
//
//            assertThat(retrievalMsg.getTransportedMessageConfirmations().get(0).getEvidenceType())
//                    .as("Message must be evidence message of type Delivery")
//                    .isEqualTo(DomibusConnectorEvidenceType.RETRIEVAL);
//            assertThat(retrievalMsg.getTransportedMessageConfirmations().get(0).getEvidence())
//                    .as("Generated evidence must be longer than 100 bytes! Ensure that there was really a evidence generated!")
//                    .hasSizeGreaterThan(100);
//            DC5Ebms deliveryEvidenceMessageDetails = retrievalMsg.getEbmsData();
//            assertThat(deliveryEvidenceMessageDetails.getRefToEbmsMessageId()).isEqualTo(EBMS_ID);
////            assertThat(deliveryEvidenceMessageDetails.getFromParty())
////                    .as("Parties must be switched")
////                    .isEqualToComparingOnlyGivenFields(DomainEntityCreator.createPartyDE(), "partyId", "partyIdType", "role");
////            assertThat(deliveryEvidenceMessageDetails.getToParty())
////                    .as("Parties must be switched")
////                    .isEqualToComparingOnlyGivenFields(DomainEntityCreator.createPartyATasInitiator(), "partyId", "partyIdType", "role");
////            assertThat(deliveryEvidenceMessageDetails.getOriginalSender())
////                    .as("original sender must have switched")
////                    .isEqualTo(FINAL_RECIPIENT);
////            assertThat(deliveryEvidenceMessageDetails.getFinalRecipient())
////                    .as("final recipient must have switched")
////                    .isEqualTo(ORIGINAL_SENDER);
//
//            //check message state
////            DC5Message messageByConnectorMessageId = messagePersistenceService.findMessageByConnectorMessageId(CONNECTOR_MESSAGE_ID);
////            assertThat(messagePersistenceService.checkMessageConfirmed(messageByConnectorMessageId))
////                    .as("Message must be in confirmed state")
////                    .isTrue();
//
//            //check send back of generated evidences
//            DC5Message toBackendDeliveryEvidence = toBackendDeliveredMessages.poll(5, TimeUnit.SECONDS);
//            DC5Message toBackendRetrievalEvidence = toBackendDeliveredMessages.poll(5, TimeUnit.SECONDS);
//
//            List toBackendEvidenceList = CollectionUtils.arrayToList(new DC5Message[]{toBackendDeliveryEvidence, toBackendRetrievalEvidence});
//            assertThat(toBackendEvidenceList).hasSize(2);
//
//        });
//    }
//
//
//    /**
//     * RCV message from GW
//     * but cannot verify ASIC-S container
//     * <p>
//     * <p>
//     * -) GW must have received RELAY_REMMD_ACCEPTANCE
//     * -) GW must have received NON_DELIVERY
//     * -) From and to Party are switched, within the evidence messages
//     * -) refToMessageId of the evidence messages are the EBMS id of the RCV message
//     */
//    @Test
//    public void testReceiveMessageFromGw_CertificateFailure(TestInfo testInfo) throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {
//
//        final EbmsMessageId EBMS_ID = EbmsMessageId.ofString("e25");
//        final DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
//        final String MSG_FOLDER = "msg3";
//
//        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {
//
//            DC5Message testMessage = deliverMessageFromGw(MSG_FOLDER, EBMS_ID, CONNECTOR_MESSAGE_ID);
//            LOGGER.info("message with confirmations: [{}]", testMessage.getTransportedMessageConfirmations());
//
//            //wait for evidence messages delivered to gw
//            DC5Message gwmsg1 = this.toGwDeliveredMessages.poll(TEST_TIMEOUT.getSeconds() / 3, TimeUnit.SECONDS);
//            DC5Message gwmsg2 = this.toGwDeliveredMessages.poll(TEST_TIMEOUT.getSeconds() / 3, TimeUnit.SECONDS);
//
//            List<DC5Message> toGwDeliveredMessages = Stream.of(gwmsg1, gwmsg2).collect(Collectors.toList());
//
//            assertThat(toGwDeliveredMessages)
//                    .extracting(m -> m.getTransportedMessageConfirmations().get(0).getEvidenceType())
//                    .as("First a RelayRemmdAcceptance message is transported back to gw, then a NonDelivery")
//                    .containsOnly(DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE, DomibusConnectorEvidenceType.NON_DELIVERY);
//
//            assertThat(toGwDeliveredMessages)
//                    .extracting(m -> m.getEbmsData().getRefToEbmsMessageId())
//                    .as("Evidence Messages transported back to GW must have as refToMessageId the EBMS id")
//                    .containsOnly(EBMS_ID, EBMS_ID);
//
//            DomibusConnectorParty partyDE = DomainEntityCreator.createPartyDE();
//            partyDE.setRoleType(DomibusConnectorParty.PartyRoleType.INITIATOR);
////            assertThat(toGwDeliveredMessages)
////                    .extracting(m -> m.getEbmsData().getFromParty())
////                    .as("From Party must be switched")
////                    .containsOnly(partyDE, partyDE);
//
////            DomibusConnectorParty partyAT = DomainEntityCreator.createPartyATasInitiator();
////            partyAT.setRoleType(DomibusConnectorParty.PartyRoleType.RESPONDER);
////            assertThat(toGwDeliveredMessages)
////                    .extracting(m -> m.getEbmsData().getToParty())
////                    .as("To Party must be switched")
////                    .containsOnly(partyAT, partyAT);
//
//            assertThat(toBackendDeliveredMessages)
//                    .as("no messages should have been delivered to backend")
//                    .hasSize(0); //queue should be empty!
//        });
//    }
//
//
//    /**
//     * RCV message from GW
//     * but cannot deliver to backend
//     * <p>
//     * <p>
//     * -) GW must have received RELAY_REMMD_ACCEPTANCE
//     * -) GW must have received NON_DELIVERY
//     */
//    @Test
//    @Disabled("not decided yet if user interaction is needed!")
//    public void testReceiveMessageFromGw_backendDeliveryFailure(TestInfo testInfo) throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {
//
//        EbmsMessageId EBMS_ID = EbmsMessageId.ofString("EBMS_" + testInfo.getDisplayName());
//        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
//        String MSG_FOLDER = "msg2";
//
//        //syntetic error on deliver message to backend
////        Mockito.doThrow(new RuntimeException("error"))
////                .when(backendInterceptor)
////                .deliveryToBackend(Mockito.any(DC5Message.class));
//
//        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {
//
//            DC5Message testMessage = deliverMessageFromGw(MSG_FOLDER, EBMS_ID, CONNECTOR_MESSAGE_ID);
//
//            LOGGER.info("message with confirmations: [{}]", testMessage.getTransportedMessageConfirmations());
//
//            //wait for evidence messages delivered to gw
//            List<DC5Message> collect = Stream.of(toGwDeliveredMessages.take(), toGwDeliveredMessages.take()).collect(Collectors.toList());
//
//            assertThat(collect)
//                    .extracting(m -> m.getTransportedMessageConfirmations().get(0).getEvidenceType())
//                    .containsOnly(DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE, DomibusConnectorEvidenceType.NON_DELIVERY);
//
//            assertThat(toBackendDeliveredMessages).as("no messages should be delivered to backend").hasSize(0); //queue should be empty!
//        });
//    }
//
//
//    private DC5Message deliverMessageFromGw(String msgFolder, EbmsMessageId EBMS_ID, DomibusConnectorMessageId CONNECTOR_MESSAGE_ID) {
//        DC5Message testMessage = createTestMessage(msgFolder, EBMS_ID, CONNECTOR_MESSAGE_ID);
//        submitFromGatewayToController(testMessage);
//        return testMessage;
//    }
//
//    public static final String FINAL_RECIPIENT = "final_recipient";
//    public static final String ORIGINAL_SENDER = "original_sender";
//
    private DC5Message createTestMessage(String msgFolder, EbmsMessageId EBMS_ID, DomibusConnectorMessageId CONNECTOR_MESSAGE_ID) {
        try {
            DC5Message testMessage = LoadStoreMessageFromPath.loadMessageFrom(new ClassPathResource("/testmessages/" + msgFolder + "/"));
            assertThat(testMessage).isNotNull();
//            testMessage.setMessageLaneId(DomibusConnectorBusinessDomain.getDefaultMessageLaneId());
//            testMessage.getEbmsData().setFinalRecipient(FINAL_RECIPIENT);
//            testMessage.getEbmsData().setOriginalSender(ORIGINAL_SENDER);
            testMessage.getEbmsData().setEbmsMessageId(EBMS_ID);
//            testMessage.getEbmsData().setBackendMessageId(null);
            testMessage.setConnectorMessageId(CONNECTOR_MESSAGE_ID);
            return testMessage;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
//
//
//    /**
//     * Send message from Backend to GW
//     * <p>
//     * -) Backend must have received SUBMISSION_ACCEPTANCE
//     * -) GW must have received Business MSG with SUBMISSION_ACCEPTANCE and 2 attachments ASICS-S, tokenXml
//     */
//    @Test
//    public void sendMessageFromBackend(TestInfo testInfo) {
//        EbmsMessageId EBMS_ID = null;
//        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
//        BackendMessageId BACKEND_MESSAGE_ID = BackendMessageId.ofString("backend_" + testInfo.getDisplayName());
//        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {
//            DC5Message submittedMessage = submitMessage(EBMS_ID, CONNECTOR_MESSAGE_ID, BACKEND_MESSAGE_ID);
//
//
//            DC5Message take = toGwDeliveredMessages.take(); //wait until a message is put into queue
//
//            assertThat(take).as("Gw must RCV message").isNotNull();
//
//            assertThat(take.getTransportedMessageConfirmations()).as("submission acceptance evidence must be a part of message").hasSize(1); //SUBMISSION_ACCEPTANCE
//            assertThat(take.getTransportedMessageConfirmations().get(0).getEvidenceType())
//                    .as("evidence must be of type submission acceptance")
//                    .isEqualTo(SUBMISSION_ACCEPTANCE);
//
//            //ASIC-S + token XML
////            assertThat(take.getMessageAttachments()).hasSize(2);
////            assertThat(take.getMessageAttachments()).extracting(a -> a.getIdentifier()).containsOnly("ASIC-S", "tokenXML");
////            assertThat(take.getMessageContent().getXmlContent()).isNotNull(); //business XML
//
//
//            //check sent message in DB
////            DC5Message loadedMsg = messagePersistenceService.findMessageByConnectorMessageId(CONNECTOR_MESSAGE_ID);
////            assertThat(loadedMsg.getEbmsData().getEbmsMessageId()).isNotBlank();
//////            assertThat(loadedMsg.getTransportedMessageConfirmations()).hasSize(1);
////            assertThat(loadedMsg.getRelatedMessageConfirmations()).hasSize(1);
//
//
//            DC5Message toBackendEvidence = toBackendDeliveredMessages.take();
//            assertThat(toBackendEvidence).isNotNull();
//            DC5Ebms toBackendEvidenceMsgDetails = toBackendEvidence.getEbmsData();
//            assertThat(toBackendEvidence.getTransportedMessageConfirmations().get(0).getEvidenceType())
//                    .isEqualTo(SUBMISSION_ACCEPTANCE);
//            assertThat(toBackendEvidence.getTransportedMessageConfirmations().get(0).getEvidence())
//                    .as("Generated evidence must be longer than 100 bytes - make sure this way a evidence has been generated")
//                    .hasSizeGreaterThan(100);
//
////            assertThat(toBackendEvidenceMsgDetails.getDirection())
////                    .as("Direction must be set!")
////                    .isNotNull();
////            assertThat(toBackendEvidenceMsgDetails.getRefToBackendMessageId())
////                    .as("To backend back transported evidence message must use refToBackendMessageId to ref original backend msg id!")
////                    .isEqualTo(BACKEND_MESSAGE_ID);
////
////            assertThat(toBackendEvidenceMsgDetails.getFromParty())
////                    .as("Parties must be switched")
////                    .isEqualTo(DomibusConnectorPartyBuilder.createBuilder().copyPropertiesFrom(submittedMessage.getEbmsData().getToParty())
////                            .setRoleType(DomibusConnectorParty.PartyRoleType.INITIATOR)
////                            .build());
////            assertThat(toBackendEvidenceMsgDetails.getToParty())
////                    .as("Parties must be switched")
////                    .isEqualTo(DomibusConnectorPartyBuilder.createBuilder().copyPropertiesFrom(submittedMessage.getEbmsData().getFromParty())
////                            .setRoleType(DomibusConnectorParty.PartyRoleType.RESPONDER)
////                            .build());
//
//
//        });
//    }
//
//
//    /**
//     * Send message from Backend to GW with no BusinessContent (only business XML is provided!)
//     * <p>
//     * -) Backend must have received SUBMISSION_ACCEPTANCE
//     * -) GW must have received Business MSG with SUBMISSION_ACCEPTANCE and 2 attachments ASICS-S, tokenXml
//     */
//    @Test
//    public void sendMessageFromBackend_noBusinessDoc(TestInfo testInfo) {
//        String EBMS_ID = null;
//        String CONNECTOR_MESSAGE_ID = testInfo.getDisplayName();
//        String BACKEND_MESSAGE_ID = "n1";
//        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {
////            DomibusConnectorMessage submittedMessage = submitMessage(EBMS_ID, CONNECTOR_MESSAGE_ID, BACKEND_MESSAGE_ID);
//
////            DomibusConnectorMessageBuilder msgBuilder = DomibusConnectorMessageBuilder.createBuilder();
////            DC5Message msg = msgBuilder.setMessageContent(DomainEntityCreator.createMessageContentWithDocumentWithNoSignature())
////                    .setConnectorMessageId(CONNECTOR_MESSAGE_ID)
////                    .setMessageDetails(DomibusConnectorMessageDetailsBuilder
////                            .create()
////                            .withEbmsMessageId(EBMS_ID)
////                            .withAction("action1")
////                            .withService("service1", "servicetype")
////                            .withConversationId("conv1")
////                            .withBackendMessageId(BACKEND_MESSAGE_ID)
////                            .withFromParty(DomainEntityCreator.createPartyATasInitiator())
////                            .withToParty(DomainEntityCreator.createPartyDE())
////                            .withFinalRecipient("final")
////                            .withOriginalSender("original")
////                            .build()
////                    ).build();
////            msg.getMessageContent().setDocument(null);
//
////            DC5Message submittedMessage = msg;
////
////
////            submitFromBackendToController(msg);
//
//
//            DC5Message toGwSubmittedBusinessMessage = toGwDeliveredMessages.take(); //wait until a message is put into queue
//
//            assertThat(toGwSubmittedBusinessMessage).as("Gw must RCV message").isNotNull();
//
//            assertThat(toGwSubmittedBusinessMessage.getTransportedMessageConfirmations()).as("submission acceptance evidence must be a part of message").hasSize(1); //SUBMISSION_ACCEPTANCE
//            assertThat(toGwSubmittedBusinessMessage.getTransportedMessageConfirmations().get(0).getEvidenceType())
//                    .as("evidence must be of type submission acceptance")
//                    .isEqualTo(SUBMISSION_ACCEPTANCE);
//
//            //ASIC-S + token XML
////            assertThat(toGwSubmittedBusinessMessage.getMessageAttachments()).hasSize(2);
////            assertThat(toGwSubmittedBusinessMessage.getMessageAttachments()).extracting(a -> a.getIdentifier()).containsOnly("ASIC-S", "tokenXML");
////            assertThat(toGwSubmittedBusinessMessage.getMessageContent().getXmlContent()).isNotNull(); //business XML
//
////            assertThat(toGwSubmittedBusinessMessage.getEbmsData().getToParty())
////                    .as("Parties must be same")
////                    .isEqualToComparingOnlyGivenFields(submittedMessage.getEbmsData().getToParty(), "partyId", "partyIdType", "role");
////            assertThat(toGwSubmittedBusinessMessage.getEbmsData().getFromParty())
////                    .as("Parties must be same")
////                    .isEqualToComparingOnlyGivenFields(submittedMessage.getEbmsData().getFromParty(), "partyId", "partyIdType", "role");
////            assertThat(toGwSubmittedBusinessMessage.getEbmsData().getOriginalSender())
////                    .as("Original sender must be same")
////                    .isEqualTo(submittedMessage.getEbmsData().getOriginalSender());
////            assertThat(toGwSubmittedBusinessMessage.getEbmsData().getFinalRecipient())
////                    .as("Final Recipient must be same")
////                    .isEqualTo(submittedMessage.getEbmsData().getFinalRecipient());
//
//            //check sent message in DB
////            DC5Message loadedMsg = messagePersistenceService.findMessageByConnectorMessageId(CONNECTOR_MESSAGE_ID);
////            assertThat(loadedMsg.getEbmsData().getEbmsMessageId()).isNotBlank();
//
//
//            DC5Message toBackendEvidence = toBackendDeliveredMessages.take();
//            assertThat(toBackendEvidence).isNotNull();
//            DC5Ebms toBackendEvidenceMsgDetails = toBackendEvidence.getEbmsData();
//            assertThat(toBackendEvidence.getTransportedMessageConfirmations().get(0).getEvidenceType())
//                    .isEqualTo(SUBMISSION_ACCEPTANCE);
//            assertThat(toBackendEvidence.getTransportedMessageConfirmations().get(0).getEvidence())
//                    .as("Generated evidence must be longer than 100 bytes - make sure this way a evidence has been generated")
//                    .hasSizeGreaterThan(100);
//
////            assertThat(toBackendEvidenceMsgDetails.getDirection())
////                    .as("Direction must be set!")
////                    .isNotNull();
////            assertThat(toBackendEvidenceMsgDetails.getRefToBackendMessageId())
////                    .as("To backend back transported evidence message must use refToBackendMessageId to ref original backend msg id!")
////                    .isEqualTo(BACKEND_MESSAGE_ID);
////
////            assertThat(toBackendEvidenceMsgDetails.getFromParty())
////                    .as("Parties must be switched")
////                    .isEqualToComparingOnlyGivenFields(submittedMessage.getEbmsData().getToParty(), "partyId", "partyIdType", "role");
////            assertThat(toBackendEvidenceMsgDetails.getToParty())
////                    .as("Parties must be switched")
////                    .isEqualToComparingOnlyGivenFields(submittedMessage.getEbmsData().getFromParty(), "partyId", "partyIdType", "role");
//
//
//        });
//    }
//
//
//    /**
//     * Send message from Backend to GW and RCV evidences for the message
//     * <p>
//     * PRE
//     * -) Backend has received SUBMISSION_ACCEPTANCE
//     * -) GW must has received Business MSG with SUBMISSION_ACCEPTANCE and 2 attachments ASICS-S, tokenXml
//     * <p>
//     * DO:
//     * -) Generate evidence RELAY_REMMD_ACCEPTANCE
//     * <p>
//     * ASSERT:
//     * -) backend has received RELAY_REMMD_ACCEPTANCE
//     */
//    @Test
//    public void sendMessageFromBackend_rcvEvidences(TestInfo testInfo) {
//        EbmsMessageId EBMS_ID = null;
//        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
//        BackendMessageId BACKEND_MESSAGE_ID = BackendMessageId.ofString("backend_" + testInfo.getDisplayName());
//
//        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {
//            DC5Message DC5Message = submitMessage(EBMS_ID, CONNECTOR_MESSAGE_ID, BACKEND_MESSAGE_ID);
//
//            DC5Message take = toGwDeliveredMessages.take(); //wait until a message is put into queue
//            EbmsMessageId newEbmsId = take.getEbmsData().getEbmsMessageId();
//
//            DC5Message toBackendEvidence = toBackendDeliveredMessages.take();
//            assertThat(toBackendEvidence).isNotNull();
//
//            //DO
//            DC5Message relayRemmdAcceptanceEvidenceForMessage = DomainEntityCreator.createRelayRemmdAcceptanceEvidenceForMessage(DC5Message);
//            relayRemmdAcceptanceEvidenceForMessage.getEbmsData().setRefToEbmsMessageId(newEbmsId);
//            relayRemmdAcceptanceEvidenceForMessage.getEbmsData().setEbmsMessageId(EbmsMessageId.ofString(testInfo.getDisplayName() + "_remote_2"));
//            this.submitFromGatewayToController(relayRemmdAcceptanceEvidenceForMessage);
//
//            //ASSERT
//            DC5Message relayReemdEvidenceMsg = toBackendDeliveredMessages.take();
//            assertThat(relayReemdEvidenceMsg)
//                    .isNotNull();
////            assertThat(relayReemdEvidenceMsg.getEbmsData().getRefToBackendMessageId())
////                    .isEqualTo(BACKEND_MESSAGE_ID);
//
//
//        });
//    }
//
//    /**
//     * Send message from Backend to GW and RCV evidences for the message
//     * but first receive a negative confirmation and afterwards a positive confirmation
//     * <p>
//     * PRE
//     * -) Backend has received SUBMISSION_ACCEPTANCE
//     * -) GW must has received Business MSG with SUBMISSION_ACCEPTANCE and 2 attachments ASICS-S, tokenXml
//     * <p>
//     * DO:
//     * -) Generate evidence RELAY_REMMD_REJECTION
//     * -) Generate evidence DELIVERY
//     * <p>
//     * ASSERT:
//     * -) backend has received RELAY_REMMD_REJECTION
//     * -) backend has NOT received any more confirmations
//     * -) message state in connector is rejected!
//     */
//    @Test
//    public void sendMessageFromBackend_rcvEvidencesPosThenNegative(TestInfo testInfo) {
//        EbmsMessageId EBMS_ID = null;
//        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
//        BackendMessageId BACKEND_MESSAGE_ID = BackendMessageId.ofString("backend_" + testInfo.getDisplayName());
//        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {
//            DC5Message DC5Message = submitMessage(EBMS_ID, CONNECTOR_MESSAGE_ID, BACKEND_MESSAGE_ID);
//
//            DC5Message take = toGwDeliveredMessages.take(); //wait until a message is put into queue
//
//            EbmsMessageId newEbmsId = take.getEbmsData().getEbmsMessageId();
//            LOGGER.info("Message reached toGwDeliveredMessages Queue with id [{}], ebmsid [{}]", take.getConnectorMessageId(), newEbmsId);
//
//            DC5Message toBackendEvidence = toBackendDeliveredMessages.take();
//            assertThat(toBackendEvidence).isNotNull();
//
//            //DO
//            DC5Message relayRemmdAcceptanceEvidenceForMessage = DomainEntityCreator.createRelayRemmdAcceptanceEvidenceForMessage(DC5Message);
//            relayRemmdAcceptanceEvidenceForMessage.getEbmsData().setRefToEbmsMessageId(newEbmsId);
//            relayRemmdAcceptanceEvidenceForMessage.getEbmsData().setEbmsMessageId(EbmsMessageId.ofString(testInfo.getDisplayName() + "_remote_2"));
//            submitFromGatewayToController(relayRemmdAcceptanceEvidenceForMessage);
//
//            //ASSERT
//            DC5Message relayReemdEvidenceMsg = toBackendDeliveredMessages.take();
//            assertThat(relayReemdEvidenceMsg)
//                    .isNotNull();
//        });
//    }
//
//
//    /**
//     * Send message from Backend to GW and RCV evidences for the message
//     * <p>
//     * PRE
//     * -) Backend has received SUBMISSION_ACCEPTANCE
//     * -) GW must has received Business MSG with SUBMISSION_ACCEPTANCE and 2 attachments ASICS-S, tokenXml
//     * <p>
//     * DO:
//     * -) Generate evidence RELAY_REMMD_ACCEPTANCE
//     * -) Generate evidence DELIVERY_EVIDENCE
//     * -) Generate evidence RETRIEVAL
//     * <p>
//     * ASSERT:
//     * -) backend has received RELAY_REMMD_ACCEPTANCE, DELIVERY, RETRIEVAL
//     */
//    @Test
//    public void sendMessageFromBackend_rcvEvidenceRelayDeliveryRetrieval(TestInfo testInfo) {
//        EbmsMessageId EBMS_ID = null;
//        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
//        BackendMessageId BACKEND_MESSAGE_ID = BackendMessageId.ofString("backend_" + testInfo.getDisplayName());
//        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {
//            DC5Message DC5Message = submitMessage(EBMS_ID, CONNECTOR_MESSAGE_ID, BACKEND_MESSAGE_ID);
//
//            DC5Message take = toGwDeliveredMessages.take(); //wait until a message is put into queue
//            EbmsMessageId newEbmsId = take.getEbmsData().getEbmsMessageId();
//
//            DC5Message toBackendEvidence = toBackendDeliveredMessages.take();
//            assertThat(toBackendEvidence)
//                    .isNotNull()
//                    .extracting(DomainModelHelper::getEvidenceTypeOfEvidenceMessage).isEqualTo(SUBMISSION_ACCEPTANCE);
//
//            //DO
//            DC5Message relayRemmdAcceptanceEvidenceForMessage = DomainEntityCreator.createRelayRemmdAcceptanceEvidenceForMessage(DC5Message);
//            relayRemmdAcceptanceEvidenceForMessage.getEbmsData().setRefToEbmsMessageId(newEbmsId);
//            relayRemmdAcceptanceEvidenceForMessage.getEbmsData().setEbmsMessageId(EbmsMessageId.ofString(testInfo.getDisplayName() + "_remote_2"));
//            this.submitFromGatewayToController(relayRemmdAcceptanceEvidenceForMessage);
//
//            DC5Message deliveryEvidenceForMessage = DomainEntityCreator.creatEvidenceMsgForMessage(DC5Message,
//                    DomainEntityCreator.createMessageDeliveryConfirmation());
//            deliveryEvidenceForMessage.getEbmsData().setRefToEbmsMessageId(newEbmsId);
//            deliveryEvidenceForMessage.getEbmsData().setEbmsMessageId(EbmsMessageId.ofString(testInfo.getDisplayName() + "_remote_3"));
//            this.submitFromGatewayToController(deliveryEvidenceForMessage);
//
//            DC5Message retrievalEvidenceForMessage = DomainEntityCreator.creatEvidenceMsgForMessage(DC5Message,
//                    DomainEntityCreator.createRetrievalEvidenceMessage());
//            retrievalEvidenceForMessage.getEbmsData().setRefToEbmsMessageId(newEbmsId);
//            retrievalEvidenceForMessage.getEbmsData().setEbmsMessageId(EbmsMessageId.ofString(testInfo.getDisplayName() + "_remote_4"));
//            this.submitFromGatewayToController(retrievalEvidenceForMessage);
//
//            //ASSERT
//            DC5Message relayReemdEvidenceMsg = toBackendDeliveredMessages.take();
//            assertThat(relayReemdEvidenceMsg)
//                    .isNotNull()
//                    .extracting(DomainModelHelper::getEvidenceTypeOfEvidenceMessage).isEqualTo(RELAY_REMMD_ACCEPTANCE);
//
//
//            DC5Message deliveryEvidenceMsg = toBackendDeliveredMessages.take();
//            assertThat(deliveryEvidenceMsg)
//                    .isNotNull()
//                    .extracting(DomainModelHelper::getEvidenceTypeOfEvidenceMessage).isEqualTo(DELIVERY);
//
//            DC5Message retrievalEvidenceMsg = toBackendDeliveredMessages.take();
//            assertThat(retrievalEvidenceMsg)
//                    .isNotNull()
//                    .extracting(DomainModelHelper::getEvidenceTypeOfEvidenceMessage).isEqualTo(RETRIEVAL);
//
//
//            //ASSERT MSG State
////            DC5Message originalMessage = messagePersistenceService.findMessageByConnectorMessageId(CONNECTOR_MESSAGE_ID);
////            messagePersistenceService.checkMessageConfirmed(originalMessage);
//
//
//            assertThat(toBackendDeliveredMessages).isEmpty();
//
//        });
//    }
//
//
//    /**
//     * Send message from Backend to GW and RCV evidences for the message
//     * <p>
//     * PRE
//     * -) Backend has received SUBMISSION_ACCEPTANCE
//     * -) GW must has received Business MSG with SUBMISSION_ACCEPTANCE and 2 attachments ASICS-S, tokenXml
//     * <p>
//     * DO:
//     * -) Generate evidence RELAY_REMMD_ACCEPTANCE
//     * -) Generate evidence NON_DELIVERY_EVIDENCE
//     * -) Generate evidence RETRIEVAL
//     * <p>
//     * ASSERT:
//     * -) backend has received RELAY_REMMD_ACCEPTANCE, NON_DELIVERY
//     * -) backend has NOT received any RETRIEVAL evidence!
//     * <p>
//     * -) message is still in rejected state
//     */
//    @Test
//    @Disabled("fails when executed with other tests...")
//    public void sendMessageFromBackend_rcvEvidenceRelayNonDeliveryRetrieval(TestInfo testInfo) {
//        EbmsMessageId EBMS_ID = null;
//        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
//        BackendMessageId BACKEND_MESSAGE_ID = BackendMessageId.ofString("backend_" + testInfo.getDisplayName());
//        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {
//            DC5Message DC5Message = submitMessage(EBMS_ID, CONNECTOR_MESSAGE_ID, BACKEND_MESSAGE_ID);
//
//            DC5Message take = toGwDeliveredMessages.take(); //wait until a message is put into queue
//            EbmsMessageId newEbmsId = take.getEbmsData().getEbmsMessageId();
//
//            DC5Message toBackendEvidence = toBackendDeliveredMessages.take();
//
//            LOGGER.info("toBackendEvidence [{}], [{}]", toBackendEvidence.getConnectorMessageIdAsString(), toBackendEvidence.getEbmsData().getRefToEbmsMessageId());
//
//            assertThat(toBackendEvidence)
//                    .isNotNull()
//                    .as("First evidence for backend must be submission acceptance!")
//                    .extracting(DomainModelHelper::getEvidenceTypeOfEvidenceMessage).isEqualTo(SUBMISSION_ACCEPTANCE);
//
//            //DO
//            //deliver relay remmd acceptance
//            DC5Message relayRemmdAcceptanceEvidenceForMessage = DomainEntityCreator.createRelayRemmdAcceptanceEvidenceForMessage(DC5Message);
//            relayRemmdAcceptanceEvidenceForMessage.getEbmsData().setRefToEbmsMessageId(newEbmsId);
//            relayRemmdAcceptanceEvidenceForMessage.getEbmsData().setEbmsMessageId(EbmsMessageId.ofString(testInfo.getDisplayName() + "_r_2"));
//            this.submitFromGatewayToController(relayRemmdAcceptanceEvidenceForMessage);
//
//
//            DC5Message relayReemdEvidenceMsg = toBackendDeliveredMessages.take();
//            assertThat(relayReemdEvidenceMsg)
//                    .as("Backend must have RCV relayREMMD msg")
//                    .isNotNull()
//                    .extracting(DomainModelHelper::getEvidenceTypeOfEvidenceMessage).isEqualTo(RELAY_REMMD_ACCEPTANCE);
//
//
//            //deliver non delivery
//            DC5Message nonDeliveryEvidenceForMessage = DomainEntityCreator.creatEvidenceMsgForMessage(DC5Message,
//                    DomainEntityCreator.createMessageNonDeliveryConfirmation());
//            nonDeliveryEvidenceForMessage.getEbmsData().setRefToEbmsMessageId(newEbmsId);
//            nonDeliveryEvidenceForMessage.getEbmsData().setEbmsMessageId(EbmsMessageId.ofString(testInfo.getDisplayName() + "_r_3"));
//            this.submitFromGatewayToController(nonDeliveryEvidenceForMessage);
//
//
//            DC5Message deliveryEvidenceMsg = toBackendDeliveredMessages.take();
//            assertThat(deliveryEvidenceMsg)
//                    .as("Backend must have RCV delivery msg")
//                    .isNotNull()
//                    .extracting(DomainModelHelper::getEvidenceTypeOfEvidenceMessage).isEqualTo(NON_DELIVERY);
//
//            //deliver retrieval - must not reach the backend!
//            DC5Message retrievalEvidenceForMessage = DomainEntityCreator.creatEvidenceMsgForMessage(DC5Message,
//                    DomainEntityCreator.createRetrievalEvidenceMessage());
//            retrievalEvidenceForMessage.getEbmsData().setRefToEbmsMessageId(newEbmsId);
//            retrievalEvidenceForMessage.getEbmsData().setEbmsMessageId(EbmsMessageId.ofString(testInfo.getDisplayName() + "_r_4"));
//            this.submitFromGatewayToController(retrievalEvidenceForMessage);
//
//
//            //wait for any more messages for 5s
//            DC5Message retrieval = toBackendDeliveredMessages.poll(5, TimeUnit.SECONDS);
//            assertThat(retrieval)
//                    .as("No more msg should be transported to backend!")
//                    .isNull();
//
//
//            //ASSERT MSG State
////            DC5Message originalMessage = messagePersistenceService.findMessageByConnectorMessageId(CONNECTOR_MESSAGE_ID);
////            assertThat(messagePersistenceService.checkMessageRejected(originalMessage))
////                    .as("Message must be in rejected state!")
////                    .isTrue();
//
//            assertThat(toBackendDeliveredMessages)
//                    .as("There should be no retrieval message transported to the backend!")
//                    .isEmpty();
//
//
//        });
//    }
//
//    /**
//     * Send message from Backend to GW
//     * <p>
//     * -) Backend must have received SUBMISSION_REJECTION
//     * -) GW receives nothing
//     */
//    @Test
//    @Disabled("Todo repair test")
//    public void sendMessageFromBackend_submitToGwFails(TestInfo testInfo) throws DomibusConnectorGatewaySubmissionException {
//
//        //syntetic error on submitting message...
////        Mockito.doThrow(new DomibusConnectorGatewaySubmissionException("error"))
////                .when(domibusConnectorGatewaySubmissionServiceInterceptor)
////                .submitToGateway(Mockito.any(DC5Message.class));
//
//        EbmsMessageId EBMS_ID = null;
//        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
//        BackendMessageId BACKEND_MESSAGE_ID = BackendMessageId.ofString("backend_" + testInfo.getDisplayName());
//        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {
//            submitMessage(EBMS_ID, CONNECTOR_MESSAGE_ID, BACKEND_MESSAGE_ID);
//
//            DC5Message toBackendEvidence = toBackendDeliveredMessages.take();
//            assertThat(toBackendEvidence)
//                    .isNotNull()
//                    .as("Backend must RCV a Submission Rejection")
//                    .extracting(DomainModelHelper::getEvidenceTypeOfEvidenceMessage).isEqualTo(SUBMISSION_REJECTION);
//
//
//            //check attribute refToMessageId
////            assertThat(toBackendEvidence.getEbmsData().getRefToBackendMessageId())
////                    .as("backendRefToMessageId must match backend message id")
////                    .isEqualTo(BACKEND_MESSAGE_ID);
//
//            //parties, services, action..
//
//
//        });
//    }
//
//
//    /**
//     * Send message from Backend to GW and test if relayRemmd timeout works
//     * <p>
//     * -) Backend must have received SUBMISSION_ACCEPTANCE
//     * -) GW must have received Business MSG with SUBMISSION_ACCEPTANCE and 2 attachments ASICS-S, tokenXml
//     * <p>
//     * -) EvidenceTimoutProcessor is started
//     * <p>
//     * -) Backend must have RCV RelayRemmdFailure due timeout (set to 1s for test)
//     */
//    @Test
//    @Disabled
//    public void sendMessageFromBackend_timeoutRelayRemmd(TestInfo testInfo) {
//        EbmsMessageId EBMS_ID = null;
//        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
//        BackendMessageId BACKEND_MESSAGE_ID = BackendMessageId.ofString("backend_" + testInfo.getDisplayName());
//        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {
//            DC5Message submittedMessage = submitMessage(EBMS_ID, CONNECTOR_MESSAGE_ID, BACKEND_MESSAGE_ID);
//
//            DC5Message toGw = toGwDeliveredMessages.take(); //wait until a message is put into queue
//            DC5Message toBackend = toBackendDeliveredMessages.take(); //take backtraveling submission_acceptance
//
//            Thread.sleep(2000); //sleep 2s to make sure relay remmd timeout is reached...
//
//            checkEvidencesTimeoutProcessor.checkEvidencesTimeout();
//
//            DC5Message toBackendRelayRemmdFailure = toBackendDeliveredMessages.take(); //should be relayRemmdFailure
//            assertThat(toBackendRelayRemmdFailure.getTransportedMessageConfirmations().get(0).getEvidenceType())
//                    .isEqualTo(RELAY_REMMD_FAILURE);
//
//            assertThat(toBackendRelayRemmdFailure).isNotNull();
//            DC5Ebms toBackendEvidenceMsgDetails = toBackendRelayRemmdFailure.getEbmsData();
//
//            assertThat(toBackendRelayRemmdFailure.getTransportedMessageConfirmations().get(0).getEvidence())
//                    .as("Generated evidence must be longer than 100 bytes - make sure this way a evidence has been generated")
//                    .hasSizeGreaterThan(100);
//
////            assertThat(toBackendEvidenceMsgDetails.getDirection())
////                    .as("Direction must be set!")
////                            .isNotNull();
////            assertThat(toBackendEvidenceMsgDetails.getRefToBackendMessageId())
////                    .as("To backend back transported evidence message must use refToBackendMessageId to ref original backend msg id!")
////                            .isEqualTo(BACKEND_MESSAGE_ID);
////
////            assertThat(toBackendEvidenceMsgDetails.getToParty())
////                    .as("Parties must be switched")
////                    .isEqualToComparingOnlyGivenFields(submittedMessage.getEbmsData().getFromParty(), "partyId", "partyIdType", "role");
////            assertThat(toBackendEvidenceMsgDetails.getFromParty())
////                    .as("Parties must be switched")
////                    .isEqualToComparingOnlyGivenFields(submittedMessage.getEbmsData().getToParty(), "partyId", "partyIdType", "role");
////            assertThat(toBackendEvidenceMsgDetails.getOriginalSender())
////                    .as("OriginalSender final recipient must be switched")
////                    .isEqualTo(submittedMessage.getEbmsData().getFinalRecipient());
////            assertThat(toBackendEvidenceMsgDetails.getFinalRecipient())
////                    .as("OriginalSender final recipient must be switched")
////                    .isEqualTo(submittedMessage.getEbmsData().getOriginalSender());
//
//
//            //check msg is rejected in DB
////            DC5Message businessMessage = messagePersistenceService.findMessageByConnectorMessageId(CONNECTOR_MESSAGE_ID);
////            assertThat(messagePersistenceService.checkMessageRejected(businessMessage)).isTrue();
//
//        });
//    }
//
//
//    private DC5Message submitMessage(EbmsMessageId ebmsId, DomibusConnectorMessageId connectorMessageId, BackendMessageId backendMessageId) {
//        DomibusConnectorMessageBuilder msgBuilder = DomibusConnectorMessageBuilder.createBuilder();
////        DC5Message msg = msgBuilder.setMessageContent(DomainEntityCreator.createMessageContentWithDocumentWithNoSignature())
////                .setConnectorMessageId(connectorMessageId)
////                .setMessageLaneId(DomibusConnectorBusinessDomain.getDefaultMessageLaneId())
////                .setMessageDetails(DomibusConnectorMessageDetailsBuilder
////                        .create()
////                        .withEbmsMessageId(ebmsId)
////                        .withAction("action1")
////                        .withService("service1", "servicetype")
////                        .withConversationId("conv1")
////                        .withBackendMessageId(backendMessageId)
////                        .withFromParty(DomainEntityCreator.createPartyATasInitiator())
////                        .withToParty(DomainEntityCreator.createPartyDE())
////                        .withFinalRecipient("final")
////                        .withOriginalSender("original")
////                        .build()
////                ).build();
//
////        submitFromBackendToController(msg);
//        LOGGER.info("Message with id [{}] submitted", connectorMessageId);
////        return msg;
//        return null;
//    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void submitFromBackendToController(DC5Message message) {
        if (message.getConnectorMessageId() == null) {
            message.setConnectorMessageId(messageIdGenerator.generateDomibusConnectorMessageId());
        }
        if (message.getMessageLaneId() == null || StringUtils.isEmpty(message.getMessageLaneId().getBusinessDomainId())) {
            message.setMessageLaneId(DomibusConnectorBusinessDomain.getDefaultBusinessDomainId());
        }
        DomibusConnectorLinkPartner testLink = new DomibusConnectorLinkPartner();
        testLink.setLinkPartnerName(new DomibusConnectorLinkPartner.LinkPartnerName("test_backend"));
        testLink.setLinkType(LinkType.BACKEND);
//        submitToConnector.submitToConnector(message, testLink);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void submitFromGatewayToController(DC5Message message) {
        if (message.getConnectorMessageId() == null) {
            message.setConnectorMessageId(messageIdGenerator.generateDomibusConnectorMessageId());
        }
        if (message.getMessageLaneId() == null || StringUtils.isEmpty(message.getMessageLaneId().getBusinessDomainId())) {
            message.setMessageLaneId(DomibusConnectorBusinessDomain.getDefaultBusinessDomainId());
        }
        DomibusConnectorLinkPartner testLink = new DomibusConnectorLinkPartner();
        testLink.setLinkPartnerName(new DomibusConnectorLinkPartner.LinkPartnerName("test_gw"));
        testLink.setLinkType(LinkType.GATEWAY);
        submitToConnector.submitToConnector(message, testLink);
    }
}
