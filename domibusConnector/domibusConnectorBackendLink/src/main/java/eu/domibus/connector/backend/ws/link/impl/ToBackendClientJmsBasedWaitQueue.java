
package eu.domibus.connector.backend.ws.link.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.backend.ws.link.spring.BackendLinkInternalWaitQueueProperties;
import eu.domibus.connector.common.annotations.DomainModelJsonObjectMapper;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.jms.*;

import eu.domibus.connector.tools.logging.SetMessageOnLoggingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static eu.domibus.connector.backend.ws.link.spring.WSBackendLinkContextConfiguration.WS_BACKEND_LINK_PROFILE;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Profile(WS_BACKEND_LINK_PROFILE)
@Component
public class ToBackendClientJmsBasedWaitQueue implements MessageToBackendClientWaitQueue {

    private static final Logger LOGGER = LoggerFactory.getLogger(ToBackendClientJmsBasedWaitQueue.class);

    private static final String BACKEND_CLIENT_NAME = "BACKEND_CLIENT_NAME";

    private static final String CONNECTOR_MESSAGE_ID = "CONNECTOR_MESSAGE_ID";

    private static final String CONNECTOR_BACKEND_IS_PUSH_BACKEND = "CONNECTOR_BACKEND_IS_PUSH_BACKEND";

    private static final String BACKEND_CLIENT_DELIVERY_RETRIES = "BACKEND_CLIENT_DELIVERY_RETRIES";


    @Autowired
    private BackendLinkInternalWaitQueueProperties backendLinkInternalWaitQueueProperties;

    @Autowired(required = false) //can be null if there is no push impl
    @Nullable
    private
    PushMessageToBackendClient pushMessageToBackendCallback;
       
    @Autowired
    private JmsTemplate jmsTemplate;

    private long receiveTimeout;
    private String waitQueueName;

    @DomainModelJsonObjectMapper
    private ObjectMapper mapper;


    @PostConstruct
    public void init() {
        this.waitQueueName = backendLinkInternalWaitQueueProperties.getName();
        this.receiveTimeout = backendLinkInternalWaitQueueProperties.getReceiveTimeout();

//        this.mapper = DomainModeJsonObjectMapperFactory.getObjectMapper();

    }

    @Override
    public void putMessageInWaitingQueue(final DomibusConnectorBackendMessage backendMessage) {
        if (backendMessage == null) {
            throw new IllegalArgumentException("#putMessageInWaitingQueue: backendMessage is not allowed to be null!");
        }
        DomibusConnectorMessage message = backendMessage.getDomibusConnectorMessage();
        if (message == null) {
            throw new IllegalArgumentException("#putMessageInWaitingQueue: message must be not null in backendMessage!");
        }
        DomibusConnectorBackendClientInfo backendClientInfo = backendMessage.getBackendClientInfo();
        if (backendClientInfo == null) {
            throw new IllegalArgumentException("#putMessageInWaitingQueue: backendClientInfo must not be null in backendMessage!");
        }

        final String backendClientName = message.getMessageDetails().getConnectorBackendClientName();
        final String connectorMessageId = message.getConnectorMessageIdAsString();
        LOGGER.debug("#putMessageInWaitingQueue: put message id [{}] for backendClientName [{}] in waiting queue [{}]",
                connectorMessageId, backendClientName, waitQueueName);
        jmsTemplate.send(waitQueueName, (Session session) -> {

            try {
                //Message msg = session.createObjectMessage(backendMessage);
                Message msg = session.createTextMessage(mapper.writeValueAsString(backendMessage));
                msg.setIntProperty(BACKEND_CLIENT_DELIVERY_RETRIES, 0);
                msg.setStringProperty(BACKEND_CLIENT_NAME, backendClientName);
                msg.setStringProperty(CONNECTOR_MESSAGE_ID, connectorMessageId);
                msg.setBooleanProperty(CONNECTOR_BACKEND_IS_PUSH_BACKEND, backendClientInfo.isPushBackend());
                msg.setIntProperty(BACKEND_CLIENT_DELIVERY_RETRIES, 0);
                LOGGER.trace("#putMessageInWaitingQueue: Send message [{}] to queue [{}]", msg, waitQueueName);
                return msg;

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }


        });                
    }
        
    @JmsListener(destination="#{backendLinkInternalWaitQueueProperties.getName()}", selector=CONNECTOR_BACKEND_IS_PUSH_BACKEND + " = TRUE")
    public void pushToBackend(TextMessage msg) throws JMSException {
        try {
            LOGGER.trace("#pushToBackend: jms listener received jms message [{}]", msg);
            DomibusConnectorBackendMessage backendMessage = mapper.readValue(msg.getText(), DomibusConnectorBackendMessage.class);
//        DomibusConnectorBackendMessage backendMessage = (DomibusConnectorBackendMessage) msg.getObject();
            SetMessageOnLoggingContext.putConnectorMessageIdOnMDC(backendMessage.getDomibusConnectorMessage().getConnectorMessageIdAsString());
            pushMessageToBackendCallback.push(backendMessage);
            SetMessageOnLoggingContext.putConnectorMessageIdOnMDC((String)null);
        } catch (JsonProcessingException e) {
           throw new RuntimeException("Was not able to deserialize message back from queue " + backendLinkInternalWaitQueueProperties.getName(), e);
        }
    }

    @Override
    public List<DomibusConnectorMessage> getConnectorMessageIdForBackend(String backendName) {
        LOGGER.debug("#getConnectorMessageIdForBackend: lookup waiting messages on queue [{}] for backendName [{}]", waitQueueName, backendName);
        List<DomibusConnectorMessage> waitingMessages = new ArrayList<>();
        try {            
            jmsTemplate.setReceiveTimeout(receiveTimeout);

//            ObjectMessage receiveSelected = null;
            TextMessage receiveSelected = null;
            do {
                String selector = String.format("%s = '%s' AND %s = FALSE", BACKEND_CLIENT_NAME, backendName, CONNECTOR_BACKEND_IS_PUSH_BACKEND);
                LOGGER.debug("#getConnectorMessageIdForBackend: try to receiveSelected message from queue [{}] with selector [{}]", waitQueueName, selector);
//                receiveSelected = (ObjectMessage) jmsTemplate.receiveSelected(waitQueueName, selector);
                receiveSelected = (TextMessage) jmsTemplate.receiveSelected(waitQueueName, selector);
                LOGGER.trace("#getConnectorMessageIdForBackend: received message [{}]", receiveSelected);
                if (receiveSelected != null) {
                    try {
//                    DomibusConnectorBackendMessage msg = (DomibusConnectorBackendMessage) receiveSelected.getObject();
                        DomibusConnectorBackendMessage msg = mapper.readValue(receiveSelected.getText(), DomibusConnectorBackendMessage.class);
                        LOGGER.trace("#getConnectorMessageIdForBackend: got message [{}] from queue [{}]", msg, waitQueueName);
                        waitingMessages.add(msg.getDomibusConnectorMessage());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Was not able to deserialize message back from queue " + backendLinkInternalWaitQueueProperties.getName(), e);
                    }
                }
            } while (receiveSelected != null);
            
        } catch (JMSException jmsException) {
            LOGGER.error("A jms exception occured during reading messages from waiting queue [{}]", waitQueueName);
            throw new org.springframework.jms.UncategorizedJmsException(jmsException);
        }
        LOGGER.trace("#getConnectorMessageIdForBackend: found follwing message ids [{}] on queue [{}]", waitingMessages, waitQueueName);
        return waitingMessages;
    }
    
}
