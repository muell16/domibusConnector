package eu.domibus.connector.controller.service.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.domibus.connector.domain.model.json.DomainModeJsonObjectMapperFactory;
import eu.domibus.connector.persistence.dao.DomibusConnectorEvidenceDao;
import eu.domibus.connector.tools.logging.SetMessageOnLoggingContext;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.xml.soap.Text;

@Component
public class GetDomibusConnectorMessageFromJmsMessageImpl implements GetDomibusConnectorMessageFromJmsMessage {

    private static final Logger LOGGER  = LoggerFactory.getLogger(GetDomibusConnectorMessageFromJmsMessageImpl.class);

    @Autowired
    private DomibusConnectorMessagePersistenceService persistenceService;
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        this.objectMapper = DomainModeJsonObjectMapperFactory.getObjectMapper();
    }

    @Override
    public DomibusConnectorMessage getMessage(Message message) {
        String jmsDestination = "";
        try {
            if (message instanceof TextMessage) {
                jmsDestination = message.getJMSDestination().toString();
//                ObjectMessage msg = (ObjectMessage) message;
                TextMessage msg = (TextMessage) message;
                DomibusConnectorMessage domibusConnectorMessage = objectMapper.readValue(msg.getText(), DomibusConnectorMessage.class);
                if(LOGGER.isDebugEnabled()) {
                	if(domibusConnectorMessage.getMessageContent()!=null && domibusConnectorMessage.getMessageContent().getXmlContent()!=null)
                		LOGGER.debug("Business content XML after queue: {}", new String(domibusConnectorMessage.getMessageContent().getXmlContent()));
                }
//                Object obj = msg.getObject();
//                if (!(obj instanceof  DomibusConnectorMessage)) {
//                    throw new IllegalArgumentException("Object message from queue must be of type DomibusConnectorMessage!");
//                }
//                DomibusConnectorMessage domibusConnectorMessage = (DomibusConnectorMessage) obj;

                String connectorMessageId = domibusConnectorMessage.getConnectorMessageId();
                SetMessageOnLoggingContext.putConnectorMessageIdOnMDC(connectorMessageId);
                LOGGER.info("received messageID [{}] from queue [{}].", connectorMessageId, jmsDestination);
//                DomibusConnectorMessage connectorMessage = null;
//                try {
//                    connectorMessage = persistenceService.findMessageByConnectorMessageId(connectorMessageId);
//                } catch (PersistenceException e) {
//                    LOGGER.error("Message [{}] could not be loaded from database!", connectorMessageId, e);
//                }
//
//                if (connectorMessage == null) {
//                    LOGGER.error("Message loaded from db with id [{}] is null!", connectorMessageId);
//                    throw new RuntimeException("Cannot get message null from queue!");
//                }
                return domibusConnectorMessage;

            } else {
                throw new IllegalArgumentException("Message must be of type TextMessage");
            }
        } catch (JMSException e) {
            LOGGER.error("JMS Exception while receiving message from queue [{}].", jmsDestination);
            throw new RuntimeException("A jms exception occured while loading message", e);
        } catch (JsonProcessingException e) {
            LOGGER.error("JSON exception while receiving message from queue [{}].", jmsDestination);
            throw new RuntimeException("A json exception occured while loading message", e);
        }
    }
}