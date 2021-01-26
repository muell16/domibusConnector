package eu.domibus.connector.controller.service.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.domibus.connector.common.annotations.DomainModelJsonObjectMapper;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class PutMessageOnQueueServiceImpl implements PutMessageOnQueue {

    private final static Logger LOGGER = LoggerFactory.getLogger(PutMessageOnQueueServiceImpl.class);

    @NotNull
    private JmsTemplate jmsTemplate;

    @NotEmpty
    private String queueName;

    @DomainModelJsonObjectMapper
    private ObjectMapper objectMapper;

    @NotNull
    private DomibusConnectorMessageDirection messageDirection;

    //setter
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public void setMessageDirection(DomibusConnectorMessageDirection messageDirection) {
        this.messageDirection = messageDirection;
    }

    @PostConstruct
    public void init() {
        if (this.messageDirection == null) {
            throw new IllegalArgumentException("Message direction cannot be null!");
        }
//        this.objectMapper = DomainModeJsonObjectMapperFactory.getObjectMapper();

    }

    @Override
    public void putMessageOnMessageQueue(@Nonnull DomibusConnectorMessage message) {
        String connectorMessageId = message.getConnectorMessageIdAsString();
        //TODO: make sure message is persisted!

        if (message.getMessageDetails().getDirection() == null) {
            message.getMessageDetails().setDirection(messageDirection);
        }

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
