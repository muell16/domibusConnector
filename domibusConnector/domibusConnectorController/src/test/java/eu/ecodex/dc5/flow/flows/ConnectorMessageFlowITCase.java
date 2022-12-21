
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
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.service.LargeFilePersistenceService;
import eu.domibus.connector.persistence.testutils.LargeFileMemoryProviderConfiguration;
import eu.ecodex.dc5.flow.events.MessageTransportEvent;
import eu.ecodex.dc5.message.model.*;
import eu.ecodex.dc5.message.repo.DC5MessageRepo;
import eu.ecodex.dc5.transport.model.DC5TransportRequest;
import eu.ecodex.dc5.transport.repo.DC5TransportRequestRepo;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

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

@AutoConfigureTestDatabase
@SpringBootTest(classes = {eu.ecodex.dc5.DC5FlowModule.class,
        ConnectorMessageFlowITCase.MySubmitToLink.class,
        LargeFileMemoryProviderConfiguration.class})
@ActiveProfiles({"flow-test", "test"})

@Log4j2
public class ConnectorMessageFlowITCase {


    @Autowired
    private SubmitToConnector submitToConnector;
    private TransactionTemplate txTemplate;

    @Autowired
    private DC5MessageRepo messageRepo;

    @Autowired
    private LargeFilePersistenceService largeFilePersistenceService;

    @Data
    public static class AddToQueueEvent {
        DC5TransportRequest.TransportRequestId id;
        MessageTargetSource target;
    }

    @Primary
    @Component
    @Getter
    @Log4j2
    @RequiredArgsConstructor
    public static class MySubmitToLink implements SubmitToLinkService {


        private final DC5MessageRepo messageRepo;
        private final DC5TransportRequestRepo transportRequestRepo;
        private final ApplicationEventPublisher eventPublisher;

        @TransactionalEventListener
        public void handleAddToQueueEvent(AddToQueueEvent event) {

            try {
                if (event.getTarget() == MessageTargetSource.GATEWAY) {
                    toGwDeliveredMessages.put(event.getId());
                } else if (event.getTarget() == MessageTargetSource.BACKEND) {
                    toBackendDeliveredMessages.put(event.getId());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void submitToLink(SubmitToLinkEvent event) throws DomibusConnectorSubmitToLinkException {
            //use events to add to queue AFTER TX completed
            AddToQueueEvent e = new AddToQueueEvent();
            e.setId(event.getTransportRequestId());
            e.setTarget(event.getTarget());
            eventPublisher.publishEvent(e);
        }

        @SneakyThrows
        public DC5Message takeToGwMessage() {
            DC5TransportRequest.TransportRequestId take = toGwDeliveredMessages.take();
            DC5TransportRequest transportRequest = transportRequestRepo.findByTransportRequestId(take).get();
            return transportRequest.getMessage();
        }

        @SneakyThrows
        public DC5Message takeToBackendMessage() {
            DC5TransportRequest.TransportRequestId take = toBackendDeliveredMessages.take();
            DC5TransportRequest transportRequest = transportRequestRepo.findByTransportRequestId(take).get();
            return transportRequest.getMessage();
        }

        @SneakyThrows
        public DC5TransportRequest takeToGwTransport() {
            DC5TransportRequest.TransportRequestId take = toGwDeliveredMessages.take();
            DC5TransportRequest transportRequest = transportRequestRepo.findByTransportRequestId(take).get();
            return transportRequest;
        }

        @SneakyThrows
        public DC5TransportRequest takeToBackendTransport() {
            DC5TransportRequest.TransportRequestId take = toBackendDeliveredMessages.take();
            DC5TransportRequest transportRequest = transportRequestRepo.findByTransportRequestId(take).get();
            return transportRequest;
        }

        BlockingQueue<DC5TransportRequest.TransportRequestId> toGwDeliveredMessages = new ArrayBlockingQueue<>(50);

        BlockingQueue<DC5TransportRequest.TransportRequestId> toBackendDeliveredMessages = new ArrayBlockingQueue<>(50);

        public void clear() {
            toBackendDeliveredMessages.clear();
            toGwDeliveredMessages.clear();
        }


        public void acceptTransport(DC5TransportRequest transportRequest, TransportState transportState, BackendMessageId backendMessageId) {
            acceptTransport(transportRequest, transportState, backendMessageId.getBackendMessageId());
        }

        public void acceptTransport(DC5TransportRequest transportRequest, TransportState transportState, EbmsMessageId backendMessageId) {
            acceptTransport(transportRequest, transportState, backendMessageId.getEbmsMesssageId());
        }

        private void acceptTransport(DC5TransportRequest transportRequest, TransportState transportState, String remoteMessageId) {
            MessageTransportEvent messageTransportEvent = MessageTransportEvent.builder()
                    .transportId(transportRequest.getTransportRequestId())
                    .state(transportState)
                    .linkName(transportRequest.getLinkName())
                    .remoteTransportId(remoteMessageId)
                    .build();
            eventPublisher.publishEvent(messageTransportEvent);
        }

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectorMessageFlowITCase.class);

    public static final Duration TEST_TIMEOUT = Duration.ofSeconds(120);

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

    @Autowired
    DC5TransportRequestRepo transportRequestRepo;


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
            submitFromGatewayToController(testMessage);

            LOGGER.info("message with confirmations: [{}]", testMessage.getTransportedMessageConfirmations());

            txTemplate.executeWithoutResult((state) -> {
                DC5Message toBackendDelivered = mySubmitToLink.takeToBackendMessage(); //wait until a message is put into queue
                assertThat(mySubmitToLink.toBackendDeliveredMessages).hasSize(0); //queue should be empty!
                assertThat(toBackendDelivered).isNotNull();
                assertThat(toBackendDelivered.getBackendLinkName())
                        .as("service2 should delivered to backend2")
                        .isEqualTo(DomibusConnectorLinkPartner.LinkPartnerName.of("backend2"));
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

                assertThat(testMessage.getEbmsData().getBackendAddress())
                        .as("Parties must be switched")
                        .usingRecursiveComparison().ignoringFields("role")
                        .isEqualTo(relayRemmdEvidenceMsgDetails.getBackendAddress());

            });

            //message status confirmed
            txTemplate.executeWithoutResult((state) -> {
                DC5Message persistedMessage = messageRepo.getByConnectorMessageId(CONNECTOR_MESSAGE_ID);
                assertThat(persistedMessage.getMessageContent().getCurrentState().getState())
                        .as("Message must be in state confirmed!")
                        .isEqualTo(DC5BusinessMessageState.BusinessMessagesStates.RELAYED);
                assertThat(persistedMessage.getMessageLaneId())
                        .as("Should assigned to default domain!")
                        .isEqualTo(DomibusConnectorBusinessDomain.getDefaultBusinessDomainId());
            });


        });
    }

