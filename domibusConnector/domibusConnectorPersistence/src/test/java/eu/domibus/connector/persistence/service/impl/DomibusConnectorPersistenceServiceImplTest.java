/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.Action;
import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.Party;
import eu.domibus.connector.domain.Service;
import eu.domibus.connector.domain.enums.EvidenceType;
import eu.domibus.connector.domain.test.util.DomainMessageCreator;
import eu.domibus.connector.persistence.dao.DomibusConnectorActionDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorEvidenceDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageErrorDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageInfoDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorPartyDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorServiceDao;
import eu.domibus.connector.persistence.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.model.test.util.PersistenceEntityCreator;
import static eu.domibus.connector.persistence.model.test.util.PersistenceEntityCreator.createPartyAT;
import static eu.domibus.connector.persistence.model.test.util.PersistenceEntityCreator.createPartyPKforPartyAT;
import eu.domibus.connector.persistence.model.test.util.PersistenceMessageCreator;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Matchers.eq;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
public class DomibusConnectorPersistenceServiceImplTest {
    
    @Mock
    DomibusConnectorActionDao domibusConnectorActionDao;
    
    @Mock
    DomibusConnectorEvidenceDao domibusConnectorEvidenceDao;
    
    @Mock
    DomibusConnectorMessageDao domibusConnectorMessageDao;
    
    @Mock
    DomibusConnectorMessageErrorDao domibusConnectorMessageErrorDao;
    
    @Mock
    DomibusConnectorMessageInfoDao domibusConnectorMessageInfoDao;
    
    @Mock
    DomibusConnectorPartyDao domibusConnectorPartyDao;
    
    @Mock
    DomibusConnectorServiceDao domibusConnectorServiceDao;
    

    DomibusConnectorPersistenceService domibusConnectorPersistenceService;
    
    
    public DomibusConnectorPersistenceServiceImplTest() {
    }
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        DomibusConnectorPersistenceServiceImpl persistenceService = new DomibusConnectorPersistenceServiceImpl();
        persistenceService.setActionDao(domibusConnectorActionDao);
        persistenceService.setEvidenceDao(domibusConnectorEvidenceDao);
        persistenceService.setMessageDao(domibusConnectorMessageDao);
        persistenceService.setMessageErrorDao(domibusConnectorMessageErrorDao);
        persistenceService.setMessageInfoDao(domibusConnectorMessageInfoDao);
        persistenceService.setPartyDao(domibusConnectorPartyDao);
        persistenceService.setServiceDao(domibusConnectorServiceDao);
                
