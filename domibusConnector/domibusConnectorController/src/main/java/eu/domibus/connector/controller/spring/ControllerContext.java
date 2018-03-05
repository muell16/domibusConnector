
package eu.domibus.connector.controller.spring;

import eu.domibus.connector.controller.service.queue.PutMessageOnQueue;
import eu.domibus.connector.controller.service.queue.PutMessageOnQueueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;

/**
 * Configures Controller Context
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Configuration
@Import(value = { QuartzContext.class })
public class ControllerContext {

    @Value("${domibus.connector.internal.backend.to.controller.queue}")
    private String internalBackendToControllerQueueName;

    @Value("${domibus.connector.internal.gateway.to.controller.queue}")
    private String internalGWToControllerQueueName;

    @Autowired
    JmsTemplate jmsTemplate;

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