    /**
     * RCV message from GW
     * <p>
     * -) Backend must have received MSG
     * -) GW must have received RELAY_REMMD_ACCEPTANCE
     * <p>
     * -) test responds with DELIVERY Trigger
     * <p>
     * -) GW must have received DELIVERY_EVIDENCE
     * <p>
     * -) Backend must have rcv DELIVERY_EVIDENCE
     */
    @Test
    public void testReceiveMessageFromGw_respondWithDelivery(TestInfo testInfo) throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {

        EbmsMessageId EBMS_ID = EbmsMessageId.ofString("EBMS_" + testInfo.getDisplayName());
        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
        BackendMessageId deliveryTriggerBackendId = BackendMessageId.ofString("BACKEND_delivery_trigger_" + testInfo.getDisplayName());
        String MSG_FOLDER = "msg2";

        final BackendMessageId businessMsgBackendMsgId = BackendMessageId.ofString("BACKEND_businessmessageid_" + testInfo.getDisplayName());


        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {

            DC5Message testMessage = createTestMessage(MSG_FOLDER, EBMS_ID, CONNECTOR_MESSAGE_ID);
            submitFromGatewayToController(testMessage);

            Assertions.assertTimeout(Duration.ofSeconds(30), () -> {
                final DC5TransportRequest transportRequest = mySubmitToLink.takeToBackendTransport();//wait until something transported to Backend
                txTemplate.executeWithoutResult((state) -> {
                    DC5TransportRequest byTransportRequestId = transportRequestRepo.getById(transportRequest.getTransportRequestId());
                    DC5Message m = byTransportRequestId.getMessage();
                    assertThat(m.getMessageLaneId()).isNotNull();
                });

                mySubmitToLink.acceptTransport(transportRequest, TransportState.ACCEPTED, businessMsgBackendMsgId);    //Accept transport to backend and assign backend messageid
            }, "Should not take so long to rcv incoming business msg and ack it!");


            // confirm message to gw RELAY_REMMD_ACCEPTANCE
            EbmsMessageId ebmsId = EbmsMessageId.ofRandom();
            DC5TransportRequest toGW = mySubmitToLink.takeToGwTransport();//wait until something transported to GW
            mySubmitToLink.acceptTransport(toGW, TransportState.ACCEPTED, ebmsId);


            //create confirmation trigger message...
            DC5Message deliveryTriggerMessage = DC5Message.builder()
                    .transportedMessageConfirmation(DC5Confirmation.builder()
                            .evidenceType(DELIVERY)
                            .build()
                    )
//                    .refToConnectorMessageId(conId)
                    .backendData(DC5BackendData.builder()
                            .backendMessageId(deliveryTriggerBackendId)
                            .refToBackendMessageId(businessMsgBackendMsgId)
                            .build())
                    .build();
            submitFromBackendToController(deliveryTriggerMessage);

            //wait for Confirmation Message RCV on GW
            EbmsMessageId ebmsId2 = EbmsMessageId.ofRandom();
            DC5TransportRequest toGW2 = mySubmitToLink.takeToGwTransport();//wait until something transported to GW
            mySubmitToLink.acceptTransport(toGW2, TransportState.ACCEPTED, ebmsId2);
            txTemplate.executeWithoutResult((state) -> {
                DC5Message msg = transportRequestRepo.getById(toGW2.getTransportRequestId()).getMessage();
                assertThat(msg.isConfirmationMessage()).as("must be a confirmation message").isTrue();
            });


            //wait for Confirmation Message RCV on backend
            BackendMessageId backendId2 = BackendMessageId.ofRandom();
            DC5TransportRequest toBackend2 = mySubmitToLink.takeToBackendTransport();//wait until something transported to Backend
            mySubmitToLink.acceptTransport(toBackend2, TransportState.ACCEPTED, backendId2);
            txTemplate.executeWithoutResult((state) -> {
                DC5Message msg = transportRequestRepo.getById(toBackend2.getTransportRequestId()).getMessage();
                assertThat(msg.isConfirmationMessage()).as("must be a confirmation message").isTrue();
            });


            //message status delivered
            txTemplate.executeWithoutResult((state) -> {
                DC5Message persistedMessage = messageRepo.getByConnectorMessageId(CONNECTOR_MESSAGE_ID);
                assertThat(persistedMessage.getMessageContent().getCurrentState().getState())
                        .as("Message must be in state delivered!")
                        .isEqualTo(DC5BusinessMessageState.BusinessMessagesStates.DELIVERED);
                assertThat(persistedMessage.getMessageLaneId())
                        .as("Should assigned to default domain!")
                        .isEqualTo(DomibusConnectorBusinessDomain.getDefaultBusinessDomainId());
            });

        });
    }


    /**
     * RCV message from GW
     * <p>
     * -) Backend must have received MSG
     * -) GW must have received RELAY_REMMD_ACCEPTANCE
     * <p>
     * -) test responds with DELIVERY Trigger
     * -) test responds with a 2nd DELIVERY Trigger
     * <p>
     * -) GW must have received only one DELIVERY_EVIDENCE
     * <p>
     * -) Backend must have rcv only one DELIVERY_EVIDENCE
     */
    @Test
    public void testReceiveMessageFromGw_triggerDeliveryTwice_shouldOnlyRcvOne(TestInfo testInfo) throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {
        EbmsMessageId EBMS_ID = EbmsMessageId.ofString("EBMS_" + testInfo.getDisplayName());
        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
        BackendMessageId deliveryTriggerBackendId = BackendMessageId.ofString("BACKEND_delivery_trigger_" + testInfo.getDisplayName());
        String MSG_FOLDER = "msg2";

        final BackendMessageId businessMsgBackendMsgId = BackendMessageId.ofString("BACKEND_businessmessageid_" + testInfo.getDisplayName());

        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {

            DC5Message testMessage = createTestMessage(MSG_FOLDER, EBMS_ID, CONNECTOR_MESSAGE_ID);
            submitFromGatewayToController(testMessage);

            Assertions.assertTimeout(Duration.ofSeconds(30), () -> {
                final DC5TransportRequest transportRequest = mySubmitToLink.takeToBackendTransport();//wait until something transported to Backend
                txTemplate.executeWithoutResult((state) -> {
                    DC5TransportRequest byTransportRequestId = transportRequestRepo.getById(transportRequest.getTransportRequestId());
                    DC5Message m = byTransportRequestId.getMessage();
                    assertThat(m.getMessageLaneId()).isNotNull();
                });

                mySubmitToLink.acceptTransport(transportRequest, TransportState.ACCEPTED, businessMsgBackendMsgId);    //Accept transport to backend and assign backend messageid
            }, "Should not take so long to rcv incoming business msg and ack it!");


            // confirm message to gw RELAY_REMMD_ACCEPTANCE
            EbmsMessageId ebmsId = EbmsMessageId.ofRandom();
            DC5TransportRequest toGW = mySubmitToLink.takeToGwTransport();//wait until something transported to GW
            mySubmitToLink.acceptTransport(toGW, TransportState.ACCEPTED, ebmsId);


            //create confirmation trigger message...
            DC5Message deliveryTriggerMessage = DC5Message.builder()
                    .transportedMessageConfirmation(DC5Confirmation.builder()
                            .evidenceType(DELIVERY)
                            .build()
                    )
                    .backendData(DC5BackendData.builder()
                            .backendMessageId(deliveryTriggerBackendId)
                            .refToBackendMessageId(businessMsgBackendMsgId)
                            .build())
                    .build();
            submitFromBackendToController(deliveryTriggerMessage);

            //wait for Confirmation Message RCV on GW
            EbmsMessageId ebmsId2 = EbmsMessageId.ofRandom();
            DC5TransportRequest toGW2 = mySubmitToLink.takeToGwTransport(); //wait until something transported to GW
            mySubmitToLink.acceptTransport(toGW2, TransportState.ACCEPTED, ebmsId2);

            //wait for Confirmation Message RCV on backend
            BackendMessageId backendId2 = BackendMessageId.ofRandom();
            DC5TransportRequest toBackend2 = mySubmitToLink.takeToBackendTransport(); //wait until something transported to Backend
            mySubmitToLink.acceptTransport(toBackend2, TransportState.ACCEPTED, backendId2);


            //send 2nd trigger
            DC5Message deliveryTriggerMessage2 = DC5Message.builder()
                    .transportedMessageConfirmation(DC5Confirmation.builder()
                            .evidenceType(DELIVERY)
                            .build()
                    )
                    .backendData(DC5BackendData.builder()
                            .backendMessageId(deliveryTriggerBackendId)
                            .refToBackendMessageId(businessMsgBackendMsgId)
                            .build())
                    .build();
            submitFromBackendToController(deliveryTriggerMessage2);

            mySubmitToLink.toGwDeliveredMessages.poll(10, TimeUnit.SECONDS); //wait 10 seconds
            assertThat(mySubmitToLink.toGwDeliveredMessages).isEmpty();

            mySubmitToLink.toBackendDeliveredMessages.poll(2, TimeUnit.SECONDS); //wait 2 seconds
            assertThat(mySubmitToLink.toBackendDeliveredMessages).isEmpty();

        });

    }


    /**
     * RCV message from GW
     * <p>
     * -) Backend must have received MSG
     * -) GW must have received RELAY_REMMD_ACCEPTANCE
     * <p>
     * -) test responds with DELIVERY Trigger
     * <p>
     * -) GW must have received DELIVERY
     * <p>
     * -) Message must be in DELIVERED state
     * <p>
     * -) test responds with NON_RETRIEVAL
     * -) GW must have rcv NON_RETRIEVAL
     * -) Backend must have rcv NON_RETRIEVAL
     * <p>
     * -) Message must be in REJECTED state
     */
    @Test
    public void testReceiveMessageFromGw_respondWithDeliveryThenWithNonRetrieval(TestInfo testInfo) {
        EbmsMessageId EBMS_ID = EbmsMessageId.ofString("EBMS_" + testInfo.getDisplayName());
        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
        BackendMessageId deliveryTriggerBackendId = BackendMessageId.ofString("BACKEND_delivery_trigger_" + testInfo.getDisplayName());
        String MSG_FOLDER = "msg2";

        final BackendMessageId businessMsgBackendMsgId = BackendMessageId.ofString("BACKEND_businessmessageid_" + testInfo.getDisplayName());

        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {

            DC5Message testMessage = createTestMessage(MSG_FOLDER, EBMS_ID, CONNECTOR_MESSAGE_ID);
            submitFromGatewayToController(testMessage);

            Assertions.assertTimeout(Duration.ofSeconds(30), () -> {
                final DC5TransportRequest transportRequest = mySubmitToLink.takeToBackendTransport();//wait until something transported to Backend
                txTemplate.executeWithoutResult((state) -> {
                    DC5TransportRequest byTransportRequestId = transportRequestRepo.getById(transportRequest.getTransportRequestId());
                    DC5Message m = byTransportRequestId.getMessage();
                    assertThat(m.getMessageLaneId()).isNotNull();
                });

                mySubmitToLink.acceptTransport(transportRequest, TransportState.ACCEPTED, businessMsgBackendMsgId);    //Accept transport to backend and assign backend messageid
            }, "Should not take so long to rcv incoming business msg and ack it!");


            // confirm message to gw RELAY_REMMD_ACCEPTANCE
            EbmsMessageId ebmsId = EbmsMessageId.ofRandom();
            DC5TransportRequest toGW = mySubmitToLink.takeToGwTransport();//wait until something transported to GW
            mySubmitToLink.acceptTransport(toGW, TransportState.ACCEPTED, ebmsId);


            //create confirmation trigger message...
            DC5Message deliveryTriggerMessage = DC5Message.builder()
                    .transportedMessageConfirmation(DC5Confirmation.builder()
                            .evidenceType(DELIVERY)
                            .build()
                    )
                    .backendData(DC5BackendData.builder()
                            .backendMessageId(deliveryTriggerBackendId)
                            .refToBackendMessageId(businessMsgBackendMsgId)
                            .build())
                    .build();
            submitFromBackendToController(deliveryTriggerMessage);

            //wait for Confirmation Message RCV on GW
            EbmsMessageId ebmsId2 = EbmsMessageId.ofRandom();
            DC5TransportRequest toGW2 = mySubmitToLink.takeToGwTransport(); //wait until something transported to GW
            mySubmitToLink.acceptTransport(toGW2, TransportState.ACCEPTED, ebmsId2);

            //wait for Confirmation Message RCV on backend
            BackendMessageId backendId2 = BackendMessageId.ofRandom();
            DC5TransportRequest toBackend2 = mySubmitToLink.takeToBackendTransport(); //wait until something transported to Backend
            mySubmitToLink.acceptTransport(toBackend2, TransportState.ACCEPTED, backendId2);

            //message status delivered
            txTemplate.executeWithoutResult((state) -> {
                DC5Message persistedMessage = messageRepo.getByConnectorMessageId(CONNECTOR_MESSAGE_ID);
                assertThat(persistedMessage.getMessageContent().getCurrentState().getState())
                        .as("Message must be in state delivered!")
                        .isEqualTo(DC5BusinessMessageState.BusinessMessagesStates.DELIVERED);
                assertThat(persistedMessage.getMessageLaneId())
                        .as("Should assigned to default domain!")
                        .isEqualTo(DomibusConnectorBusinessDomain.getDefaultBusinessDomainId());
            });


            //send 2nd trigger
            DC5Message deliveryTriggerMessage2 = DC5Message.builder()
                    .transportedMessageConfirmation(DC5Confirmation.builder()
                            .evidenceType(NON_RETRIEVAL)
                            .build()
                    )
                    .backendData(DC5BackendData.builder()
                            .backendMessageId(deliveryTriggerBackendId)
                            .refToBackendMessageId(businessMsgBackendMsgId)
                            .build())
                    .build();
            submitFromBackendToController(deliveryTriggerMessage2);
        });

        //wait for Confirmation Message RCV on GW
        EbmsMessageId ebmsId3 = EbmsMessageId.ofRandom();
        DC5TransportRequest toGW3 = mySubmitToLink.takeToGwTransport(); //wait until something transported to GW
        mySubmitToLink.acceptTransport(toGW3, TransportState.ACCEPTED, ebmsId3);

        //wait for Confirmation Message RCV on backend
        BackendMessageId backendId3 = BackendMessageId.ofRandom();
        DC5TransportRequest toBackend3 = mySubmitToLink.takeToBackendTransport(); //wait until something transported to Backend
        mySubmitToLink.acceptTransport(toBackend3, TransportState.ACCEPTED, backendId3);

        //message status rejected
        txTemplate.executeWithoutResult((state) -> {
            DC5Message persistedMessage = messageRepo.getByConnectorMessageId(CONNECTOR_MESSAGE_ID);
            assertThat(persistedMessage.getMessageContent().getCurrentState().getState())
                    .as("Message must be in state rejected!")
                    .isEqualTo(DC5BusinessMessageState.BusinessMessagesStates.REJECTED);
            assertThat(persistedMessage.getMessageLaneId())
                    .as("Should assigned to default domain!")
                    .isEqualTo(DomibusConnectorBusinessDomain.getDefaultBusinessDomainId());
        });

    }


    /**
     * RCV message from GW
     * <p>
     * -) Backend must have received MSG
     * -) GW must have received RELAY_REMMD_ACCEPTANCE
     * <p>
     * -) test responds with NON_DELIVERY Trigger
     * <p>
     * -) GW must have received NON_DELIVERY_EVIDENCE
     * <p>
     * -) Message must be in rejected state
     */
    @Test
    public void testReceiveMessageFromGw_respondWithNonDelivery(TestInfo testInfo) throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {

        EbmsMessageId EBMS_ID = EbmsMessageId.ofString("EBMS_" + testInfo.getDisplayName());
        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
        BackendMessageId deliveryTriggerBackendId = BackendMessageId.ofString("BACKEND_delivery_trigger_" + testInfo.getDisplayName());
        String MSG_FOLDER = "msg2";

        final BackendMessageId businessMsgBackendMsgId = BackendMessageId.ofString("BACKEND_businessmessageid_" + testInfo.getDisplayName());

        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {

            DC5Message testMessage = createTestMessage(MSG_FOLDER, EBMS_ID, CONNECTOR_MESSAGE_ID);
            submitFromGatewayToController(testMessage);

            Assertions.assertTimeout(Duration.ofSeconds(30), () -> {
                final DC5TransportRequest transportRequest = mySubmitToLink.takeToBackendTransport();//wait until something transported to Backend
                txTemplate.executeWithoutResult((state) -> {
                    DC5TransportRequest byTransportRequestId = transportRequestRepo.getById(transportRequest.getTransportRequestId());
                    DC5Message m = byTransportRequestId.getMessage();
                    assertThat(m.getMessageLaneId()).isNotNull();
                });

                mySubmitToLink.acceptTransport(transportRequest, TransportState.ACCEPTED, businessMsgBackendMsgId);    //Accept transport to backend and assign backend messageid
            }, "Should not take so long to rcv incoming business msg and ack it!");


            // confirm message to gw RELAY_REMMD_ACCEPTANCE
            EbmsMessageId ebmsId = EbmsMessageId.ofRandom();
            DC5TransportRequest toGW = mySubmitToLink.takeToGwTransport();//wait until something transported to GW
            mySubmitToLink.acceptTransport(toGW, TransportState.ACCEPTED, ebmsId);


            //create confirmation trigger message...
            DC5Message deliveryTriggerMessage = DC5Message.builder()
                    .transportedMessageConfirmation(DC5Confirmation.builder()
                            .evidenceType(NON_DELIVERY)
                            .build()
                    )
                    .backendData(DC5BackendData.builder()
                            .backendMessageId(deliveryTriggerBackendId)
                            .refToBackendMessageId(businessMsgBackendMsgId)
                            .build())
                    .build();
            submitFromBackendToController(deliveryTriggerMessage);

            //wait for Confirmation Message RCV on GW
            EbmsMessageId ebmsId2 = EbmsMessageId.ofRandom();
            DC5TransportRequest toGW2 = mySubmitToLink.takeToGwTransport(); //wait until something transported to GW
            mySubmitToLink.acceptTransport(toGW2, TransportState.ACCEPTED, ebmsId2);

            //wait for Confirmation Message RCV on backend
            BackendMessageId backendId2 = BackendMessageId.ofRandom();
            DC5TransportRequest toBackend2 = mySubmitToLink.takeToBackendTransport(); //wait until something transported to Backend
            mySubmitToLink.acceptTransport(toBackend2, TransportState.ACCEPTED, backendId2);

        });

        //message status delivered
        txTemplate.executeWithoutResult((state) -> {
            DC5Message persistedMessage = messageRepo.getByConnectorMessageId(CONNECTOR_MESSAGE_ID);
            assertThat(persistedMessage.getMessageContent().getCurrentState().getState())
                    .as("Message must be in state delivered!")
                    .isEqualTo(DC5BusinessMessageState.BusinessMessagesStates.REJECTED);
            assertThat(persistedMessage.getMessageLaneId())
                    .as("Should assigned to default domain!")
                    .isEqualTo(DomibusConnectorBusinessDomain.getDefaultBusinessDomainId());
        });

    }

    /**
     * RCV message from GW
     * <p>
     * -) Backend must have received MSG
     * -) GW must have received RELAY_REMMD_ACCEPTANCE
     * <p>
     * -) test responds with DELIVERY Trigger
     * -) test responds with RETRIEVAL Trigger
     * <p>
     * -) GW must have received RETRIEVAL_EVIDENCE
     * <p>
     * -) Backend must have RCV DELIVERY Evidence
     * -) Backend must have RCV RETRIEVAL Evidence
     * <p>
     * -) message must be in confirmed state
     */
    @Test
    public void testReceiveMessageFromGw_respondWithDeliveryAndRetrieval(TestInfo testInfo) throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {
        EbmsMessageId EBMS_ID = EbmsMessageId.ofString("EBMS_" + testInfo.getDisplayName());
        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
        BackendMessageId deliveryTriggerBackendId = BackendMessageId.ofString("BACKEND_delivery_trigger_" + testInfo.getDisplayName());
        String MSG_FOLDER = "msg2";

        final BackendMessageId businessMsgBackendMsgId = BackendMessageId.ofString("BACKEND_businessmessageid_" + testInfo.getDisplayName());

        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {

            DC5Message testMessage = createTestMessage(MSG_FOLDER, EBMS_ID, CONNECTOR_MESSAGE_ID);
            submitFromGatewayToController(testMessage);

            Assertions.assertTimeout(Duration.ofSeconds(30), () -> {
                final DC5TransportRequest transportRequest = mySubmitToLink.takeToBackendTransport();//wait until something transported to Backend
                txTemplate.executeWithoutResult((state) -> {
                    DC5TransportRequest byTransportRequestId = transportRequestRepo.getById(transportRequest.getTransportRequestId());
                    DC5Message m = byTransportRequestId.getMessage();
                    assertThat(m.getMessageLaneId()).isNotNull();
                });

                mySubmitToLink.acceptTransport(transportRequest, TransportState.ACCEPTED, businessMsgBackendMsgId);    //Accept transport to backend and assign backend messageid
            }, "Should not take so long to rcv incoming business msg and ack it!");


            // confirm message to gw RELAY_REMMD_ACCEPTANCE
            EbmsMessageId ebmsId = EbmsMessageId.ofRandom();
            DC5TransportRequest toGW = mySubmitToLink.takeToGwTransport();//wait until something transported to GW
            mySubmitToLink.acceptTransport(toGW, TransportState.ACCEPTED, ebmsId);


            //create confirmation trigger message...
            DC5Message deliveryTriggerMessage = DC5Message.builder()
                    .transportedMessageConfirmation(DC5Confirmation.builder()
                            .evidenceType(DELIVERY)
                            .build()
                    )
                    .backendData(DC5BackendData.builder()
                            .backendMessageId(deliveryTriggerBackendId)
                            .refToBackendMessageId(businessMsgBackendMsgId)
                            .build())
                    .build();
            submitFromBackendToController(deliveryTriggerMessage);


            //wait for Confirmation Message RCV on GW
            mySubmitToLink.acceptTransport(mySubmitToLink.takeToGwTransport(), TransportState.ACCEPTED, EbmsMessageId.ofRandom());

            //wait for Confirmation Message RCV on backend
            mySubmitToLink.acceptTransport(mySubmitToLink.takeToBackendTransport(), TransportState.ACCEPTED, BackendMessageId.ofRandom());

            //create confirmation trigger message...
            DC5Message retrievalTriggerMsg = DC5Message.builder()
                    .transportedMessageConfirmation(DC5Confirmation.builder()
                            .evidenceType(RETRIEVAL)
                            .build()
                    )
                    .backendData(DC5BackendData.builder()
                            .backendMessageId(deliveryTriggerBackendId)
                            .refToBackendMessageId(businessMsgBackendMsgId)
                            .build())
                    .build();
            submitFromBackendToController(retrievalTriggerMsg);

            //wait for Confirmation Message RCV on GW
            mySubmitToLink.acceptTransport(mySubmitToLink.takeToGwTransport(), TransportState.ACCEPTED, EbmsMessageId.ofRandom());

            //wait for Confirmation Message RCV on backend
            mySubmitToLink.acceptTransport(mySubmitToLink.takeToBackendTransport(), TransportState.ACCEPTED, BackendMessageId.ofRandom());

            //message status delivered
            txTemplate.executeWithoutResult((state) -> {
                DC5Message persistedMessage = messageRepo.getByConnectorMessageId(CONNECTOR_MESSAGE_ID);
                assertThat(persistedMessage.getMessageContent().getCurrentState().getState())
                        .as("Message must be in state delivered!")
                        .isEqualTo(DC5BusinessMessageState.BusinessMessagesStates.RETRIEVED);
                assertThat(persistedMessage.getMessageLaneId())
                        .as("Should assigned to default domain!")
                        .isEqualTo(DomibusConnectorBusinessDomain.getDefaultBusinessDomainId());
            });


        });


    }

    /**
     * RCV message from GW
     * but cannot verify ASIC-S container
     * <p>
     * <p>
     * -) GW must have received RELAY_REMMD_ACCEPTANCE
     * -) GW must have received NON_DELIVERY
     * -) From and to Party are switched, within the evidence messages
     * -) refToMessageId of the evidence messages are the EBMS id of the RCV message
     */
    @Test
    public void testReceiveMessageFromGw_CertificateFailure(TestInfo testInfo) throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {
        EbmsMessageId EBMS_ID = EbmsMessageId.ofString("EBMS_" + testInfo.getDisplayName());
        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
        String MSG_FOLDER = "msg3";

        List<DC5TransportRequest> toGW = new ArrayList<>();

        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {

            DC5Message testMessage = createTestMessage(MSG_FOLDER, EBMS_ID, CONNECTOR_MESSAGE_ID);
            submitFromGatewayToController(testMessage);


            Assertions.assertTimeout(Duration.ofSeconds(30), () -> {
                // confirm message to gw RELAY_REMMD_ACCEPTANCE
                EbmsMessageId ebmsId = EbmsMessageId.ofRandom();
                DC5TransportRequest tr = mySubmitToLink.takeToGwTransport();//wait until something transported to GW
                mySubmitToLink.acceptTransport(tr, TransportState.ACCEPTED, ebmsId);
                toGW.add(tr);
            }, "Taking RELAY_REMMD_ACCEPTANCE from GW");


            Assertions.assertTimeout(Duration.ofSeconds(30), () -> {
                // confirm message to gw NON_DELIVERY
                EbmsMessageId ebmsId1 = EbmsMessageId.ofRandom();
                DC5TransportRequest tr = mySubmitToLink.takeToGwTransport();//wait until something transported to GW
                mySubmitToLink.acceptTransport(tr, TransportState.ACCEPTED, ebmsId1);
                toGW.add(tr);
            }, "Taking NON_DELIVERY from GW");


            Assertions.assertAll(
                    () -> {
                        txTemplate.executeWithoutResult((state) -> {
                            DC5TransportRequest byTransportRequestId = transportRequestRepo.getById(toGW.get(0).getTransportRequestId());
                            DC5Message m = byTransportRequestId.getMessage();
                            assertThat(m.getMessageLaneId()).isNotNull();
                            assertThat(m.getTransportedMessageConfirmations().get(0).getEvidenceType())
                                    .as("Expecting NON_DELIVERY")
                                    .isEqualTo(NON_DELIVERY);
                        });
                    },
                    () -> {
                        txTemplate.executeWithoutResult((state) -> {
                            DC5TransportRequest byTransportRequestId = transportRequestRepo.getById(toGW.get(1).getTransportRequestId());
                            DC5Message m = byTransportRequestId.getMessage();
                            assertThat(m.getMessageLaneId()).isNotNull();
                            assertThat(m.getTransportedMessageConfirmations().get(0).getEvidenceType())
                                    .as("Expecting RELAY_REMMD_ACCEPTANCE")
                                    .isEqualTo(RELAY_REMMD_ACCEPTANCE);
                        });
                    }
            );

        });
    }


    /**
     * RCV message from GW
     * but cannot deliver to backend
     * <p>
     * <p>
     * -) GW must have received RELAY_REMMD_ACCEPTANCE
     * -) GW must have received NON_DELIVERY
     */
    @Test
    public void testReceiveMessageFromGw_backendDeliveryFailure(TestInfo testInfo) throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {
        EbmsMessageId EBMS_ID = EbmsMessageId.ofString("EBMS_" + testInfo.getDisplayName());
        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
        BackendMessageId deliveryTriggerBackendId = BackendMessageId.ofString("BACKEND_delivery_trigger_" + testInfo.getDisplayName());
        String MSG_FOLDER = "msg2";

        final BackendMessageId businessMsgBackendMsgId = BackendMessageId.ofString("BACKEND_businessmessageid_" + testInfo.getDisplayName());

        Assertions.assertTimeoutPreemptively(TEST_TIMEOUT, () -> {

            DC5Message testMessage = createTestMessage(MSG_FOLDER, EBMS_ID, CONNECTOR_MESSAGE_ID);
            submitFromGatewayToController(testMessage);

            Assertions.assertTimeout(Duration.ofSeconds(30), () -> {
                final DC5TransportRequest transportRequest = mySubmitToLink.takeToBackendTransport();//wait until something transported to Backend
                txTemplate.executeWithoutResult((state) -> {
                    DC5TransportRequest byTransportRequestId = transportRequestRepo.getById(transportRequest.getTransportRequestId());
                    DC5Message m = byTransportRequestId.getMessage();
                    assertThat(m.getMessageLaneId()).isNotNull();
                });

                mySubmitToLink.acceptTransport(transportRequest, TransportState.FAILED, businessMsgBackendMsgId);    //Fail transport to backend and assign backend messageid
            }, "Should not take so long to rcv incoming business msg and ack it!");
        });

        // wait for RELAY_REMMD_ACCEPTANCE on GW
        Assertions.assertTimeout(Duration.ofSeconds(30), () -> {
            EbmsMessageId ebmsId = EbmsMessageId.ofRandom();
            DC5TransportRequest toGW1 = mySubmitToLink.takeToGwTransport();//wait until something transported to GW
            mySubmitToLink.acceptTransport(toGW1, TransportState.ACCEPTED, ebmsId);
            txTemplate.executeWithoutResult((state) -> {
                DC5TransportRequest byTransportRequestId = transportRequestRepo.getById(toGW1.getTransportRequestId());
                DC5Message m = byTransportRequestId.getMessage();
                assertThat(m.getMessageLaneId()).isNotNull();
                assertThat(m.getTransportedMessageConfirmations().get(0).getEvidenceType())
                        .as("Expecting RELAY_REMMD_ACCEPTANCE")
                        .isEqualTo(RELAY_REMMD_ACCEPTANCE);
            });
        }, "Taking RELAY_REMMD_ACCEPTANCE from GW");

        //wait for non_delivery on GW
        Assertions.assertTimeout(Duration.ofSeconds(30), () -> {
            EbmsMessageId ebmsId = EbmsMessageId.ofRandom();
            DC5TransportRequest toGW1 = mySubmitToLink.takeToGwTransport();//wait until something transported to GW
            mySubmitToLink.acceptTransport(toGW1, TransportState.ACCEPTED, ebmsId);
            txTemplate.executeWithoutResult((state) -> {
                DC5TransportRequest byTransportRequestId = transportRequestRepo.getById(toGW1.getTransportRequestId());
                DC5Message m = byTransportRequestId.getMessage();
                assertThat(m.getMessageLaneId()).isNotNull();
                assertThat(m.getTransportedMessageConfirmations().get(0).getEvidenceType())
                        .as("Expecting NON_DELIVERY")
                        .isEqualTo(NON_DELIVERY);
            });


        }, "Taking NON_DELIVERY from GW");

    }

    private DC5Message createTestMessage(String msgFolder, EbmsMessageId EBMS_ID, DomibusConnectorMessageId CONNECTOR_MESSAGE_ID) {
        try {
            DC5Message testMessage = LoadStoreMessageFromPath.loadMessageFrom(new ClassPathResource("/testmessages/" + msgFolder + "/"), largeFilePersistenceService);
            assertThat(testMessage).isNotNull();
            if (testMessage.getEbmsData() == null && EBMS_ID != null) {
                throw new IllegalArgumentException("TestMessage does not have any EBMS data!");
            } else if (testMessage.getEbmsData() != null && EBMS_ID != null) {
                testMessage.getEbmsData().setEbmsMessageId(EBMS_ID);
            }
            testMessage.setConnectorMessageId(CONNECTOR_MESSAGE_ID);
            return testMessage;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }


    //TODO: check for mixed income of confirmation:
    // RELAY_ACCEPTANCE, DELIVERY, RETRIEVAL
    // DELIVERY, RELAY, RETRIEVAL
    // ...
    // TIMESTAMP OF EVIDENCE COUNTS!

    /**
     * Send message from Backend to GW
     * <p>
     * -) Backend must have received SUBMISSION_ACCEPTANCE
     * -) GW must have received Business MSG with SUBMISSION_ACCEPTANCE and 2 attachments ASICS-S, tokenXml
     */
    @Test
    public void sendMessageFromBackend(TestInfo testInfo) {

        DomibusConnectorMessageId CONNECTOR_MESSAGE_ID = DomibusConnectorMessageId.ofString(testInfo.getDisplayName());
        BackendMessageId BACKEND_MESSAGE_ID = BackendMessageId.ofString("BACKEND_MSG_ID" + testInfo.getDisplayName());

        DC5Message dc5Message = submitMessage(CONNECTOR_MESSAGE_ID, BACKEND_MESSAGE_ID);

        //wait for outgoing EcodexBusinessMsg on GW
        Assertions.assertTimeout(Duration.ofSeconds(30), () -> {
            EbmsMessageId ebmsId = EbmsMessageId.ofRandom();
            DC5TransportRequest toGW1 = mySubmitToLink.takeToGwTransport();//wait until something transported to GW
            mySubmitToLink.acceptTransport(toGW1, TransportState.ACCEPTED, ebmsId);
            txTemplate.executeWithoutResult((state) -> {
                DC5TransportRequest byTransportRequestId = transportRequestRepo.getById(toGW1.getTransportRequestId());
                DC5Message m = byTransportRequestId.getMessage();
                assertThat(m.getMessageLaneId()).isNotNull();
                assertThat(m.getTransportedMessageConfirmations().get(0).getEvidenceType())
                        .as("Expecting SUBMISSION_ACCEPTANCE")
                        .isEqualTo(SUBMISSION_ACCEPTANCE);
            });

        }, "Taking Business Message from GW");

    }

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
    private DC5Message submitMessage(DomibusConnectorMessageId connectorMessageId, BackendMessageId backendMessageId) {
        DC5Message msg = DC5Message.builder()
                .connectorMessageId(connectorMessageId)
                .backendData(DC5BackendData.builder()
                        .backendMessageId(backendMessageId)
                        .build())
                .messageContent(DC5MessageContent.builder()
                        .businessContent(DC5BackendContent.builder()
                                .businessDocument(DomainEntityCreator.createSimpleMessageAttachment())
                                .businessXml(DomainEntityCreator.createSimpleMessageAttachment())
                                .build())
                        .build())
                .ebmsData(DC5Ebms.builder()
                        .service(DomainEntityCreator.createServiceEPO())
                        .action(DomainEntityCreator.createActionForm_A())
                        .gatewayAddress(DomainEntityCreator.defaultRecipient())
                        .backendAddress(DomainEntityCreator.defaultSender())
                        .build())
                .build();
        submitFromBackendToController(msg);
        return msg;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void submitFromBackendToController(DC5Message message) {
        message.setSource(MessageTargetSource.BACKEND);
        message.setTarget(MessageTargetSource.GATEWAY);
        if (message.getConnectorMessageId() == null) {
            message.setConnectorMessageId(messageIdGenerator.generateDomibusConnectorMessageId());
        }
        if (message.getMessageLaneId() == null || StringUtils.isEmpty(message.getMessageLaneId().getBusinessDomainId())) {
            message.setMessageLaneId(DomibusConnectorBusinessDomain.getDefaultBusinessDomainId());
        }
        DomibusConnectorLinkPartner testLink = new DomibusConnectorLinkPartner();
        testLink.setLinkPartnerName(new DomibusConnectorLinkPartner.LinkPartnerName("test_backend"));
        testLink.setLinkType(LinkType.BACKEND);
        submitToConnector.submitToConnector(message, testLink);
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
