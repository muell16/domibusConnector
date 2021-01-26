package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.test.util.ITCaseTestContext;
import eu.domibus.connector.controller.test.util.LoadStoreMessageFromPath;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.LargeFileReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDocument;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageDocumentBuilder;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessageContentManager;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StreamUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;

import static eu.domibus.connector.persistence.spring.PersistenceProfiles.STORAGE_DB_PROFILE_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ITCaseTestContext.class})
@Commit
@ActiveProfiles({"ITCaseTestContext", STORAGE_DB_PROFILE_NAME, "test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Disabled
public class BackendToGatewayMessageProcessorITCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackendToGatewayMessageProcessorITCase.class);

    @Autowired
    @Qualifier("BackendToGatewayMessageProcessor")
    private DomibusConnectorMessageProcessor backendToGatewayMessageProcessor;

    @Autowired
    @Qualifier(ITCaseTestContext.TO_GW_DELIVERD_MESSAGES_LIST_BEAN_NAME)
    private BlockingQueue<DomibusConnectorMessage> toGatewayDeliveredMessages;

    @Autowired
    @Qualifier(ITCaseTestContext.TO_BACKEND_DELIVERD_MESSAGES_LIST_BEAN_NAME)
    private BlockingQueue<DomibusConnectorMessage> toBackendDeliveredMessages;

    @Autowired
    @Qualifier("GatewayToBackendMessageProcessor")
    private DomibusConnectorMessageProcessor gatewayToBackendMessageProcessor;

    @Autowired
    private DCMessagePersistenceService messagePersistenceService;

    @Autowired
    private DomibusConnectorMessageContentManager bigDataWithMessagePersistenceService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    ITCaseTestContext.DomibusConnectorGatewaySubmissionServiceInterceptor submissionServiceInterceptor;

    @BeforeEach
    public void setUp() {
        toGatewayDeliveredMessages.clear();
        toBackendDeliveredMessages.clear();
    }

    @Test
    public void testProcessMessage() throws IOException, DataSetException, SQLException, InterruptedException {
        Assertions.assertTimeout(Duration.ofSeconds(20), () -> {
            DomibusConnectorMessage message = prepareMessage("msg1");

            backendToGatewayMessageProcessor.processMessage(message);

            assertThat(toGatewayDeliveredMessages).hasSize(1);
            assertThat(toBackendDeliveredMessages).hasSize(1);

            DomibusConnectorMessage toGwMsg = toGatewayDeliveredMessages.take();
            assertThat(toGwMsg.getTransportedMessageConfirmations()).hasSize(1); //should contain submission acceptance evidence!
            assertThat(toGwMsg.getTransportedMessageConfirmations().get(0).getEvidenceType()).isEqualTo(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE);


            DomibusConnectorMessage evidence = toBackendDeliveredMessages.take();
            assertThat(evidence.getTransportedMessageConfirmations()).hasSize(1);
            assertThat(evidence.getTransportedMessageConfirmations().get(0).getEvidenceType()).isEqualTo(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE);

            //TODO: verify DB
            DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(dataSource);
            QueryDataSet dataSet = new QueryDataSet(conn);
            dataSet.addTable("DOMIBUS_CONNECTOR_BIGDATA", "SELECT * FROM DOMIBUS_CONNECTOR_BIGDATA");
            dataSet.addTable("DOMIBUS_CONNECTOR_MESSAGE", "SELECT * FROM DOMIBUS_CONNECTOR_MESSAGE");


//        ITable domibusConnectorTable = dataSet.getTable("DOMIBUS_CONNECTOR_BIGDATA");
//        FlatXmlDataSet.write(dataSet, System.out);
//        Object content = domibusConnectorTable.getValue(2, "CONTENT");
//        assertThat(content).isNotNull();
//        content = domibusConnectorTable.getValue(4, "CONTENT");
//        assertThat(content).isNotNull();

//        //write by gw rcv originalMessage to file system
//        FileSystemUtils.deleteRecursively(new File("./target/testm/"));
//        msg = bigDataWithMessagePersistenceService.loadAllBigFilesFromMessage(msg);
//        LoadStoreMessageFromPath.storeMessageTo(new FileSystemResource("./target/testm/"), msg);


//        ITable messageTable = dataSet.getTable("DOMIBUS_CONNECTOR_MESSAGE");
//        FlatXmlDataSet.write(dataSet, System.out);


//        System.out.println("#############\n#############\n#############\n#############\nOTHER WAY!!!!");
//
//        bigDataWithMessagePersistenceService.loadAllBigFilesFromMessage(msg);
//
//        gatewayToBackendMessageProcessor.processMessage(msg);
        });
    }

    /*
     * Test send to GW, but gw fails
     * check that SUBMISSION_REJECTION evidence exists in db AND is sent to backend!
     */
    @Test
    public void testProcessMessage_readAttachment() throws IOException, DataSetException, SQLException, DomibusConnectorGatewaySubmissionException, InterruptedException {
        Assertions.assertTimeout(Duration.ofSeconds(20), () -> {
            Mockito.doAnswer(invoc -> {
                DomibusConnectorMessage msg = invoc.getArgument(0);
                InputStream inputStream = msg.getMessageAttachments().get(0).getAttachment().getInputStream();
                byte[] bytes = StreamUtils.copyToByteArray(inputStream);


                return null;
            })
                    .when(submissionServiceInterceptor).submitToGateway(Mockito.any(DomibusConnectorMessage.class));


            DomibusConnectorMessage message = prepareMessage("msg2");

            backendToGatewayMessageProcessor.processMessage(message);


            DomibusConnectorMessage take = toBackendDeliveredMessages.take();
            System.out.println("originalMessage: " + take.getMessageDetails());
        });
    }


    /*
     * Test send to GW, but gw fails
     * check that SUBMISSION_REJECTION evidence exists in db AND is sent to backend!
     */
    @Test
    public void testProcessMessage_sendToGwFails() throws IOException, DataSetException, SQLException, DomibusConnectorGatewaySubmissionException, InterruptedException {
        Assertions.assertTimeout(Duration.ofSeconds(20), () -> {
            Mockito.doThrow(new DomibusConnectorGatewaySubmissionException("a error occured!"))
                    .when(submissionServiceInterceptor).submitToGateway(Mockito.any(DomibusConnectorMessage.class));


            DomibusConnectorMessage message = prepareMessage("msg3");
            String connectorMessageid = message.getConnectorMessageIdAsString();
            try {
                backendToGatewayMessageProcessor.processMessage(message);
            } catch (Exception e) {

            }

            assertThat(toBackendDeliveredMessages).hasSize(1);

            //verify DB
            DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(dataSource);
            QueryDataSet dataSet = new QueryDataSet(conn);
            dataSet.addTable("DOMIBUS_CONNECTOR_MESSAGE ", "SELECT * FROM DOMIBUS_CONNECTOR_MESSAGE WHERE CONNECTOR_MESSAGE_ID = '" + connectorMessageid + "'");
            ITable domibusConnectorTable = dataSet.getTable("DOMIBUS_CONNECTOR_MESSAGE ");
            FlatXmlDataSet.write(dataSet, System.out);
            long dbMessageId = ((BigDecimal) domibusConnectorTable.getValue(0, "id")).longValue();


            QueryDataSet evidenceDataSet = new QueryDataSet(conn);
            evidenceDataSet.addTable("DOMIBUS_CONNECTOR_EVIDENCE  ", "SELECT * FROM DOMIBUS_CONNECTOR_EVIDENCE WHERE MESSAGE_ID=" + dbMessageId);
            ITable evidenceTable = evidenceDataSet.getTable("DOMIBUS_CONNECTOR_EVIDENCE  ");
            FlatXmlDataSet.write(evidenceDataSet, System.out);

            //2nd evidence should be submission_rejection (because of gw error)
            Object type = evidenceTable.getValue(1, "TYPE");
            assertThat(type).isEqualTo("SUBMISSION_REJECTION");
        });
    }

    /*
     * prepares test originalMessage for originalMessage processing by
     *  creating (test) originalMessage
     *  persisting originalMessage
     *
     */
    private DomibusConnectorMessage prepareMessage(String idsuffix) {
        DomibusConnectorMessage message = DomainEntityCreator.createMessage();
        message.getMessageDetails().setEbmsMessageId(null);
        message.getMessageDetails().setBackendMessageId("backend_" + idsuffix);
        message.getMessageDetails().setConnectorBackendClientName("bob");
        message.setConnectorMessageId("msg_" + idsuffix);
        message.getTransportedMessageConfirmations().clear();


        LargeFileReference bigDataReference = LoadStoreMessageFromPath.loadResourceAsBigDataRef(new ClassPathResource("testmessages/testdata/ExamplePdfSigned.pdf"));
        DomibusConnectorMessageDocument signedDocument = DomibusConnectorMessageDocumentBuilder.createBuilder()
                .setContent(bigDataReference)
                .setName("SignedDocument")
                .build();
        message.getMessageContent().setDocument(signedDocument);

        message = messagePersistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);
//        message = bigDataWithMessagePersistenceService.persistAllBigFilesFromMessage(message);
//        message = messagePersistenceService.mergeMessageWithDatabase(message);
        return message;
    }
}
