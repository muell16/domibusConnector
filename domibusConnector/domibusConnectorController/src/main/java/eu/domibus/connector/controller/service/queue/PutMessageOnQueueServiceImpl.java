package eu.domibus.connector.controller.service.queue;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public class PutMessageOnQueueServiceImpl implements PutMessageOnQueue {

    private final static Logger LOGGER = LoggerFactory.getLogger(PutMessageOnQueueServiceImpl.class);

    private JmsTemplate jmsTemplate;

    private String queueName;

    //setter
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    @Override
    public void putMessageOnMessageQueue(@Nonnull DomibusConnectorMessage message) {
        String connectorMessageId = message.getConnectorMessageId();
        LOGGER.debug("Putting message [{}] on queue [{}].", connectorMessageId, queueName);
        try {
            jmsTemplate.send(queueName, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(connectorMessageId);
                }

            });
        } catch (JmsException je) {
            LOGGER.error("Exception putting message [{}] on queue [{}]!", connectorMessageId, queueName);
        }
        LOGGER.info("Message with ID [{}] put on queue [{}].", connectorMessageId, queueName);

    }


}
