
package eu.domibus.connector.ws.backend.link.impl;

import eu.domibus.connector.backend.persistence.service.BackendClientInfoPersistenceService;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.controller.service.DomibusConnectorBackendSubmissionService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessagesType;
import eu.domibus.connector.domain.transition.testutil.TransitionCreator;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistAllBigDataOfMessageService;
import eu.domibus.connector.persistence.service.impl.BigDataWithMessagePersistenceService;
import eu.domibus.connector.persistence.service.testutil.DomibusConnectorBigDataPersistenceServiceMemoryImpl;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import eu.domibus.connector.ws.backend.webservice.EmptyRequestType;
import java.security.Principal;
import java.util.Arrays;
import java.util.UUID;
import javax.xml.ws.WebServiceContext;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
public class DomibusConnectorBackendImplTest {

    DomibusConnectorWsBackendImpl backendWebService;
    
    WebServiceContext webServiceContext;
    
    BackendClientInfoPersistenceService backendClientInfoPersistenceService;
    
    MessageToBackendClientWaitQueue messageToBackendClientWaitQueue;
    
    DomibusConnectorMessagePersistenceService messagePersistenceService;
    
    DomibusConnectorPersistAllBigDataOfMessageService domibusConnectorPersistAllBigDataOfMessageService;
    
    DomibusConnectorBackendSubmissionService backendSubmissionService;
    
    @Before
    public void setUp() {
        webServiceContext = Mockito.mock(WebServiceContext.class);
     
        DomibusConnectorWsBackendImpl domibusConnectorBackendImpl = new DomibusConnectorWsBackendImpl();
        domibusConnectorBackendImpl.setWsContext(webServiceContext);
        
        backendClientInfoPersistenceService = Mockito.mock(BackendClientInfoPersistenceService.class);
        domibusConnectorBackendImpl.setBackendClientInfoPersistenceService(backendClientInfoPersistenceService);
        
        messageToBackendClientWaitQueue = Mockito.mock(MessageToBackendClientWaitQueue.class);
        domibusConnectorBackendImpl.setMessageToBackendClientWaitQueue(messageToBackendClientWaitQueue);
        
        messagePersistenceService = Mockito.mock(DomibusConnectorMessagePersistenceService.class);
        domibusConnectorBackendImpl.setMessagePersistenceService(messagePersistenceService);
        
        DomibusConnectorBigDataPersistenceServiceMemoryImpl bigDataPersistenceService = new DomibusConnectorBigDataPersistenceServiceMemoryImpl();        
        domibusConnectorPersistAllBigDataOfMessageService = new BigDataWithMessagePersistenceService();
        ((BigDataWithMessagePersistenceService)domibusConnectorPersistAllBigDataOfMessageService).setBigDataPersistenceServiceImpl(bigDataPersistenceService);        
        domibusConnectorBackendImpl.setDomibusConnectorPersistAllBigDataOfMessageService(domibusConnectorPersistAllBigDataOfMessageService);
                
        backendSubmissionService = Mockito.mock(DomibusConnectorBackendSubmissionService.class);
        domibusConnectorBackendImpl.setBackendSubmissionService(backendSubmissionService);
        
        backendWebService = domibusConnectorBackendImpl;
    }

    
    
    @Test
    public void testRequestMessages() {
        Mockito.when(webServiceContext.getUserPrincipal()).thenReturn(createUserPrinicipal("bob"));
        Mockito.when(backendClientInfoPersistenceService.getBackendClientInfoByName(Mockito.eq("bob")))
                .thenReturn(backendClientInfoBob());
        Mockito.when(messageToBackendClientWaitQueue.getConnectorMessageIdForBackend(Mockito.eq("bob")))
                .thenReturn(Arrays.asList(new String[]{"msg1", "msg2"}));
        Mockito.when(messagePersistenceService.findMessageByConnectorMessageId(Mockito.eq("msg1")))
                .thenReturn(DomainEntityCreator.createMessage());
        Mockito.when(messagePersistenceService.findMessageByConnectorMessageId(Mockito.eq("msg2")))
                .thenReturn(DomainEntityCreator.createMessage());
        
        DomibusConnectorMessagesType requestMessages = backendWebService.requestMessages(new EmptyRequestType());
        
        assertThat(requestMessages).isNotNull();
        assertThat(requestMessages.getMessages()).as("2 messages are expected").hasSize(2);
                
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
                DomibusConnectorMessage argumentAt = invocation.getArgumentAt(0, DomibusConnectorMessage.class);
                argumentAt.setConnectorMessageId(UUID.randomUUID().toString());
                return null;
            }
        }).when(backendSubmissionService).submitToController(any(DomibusConnectorMessage.class));
        
        Mockito.when(webServiceContext.getUserPrincipal()).thenReturn(createUserPrinicipal("bob"));
        Mockito.when(backendClientInfoPersistenceService.getBackendClientInfoByName(Mockito.eq("bob")))
                .thenReturn(backendClientInfoBob());
        
        DomibusConnectorMessageType transitionMessage = TransitionCreator.createMessage();
        
        DomibsConnectorAcknowledgementType submitMessage = backendWebService.submitMessage(transitionMessage);
        
        assertThat(submitMessage).isNotNull();
        assertThat(submitMessage.isResult()).isTrue();
        assertThat(submitMessage.getMessageId()).isNotNull();
        
    }

}