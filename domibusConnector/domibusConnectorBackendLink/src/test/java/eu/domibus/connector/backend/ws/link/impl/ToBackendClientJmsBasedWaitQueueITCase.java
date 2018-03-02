
package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.ws.link.impl.PushMessageToBackendClient;
import eu.domibus.connector.backend.ws.link.impl.ToBackendClientJmsBasedWaitQueue;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.backend.persistence.model.BackendClientInfo;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.jms.Message;
import javax.jms.Session;

import org.junit.Ignore;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import org.junit.Rule;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ToBackendClientJmsBasedWaitQueueITCase.TestConfig.class})
@TestPropertySource(properties = { "connector.backend.internal.wait-queue.name=waitqueue"})
@Ignore("TODO: repair test!")
public class ToBackendClientJmsBasedWaitQueueITCase {

    @EnableJms
    @EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, LiquibaseAutoConfiguration.class})
    @Import(ToBackendClientJmsBasedWaitQueue.class)
    public final static class TestConfig {



    }
        
    @Value("${connector.backend.internal.wait-queue.name}")
    String waitQueueName;
    
    @Autowired
    ToBackendClientJmsBasedWaitQueue waitQueue;
    
    @Autowired
    JmsTemplate jmsTemplate;
    
    @MockBean
    PushMessageToBackendClient pushMessageToBackend;
        
    @Test
    public void testPutMessageOnQueue() throws InterruptedException {
        DomibusConnectorMessage msg = DomainEntityCreator.createMessage();
        msg.getMessageDetails().setConnectorBackendClientName("bob");
        
        DomibusConnectorBackendClientInfo backendClientInfo = new DomibusConnectorBackendClientInfo();
        backendClientInfo.setBackendName("bob");

        DomibusConnectorBackendMessage backendMessage = new DomibusConnectorBackendMessage();
        backendMessage.setDomibusConnectorMessage(msg);
        backendMessage.setBackendClientInfo(backendClientInfo);

        waitQueue.putMessageInWaitingQueue(backendMessage);
        
        jmsTemplate.setReceiveTimeout(10);
        Message receive = jmsTemplate.receive(waitQueueName);
        
        assertThat(receive).isNotNull();        
    }
    
    @Test
    public void testGetConnectorMessageIdForBackend() throws InterruptedException {
        DomibusConnectorMessage msg = DomainEntityCreator.createMessage();
        msg.getMessageDetails().setConnectorBackendClientName("bob");
        msg.setConnectorMessageId("msg1");
        
        DomibusConnectorBackendClientInfo backendClientBob = new DomibusConnectorBackendClientInfo();
        backendClientBob.setBackendName("bob"); //no push backend

        DomibusConnectorBackendMessage backendMessage = new DomibusConnectorBackendMessage(msg, backendClientBob);
        waitQueue.putMessageInWaitingQueue(backendMessage);
        
        msg.setConnectorMessageId("msg2");
        DomibusConnectorBackendMessage backendMessage2 = new DomibusConnectorBackendMessage(msg, backendClientBob);
        waitQueue.putMessageInWaitingQueue(backendMessage2);
        
        msg.setConnectorMessageId("msg3");
        msg.setConnectorMessageId("alice");
        DomibusConnectorBackendClientInfo backendClientAlice = new DomibusConnectorBackendClientInfo();
        DomibusConnectorBackendMessage backendMessage3 = new DomibusConnectorBackendMessage(msg, backendClientAlice);

        backendClientAlice.setBackendName("alice"); //no push backend
        backendClientAlice.setBackendPushAddress("pushAddress");        
        waitQueue.putMessageInWaitingQueue(backendMessage3);
        
        Thread.sleep(1000);
        //there are 3 messages on queue (2 from backend bob, 1 from backend alice)
        List<String> connectorMessageIdsForBackend = waitQueue.getConnectorMessageIdForBackend("bob");
                
//        assertThat(connectorMessageIdsForBackend).hasSize(2);
        assertThat(connectorMessageIdsForBackend).hasSameElementsAs(Arrays.asList(new String[] {"msg1", "msg2"}));
    }

    
    /**
     * explorative test to find out how message selector is working
     */
    @Test(timeout=2000)
    public void testJmsMessageSelector() {
        String queueName = "my-test-queue";
        jmsTemplate.send(queueName, (Session s) -> {
            Message msg = s.createMessage();
            msg.setStringProperty("backendname", "bob");
            return msg;
        });
        
        jmsTemplate.setReceiveTimeout(0);
        
        Message receiveSelected = jmsTemplate.receiveSelected(queueName, "backendname='bob'");
        
        assertThat(receiveSelected).isNotNull();
    }

// push is not done by wait queue
//    @Test
//    public void testPushToBackend() throws InterruptedException {
//        final List<String> messageIds = new ArrayList<>();
//        Mockito.doAnswer((Answer<Void>) (InvocationOnMock invocation) -> {
//            DomibusConnectorBackendMessage msg = invocation.getArgumentAt(0, DomibusConnectorBackendMessage.class);
//            String msgId = msg.getDomibusConnectorMessage().getConnectorMessageId();
//            messageIds.add(msgId);
//            return null;
//        }).when(pushMessageToBackend).push(any(DomibusConnectorBackendMessage.class));
//
//
//        DomibusConnectorMessage msg = DomainEntityCreator.createMessage();
//        msg.setConnectorBackendClientName("bob");
//        msg.setConnectorMessageId("msg1");
//
//        DomibusConnectorBackendClientInfo backendClientBob = new DomibusConnectorBackendClientInfo();
//        backendClientBob.setBackendName("bob"); //no push backend
//
//        waitQueue.putMessageInWaitingQueue(new DomibusConnectorBackendMessage(msg, backendClientBob));
//
//        DomibusConnectorBackendClientInfo backendClientAlice = new DomibusConnectorBackendClientInfo();
//        backendClientAlice.setBackendName("alice"); //no push backend
//        backendClientAlice.setBackendPushAddress("pushAddress");
//
//        msg.setConnectorMessageId("msg2");
//        waitQueue.putMessageInWaitingQueue(new DomibusConnectorBackendMessage(msg, backendClientAlice));
//
//        msg.setConnectorMessageId("msg3");
//        waitQueue.putMessageInWaitingQueue(new DomibusConnectorBackendMessage(msg, backendClientAlice));
//
//        Thread.sleep(2000);
//
//        //there are 3 messages send to queue 2 push message wich should be in the messageIds List now
//        assertThat(messageIds).hasSameElementsAs(Arrays.asList(new String[] {"msg2", "msg3"}));
//
//    }


}