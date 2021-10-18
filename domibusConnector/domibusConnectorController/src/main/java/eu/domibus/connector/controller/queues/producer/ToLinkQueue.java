package eu.domibus.connector.controller.queues.producer;

import eu.domibus.connector.controller.queues.QueueHelper;
import eu.domibus.connector.controller.service.HasManageableDlq;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.Queue;
import java.util.List;

import static eu.domibus.connector.controller.queues.JmsConfiguration.TO_LINK_DEAD_LETTER_QUEUE_BEAN;
import static eu.domibus.connector.controller.queues.JmsConfiguration.TO_LINK_QUEUE_BEAN;

@Component
public class ToLinkQueue implements HasManageableDlq {

    @Getter
    private final QueueHelper queueHelper;

    public ToLinkQueue(@Qualifier(TO_LINK_QUEUE_BEAN) Queue destination,
                       @Qualifier(TO_LINK_DEAD_LETTER_QUEUE_BEAN) Queue dlq,
                       JmsTemplate jmsTemplate)
    {
        this.queueHelper = new QueueHelper(destination, dlq, jmsTemplate);
    }

    @Override
    public List<Message> listAllMessages() {
        return queueHelper.listAllMessages();
    }

    @Override
    public List<Message> listAllMessagesInDlq() {
        return queueHelper.listAllMessagesInDlq();
    }

    @Override
    public void moveMsgFromDlqToQueue(String jmsId) {
        queueHelper.moveMsgFromDlqToQueue(jmsId);
    }

    @Override
    public Queue getDlq() {
        return queueHelper.getDlq();
    }

    @Override
    public void deleteMsgFromDlq(String jmsId) {
        queueHelper.deleteMsgFromDlq(jmsId);
    }

    @Override
    public void deleteMsgFromQueue(String jmsId) {
        queueHelper.deleteMsgFromQueue(jmsId);
    }

    @Override
    public void putOnQueue(DomibusConnectorMessage message) {
        queueHelper.putOnQueue(message);
    }

    @Override
    public Queue getQueue() {
        return queueHelper.getQueue();
    }
}
