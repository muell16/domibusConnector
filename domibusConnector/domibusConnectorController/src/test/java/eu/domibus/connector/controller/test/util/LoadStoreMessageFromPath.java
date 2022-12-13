package eu.domibus.connector.controller.test.util;


//TODO: NOT FINIHSED YET

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.model.builder.*;
import eu.domibus.connector.domain.testutil.LargeFileReferenceGetSetBased;
import eu.domibus.connector.testdata.LoadStoreTransitionMessage;
import eu.ecodex.dc5.message.model.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.DigestUtils;
import org.springframework.util.StreamUtils;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Should load test messages from directory
 * store messages to directory
 */
public class LoadStoreMessageFromPath {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadStoreMessageFromPath.class);

    public static final String ECX_MESSAGE_ASIC_CONTENT_PROP_NAME = "ecx.asics";
    public static final String ECX_MESSAGE_XML_CONTENT_PROP_NAME = "ecx.xml";
    public static final String ECX_MESSAGE_TOKEN_XML_CONTENT_PROP_NAME = "ecx.tokenxml";
    public static final String PARTY_ID_PROP_NAME = "party-id";
    public static final String PARTY_ID_TYPE_PROP_NAME = "party-id-type";
    public static final String PARTY_ROLE_NAME_PROP_NAME = "role";
    public static final String PARTY_ROLE_TYPE_PROP_NAME = "role-type";
    public static final String ECX_ADDRESS_PROP_NAME = "ecxaddress";

    public static final String BACKEND_MESSAGE_XML_CONTENT_PROP_NAME = "backend.content.xml";
    public static final String BACKEND_MESSAGE_DOC_CONTENT_PROP_NAME = "backend.content.doc";
    public static final String BACKEND_MESSAGE_TRUST_TOKEN_PDF_PROP_NAME = "backend.content.token.pdf";
    public static final String BACKEND_MESSAGE_TRUST_TOKEN_XML_PROP_NAME = "backend.content.token.xml";
    public static final String BACKEND_MESSAGE_ATTACHMENTS_PREFIX = "backend.content.attachment";


    public static final String ATTACHMENT_NAME_POSTFIX = ".name";
    public static final String ATTACHMENT_IDENTIFIER_POSTFIX = ".identifier";
    public static final String ATTACHMENT_DIGEST_POSTFIX = ".digest";
    public static final String ATTACHMENT_FILENAME_POSTFIX = ".filename";

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
        if (message.getMessageContent() != null && message.getMessageContent().getBusinessContent() != null) {
            storeBackendContent(message.getMessageContent().getBusinessContent());
        }
        if (message.getMessageContent() != null && message.getMessageContent().getEcodexContent() != null) {
            storeEcodexContent(message.getMessageContent().getEcodexContent());
        }


        //store confirmations
        storeMessageConfirmations(message.getTransportedMessageConfirmations());

        File file = propertiesResource.getFile();
        System.out.println(file.getAbsolutePath());
        boolean created = file.createNewFile();
        if (!created) {
            throw new IllegalStateException("Cannot continue, file creation failed!");
        }
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


    private void storeMessageAttachments(List<DomibusConnectorMessageAttachment> attachments, String prefix) {
        if (attachments != null) {
            for (int i = 0; i < attachments.size(); i++) {
                DomibusConnectorMessageAttachment a = attachments.get(i);
                String p = calculatePrefixFromIndex(prefix, i);
                storeResource(a, p);
            }
        }
    }

    private String calculatePrefixFromIndex(String prefix, int i) {
        return String.format("%s[%d]", prefix, i);
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
        DC5Message.DC5MessageBuilder builder = DC5Message.builder();

        Resource propertiesResource = basicFolder.createRelative("message.properties");
        if (!propertiesResource.exists()) {
            throw new IOException("properties " + propertiesResource + " does not exist!");
        }

        messageProperties.load(propertiesResource.getInputStream());

//        messageBuilder.setMessageDetails(loadMessageDetailsFromProperties());

        DC5MessageContent.DC5MessageContentBuilder contentBuilder = DC5MessageContent.builder();

        if (messageProperties.containsKey(BACKEND_MESSAGE_DOC_CONTENT_PROP_NAME + ATTACHMENT_FILENAME_POSTFIX)) {
            contentBuilder.businessContent(loadBackendContent().build());
        }
        if (messageProperties.containsKey(ECX_MESSAGE_ASIC_CONTENT_PROP_NAME + ATTACHMENT_FILENAME_POSTFIX)) {
            contentBuilder.ecodexContent(loadEcodexContent().build());
        }
        if (messageProperties.containsKey(BACKEND_MESSAGE_DOC_CONTENT_PROP_NAME + ATTACHMENT_FILENAME_POSTFIX)
                || messageProperties.containsKey(ECX_MESSAGE_ASIC_CONTENT_PROP_NAME + ATTACHMENT_FILENAME_POSTFIX)) {
            builder.messageContent(contentBuilder.build());
        }

        builder.ebmsData(loadEcxDatailsFromProperties().build());
        builder.backendData(loadBackendDetailsFromProperties().build());


//        Resource contentResource = createRelativeResource(messageProperties.getProperty(LoadStoreTransitionMessage.MESSAGE_CONTENT_XML_PROP_NAME));
//        if (contentResource != null && contentResource.exists()) {
//
//            LargeFileReference largeFileReference = loadResourceAsBigDataRef(contentResource);
//
//            content.getEcodexContent().setBusinessXml(DomibusConnectorMessageAttachment
//                    .builder()
//                    .attachment(largeFileReference)
//                    .digest(largeFileReference.getDigest())
//                    .identifier("BUSINESS_XML")
//                    .build());
//
//            messageBuilder.setMessageContent(content);
//            //load document
//            String docFileName = messageProperties.getProperty("message.content.document.file");
//            if (docFileName != null) {
//                DomibusConnectorMessageAttachmentBuilder documentBuilder = DomibusConnectorMessageAttachmentBuilder.createBuilder();
//                Resource r = basicFolder.createRelative(docFileName);
//                LargeFileReference bigDataReferenceDocument = loadResourceAsBigDataRef(r);
//                documentBuilder.setAttachment(bigDataReferenceDocument);
//                String docName = messageProperties.getProperty("message.content.document.name");
//                documentBuilder.setIdentifier(docName);
//
//                //load signature
//                String signatureFileName = messageProperties.getProperty("message.content.document.signature.file");
//                if (signatureFileName != null) {
//
//                    DetachedSignatureMimeType mimeType = DetachedSignatureMimeType.valueOf(messageProperties.getProperty("message.content.document.signature.type"));
//                    String name = messageProperties.getProperty("message.content.document.signature.name");
//                    Resource fResource = basicFolder.createRelative(signatureFileName);
//                    byte[] signatureBytes = loadResourceAsByteArray(fResource);
//
//                    DetachedSignature detachedSignature = DetachedSignatureBuilder.createBuilder()
//                            .setMimeType(mimeType)
//                            .setName(name)
//                            .setSignature(signatureBytes)
//                            .build();
//                    documentBuilder.withDetachedSignature(detachedSignature);
//                }
//
//                content.getBusinessContent().setBusinessDocument(documentBuilder.build());
//
//            }
//
//        }

//        messageBuilder.addAttachments(loadAttachments());
//        messageBuilder.addTransportedConfirmations(loadConfirmations());
        builder.transportedMessageConfirmations(loadConfirmations());

        String connid = messageProperties.getProperty("message.connector-id", null);
        if (connid != null) {
            builder.connectorMessageId(DomibusConnectorMessageId.ofString(connid));
        }

        return builder.build();

    }




    private DC5BackendContent.DC5BackendContentBuilder loadBackendContent() {
        DC5BackendContent.DC5BackendContentBuilder builder = DC5BackendContent.builder();

        if (existsInProperties(BACKEND_MESSAGE_XML_CONTENT_PROP_NAME)) {
            builder.businessXml(loadResource(BACKEND_MESSAGE_XML_CONTENT_PROP_NAME).build());
        }
        if (existsInProperties(BACKEND_MESSAGE_DOC_CONTENT_PROP_NAME)) {
            builder.businessDocument(loadResource(BACKEND_MESSAGE_DOC_CONTENT_PROP_NAME).build());
        }
        if (existsInProperties(BACKEND_MESSAGE_TRUST_TOKEN_XML_PROP_NAME)) {
            builder.trustTokenXml(loadResource(BACKEND_MESSAGE_TRUST_TOKEN_XML_PROP_NAME).build());
        }
        if (existsInProperties(BACKEND_MESSAGE_TRUST_TOKEN_PDF_PROP_NAME)) {
            builder.trustTokenPDF(loadResource(BACKEND_MESSAGE_TRUST_TOKEN_PDF_PROP_NAME).build());
        }

        List<DomibusConnectorMessageAttachment> domibusConnectorMessageAttachments = loadAttachments(BACKEND_MESSAGE_ATTACHMENTS_PREFIX);

        return builder.attachments(domibusConnectorMessageAttachments);

    }

    private DC5EcodexContent.DC5EcodexContentBuilder loadEcodexContent() {
        DC5EcodexContent.DC5EcodexContentBuilder builder = DC5EcodexContent.builder();
        if (existsInProperties(ECX_MESSAGE_XML_CONTENT_PROP_NAME)) {
            builder.businessXml(loadResource(ECX_MESSAGE_XML_CONTENT_PROP_NAME).build());
        }
        if (existsInProperties(ECX_MESSAGE_ASIC_CONTENT_PROP_NAME)) {
            builder.asicContainer(loadResource(ECX_MESSAGE_ASIC_CONTENT_PROP_NAME).build());
        }
        if (existsInProperties(ECX_MESSAGE_TOKEN_XML_CONTENT_PROP_NAME)) {
            builder.trustTokenXml(loadResource(ECX_MESSAGE_TOKEN_XML_CONTENT_PROP_NAME).build());
        }
        return builder;
    }


    private boolean existsInProperties(String prefix) {
        return messageProperties.containsKey(prefix + ATTACHMENT_FILENAME_POSTFIX);
    }


    private void storeBackendContent(DC5BackendContent content) {
        storeIfNotNull(content.getBusinessDocument(), BACKEND_MESSAGE_DOC_CONTENT_PROP_NAME);
        storeIfNotNull(content.getBusinessXml(), BACKEND_MESSAGE_XML_CONTENT_PROP_NAME);
        storeIfNotNull(content.getTrustTokenXml(), BACKEND_MESSAGE_TRUST_TOKEN_XML_PROP_NAME);
        storeIfNotNull(content.getTrustTokenPDF(), BACKEND_MESSAGE_TRUST_TOKEN_PDF_PROP_NAME);
        storeMessageAttachments(content.getAttachments(), BACKEND_MESSAGE_ATTACHMENTS_PREFIX);
    }


    private void storeEcodexContent(DC5EcodexContent content) {
        storeIfNotNull(content.getAsicContainer(), ECX_MESSAGE_ASIC_CONTENT_PROP_NAME);
        storeIfNotNull(content.getBusinessXml(), ECX_MESSAGE_XML_CONTENT_PROP_NAME);
        storeIfNotNull(content.getTrustTokenXml(), ECX_MESSAGE_TOKEN_XML_CONTENT_PROP_NAME);
    }

    private void storeIfNotNull(DomibusConnectorMessageAttachment a, String prefix) {
        if (a != null) {
            storeResource(a, prefix);
        }
    }


    private List<DC5Confirmation> loadConfirmations() {
        return messageProperties.stringPropertyNames()
                .stream()
                .sorted()
                .filter((k) -> k.startsWith(LoadStoreTransitionMessage.MESSAGE_CONFIRMATIONS_PREFIX))
                .map((k) -> k.split("\\.")[2])
                .distinct()
                .map((k) -> {


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


    private List<DomibusConnectorMessageAttachment> loadAttachments(String attachmentPrefix) {

        return messageProperties.stringPropertyNames()
                .stream()
                .sorted()
                .filter((k) -> k.startsWith(attachmentPrefix))
                .map((k) -> k.split("]")[0] + "]")
                .distinct()
                .map(this::loadResource)
                .map(DomibusConnectorMessageAttachment.DomibusConnectorMessageAttachmentBuilder::build)
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
            byte[] bytes = StreamUtils.copyToByteArray(inputStream);
            String hash = DigestUtils.md5DigestAsHex(bytes);
            Digest digest = Digest.builder()
                    .digestAlgorithm("md5")
                    .digestValue(hash)
                    .build();

            LargeFileReferenceGetSetBased inMemory = new LargeFileReferenceGetSetBased();
            inMemory.setBytes(bytes);
            inMemory.setReadable(true);
            inMemory.setStorageIdReference(UUID.randomUUID().toString());
            inMemory.setDigest(digest);
            return inMemory;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private byte[] loadResourceAsByteArray(Resource res) {
        try {
            InputStream inputStream = res.getInputStream();
            return StreamUtils.copyToByteArray(inputStream);
        } catch (IOException ioe) {
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
            messageProperties.put(LoadStoreTransitionMessage.BACKEND_MESSAGE_ID_PROP_NAME, backendData.getBackendMessageId().getBackendMessageId());
        }
        if (backendData.getRefToBackendMessageId() != null) {
            messageProperties.put(LoadStoreTransitionMessage.BACKEND_REF_BACKEND_MESSAGE_ID_PROP_NAME, backendData.getBackendMessageId().getBackendMessageId());
        }
        if (backendData.getBackendConversationId() != null) {
            messageProperties.put(LoadStoreTransitionMessage.BACKEND_CONVERSATION_ID_PROP_NAME, backendData.getBackendConversationId());
        }
    }


    @SneakyThrows
    private DomibusConnectorMessageAttachment.DomibusConnectorMessageAttachmentBuilder loadResource(String propertyPrefix) {
        String filename = messageProperties.getProperty(propertyPrefix + ATTACHMENT_FILENAME_POSTFIX);
        if (filename == null) {
            filename = getFileNameFromPrefix(propertyPrefix);
        }

        LargeFileReference largeFileReference = loadResourceAsBigDataRef(basicFolder.createRelative(filename));

        return DomibusConnectorMessageAttachment.builder()
                .name(messageProperties.getProperty(propertyPrefix + ATTACHMENT_NAME_POSTFIX))
                .identifier(messageProperties.getProperty(propertyPrefix + ATTACHMENT_IDENTIFIER_POSTFIX))
                .digest(Digest.ofString(messageProperties.getProperty(propertyPrefix + ATTACHMENT_DIGEST_POSTFIX)))
                .attachment(largeFileReference);
    }

    private static String getFileNameFromPrefix(String propertyPrefix) {
        String filename = propertyPrefix.replaceAll("(\\[\\])", "_");
        return filename;
    }

    private void storeResource(DomibusConnectorMessageAttachment a, String propertyPrefix) {
        if (a.getAttachment() == null) {
            return;
        }

        String filename = getFileNameFromPrefix(propertyPrefix);

        messageProperties.put(propertyPrefix + ATTACHMENT_FILENAME_POSTFIX, filename);
        messageProperties.put(propertyPrefix + ATTACHMENT_IDENTIFIER_POSTFIX, a.getIdentifier());
        if (a.getName() != null) {
            messageProperties.put(propertyPrefix + ATTACHMENT_NAME_POSTFIX, a.getName());
        }
        messageProperties.put(propertyPrefix + ATTACHMENT_DIGEST_POSTFIX, Digest.convertToString(a.getDigest()));

        File file;
        try {
            file = basicFolder.createRelative(filename).getFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (OutputStream os = Files.newOutputStream(file.toPath()); InputStream is = a.getAttachment().getInputStream()) {
            StreamUtils.copy(is, os);
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    private DC5Ebms.DC5EbmsBuilder loadEcxDatailsFromProperties() {
        DC5Ebms.DC5EbmsBuilder builder = DC5Ebms.builder();
        builder.receiver(loadEcxAddress(RECEIVER_PREFIX));
        builder.sender(loadEcxAddress(SENDER_PREFIX));
        builder.action(DC5Action.builder()
                        .action(messageProperties.getProperty(LoadStoreTransitionMessage.ACTION_PROP_NAME))
                .build());
        builder.service(DC5Service.builder()
                        .serviceType(messageProperties.getProperty(LoadStoreTransitionMessage.SERVICE_TYPE_PROP_NAME))
                        .service(messageProperties.getProperty(LoadStoreTransitionMessage.SERVICE_NAME_PROP_NAME))
                .build());
        builder.ebmsMessageId(EbmsMessageId.ofString(messageProperties.getProperty(LoadStoreTransitionMessage.EBMS_ID_PROP_NAME)));
        builder.refToEbmsMessageId(EbmsMessageId.ofString(messageProperties.getProperty(LoadStoreTransitionMessage.CONVERSATION_ID_PROP_NAME)));

        return builder;
    }

    private DC5BackendData.DC5BackendDataBuilder loadBackendDetailsFromProperties() {
        DC5BackendData.DC5BackendDataBuilder builder = DC5BackendData.builder();
        if (messageProperties.getProperty(LoadStoreTransitionMessage.BACKEND_MESSAGE_ID_PROP_NAME) != null) {
            builder.backendMessageId(BackendMessageId.ofString(messageProperties.getProperty(LoadStoreTransitionMessage.BACKEND_MESSAGE_ID_PROP_NAME)));
        }
        if (messageProperties.getProperty(LoadStoreTransitionMessage.BACKEND_CONVERSATION_ID_PROP_NAME) != null) {
            builder.backendConversationId(messageProperties.getProperty(LoadStoreTransitionMessage.BACKEND_CONVERSATION_ID_PROP_NAME));
        }
        if (messageProperties.getProperty(LoadStoreTransitionMessage.BACKEND_REF_BACKEND_MESSAGE_ID_PROP_NAME) != null) {
            builder.refToBackendMessageId(BackendMessageId.ofString(messageProperties.getProperty(LoadStoreTransitionMessage.BACKEND_REF_BACKEND_MESSAGE_ID_PROP_NAME)));
        }
        return builder;
    }


    private DC5Ebms loadMessageDetailsFromProperties() {

        DC5Ebms.DC5EbmsBuilder builder = DC5Ebms.builder()
                .action(DC5Action.builder().action(messageProperties.getProperty("action")).build())
                .service(DC5Service.builder()
                        .service(messageProperties.getProperty(LoadStoreTransitionMessage.SERVICE_NAME_PROP_NAME))
                        .serviceType(messageProperties.getProperty(LoadStoreTransitionMessage.SERVICE_TYPE_PROP_NAME))
                        .build());
        if (!StringUtils.isBlank(messageProperties.getProperty(LoadStoreTransitionMessage.EBMS_ID_PROP_NAME))) {
            builder.ebmsMessageId(EbmsMessageId.ofString(messageProperties.getProperty(LoadStoreTransitionMessage.EBMS_ID_PROP_NAME)));
        }
        if (!StringUtils.isBlank(messageProperties.getProperty(LoadStoreTransitionMessage.CONVERSATION_ID_PROP_NAME))) {
            builder.conversationId(messageProperties.getProperty(LoadStoreTransitionMessage.CONVERSATION_ID_PROP_NAME));
        }
        if (!StringUtils.isBlank(messageProperties.getProperty(LoadStoreTransitionMessage.REF_TO_MESSAGE_ID_PROP_NAME))) {
            builder.refToEbmsMessageId(EbmsMessageId.ofString(messageProperties.getProperty(LoadStoreTransitionMessage.REF_TO_MESSAGE_ID_PROP_NAME)));
        }
        builder.sender(loadEcxAddress(SENDER_PREFIX));
        return builder.build();
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
