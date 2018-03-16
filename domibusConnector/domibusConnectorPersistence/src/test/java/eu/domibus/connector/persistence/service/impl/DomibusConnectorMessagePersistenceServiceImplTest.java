package eu.domibus.connector.persistence.service.impl;

//import eu.domibus.connector.domain.Action;
//import eu.domibus.connector.domain.Message;
//import eu.domibus.connector.domain.MessageConfirmation;
//import eu.domibus.connector.domain.MessageDetails;
//import eu.domibus.connector.domain.MessageError;
//import eu.domibus.connector.domain.Party;
//import eu.domibus.connector.domain.Service;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageContent;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.domain.test.util.DomainEntityCreatorForPersistenceTests;
import eu.domibus.connector.persistence.dao.DomibusConnectorActionDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorEvidenceDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageErrorDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageInfoDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorPartyDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorServiceDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessageInfo;
import eu.domibus.connector.persistence.model.enums.MessageDirection;
import eu.domibus.connector.persistence.model.test.util.PersistenceEntityCreator;

import static eu.domibus.connector.persistence.model.test.util.PersistenceEntityCreator.createPartyAT;

import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.PersistenceException;
import eu.domibus.connector.persistence.service.impl.helper.MsgContentPersistenceService;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.allOf;

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
import static org.hamcrest.CoreMatchers.allOf;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.eq;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
public class DomibusConnectorMessagePersistenceServiceImplTest {
    
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
    
    @Mock
    MsgContentPersistenceService msgContService;

    DomibusConnectorMessagePersistenceService messagePersistenceService;
    
    public DomibusConnectorMessagePersistenceServiceImplTest() {
    }
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        DomibusConnectorMessagePersistenceServiceImpl impl = new DomibusConnectorMessagePersistenceServiceImpl();
        impl.setEvidenceDao(domibusConnectorEvidenceDao);
        impl.setMessageDao(domibusConnectorMessageDao);
        impl.setMessageInfoDao(domibusConnectorMessageInfoDao);
        impl.setMsgContentService(msgContService);

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
        Mockito.when(domibusConnectorMessageDao.findOneByBackendMessageId(eq("national1")))
                .thenReturn(PersistenceEntityCreator.createSimpleDomibusConnectorMessage());
        
        
        eu.domibus.connector.domain.model.DomibusConnectorMessage national1 = messagePersistenceService.findMessageByNationalId("national1");
        assertThat(national1).isNotNull();
        
