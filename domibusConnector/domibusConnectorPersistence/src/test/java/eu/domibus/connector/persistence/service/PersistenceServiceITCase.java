package eu.domibus.connector.persistence.service;



import eu.domibus.connector.domain.Action;
import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.MessageContent;
import eu.domibus.connector.domain.MessageDetails;
import eu.domibus.connector.domain.Party;
import eu.domibus.connector.domain.Service;
import eu.domibus.connector.domain.enums.EvidenceType;
import eu.domibus.connector.domain.enums.MessageDirection;
import eu.domibus.connector.domain.test.util.DomainCreator;
import eu.domibus.connector.persistence.dao.PDomibusConnectorRepositories;
import eu.domibus.connector.persistence.model.PDomibusConnectorPersistenceModel;
import eu.domibus.connector.persistence.spring.DomibusConnectorPersistenceContext;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
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
 * database settings are configured in {@literal application-<profile>.properties}
 * 
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at>}
 */
public class PersistenceServiceITCase {

    @SpringBootApplication(scanBasePackages={"eu.domibus.connector"})
//    @Import({DomibusConnectorPersistenceContext.class})
//    @EntityScan(basePackageClasses={PDomibusConnectorPersistenceModel.class})
//    @EnableJpaRepositories(basePackageClasses = {PDomibusConnectorRepositories.class} )
//    @EnableTransactionManagement
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
        Service epo = persistenceService.getService("EPO");
        assertThat(epo).isNotNull();
    }

    
    @Test
    public void testPersistMessageIntoDatabase() throws PersistenceException {
//        MessageDetails messageDetails = new MessageDetails();
//        MessageContent messageContent = new MessageContent();
        Message message = DomainCreator.createSimpleTestMessage();
        message.setDbMessageId(null);
        //MessageDirection messageDirection = MessageDirection.GW_TO_NAT;
        
        
        
        persistenceService.persistMessageIntoDatabase(message, MessageDirection.GW_TO_NAT);
        
        //TODO: test db if entity is there!                
    }
    
    
    
    @Test
    @Ignore
    public void testMergeMessageWithDatabase() {
        
        MessageDetails messageDetails = new MessageDetails();
        MessageContent messageContent = new MessageContent();
        Message message = new Message(messageDetails, messageContent);
        //MessageDirection messageDirection = MessageDirection.GW_TO_NAT;
        
//        persistenceService.persistMessageIntoDatabase(message, MessageDirection.GW_TO_NAT);
//        
//        persistenceService.mergeMessageWithDatabase(message);
        
    }
    
    @Test(expected=PersistenceException.class)
    public void testMergeMessageWithDatabase_doesNotExistInDatabase() throws PersistenceException {
        MessageDetails messageDetails = new MessageDetails();
        MessageContent messageContent = new MessageContent();
        Message message = new Message(messageDetails, messageContent);
        message.setDbMessageId(48L);
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
    @Ignore
    public void testFindMessageByNationalId_doesNotExist_shouldThrowEmptyResultException() {
        String nationalIdString = "TEST1";
        Message findMessageByNationalId = persistenceService.findMessageByNationalId(nationalIdString);
        
    }
    
    @Test
    public void testGetAction_doesNotExistInDb_retShouldBeNull() {
        Action action = persistenceService.getAction("DOESNOTEXIST");
        assertThat(action).as("should be null beacause does not exist in db").isNull();
    }
    
    @Test
//    @Ignore
    public void testGetAction() {
        Action action = persistenceService.getAction("Form_A");
        assertThat(action).isNotNull();
    }
    
    @Test
//    @Ignore
    public void testGetParty() {       
        Party party = persistenceService.getParty("AT", "GW");
        assertThat(party).isNotNull();
    }
    
    @Test
//    @Ignore
    public void testGetParty_doesNotExistInDB_retShouldBeNull() {       
        Party party = persistenceService.getParty("ATEA", "GW");
        assertThat(party).isNull();
    }
    
    @Test
//    @Ignore
    public void testGetPartyById() {
        Party party = persistenceService.getPartyByPartyId("AT");
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
