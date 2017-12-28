package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.Action;
import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.MessageConfirmation;
import eu.domibus.connector.domain.MessageDetails;
import eu.domibus.connector.domain.MessageError;
import eu.domibus.connector.domain.Party;
import eu.domibus.connector.domain.Service;
import eu.domibus.connector.domain.enums.EvidenceType;
import eu.domibus.connector.domain.enums.MessageDirection;
import eu.domibus.connector.domain.test.util.DomainCreator;
import eu.domibus.connector.persistence.dao.DomibusConnectorActionDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorEvidenceDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageErrorDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageInfoDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorPartyDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorServiceDao;
import eu.domibus.connector.persistence.model.DomibusConnectorEvidence;
import eu.domibus.connector.persistence.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.model.DomibusConnectorMessageError;
import eu.domibus.connector.persistence.model.DomibusConnectorMessageInfo;
import eu.domibus.connector.persistence.model.test.util.PersistenceEntityCreator;
import static eu.domibus.connector.persistence.model.test.util.PersistenceEntityCreator.createDeliveryEvidence;
import static eu.domibus.connector.persistence.model.test.util.PersistenceEntityCreator.createNonDeliveryEvidence;
import static eu.domibus.connector.persistence.model.test.util.PersistenceEntityCreator.createPartyAT;
import static eu.domibus.connector.persistence.model.test.util.PersistenceEntityCreator.createPartyPKforPartyAT;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import eu.domibus.connector.persistence.service.PersistenceException;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import org.assertj.core.api.Condition;
import static org.hamcrest.CoreMatchers.allOf;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Matchers.eq;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
                .thenReturn(PersistenceEntityCreator.createSimpleDomibusConnectorMessage());
        
        
        Message national1 = domibusConnectorPersistenceService.findMessageByNationalId("national1");        
        assertThat(national1).isNotNull();
        
        assertThat(national1.getDbMessageId()).isEqualTo(47L);
        
    }

    @Test
    public void testFindMessageByEbmsId() {        
        Mockito.when(domibusConnectorMessageDao.findOneByEbmsMessageId(eq("ebms1")))
                .thenReturn(PersistenceEntityCreator.createSimpleDomibusConnectorMessage());
        
        
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
            {PersistenceEntityCreator.createSimpleDomibusConnectorMessage(), 
            PersistenceEntityCreator.createSimpleDomibusConnectorMessage()});
        Mockito.when(domibusConnectorMessageDao.findByConversationId(eq("conversation1")))
                .thenReturn(list);
                
        List<Message> messageList = domibusConnectorPersistenceService.findMessagesByConversationId("conversation1");
        
        assertThat(messageList).hasSize(2);
        assertThat(messageList.get(0)).isOfAnyClassIn(Message.class);
    }
    
    

    @Test
    public void testFindOutgoingUnconfirmedMessages() {
        List<DomibusConnectorMessage> list = Arrays.asList(new DomibusConnectorMessage[]
            {PersistenceEntityCreator.createSimpleDomibusConnectorMessage(), 
            PersistenceEntityCreator.createSimpleDomibusConnectorMessage()});
        
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
        //assertThat(messageList).are(new IsAssignableFromCondition(Message.class));
    }

    @Test
    public void testFindOutgoingMessagesNotRejectedAndWithoutDelivery() {
        List<DomibusConnectorMessage> list = Arrays.asList(new DomibusConnectorMessage[]
            {PersistenceEntityCreator.createSimpleDomibusConnectorMessage(), 
            PersistenceEntityCreator.createSimpleDomibusConnectorMessage()});
        
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
            {PersistenceEntityCreator.createSimpleDomibusConnectorMessage(), 
            PersistenceEntityCreator.createSimpleDomibusConnectorMessage()});
        
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
            {PersistenceEntityCreator.createSimpleDomibusConnectorMessage(), 
            PersistenceEntityCreator.createSimpleDomibusConnectorMessage()});
        
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
                
        Message message = DomainCreator.createSimpleTestMessage();
        message.setDbMessageId(78L);
        domibusConnectorPersistenceService.confirmMessage(message);        
        Mockito.verify(domibusConnectorMessageDao, Mockito.times(1)).confirmMessage(eq(78L));        
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConfirmMessage_noMessageIdSet_shouldThrowIllegalArgumentException() {
        Message message = DomainCreator.createSimpleTestMessage();
        message.setDbMessageId(null);
        domibusConnectorPersistenceService.confirmMessage(message);                
    }
    
    @Test(expected=RuntimeException.class)
    public void testConfirmMessage_confirmFails_shouldThrowException() {
        Mockito.when(domibusConnectorMessageDao.confirmMessage(any(Long.class)))
                .thenReturn(0); //tell service nothing has been updated!                
        Message message = DomainCreator.createSimpleTestMessage();
        message.setDbMessageId(78L);
        domibusConnectorPersistenceService.confirmMessage(message);        
        Mockito.verify(domibusConnectorMessageDao, Mockito.times(1)).confirmMessage(eq(78L));        
    }

    
    
    
    @Test
    public void testRejectMessage() {
        Mockito.when(domibusConnectorMessageDao.rejectMessage(any(Long.class)))
                .thenReturn(1);
                
        Message message = DomainCreator.createSimpleTestMessage();
        message.setDbMessageId(78L);
        domibusConnectorPersistenceService.rejectMessage(message);        
        Mockito.verify(domibusConnectorMessageDao, Mockito.times(1)).rejectMessage(eq(78L));    
    }
    
        
    @Test(expected=IllegalArgumentException.class)
    public void testRejectMessage_noMessageIdSet_shouldThrowIllegalArgumentException() {
        Message message = DomainCreator.createSimpleTestMessage();
        message.setDbMessageId(null);
        domibusConnectorPersistenceService.rejectMessage(message);                
    }
    
    @Test(expected=RuntimeException.class)
    public void testRejectMessage_confirmFails_shouldThrowException() {
        Mockito.when(domibusConnectorMessageDao.rejectMessage(any(Long.class)))
                .thenReturn(0); //tell service nothing has been updated!                
        Message message = DomainCreator.createSimpleTestMessage();
        message.setDbMessageId(78L);
        domibusConnectorPersistenceService.rejectMessage(message);        
        Mockito.verify(domibusConnectorMessageDao, Mockito.times(1)).rejectMessage(eq(78L));        
    }
    
    
    @Test
    public void testPersistMessageIntoDatabase() throws PersistenceException {
        Message message = DomainCreator.createSimpleTestMessage();
        
        message.setDbMessageId(null);
        
        MessageConfirmation messageConfirmation = new MessageConfirmation();
        messageConfirmation.setEvidence("MYEVIDENCE".getBytes());
        messageConfirmation.setEvidenceType(EvidenceType.DELIVERY);
        
        message.addConfirmation(messageConfirmation);
        
        message.getMessageDetails().setEbmsMessageId("ebmsid");
        message.getMessageDetails().setConversationId("conversation1");
        
        
        
        Mockito.when(domibusConnectorMessageDao.save(any(DomibusConnectorMessage.class)))
                .then(new Answer<DomibusConnectorMessage>() {
                    @Override
                    public DomibusConnectorMessage answer(InvocationOnMock invocation) throws Throwable {
                        DomibusConnectorMessage message = invocation.getArgumentAt(0, DomibusConnectorMessage.class);
                        
                        //TODO: check mapping
                        assertThat(message.getEbmsMessageId()).isEqualTo("ebmsid");
                        assertThat(message.getConversationId()).isEqualTo("conversation1");
                        
                        return message;
                    }                    
                });
        
        
        
        domibusConnectorPersistenceService.persistMessageIntoDatabase(message, MessageDirection.NAT_TO_GW);

        Mockito.verify(domibusConnectorMessageDao, Mockito.times(1)).save(any(DomibusConnectorMessage.class));
    }

    @Test
    public void testMergeMessageWithDatabase() throws PersistenceException {
        Message message = DomainCreator.createSimpleTestMessage();
        message.setDbMessageId(47L);

        MessageDetails messageDetails = message.getMessageDetails();
                
        messageDetails.setFinalRecipient("finalRecipient");
        messageDetails.setFromParty(DomainCreator.createPartyAT());       
        messageDetails.setOriginalSender("original1");
        messageDetails.setRefToMessageId("reftomessageid");
        messageDetails.setService(DomainCreator.createServiceEPO());
        messageDetails.setToParty(DomainCreator.createPartyAT());
        messageDetails.setAction(DomainCreator.createActionForm_A());
        
        DomibusConnectorMessage pMessage = PersistenceEntityCreator.createSimpleDomibusConnectorMessage();
        pMessage.setId(47L);
        
        Mockito.when(domibusConnectorMessageInfoDao.save(any(DomibusConnectorMessageInfo.class)))
                .then(new Answer<DomibusConnectorMessageInfo>() {
                    @Override
                    public DomibusConnectorMessageInfo answer(InvocationOnMock invocation) throws Throwable {
                        DomibusConnectorMessageInfo messageInfo = invocation.getArgumentAt(0, DomibusConnectorMessageInfo.class);     
                                                
                        assertThat(messageInfo.getFinalRecipient()).isEqualTo("finalRecipient");
                        assertThat(messageInfo.getOriginalSender()).isEqualTo("original1");   
                        //check action
                        assertThat(messageInfo.getAction()).isNotNull();
                        assertThat(messageInfo.getAction().isPdfRequired()).isTrue();
                        assertThat(messageInfo.getAction().getAction()).isEqualTo("Form_A");
                        //check service
                        assertThat(messageInfo.getService()).isNotNull();
                        assertThat(messageInfo.getService().getService()).isEqualTo("EPO");
                        assertThat(messageInfo.getService().getServiceType()).isEqualTo(DomainCreator.createServiceEPO().getServiceType());
                        //check party from
                        assertThat(messageInfo.getFrom()).isNotNull();
                        assertThat(messageInfo.getFrom().getPartyId()).isEqualTo("AT");
                        assertThat(messageInfo.getFrom().getRole()).isEqualTo("GW");
                        assertThat(messageInfo.getFrom().getPartyIdType()).isEqualTo(createPartyAT().getPartyIdType());
                        //check party to
                        assertThat(messageInfo.getTo()).isNotNull();
                        assertThat(messageInfo.getTo().getPartyId()).isEqualTo("AT");
                        assertThat(messageInfo.getTo().getRole()).isEqualTo("GW");
                        assertThat(messageInfo.getTo().getPartyIdType()).isEqualTo(createPartyAT().getPartyIdType());
                        
                        return messageInfo;
                    }                
                });
        
        Mockito.when(domibusConnectorMessageDao.findOne(47L)).thenReturn(pMessage);
        
        domibusConnectorPersistenceService.mergeMessageWithDatabase(message);
        
        //TODO: test mapping back to db
        Mockito.verify(domibusConnectorMessageDao, Mockito.times(1)).findOne(eq(47L));
        //Mockito.verify(domibusConnectorMessageDao, Mockito.times(1)).save(any(DomibusConnectorMessage.class));
        
        Mockito.verify(domibusConnectorMessageInfoDao, Mockito.times(1)).save(any(DomibusConnectorMessageInfo.class));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testMergeMessageWithDatabase_messageIdNotSet_shouldThrowIllegalArgumentException() throws PersistenceException {
        Message message = DomainCreator.createSimpleTestMessage();
        message.setDbMessageId(null); //message id not set!
        
        domibusConnectorPersistenceService.mergeMessageWithDatabase(message);
    }
    
    @Test(expected=PersistenceException.class)
    public void testMergeMessageWithDatabase_doesNotExistInDB_shouldThrowPersistenceException() throws PersistenceException {
        Message message = DomainCreator.createSimpleTestMessage();
        message.setDbMessageId(89L); //does not exist in db, because dao will return null anyway is not mocked!
        
        domibusConnectorPersistenceService.mergeMessageWithDatabase(message);
    }

    @Test
    public void testSetMessageDeliveredToGateway() {
        Message message = DomainCreator.createSimpleTestMessage();
        message.setDbMessageId(47L);

        domibusConnectorPersistenceService.setMessageDeliveredToGateway(message);
        
        Mockito.verify(domibusConnectorMessageDao, Mockito.times(1)).setMessageDeliveredToGateway(eq(47L));
    }

    @Test
    public void testSetMessageDeliveredToNationalSystem() {
        Message message = DomainCreator.createSimpleTestMessage();
        message.setDbMessageId(47L);

        domibusConnectorPersistenceService.setMessageDeliveredToNationalSystem(message);
        
        Mockito.verify(domibusConnectorMessageDao, Mockito.times(1)).setMessageDeliveredToBackend(eq(47L));
    }

    /**
     * Evidence related tests
     */
    

    @Test    
    public void testSetEvidenceDeliveredToGateway() throws PersistenceException {
        DomainCreator.createMessageDeliveryConfirmation();
        
        Message message = DomainCreator.createSimpleTestMessage();
        message.setDbMessageId(47L);
        
        List<DomibusConnectorEvidence> evidences = Arrays.asList(new DomibusConnectorEvidence[]{
            createDeliveryEvidence(),
            createNonDeliveryEvidence()
        });
        
        Mockito.when(domibusConnectorEvidenceDao.findEvidencesForMessage(eq(47L))).thenReturn(evidences);
        Mockito.when(domibusConnectorMessageDao.findOne(eq(47L))).thenReturn(PersistenceEntityCreator.createSimpleDomibusConnectorMessage());
        
        domibusConnectorPersistenceService.setEvidenceDeliveredToGateway(message, EvidenceType.DELIVERY);
                        
        Mockito.verify(this.domibusConnectorMessageDao, Mockito.times(1)).save(any(DomibusConnectorMessage.class));
        Mockito.verify(this.domibusConnectorEvidenceDao, Mockito.times(1)).setDeliveredToGateway(eq(13L));
        
    }

    @Test
    public void testSetEvidenceDeliveredToNationalSystem() throws PersistenceException {
        DomainCreator.createMessageDeliveryConfirmation();
        
        Message message = DomainCreator.createSimpleTestMessage();
        message.setDbMessageId(47L);
        
        List<DomibusConnectorEvidence> evidences = Arrays.asList(new DomibusConnectorEvidence[]{
            createDeliveryEvidence(),
            createNonDeliveryEvidence()
        });
        
        Mockito.when(domibusConnectorEvidenceDao.findEvidencesForMessage(eq(47L))).thenReturn(evidences);
        Mockito.when(domibusConnectorMessageDao.findOne(eq(47L))).thenReturn(PersistenceEntityCreator.createSimpleDomibusConnectorMessage());
                
        domibusConnectorPersistenceService.setEvidenceDeliveredToNationalSystem(message, EvidenceType.DELIVERY);
                        
        Mockito.verify(this.domibusConnectorMessageDao, Mockito.times(1)).save(any(DomibusConnectorMessage.class));
        Mockito.verify(this.domibusConnectorEvidenceDao, Mockito.times(1)).setDeliveredToBackend(eq(13L));
    }

    @Test    
    public void testPersistEvidenceForMessageIntoDatabase() {
        Message message = DomainCreator.createSimpleTestMessage();
        message.setDbMessageId(47L);
        
        DomibusConnectorMessage dbMessage = PersistenceEntityCreator.createSimpleDomibusConnectorMessage();
        
        byte[] evidence = "EVIDENCE1".getBytes();
        
        Mockito.when(this.domibusConnectorMessageDao.findOne(eq(47L))).thenReturn(dbMessage);
        
        Mockito.when(this.domibusConnectorEvidenceDao.save(any(DomibusConnectorEvidence.class)))
                .thenAnswer(new Answer<DomibusConnectorEvidence>() {
                    @Override
                    public DomibusConnectorEvidence answer(InvocationOnMock invocation) throws Throwable {
                        DomibusConnectorEvidence evidence = invocation.getArgumentAt(0, DomibusConnectorEvidence.class);
                        assertThat(evidence.getDeliveredToGateway()).isNull();
                        assertThat(evidence.getDeliveredToNationalSystem()).isNull();
                        assertThat(evidence.getType()).isEqualTo(eu.domibus.connector.persistence.model.enums.EvidenceType.DELIVERY);
                        assertThat(evidence.getMessage()).isNotNull();
                        return evidence;
                    }
        });
        
        domibusConnectorPersistenceService.persistEvidenceForMessageIntoDatabase(message, evidence, EvidenceType.DELIVERY);
        
        Mockito.verify(this.domibusConnectorEvidenceDao, Mockito.times(1)).save(any(DomibusConnectorEvidence.class));
    }


    /**
     * Message Error related
     */
    @Test    
    public void testPersistMessageError() {        
        MessageError messageError = DomainCreator.createMessageError();
        
        DomibusConnectorMessage dbMessage = PersistenceEntityCreator.createSimpleDomibusConnectorMessage();
        Mockito.when(this.domibusConnectorMessageDao.findOne(eq(47L))).thenReturn(dbMessage);
        
        Mockito.when(this.domibusConnectorMessageErrorDao.save(any(DomibusConnectorMessageError.class)))
                .thenAnswer(new Answer<DomibusConnectorMessageError>() {
                    @Override
                    public DomibusConnectorMessageError answer(InvocationOnMock invocation) throws Throwable {
                        DomibusConnectorMessageError msgError = invocation.getArgumentAt(0, DomibusConnectorMessageError.class);
                        assertThat(msgError.getDetailedText()).isEqualTo("error detail message");
                        assertThat(msgError.getErrorSource()).isEqualTo("error source");
                        assertThat(msgError.getErrorMessage()).isEqualTo("error message");
                        return msgError;
                    }
        });
        
        domibusConnectorPersistenceService.persistMessageError(messageError);
                
        Mockito.verify(this.domibusConnectorMessageErrorDao, Mockito.times(1)).save(any(DomibusConnectorMessageError.class));
        
        
    }

    @Test
    public void testPersistMessageErrorFromException() {
        Message message = DomainCreator.createSimpleTestMessage();
        message.setDbMessageId(47L);
        Exception ex = new RuntimeException("hallo welt!");
        Class source = Integer.class;
        
        DomibusConnectorMessage dbMessage = PersistenceEntityCreator.createSimpleDomibusConnectorMessage();
        Mockito.when(this.domibusConnectorMessageDao.findOne(eq(47L))).thenReturn(dbMessage);
        
        domibusConnectorPersistenceService.persistMessageErrorFromException(message, ex, source);
                
        Mockito.verify(this.domibusConnectorMessageErrorDao, Mockito.times(1)).save(any(DomibusConnectorMessageError.class));
    }
    
    @Test(expected=RuntimeException.class)
    public void testPersistMessageErrorFromException_messageHasNoId_shouldThrowException() {
        Message message = DomainCreator.createSimpleTestMessage();
        message.setDbMessageId(null);
        Exception ex = new RuntimeException("hallo welt!");
        Class source = Integer.class;
        
        DomibusConnectorMessage dbMessage = PersistenceEntityCreator.createSimpleDomibusConnectorMessage();
        Mockito.when(this.domibusConnectorMessageDao.findOne(eq(47L))).thenReturn(dbMessage);
        
        domibusConnectorPersistenceService.persistMessageErrorFromException(message, ex, source);
                
        Mockito.verify(this.domibusConnectorMessageErrorDao, Mockito.times(1)).save(any(DomibusConnectorMessageError.class));
    }

    @Test
    public void testGetMessageErrors() throws Exception {
        Message message = DomainCreator.createSimpleTestMessage();
        message.setDbMessageId(47L);
        
        List<DomibusConnectorMessageError> errorList = Arrays.asList( new DomibusConnectorMessageError[] {
            PersistenceEntityCreator.createMessageError(),
            PersistenceEntityCreator.createMessageError()     
        });
        
        Mockito.when(this.domibusConnectorMessageErrorDao.findByMessage(eq(47L))).thenReturn(errorList);
                
        List<MessageError> messageErrors = domibusConnectorPersistenceService.getMessageErrors(message);
        
        assertThat(messageErrors).hasSize(2);
    }
    
    @Test
    public void testGetMessageErrors_noResult_shouldReturnEmptyList() throws Exception {
        Message message = DomainCreator.createSimpleTestMessage();
        message.setDbMessageId(47L);
          
        List<MessageError> messageErrors = domibusConnectorPersistenceService.getMessageErrors(message);
        
        assertThat(messageErrors).hasSize(0);
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
    public void testGetPartyByPartyId() {
        
        Mockito.when(this.domibusConnectorPartyDao.findOneByPartyId(eq("AT")))
                .thenReturn(createPartyAT());        
        
        Party party = domibusConnectorPersistenceService.getPartyByPartyId("AT");
        
        assertThat(party).isNotNull();
        assertThat(party.getPartyId()).isEqualTo("AT");
        assertThat(party.getRole()).isEqualTo("GW");
        assertThat(party.getPartyIdType()).isEqualTo("urn:oasis:names:tc:ebcore:partyid-type:iso3166-1");
        
    }



    private class IsAssignableFromCondition extends Condition {

        private final Class clazz;

        public IsAssignableFromCondition(Class clazz) {
            this.clazz = clazz;
        }
        
        @Override
        public boolean matches(Object value) {
            if (value == null) return false;
            return clazz.isAssignableFrom(value.getClass());
        }
    
    }


    
}
