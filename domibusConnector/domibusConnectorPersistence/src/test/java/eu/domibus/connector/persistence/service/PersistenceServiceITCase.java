package eu.domibus.connector.persistence.service;



//import eu.domibus.connector.domain.Action;
//import eu.domibus.connector.domain.Message;
//import eu.domibus.connector.domain.MessageContent;
//import eu.domibus.connector.domain.MessageDetails;
//import eu.domibus.connector.domain.Party;
//import eu.domibus.connector.domain.Service;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageAttachment;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.domain.model.DomibusConnectorParty;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.domain.test.util.DomainEntityCreatorForPersistenceTests;
import eu.domibus.connector.domain.transformer.util.DomibusConnectorBigDataReferenceMemoryBacked;
import eu.domibus.connector.persistence.model.PDomibusConnectorPersistenceModel;
import eu.domibus.connector.persistence.spring.DomibusConnectorPersistenceContext;
import java.sql.SQLException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import org.dbunit.database.AmbiguousTableNameException;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import eu.domibus.connector.persistence.dao.PackageDomibusConnectorRepositories;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration Test for testing persistence service
 * 
 * creates a embedded h2 database for tests
 * this tests are _WRITING_ to the database, there is no rollback
 * 
 * then liquibase is used to initialize tables and basic testdata in
 * the testing database
 * 
 * additional testdata is loaded by dbunit in setUp method
 * 
 * database settings are configured in {@literal application-<profile>.properties}
 * 
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at>}
 */
public class PersistenceServiceITCase {

    @SpringBootApplication(scanBasePackages={"eu.domibus.connector"})
    static class TestConfiguration {
    }

    static ConfigurableApplicationContext APPLICATION_CONTEXT;

    @BeforeClass
    public static void InitClass() {
        SpringApplicationBuilder springAppBuilder = new SpringApplicationBuilder(TestConfiguration.class)
                //.profiles("test", "db_mysql")
                .profiles("test", "db_h2")
                .properties("liquibase.change-log=/db/changelog/install/initial-4.0.xml")
                ;
        APPLICATION_CONTEXT = springAppBuilder.run();
        System.out.println("APPCONTEXT IS STARTED...:" + APPLICATION_CONTEXT.isRunning());

    }

    @AfterClass
    public static void afterClass() {
        APPLICATION_CONTEXT.close();
    }

    @Before
    public void setUp() throws InterruptedException {
        
        //lookup type
        this.ds = APPLICATION_CONTEXT.getBean(DataSource.class);
        //lookup name
        this.persistenceService = APPLICATION_CONTEXT.getBean("persistenceService", DomibusConnectorPersistenceService.class);
    }

    DataSource ds;

    DomibusConnectorPersistenceService persistenceService;


    @Test
    public void testGetService() {
        DomibusConnectorService epo = persistenceService.getService("EPO");
        assertThat(epo).isNotNull();
    }

    
    @Test
    public void testPersistMessageIntoDatabase() throws PersistenceException, SQLException, AmbiguousTableNameException, DataSetException {
        String connectorMessageId = "msg0021";

        DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createMessage(connectorMessageId);
        //message.setDbMessageId(null);
        //MessageDirection messageDirection = MessageDirection.GW_TO_NAT;
        DomibusConnectorMessageDetails messageDetails = message.getMessageDetails();
        
        messageDetails.setConversationId("conversation421");
        messageDetails.setEbmsMessageId("ebms421");
        messageDetails.setBackendMessageId("backend421");
        
        
        //message.getMessageContent().setDetachedSignature("HalloWelt".getBytes());       
        
        
        DomibusConnectorMessage persistedMessage = persistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.GW_TO_NAT);
        
        assertThat(persistedMessage).isNotNull();

        //check result in DB
        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(ds);
        QueryDataSet dataSet = new QueryDataSet(conn);
        dataSet.addTable("DOMIBUS_CONNECTOR_MESSAGE", 
                String.format("SELECT * FROM DOMIBUS_CONNECTOR_MESSAGE WHERE CONNECTOR_MESSAGE_ID='%s'", connectorMessageId));
       
        ITable domibusConnectorTable = dataSet.getTable("DOMIBUS_CONNECTOR_MESSAGE");
        
        String ebmsId = (String) domibusConnectorTable.getValue(0, "ebms_message_id");
        assertThat(ebmsId).isEqualTo("ebms421");
        
