package eu.domibus.connector.controller.queues.producer;

import eu.domibus.connector.controller.service.HasManageableDlq;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import static eu.domibus.connector.controller.queues.JmsConfiguration.TO_LINK_QUEUE_BEAN;

@Component
public class ToLinkQueue implements HasManageableDlq {

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


//    myJmsTemplate.browseSelected(myQueue, myCriteria, new BrowserCallback<Integer>() {
//        @Overridepublic Integer doInJms(Session s, QueueBrowser qb) throws JMSException {
//            @SuppressWarnings("unchecked")final Enumeration<Message> e = qb.getEnumeration();
//            int count = 0;
//            while (e.hasMoreElements()) {
//                final Message m = e.nextElement();
//                final TextMessage tm = (TextMessage) MyClass.this.jmsQueueTemplate.receiveSelected(
//                        MyClass.this.myQueue, "JMSMessageID = '" + m.getJMSMessageID() + "'");
//
//                myMessages.add(tm);
//                count++;
//            }
//
//            return count;
//        }
//    });

    @Override
    public List<Message> listAllMessagesWithinQueue() {
        return jmsTemplate.browse(destination, (BrowserCallback<List<Message>>) (s, qb) -> {
            List<Message> result = new ArrayList<>();
            @SuppressWarnings("unchecked")final Enumeration<Message> enumeration = qb.getEnumeration();
            while (enumeration.hasMoreElements()) {
                final Message message = enumeration.nextElement();
                result.add(message);
            }
            return result;
        });
    }

    //fetch specific message from queue
    public Optional<Message> fetchMsg(String jmsId) {
        final List<Message> messages = listAllMessagesWithinQueue();
        final Optional<Message> any = messages.stream().filter(m -> {
            try {
                return m.getJMSMessageID().equals(jmsId);
            } catch (JMSException e) {
                e.printStackTrace();
                return false;
            }
        }).findAny();
        // TODO this needs to happen elsewhere, also I did not find jmsId, mabe this is messageId ???
       return any.map(m -> jmsTemplate.receiveSelected(destination, "jmsId = " + jmsId));
    }

    @Override
    public Queue getQueue() {
        return destination;
    }
}
