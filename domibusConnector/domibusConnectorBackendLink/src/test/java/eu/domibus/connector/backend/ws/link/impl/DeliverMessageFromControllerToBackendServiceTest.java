package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.backend.persistence.service.BackendClientInfoPersistenceService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;


public class DeliverMessageFromControllerToBackendServiceTest {

    private DeliverMessageFromControllerToBackendService deliverMessageFromControllerToBackendService;

    @Mock
    private BackendClientInfoPersistenceService backendClientInfoPersistenceService;

    @Mock
    private PushMessageToBackendClient pushMessageToBackendClient;

    @Mock
    private MessageToBackendClientWaitQueue waitQueue;

    @Mock
    private DomibusConnectorMessagePersistenceService messagePersistenceService;

    private List<DomibusConnectorBackendMessage> toClientPushedMessages;

    private List<DomibusConnectorBackendMessage> waitQueueMessages;

    @Before
    public void setUp() {
        toClientPushedMessages = new ArrayList<>();
        waitQueueMessages = new ArrayList<>();

        MockitoAnnotations.initMocks(this);
        deliverMessageFromControllerToBackendService = new DeliverMessageFromControllerToBackendService();
        deliverMessageFromControllerToBackendService.setBackendClientInfoPersistenceService(backendClientInfoPersistenceService);
        deliverMessageFromControllerToBackendService.setPushMessageToBackendClient(pushMessageToBackendClient);
        deliverMessageFromControllerToBackendService.setWaitQueue(waitQueue);
        deliverMessageFromControllerToBackendService.setMessagePersistenceService(messagePersistenceService);


        //collect all messages put in wait queue
        Mockito.doAnswer((invocation) -> waitQueueMessages.add(invocation.getArgumentAt(0, DomibusConnectorBackendMessage.class)))
                .when(waitQueue).putMessageInWaitingQueue(any(DomibusConnectorBackendMessage.class));

        //collect all pushed messages in array list
        Mockito.doAnswer((invocation) -> toClientPushedMessages.add(invocation.getArgumentAt(0, DomibusConnectorBackendMessage.class)))
                .when(pushMessageToBackendClient).push(any(DomibusConnectorBackendMessage.class));

        //send backend alice back, if conversation is conversation1
        List<DomibusConnectorMessage> messages = new ArrayList<>();
        DomibusConnectorMessage msg1 = DomainEntityCreator.createMessage();
        msg1.getMessageDetails().setConnectorBackendClientName("alice");
        messages.add(msg1);
        Mockito.when(messagePersistenceService.findMessagesByConversationId(eq("conversation1")))
                .thenReturn(messages);

        Mockito.when(backendClientInfoPersistenceService.getEnabledBackendClientInfoByName(eq("alice")))
                .thenReturn(createBackendClientInfoAlice());

        //if service epo, then send backend bob back
        Mockito.when(backendClientInfoPersistenceService.getEnabledBackendClientInfoByService(
                eq(DomainEntityCreator.createServiceEPO())))
                .thenReturn(createBackendClientInfoBob());

        //relatedMessage with id msg2, has backendClient name catrina
        DomibusConnectorMessage relatedMessage = DomainEntityCreator.createMessage();
        relatedMessage.setConnectorMessageId("msg2");
        relatedMessage.getMessageDetails().setConnectorBackendClientName("catrina");
        Mockito.when(messagePersistenceService.findMessageByConnectorMessageId(eq("msg2"))).thenReturn(relatedMessage);

        Mockito.when(backendClientInfoPersistenceService.getEnabledBackendClientInfoByName(eq("catrina"))).thenReturn(createBackendClientInfoCatrina());

        Mockito.when(backendClientInfoPersistenceService.getDefaultBackendClientInfo()).thenReturn(createBackendClientInfoDefault());

    }

    @Test
    public void testDeliverMessageToBackend_usePushBackend() {
        DomibusConnectorMessage message = DomainEntityCreator.createEpoMessage();
        message.getMessageDetails().setRefToMessageId("msg2");

        deliverMessageFromControllerToBackendService.deliverMessageToBackend(message);

        assertThat(toClientPushedMessages).hasSize(1);
        assertThat(waitQueueMessages).hasSize(0);
    }

    @Test
    public void testDeliverMessageToBackend_usePullBackend() {
        DomibusConnectorMessage message = DomainEntityCreator.createEpoMessage();
        message.getMessageDetails().setRefToMessageId(null);
        message.getMessageDetails().setConversationId("conversation1");

        deliverMessageFromControllerToBackendService.deliverMessageToBackend(message);

        assertThat(toClientPushedMessages).hasSize(0);
        assertThat(waitQueueMessages).hasSize(1);
    }

