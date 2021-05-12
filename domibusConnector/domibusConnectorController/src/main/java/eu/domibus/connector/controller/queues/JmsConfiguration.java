package eu.domibus.connector.controller.queues;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.domibus.connector.common.annotations.DomainModelJsonObjectMapper;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.jms.Queue;
import javax.validation.Validator;

@EnableJms
@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(QueuesConfigurationProperties.class)
public class JmsConfiguration {

    public static final String TO_CONNECTOR_QUEUE_BEAN = "toConnectorQueueBean";
    public static final String TO_CONNECTOR_ERROR_QUEUE_BEAN = "toConnectorErrorQueueBean";
    public static final String TO_LINK_QUEUE_BEAN = "toLinkQueueBean";
    public static final String TO_LINK_ERROR_QUEUE_BEAN = "toLinkErrorQueueBean";
    public static final String TO_CLEANUP_QUEUE_BEAN = "toCleanupQueueBean";
    public static final String TO_CLEANUP_ERROR_QUEUE_BEAN = "toCleanupErrorQueueBean";

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

    @Bean(TO_CONNECTOR_ERROR_QUEUE_BEAN)
    public Queue toConnectorErrorQueueBean(
            QueuesConfigurationProperties config
    ) {
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(config.getToConnectorControllerErrorQueue());
        return activeMQQueue;
    }


    @Bean(TO_LINK_QUEUE_BEAN)
    public Queue toLinkQueue(
            QueuesConfigurationProperties config
    ) {
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(config.getToLinkQueue());
        return activeMQQueue;
    }

    @Bean(TO_LINK_ERROR_QUEUE_BEAN)
    public Queue toLinkErrorQueue(
            QueuesConfigurationProperties config
    ) {
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(config.getToLinkErrorQueue());
        return activeMQQueue;
    }




}
