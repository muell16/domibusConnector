package eu.domibus.connector.controller.listener;

import eu.domibus.connector.controller.processor.EvidenceMessageProcessor;
import eu.domibus.connector.controller.processor.steps.EvidenceTriggerStep;
import eu.domibus.connector.controller.processor.ToBackendBusinessMessageProcessor;
import eu.domibus.connector.controller.processor.ToGatewayBusinessMessageProcessor;
import eu.domibus.connector.controller.queues.ToConnectorQueue;
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
import static eu.domibus.connector.controller.queues.QueuesConfiguration.TO_CONNECTOR_QUEUE_BEAN;

@Component
@RequiredArgsConstructor
@Slf4j
public class ToConnectorControllerListener {

    private final ToGatewayBusinessMessageProcessor toGatewayBusinessMessageProcessor;
    private final ToBackendBusinessMessageProcessor toBackendBusinessMessageProcessor;
    private final EvidenceMessageProcessor evidenceMessageProcessor;
    private final ToConnectorQueue toConnectorQueue;

    @JmsListener(destination = TO_CONNECTOR_QUEUE_BEAN)
    @Transactional
    @eu.domibus.connector.lib.logging.MDC(name = LoggingMDCPropertyNames.MDC_DC_QUEUE_LISTENER_PROPERTY_NAME, value = "ToConnectorControllerListener")
    public void handleMessage(DomibusConnectorMessage message) {
        if (message == null || message.getMessageDetails() == null) {
            throw new IllegalArgumentException("Message and Message Details must not be null");
        }
        String messageId = message.getConnectorMessageId().toString();
        try (MDC.MDCCloseable mdcCloseable = MDC.putCloseable(LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_ID_PROPERTY_NAME, messageId)) {
            DomibusConnectorMessageDirection direction = message.getMessageDetails().getDirection();
            if (DomainModelHelper.isEvidenceMessage(message)) {
                evidenceMessageProcessor.processMessage(message);
            } else if (DomainModelHelper.isBusinessMessage(message) && direction.getTarget() == MessageTargetSource.GATEWAY) {
                toGatewayBusinessMessageProcessor.processMessage(message);
            } else if (DomainModelHelper.isBusinessMessage(message) && direction.getTarget() == MessageTargetSource.BACKEND) {
                toBackendBusinessMessageProcessor.processMessage(message);
            } else {
                throw new IllegalStateException("Illegal Message format received!");
            }
        } catch (PersistenceException persistenceException) {
            //cannot recover here: put into DLQ!
            log.error(LoggingMarker.BUSINESS_LOG, "Failed to process message! Check Dead Letter Queue and technical logs for details!");
            toConnectorQueue.putOnErrorQueue(message);
        }
    }

}
