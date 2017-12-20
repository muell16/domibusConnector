package eu.domibus.connector.persistence.service;


import eu.domibus.connector.persistence.model.DomibusConnectorAction;
import eu.domibus.connector.persistence.model.DomibusConnectorParty;
import eu.domibus.connector.persistence.model.DomibusConnectorService;
import eu.domibus.connector.common.enums.EvidenceType;
import eu.domibus.connector.common.enums.MessageDirection;
import eu.domibus.connector.common.exception.PersistenceException;
import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.common.message.MessageContent;
import eu.domibus.connector.common.message.MessageDetails;
import javax.persistence.NoResultException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Ignore;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * Integration Test for testing persistence service
 * 
 * creates a embedded h2 database for tests
 * this tests are _WRITING_ to the database, there is no rollback
 * 
 * then liquibase is used to initialize tables and basic testdata in
 * the testing database
 * 
 * database settings are configured in {@literal application-<profile>.properties}
 * 
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at>}
 */
public class PersistenceServiceITCase {

    @Configuration
    @EnableAutoConfiguration
    @ComponentScan(basePackages = { "eu.domibus.connector.persistence" })
//    @EntityScan("eu.domibus.connector.persistence.model")
    static class TestConfiguration {
//        @Bean
//        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
//            return new PropertySourcesPlaceholderConfigurer();
//        }
    }

    static ConfigurableApplicationContext APPLICATION_CONTEXT;

    @BeforeClass
    public static void InitClass() {
        SpringApplicationBuilder springAppBuilder = new SpringApplicationBuilder(TestConfiguration.class)
                //.profiles("test", "db_mysql")
                .profiles("test", "db_h2")
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
    public void simpleTest() {
        assertThat(ds).isNotNull();
    }


    @Test
    public void testGetService() {
        DomibusConnectorService epo = persistenceService.getService("EPO");
        assertThat(epo).isNotNull();
    }

    
    @Test
    public void testPersistMessageIntoDatabase() throws PersistenceException {
        MessageDetails messageDetails = new MessageDetails();
        MessageContent messageContent = new MessageContent();
        Message message = new Message(messageDetails, messageContent);
        //MessageDirection messageDirection = MessageDirection.GW_TO_NAT;
        
        persistenceService.persistMessageIntoDatabase(message, MessageDirection.GW_TO_NAT);
        
        //TODO: test db if entity is there!                
    }
    
    
    
    @Test
    @Ignore
    public void testMergeMessageWithDatabase() throws PersistenceException {
        
        MessageDetails messageDetails = new MessageDetails();
        MessageContent messageContent = new MessageContent();
        Message message = new Message(messageDetails, messageContent);
        //MessageDirection messageDirection = MessageDirection.GW_TO_NAT;
        
//        persistenceService.persistMessageIntoDatabase(message, MessageDirection.GW_TO_NAT);
//        
//        persistenceService.mergeMessageWithDatabase(message);
        
    }
    
    @Test(expected=Exception.class)
    public void testMergeMessageWithDatabase_doesNotExistInDatabase() {
        MessageDetails messageDetails = new MessageDetails();
        MessageContent messageContent = new MessageContent();
        Message message = new Message(messageDetails, messageContent);
        
        persistenceService.mergeMessageWithDatabase(message);
    }
    
    @Test
    @Ignore
    public void testPersistEvidenceForMessageIntoDatabase() {
        MessageDetails messageDetails = new MessageDetails();
        MessageContent messageContent = new MessageContent();
        Message message = new Message(messageDetails, messageContent);
        
        byte[] evidence = "hallo Welt".getBytes();
        
        persistenceService.persistEvidenceForMessageIntoDatabase(message, evidence, EvidenceType.DELIVERY);
    }
    
    //TODO: load testdata and do check with testdata
    
    @Test(expected=EmptyResultDataAccessException.class)
    public void testFindMessageByNationalId_doesNotExist_shouldThrowEmptyResultException() {
        String nationalIdString = "TEST1";
        Message findMessageByNationalId = persistenceService.findMessageByNationalId(nationalIdString);
        
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
    public void testGetParty_doesNotExistInDB_shouldBeNull() {       
        DomibusConnectorParty party = persistenceService.getParty("ATEA", "GW");
        assertThat(party).isNull();
    }
    
    @Test
    public void testGetPartyById() {
        DomibusConnectorParty party = persistenceService.getPartyByPartyId("AT");
        assertThat(party).isNotNull();
    }
    
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
