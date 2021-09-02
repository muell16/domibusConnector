package eu.domibus.connector.controller.queues;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.domibus.connector.common.annotations.DomainModelJsonObjectMapper;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ErrorHandler;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.validation.Validator;

@EnableJms
@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(QueuesConfigurationProperties.class)
public class JmsConfiguration {

    public static final String TO_CONNECTOR_QUEUE_BEAN = "toConnectorQueueBean";
    public static final String TO_CONNECTOR_DEAD_LETTER_QUEUE_BEAN = "toConnectorDeadLetterQueueBean";
    public static final String TO_LINK_QUEUE_BEAN = "toLinkQueueBean";
    public static final String TO_LINK_DEAD_LETTER_QUEUE_BEAN = "toLinkDeadLetterQueueBean";
    public static final String TO_CLEANUP_QUEUE_BEAN = "toCleanupQueueBean";
    public static final String TO_CLEANUP_DEAD_LETTER_QUEUE_BEAN = "toCleanupDeadLetterQueueBean";

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter(
            @DomainModelJsonObjectMapper ObjectMapper objectMapper,
            final Validator validator
            ) {
        //using a message converter which calls bean validation before serialisation object
        MessageConverter converter = new ValidationMappingJackson2MessageConverter(objectMapper, validator);
        return converter;
    }


    @Bean(TO_CONNECTOR_QUEUE_BEAN)
    public Queue toConnectorQueue(
            QueuesConfigurationProperties config
    ) {
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(config.getToConnectorControllerQueue());
        return activeMQQueue;
    }

    @Bean(TO_CONNECTOR_DEAD_LETTER_QUEUE_BEAN)
    public Queue toConnectorErrorQueueBean(
            QueuesConfigurationProperties config
    ) {
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(config.getToConnectorControllerDeadLetterQueue());
        return activeMQQueue;
    }


    @Bean(TO_LINK_QUEUE_BEAN)
    public Queue toLinkQueue(
            QueuesConfigurationProperties config
    ) {
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(config.getToLinkQueue());
        return activeMQQueue;
    }

    @Bean(TO_LINK_DEAD_LETTER_QUEUE_BEAN)
    public Queue toLinkErrorQueue(
            QueuesConfigurationProperties config
    ) {
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(config.getToLinkDeadLetterQueue());
        return activeMQQueue;
    }

    @Bean(TO_CLEANUP_QUEUE_BEAN)
    public Queue toCleanupQueue(
            QueuesConfigurationProperties config
    ) {
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(config.getCleanupQueue());
        return activeMQQueue;
    }

    @Bean(TO_CLEANUP_DEAD_LETTER_QUEUE_BEAN)
    public Queue toCleanupErrorQueue(
            QueuesConfigurationProperties config
    ) {
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(config.getCleanupDeadLetterQueue());
        return activeMQQueue;
    }


}