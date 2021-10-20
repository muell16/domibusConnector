package eu.domibus.connector.ui.controller;

import eu.domibus.connector.controller.queues.producer.ToCleanupQueue;
import eu.domibus.connector.controller.queues.producer.ToConnectorQueue;
import eu.domibus.connector.controller.queues.producer.ToLinkQueue;
import eu.domibus.connector.ui.dto.WebQueue;
import lombok.Getter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class QueueController {

    private final ToLinkQueue toLinkQueue;
    private final ToConnectorQueue toConnectorQueue;
    private final ToCleanupQueue toCleanupQueue;
    private final List<WebQueue> queues;
    private final MessageConverter converter;

    public QueueController(ToLinkQueue toLinkQueue, ToConnectorQueue toConnectorQueue, ToCleanupQueue toCleanupQueue, MessageConverter converter) {
        this.toLinkQueue = toLinkQueue;
        this.toConnectorQueue = toConnectorQueue;
        this.toCleanupQueue = toCleanupQueue;
        this.converter = converter;
        this.queues = getQueues();
    }

    @Transactional
    public List<WebQueue> getQueues() {
        List<WebQueue> result = new ArrayList<>();
        final WebQueue toLinkWebQueue = new WebQueue();
        final WebQueue toConnectorWebQueue = new WebQueue();
        final WebQueue toCleanupWebQueue = new WebQueue();
        try {
            toLinkWebQueue.setName(toLinkQueue.getQueue().getQueueName());
            final List<Message> linkMsgs = toLinkQueue.listAllMessages();
            toLinkWebQueue.setMessages(linkMsgs);
            toLinkWebQueue.setMsgsOnQueue(linkMsgs.size());
            final List<Message> linkDlqMessages = toLinkQueue.listAllMessagesInDlq();
            toLinkWebQueue.setDlqMessages(linkDlqMessages);
            toLinkWebQueue.setMsgsOnDlq(linkDlqMessages.size());

            toConnectorWebQueue.setName(toConnectorQueue.getQueue().getQueueName());
            final List<Message> connectorMsgs = toConnectorQueue.listAllMessages();
            toConnectorWebQueue.setMessages(connectorMsgs);
            toConnectorWebQueue.setMsgsOnQueue(connectorMsgs.size());
            final List<Message> connectorDlqMsgs = toConnectorQueue.listAllMessagesInDlq();
            toConnectorWebQueue.setDlqMessages(connectorDlqMsgs);
            toConnectorWebQueue.setMsgsOnQueue(connectorDlqMsgs.size());

            toCleanupWebQueue.setName(toCleanupQueue.getQueue().getQueueName());
            final List<Message> cleanupMsgs = toCleanupQueue.listAllMessages();
            toCleanupWebQueue.setMessages(cleanupMsgs);
            toCleanupWebQueue.setMsgsOnQueue(cleanupMsgs.size());
            final List<Message> cleanupDlqMsgs = toCleanupQueue.listAllMessagesInDlq();
            toCleanupWebQueue.setDlqMessages(cleanupDlqMsgs);
            toCleanupWebQueue.setMsgsOnDlq(cleanupDlqMsgs.size());

            result.add(toLinkWebQueue);
            result.add(toConnectorWebQueue);
            result.add(toCleanupWebQueue);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return result;
    }

}
