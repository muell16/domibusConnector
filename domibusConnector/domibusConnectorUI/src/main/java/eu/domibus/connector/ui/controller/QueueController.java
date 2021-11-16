package eu.domibus.connector.ui.controller;

import eu.domibus.connector.controller.queues.producer.ManageableQueue;
import eu.domibus.connector.controller.queues.producer.ToCleanupQueue;
import eu.domibus.connector.controller.queues.producer.ToConnectorQueue;
import eu.domibus.connector.controller.queues.producer.ToLinkQueue;
import eu.domibus.connector.ui.dto.WebQueue;
import lombok.Getter;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class QueueController {

    private final ToLinkQueue toLinkQueue;
    private final ToConnectorQueue toConnectorQueue;
    private final ToCleanupQueue toCleanupQueue;
    @Getter
    private final MessageConverter converter;

    @Getter
    private final Map<String, ManageableQueue> queueMap;

    public QueueController(ToLinkQueue toLinkQueue, ToConnectorQueue toConnectorQueue, ToCleanupQueue toCleanupQueue, MessageConverter converter) {
        this.toLinkQueue = toLinkQueue;
        this.toConnectorQueue = toConnectorQueue;
        this.toCleanupQueue = toCleanupQueue;
        this.converter = converter;
        this.queueMap = new HashMap<>();
    }

    @EventListener
    public void loadList(ApplicationReadyEvent event) {
        try {
            queueMap.put(toLinkQueue.getDlq().getQueueName(), toLinkQueue);
            queueMap.put(toLinkQueue.getQueue().getQueueName(), toLinkQueue);
            queueMap.put(toConnectorQueue.getQueue().getQueueName(), toConnectorQueue);
            queueMap.put(toConnectorQueue.getDlq().getQueueName(), toConnectorQueue);
            queueMap.put(toCleanupQueue.getQueue().getQueueName(), toCleanupQueue);
            queueMap.put(toCleanupQueue.getDlq().getQueueName(), toCleanupQueue);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void deleteMsg(Message msg) {
        // can be any queue
        toLinkQueue.deleteMsg(msg);
    }

    @Transactional
    public void moveMsgFromDlqToQueue(Message msg) {
        Destination jmsDestination = null;
        String linkQueueName = null;
        String conncetorQueueName = null;
        String cleanupQueueName = null;
        try {
            jmsDestination = msg.getJMSDestination();
            linkQueueName = toLinkQueue.getDlq().getQueueName();
            conncetorQueueName = toConnectorQueue.getDlq().getQueueName();
            cleanupQueueName = toCleanupQueue.getDlq().getQueueName();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        if (jmsDestination != null) {
            final String dlqName = ((ActiveMQQueue) jmsDestination).getQueueName();
            if (dlqName.equals(cleanupQueueName)) {
                toCleanupQueue.moveMsgFromDlqToQueue(msg);
            } else if (dlqName.equals(linkQueueName)) {
                toLinkQueue.moveMsgFromDlqToQueue(msg);
            } else if (dlqName.equals(conncetorQueueName)) {
                toConnectorQueue.moveMsgFromDlqToQueue(msg);
            }
        }
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
            toConnectorWebQueue.setMsgsOnDlq(connectorDlqMsgs.size());

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