        //assertThat(national1.getDbMessageId()).isEqualTo(47L);
        
    }

    @Test
    public void testFindMessageByEbmsId() {        
        Mockito.when(domibusConnectorMessageDao.findOneByEbmsMessageId(eq("ebms1")))
                .thenReturn(PersistenceEntityCreator.createSimpleDomibusConnectorMessage());
        
        
        DomibusConnectorMessage ebms1 = messagePersistenceService.findMessageByEbmsId("ebms1");
        assertThat(ebms1).isNotNull();
    }

    @Test
    public void testFindMessageByConnectorMessageId() {
        Mockito.when(domibusConnectorMessageDao.findOneByConnectorMessageId(eq("msg72")))
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
        Mockito.when(domibusConnectorMessageDao.findByConversationId(eq("conversation1")))
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
        Mockito.when(domibusConnectorMessageDao.findOutgoingUnconfirmedMessages())
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
        
        Mockito.when(domibusConnectorMessageDao.findOutgoingMessagesNotRejectedAndWithoutDelivery())
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
        
        Mockito.when(domibusConnectorMessageDao.findOutgoingMessagesNotRejectedNorConfirmedAndWithoutRelayREMMD())
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
        
        Mockito.when(domibusConnectorMessageDao.findIncomingUnconfirmedMessages())
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
        Mockito.when(domibusConnectorMessageDao.confirmMessage(any(Long.class)))
                .thenReturn(1);
        PDomibusConnectorMessage dbMessage = PersistenceEntityCreator.createSimpleDomibusConnectorMessage();
        dbMessage.setId(78L);
        Mockito.when(domibusConnectorMessageDao.findOneByConnectorMessageId(eq("msgid"))).thenReturn(dbMessage);
        
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        //message.setDbMessageId(78L);
        messagePersistenceService.confirmMessage(message);
        Mockito.verify(domibusConnectorMessageDao, Mockito.times(1)).confirmMessage(eq(78L));        
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConfirmMessage_noMessageIdSet_shouldThrowIllegalArgumentException() {
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        //message.setDbMessageId(null);
        messagePersistenceService.confirmMessage(message);
    }
    
    @Test(expected=RuntimeException.class)
    public void testConfirmMessage_confirmFails_shouldThrowException() {
        Mockito.when(domibusConnectorMessageDao.confirmMessage(any(Long.class)))
                .thenReturn(0); //tell service nothing has been updated!                
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        //message.setDbMessageId(78L);
        messagePersistenceService.confirmMessage(message);
        Mockito.verify(domibusConnectorMessageDao, Mockito.times(1)).confirmMessage(eq(78L));        
    }

    
    
    
    @Test
    public void testRejectMessage() {
        PDomibusConnectorMessage dbMessage = PersistenceEntityCreator.createSimpleDomibusConnectorMessage();
        dbMessage.setId(78L);
        Mockito.when(this.domibusConnectorMessageDao.findOneByConnectorMessageId(eq("msgid"))).thenReturn(dbMessage);
        Mockito.when(domibusConnectorMessageDao.rejectMessage(any(Long.class)))
                .thenReturn(1);
                
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        //message.setDbMessageId(78L);
        messagePersistenceService.rejectMessage(message);
        Mockito.verify(domibusConnectorMessageDao, Mockito.times(1)).rejectMessage(eq(78L));    
    }
    
        
    @Test(expected=IllegalArgumentException.class)
    public void testRejectMessage_noMessageIdSet_shouldThrowIllegalArgumentException() {
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        //message.setDbMessageId(null);
        messagePersistenceService.rejectMessage(message);
    }
    
    @Test(expected=RuntimeException.class)
    public void testRejectMessage_confirmFails_shouldThrowException() {
        Mockito.when(domibusConnectorMessageDao.rejectMessage(any(Long.class)))
                .thenReturn(0); //tell service nothing has been updated!                
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        //message.setDbMessageId(78L);
        messagePersistenceService.rejectMessage(message);
        Mockito.verify(domibusConnectorMessageDao, Mockito.times(1)).rejectMessage(eq(78L));        
    }
    
    
    @Test
    public void testPersistMessageIntoDatabase() throws PersistenceException {
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        
        //message.setDbMessageId(null);
        
        DomibusConnectorMessageConfirmation messageConfirmation = new DomibusConnectorMessageConfirmation();
        messageConfirmation.setEvidence("MYEVIDENCE".getBytes());
        messageConfirmation.setEvidenceType(DomibusConnectorEvidenceType.DELIVERY);
        
        message.addConfirmation(messageConfirmation);
        
        message.getMessageDetails().setEbmsMessageId("ebmsid");
        message.getMessageDetails().setConversationId("conversation1");
        message.getMessageDetails().setConnectorBackendClientName("BOB");
        message.getMessageDetails().setBackendMessageId("backendid1");
        
        Mockito.when(domibusConnectorMessageDao.save(any(PDomibusConnectorMessage.class)))
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

        Mockito.verify(domibusConnectorMessageDao, Mockito.times(1)).save(any(PDomibusConnectorMessage.class));
        
        //make sure message content is also saved to db
        Mockito.verify(msgContService, Mockito.times(1)).storeMsgContent(eq(message));
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
        Mockito.when(domibusConnectorMessageDao.findOneByConnectorMessageId(eq("msgid"))).thenReturn(pMessage);
        
        Mockito.when(domibusConnectorMessageInfoDao.save(any(PDomibusConnectorMessageInfo.class)))
                .then(new Answer<PDomibusConnectorMessageInfo>() {
                    @Override
                    public PDomibusConnectorMessageInfo answer(InvocationOnMock invocation) throws Throwable {
                        PDomibusConnectorMessageInfo messageInfo = invocation.getArgumentAt(0, PDomibusConnectorMessageInfo.class);     
                               
                        //detailed mapping is checked at an other location
                        assertThat(messageInfo.getFinalRecipient()).as("final recipient must match").isEqualTo("finalRecipient");
                        assertThat(messageInfo.getOriginalSender()).as("original sender must match").isEqualTo("original1");   
                        //check action
                        assertThat(messageInfo.getAction()).as("action is not null").isNotNull();
                        assertThat(messageInfo.getAction().isDocumentRequired()).as("action pdf required is true").isTrue();
                        assertThat(messageInfo.getAction().getAction()).isEqualTo("Form_A");
                        //check service
                        assertThat(messageInfo.getService()).isNotNull();
                        assertThat(messageInfo.getService().getService()).isEqualTo("EPO");
                        assertThat(messageInfo.getService().getServiceType()).isEqualTo(DomainEntityCreatorForPersistenceTests.createServiceEPO().getServiceType());
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

        messagePersistenceService.mergeMessageWithDatabase(message);
        
        //TODO: test mapping back to db
        //Mockito.verify(messageDao, Mockito.times(1)).findOne(eq(47L));
        //Mockito.verify(messageDao, Mockito.times(1)).save(any(PDomibusConnectorMessage.class));
        
        Mockito.verify(domibusConnectorMessageInfoDao, Mockito.times(1)).save(any(PDomibusConnectorMessageInfo.class));
        
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
        Mockito.when(domibusConnectorMessageDao.findOneByConnectorMessageId(eq("msgid"))).thenReturn(dbMessage);


        messagePersistenceService.setDeliveredToGateway(message);
        
        Mockito.verify(domibusConnectorMessageDao, Mockito.times(1)).setMessageDeliveredToGateway(eq(47L));
    }

    @Test
    public void testSetMessageDeliveredToNationalSystem() {
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestMessage();
        //message.setDbMessageId(47L);

        PDomibusConnectorMessage dbMessage = PersistenceEntityCreator.createSimpleDomibusConnectorMessage();        
        Mockito.when(domibusConnectorMessageDao.findOneByConnectorMessageId(eq("msgid"))).thenReturn(dbMessage);


        messagePersistenceService.setMessageDeliveredToNationalSystem(message);
        
        Mockito.verify(domibusConnectorMessageDao, Mockito.times(1)).setMessageDeliveredToBackend(eq(47L));
    }

    @Ignore //TODO: fix test!
    @Test
    public void testSetMessageDeliveredToNationalSystem_isEvidence() {
        eu.domibus.connector.domain.model.DomibusConnectorMessage message = DomainEntityCreatorForPersistenceTests.createSimpleTestConfirmationMessage();
        message.setConnectorMessageId("msg47");
        message.getMessageDetails().setRefToMessageId("reftomsg");

        PDomibusConnectorMessage dbMessage = PersistenceEntityCreator.createSimpleDomibusConnectorMessage();
        dbMessage.setId(81L);
        Mockito.when(domibusConnectorMessageDao.findOneByEbmsMessageIdOrBackendMessageId(eq("reftomsg")))
                .thenReturn(dbMessage);


        //Mockito.when(messageDao.findOneByConnectorMessageId(eq("msgid2"))).thenReturn(dbMessage);

        messagePersistenceService.setMessageDeliveredToNationalSystem(message);

        //Mockito.verify(messageDao, Mockito.times(1)).setMessageDeliveredToBackend(eq(47L));
//        Mockito.verify(domibusConnectorEvidenceDao, Mockito.times(1))
//                .setDeliveredToBackend(eq(81L), eq(EvidenceType.DELIVERY));
    }


    @Test
    @Ignore("not implemented yet!")
    public void testCheckMessageConfirmedOrRejected() {
        DomibusConnectorMessage msg = DomainEntityCreatorForPersistenceTests.createMessage();
        msg.setConnectorMessageId("msg71");
        mockFindMessageByConnectorMessageId("msg71");
        
        Mockito.when(this.domibusConnectorMessageDao.checkMessageConfirmedOrRejected(eq(47L)))
                .thenReturn(false);
        
        boolean confirmedOrRejected = messagePersistenceService.checkMessageConfirmedOrRejected(msg);
        
        assertThat(confirmedOrRejected).isFalse();
        
    }
    
    @Test
    @Ignore("not implemented yet!")
    public void testCheckMessageRejected() {
        DomibusConnectorMessage msg = DomainEntityCreatorForPersistenceTests.createMessage();
        msg.setConnectorMessageId("msg71");
        mockFindMessageByConnectorMessageId("msg71");
        
        Mockito.when(domibusConnectorMessageDao.checkMessageRejected(eq(47L)))
                .thenReturn(false);
        
        boolean messageRejected = this.messagePersistenceService.checkMessageRejected(msg); //this.messageDao.isMessageRejected(msg);
        
        assertThat(messageRejected).isFalse();
    }
    
    private void mockFindMessageByConnectorMessageId(String messageId) {
        Mockito.when(this.domibusConnectorMessageDao.findOneByConnectorMessageId(eq(messageId)))
                .thenReturn(PersistenceEntityCreator.createSimpleDomibusConnectorMessage()); 
    }


    


    




    
}