package eu.domibus.connector.controller.queues.producer;

import eu.domibus.connector.controller.service.PutOnQueue;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

import static eu.domibus.connector.controller.queues.JmsConfiguration.*;

@Component
public class ToLinkQueue implements PutOnQueue {

    private final Queue destination;
    private final JmsTemplate jmsTemplate;

    public ToLinkQueue(@Qualifier(TO_LINK_QUEUE_BEAN) Queue destination,
                            JmsTemplate jmsTemplate) {
        this.destination = destination;
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void putOnQueue(DomibusConnectorMessage message) {
        jmsTemplate.convertAndSend(destination, message);
    }

    @Override
    public Queue getQueue() {
        return destination;
    }

}
