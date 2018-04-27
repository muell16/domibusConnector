
package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.backend.ws.link.spring.BackendLinkInternalWaitQueueProperties;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.jms.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


    @Autowired
    private BackendLinkInternalWaitQueueProperties backendLinkInternalWaitQueueProperties;

    @Autowired(required = false) //can be null if there is no push impl
    private @Nullable
    PushMessageToBackendClient pushMessageToBackendCallback;
       
    @Autowired
    private JmsTemplate jmsTemplate;

    private long receiveTimeout;
    private String waitQueueName;


    @PostConstruct
    public void init() {
        this.waitQueueName = backendLinkInternalWaitQueueProperties.getName();
        this.receiveTimeout = backendLinkInternalWaitQueueProperties.getReceiveTimeout();
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
        final String connectorMessageId = message.getConnectorMessageId();
        LOGGER.debug("#putMessageInWaitingQueue: put message id [{}] for backendClientName [{}] in waiting queue [{}]",
                connectorMessageId, backendClientName, waitQueueName);
        jmsTemplate.send(waitQueueName, (Session session) -> {
            Message msg = session.createObjectMessage(backendMessage);
            msg.setIntProperty(BACKEND_CLIENT_DELIVERY_RETRIES, 0);
            msg.setStringProperty(BACKEND_CLIENT_NAME, backendClientName);
            msg.setStringProperty(CONNECTOR_MESSAGE_ID, connectorMessageId);
            msg.setBooleanProperty(CONNECTOR_BACKEND_IS_PUSH_BACKEND, backendClientInfo.isPushBackend());                
            msg.setIntProperty(BACKEND_CLIENT_DELIVERY_RETRIES, 0);
            LOGGER.trace("#putMessageInWaitingQueue: Send message [{}] to queue [{}]", msg, waitQueueName);
            return msg;          
        });                
    }
        
    @JmsListener(destination="#{backendLinkInternalWaitQueueProperties.getName()}", selector=CONNECTOR_BACKEND_IS_PUSH_BACKEND + " = TRUE")
    public void pushToBackend(ObjectMessage msg) throws JMSException {
        LOGGER.trace("#pushToBackend: jms listener received jms message [{}]", msg);
        String connectorMessageId = msg.getStringProperty(CONNECTOR_MESSAGE_ID);
        String backendName = msg.getStringProperty(BACKEND_CLIENT_NAME);
        DomibusConnectorBackendMessage backendMessage = (DomibusConnectorBackendMessage) msg.getObject();
        pushMessageToBackendCallback.push(backendMessage);
    }

    @Override
    public List<DomibusConnectorMessage> getConnectorMessageIdForBackend(String backendName) {
        LOGGER.debug("#getConnectorMessageIdForBackend: lookup waiting messages on queue [{}] for backendName [{}]", waitQueueName, backendName);
        List<DomibusConnectorMessage> waitingMessages = new ArrayList<>();
        try {            
            jmsTemplate.setReceiveTimeout(receiveTimeout);

            ObjectMessage receiveSelected = null;
            do {
                String selector = String.format("%s = '%s' AND %s = FALSE", BACKEND_CLIENT_NAME, backendName, CONNECTOR_BACKEND_IS_PUSH_BACKEND);
                LOGGER.debug("#getConnectorMessageIdForBackend: try to receiveSelected message from queue [{}] with selector [{}]", waitQueueName, selector);
                receiveSelected = (ObjectMessage) jmsTemplate.receiveSelected(waitQueueName, selector);
                LOGGER.trace("#getConnectorMessageIdForBackend: received message [{}]", receiveSelected);
                if (receiveSelected != null) {
                    DomibusConnectorBackendMessage msg = (DomibusConnectorBackendMessage) receiveSelected.getObject();
                    LOGGER.trace("#getConnectorMessageIdForBackend: got message [{}] from queue [{}]", msg, waitQueueName);
                    waitingMessages.add(msg.getDomibusConnectorMessage());
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
