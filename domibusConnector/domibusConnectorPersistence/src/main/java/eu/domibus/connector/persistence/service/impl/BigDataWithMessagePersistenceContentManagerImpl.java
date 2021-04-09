
package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.LargeFileReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageAttachment;
import eu.domibus.connector.domain.model.DomibusConnectorMessageContent;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.persistence.service.LargeFilePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorMessageContentManager;
import eu.domibus.connector.persistence.service.exceptions.LargeFileDeletionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Facade Service to make it easier to persist all big data of 
 * a message
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
//@Service
public class BigDataWithMessagePersistenceContentManagerImpl implements DomibusConnectorMessageContentManager {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(BigDataWithMessagePersistenceContentManagerImpl.class);
    
    @Autowired
    private LargeFilePersistenceService largeFilePersistenceService;


    
    // START GETTER / SETTER //
    public void setLargeFilePersistenceService(LargeFilePersistenceService largeFilePersistenceService) {
        this.largeFilePersistenceService = largeFilePersistenceService;
    }
    // ENDE GETTER / SETTER //
    


    @Override
    @Transactional
    public void cleanForMessage(DomibusConnectorMessage message) {
        if (DomainModelHelper.isEvidenceMessage(message)) {
            LOGGER.debug("#deleteAllBigFilesFromMessage: is evidence message doing nothing...");
        }

        List<LargeFileDeletionException> deletionExceptions = new ArrayList<>();



        Map<DomibusConnectorMessage.DomibusConnectorMessageId, List<LargeFileReference>> allAvailableReferences = largeFilePersistenceService.getAllAvailableReferences();
        List<LargeFileReference> largeFileReferences = allAvailableReferences.getOrDefault(message.getConnectorMessageId(), new ArrayList<>());
        largeFileReferences
                .stream()
                .forEach(ref ->  {
                    try {
                        largeFilePersistenceService.deleteDomibusConnectorBigDataReference(ref);
                    } catch (LargeFileDeletionException deletionException) {
                        deletionExceptions.add(deletionException);
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.trace(String.format("The following largeFile Reference [%s] will be deleted later by timer jobs.\n" +
                                    "Because I was unable to delete it now due the following exception:", ref), deletionException);
                        }
                    }
                });

        String storageRefs = deletionExceptions.stream().map(d -> d.getReferenceFailedToDelete().getStorageIdReference()).collect(Collectors.joining(","));
        LOGGER.info("The following storage references [{}] failed to be deleted immediately. The will be deleted later by timer jobs.", storageRefs);




    }



    @Transactional
    public DomibusConnectorMessage setAllLargeFilesReadable(@Nonnull DomibusConnectorMessage message) {
        if (DomainModelHelper.isEvidenceMessage(message)) {
            LOGGER.debug("#persistAllBigFilesFromMessage: is evidence message doing nothing...");
            return message;
        }
        LOGGER.trace("#loadAllBigFilesFromMessage: message [{}]", message);
        for (DomibusConnectorMessageAttachment attachment : message.getMessageAttachments()) {
            LargeFileReference activeRead = attachment.getAttachment();
            LOGGER.trace("#loadAllBigFilesFromMessage: loading attachment [{}]", activeRead);
            LargeFileReference activatedRead = largeFilePersistenceService.getReadableDataSource(activeRead);
            attachment.setAttachment(activatedRead);
        }
        DomibusConnectorMessageContent messageContent = message.getMessageContent();
        if (hasMainDocument(messageContent)) {
            LargeFileReference docRefresRead = messageContent.getDocument().getDocument();
            LOGGER.trace("#loadAllBigFilesFromMessage: loading content document [{}]", docRefresRead);
            LargeFileReference docActivatedRead = largeFilePersistenceService.getReadableDataSource(docRefresRead);
            messageContent.getDocument().setDocument(docActivatedRead);
        }
        return message;
    }

    private boolean hasMainDocument(DomibusConnectorMessageContent content) {
        return content != null && content.getDocument() != null && content.getDocument().getDocument() != null;
    }
 

}