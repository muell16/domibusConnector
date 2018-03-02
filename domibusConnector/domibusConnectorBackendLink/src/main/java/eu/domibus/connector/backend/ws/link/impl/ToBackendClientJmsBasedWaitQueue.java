
package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.backend.persistence.model.BackendClientInfo;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.validation.constraints.Null;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.MessagePostProcessor;
import static org.springframework.jms.support.destination.JmsDestinationAccessor.RECEIVE_TIMEOUT_NO_WAIT;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component
public class ToBackendClientJmsBasedWaitQueue implements MessageToBackendClientWaitQueue {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(ToBackendClientJmsBasedWaitQueue.class);
    
    private final static String BACKEND_CLIENT_NAME = "BACKEND_CLIENT_NAME";
    
    private final static String CONNECTOR_MESSAGE_ID = "CONNECTOR_MESSAGE_ID";
    
    private final static String CONNECTOR_BACKEND_CLIENT_INFO = "CONNECTOR_BACKEND_CLIENT_INFO";
    
    private final static String CONNECTOR_BACKEND_IS_PUSH_BACKEND = "CONNECTOR_BACKEND_IS_PUSH_BACKEND";
    
    private final static String BACKEND_CLIENT_DELIVERY_RETRIES = "BACKEND_CLIENT_DELIVERY_RETRIES";
    
    private final static String WAIT_QUEUE_PROPERTY_NAME = "${connector.backend.internal.wait-queue.name}";
    
    @Value(WAIT_QUEUE_PROPERTY_NAME)
    private String waitQueueName;
    
    @Value("${connector.backend.push.max-retries:3}")
    private int maxDeliveryRetries;
    
    @Value("${connector.backend.internal.wait-queue.receive-timeout:10}")
    private long receiveTimeout;
    
//    @Autowired(required = false) //can be null if there is no push impl
//    private @Nullable
//    PushMessageToBackendClient pushMessageToBackendCallback;
       
    @Autowired
    private JmsTemplate jmsTemplate;

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
        final String connectorMessageId = message.getConnectorMessageId();
        LOGGER.debug("#putMessageInWaitingQueue: put message id [{}] for backendClientName [{}] in waiting queue [{}]",
                connectorMessageId, backendClientName, waitQueueName);
        jmsTemplate.send(waitQueueName, (Session session) -> {
            Message msg = session.createMessage();
            msg.setIntProperty(BACKEND_CLIENT_DELIVERY_RETRIES, 0);
            msg.setStringProperty(BACKEND_CLIENT_NAME, backendClientName);
            msg.setStringProperty(CONNECTOR_MESSAGE_ID, connectorMessageId);
            msg.setBooleanProperty(CONNECTOR_BACKEND_IS_PUSH_BACKEND, backendClientInfo.isPushBackend());                
            msg.setIntProperty(BACKEND_CLIENT_DELIVERY_RETRIES, 0);
            LOGGER.trace("#putMessageInWaitingQueue: Send message [{}] to queue [{}]", msg, waitQueueName);
            return msg;          
        });                
    }
        
//    @JmsListener(destination=WAIT_QUEUE_PROPERTY_NAME, selector=CONNECTOR_BACKEND_IS_PUSH_BACKEND + " = TRUE")
//    public void pushToBackend(Message msg) throws JMSException {
//        String connectorMessageId = msg.getStringProperty(CONNECTOR_MESSAGE_ID);
//        String backendName = msg.getStringProperty(BACKEND_CLIENT_NAME);
//        //pushMessageToBackendCallback.push(connectorMessageId, backendName);
//    }

    @Override
    public List<String> getConnectorMessageIdForBackend(String backendName) {
        LOGGER.debug("#getConnectorMessageIdForBackend: lookup waiting messages on queue [{}] for backendName [{}]", waitQueueName, backendName);
        List<String> waitingMessageIds = new ArrayList<>();
        try {            
            jmsTemplate.setReceiveTimeout(receiveTimeout);

            Message receiveSelected = null;
            do {
                String selector = String.format("%s = '%s'", BACKEND_CLIENT_NAME, backendName);
                LOGGER.debug("#getConnectorMessageIdForBackend: try to receiveSelected message from queue [{}] with selector [{}]", waitQueueName, selector);
                receiveSelected = jmsTemplate.receiveSelected(waitQueueName, selector);            
                if (receiveSelected != null) {
                    String connectorMessageId = receiveSelected.getStringProperty(CONNECTOR_MESSAGE_ID);
                    waitingMessageIds.add(connectorMessageId);
                }            
            } while (receiveSelected != null);
            
        } catch (JMSException jmsException) {
            LOGGER.error("A jms exception occured during reading messages from waiting queue [{}]", waitQueueName);
            throw new org.springframework.jms.UncategorizedJmsException(jmsException);
        }
        LOGGER.trace("#getConnectorMessageIdForBackend: found follwing message ids [{}] on queue [{}]", waitingMessageIds, waitQueueName);
        return waitingMessageIds;
    }
    
}
