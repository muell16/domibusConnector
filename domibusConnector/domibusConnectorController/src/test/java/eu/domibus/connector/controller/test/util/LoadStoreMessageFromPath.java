package eu.domibus.connector.controller.test.util;



//TODO: NOT FINIHSED YET

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.model.builder.*;
import eu.domibus.connector.domain.testutil.LargeFileReferenceGetSetBased;
import eu.domibus.connector.testdata.LoadStoreTransitionMessage;
import eu.ecodex.dc5.message.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import javax.annotation.Nullable;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Should load test messages from directory
 * store messages to directory
 *
 */
public class LoadStoreMessageFromPath {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadStoreMessageFromPath.class);

    private final Resource basicFolder;

    private Properties messageProperties;



    public static void storeMessageTo(Resource resource, DC5Message message) throws IOException {
        LoadStoreMessageFromPath loadStoreMessageFromPath = new LoadStoreMessageFromPath(resource);
        loadStoreMessageFromPath.storeMessage(message);
    }

    private LoadStoreMessageFromPath(Resource basicFolder) {
        this.basicFolder = basicFolder;
        this.messageProperties = new Properties();
    }




    private void storeMessage(DC5Message message) throws IOException {

        if (!basicFolder.exists()) {
            System.out.println("basic folder is: " + basicFolder.getFile().getAbsolutePath());
            basicFolder.getFile().mkdirs();
        }

        Resource propertiesResource = basicFolder.createRelative("/message.properties");
        if (propertiesResource.exists()) {
            throw new RuntimeException("message already exists cannot overwrite it!");
        }

        if (message.getEbmsData() != null) {
            storeEbmsMessageDetails(message.getEbmsData());
        }
        if (message.getBackendData() != null) {
            storeBackendData(message.getBackendData());
        }

        //store content
        DC5MessageContent messageContent = message.getMessageContent();
        if (messageContent != null) {
            DomibusConnectorMessageAttachment xmlContent = messageContent.getBusinessContent().getBusinessXml();
            Resource r = basicFolder.createRelative(LoadStoreTransitionMessage.DEFAULT_CONTENT_XML_FILE_NAME);
            messageProperties.put(LoadStoreTransitionMessage.MESSAGE_CONTENT_XML_PROP_NAME, LoadStoreTransitionMessage.DEFAULT_CONTENT_XML_FILE_NAME);
            writeBigDataReferenceToResource(r, xmlContent.getAttachment());

            //store message document
            DomibusConnectorMessageAttachment messageDocument = messageContent.getBusinessContent().getBusinessDocument();
            if (messageDocument != null) {

                String fileName = messageDocument.getIdentifier() == null ? "document.pdf" : messageDocument.getIdentifier();
                Resource d = basicFolder.createRelative(fileName);
                writeBigDataReferenceToResource(d, messageDocument.getAttachment());

                messageProperties.put(LoadStoreTransitionMessage.MESSAGE_DOCUMENT_FILE_PROP_NAME, fileName);

                if (messageDocument.getIdentifier() != null ) {
                    messageProperties.put(LoadStoreTransitionMessage.MESSAGE_DOCUMENT_NAME_PROP_NAME, messageDocument.getIdentifier());
                }
                if (messageDocument.getHash() != null) {
                    messageProperties.put(LoadStoreTransitionMessage.MESSAGE_DOCUMENT_HASH_PROP_NAME, messageDocument.getHash());
                }

                DetachedSignature detachedSignature = messageDocument.getDetachedSignature();
                if (detachedSignature != null) {
                    byte[] detachedSignatureBytes = detachedSignature.getDetachedSignature();
                    DetachedSignatureMimeType detachedSignatureMimeType = detachedSignature.getMimeType();
                    String detachedSignatureName = detachedSignature.getDetachedSignatureName() == null ? "detachedSignature" : detachedSignature.getDetachedSignatureName();

                    String appendix = detachedSignatureMimeType.name().toLowerCase();
                    String detachedResourceFilename = detachedSignatureName + "." + appendix;
                    Resource res = basicFolder.createRelative(detachedResourceFilename);

                    writeByteArrayToResource(res, detachedSignatureBytes);

                    messageProperties.put(LoadStoreTransitionMessage.MESSAGE_DOCUMENT_SIGNATURE_FILE_PROP_NAME, detachedResourceFilename);
                    messageProperties.put(LoadStoreTransitionMessage.MESSAGE_DOCUMENT_SIGNATURE_TYPE_PROP_NAME, detachedSignatureMimeType.name());
                    if (detachedSignatureName != null) {
                        messageProperties.put(LoadStoreTransitionMessage.MESSAGE_DOCUMENT_SIGNATURE_NAME_PROP_NAME, detachedSignatureName);
                    }

                }

            }

        }

        //store attachments
        List<DomibusConnectorMessageAttachment> attachments = message.getMessageContent().getBusinessContent().getAttachments();
        storeMessageAttachments(attachments);


        //store confirmations
        storeMessageConfirmations(message.getTransportedMessageConfirmations());

        File file = propertiesResource.getFile();
        System.out.println(file.getAbsolutePath());
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        messageProperties.store(fileOutputStream, null);

    }

    private void storeMessageConfirmations(List<DC5Confirmation> messageConfirmations) {
        for (int i = 0; i < messageConfirmations.size(); i++) {
            try {
                DC5Confirmation confirmation = messageConfirmations.get(i);

                String evidenceFilePropertyName = String.format("%s.%s.%s", LoadStoreTransitionMessage.MESSAGE_CONFIRMATIONS_PREFIX, i, "file");
                String evidenceTypePropertyName = String.format("%s.%s.%s", LoadStoreTransitionMessage.MESSAGE_CONFIRMATIONS_PREFIX, i, "type");

                messageProperties.put(evidenceTypePropertyName, confirmation.getEvidenceType().name());

                String fileName = confirmation.getEvidenceType().name() + ".xml";
                messageProperties.put(evidenceFilePropertyName, confirmation.getEvidenceType().name() + ".xml");

                Resource r = basicFolder.createRelative(fileName);
                writeByteArrayToResource(r, confirmation.getEvidence());

            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }

        }
    }


    private void storeMessageAttachments(List<DomibusConnectorMessageAttachment> attachments) {
        for (int i = 0; i < attachments.size(); i++) {
            try {
                DomibusConnectorMessageAttachment a = attachments.get(i);

                String attachmentPropertyFile = String.format("%s.%s.%s", LoadStoreTransitionMessage.MESSAGE_ATTACHMENT_PREFIX, i, "file");


                String fileName = a.getName();
                if (fileName == null) {
                    fileName = a.getIdentifier();
                }
                Resource attachmentOutputResource = basicFolder.createRelative("/" + fileName);
                writeBigDataReferenceToResource(attachmentOutputResource, a.getAttachment());
                messageProperties.put(attachmentPropertyFile, fileName);

                String attachmentPropertyName = String.format("%s.%s.%s", LoadStoreTransitionMessage.MESSAGE_ATTACHMENT_PREFIX, i, "identifier");
                messageProperties.put(attachmentPropertyName, a.getIdentifier());
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
    }

    private void writeBigDataReferenceToResource(Resource res, LargeFileReference bigDataReference) {
        try {
            File attachmentOutputFile = res.getFile();
            InputStream inputStream = null;

            inputStream = bigDataReference.getInputStream();

            FileOutputStream fileOutputStream = new FileOutputStream(attachmentOutputFile);
            StreamUtils.copy(inputStream, fileOutputStream);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }


    public static DC5Message loadMessageFrom(Resource resource) throws IOException {
        LoadStoreMessageFromPath loadStoreMessageFromPath = new LoadStoreMessageFromPath(resource);
        DC5Message message = loadStoreMessageFromPath.loadMessage();

        return message;
    }

    private DC5Message loadMessage() throws IOException {
        DomibusConnectorMessageBuilder messageBuilder = DomibusConnectorMessageBuilder.createBuilder();



        Resource propertiesResource = basicFolder.createRelative("message.properties");
        if (!propertiesResource.exists()) {
            throw new IOException("properties " + propertiesResource + " does not exist!");
        }

        messageProperties.load(propertiesResource.getInputStream());

        messageBuilder.setMessageDetails(loadMessageDetailsFromProperties());

        DC5MessageContent content = new DC5MessageContent();
        content.setBusinessContent(new DC5BackendContent());
        content.setEcodexContent(new DC5EcodexContent());

        Resource backendContentResource = createRelativeResource(messageProperties.getProperty(LoadStoreTransitionMessage.BACKEND_MESSAGE_CONTENT_PROP_NAME));
        if (backendContentResource != null && backendContentResource.exists()) {
            LargeFileReference backendContentR = loadResourceAsBigDataRef(backendContentResource);
            content.getBusinessContent().setBusinessXml(DomibusConnectorMessageAttachment
                    .builder()
                    .attachment(backendContentR)
                    .identifier("BUSINESS_XML")
                    .build());
        }

        Resource contentResource = createRelativeResource(messageProperties.getProperty(LoadStoreTransitionMessage.MESSAGE_CONTENT_XML_PROP_NAME));
        if (contentResource != null && contentResource.exists()) {

            LargeFileReference largeFileReference = loadResourceAsBigDataRef(contentResource);

            content.getEcodexContent().setBusinessXml(DomibusConnectorMessageAttachment
                    .builder()
                    .attachment(largeFileReference)
                    .identifier("BUSINESS_XML")
                    .build());

            messageBuilder.setMessageContent(content);
            //load document
            String docFileName = messageProperties.getProperty("message.content.document.file");
            if (docFileName != null) {
                DomibusConnectorMessageAttachmentBuilder documentBuilder = DomibusConnectorMessageAttachmentBuilder.createBuilder();
                Resource r = basicFolder.createRelative(docFileName);
                LargeFileReference bigDataReferenceDocument = loadResourceAsBigDataRef(r);
                documentBuilder.setAttachment(bigDataReferenceDocument);
                String docName = messageProperties.getProperty("message.content.document.name");
                documentBuilder.setIdentifier(docName);

                //load signature
                String signatureFileName = messageProperties.getProperty("message.content.document.signature.file");
                if (signatureFileName != null) {

                    DetachedSignatureMimeType mimeType = DetachedSignatureMimeType.valueOf(messageProperties.getProperty("message.content.document.signature.type"));
                    String name = messageProperties.getProperty("message.content.document.signature.name");
                    Resource fResource = basicFolder.createRelative(signatureFileName);
                    byte[] signatureBytes = loadResourceAsByteArray(fResource);

                    DetachedSignature detachedSignature = DetachedSignatureBuilder.createBuilder()
                            .setMimeType(mimeType)
                            .setName(name)
                            .setSignature(signatureBytes)
                            .build();
                    documentBuilder.withDetachedSignature(detachedSignature);
                }

                content.getBusinessContent().setBusinessDocument(documentBuilder.build());

            }

        }

        messageBuilder.addAttachments(loadAttachments());
        messageBuilder.addTransportedConfirmations(loadConfirmations());

        String connid = messageProperties.getProperty("message.connector-id", null);
        if (connid != null) {
            messageBuilder.setConnectorMessageId(connid);
        }

        return messageBuilder.build();

    }




    private List<DC5Confirmation> loadConfirmations() {
        return messageProperties.stringPropertyNames()
                .stream()
                .sorted()
                .filter( (k) -> k.startsWith(LoadStoreTransitionMessage.MESSAGE_CONFIRMATIONS_PREFIX))
                .map( (k) -> k.split("\\.")[2])
                .distinct()
                .map( (k) -> {


                        String evidenceFilePropertyName = String.format("%s.%s.%s", LoadStoreTransitionMessage.MESSAGE_CONFIRMATIONS_PREFIX, k, "file");
                        String evidenceTypePropertyName = String.format("%s.%s.%s", LoadStoreTransitionMessage.MESSAGE_CONFIRMATIONS_PREFIX, k, "type");

                        Resource resEvidenceFile = createRelativeResource(messageProperties.getProperty(evidenceFilePropertyName));
                        DomibusConnectorEvidenceType domibusConnectorEvidenceType = DomibusConnectorEvidenceType.valueOf(messageProperties.getProperty(evidenceTypePropertyName));

                        DomibusConnectorMessageConfirmationBuilder builder = DomibusConnectorMessageConfirmationBuilder
                                .createBuilder()
                                .setEvidence(loadResourceAsByteArray(resEvidenceFile))
                                .setEvidenceType(domibusConnectorEvidenceType);

                        return builder.build();

                })
                .collect(Collectors.toList());
    }



    private List<DomibusConnectorMessageAttachment> loadAttachments() {

        return messageProperties.stringPropertyNames()
                .stream()
                .sorted()
                .filter( (k) -> k.startsWith(LoadStoreTransitionMessage.MESSAGE_ATTACHMENT_PREFIX) )
                .map( (k) -> k.split("\\.")[2])
                .distinct()
                .map( (k) -> {
                    try {
                        DomibusConnectorMessageAttachmentBuilder builder = DomibusConnectorMessageAttachmentBuilder.createBuilder();
                        String filePropertyName = String.format("%s.%s.%s", LoadStoreTransitionMessage.MESSAGE_ATTACHMENT_PREFIX, k, "file");
                        String identifierPropertyName = String.format("%s.%s.%s", LoadStoreTransitionMessage.MESSAGE_ATTACHMENT_PREFIX, k, "identifier");
                        Resource res = basicFolder.createRelative(messageProperties.getProperty(filePropertyName));
                        builder.setAttachment(loadResourceAsBigDataRef(res));
                        builder.setIdentifier(messageProperties.getProperty(identifierPropertyName));

                        return builder.build();

                    } catch (IOException ioe) {
                        throw new RuntimeException(ioe);
                    }
                })
                .collect(Collectors.toList());
    }

    private @Nullable Resource createRelativeResource(@Nullable String relativePath) {
        if (relativePath == null) {
            return null;
        }
        try {
            Resource contentResource = basicFolder.createRelative(relativePath);
            return contentResource;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }



    public static LargeFileReference loadResourceAsBigDataRef(Resource resource) {
        try {
            InputStream inputStream = resource.getInputStream();

            LargeFileReferenceGetSetBased inMemory = new LargeFileReferenceGetSetBased();
            inMemory.setBytes(StreamUtils.copyToByteArray(inputStream));
            inMemory.setReadable(true);
            inMemory.setStorageIdReference(UUID.randomUUID().toString());
            return inMemory;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private byte[] loadResourceAsByteArray(Resource res) {
        try {
            InputStream inputStream = res.getInputStream();
            return StreamUtils.copyToByteArray(inputStream);
        } catch(IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private void writeByteArrayToResource(Resource res, byte[] bytes) {
        try {
            File f = res.getFile();
            FileOutputStream fileOutputStream = new FileOutputStream(f);

            org.springframework.util.StreamUtils.copy(bytes, fileOutputStream);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void storeBackendData(DC5BackendData backendData) {
        if (backendData.getBackendMessageId() != null) {
            messageProperties.put(LoadStoreTransitionMessage.BACKEND_MESSAGE_ID_PROP_NAME, backendData.getBackendMessageId());
        }
        if (backendData.getRefToBackendMessageId() != null) {
            messageProperties.put(LoadStoreTransitionMessage.BACKEND_REF_BACKEND_MESSAGE_ID_PROP_NAME, backendData.getBackendMessageId());
        }
        if (backendData.getBackendConversationId() != null) {
            messageProperties.put(LoadStoreTransitionMessage.BACKEND_CONVERSATION_ID_PROP_NAME, backendData.getBackendMessageId());
        }
    }

    public static final String SENDER_PREFIX = "sender.";
    public static final String RECEIVER_PREFIX = "receiver.";

    private void storeEbmsMessageDetails(DC5Ebms details) {

        if (details.getAction() != null && details.getAction().getAction() != null) {
            messageProperties.put(LoadStoreTransitionMessage.ACTION_PROP_NAME, details.getAction().getAction());
        }

        if (details.getSender() != null) {
            Map<String, String> senderProperties = getSenderProperties(details.getSender());
            senderProperties.forEach((key, value) ->
                    messageProperties.put(SENDER_PREFIX + key, value));
        }
        if (details.getReceiver() != null) {
            Map<String, String> senderProperties = getSenderProperties(details.getSender());
            senderProperties.forEach((key, value) ->
                    messageProperties.put(RECEIVER_PREFIX + key, value));
        }

        if (details.getService() != null && details.getService() != null) {
            messageProperties.put(LoadStoreTransitionMessage.SERVICE_NAME_PROP_NAME, details.getService().getService());
            messageProperties.put(LoadStoreTransitionMessage.SERVICE_TYPE_PROP_NAME, details.getService().getServiceType());
        }

        if (details.getEbmsMessageId() != null) {
            messageProperties.put(LoadStoreTransitionMessage.EBMS_ID_PROP_NAME, details.getEbmsMessageId());
        }
        if (details.getConversationId() != null) {
            messageProperties.put(LoadStoreTransitionMessage.CONVERSATION_ID_PROP_NAME, details.getConversationId());
        }

    }

    public static final String PARTY_ID_PROP_NAME = "party-id";
    public static final String PARTY_ID_TYPE_PROP_NAME = "party-id-type";
    public static final String PARTY_ROLE_NAME_PROP_NAME = "role";
    public static final String PARTY_ROLE_TYPE_PROP_NAME = "role-type";
    public static final String ECX_ADDRESS_PROP_NAME = "ecxaddress";

    private Map<String, String> getSenderProperties(DC5EcxAddress addr) {
        Map<String, String> p = new HashMap<>();
        p.put(ECX_ADDRESS_PROP_NAME, addr.getEcxAddress());
        if (addr.getParty() != null) {
            p.put(PARTY_ID_PROP_NAME, addr.getParty().getPartyId());
            p.put(PARTY_ID_TYPE_PROP_NAME, addr.getParty().getPartyIdType());
        }
        if (addr.getRole() != null) {
            p.put(PARTY_ROLE_NAME_PROP_NAME, addr.getRole().getRole());
            p.put(PARTY_ROLE_TYPE_PROP_NAME, addr.getRole().getRoleType().name());
        }
        return p;
    }


    private DC5BackendData loadBackendDetailsFromProperties() {
        return DC5BackendData.builder()
                .backendMessageId(BackendMessageId.ofString(messageProperties.getProperty(LoadStoreTransitionMessage.BACKEND_MESSAGE_ID_PROP_NAME)))
                .backendConversationId(messageProperties.getProperty(LoadStoreTransitionMessage.BACKEND_CONVERSATION_ID_PROP_NAME))
                .refToBackendMessageId(BackendMessageId.ofString(messageProperties.getProperty(LoadStoreTransitionMessage.BACKEND_REF_BACKEND_MESSAGE_ID_PROP_NAME)))
                .build();
    }


    private DC5Ebms loadMessageDetailsFromProperties() {

        return DC5Ebms.builder()
                .action(DC5Action.builder().action(messageProperties.getProperty("action")).build())
                .service(DC5Service.builder()
                                        .service(messageProperties.getProperty(LoadStoreTransitionMessage.SERVICE_NAME_PROP_NAME))
                                        .serviceType(messageProperties.getProperty(LoadStoreTransitionMessage.SERVICE_TYPE_PROP_NAME))
                                                .build())
                .ebmsMessageId(EbmsMessageId.ofString(messageProperties.getProperty(LoadStoreTransitionMessage.EBMS_ID_PROP_NAME)))
                .conversationId(messageProperties.getProperty(LoadStoreTransitionMessage.CONVERSATION_ID_PROP_NAME))
                .refToEbmsMessageId(EbmsMessageId.ofString(messageProperties.getProperty(LoadStoreTransitionMessage.REF_TO_MESSAGE_ID_PROP_NAME)))
                .sender(loadEcxAddress(SENDER_PREFIX))
                .receiver(loadEcxAddress(RECEIVER_PREFIX))
                .build();

    }

    private DC5EcxAddress loadEcxAddress(String senderPrefix) {

         return DC5EcxAddress.builder()
                .ecxAddress(messageProperties.getProperty(senderPrefix + ECX_ADDRESS_PROP_NAME))
                .party(DC5Party.builder()
                        .partyId(messageProperties.getProperty(senderPrefix + PARTY_ID_PROP_NAME))
                        .partyIdType(messageProperties.getProperty(senderPrefix + PARTY_ID_TYPE_PROP_NAME))
                        .build()
                ).role(DC5Role.builder()
                        .role(messageProperties.getProperty(senderPrefix + PARTY_ROLE_NAME_PROP_NAME))
                        .roleType(DC5RoleType.INITIATOR) //TODO: read from properties...?
                        .build()
                ).build();

    }


}
