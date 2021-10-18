package eu.domibus.connector.controller.queues;

import eu.domibus.connector.controller.queues.producer.ToLinkQueue;
import eu.domibus.connector.controller.service.HasManageableDlq;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class QueueHelper implements HasManageableDlq {

    private static final Logger LOGGER = LogManager.getLogger(ToLinkQueue.class);

    @Getter
    private final Queue destination;
    @Getter
    private final Queue dlq;
    private final JmsTemplate jmsTemplate;

    public QueueHelper(Queue destination, Queue dlq, JmsTemplate jmsTemplate) {
        this.destination = destination;
        this.dlq = dlq;
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void putOnQueue(DomibusConnectorMessage message) {
        jmsTemplate.convertAndSend(destination, message);
    }

    @Override
    public List<Message> listAllMessages() {
        return list(destination);
    }

    @Override
    public List<Message> listAllMessagesInDlq() {
        return list(dlq);
    }

    private List<Message> list(Queue destination) {
        return jmsTemplate.browse(destination, (BrowserCallback<List<Message>>) (s, qb) -> {
            List<Message> result = new ArrayList<>();
            @SuppressWarnings("unchecked") final Enumeration<Message> enumeration = qb.getEnumeration();
            while (enumeration.hasMoreElements()) {
                final Message message = enumeration.nextElement();
                result.add(message);
            }
            return result;
        });
    }

    @Override
    public void moveMsgFromDlqToQueue(String jmsId) {
        final DomibusConnectorMessage c = (DomibusConnectorMessage) jmsTemplate.receiveSelectedAndConvert(dlq, "JMSMessageID = '" + jmsId + "'");
        putOnQueue(c);
    }

    @Override
    public void deleteMsgFromDlq(String jmsId) {
        deleteMsg(jmsId, dlq);
    }

    @Override
    public void deleteMsgFromQueue(String jmsId) {
        deleteMsg(jmsId, destination);
    }

    private void deleteMsg(String jmsId, Destination destination) {
        final Message m = jmsTemplate.receiveSelected(destination, "JMSMessageID = '" + jmsId + "'");
        try {
            if (m != null) {
                m.acknowledge();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Queue getQueue() {
        return destination;
    }
}
