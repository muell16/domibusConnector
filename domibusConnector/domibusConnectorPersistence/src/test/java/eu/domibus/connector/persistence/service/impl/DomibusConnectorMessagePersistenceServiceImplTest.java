package eu.domibus.connector.persistence.service.impl;


import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageContent;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.domain.test.util.DomainEntityCreatorForPersistenceTests;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.dao.DomibusConnectorEvidenceDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.model.enums.EvidenceType;
import eu.domibus.connector.persistence.model.enums.MessageDirection;
import eu.domibus.connector.persistence.model.test.util.PersistenceEntityCreator;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.exceptions.PersistenceException;
import eu.domibus.connector.persistence.service.impl.helper.MsgContentPersistenceService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

/**
 *
 *
 */
public class DomibusConnectorMessagePersistenceServiceImplTest {

    
    @Mock
    DomibusConnectorEvidenceDao evidenceDao;
    
    @Mock
    DomibusConnectorMessageDao messageDao;

    @Mock
    MsgContentPersistenceService msgContService;

    @Mock
    InternalEvidencePersistenceService internalEvidencePersistenceService;

    @Mock
    InternalMessageInfoPersistenceService messageInfoPersistenceService;

    DomibusConnectorMessagePersistenceService messagePersistenceService;
    
    public DomibusConnectorMessagePersistenceServiceImplTest() {
    }
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        DomibusConnectorMessagePersistenceServiceImpl impl = new DomibusConnectorMessagePersistenceServiceImpl();
        impl.setEvidenceDao(evidenceDao);
        impl.setMessageDao(messageDao);
        impl.setInternalMessageInfoPersistenceService(messageInfoPersistenceService);

        impl.setMsgContentService(msgContService);
        impl.setEvidencePersistenceService(internalEvidencePersistenceService);

