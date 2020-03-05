
package eu.domibus.connector.controller.service;


import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.test.util.ITCaseTestContext;
import eu.domibus.connector.controller.test.util.LoadStoreMessageFromPath;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
@TestPropertySource("classpath:application-test.properties")
@Sql(scripts = "/testdata.sql") //adds testdata to database like domibus-blue party
@ActiveProfiles({"ITCaseTestContext", "storage-db", "test"})
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
    DomibusConnectorGatewayDeliveryService rcvMessageFromGwService;

    @Autowired
    DomibusConnectorBackendSubmissionService sendMessageToBackendService;


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
    public void testReceiveMessageFromGw() throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
            DomibusConnectorMessage testMessage = LoadStoreMessageFromPath.loadMessageFrom(new ClassPathResource("/testmessages/msg2/"));

            assertThat(testMessage).isNotNull();
            testMessage.getMessageDetails().setFinalRecipient("final recipient");
            testMessage.getMessageDetails().setOriginalSender("original sender");
            testMessage.getMessageDetails().setEbmsMessageId("EBMS_TEST_ID");

            LOGGER.info("message with confirmations: [{}]", testMessage.getMessageConfirmations());

            rcvMessageFromGwService.deliverMessageFromGatewayToController(testMessage);

            DomibusConnectorMessage take = toBackendDeliveredMessages.take(); //wait until a message is put into queue
            assertThat(toBackendDeliveredMessages).hasSize(0); //queue should be empty!
            assertThat(take).isNotNull();

            //TODO: check database!
        });
    }
}
