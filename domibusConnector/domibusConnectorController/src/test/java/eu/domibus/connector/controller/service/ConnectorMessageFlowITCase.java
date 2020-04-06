
package eu.domibus.connector.controller.service;


import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.test.util.ITCaseTestContext;
import eu.domibus.connector.controller.test.util.LoadStoreMessageFromPath;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageDetailsBuilder;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ITCaseTestContext.class})
@TestPropertySource("classpath:config/application-test.properties")
@Sql(scripts = "/testdata.sql") //adds testdata to database like domibus-blue party
@ActiveProfiles({"ITCaseTestContext", STORAGE_DB_PROFILE_NAME, "test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ConnectorMessageFlowITCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectorMessageFlowITCase.class);

    public static String TEST_FILE_RESULTS_DIR_PROPERTY_NAME = "test.file.results";
    private File testResultsFolder;
    private String testDateAsString;


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

    @BeforeEach
    public void setUp() {
        String dir = System.getenv().getOrDefault(TEST_FILE_RESULTS_DIR_PROPERTY_NAME, "./target/testfileresults/");
        dir = dir + "/" + ConnectorMessageFlowITCase.class.getSimpleName();
        testResultsFolder = new File(dir);
        testResultsFolder.mkdirs();

        DateFormatter simpleDateFormatter = new DateFormatter();
        simpleDateFormatter.setPattern("yyyy-MM-dd-hh-mm");
        testDateAsString = simpleDateFormatter.print(new Date(), Locale.ENGLISH);

        //clear delivery lists
        toGwDeliveredMessages.clear();
        toBackendDeliveredMessages.clear();
    }


    @Test
    @Disabled("new certificates does not match testmessage")
    public void testReceiveMessageFromGw() throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(20), () -> {
            DomibusConnectorMessage testMessage = LoadStoreMessageFromPath.loadMessageFrom(new ClassPathResource("/testmessages/msg2/"));

            assertThat(testMessage).isNotNull();
            testMessage.getMessageDetails().setFinalRecipient("final recipient");
            testMessage.getMessageDetails().setOriginalSender("original sender");
            testMessage.getMessageDetails().setEbmsMessageId("EBMS_TEST_ID");

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
     *   -) GW must have received Business MSG with SUBMISSION_ACCEPTANCE
     *
     */
    @Test
//    @Disabled("in progress")
    public void sendMessageFromBackend() {
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(20), () -> {
            DomibusConnectorMessageBuilder msgBuilder = DomibusConnectorMessageBuilder.createBuilder();
            DomibusConnectorMessage msg = msgBuilder.setMessageContent(DomainEntityCreator.createMessageContentWithDocumentWithNoSignature())
                    .setConnectorMessageId("t1")
                    .setMessageDetails(DomibusConnectorMessageDetailsBuilder
                            .create()
                            .withAction("action1")
                            .withService("service1", "servicetype")
                            .withBackendMessageId("backend1")
                            .withConversationId("conv1")
                            .withBackendMessageId("b1")
                            .withFromParty(DomainEntityCreator.createPartyAT())
                            .withToParty(DomainEntityCreator.createPartyDE())
                            .withFinalRecipient("final")
                            .withOriginalSender("original")
                            .build()
                    ).build();

            msg = messagePersistenceService.persistMessageIntoDatabase(msg, DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);

            backendSubmissionService.submitToController(msg);


            DomibusConnectorMessage take = toGwDeliveredMessages.take(); //wait until a message is put into queue

//            DomibusConnectorMessage m2 = toGwDeliveredMessages.take();



            assertThat(toBackendDeliveredMessages.isEmpty()).isTrue();
            assertThat(take).as("Gw must RCV message").isNotNull();

            //TODO: analyze take
            assertThat(take.getMessageConfirmations()).as("submission acceptance evidence must be a part of message").hasSize(1); //SUBMISSION_ACCEPTANCE

            //TODO: check DB

        });
    }
}
