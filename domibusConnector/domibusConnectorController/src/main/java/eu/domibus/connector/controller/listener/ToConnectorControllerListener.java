package eu.domibus.connector.controller.listener;

import eu.domibus.connector.controller.process.*;
import eu.domibus.connector.controller.processor.EvidenceTriggerMessageProcessor;
import eu.domibus.connector.controller.processor.ToGatewayBusinessMessageProcessor;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.persistence.service.exceptions.PersistenceException;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.LoggingMarker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.transaction.Transactional;

import static eu.domibus.connector.controller.queues.QueuesConfiguration.TO_CONNECTOR_ERROR_QUEUE_BEAN;

@Component
@RequiredArgsConstructor
@Slf4j
public class ToConnectorControllerListener {

    private final ToGatewayBusinessMessageProcessor toGatewayBusinessMessageProcessor;
    private final ToBackendBusinessMessageProcessor toBackendBusinessMessageProcessor;
    private final EvidenceMessageProcessor evidenceMessageProcessor;
    private final EvidenceTriggerMessageProcessor evidenceTriggerMessageProcessor;
    private final JmsTemplate jmsTemplate;
    @Qualifier(TO_CONNECTOR_ERROR_QUEUE_BEAN)
    private final Destination toConnectorControllerErrorQueue;

    @JmsListener(destination = "#{toConnectorQueueBean.getQueueName}")
    @Transactional
    public void handleMessage(DomibusConnectorMessage message) {
        if (message == null || message.getMessageDetails() == null) {
            throw new IllegalArgumentException("Message and Message Details must not be null");
        }
        String messageId = message.getConnectorMessageId().toString();
        try (MDC.MDCCloseable mdcCloseable = MDC.putCloseable(LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_ID_PROPERTY_NAME, messageId)) {
            DomibusConnectorMessageDirection direction = message.getMessageDetails().getDirection();
            if (DomainModelHelper.isEvidenceTriggerMessage(message)) {
                evidenceTriggerMessageProcessor.processMessage(message);
            } else if (DomainModelHelper.isEvidenceMessage(message)) {
                evidenceMessageProcessor.processMessage(message);
            } else if (DomainModelHelper.isBusinessMessage(message) && direction.getTarget() == MessageTargetSource.GATEWAY) {
                toGatewayBusinessMessageProcessor.processMessage(message);
            } else if (DomainModelHelper.isBusinessMessage(message) && direction.getTarget() == MessageTargetSource.BACKEND) {
                toBackendBusinessMessageProcessor.processMessage(message);
            }
        } catch (PersistenceException persistenceException) {
            //cannot recover here: put into DLQ!
            log.error(LoggingMarker.BUSINESS_LOG, "Failed to process message! Check Dead Letter Queue and technical logs for details!");
            jmsTemplate.convertAndSend(toConnectorControllerErrorQueue, message);
        }
    }

}
