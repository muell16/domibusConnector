package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.model.builder.*;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMsgContDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorDetachedSignature;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.model.PDomibusConnectorMsgCont;
import eu.domibus.connector.persistence.service.DomibusConnectorMessageContentManager;
import eu.domibus.connector.persistence.service.LargeFilePersistenceService;
import eu.domibus.connector.persistence.service.exceptions.LargeFileDeletionException;
import eu.domibus.connector.persistence.service.exceptions.PersistenceException;
import eu.domibus.connector.persistence.service.impl.helper.StoreType;
import liquibase.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StreamUtils;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

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
public class MsgContentPersistenceService implements DomibusConnectorMessageContentManager {

    public static final String BUSINESS_XML_DOCUMENT_IDENTIFIER = "BusinessDocumentXML";
    public static final String BUSINESS_XML_DOCUMENT_NAME = "BusinessDocument.xml";

    public static final String BUSINESS_DOCUMENT_IDENTIFIER = "BusinessDocument";

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
        loadMsgDocs(messageBuilder, findByMessage);

        //mapFromDbToDomain evidence back
        loadConfirmations(messageBuilder, findByMessage);

        //mapFromDbToDomain attachments back
        loadAttachments(messageBuilder, findByMessage);
    }

    private void loadConfirmations(DomibusConnectorMessageBuilder messageBuilder, List<PDomibusConnectorMsgCont> findByMessage) {
        findByMessage.stream()
                .filter(s -> StoreType.MESSAGE_CONFIRMATION_XML.equals(s.getContentType()))
                .forEach(c -> {

                    messageBuilder.addConfirmation(DomibusConnectorMessageConfirmationBuilder.createBuilder()
                            .setEvidence(c.getContent())
                            .setEvidenceType(DomibusConnectorEvidenceType.valueOf(c.getPayloadIdentifier()))
                            .build()
                    );

                });

        //legacy mapping
        findByMessage.stream()
                .filter(s -> StoreType.MESSAGE_CONFIRMATION.equals(s.getContentType()))
                .forEach(c -> {
                    DomibusConnectorMessageConfirmation msgConfirmation = mapFromMsgCont(c, DomibusConnectorMessageConfirmation.class);
                    messageBuilder.addConfirmation(msgConfirmation);
                });
    }

    private void loadAttachments(DomibusConnectorMessageBuilder messageBuilder, List<PDomibusConnectorMsgCont> findByMessage) {
        findByMessage.stream()
                .filter(s -> StoreType.MESSAGE_ATTACHMENT_CONTENT.equals(s.getContentType()))
                .forEach(c -> {
                    messageBuilder.addAttachment(
                        DomibusConnectorMessageAttachmentBuilder.createBuilder()
                            .setAttachment(loadLargeFileReference(c))
                            .setIdentifier(c.getPayloadIdentifier())
                            .withDescription(c.getPayloadDescription())
                            .withName(c.getPayloadName())
                            .build());
                });

        //legacy mapping
        findByMessage.stream()
                .filter(s -> StoreType.MESSAGE_ATTACHMENT.equals(s.getContentType()))
                .forEach(c -> {
                    DomibusConnectorMessageAttachment msgAttachment = mapFromMsgCont(c, DomibusConnectorMessageAttachment.class);
                    messageBuilder.addAttachment(msgAttachment);
                });

    }

    private void loadMsgDocs(DomibusConnectorMessageBuilder messageBuilder, List<PDomibusConnectorMsgCont> findByMessage) {

        DomibusConnectorMessageContentBuilder messageContentBuilder = DomibusConnectorMessageContentBuilder.createBuilder();
        Optional<PDomibusConnectorMsgCont> foundXmlContent = findByMessage.stream()
                .filter(s -> StoreType.MESSAGE_BUSINESSS_CONTENT_XML.equals(s.getContentType()))
                .findFirst();
        Optional<PDomibusConnectorMsgCont> foundDocumentContent = findByMessage.stream()
                .filter(s -> StoreType.MESSAGE_BUSINESS_CONTENT_DOCUMENT.equals(s.getContentType()))
                .findFirst();

        if (foundXmlContent.isPresent()) {
            PDomibusConnectorMsgCont xmlContent = foundXmlContent.get();

            messageContentBuilder.setXmlContent(xmlContent.getContent());

            if (foundDocumentContent.isPresent()) {
                loadDocumentContent(messageContentBuilder, foundDocumentContent.get());
            }
            messageBuilder.setMessageContent(messageContentBuilder.build());
        } else {
            //if nothing found call deprecated content loader
            loadMsgContent_legacy(messageBuilder, findByMessage);
        }

    }

    private void loadDocumentContent(DomibusConnectorMessageContentBuilder messageContent, PDomibusConnectorMsgCont pDomibusConnectorMsgCont) {
        LargeFileReference largeFileReference = loadLargeFileReference(pDomibusConnectorMsgCont);

        DomibusConnectorMessageDocumentBuilder documentBuilder = DomibusConnectorMessageDocumentBuilder.createBuilder();
        documentBuilder.setContent(largeFileReference);
        documentBuilder.setName(pDomibusConnectorMsgCont.getPayloadName());

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
        if (pDomibusConnectorMsgCont.getContent() != null) {
            largeFileReference.setText(new String(pDomibusConnectorMsgCont.getContent(), StandardCharsets.UTF_8));
        }
        largeFileReference.setName(pDomibusConnectorMsgCont.getPayloadName());
        largeFileReference.setMimetype(pDomibusConnectorMsgCont.getPayloadMimeType());
        largeFileReference.setSize(pDomibusConnectorMsgCont.getSize());
//        LargeFileReference readableDataSource = largeFilePersistenceService.getReadableDataSource(largeFileReference);
//        LargeFileReference largeFileReference = largeFilePersistenceService.getLargeFileReference(largeFileReference);
        return largeFileReference;
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
        if (messageContent != null && messageContent.getDocument() != null) {
            toStoreList.add(mapDocumentToDb(dbMessage, messageContent.getDocument()));
        }
        if (messageContent != null) {
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
        List<PDomibusConnectorMsgCont> byMessage = this.msgContDao.findByMessage(dbMessage);
        this.msgContDao.saveAll(toStoreList); //save new contents
        this.msgContDao.deleteAll(byMessage);
    }

    PDomibusConnectorMsgCont mapXmlContentToDB(String connectorMessageId, PDomibusConnectorMessage message, byte[] xmlDocument) {
        if (xmlDocument == null) {
            throw new IllegalArgumentException("Xml content is not allowed to be null!");
        }

        PDomibusConnectorMsgCont pDomibusConnectorMsgCont = storeObjectIntoMsgCont(message, StoreType.MESSAGE_BUSINESSS_CONTENT_XML, null);
        pDomibusConnectorMsgCont.setPayloadIdentifier(BUSINESS_XML_DOCUMENT_IDENTIFIER);
        pDomibusConnectorMsgCont.setPayloadName(BUSINESS_XML_DOCUMENT_NAME);
        pDomibusConnectorMsgCont.setPayloadMimeType(MimeTypeUtils.APPLICATION_XML_VALUE);

        //Business XML doc is stored as clob in DB
        pDomibusConnectorMsgCont.setContent(xmlDocument);

        return pDomibusConnectorMsgCont;
    }

    PDomibusConnectorMsgCont mapDocumentToDb(PDomibusConnectorMessage message, DomibusConnectorMessageDocument document) {
        if (document == null) {
            throw new IllegalArgumentException("document is not allowed to be null!");
        }
        PDomibusConnectorMsgCont pDomibusConnectorMsgCont = storeObjectIntoMsgCont(message, StoreType.MESSAGE_BUSINESS_CONTENT_DOCUMENT, document.getDocument());
        pDomibusConnectorMsgCont.setPayloadName(document.getDocumentName());
        pDomibusConnectorMsgCont.setDigest(document.getHashValue());
        pDomibusConnectorMsgCont.setPayloadIdentifier(BUSINESS_DOCUMENT_IDENTIFIER);

        DetachedSignature detachedSignature = document.getDetachedSignature();
        if (detachedSignature != null) {
            PDomibusConnectorDetachedSignature dbDetachedSignature = new PDomibusConnectorDetachedSignature();
            dbDetachedSignature.setDetachedSignature(detachedSignature.getDetachedSignature());
            dbDetachedSignature.setDetachedSignatureName(detachedSignature.getDetachedSignatureName());
            dbDetachedSignature.setMimeType(detachedSignature.getMimeType());

            pDomibusConnectorMsgCont.setDetachedSignature(dbDetachedSignature);
        }
        return pDomibusConnectorMsgCont;
    }

    PDomibusConnectorMsgCont mapAttachment(PDomibusConnectorMessage message, DomibusConnectorMessageAttachment attachment) {
        PDomibusConnectorMsgCont pDomibusConnectorMsgCont = storeObjectIntoMsgCont(message, StoreType.MESSAGE_ATTACHMENT_CONTENT, attachment.getAttachment());
        pDomibusConnectorMsgCont.setPayloadIdentifier(attachment.getIdentifier());
        pDomibusConnectorMsgCont.setPayloadName(attachment.getName());
        pDomibusConnectorMsgCont.setPayloadDescription(attachment.getDescription());
        pDomibusConnectorMsgCont.setPayloadMimeType(attachment.getMimeType());
        return pDomibusConnectorMsgCont;
    }

    PDomibusConnectorMsgCont mapConfirmation(PDomibusConnectorMessage message, DomibusConnectorMessageConfirmation confirmation) {
        PDomibusConnectorMsgCont pDomibusConnectorMsgCont = storeObjectIntoMsgCont(message, StoreType.MESSAGE_CONFIRMATION_XML, null);
        pDomibusConnectorMsgCont.setPayloadMimeType(MimeTypeUtils.APPLICATION_XML_VALUE);
        pDomibusConnectorMsgCont.setPayloadIdentifier(confirmation.getEvidenceType().toString());
        pDomibusConnectorMsgCont.setContent(confirmation.getEvidence());
        return pDomibusConnectorMsgCont;
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
            msgCont.setMessage(message);

            if (ref != null && StringUtils.isEmpty(ref.getStorageProviderName())) {
                LOGGER.debug("No storage provider is set for the large file reference [{}]!\nWill be converted to default Storage provider!", ref);
                ref = convertToDefaultStorageProvider(message.getConnectorMessageId(), ref);
            }
            if (ref != null && StringUtils.isEmpty(ref.getStorageIdReference())) {
                throw new PersistenceException("No storage id reference is set for the large file reference!");
            }
            if (ref != null && ref.getStorageProviderName() != null && !largeFilePersistenceService.isStorageProviderAvailable(ref)) {
                LOGGER.warn("Storage Provider [{}] is not available, will be converted to default provider [{}]",
                        ref.getStorageProviderName(),
                        largeFilePersistenceService.getDefaultProvider());
                ref = convertToDefaultStorageProvider(message.getConnectorMessageId(), ref);
            }
            if (ref != null) {
                msgCont.setStorageProviderName(ref.getStorageProviderName());
                msgCont.setStorageReferenceId(ref.getStorageIdReference());
                msgCont.setPayloadMimeType(ref.getContentType());
                msgCont.setPayloadName(ref.getName());
                msgCont.setSize(ref.getSize());
                if (StringUtils.isNotEmpty(ref.getText())) {
                    msgCont.setContent(ref.getText().getBytes(StandardCharsets.UTF_8));
                }
            }
//            try {
//				ref.getOutputStream().close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
            return msgCont;
    }

    private LargeFileReference convertToDefaultStorageProvider(String connectorMessageId, LargeFileReference ref) {
        LargeFileReference newRef = this.largeFilePersistenceService.createDomibusConnectorBigDataReference(connectorMessageId, ref.getName(), ref.getContentType());
        try (InputStream is = ref.getInputStream(); OutputStream os = newRef.getOutputStream()) {
            StreamUtils.copy(is, os);
            is.close();
        } catch (IOException e) {
        	throw new RuntimeException("Copying from unsupported LargeFileReference to default LargeFileReference failed due", e);
        }
        //also set storage name and provider for the "old" large file reference
        ref.setStorageProviderName(newRef.getStorageProviderName());
        ref.setStorageIdReference(newRef.getStorageIdReference());
        return newRef;
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


    @Override
    @Transactional
    public void cleanForMessage(DomibusConnectorMessage message) {
        List<PDomibusConnectorMsgCont> byMessage = findByMessage(message);

        //delete msg content fields within database
        byMessage
                .stream()
                .filter(msgCont -> msgCont.getContentType() != StoreType.MESSAGE_CONFIRMATION_XML) //do not delete evidences...
                .forEach(this::deleteMsgContent);

        //delete large file references, by calling the responsible LargeFilePersistenceProvider
        List<LargeFileDeletionException> deletionExceptions = new ArrayList<>();
        byMessage
                .stream()
                .filter(msgCont -> msgCont.getStorageProviderName() != null)
                .map(this::loadLargeFileReference)
                .forEach(ref ->  {
                    try {
                        largeFilePersistenceService.deleteDomibusConnectorBigDataReference(ref);
                    } catch (LargeFileDeletionException deletionException) {
                        deletionExceptions.add(deletionException);
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug(String.format("The following largeFile Reference [%s] will be deleted later by timer jobs.\n" +
                                    "Because I was unable to delete it now due the following exception:", ref), deletionException);
                        }
                    }
                });

        String storageRefs = deletionExceptions.stream().map(d -> d.getReferenceFailedToDelete().getStorageIdReference()).collect(Collectors.joining(","));
        LOGGER.info("The following storage references [{}] failed to be deleted immediately. The will be deleted later by timer jobs.", storageRefs);

    }

    /**
     * set the content and the delete date within the
     * database, and call save of the dao
     * @param pDomibusConnectorMsgCont - the message content
     */
    private void deleteMsgContent(PDomibusConnectorMsgCont pDomibusConnectorMsgCont) {
        pDomibusConnectorMsgCont.setContent(null);
        pDomibusConnectorMsgCont.setDeleted(new Date());
        this.msgContDao.save(pDomibusConnectorMsgCont);
    }

    /**
     * Finds all messageContent for a message
     * @param message - the message from DomainModel
     * @return a list of message content
     */
    public List<PDomibusConnectorMsgCont> findByMessage(DomibusConnectorMessage message) {
        PDomibusConnectorMessage dbMessage = this.msgDao.findOneByConnectorMessageId(message.getConnectorMessageId());
        return this.msgContDao.findByMessage(dbMessage);
    }
}
