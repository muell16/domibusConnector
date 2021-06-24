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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

import javax.jms.JMSException;
import javax.jms.Queue;
import java.time.Duration;

import static eu.domibus.connector.controller.queues.JmsConfiguration.TO_CLEANUP_DEAD_LETTER_QUEUE_BEAN;
import static org.assertj.core.api.Assertions.assertThat;
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

    @Autowired
    TransactionTemplate txTemplate;

    @Test
    public void testDlq() throws JMSException, InterruptedException {

//        RedeliveryPolicy policy = DlqTestSuite.MyTestContext.connection.getRedeliveryPolicy();
//        policy.setInitialRedeliveryDelay(500);
//        policy.setBackOffMultiplier(2);
//        policy.setUseExponentialBackOff(true);
//        policy.setMaximumRedeliveries(2);

        final DomibusConnectorMessage[] domibusConnectorMessage = new DomibusConnectorMessage[1];

        //stop test latest after 40s
        Assertions.assertAll("Should return Message from DLQ",
                () -> Assertions.assertTimeoutPreemptively(Duration.ofSeconds(40), () -> {

                    Mockito.doThrow(new RuntimeException("FAIL MESSAGE")).when(cleanupMessageProcessor).processMessage(any());
                    DomibusConnectorMessage message = DomainEntityCreator.createMessage();
                    message.setConnectorMessageId(new DomibusConnectorMessageId("asdfasdfasdf"));

                    txTemplate.executeWithoutResult(tx -> toCleanupQueueProducer.putOnQueue(message));

//            Thread.sleep(20000L);

                    System.out.println("\n\n######\n" + Mockito.mockingDetails(cleanupMessageProcessor).getInvocations().size() + "\n\n###");

                    //wait 20s for a message to rcv
//            jmsTemplate.setReceiveTimeout(20000);
//            jmsTemplate.setSessionTransacted(true);

                    domibusConnectorMessage[0] = txTemplate.execute(status -> (DomibusConnectorMessage) jmsTemplate.receiveAndConvert(toCleanupDlq));

                    System.out.println("\n\n######\n END OF LAMBDA EXECUTION \n\n###");
                }),
                () -> assertThat(domibusConnectorMessage[0]).isNotNull()
        );

        System.out.println("\n\n######\n" + domibusConnectorMessage[0].getConnectorMessageId() + "\n\n###");

    }
}



