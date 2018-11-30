
package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageAttachment;
import eu.domibus.connector.domain.model.DomibusConnectorMessageContent;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.persistence.service.DomibusConnectorBigDataPersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistAllBigDataOfMessageService;
import eu.domibus.connector.persistence.service.exceptions.LargeFileDeletionException;
import org.apache.cxf.helpers.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Facade Service to make it easier to persist all big data of 
 * a message
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Service
public class BigDataWithMessagePersistenceServiceImpl implements DomibusConnectorPersistAllBigDataOfMessageService {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(BigDataWithMessagePersistenceServiceImpl.class);
    
    @Autowired
    private DomibusConnectorBigDataPersistenceService bigDataPersistenceServiceImpl;


    
    // START GETTER / SETTER //
    public void setBigDataPersistenceServiceImpl(DomibusConnectorBigDataPersistenceService bigDataPersistenceServiceImpl) {
        this.bigDataPersistenceServiceImpl = bigDataPersistenceServiceImpl;
    }
    // ENDE GETTER / SETTER //
    

    private void copyDataRef(DomibusConnectorBigDataReference readFrom, DomibusConnectorBigDataReference writeTo) {
        try (InputStream is = readFrom.getInputStream(); OutputStream os = writeTo.getOutputStream()) {
            IOUtils.copy(is, os);
        } catch (IOException e) {
            String error = String.format("IOException occured while writing [%s] to storage", readFrom.getName());
            LOGGER.error(error, e);
        }
    }


    @Override
    @Transactional
    public DomibusConnectorMessage persistAllBigFilesFromMessage(DomibusConnectorMessage message) {
        if (DomainModelHelper.isEvidenceMessage(message)) {
            LOGGER.debug("#persistAllBigFilesFromMessage: is evidence message doing nothing...");
            return message;
        }
        LOGGER.debug("persistAllBigFilesFromMessage: message [{}]", message);
        for (DomibusConnectorMessageAttachment attachment : message.getMessageAttachments()) {
            DomibusConnectorBigDataReference readFrom = attachment.getAttachment();
            DomibusConnectorBigDataReference writeTo = null;

            writeTo = bigDataPersistenceServiceImpl.createDomibusConnectorBigDataReference(message.getConnectorMessageId(), attachment.getName(), attachment.getMimeType());
            copyDataRef(readFrom, writeTo);
            attachment.setAttachment(writeTo);
        }

        DomibusConnectorMessageContent messageContent = message.getMessageContent();
        if (hasMainDocument(messageContent)) {
            DomibusConnectorBigDataReference docReadFrom = messageContent.getDocument().getDocument();
            DomibusConnectorBigDataReference docWriteTo = null;

            docWriteTo = bigDataPersistenceServiceImpl.createDomibusConnectorBigDataReference(message.getConnectorMessageId(), messageContent.getDocument().getDocumentName(), messageContent.getDocument().getDocument().getContentType());
            copyDataRef(docReadFrom, docWriteTo);
            messageContent.getDocument().setDocument(docWriteTo);
        }
        LOGGER.debug("persistAllBigFilesFromMessage: SUCCESS - message [{}]", message);
        return message;
    }

    @Override
    @Transactional
    public void cleanForMessage(DomibusConnectorMessage message) {
        if (DomainModelHelper.isEvidenceMessage(message)) {
            LOGGER.debug("#deleteAllBigFilesFromMessage: is evidence message doing nothing...");
        }

        List<LargeFileDeletionException> deletionExceptions = new ArrayList<>();
        collectBigDataRefsOfMessage(message)
                .stream()
                .forEach(ref ->  {
                    try {
                        bigDataPersistenceServiceImpl.deleteDomibusConnectorBigDataReference(ref);
                    } catch (LargeFileDeletionException deletionException) {
                        deletionExceptions.add(deletionException);
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.trace(String.format("The following largeFile Reference [%s] will be deleted later by timer jobs.\n" +
                                    "Because I was unable to delete it now due the following exception:", ref), deletionException);
                        }
                    }
                });
        String storageRefs = deletionExceptions.stream().map(d -> d.getReferenceFailedToDelete().getStorageIdReference()).collect(Collectors.joining(","));
        LOGGER.info("The following storage references [{}] failed to be deleted immediatly. The will be deleted later by timer jobs.", storageRefs);
    }


    private List<DomibusConnectorBigDataReference> collectBigDataRefsOfMessage(DomibusConnectorMessage message) {
        List<DomibusConnectorBigDataReference> collectedBigDataRefs = collectBigDataRefsOfAttachments(message);
        DomibusConnectorMessageContent messageContent = message.getMessageContent();
        if (hasMainDocument(messageContent)) {
            collectedBigDataRefs.add(messageContent.getDocument().getDocument());
        }
        return collectedBigDataRefs;
    }

    private List<DomibusConnectorBigDataReference> collectBigDataRefsOfAttachments(DomibusConnectorMessage message) {
        return message.getMessageAttachments().stream().map(DomibusConnectorMessageAttachment::getAttachment).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DomibusConnectorMessage loadAllBigFilesFromMessage(@Nonnull DomibusConnectorMessage message) {
        if (DomainModelHelper.isEvidenceMessage(message)) {
            LOGGER.debug("#persistAllBigFilesFromMessage: is evidence message doing nothing...");
            return message;
        }
        LOGGER.trace("#loadAllBigFilesFromMessage: message [{}]", message);
        for (DomibusConnectorMessageAttachment attachment : message.getMessageAttachments()) {
            DomibusConnectorBigDataReference activeRead = attachment.getAttachment();
            LOGGER.trace("#loadAllBigFilesFromMessage: loading attachment [{}]", activeRead);
            DomibusConnectorBigDataReference activatedRead = bigDataPersistenceServiceImpl.getReadableDataSource(activeRead);
            attachment.setAttachment(activatedRead);
        }
        DomibusConnectorMessageContent messageContent = message.getMessageContent();
        if (hasMainDocument(messageContent)) {
            DomibusConnectorBigDataReference docRefresRead = messageContent.getDocument().getDocument();
            LOGGER.trace("#loadAllBigFilesFromMessage: loading content document [{}]", docRefresRead);
            DomibusConnectorBigDataReference docActivatedRead = bigDataPersistenceServiceImpl.getReadableDataSource(docRefresRead);
            messageContent.getDocument().setDocument(docActivatedRead);
        }
        
        return message;
    }
        
    private boolean hasMainDocument(DomibusConnectorMessageContent content) {
        return content != null && content.getDocument() != null && content.getDocument().getDocument() != null;
    }
 

}
