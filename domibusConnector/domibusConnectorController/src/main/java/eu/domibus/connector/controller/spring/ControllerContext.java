
package eu.domibus.connector.controller.spring;

import eu.domibus.connector.controller.service.queue.PutMessageOnQueue;
import eu.domibus.connector.controller.service.queue.PutMessageOnQueueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.jms.ConnectionFactory;

/**
 * Configures Controller Context
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Configuration
@EnableJms
@EnableScheduling
@PropertySource("classpath:/eu/domibus/connector/controller/spring/default-connector.properties")
public class ControllerContext {

    public static final String NON_TRANSACTED_JMS_LISTENER_CONTAINER_FACTORY_BEAN_NAME = "nonTransactedJmsListenerContainerFactory";

    @Value("${domibus.connector.internal.backend.to.controller.queue}")
    private String internalBackendToControllerQueueName;

    @Value("${domibus.connector.internal.gateway.to.controller.queue}")
    private String internalGWToControllerQueueName;

    @Autowired
    JmsTemplate jmsTemplate;

    //create non transacted jmsListenerContainerFactory: https://docs.spring.io/spring-boot/docs/current/reference/html/howto-messaging.html
    @Bean(NON_TRANSACTED_JMS_LISTENER_CONTAINER_FACTORY_BEAN_NAME)
    public DefaultJmsListenerContainerFactory nonTransactedJmsListenerContainerFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory listenerFactory = new DefaultJmsListenerContainerFactory();

        listenerFactory.setTransactionManager(null);
        listenerFactory.setSessionTransacted(false);

        // This provides all boot's default to this factory, including the message converter
        configurer.configure(listenerFactory, connectionFactory);
        //override boot defaults to disable transactions
        listenerFactory.setTransactionManager(null);
        listenerFactory.setSessionTransacted(false);

        return listenerFactory;
    }

    @Bean
    @Qualifier(PutMessageOnQueue.BACKEND_TO_CONTROLLER_QUEUE)
    public PutMessageOnQueue putMessageOnBackendToControllerQueue() {
        PutMessageOnQueueServiceImpl putMessageOnQueueService = new PutMessageOnQueueServiceImpl();
        putMessageOnQueueService.setQueueName(internalBackendToControllerQueueName);
        putMessageOnQueueService.setJmsTemplate(jmsTemplate);
        return putMessageOnQueueService;
    }

    @Bean
    @Qualifier(PutMessageOnQueue.GATEWAY_TO_CONTROLLER_QUEUE)
    public PutMessageOnQueue putMessageOnBackendToGatewayToControllerQueue() {
        PutMessageOnQueueServiceImpl putMessageOnQueueService = new PutMessageOnQueueServiceImpl();
        putMessageOnQueueService.setQueueName(internalGWToControllerQueueName);
        putMessageOnQueueService.setJmsTemplate(jmsTemplate);
        return putMessageOnQueueService;
    }


}
