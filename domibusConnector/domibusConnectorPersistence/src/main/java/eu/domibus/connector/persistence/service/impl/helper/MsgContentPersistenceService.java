package eu.domibus.connector.persistence.service.impl.helper;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.model.builder.*;
import eu.domibus.connector.domain.model.helper.CopyHelper;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMsgContDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorDetachedSignature;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.model.PDomibusConnectorMsgCont;
import eu.domibus.connector.persistence.service.LargeFilePersistenceService;
import eu.domibus.connector.persistence.service.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StreamUtils;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service for persisting message content like:
 * <ul>
 *  <li>message content - the business pdf and xml content</li>
 *  <li>message attachments</li>
 *  <li>message confirmations</li>
 * </ul>
 * <p>
 * For persisting the java objects are serialized and stored into database,
 * bigDataReference fields are replaced by plain BigDataReference objects
 * to make sure that the BigDataReferenceObject can be persisted
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component
@Transactional
public class MsgContentPersistenceService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MsgContentPersistenceService.class);

    private LargeFilePersistenceService largeFilePersistenceService;
    private DomibusConnectorMsgContDao msgContDao;
    private DomibusConnectorMessageDao msgDao;

    /*
     * DAO Setter
     */
    @Autowired
    public void setMsgContDao(DomibusConnectorMsgContDao msgContDao) {
        this.msgContDao = msgContDao;
    }

    @Autowired
    public void setMsgDao(DomibusConnectorMessageDao msgDao) {
        this.msgDao = msgDao;
    }

    @Autowired
    public void setLargeFilePersistenceService(LargeFilePersistenceService largeFilePersistenceService) {
        this.largeFilePersistenceService = largeFilePersistenceService;
    }

    /*
     * END DAO Setter
     */


    /**
     * loads Message Content from Database, deserializes the stored objects back to java objects, and puts them back into the message
     *
     * @param messageBuilder - message builder
     * @param dbMessage      - the  dbMessage object
     * @throws PersistenceException - throws persistenceException in case of failure
     */
    public void loadMessagePayloads(final @Nonnull DomibusConnectorMessageBuilder messageBuilder, final PDomibusConnectorMessage dbMessage) throws PersistenceException {
        List<PDomibusConnectorMsgCont> findByMessage = this.msgContDao.findByMessage(dbMessage);

        //map fromDbToDomain business XML, businessDoc
        loadMsgContent(messageBuilder, findByMessage);

        //mapFromDbToDomain evidence back
        loadConfirmations(messageBuilder, findByMessage);

        //mapFromDbToDomain attachments back
        loadAttachments(messageBuilder, findByMessage);
    }

    private void loadConfirmations(DomibusConnectorMessageBuilder messageBuilder, List<PDomibusConnectorMsgCont> findByMessage) {
        findByMessage.stream()
                .filter(StoreType.MESSAGE_CONFIRMATION::equals)
                .forEach(c -> {
                    LargeFileReference largeFileReference = loadLargeFileReference(c);

                    messageBuilder.addConfirmation(DomibusConnectorMessageConfirmationBuilder.createBuilder()
                            .setEvidence(largeFileReferenceToByte(largeFileReference))
                            .setEvidenceType(DomibusConnectorEvidenceType.valueOf(c.getContentName()))
                            .build()
                    );

                });

        //legacy mapping
        findByMessage.stream()
                .filter(StoreType.MESSAGE_CONFIRMATION::equals)
                .forEach(c -> {
                    DomibusConnectorMessageConfirmation msgConfirmation = mapFromMsgCont(c, DomibusConnectorMessageConfirmation.class);
                    messageBuilder.addConfirmation(msgConfirmation);
                });
    }

    private void loadAttachments(DomibusConnectorMessageBuilder messageBuilder, List<PDomibusConnectorMsgCont> findByMessage) {
        findByMessage.stream()
                .filter(StoreType.MESSAGE_ATTACHMENT_CONTENT::equals)
                .forEach(c -> {
                    messageBuilder.addAttachment(
                        DomibusConnectorMessageAttachmentBuilder.createBuilder()
                            .setAttachment(loadLargeFileReference(c))
                            .setIdentifier(c.getContentName())
                            .build());
                });

        //legacy mapping
        findByMessage.stream()
                .filter(StoreType.MESSAGE_ATTACHMENT::equals)
                .forEach(c -> {
                    DomibusConnectorMessageAttachment msgAttachment = mapFromMsgCont(c, DomibusConnectorMessageAttachment.class);
                    messageBuilder.addAttachment(msgAttachment);
                });

    }

    private void loadMsgContent(DomibusConnectorMessageBuilder messageBuilder, List<PDomibusConnectorMsgCont> findByMessage) {

        DomibusConnectorMessageContentBuilder messageContentBuilder = DomibusConnectorMessageContentBuilder.createBuilder();
        Optional<PDomibusConnectorMsgCont> foundXmlContent = findByMessage.stream()
                .filter(StoreType.MESSAGE_CONTENT_XML::equals)
                .findFirst();
        Optional<PDomibusConnectorMsgCont> foundDocumentContent = findByMessage.stream()
                .filter(StoreType.MESSAGE_CONTENT_DOCUMENT::equals)
                .findFirst();

        if (foundXmlContent.isPresent()) {
            LargeFileReference largeFileReference = loadLargeFileReference(foundXmlContent.get());
            messageContentBuilder.setXmlContent(largeFileReferenceToByte(largeFileReference));

            if (foundDocumentContent.isPresent()) {
                loadDocumentContent(messageContentBuilder, foundDocumentContent.get());
            }

        } else {
            //if nothing found call deprecated content loader
            loadMsgContent_legacy(messageBuilder, findByMessage);
        }

    }

    private void loadDocumentContent(DomibusConnectorMessageContentBuilder messageContent, PDomibusConnectorMsgCont pDomibusConnectorMsgCont) {
        LargeFileReference largeFileReference = loadLargeFileReference(pDomibusConnectorMsgCont);

        DomibusConnectorMessageDocumentBuilder documentBuilder = DomibusConnectorMessageDocumentBuilder.createBuilder();
        documentBuilder.setContent(largeFileReference);
        documentBuilder.setName(pDomibusConnectorMsgCont.getContentName());

        PDomibusConnectorDetachedSignature dbDetachedSignature = pDomibusConnectorMsgCont.getDetachedSignature();
        if (pDomibusConnectorMsgCont.getDetachedSignature() != null) {
            DetachedSignature sig = DetachedSignatureBuilder.createBuilder()
                    .setMimeType(dbDetachedSignature.getMimeType())
                    .setSignature(dbDetachedSignature.getDetachedSignature())
                    .setName(dbDetachedSignature.getDetachedSignatureName())
                    .build();
            documentBuilder.withDetachedSignature(sig);
        }
        messageContent.setDocument(documentBuilder.build());
    }

    private LargeFileReference loadLargeFileReference(PDomibusConnectorMsgCont pDomibusConnectorMsgCont) {
        LargeFileReference largeFileReference = new LargeFileReference();
        largeFileReference.setStorageIdReference(pDomibusConnectorMsgCont.getStorageReferenceId());
        largeFileReference.setStorageProviderName(pDomibusConnectorMsgCont.getStorageProviderName());
        LargeFileReference readableDataSource = largeFilePersistenceService.getReadableDataSource(largeFileReference);
        return readableDataSource;
    }

    private byte[] largeFileReferenceToByte(LargeFileReference largeFileReference) {
        LOGGER.debug("Loadin byte from largeFileReference [{}]", largeFileReference);
        try (InputStream is = largeFileReference.getInputStream()) {
            return StreamUtils.copyToByteArray(is);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error while reading from LargeFile Reference [%s]", largeFileReference), e);
        }
    }

    @Deprecated
    private void loadMsgContent_legacy(DomibusConnectorMessageBuilder messageBuilder, List<PDomibusConnectorMsgCont> findByMessage) {

        //deprecated mapper: mapFromDbToDomain content back
        Optional<PDomibusConnectorMsgCont> findFirst = findByMessage.stream()
                .filter(StoreType.MESSAGE_CONTENT::equals)
                .findFirst();
        if (findFirst.isPresent()) {
            DomibusConnectorMessageContent messageContent = mapFromMsgCont(findFirst.get(), DomibusConnectorMessageContent.class);
            messageBuilder.setMessageContent(messageContent);
        }
    }

    /**
     * takes a message and stores all content into the database
     * deletes all old content regarding the message and persists it again in the database
     *
     * @param message the message
     */
    public void storeMsgContent(@Nonnull DomibusConnectorMessage message) throws PersistenceException {
        //handle document
        List<PDomibusConnectorMsgCont> toStoreList = new ArrayList<>();
        DomibusConnectorMessageContent messageContent = message.getMessageContent();
        PDomibusConnectorMessage dbMessage = this.msgDao.findOneByConnectorMessageId(message.getConnectorMessageId());
        if (messageContent != null) {
            toStoreList.add(mapDocumentToDb(dbMessage, messageContent.getDocument()));
            toStoreList.add(mapXmlContentToDB(message.getConnectorMessageId(), dbMessage, messageContent.getXmlContent()));
        }
        //handle attachments
        for (DomibusConnectorMessageAttachment attachment : message.getMessageAttachments()) {
            toStoreList.add(mapAttachment(dbMessage, attachment));
        }
        //handle confirmations
        for (DomibusConnectorMessageConfirmation c : message.getMessageConfirmations()) {
            toStoreList.add(mapConfirmation(dbMessage, c));
        }
        this.msgContDao.deleteByMessage(dbMessage);   //delete old contents
        this.msgContDao.saveAll(toStoreList); //save new contents
    }

    PDomibusConnectorMsgCont mapXmlContentToDB(String connectorMessageId, PDomibusConnectorMessage message, byte[] xmlDocument) {
        if (xmlDocument == null) {
            throw new IllegalArgumentException("Xml content is not allowed to be null!");
        }

        LargeFileReference xmlBussinessDocument = largeFilePersistenceService.createDomibusConnectorBigDataReference(connectorMessageId, "XMLBussinessDocument", MimeTypeUtils.APPLICATION_XML_VALUE);
        try ( OutputStream out = xmlBussinessDocument.getOutputStream();) {
            StreamUtils.copy(xmlDocument, out);
        } catch (IOException e) {
            throw new RuntimeException("unable to write BusinessXML to LargeFileProvider", e);
        }

        PDomibusConnectorMsgCont pDomibusConnectorMsgCont = storeObjectIntoMsgCont(message, StoreType.MESSAGE_CONTENT_XML, xmlBussinessDocument);
        return pDomibusConnectorMsgCont;
    }

    PDomibusConnectorMsgCont mapDocumentToDb(PDomibusConnectorMessage message, DomibusConnectorMessageDocument document) {
        //change BigDocumentRefernce with the plain implementation to make sure it is serializeable!
//        DomibusConnectorMessageContent copiedMessageContent = CopyHelper.copyMessageContent(content);
//        DomibusConnectorMessageDocument document = content.getDocument();
        if (document == null) {
            throw new IllegalArgumentException("document is not allowed to be null!");
        }
        LargeFileReference plainRef = createBigDataReferenceCopy(message.getConnectorMessageId(), document.getDocument());
        document.setDocument(plainRef);

        PDomibusConnectorMsgCont pDomibusConnectorMsgCont = storeObjectIntoMsgCont(message, StoreType.MESSAGE_CONTENT_DOCUMENT, plainRef);

        DetachedSignature detachedSignature = document.getDetachedSignature();
        if (detachedSignature != null) {
            PDomibusConnectorDetachedSignature dbDetachedSignature = new PDomibusConnectorDetachedSignature();
            dbDetachedSignature.setContent(pDomibusConnectorMsgCont);
            dbDetachedSignature.setDetachedSignature(detachedSignature.getDetachedSignature());
            dbDetachedSignature.setDetachedSignatureName(detachedSignature.getDetachedSignatureName());
            dbDetachedSignature.setMimeType(detachedSignature.getMimeType());

            pDomibusConnectorMsgCont.setDetachedSignature(dbDetachedSignature);
        }
        return pDomibusConnectorMsgCont;
    }

    PDomibusConnectorMsgCont mapAttachment(PDomibusConnectorMessage message, DomibusConnectorMessageAttachment attachment) {
        DomibusConnectorMessageAttachment copiedAttachment = CopyHelper.copyAttachment(attachment);
        LargeFileReference bigDataReferenceCopy = createBigDataReferenceCopy(message.getConnectorMessageId(), copiedAttachment.getAttachment());
        copiedAttachment.setAttachment(bigDataReferenceCopy);

        return storeObjectIntoMsgCont(message, StoreType.MESSAGE_ATTACHMENT, bigDataReferenceCopy);
    }

    PDomibusConnectorMsgCont mapConfirmation(PDomibusConnectorMessage message, DomibusConnectorMessageConfirmation confirmation) {

        LargeFileReference xmlEvidence = largeFilePersistenceService
                .createDomibusConnectorBigDataReference(message.getConnectorMessageId(), confirmation.getEvidenceType().name(), MimeTypeUtils.APPLICATION_XML_VALUE);
        try ( OutputStream out = xmlEvidence.getOutputStream();) {
            StreamUtils.copy(confirmation.getEvidence(), out);
        } catch (IOException e) {
            throw new RuntimeException("unable to write BusinessXML to LargeFileProvider", e);
        }

        PDomibusConnectorMsgCont pDomibusConnectorMsgCont = storeObjectIntoMsgCont(message, StoreType.MESSAGE_CONFIRMATION_XML, xmlEvidence);
        return pDomibusConnectorMsgCont;

    }

    LargeFileReference createBigDataReferenceCopy(String connectorMessageId, LargeFileReference toCopy) {
        if (toCopy == null) {
            return null;
        }
        if (largeFilePersistenceService.isAvailable(toCopy)) {
            return toCopy;
        } else {
            LargeFileReference domibusConnectorBigDataReference = largeFilePersistenceService.createDomibusConnectorBigDataReference(connectorMessageId, toCopy.getName(), toCopy.getContentType());
            try (OutputStream os = domibusConnectorBigDataReference.getOutputStream();
                 InputStream is = toCopy.getInputStream()) {
                StreamUtils.copy(is, os);
                return domibusConnectorBigDataReference;
            } catch (IOException e) {
                throw new RuntimeException("Cannot read from InputStream or write to LargeFileProvider OutputStream!", e);
            }
        }
    }



    /**
     * Takes a StoreType and a LargeFile reference and creates
     * a PDomibusConnectorMsgCont out of it
     *
     * @param message - the db message
     * @param type    - the StorageType (is it a attachment, content, ...)
     * @param ref     - the large file reference
     */
    PDomibusConnectorMsgCont storeObjectIntoMsgCont(
            PDomibusConnectorMessage message,
            @Nonnull StoreType type,
            @Nonnull LargeFileReference ref) throws PersistenceException {
            PDomibusConnectorMsgCont msgCont = new PDomibusConnectorMsgCont();

            msgCont.setContentType(type);
            msgCont.setStorageProviderName(ref.getStorageProviderName());
            msgCont.setStorageReferenceId(ref.getStorageIdReference());

            return msgCont;
    }

    /**
     *
     * This method is deprecated, because the use of
     * java serialization is an immanent security problem,
     * but the code is still there for migration purposes
     * and will be removed with next connector minor release
     *
     * @param msgContent - the Entity object
     * @param clazz - the class of the domain object
     * @param <T> - the type of the domain object
     * @return the domain object, mapped from DB
     */
    @Deprecated
    <T> T mapFromMsgCont(@Nonnull PDomibusConnectorMsgCont msgContent, Class<T> clazz) {
        ObjectInputStream inputStream;
        try {
            byte[] byteContent = msgContent.getContent();
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteContent);
            inputStream = new ObjectInputStream(byteInputStream);
            T readObject = (T) inputStream.readObject();
            if (!clazz.isAssignableFrom(readObject.getClass())) {
                LOGGER.error("read unknown object from database!");
                throw new PersistenceException("read unknown object from database!");
            }
            return readObject;
        } catch (IOException ex) {
            String error = String.format("mapFromMsgCont: IOException occured during reading object out of message content [%s]", msgContent);
            LOGGER.error(error);
            throw new PersistenceException(error, ex);
        } catch (ClassNotFoundException ex) {
            String error = String.format("mapFromMsgCont: Class not found exception occured during reading object out of message content [%s], "
                    + "maybe incompatible updates or corruped database", msgContent);
            LOGGER.error(error);
            throw new PersistenceException(error, ex);
        }
    }


}
