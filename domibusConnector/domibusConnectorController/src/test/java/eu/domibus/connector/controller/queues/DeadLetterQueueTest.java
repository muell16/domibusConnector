package eu.domibus.connector.controller.queues;


import eu.domibus.connector.controller.processor.CleanupMessageProcessor;
//import eu.ecodex.dc5.flow.flows.ConfirmationMessageProcessingFlow;
import eu.ecodex.dc5.flow.flows.ProcessIncomingBusinessMessageFlow;
import eu.ecodex.dc5.flow.flows.ProcessOutgoingBusinessMessageFlow;
import eu.domibus.connector.controller.queues.producer.ToCleanupQueue;
import eu.domibus.connector.controller.queues.producer.ToConnectorQueue;
import eu.domibus.connector.controller.queues.producer.ToLinkQueue;
import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.controller.service.SubmitToLinkService;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DomibusConnectorMessageId;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

import javax.jms.ConnectionFactory;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = {DeadLetterQueueTest.MyTestContext.class}, properties = {"spring.liquibase.enabled=false"})
@ActiveProfiles({"test", "jms-test"})
@Disabled
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class DeadLetterQueueTest {

    @SpringBootApplication
    public static class MyTestContext {
    }

    @BeforeEach
    public void beforeEach() {
        nonXaJmsTemplate = new JmsTemplate(nonXaConnectionFactory);
        nonXaJmsTemplate.setMessageConverter(converter);
        Mockito.reset(submitToLinkService);
    }

    @MockBean
    SubmitToLinkService submitToLinkService;

    @MockBean
    SubmitToConnector submitToConnector;

//    @MockBean
//    ConfirmationMessageProcessingFlow evidenceMessageProcessor;

    @MockBean
    ProcessIncomingBusinessMessageFlow processIncomingBusinessMessageFlow;

    @MockBean
    ProcessOutgoingBusinessMessageFlow processOutgoingBusinessMessageFlow;

    @MockBean
    CleanupMessageProcessor cleanupMessageProcessor;

    @Autowired
    ToLinkQueue toLinkQueueProducer;

    @Autowired
    ToConnectorQueue toConnectorQueueProducer;

    @Autowired
    JmsTemplate nonXaJmsTemplate;

    @Autowired
    ToCleanupQueue toCleanupQueueProducer;

    @Autowired
    QueuesConfigurationProperties queuesConfigurationProperties;

    @Autowired
    TransactionTemplate txTemplate;

    // Inject the primary (XA aware) ConnectionFactory
    @Autowired
    private ConnectionFactory defaultConnectionFactory;

    // Inject the XA aware ConnectionFactory (uses the alias and injects the same as above)
    @Autowired
    @Qualifier("xaJmsConnectionFactory")
    private ConnectionFactory xaConnectionFactory;

    // Inject the non-XA aware ConnectionFactory
    @Autowired
    @Qualifier("nonXaJmsConnectionFactory")
    private ConnectionFactory nonXaConnectionFactory;

    @Autowired
    MessageConverter converter;

    @Test
    public void when_message_handling_of_clean_up_queue_messages_fails_the_message_should_end_up_in_the_dead_letter_queue_configured_for_that_queue() {

        // Arrange
        Mockito.doThrow(new RuntimeException("FAIL MESSAGE")).when(cleanupMessageProcessor).processMessage(any());

        DC5Message message = DomainEntityCreator.createMessage();
        message.setConnectorMessageId(new DomibusConnectorMessageId("asdfasdfasdf"));

        final DC5Message[] DC5Message = new DC5Message[1];

        // Act
        txTemplate.executeWithoutResult(tx -> toCleanupQueueProducer.putOnQueue(message));

//        This can be used in conjunction with jconsole to find whether queues have been created and contain messages
//        Thread.sleep(1000000000L);

        // Assert
        Assertions.assertAll("Should return Message from DLQ",
                () -> Assertions.assertTimeoutPreemptively(Duration.ofSeconds(20), () -> {
                    nonXaJmsTemplate.setReceiveTimeout(20000);
                    nonXaJmsTemplate.setSessionTransacted(false);
                    DC5Message[0] = (DC5Message) nonXaJmsTemplate.receiveAndConvert(queuesConfigurationProperties.getCleanupDeadLetterQueue());
                }),
                () -> assertThat(DC5Message[0])
                        .isNotNull()
                        .extracting(c -> c.getConnectorMessageId().getConnectorMessageId()).isEqualTo("asdfasdfasdf")
        );
    }

    @Test
    public void when_message_handling_in_transition_from_gateway_to_backend_fails_the_message_should_end_up_in_the_dead_letter_queue_configured_for_connector_to_link_queue() throws InterruptedException {

        // Arrange
        Mockito.doThrow(new RuntimeException("FAIL MESSAGE")).when(submitToLinkService).submitToLink(any());

        DC5Message message = DomainEntityCreator.createMessage();
        message.setConnectorMessageId(new DomibusConnectorMessageId("qwerqwerqwrerttz"));

        final DC5Message[] DC5Message = new DC5Message[2];

        // Act
        txTemplate.executeWithoutResult(tx -> toLinkQueueProducer.putOnQueue(message));

//        Thread.sleep(300000000);

        // Assert
        Assertions.assertAll("Should return Message from DLQ",
                () -> Assertions.assertTimeoutPreemptively(Duration.ofSeconds(40), () -> {
                    nonXaJmsTemplate.setReceiveTimeout(20000);
//                    nonXaJmsTemplate.setSessionTransacted(false);
                    DC5Message[0] = (DC5Message) nonXaJmsTemplate.receiveAndConvert(queuesConfigurationProperties.getToLinkDeadLetterQueue());
                    DC5Message[1] = (DC5Message) nonXaJmsTemplate.receiveAndConvert(queuesConfigurationProperties.getToLinkQueue());
                }),
                () -> assertThat(DC5Message[0])
                        .isNotNull()
                        .extracting(c -> c.getConnectorMessageId().getConnectorMessageId()).isEqualTo("qwerqwerqwrerttz"),
                () -> assertThat(DC5Message[1])
                        .isNull()
        );
    }

    @Test
    public void when_message_handling_in_transition_from_gateway_to_connector_fails_the_message_should_end_up_in_the_connector_dead_letter_queue() {

        // Arrange
//        Mockito.doThrow(new RuntimeException("FAIL MESSAGE")).when(evidenceMessageProcessor).processMessage(any());
        Mockito.doThrow(new RuntimeException("FAIL MESSAGE")).when(processOutgoingBusinessMessageFlow).processMessage(any());
        Mockito.doThrow(new RuntimeException("FAIL MESSAGE")).when(processIncomingBusinessMessageFlow).processMessage(any());

        DC5Message message = DomainEntityCreator.createMessage();
        message.setConnectorMessageId(new DomibusConnectorMessageId("yxcvyxcvyxcv"));

        final DC5Message[] DC5Message = new DC5Message[1];

        // Act
        txTemplate.executeWithoutResult(tx -> toConnectorQueueProducer.putOnQueue(message));

        // Assert
        Assertions.assertAll("Should return Message from DLQ",
                () -> Assertions.assertTimeoutPreemptively(Duration.ofSeconds(40), () -> {
                    nonXaJmsTemplate.setReceiveTimeout(20000);
                    nonXaJmsTemplate.setSessionTransacted(false);
                    DC5Message[0] = (DC5Message) nonXaJmsTemplate.receiveAndConvert(queuesConfigurationProperties.getToConnectorControllerDeadLetterQueue());
                }),
                () -> assertThat(DC5Message[0])
                        .isNotNull()
                        .extracting(c -> c.getConnectorMessageId().getConnectorMessageId()).isEqualTo("yxcvyxcvyxcv")
        );
    }
}



