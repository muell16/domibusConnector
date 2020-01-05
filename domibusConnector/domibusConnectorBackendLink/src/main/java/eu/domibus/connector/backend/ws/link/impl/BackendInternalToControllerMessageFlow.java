package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.backend.service.DomibusConnectorBackendInternalDeliverToController;
import eu.domibus.connector.backend.ws.link.spring.CommonBackendLinkConfigurationProperties;
import eu.domibus.connector.controller.exception.DomibusConnectorRejectDeliveryException;
import eu.domibus.connector.controller.service.DomibusConnectorDeliveryRejectionService;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.controller.service.DomibusConnectorBackendSubmissionService;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistAllBigDataOfMessageService;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static eu.domibus.connector.backend.ws.link.spring.WSBackendLinkContextConfiguration.WS_BACKEND_LINK_PROFILE;

/**
 * this service handles the message transport from controller to the backend
 *
 */
@Profile(WS_BACKEND_LINK_PROFILE)
@Service
public class BackendInternalToControllerMessageFlow implements DomibusConnectorBackendInternalDeliverToController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackendInternalToControllerMessageFlow.class);

    private DomibusConnectorMessagePersistenceService messagePersistenceService;

    private DomibusConnectorPersistAllBigDataOfMessageService bigDataPersistence;

    private DomibusConnectorMessageIdGenerator messageIdGenerator;

    private DomibusConnectorBackendDeliveryService toBackendDeliveryService;

    private DomibusConnectorBackendSubmissionService backendToControllerSubmissionService;

    private CommonBackendLinkConfigurationProperties commonBackendLinkConfigurationProperties;

    private DomibusConnectorDeliveryRejectionService deliveryRejectionService;

    //setter
    @Autowired
    public void setMessagePersistenceService(DomibusConnectorMessagePersistenceService messagePersistenceService) {
        this.messagePersistenceService = messagePersistenceService;
    }

    @Autowired
    public void setBigDataPersistence(DomibusConnectorPersistAllBigDataOfMessageService bigDataPersistence) {
        this.bigDataPersistence = bigDataPersistence;
    }

    @Autowired
    public void setMessageIdGenerator(DomibusConnectorMessageIdGenerator messageIdGenerator) {
        this.messageIdGenerator = messageIdGenerator;
    }

    @Autowired
    public void setToBackendDeliveryService(DomibusConnectorBackendDeliveryService backendDeliveryService) {
        this.toBackendDeliveryService = backendDeliveryService;
    }

    @Autowired
    public void setBackendToControllerSubmissionService(DomibusConnectorBackendSubmissionService toControllerSubmissionService) {
        this.backendToControllerSubmissionService = toControllerSubmissionService;
    }

    @Autowired
    public void setCommonBackendLinkConfigurationProperties(CommonBackendLinkConfigurationProperties commonBackendLinkConfigurationProperties) {
        this.commonBackendLinkConfigurationProperties = commonBackendLinkConfigurationProperties;
    }

    @Autowired
    public void setDeliveryRejectionService(DomibusConnectorDeliveryRejectionService deliveryRejectionService) {
        this.deliveryRejectionService = deliveryRejectionService;
    }

    @Override
    public void submitToController(DomibusConnectorBackendMessage backendMessage) {


        LOGGER.debug("#submitToController: message [{}]", backendMessage);
        DomibusConnectorMessage message = backendMessage.getDomibusConnectorMessage();
        message.getMessageDetails().setConnectorBackendClientName(backendMessage.getBackendClientInfo().getBackendName());

        String msgId = messageIdGenerator.generateDomibusConnectorMessageId();
        if (StringUtils.isEmpty(message.getMessageDetails().getEbmsMessageId())) {
            LOGGER.debug("No ebmsId was passed from the client, setting ebmsId to [{}]", msgId);
            message.getMessageDetails().setEbmsMessageId(msgId);
        }
        message.setConnectorMessageId(msgId);
        LOGGER.debug("#submitToController: start to process message with message id [{}]", message.getConnectorMessageId());
        MDC.put(LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_ID_PROPERTY_NAME, message.getConnectorMessageId());
        message = messagePersistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);
        LOGGER.debug("#submitToController: message persisted");
        message = bigDataPersistence.persistAllBigFilesFromMessage(message);
        LOGGER.debug("#submitToController: message [{}] data persisted", message);
        message = messagePersistenceService.mergeMessageWithDatabase(message);


        LOGGER.debug("#submitToController: message.getMessageDetails().getService() [{}]", message.getMessageDetails().getService());

        if (isTestPingMessage(message)) {
            LOGGER.debug("#submitToController: message is ConnectorPingMessage so just sending message back to backend!\n(No Evidence-Confirmations are created)");
            toBackendDeliveryService.deliverMessageToBackend(message);
        } else {
            LOGGER.debug("#submitToController: 'normal' message.....");
            backendToControllerSubmissionService.submitToController(message);
        }
    }

    private boolean isTestPingMessage(DomibusConnectorMessage message) {
        String testPingService = commonBackendLinkConfigurationProperties.getPing().getService();
        String testPingAction = commonBackendLinkConfigurationProperties.getPing().getAction();

        return (testPingService.equals(message.getMessageDetails().getService().getService()) &&
                testPingAction.equals(message.getMessageDetails().getAction().getAction())
        );
    }

    @Override
    public void rejectDelivery(DomibusConnectorBackendMessage backendMessage, Throwable reason) {
        DomibusConnectorMessage message = backendMessage.getDomibusConnectorMessage();
        DomibusConnectorRejectDeliveryException rejectDeliveryException =
                new DomibusConnectorRejectDeliveryException(message, DomibusConnectorRejectionReason.BACKEND_REJECTION, reason);
        deliveryRejectionService.rejectDelivery(rejectDeliveryException);
    }

    @Override
    public DomibusConnectorMessage processMessageBeforeDeliverToBackend(DomibusConnectorMessage message) {
        return bigDataPersistence.loadAllBigFilesFromMessage(message);
    }

    @Override
    public DomibusConnectorMessage processMessageAfterDeliveredToBackend(DomibusConnectorMessage message) {
        messagePersistenceService.setMessageDeliveredToNationalSystem(message);
        if (!DomainModelHelper.isEvidenceMessage(message)) {
            bigDataPersistence.cleanForMessage(message);
        }
        return message;
    }

}