        this.domibusConnectorPersistenceService = persistenceService;
    }


    /**
     * Message related
     */
    @Test
    public void testFindMessageByNationalId() {
        Mockito.when(domibusConnectorMessageDao.findOneByNationalMessageId(eq("national1")))
                .thenReturn(PersistenceMessageCreator.createSimpleDomibusConnectorMessage());
        
        
        Message national1 = domibusConnectorPersistenceService.findMessageByNationalId("national1");        
        assertThat(national1).isNotNull();
        
        assertThat(national1.getDbMessageId()).isEqualTo(78L);
        
    }

    @Test
    public void testFindMessageByEbmsId() {        
        Mockito.when(domibusConnectorMessageDao.findOneByEbmsMessageId(eq("ebms1")))
                .thenReturn(PersistenceMessageCreator.createSimpleDomibusConnectorMessage());
        
        
        Message ebms1 = domibusConnectorPersistenceService.findMessageByEbmsId("ebms1");
        assertThat(ebms1).isNotNull();
    }

    @Test
    public void testFindMessagesByConversationId_noResults_shouldReturnEmptyList() {
        List<Message> messageList = domibusConnectorPersistenceService.findMessagesByConversationId("conversation1");        
        assertThat(messageList).hasSize(0);
    }
    
    @Test
    public void testFindMessagesByConversationId() {
        List<DomibusConnectorMessage> list = Arrays.asList(new DomibusConnectorMessage[]
            {PersistenceMessageCreator.createSimpleDomibusConnectorMessage(), 
            PersistenceMessageCreator.createSimpleDomibusConnectorMessage()});
        Mockito.when(domibusConnectorMessageDao.findByConversationId(eq("conversation1")))
                .thenReturn(list);
                
        List<Message> messageList = domibusConnectorPersistenceService.findMessagesByConversationId("conversation1");
        
        assertThat(messageList).hasSize(2);
        assertThat(messageList.get(0)).isOfAnyClassIn(Message.class);
    }
    
    

    @Test
    public void testFindOutgoingUnconfirmedMessages() {
        List<DomibusConnectorMessage> list = Arrays.asList(new DomibusConnectorMessage[]
            {PersistenceMessageCreator.createSimpleDomibusConnectorMessage(), 
            PersistenceMessageCreator.createSimpleDomibusConnectorMessage()});
        
        Mockito.when(domibusConnectorMessageDao.findOutgoingUnconfirmedMessages())
                .thenReturn(list);
        
        List<Message> messageList = domibusConnectorPersistenceService.findOutgoingUnconfirmedMessages();        
        assertThat(messageList).hasSize(2);
    }
    
    @Test
    public void testFindOutgoingUnconfirmedMessages_noResults_shouldReturnEmptyList() {
        List<Message> messageList = domibusConnectorPersistenceService.findOutgoingUnconfirmedMessages();        
        assertThat(messageList).hasSize(0);
        //assertThat(messageList).are(new Condition<>(Object::getClass::isAssignableFrom, Message.class));
        
    }

    @Test
    public void testFindOutgoingMessagesNotRejectedAndWithoutDelivery() {
        List<DomibusConnectorMessage> list = Arrays.asList(new DomibusConnectorMessage[]
            {PersistenceMessageCreator.createSimpleDomibusConnectorMessage(), 
            PersistenceMessageCreator.createSimpleDomibusConnectorMessage()});
        
        Mockito.when(domibusConnectorMessageDao.findOutgoingMessagesNotRejectedAndWithoutDelivery())
                .thenReturn(list);
        
        List<Message> messageList = domibusConnectorPersistenceService.findOutgoingMessagesNotRejectedAndWithoutDelivery();        
        assertThat(messageList).hasSize(2);
    }
    
    @Test    
    public void testFindOutgoingMessagesNotRejectedAndWithoutDelivery_noResults_shouldReturnEmptyList() {
        List<Message> messageList = domibusConnectorPersistenceService.findOutgoingMessagesNotRejectedAndWithoutDelivery();        
        assertThat(messageList).hasSize(0);
    }

    @Test
    public void testFindOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD() {
        List<DomibusConnectorMessage> list = Arrays.asList(new DomibusConnectorMessage[]
            {PersistenceMessageCreator.createSimpleDomibusConnectorMessage(), 
            PersistenceMessageCreator.createSimpleDomibusConnectorMessage()});
        
        Mockito.when(domibusConnectorMessageDao.findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD())
                .thenReturn(list);
        
        List<Message> messageList = domibusConnectorPersistenceService.findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD();        
        assertThat(messageList).hasSize(2);        
    }
    
    @Test
    public void testFindOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD_noResults_shouldReturnEmptyList() {
        List<Message> messageList = domibusConnectorPersistenceService.findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD();        
        assertThat(messageList).hasSize(0);
    }

    @Test
    public void testFindIncomingUnconfirmedMessages() {
        List<DomibusConnectorMessage> list = Arrays.asList(new DomibusConnectorMessage[]
            {PersistenceMessageCreator.createSimpleDomibusConnectorMessage(), 
            PersistenceMessageCreator.createSimpleDomibusConnectorMessage()});
        
        Mockito.when(domibusConnectorMessageDao.findIncomingUnconfirmedMessages())
                .thenReturn(list);
        
        List<Message> messageList = domibusConnectorPersistenceService.findIncomingUnconfirmedMessages();        
        assertThat(messageList).hasSize(2);      
    }
    
    @Test
    public void testFindIncomingUnconfirmedMessages_noResults_shouldReturnEmptyList() {
        List<Message> messageList = domibusConnectorPersistenceService.findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD();        
        assertThat(messageList).hasSize(0);        
    }

    @Test
    public void testConfirmMessage() {
        Mockito.when(domibusConnectorMessageDao.confirmMessage(any(Long.class)))
                .thenReturn(1);
                
        Message message = DomainMessageCreator.createSimpleTestMessage();
        message.setDbMessageId(78L);
        domibusConnectorPersistenceService.confirmMessage(message);        
        Mockito.verify(domibusConnectorMessageDao, Mockito.times(1)).confirmMessage(eq(78L));        
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConfirmMessage_noMessageIdSet_shouldThrowIllegalArgumentException() {
        Message message = DomainMessageCreator.createSimpleTestMessage();
        message.setDbMessageId(null);
        domibusConnectorPersistenceService.confirmMessage(message);                
    }
    
    @Test(expected=RuntimeException.class)
    public void testConfirmMessage_confirmFails_shouldThrowException() {
        Mockito.when(domibusConnectorMessageDao.confirmMessage(any(Long.class)))
                .thenReturn(0); //tell service nothing has been updated!                
        Message message = DomainMessageCreator.createSimpleTestMessage();
        message.setDbMessageId(78L);
        domibusConnectorPersistenceService.confirmMessage(message);        
        Mockito.verify(domibusConnectorMessageDao, Mockito.times(1)).confirmMessage(eq(78L));        
    }

    @Test
    @Ignore //not finished yet
    public void testRejectMessage() {
    }
    
    @Test
    @Ignore //not finished yet
    public void testPersistMessageIntoDatabase() {
        
        
    }

    @Test
    @Ignore //not finished yet
    public void testMergeMessageWithDatabase() {
    }

    @Test
    @Ignore //not finished yet
    public void testSetMessageDeliveredToGateway() {
    }

    @Test
    @Ignore //not finished yet
    public void testSetMessageDeliveredToNationalSystem() {
    }

    /**
     * Evidence related
     */
    @Test
    @Ignore //not finished yet
    public void testSetEvidenceDeliveredToGateway() {
    }

    @Test
    @Ignore //not finished yet
    public void testSetEvidenceDeliveredToNationalSystem() {
    }

    @Test
    @Ignore //not finished yet
    public void testPersistEvidenceForMessageIntoDatabase() {
    }


    /**
     * Message Error related
     */
    @Test
    @Ignore //not finished yet
    public void testPersistMessageError() {
    }

    @Test
    @Ignore //not finished yet
    public void testPersistMessageErrorFromException() {
    }

    @Test
    @Ignore //not finished yet
    public void testGetMessageErrors() throws Exception {
    }

    /**
     * Action
     */
    @Test
    public void testGetAction() {
        
        Mockito.when(this.domibusConnectorActionDao.findOne(eq("action1")))
                .thenReturn(PersistenceEntityCreator.createAction());
        
        Action action = domibusConnectorPersistenceService.getAction("action1");
        
        assertThat(action).isNotNull();
        assertThat(action.getAction()).isEqualTo("action1");
    }

    @Test
    public void testGetRelayREMMDAcceptanceRejectionAction() {
        Mockito.when(this.domibusConnectorActionDao.findOne(eq("RelayREMMDAcceptanceRejection")))
                .thenReturn(PersistenceEntityCreator.createRelayREMMDAcceptanceRejectionAction());
        
        Action action = domibusConnectorPersistenceService.getRelayREMMDAcceptanceRejectionAction();
        
        assertThat(action).isNotNull();
        assertThat(action.getAction()).isEqualTo("RelayREMMDAcceptanceRejection");
    }
    
    @Test
    public void testGetDeliveryNonDeliveryToRecipientAction() {
        Mockito.when(this.domibusConnectorActionDao.findOne(eq("DeliveryNonDeliveryToRecipient")))
                .thenReturn(PersistenceEntityCreator.createDeliveryNonDeliveryToRecipientAction());
                
        Action action = domibusConnectorPersistenceService.getDeliveryNonDeliveryToRecipientAction();
        
        assertThat(action).isNotNull();
        assertThat(action.getAction()).isEqualTo("DeliveryNonDeliveryToRecipient");
    }

    @Test
    public void testGetRetrievalNonRetrievalToRecipientAction() {

        Mockito.when(this.domibusConnectorActionDao.findOne(eq("RetrievalNonRetrievalToRecipient")))
                .thenReturn(PersistenceEntityCreator.createRetrievalNonRetrievalToRecipientAction());
                
        Action action = domibusConnectorPersistenceService.getRetrievalNonRetrievalToRecipientAction();
        
        assertThat(action).isNotNull();
        assertThat(action.getAction()).isEqualTo("RetrievalNonRetrievalToRecipient");
    }
    
    @Test
    public void testGetRelayREMMDFailure() {        
        Mockito.when(this.domibusConnectorActionDao.findOne(eq("RelayREMMDFailure")))
                .thenReturn(PersistenceEntityCreator.createRelayREMMDFailureAction());
                
        Action action = domibusConnectorPersistenceService.getRelayREMMDFailure();
        
        assertThat(action).isNotNull();
        assertThat(action.getAction()).isEqualTo("RelayREMMDFailure");
    }
    
    /**
     * Service
     */    
    @Test    
    public void testGetService() {
        Mockito.when(this.domibusConnectorServiceDao.findOne(eq("EPO")))
                .thenReturn(PersistenceEntityCreator.createServiceEPO());
        
        Service service = domibusConnectorPersistenceService.getService("EPO");
        
        assertThat(service).isNotNull();
        assertThat(service.getService()).isEqualTo("EPO");
        assertThat(service.getServiceType()).isEqualTo("urn:e-codex:services:");
    }

    @Test
    public void testGetParty() {
        
        Mockito.when(this.domibusConnectorPartyDao.findOne(eq(createPartyPKforPartyAT())))
                .thenReturn(createPartyAT());
        
        Party party = domibusConnectorPersistenceService.getParty("AT", "GW");
        
        assertThat(party).isNotNull();
        assertThat(party.getPartyId()).isEqualTo("AT");
        assertThat(party.getRole()).isEqualTo("GW");
        assertThat(party.getPartyIdType()).isEqualTo("urn:oasis:names:tc:ebcore:partyid-type:iso3166-1");
    }

    @Test
    @Ignore //not finished yet
    public void testGetPartyByPartyId() {
    }






    
}
