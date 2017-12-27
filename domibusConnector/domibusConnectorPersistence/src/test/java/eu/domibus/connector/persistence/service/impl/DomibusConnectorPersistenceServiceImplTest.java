/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.enums.EvidenceType;
import eu.domibus.connector.domain.test.util.TestMessageCreator;
import eu.domibus.connector.persistence.dao.DomibusConnectorActionDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorEvidenceDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageErrorDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageInfoDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorPartyDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorServiceDao;
import eu.domibus.connector.persistence.model.test.util.PersistenceMessageCreator;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Matchers.eq;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.mockito.Mockito;

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
        
        Message ebms1 = domibusConnectorPersistenceService.findMessageByEbmsId("ebms1");
        assertThat(ebms1).isNotNull();
    }

    @Test
    public void testFindMessagesByConversationId() {
        List<Message> messageList = domibusConnectorPersistenceService.findMessagesByConversationId("conversation1");
        
        assertThat(messageList).hasSize(2);
        assertThat(messageList.get(0)).isOfAnyClassIn(Message.class);
    }

    @Test
    @Ignore //not finished yet
    public void testFindOutgoingUnconfirmedMessages() {
    }

    @Test
    @Ignore //not finished yet
    public void testFindOutgoingMessagesNotRejectedAndWithoutDelivery() {
    }

    @Test
    @Ignore //not finished yet
    public void testFindOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD() {
    }

    @Test
    @Ignore //not finished yet
    public void testFindIncomingUnconfirmedMessages() {
    }

    @Test
    @Ignore //not finished yet
    public void testConfirmMessage() {
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
    @Ignore //not finished yet
    public void testGetAction() {
    }

    @Test
    @Ignore //not finished yet
    public void testGetRelayREMMDAcceptanceRejectionAction() {
    }
    
    @Test
    @Ignore //not finished yet
    public void testGetDeliveryNonDeliveryToRecipientAction() {
    }

    @Test
    @Ignore //not finished yet
    public void testGetRetrievalNonRetrievalToRecipientAction() {
    }
    
    
    /**
     * Service
     */
    
    @Test
    @Ignore //not finished yet
    public void testGetService() {
    }

    @Test
    @Ignore //not finished yet
    public void testGetParty() {
    }

    @Test
    @Ignore //not finished yet
    public void testGetPartyByPartyId() {
    }



    @Test
    @Ignore //not finished yet
    public void testGetRelayREMMDFailure() {
    }


    
}
