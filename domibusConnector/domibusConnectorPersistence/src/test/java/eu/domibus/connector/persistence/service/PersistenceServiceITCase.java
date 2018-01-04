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
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.domain.model.DomibusConnectorParty;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.domain.test.util.DomainCreator;
import eu.domibus.connector.persistence.dao.PDomibusConnectorRepositories;
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
//        MessageDetails messageDetails = new MessageDetails();
//        MessageContent messageContent = new MessageContent();
        DomibusConnectorMessage message = DomainCreator.createSimpleTestMessage();
        //message.setDbMessageId(null);
        //MessageDirection messageDirection = MessageDirection.GW_TO_NAT;
        
        message.getMessageDetails().setConversationId("newconversation");
        message.getMessageDetails().setEbmsMessageId("ebms1");
        
        //message.getMessageContent().setDetachedSignature("HalloWelt".getBytes());       
        
        
        persistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.GW_TO_NAT);
        
        //assertThat(message.getDbMessageId()).isNotNull();
        //TODO: test db if entity is there!         
        
        
        //long messageId = message.getDbMessageId();
        //assertThat(messageId).isNotNull();
        
        //TODO: check db changes
        //check result in DB
//        DatabaseDataSourceConnection conn = new DatabaseDataSourceConnection(ds);
//        QueryDataSet dataSet = new QueryDataSet(conn);
//        dataSet.addTable("DOMIBUS_CONNECTOR_MESSAGE", String.format("SELECT * FROM DOMIBUS_CONNECTOR_MESSAGE WHERE ID=%s", messageId));
//       
//        ITable domibusConnectorTable = dataSet.getTable("DOMIBUS_CONNECTOR_MESSAGE");
//        
//        String ebmsId = (String) domibusConnectorTable.getValue(0, "ebms_message_id");
//        assertThat(ebmsId).isEqualTo("ebms1");
//        
//        String conversationId = (String) domibusConnectorTable.getValue(0, "conversation_id");
//        assertThat(conversationId).isEqualTo("newconversation");        
    }
        
    
    @Test
    public void testMergeMessageWithDatabase() throws PersistenceException, SQLException, AmbiguousTableNameException, DataSetException {
        
        
        
//        DomibusConnectorMessageDetails messageDetails = new DomibusConnectorMessageDetails();
//        DomibusConnectorMessageContent messageContent = null; //new DomibusConnectorMessageContent();
//        DomibusConnectorMessage message = new DomibusConnectorMessage(messageDetails, messageContent);                
        //message.setDbMessageId(null); //is a new message
        DomibusConnectorMessage message = DomainCreator.createMessage();
        
        DomibusConnectorMessageDirection messageDirection = DomibusConnectorMessageDirection.GW_TO_NAT;        
        persistenceService.persistMessageIntoDatabase(message, messageDirection);
                
        persistenceService.mergeMessageWithDatabase(message);

    }
    
    @Test(expected=PersistenceException.class)
    public void testMergeMessageWithDatabase_doesNotExistInDatabase() throws PersistenceException {
        DomibusConnectorMessage message = DomainCreator.createMessage();
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
        DomibusConnectorMessage message = DomainCreator.createMessage();

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