    @Test
    public void testGetBackendClientForMessage_byRefToMessageId() {
        DomibusConnectorMessage message = DomainEntityCreator.createEvidenceNonDeliveryMessage();
        message.getMessageDetails().setRefToMessageId("msg2"); //msg is related to msg2
        message.getMessageDetails().setConversationId(null);

        DomibusConnectorBackendClientInfo backendClientForMessage = deliverMessageFromControllerToBackendService.getBackendClientForMessage(message);
        assertThat(backendClientForMessage).isNotNull();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetBackendClientForMessage_byRefToMessageId_referencedMessageDoesNotExist() {
        DomibusConnectorMessage message = DomainEntityCreator.createEvidenceNonDeliveryMessage();
        message.getMessageDetails().setRefToMessageId("msg123"); //msg is related to msg123 which does not exist
        message.getMessageDetails().setConversationId(null);

        DomibusConnectorBackendClientInfo backendClientForMessage = deliverMessageFromControllerToBackendService.getBackendClientForMessage(message);
    }

    @Test
    public void testGetBackendClientForMessage_byConversationId() {
        DomibusConnectorMessage message = DomainEntityCreator.createEvidenceNonDeliveryMessage();
        message.getMessageDetails().setRefToMessageId(null); //msg is related to msg2
        message.getMessageDetails().setConversationId("conversation1");

        DomibusConnectorBackendClientInfo backendClientForMessage = deliverMessageFromControllerToBackendService.getBackendClientForMessage(message);

        assertThat(backendClientForMessage).isNotNull();
    }

    @Test
    public void testGetBackendClientForMessage_byServiceName() {
        DomibusConnectorMessage message = DomainEntityCreator.createEpoMessage();
        message.getMessageDetails().setRefToMessageId(null); //first message
        message.getMessageDetails().setConversationId(null); //first message


        DomibusConnectorBackendClientInfo backendClientForMessage = deliverMessageFromControllerToBackendService.getBackendClientForMessage(message);
        assertThat(backendClientForMessage).isNotNull();
        assertThat(backendClientForMessage).isEqualToComparingFieldByField(createBackendClientInfoBob());
    }

    @Test
    public void testGetBackendClientForMessage_byDefaultBackend() {
        DomibusConnectorMessage message = DomainEntityCreator.createEpoMessage();
        message.getMessageDetails().setRefToMessageId(null); //first message
        message.getMessageDetails().setConversationId(null); //first message
        DomibusConnectorService service = new DomibusConnectorService("ASERVICE", "SERVICE TYPE"); //unknown service
        message.getMessageDetails().setService(service);

        DomibusConnectorBackendClientInfo backendClientForMessage = deliverMessageFromControllerToBackendService.getBackendClientForMessage(message);

        assertThat(backendClientForMessage).isEqualToComparingFieldByField(createBackendClientInfoDefault());

    }



    private DomibusConnectorBackendClientInfo createBackendClientInfoBob() {
        DomibusConnectorBackendClientInfo bob = new DomibusConnectorBackendClientInfo();
        bob.setBackendDescription("description");
        bob.setBackendKeyAlias("keyalias");
        bob.setBackendKeyPass("keypass");
        bob.setBackendName("backendname");

        return bob;
    }

    private DomibusConnectorBackendClientInfo createBackendClientInfoAlice() {
        DomibusConnectorBackendClientInfo alice = new DomibusConnectorBackendClientInfo();
        alice.setBackendName("alice");
        alice.setBackendDescription("description");
        alice.setBackendKeyAlias("keyalias");
        alice.setBackendKeyPass("keypass");

        return alice;
    }

    private DomibusConnectorBackendClientInfo createBackendClientInfoCatrina() {
        DomibusConnectorBackendClientInfo catrina = new DomibusConnectorBackendClientInfo();
        catrina.setBackendName("catrina");
        catrina.setBackendDescription("description");
        catrina.setBackendKeyAlias("keyalias");
        catrina.setBackendKeyPass("keypass");
        catrina.setBackendPushAddress("backendpushaddress");

        return catrina;
    }

    private DomibusConnectorBackendClientInfo createBackendClientInfoDefault() {
        DomibusConnectorBackendClientInfo catrina = new DomibusConnectorBackendClientInfo();
        catrina.setBackendName("default");
        catrina.setBackendDescription("description");
        catrina.setBackendKeyAlias("keyalias");
        catrina.setBackendKeyPass("keypass");
        catrina.setBackendPushAddress("backendpushaddress");

        return catrina;
    }

}