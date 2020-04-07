package eu.domibus.connector.controller.service.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.json.DomainModeJsonObjectMapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import static eu.domibus.connector.controller.service.queue.MessageOnQueueConstants.CONFIRMATION_TYPE_PROPERTY_NAME;

public class PutMessageOnQueueServiceImpl implements PutMessageOnQueue {

    private final static Logger LOGGER = LoggerFactory.getLogger(PutMessageOnQueueServiceImpl.class);

    private JmsTemplate jmsTemplate;

    private String queueName;
    private ObjectMapper objectMapper;

    //setter
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    @PostConstruct
    public void init() {
        this.objectMapper = DomainModeJsonObjectMapperFactory.getObjectMapper();
    }

    @Override
    public void putMessageOnMessageQueue(@Nonnull DomibusConnectorMessage message) {
        String connectorMessageId = message.getConnectorMessageId();
        //TODO: make sure message is persisted!

        LOGGER.debug("Putting message [{}] on queue [{}].", connectorMessageId, queueName);
        try {
            jmsTemplate.send(queueName, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    try {
                        return session.createTextMessage(objectMapper.writeValueAsString(message));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Error while serializing message to json", e);
                    }
                }
            });
        } catch (JmsException je) {
            LOGGER.error("Exception putting message [{}] on queue [{}]!", connectorMessageId, queueName);
        }
        LOGGER.info("Message with ID [{}] put on queue [{}].", connectorMessageId, queueName);

    }


}
