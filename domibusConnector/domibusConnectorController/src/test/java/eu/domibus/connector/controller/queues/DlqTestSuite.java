package eu.domibus.connector.controller.queues;


import eu.domibus.connector.controller.processor.CleanupMessageProcessor;
import eu.domibus.connector.controller.processor.EvidenceMessageProcessor;
import eu.domibus.connector.controller.processor.ToBackendBusinessMessageProcessor;
import eu.domibus.connector.controller.processor.ToGatewayBusinessMessageProcessor;
import eu.domibus.connector.controller.queues.producer.ToCleanupQueue;
import eu.domibus.connector.controller.service.SubmitToLinkService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageId;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.jms.JMSException;
import javax.jms.Queue;

import static eu.domibus.connector.controller.queues.JmsConfiguration.TO_CLEANUP_DEAD_LETTER_QUEUE_BEAN;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = {DlqTestSuite.MyTestContext.class, CleanupMessageProcessor.class}, properties = {"spring.liquibase.enabled=false"})
@ActiveProfiles({"test", "jms-test"})
class DlqTestSuite {

    @SpringBootApplication
    public static class MyTestContext {
    }

    @MockBean
    SubmitToLinkService submitToLinkService;

    @MockBean
    EvidenceMessageProcessor bar;

    @MockBean
    ToBackendBusinessMessageProcessor foo;

    @MockBean
    ToGatewayBusinessMessageProcessor bla;

    @MockBean
    CleanupMessageProcessor cleanupMessageProcessor;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    ToCleanupQueue toCleanupQueueProducer;

    @Autowired
    QueuesConfigurationProperties queuesConfigurationProperties;

    @Autowired
    @Qualifier(TO_CLEANUP_DEAD_LETTER_QUEUE_BEAN)
    Queue toCleanupDlq;

    @Test
    public void testDlq() throws JMSException, InterruptedException {

//        RedeliveryPolicy policy = DlqTestSuite.MyTestContext.connection.getRedeliveryPolicy();
//        policy.setInitialRedeliveryDelay(500);
//        policy.setBackOffMultiplier(2);
//        policy.setUseExponentialBackOff(true);
//        policy.setMaximumRedeliveries(2);

        Mockito.doThrow(new RuntimeException()).when(cleanupMessageProcessor).processMessage(any());
                DomibusConnectorMessage message = DomainEntityCreator.createMessage();
        message.setConnectorMessageId(new DomibusConnectorMessageId("asdfasdfasdf"));
        toCleanupQueueProducer.putOnQueue(message);

        Thread.sleep(120000L);

        System.out.println(Mockito.mockingDetails(cleanupMessageProcessor).getInvocations().size());

        DomibusConnectorMessage domibusConnectorMessage = (DomibusConnectorMessage) jmsTemplate.receiveAndConvert(toCleanupDlq);
        System.out.println(domibusConnectorMessage.getConnectorMessageId());

    }
}



