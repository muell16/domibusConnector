package eu.domibus.connector.controller.processor.content;

import eu.domibus.connector.controller.spring.ContentDeletionTimeoutConfigurationProperties;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessage.DomibusConnectorMessageId;
import eu.domibus.connector.persistence.service.DomibusConnectorBigDataPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CheckContentDeletedProcessorImpl implements CheckContentDeletedProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckContentDeletedProcessor.class);

    DomibusConnectorBigDataPersistenceService bigDataPersistenceService;
    DomibusConnectorMessagePersistenceService messagePersistenceService;
    ContentDeletionTimeoutConfigurationProperties contentDeletionTimeoutConfigurationProperties;

    @Autowired
    public void setBigDataPersistenceService(DomibusConnectorBigDataPersistenceService bigDataPersistenceService) {
        this.bigDataPersistenceService = bigDataPersistenceService;
    }

    @Autowired
    public void setMessagePersistenceService(DomibusConnectorMessagePersistenceService messagePersistenceService) {
        this.messagePersistenceService = messagePersistenceService;
    }

    @Override
    @Scheduled(fixedDelayString = "#{ContentDeletionTimeoutConfigurationProperties.checkTimeout.milliseconds}")
    public void checkContentDeletedProcessor() {
        Map<DomibusConnectorMessageId, List<DomibusConnectorBigDataReference>> allAvailableReferences = bigDataPersistenceService.getAllAvailableReferences();
        allAvailableReferences.forEach( (id, refList) -> {
            try {
                checkDeletion(id, refList);
            } catch (Exception e) {
                LOGGER.error("Exception occured while cleaning up content for message [{}]", id);
            }
        });
    }

    void checkDeletion(DomibusConnectorMessageId id, List<DomibusConnectorBigDataReference> references) {
        DomibusConnectorMessage msg = messagePersistenceService.findMessageByConnectorMessageId(id.getConnectorMessageId());
        if (msg == null) {
            LOGGER.warn("No message with connector message id [{}] found in database. This content will NOT be deleted. Please check and remove manual!", id.getConnectorMessageId());
            return;
        }
        DomibusConnectorMessageDirection direction = msg.getMessageDetails().getDirection();
        Date transportFinishedDate = null;
        boolean failed = false;
        if (direction == DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY || direction == DomibusConnectorMessageDirection.CONNECTOR_TO_GATEWAY) {
            transportFinishedDate = msg.getMessageDetails().getDeliveredToGateway();
        } else if (direction == DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND || direction == DomibusConnectorMessageDirection.CONNECTOR_TO_BACKEND){
            transportFinishedDate = msg.getMessageDetails().getDeliveredToBackend();
        } else {
            //should never end up here!
            assert(true);
        }

        if (msg.getMessageDetails().getFailed() != null) {
            failed = true;
            transportFinishedDate = msg.getMessageDetails().getFailed();
        }

        if (transportFinishedDate != null) {
            LOGGER.info(LoggingMarker.BUSINESS_CONTENT_LOG,
                    "Transport of message [{}] in direction [{}] has {} on [{}] - deleting content of this message",
                    msg, direction, failed ? "failed" : "finished", transportFinishedDate);
            references.stream().forEach(ref -> {
                        LOGGER.debug(LoggingMarker.BUSINESS_CONTENT_LOG, "Deleting file reference [{}]", ref.getStorageIdReference());
                        bigDataPersistenceService.deleteDomibusConnectorBigDataReference(ref);
                    });
        }
    }

}
