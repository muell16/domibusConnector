package eu.domibus.connector.controller.queues.producer;

import eu.domibus.connector.controller.service.PutOnQueue;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import javax.transaction.Transactional;

import static eu.domibus.connector.controller.queues.JmsConfiguration.*;

@Component
public class ToCleanupQueue implements PutOnQueue {

    private final Queue destination;
    private final Queue errorDestination;
    private final JmsTemplate jmsTemplate;

    public ToCleanupQueue(@Qualifier(TO_CLEANUP_QUEUE_BEAN) Queue destination,
                          @Qualifier(TO_CLEANUP_ERROR_QUEUE_BEAN) Queue errorDestination,
                          JmsTemplate jmsTemplate) {
        this.destination = destination;
        this.errorDestination = errorDestination;
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    @Transactional
    public void putOnQueue(DomibusConnectorMessage message) {
        jmsTemplate.convertAndSend(destination, message);
    }

    @Override
    @Transactional
    public void putOnErrorQueue(DomibusConnectorMessage message) {
        jmsTemplate.convertAndSend(errorDestination, message);
    }

    @Override
    public Queue getQueue() {
        return destination;
    }


    public Queue getErrorQueue() {
        return errorDestination;
    }

}