        this.messagePersistenceService = impl;
        
        
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                DomibusConnectorMessageBuilder builder = invocation.getArgumentAt(0, DomibusConnectorMessageBuilder.class);
                builder.setMessageContent(new DomibusConnectorMessageContent());
                return null;
            }
        }).when(msgContService).loadMsgContent(any(DomibusConnectorMessageBuilder.class), any(PDomibusConnectorMessage.class));
    }


    /**
     * Message related
     */
    @Test
    public void testFindMessageByNationalId() {
        Mockito.when(messageDao.findOneByBackendMessageId(eq("national1")))
                .thenReturn(PersistenceEntityCreator.createSimpleDomibusConnectorMessage());
        
        
        eu.domibus.connector.domain.model.DomibusConnectorMessage national1 = messagePersistenceService.findMessageByNationalId("national1");
        assertThat(national1).isNotNull();
        
        //assertThat(national1.getDbMessageId()).isEqualTo(47L);
        
    }

    @Test
    public void testFindMessageByEbmsId() {        
        Mockito.when(messageDao.findOneByEbmsMessageId(eq("ebms1")))
                .thenReturn(PersistenceEntityCreator.createSimpleDomibusConnectorMessage());
        
        
        DomibusConnectorMessage ebms1 = messagePersistenceService.findMessageByEbmsId("ebms1");
        assertThat(ebms1).isNotNull();
    }

    @Test
    public void testFindMessageByConnectorMessageId() {
        Mockito.when(messageDao.findOneByConnectorMessageId(eq("msg72")))
                .thenReturn(PersistenceEntityCreator.createSimpleDomibusConnectorMessage());
        
        DomibusConnectorMessage msg72 = messagePersistenceService.findMessageByConnectorMessageId("msg72");
        assertThat(msg72).isNotNull();
    }
    
    @Test
    public void testFindMessagesByConversationId_noResults_shouldReturnEmptyList() {
        List<eu.domibus.connector.domain.model.DomibusConnectorMessage> messageList = messagePersistenceService.findMessagesByConversationId("conversation1");
        assertThat(messageList).hasSize(0);
    }
    
    @Test
    public void testFindMessagesByConversationId() {
        List<PDomibusConnectorMessage> list = Arrays.asList(new PDomibusConnectorMessage[]
            {PersistenceEntityCreator.createSimpleDomibusConnectorMessage(), 
            PersistenceEntityCreator.createSimpleDomibusConnectorMessage()});
        Mockito.when(messageDao.findByConversationId(eq("conversation1")))
                .thenReturn(list);
                
        List<eu.domibus.connector.domain.model.DomibusConnectorMessage> messageList = messagePersistenceService.findMessagesByConversationId("conversation1");
        
        assertThat(messageList).hasSize(2);
        assertThat(messageList.get(0)).isOfAnyClassIn(eu.domibus.connector.domain.model.DomibusConnectorMessage.class);
    }
    
    

    @Test
    public void testFindOutgoingUnconfirmedMessages() {
        List<PDomibusConnectorMessage> list = Arrays.asList(new PDomibusConnectorMessage[]
            {PersistenceEntityCreator.createSimpleDomibusConnectorMessage(), 
            PersistenceEntityCreator.createSimpleDomibusConnectorMessage()});
        

        
        
        //TODO: mock message content...
        Mockito.when(messageDao.findOutgoingUnconfirmedMessages())
                .thenReturn(list);
        
        List<eu.domibus.connector.domain.model.DomibusConnectorMessage> messageList = messagePersistenceService.findOutgoingUnconfirmedMessages();
        assertThat(messageList).hasSize(2);
    }
    
    @Test
    public void testFindOutgoingUnconfirmedMessages_noResults_shouldReturnEmptyList() {
        List<eu.domibus.connector.domain.model.DomibusConnectorMessage> messageList = messagePersistenceService.findOutgoingUnconfirmedMessages();
        assertThat(messageList).hasSize(0);
        //assertThat(messageList).are(new Condition<>(Object::getClass::isAssignableFrom, Message.class));
        //assertThat(messageList).are(new IsAssignableFromCondition(Message.class));
    }

    @Test
    public void testFindOutgoingMessagesNotRejectedAndWithoutDelivery() {
        List<PDomibusConnectorMessage> list = Arrays.asList(new PDomibusConnectorMessage[]
            {PersistenceEntityCreator.createSimpleDomibusConnectorMessage(), 
            PersistenceEntityCreator.createSimpleDomibusConnectorMessage()});
        
        Mockito.when(messageDao.findOutgoingMessagesNotRejectedAndWithoutDelivery())
                .thenReturn(list);
        
        List<eu.domibus.connector.domain.model.DomibusConnectorMessage> messageList = messagePersistenceService.findOutgoingMessagesNotRejectedAndWithoutDelivery();
        assertThat(messageList).hasSize(2);
    }
    
    @Test    
    public void testFindOutgoingMessagesNotRejectedAndWithoutDelivery_noResults_shouldReturnEmptyList() {
        List<eu.domibus.connector.domain.model.DomibusConnectorMessage> messageList = messagePersistenceService.findOutgoingMessagesNotRejectedAndWithoutDelivery();
        assertThat(messageList).hasSize(0);
    }

    @Test
    public void testFindOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD() {
        List<PDomibusConnectorMessage> list = Arrays.asList(new PDomibusConnectorMessage[]
            {PersistenceEntityCreator.createSimpleDomibusConnectorMessage(), 
            PersistenceEntityCreator.createSimpleDomibusConnectorMessage()});
        
        Mockito.when(messageDao.findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD())
                .thenReturn(list);
        
        List<eu.domibus.connector.domain.model.DomibusConnectorMessage> messageList = messagePersistenceService.findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD();
        assertThat(messageList).hasSize(2);        
    }
    
    @Test
    public void testFindOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD_noResults_shouldReturnEmptyList() {
        List<eu.domibus.connector.domain.model.DomibusConnectorMessage> messageList = messagePersistenceService.findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD();
        assertThat(messageList).hasSize(0);
    }

    @Test
    public void testFindIncomingUnconfirmedMessages() {
        List<PDomibusConnectorMessage> list = Arrays.asList(new PDomibusConnectorMessage[]
            {PersistenceEntityCreator.createSimpleDomibusConnectorMessage(), 
            PersistenceEntityCreator.createSimpleDomibusConnectorMessage()});
        
        Mockito.when(messageDao.findIncomingUnconfirmedMessages())
                .thenReturn(list);
        
        List<eu.domibus.connector.domain.model.DomibusConnectorMessage> messageList = messagePersistenceService.findIncomingUnconfirmedMessages();
        assertThat(messageList).hasSize(2);      
    }
    
    @Test
    public void testFindIncomingUnconfirmedMessages_noResults_shouldReturnEmptyList() {
        List<eu.domibus.connector.domain.model.DomibusConnectorMessage> messageList = messagePersistenceService.findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD();
        assertThat(messageList).hasSize(0);        
    }

    @Test
    public void testConfirmMessage() {
        Mockito.when(messageDao.confirmMessage(any(Long.class)))
                .thenReturn(1);
        PDomibusConnectorMessage dbMessage = PersistenceEntityCreator.createSimpleDomibusConnectorMessage();
        dbMessage.setId(78L);
        Mockito.when(messageDao.findOneByConnectorMessageId(eq("msgid"))).thenReturn(dbMessage);
        
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        //message.setDbMessageId(78L);
        messagePersistenceService.confirmMessage(message);
        Mockito.verify(messageDao, Mockito.times(1)).confirmMessage(eq(78L));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConfirmMessage_noMessageIdSet_shouldThrowIllegalArgumentException() {
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        //message.setDbMessageId(null);
        messagePersistenceService.confirmMessage(message);
    }
    
    @Test(expected=RuntimeException.class)
    public void testConfirmMessage_confirmFails_shouldThrowException() {
        Mockito.when(messageDao.confirmMessage(any(Long.class)))
                .thenReturn(0); //tell service nothing has been updated!                
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        //message.setDbMessageId(78L);
        messagePersistenceService.confirmMessage(message);
        Mockito.verify(messageDao, Mockito.times(1)).confirmMessage(eq(78L));
    }

    
    
    
    @Test
    public void testRejectMessage() {
        PDomibusConnectorMessage dbMessage = PersistenceEntityCreator.createSimpleDomibusConnectorMessage();
        dbMessage.setId(78L);
        Mockito.when(this.messageDao.findOneByConnectorMessageId(eq("msgid"))).thenReturn(dbMessage);
        Mockito.when(messageDao.rejectMessage(any(Long.class)))
                .thenReturn(1);
                
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        //message.setDbMessageId(78L);
        messagePersistenceService.rejectMessage(message);
        Mockito.verify(messageDao, Mockito.times(1)).rejectMessage(eq(78L));
    }
    
        
    @Test(expected=IllegalArgumentException.class)
    public void testRejectMessage_noMessageIdSet_shouldThrowIllegalArgumentException() {
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        //message.setDbMessageId(null);
        messagePersistenceService.rejectMessage(message);
    }
    
    @Test(expected=RuntimeException.class)
    public void testRejectMessage_confirmFails_shouldThrowException() {
        Mockito.when(messageDao.rejectMessage(any(Long.class)))
                .thenReturn(0); //tell service nothing has been updated!                
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        //message.setDbMessageId(78L);
        messagePersistenceService.rejectMessage(message);
        Mockito.verify(messageDao, Mockito.times(1)).rejectMessage(eq(78L));
    }
    
    
    @Test
    public void testPersistMessageIntoDatabase() throws PersistenceException {
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        
        DomibusConnectorMessageConfirmation messageConfirmation = new DomibusConnectorMessageConfirmation();
        messageConfirmation.setEvidence("MYEVIDENCE".getBytes());
        messageConfirmation.setEvidenceType(DomibusConnectorEvidenceType.DELIVERY);
        
        message.addConfirmation(messageConfirmation);
        
        message.getMessageDetails().setEbmsMessageId("ebmsid");
        message.getMessageDetails().setConversationId("conversation1");
        message.getMessageDetails().setConnectorBackendClientName("BOB");
        message.getMessageDetails().setBackendMessageId("backendid1");
        
        Mockito.when(messageDao.save(any(PDomibusConnectorMessage.class)))
                .then(new Answer<PDomibusConnectorMessage>() {
                    @Override
                    public PDomibusConnectorMessage answer(InvocationOnMock invocation) throws Throwable {
                        PDomibusConnectorMessage message = invocation.getArgumentAt(0, PDomibusConnectorMessage.class);
                        
                        //TODO: complete mapping check
                        assertThat(message.getEbmsMessageId()).isEqualTo("ebmsid");
                        assertThat(message.getConversationId()).isEqualTo("conversation1");
                        assertThat(message.getBackendName()).isEqualTo("BOB");
                        assertThat(message.getBackendMessageId()).isEqualTo("backendid1");
                        assertThat(message.getDirection()).isEqualTo(MessageDirection.NAT_TO_GW);
                        return message;
                    }                    
                });


        messagePersistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.NAT_TO_GW);

        Mockito.verify(messageDao, Mockito.times(1)).save(any(PDomibusConnectorMessage.class));
        
        //make sure message content is also saved to db
        Mockito.verify(msgContService, Mockito.times(1)).storeMsgContent(eq(message));
    }

    /*
    Test persistence of an evidence message (no content only confirmation)
     */
    @Test
    public void testPersistMessageIntoDatabase_isEvidenceMessage() throws PersistenceException {
        DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestConfirmationMessage();

        message.getMessageDetails().setConversationId("conversation1");
        message.getMessageDetails().setBackendMessageId("backendid1");
        message.getMessageDetails().setConnectorBackendClientName("BOB");
        message.getMessageDetails().setRefToMessageId("reftomsg1");

        messagePersistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.NAT_TO_GW);

        Mockito.verify(internalEvidencePersistenceService, Mockito.times(1)).persistAsEvidence(eq(message));
        Mockito.verifyZeroInteractions(messageDao);

    }

    @Test
    public void testMergeMessageWithDatabase() throws PersistenceException {
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        //message.setDbMessageId(47L);

        eu.domibus.connector.domain.model.DomibusConnectorMessageDetails messageDetails = message.getMessageDetails();
                
        messageDetails.setEbmsMessageId("ebms1");
        messageDetails.setFinalRecipient("finalRecipient");
        messageDetails.setFromParty(DomainEntityCreatorForPersistenceTests.createPartyAT());       
        messageDetails.setOriginalSender("original1");
        messageDetails.setRefToMessageId("reftomessageid");
        messageDetails.setService(DomainEntityCreatorForPersistenceTests.createServiceEPO());
        messageDetails.setToParty(DomainEntityCreatorForPersistenceTests.createPartyAT());
        messageDetails.setAction(DomainEntityCreatorForPersistenceTests.createActionForm_A());
        
        PDomibusConnectorMessage pMessage = PersistenceEntityCreator.createSimpleDomibusConnectorMessage();
        pMessage.setId(47L);        
        Mockito.when(messageDao.findOneByConnectorMessageId(eq("msgid"))).thenReturn(pMessage);

        Mockito.when(messageDao.findOne(47L)).thenReturn(pMessage);

        messagePersistenceService.mergeMessageWithDatabase(message);

        Mockito.verify(messageInfoPersistenceService, Mockito.times(1))
                .mergeMessageInfo(any(DomibusConnectorMessage.class), any(PDomibusConnectorMessage.class));
        
        //make sure message content is also saved to db
        Mockito.verify(this.msgContService, Mockito.times(1)).storeMsgContent(eq(message));
    }
    
    @Test(expected=PersistenceException.class)
    public void testMergeMessageWithDatabase_messageIdNotSet_shouldThrowPersistenceException() throws PersistenceException {
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        //message.setDbMessageId(null); //message id not set!

        messagePersistenceService.mergeMessageWithDatabase(message);
    }
    
    @Test(expected=PersistenceException.class)
    public void testMergeMessageWithDatabase_doesNotExistInDB_shouldThrowPersistenceException() throws PersistenceException {
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        //message.setDbMessageId(89L); //does not exist in db, because dao will return null anyway is not mocked!

        messagePersistenceService.mergeMessageWithDatabase(message);
    }

    @Test
    public void testSetMessageDeliveredToGateway() {
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        
        PDomibusConnectorMessage dbMessage = PersistenceEntityCreator.createSimpleDomibusConnectorMessage();        
        Mockito.when(messageDao.findOneByConnectorMessageId(eq("msgid"))).thenReturn(dbMessage);

        messagePersistenceService.setDeliveredToGateway(message);
        
        Mockito.verify(messageDao, Mockito.times(1)).setMessageDeliveredToGateway(eq(dbMessage));
    }

    /*
        Ensure that the evidences of an to gw delivered msg are also set as delivered!
     */
    @Test
    public void testSetMessageDeliveredToGateway_confirmationsOfMessageShouldBeSetDeliveredTo() {
        DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        message.setConnectorMessageId("msgid");
        message.addConfirmation(DomainEntityCreatorForPersistenceTests.createMessageDeliveryConfirmation()); //add DELIVERY evidence
        message.addConfirmation(DomainEntityCreator.createMessageSubmissionAcceptanceConfirmation());

        PDomibusConnectorMessage dbMessage = PersistenceEntityCreator.createSimpleDomibusConnectorMessage();
        //Mockito.when(messageDao.findOneByConnectorMessageId(eq("msgid"))).thenReturn(dbMessage);
        Mockito.when(messageDao.findOneByConnectorMessageId(eq("msgid"))).thenReturn(dbMessage);

        messagePersistenceService.setDeliveredToGateway(message);


        //msg itself should be set delivered
        Mockito.verify(messageDao, Mockito.times(1)).setMessageDeliveredToGateway(eq(dbMessage));

        //confirmation 1 should be set delivered!
        Mockito.verify(evidenceDao, Mockito.times(1)).setDeliveredToGateway(eq(dbMessage), eq(EvidenceType.DELIVERY));
        Mockito.verify(evidenceDao, Mockito.times(1)).setDeliveredToGateway(eq(dbMessage), eq(EvidenceType.SUBMISSION_ACCEPTANCE));
    }

    /*
    Test if an evidence message can also be set as delivered to gw
     for this purpose the evidence itself must be set as delivered!
     */
    @Test
    public void testSetMessageDeliveredToGateway_isEvidence() {

        DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestConfirmationMessage();
        message.getMessageDetails().setRefToMessageId("firstmsg");

        PDomibusConnectorMessage dbMessage = PersistenceEntityCreator.createSimpleDomibusConnectorMessage();
//        Mockito.when(messageDao.findOneByConnectorMessageId(eq("msgid"))).thenReturn(dbMessage);
        Mockito.when(messageDao.findOneByEbmsMessageIdOrBackendMessageId(eq("firstmsg"))).thenReturn(dbMessage);

        messagePersistenceService.setDeliveredToGateway(message);

        Mockito.verify(evidenceDao, Mockito.times(1)).setDeliveredToGateway(eq(dbMessage), eq(EvidenceType.DELIVERY));
    }

    @Test
    public void testSetMessageDeliveredToNationalSystem() {
        DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        PDomibusConnectorMessage dbMessage = PersistenceEntityCreator.createSimpleDomibusConnectorMessage();        
        Mockito.when(messageDao.findOneByConnectorMessageId(eq("msgid"))).thenReturn(dbMessage);

        messagePersistenceService.setMessageDeliveredToNationalSystem(message);
        
        Mockito.verify(messageDao, Mockito.times(1)).setMessageDeliveredToBackend(dbMessage);
    }

    @Test
    public void testSetMessageDeliveredToNationalSystem_isEvidence() {
        DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestConfirmationMessage();
        message.setConnectorMessageId("msg47");
        message.getMessageDetails().setRefToMessageId("reftomsg");

        PDomibusConnectorMessage dbMessage = PersistenceEntityCreator.createSimpleDomibusConnectorMessage();
        dbMessage.setId(81L);
        Mockito.when(messageDao.findOneByEbmsMessageIdOrBackendMessageId(eq("reftomsg")))
                .thenReturn(dbMessage);

        messagePersistenceService.setMessageDeliveredToNationalSystem(message);

        Mockito.verify(this.evidenceDao).setDeliveredToBackend(eq(dbMessage), eq(EvidenceType.DELIVERY));
    }

    /*
    *   test that the evidences transported with an message are also set as delivered to backend
    */
    @Test
    public void testSetMessageDeliveredToNationalSystem_alsoSetWithMessageTransportedEvidencesAsDelivered() {
        DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        message.setConnectorMessageId("msgid");
        message.addConfirmation(DomainEntityCreatorForPersistenceTests.createMessageDeliveryConfirmation());
        message.addConfirmation(DomainEntityCreator.createMessageSubmissionAcceptanceConfirmation());

        PDomibusConnectorMessage dbMessage = PersistenceEntityCreator.createSimpleDomibusConnectorMessage();
        Mockito.when(messageDao.findOneByConnectorMessageId(eq("msgid"))).thenReturn(dbMessage);

        messagePersistenceService.setMessageDeliveredToNationalSystem(message);

        //msg itself should be set delivered
        Mockito.verify(messageDao, Mockito.times(1)).setMessageDeliveredToBackend(dbMessage);

        //confirmations should be set as delivered!
        Mockito.verify(evidenceDao, Mockito.times(1)).setDeliveredToBackend(eq(dbMessage), eq(EvidenceType.DELIVERY));
        Mockito.verify(evidenceDao, Mockito.times(1)).setDeliveredToBackend(eq(dbMessage), eq(EvidenceType.SUBMISSION_ACCEPTANCE));
    }


    @Test
    public void testCheckMessageConfirmedOrRejected() {
        DomibusConnectorMessage msg = DomainEntityCreatorForPersistenceTests.createMessage();
        msg.setConnectorMessageId("msg71");
        mockFindMessageByConnectorMessageId("msg71");
        
        Mockito.when(this.messageDao.checkMessageConfirmedOrRejected(eq(47L)))
                .thenReturn(false);
        
        boolean confirmedOrRejected = messagePersistenceService.checkMessageConfirmedOrRejected(msg);
        
        assertThat(confirmedOrRejected).isFalse();
        
    }
    
    @Test
    public void testCheckMessageRejected() {
        DomibusConnectorMessage msg = DomainEntityCreatorForPersistenceTests.createMessage();
        msg.setConnectorMessageId("msg71");
        mockFindMessageByConnectorMessageId("msg71");
        
        Mockito.when(messageDao.checkMessageRejected(eq(47L)))
                .thenReturn(false);
        
        boolean messageRejected = this.messagePersistenceService.checkMessageRejected(msg); //this.messageDao.isMessageRejected(msg);
        
        assertThat(messageRejected).isFalse();
    }

    private void mockFindMessageByConnectorMessageId(String messageId) {
        Mockito.when(this.messageDao.findOneByConnectorMessageId(eq(messageId)))
                .thenReturn(PersistenceEntityCreator.createSimpleDomibusConnectorMessage()); 
    }


    
}
