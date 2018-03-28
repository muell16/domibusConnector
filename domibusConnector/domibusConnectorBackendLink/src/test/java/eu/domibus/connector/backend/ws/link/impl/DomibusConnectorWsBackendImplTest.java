
package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.backend.service.DomibusConnectorBackendInternalDeliverToController;
import eu.domibus.connector.backend.persistence.service.BackendClientInfoPersistenceService;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessagesType;
import eu.domibus.connector.domain.transition.testutil.TransitionCreator;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistAllBigDataOfMessageService;
import eu.domibus.connector.persistence.service.impl.BigDataWithMessagePersistenceServiceImpl;
import eu.domibus.connector.persistence.service.testutil.DomibusConnectorBigDataPersistenceServiceMemoryImpl;
import eu.domibus.connector.ws.backend.webservice.EmptyRequestType;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.xml.ws.WebServiceContext;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
public class  DomibusConnectorWsBackendImplTest {

    DomibusConnectorWsBackendImpl backendWebService;
    
    WebServiceContext webServiceContext;

    @Mock
    BackendClientInfoPersistenceService backendClientInfoPersistenceService;
    @Mock
    MessageToBackendClientWaitQueue messageToBackendClientWaitQueue;
    @Mock
    DomibusConnectorMessagePersistenceService messagePersistenceService;
    @Mock
    DomibusConnectorPersistAllBigDataOfMessageService domibusConnectorPersistAllBigDataOfMessageService;
    @Mock
    DomibusConnectorBackendInternalDeliverToController backendSubmissionService;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        webServiceContext = Mockito.mock(WebServiceContext.class);
     
        DomibusConnectorWsBackendImpl domibusConnectorBackendImpl = new DomibusConnectorWsBackendImpl();
        domibusConnectorBackendImpl.setWsContext(webServiceContext);
        domibusConnectorBackendImpl.setBackendClientInfoPersistenceService(backendClientInfoPersistenceService);
        domibusConnectorBackendImpl.setMessageToBackendClientWaitQueue(messageToBackendClientWaitQueue);
        domibusConnectorBackendImpl.setMessagePersistenceService(messagePersistenceService);
        domibusConnectorBackendImpl.setBackendSubmissionService(backendSubmissionService);


        Mockito.when(backendSubmissionService.processMessageBeforeDeliverToBackend(any(DomibusConnectorMessage.class)))
                .thenAnswer(invoc -> invoc.getArgumentAt(0, DomibusConnectorMessage.class));

        DomibusConnectorBigDataPersistenceServiceMemoryImpl bigDataPersistenceService = new DomibusConnectorBigDataPersistenceServiceMemoryImpl();
        domibusConnectorPersistAllBigDataOfMessageService = spy(new BigDataWithMessagePersistenceServiceImpl());
        ((BigDataWithMessagePersistenceServiceImpl)domibusConnectorPersistAllBigDataOfMessageService).setBigDataPersistenceServiceImpl(bigDataPersistenceService);
        domibusConnectorBackendImpl.setDomibusConnectorPersistAllBigDataOfMessageService(domibusConnectorPersistAllBigDataOfMessageService);


        backendWebService = domibusConnectorBackendImpl;
    }

    
    
    @Test
    public void testRequestMessages() {


        Mockito.when(webServiceContext.getUserPrincipal()).thenReturn(createUserPrinicipal("bob"));
        Mockito.when(backendClientInfoPersistenceService.getEnabledBackendClientInfoByName(Mockito.eq("bob")))
                .thenReturn(backendClientInfoBob());
        Mockito.when(messageToBackendClientWaitQueue.getConnectorMessageIdForBackend(Mockito.eq("bob")))
                .thenReturn(createTestMessages());
        Mockito.when(messagePersistenceService.findMessageByConnectorMessageId(Mockito.eq("msg1")))
                .thenReturn(DomainEntityCreator.createMessage());
        Mockito.when(messagePersistenceService.findMessageByConnectorMessageId(Mockito.eq("msg2")))
                .thenReturn(DomainEntityCreator.createMessage());
        
        DomibusConnectorMessagesType requestMessages = backendWebService.requestMessages(new EmptyRequestType());
        
        assertThat(requestMessages).isNotNull();
        assertThat(requestMessages.getMessages()).as("2 messages are expected").hasSize(2);

        Mockito.verify(domibusConnectorPersistAllBigDataOfMessageService, times(2)).loadAllBigFilesFromMessage(any(DomibusConnectorMessage.class));
    }

    @Test
    public void testRetrieveWaitingMessagesFromQueue() {
        Mockito.when(messageToBackendClientWaitQueue.getConnectorMessageIdForBackend(Mockito.eq("bob")))
                .thenReturn(createTestMessages());

        DomibusConnectorMessagesType domibusConnectorMessagesType = backendWebService.retrieveWaitingMessagesFromQueue(backendClientInfoBob());

        assertThat(domibusConnectorMessagesType.getMessages()).hasSize(2);
    }



    private List<DomibusConnectorMessage> createTestMessages() {
        ArrayList<DomibusConnectorMessage> messages = new ArrayList<>();

        DomibusConnectorMessage msg1 = DomainEntityCreator.createMessage();
        msg1.setConnectorMessageId("msg1");
        messages.add(msg1);

        DomibusConnectorMessage msg2 = DomainEntityCreator.createMessage();
        msg1.setConnectorMessageId("msg2");
        messages.add(msg2);

        return messages;
    }

    private DomibusConnectorBackendClientInfo backendClientInfoBob() {
        DomibusConnectorBackendClientInfo backendClientInfo = new DomibusConnectorBackendClientInfo();
        backendClientInfo.setBackendName("bob");
        return backendClientInfo;
    }
    
    private Principal createUserPrinicipal(final String name) {
        Principal bob = new Principal() {
            @Override
            public String getName() {
                return name;
            }
        };
        return bob;
    }
    
    @Test
    public void testSubmitMessage() {
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                DomibusConnectorBackendMessage argumentAt = invocation.getArgumentAt(0, DomibusConnectorBackendMessage.class);
                argumentAt.getDomibusConnectorMessage().setConnectorMessageId(UUID.randomUUID().toString());
                return null;
            }
        }).when(backendSubmissionService).submitToController(any(DomibusConnectorBackendMessage.class));
        
        Mockito.when(webServiceContext.getUserPrincipal()).thenReturn(createUserPrinicipal("bob"));
        Mockito.when(backendClientInfoPersistenceService.getEnabledBackendClientInfoByName(Mockito.eq("bob")))
                .thenReturn(backendClientInfoBob());
        
        DomibusConnectorMessageType transitionMessage = TransitionCreator.createMessage();
        
        DomibsConnectorAcknowledgementType submitMessage = backendWebService.submitMessage(transitionMessage);
        
        assertThat(submitMessage).isNotNull();
        assertThat(submitMessage.isResult()).isTrue();
        assertThat(submitMessage.getMessageId()).isNotNull();
        
    }

}