        String conversationId = (String) domibusConnectorTable.getValue(0, "conversation_id");
        assertThat(conversationId).isEqualTo("conversation421");        
    }
    
    /**
     * tests complete message, if can be stored to DB
     * and also loaded again from DB    
     * 
     * test restore evidenceMessage!
     */
    @Test
    public void testPersistMessageIntoDatabase_testContentPersist() throws PersistenceException, SQLException, AmbiguousTableNameException, DataSetException {
        String ebmsId = "ebamHUGO1";
        DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createMessage("superid23");
        message.getMessageDetails().setEbmsMessageId(ebmsId);
        message.getMessageDetails().setBackendMessageId("fklwefa");
        
        DomibusConnectorBigDataReferenceMemoryBacked attachRef = 
                new DomibusConnectorBigDataReferenceMemoryBacked("hallo welt".getBytes());
        DomibusConnectorMessageAttachment attach1 = 
                new DomibusConnectorMessageAttachment(attachRef, "idf");        
        message.addAttachment(attach1);
        
        DomibusConnectorMessageConfirmation confirmation = new DomibusConnectorMessageConfirmation(DomibusConnectorEvidenceType.DELIVERY, "hallowelt".getBytes());
        message.addConfirmation(confirmation);
        
        
        DomibusConnectorMessageDirection messageDirection = DomibusConnectorMessageDirection.GW_TO_NAT;        
        persistenceService.persistMessageIntoDatabase(message, messageDirection);
                
        
        
        //load persisted message again from db and run checks
        DomibusConnectorMessage messageToCheck = persistenceService.findMessageByEbmsId(ebmsId);
        
        assertThat(messageToCheck.getMessageContent()).isNotNull();
        
        assertThat(messageToCheck.getMessageAttachments()).as("should contain two attachments!").hasSize(2);
        DomibusConnectorMessageAttachment messageAttachment = messageToCheck
                .getMessageAttachments()
                .stream()
                .filter(a -> "idf".equals(a.getIdentifier()))
                .findFirst()
                .get();
//        assertThat(messageAttachment.getAttachment())
//                .as("Attachment content [%s] should equal [%s]", messageAttachment.getAttachment(), attach1.getAttachment())
//                .containsExactly(attach1.getAttachment());

    }
    
    @Test
    public void testMergeMessageWithDatabase() throws PersistenceException, SQLException, AmbiguousTableNameException, DataSetException {
        DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createMessage("superid");
        message.getMessageDetails().setEbmsMessageId("ebamdsafae3");
        message.getMessageDetails().setBackendMessageId("adfsöljabafklwefa");
        
        DomibusConnectorMessageDirection messageDirection = DomibusConnectorMessageDirection.GW_TO_NAT;        
        persistenceService.persistMessageIntoDatabase(message, messageDirection);
                
        //TODO: make changes to message
        
        persistenceService.mergeMessageWithDatabase(message);

    }
    
    @Test(expected=PersistenceException.class)
    public void testMergeMessageWithDatabase_doesNotExistInDatabase() throws PersistenceException {
        DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createMessage();
//        MessageDetails messageDetails = new MessageDetails();
//        MessageContent messageContent = new MessageContent();
//        Message message = new Message(messageDetails, messageContent);
//        message.setDbMessageId(48L);
        persistenceService.mergeMessageWithDatabase(message);
    }
    
    @Test
    public void testPersistEvidenceForMessageIntoDatabase() throws PersistenceException {
//        MessageDetails messageDetails = new MessageDetails();
//        MessageContent messageContent = new MessageContent();
//        Message message = new Message(messageDetails, messageContent);
        DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createMessage("myid");

        persistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.NAT_TO_GW); //create message first
                
        byte[] evidence = "hallo Welt".getBytes();
        
        persistenceService.persistEvidenceForMessageIntoDatabase(message, evidence, DomibusConnectorEvidenceType.DELIVERY);
        
        //TODO: check db changes
    }

    
    @Test  
    public void testFindMessageByNationalId_doesNotExist_shouldThrowEmptyResultException() {
        String nationalIdString = "TEST1";
        DomibusConnectorMessage findMessageByNationalId = persistenceService.findMessageByNationalId(nationalIdString);
        
        assertThat(findMessageByNationalId).isNull();
    }
    
    @Test
    public void testGetAction_doesNotExistInDb_retShouldBeNull() {
        DomibusConnectorAction action = persistenceService.getAction("DOESNOTEXIST");
        assertThat(action).as("should be null beacause does not exist in db").isNull();
    }
    
    @Test
    public void testGetAction() {
        DomibusConnectorAction action = persistenceService.getAction("Form_A");
        assertThat(action).isNotNull();
    }
    
    @Test
    public void testGetParty() {       
        DomibusConnectorParty party = persistenceService.getParty("AT", "GW");
        assertThat(party).isNotNull();
    }
    
    @Test
    public void testGetParty_doesNotExistInDB_retShouldBeNull() {       
        DomibusConnectorParty party = persistenceService.getParty("ATEA", "GW");
        assertThat(party).isNull();
    }
    
    @Test
    public void testGetPartyById() {
        DomibusConnectorParty party = persistenceService.getPartyByPartyId("AT");
        assertThat(party).isNotNull();
    }
    
    
    //TODO: test other methods/use cases
    /**
     *  void persistMessageIntoDatabase(Message message, MessageDirection direction) throws PersistenceException;

    void mergeMessageWithDatabase(Message message);

    void persistEvidenceForMessageIntoDatabase(Message message, byte[] evidence, EvidenceType evidenceType);

    Message findMessageByNationalId(String nationalMessageId);

    Message findMessageByEbmsId(String ebmsMessageId);

    void setEvidenceDeliveredToGateway(Message message, EvidenceType evidenceType);

    void setEvidenceDeliveredToNationalSystem(Message message, EvidenceType evidenceType);

    void setMessageDeliveredToGateway(Message message);

    void setMessageDeliveredToNationalSystem(Message message);

    List<Message> findOutgoingUnconfirmedMessages();

    List<Message> findIncomingUnconfirmedMessages();

    void confirmMessage(Message message);

    void rejectMessage(Message message);

    DomibusConnectorAction getAction(String action);

    DomibusConnectorAction getRelayREMMDAcceptanceRejectionAction();

    DomibusConnectorAction getRelayREMMDFailure();

    DomibusConnectorAction getDeliveryNonDeliveryToRecipientAction();

    DomibusConnectorAction getRetrievalNonRetrievalToRecipientAction();

    DomibusConnectorService getService(String service);

    DomibusConnectorParty getParty(String partyId, String role);

    DomibusConnectorParty getPartyByPartyId(String partyId);

    List<Message> findMessagesByConversationId(String conversationId);

    void persistMessageError(MessageError messageError);

    List<MessageError> getMessageErrors(Message message) throws Exception;

    void persistMessageErrorFromException(Message message, Throwable ex, Class<?> source) throws PersistenceException;

	List<Message> findOutgoingMessagesNotRejectedAndWithoutDelivery();

	List<Message> findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD();
     */
}
