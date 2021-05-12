package eu.domibus.connector.controller.processor.content;

import eu.domibus.connector.controller.queues.producer.ToCleanupQueue;
import eu.domibus.connector.controller.spring.ContentDeletionConfigurationProperties;
import eu.domibus.connector.domain.model.LargeFileReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageId;
import eu.domibus.connector.persistence.service.LargeFilePersistenceService;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import eu.domibus.connector.persistence.service.exceptions.LargeFileDeletionException;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * This service is triggered by an timer job
 * and is responsible for deleting
 * large file storage references if the reference is
 * older than a day and has no associated business message
 * within the database or the associated business message
 * has already been confirmed or rejected
 */
@Service
@ConditionalOnProperty(prefix = ContentDeletionConfigurationProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class CheckContentDeletedProcessorImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckContentDeletedProcessorImpl.class);

    private final LargeFilePersistenceService largeFilePersistenceService;
    private final DCMessagePersistenceService messagePersistenceService;
    private final ToCleanupQueue toCleanupQueue;

    public CheckContentDeletedProcessorImpl(LargeFilePersistenceService largeFilePersistenceService,
                                            DCMessagePersistenceService messagePersistenceService,
                                            ToCleanupQueue toCleanupQueue) {
        this.largeFilePersistenceService = largeFilePersistenceService;
        this.messagePersistenceService = messagePersistenceService;
        this.toCleanupQueue = toCleanupQueue;
    }

    @Scheduled(fixedDelayString = "#{ContentDeletionTimeoutConfigurationProperties.checkTimeout.milliseconds}")
    public void checkContentDeletedProcessor() {
        Map<DomibusConnectorMessageId, List<LargeFileReference>> allAvailableReferences = largeFilePersistenceService.getAllAvailableReferences();
        allAvailableReferences.forEach(this::checkDelete);
    }

    private void checkDelete(DomibusConnectorMessageId id, List<LargeFileReference> references) {
        DomibusConnectorMessage msg = messagePersistenceService.findMessageByConnectorMessageId(id.getConnectorMessageId());
        if (msg == null) {
            LOGGER.debug("No message with connector message id [{}] found in database", id);
            ZonedDateTime tomorrow = ZonedDateTime.now().plusDays(1);
            //return only refs which are older than one day
            references.stream()
                    .filter(r -> r.getCreationDate() != null && r.getCreationDate().isAfter(tomorrow))
                    .forEach(this::deleteReference);
        } else if (msg.getMessageDetails().getConfirmed() != null || msg.getMessageDetails().getRejected() != null) {
            //put on delete queue if message exists...
            toCleanupQueue.putOnQueue(msg);
        }
    }

    private void deleteReference(LargeFileReference ref) {
        LOGGER.debug("Deleting reference with id [{}]", ref.getStorageIdReference());
        try {
            largeFilePersistenceService.deleteDomibusConnectorBigDataReference(ref);
        } catch (LargeFileDeletionException delException) {
            LOGGER.error(LoggingMarker.BUSINESS_CONTENT_LOG, "Was unable to delete the reference [{}] in the timer job. The data must be manually deleted by the administrator!", ref);
            LOGGER.error("Was unable to delete due exception: ", delException);
        }
    }

}
