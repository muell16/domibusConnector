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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

import javax.jms.ConnectionFactory;
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

    @BeforeEach
    public void beforeEach() {
        nonXaJmsTemplate = new JmsTemplate(nonXaConnectionFactory);
        nonXaJmsTemplate.setMessageConverter(converter);
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
    JmsTemplate nonXaJmsTemplate;

    @Autowired
    ToCleanupQueue toCleanupQueueProducer;

    @Autowired
    QueuesConfigurationProperties queuesConfigurationProperties;

    @Autowired
    @Qualifier(TO_CLEANUP_DEAD_LETTER_QUEUE_BEAN)
    Queue toCleanupDlq;

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
    public void when_message_handling_in_clean_up_queue_fails_the_message_should_end_up_in_the_cleanupqueue_deadletterqueue() {

        // Arrange
        Mockito.doThrow(new RuntimeException("FAIL MESSAGE")).when(cleanupMessageProcessor).processMessage(any());

        DomibusConnectorMessage message = DomainEntityCreator.createMessage();
        message.setConnectorMessageId(new DomibusConnectorMessageId("asdfasdfasdf"));

        final DomibusConnectorMessage[] domibusConnectorMessage = new DomibusConnectorMessage[1];

        // Act
        txTemplate.executeWithoutResult(tx -> toCleanupQueueProducer.putOnQueue(message));

        // Assert
        Assertions.assertAll("Should return Message from DLQ",
                () -> Assertions.assertTimeoutPreemptively(Duration.ofSeconds(40), () -> {
                    nonXaJmsTemplate.setReceiveTimeout(20000);
                    nonXaJmsTemplate.setSessionTransacted(false);
                    domibusConnectorMessage[0] = (DomibusConnectorMessage) nonXaJmsTemplate.receiveAndConvert(queuesConfigurationProperties.getCleanupDeadLetterQueue());
                }),
                () -> assertThat(domibusConnectorMessage[0]).isNotNull(),
                () -> assertThat(domibusConnectorMessage[0].getConnectorMessageId().getConnectorMessageId()).isEqualTo("asdfasdfasdf")
        );
    }
}



