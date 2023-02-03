package eu.domibus.connector.controller.test.util;


//TODO: NOT FINIHSED YET

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.model.builder.*;
import eu.domibus.connector.domain.testutil.LargeFileReferenceGetSetBased;
import eu.domibus.connector.persistence.service.LargeFilePersistenceService;
import eu.domibus.connector.testdata.LoadStoreTransitionMessage;
import eu.ecodex.dc5.message.model.*;
import eu.ecodex.dc5.process.MessageProcessId;
import eu.ecodex.dc5.process.model.DC5MsgProcess;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.DigestUtils;
import org.springframework.util.StreamUtils;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Should load test messages from directory
 * store messages to directory
 */
public class LoadStoreMessageFromPath {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadStoreMessageFromPath.class);

    public static final String MESSAGE_TARGET_PROP_NAME = "msg.target";
    public static final String MESSAGE_SOURCE_PROP_NAME = "msg.source";

    public static final String MESSAGE_PROCESS_PROP_NAME = "msg.process";
    public static String INITIATOR_ROLE_PROP_NAME = "role.initiator";
    public static String RESPONDER_ROLE_PROP_NAME = "role.responder";
    public static final String ECX_MESSAGE_ASIC_CONTENT_PROP_NAME = "ecx.asics";
    public static final String ECX_MESSAGE_XML_CONTENT_PROP_NAME = "ecx.xml";
    public static final String ECX_MESSAGE_TOKEN_XML_CONTENT_PROP_NAME = "ecx.tokenxml";
    public static final String PARTY_ID_PROP_NAME = "party-id";
    public static final String PARTY_ID_TYPE_PROP_NAME = "party-id-type";

    public static final String ECX_ADDRESS_PROP_NAME = "ecxaddress";
    public static final String MSG_INITIATOR_PREFIX = "initiator-addr."; //old gw-addr.
    public static final String MSG_RESPONDER_PREFIX = "responder-addr."; //backend-addr.

    public static final String CONNECTOR_MESSAGE_ID_PROP_NAME = "msg.connector-message-id";
    public static final String REF_TO_CONNECTOR_MESSAGE_ID_PROP_NAME = "msg.ref-to-connector-message-id";

    public static final String BACKEND_MESSAGE_XML_CONTENT_PROP_NAME = "backend.content.xml";
    public static final String BACKEND_MESSAGE_DOC_CONTENT_PROP_NAME = "backend.content.doc";
    public static final String BACKEND_MESSAGE_TRUST_TOKEN_PDF_PROP_NAME = "backend.content.token.pdf";
    public static final String BACKEND_MESSAGE_TRUST_TOKEN_XML_PROP_NAME = "backend.content.token.xml";
    public static final String BACKEND_MESSAGE_ATTACHMENTS_PREFIX = "backend.content.attachment";


    public static final String ATTACHMENT_NAME_POSTFIX = ".name";
    public static final String ATTACHMENT_IDENTIFIER_POSTFIX = ".identifier";
    public static final String ATTACHMENT_DIGEST_POSTFIX = ".digest";
    public static final String ATTACHMENT_FILENAME_POSTFIX = ".filename";

    private Resource messagePropertiesResource;
    private Resource messageFolder;
    private final LargeFilePersistenceService largeFilePersistenceService;

    private Properties messageProperties;
    private DC5MessageId id;


    public static void storeMessageTo(Resource resource, LargeFilePersistenceService largeFilePersistenceService, DC5Message message) throws IOException {
        LoadStoreMessageFromPath loadStoreMessageFromPath = new LoadStoreMessageFromPath(resource, largeFilePersistenceService);
        loadStoreMessageFromPath.storeMessage(message);
    }

    private LoadStoreMessageFromPath(Resource messageFolder, LargeFilePersistenceService largeFilePersistenceService) {
        this.largeFilePersistenceService = largeFilePersistenceService;
        this.messageFolder = messageFolder;
        this.messageProperties = new Properties();
    }


    private void storeMessage(DC5Message message) throws IOException {

        if (!messageFolder.exists()) {
            System.out.println("basic folder is: " + messageFolder.getFile().getAbsolutePath());
            messageFolder.getFile().mkdirs();
        }

        messagePropertiesResource = new FileSystemResource(messageFolder.getFile().toPath().resolve("message.properties"));
        if (messagePropertiesResource.exists()) {
            throw new RuntimeException("message already exists cannot overwrite it!");
        }

        storeMessageData(message);
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
        storeMessageConfirmations(Stream.of(message.getTransportedMessageConfirmation()).collect(Collectors.toList()));

        //store confirmations
        storeMessageStates(message.getMessageContent().getMessageStates());

        File file = messagePropertiesResource.getFile();
        System.out.println(file.getAbsolutePath());
        boolean created = file.createNewFile();
        if (!created) {
            throw new IllegalStateException("Cannot continue, file creation failed!");
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        messageProperties.store(fileOutputStream, null);

    }

    private void storeMessageStates(List<DC5BusinessMessageState> messageStates) {
        for (int i = 0; i < messageStates.size(); i++) {
            try {
                DC5BusinessMessageState state = messageStates.get(i);
                String statePrefix = String.format("%s.%s", LoadStoreTransitionMessage.MESSAGE_STATE_PREFIX, i);
                messageProperties.put(statePrefix + ".state-name", state.getState().name());

                if (state.getConfirmation() != null) {
                    DC5Confirmation confirmation = state.getConfirmation();
                    String evidenceFilePropertyName = String.format("%s.%s", statePrefix, "file");
                    String evidenceTypePropertyName = String.format("%s.%s", statePrefix, "type");
                    String fileName = getFileNameFromPrefix(evidenceFilePropertyName) + ".xml";
                    ;

                    messageProperties.put(evidenceTypePropertyName, confirmation.getEvidenceType().name());
                    messageProperties.put(evidenceFilePropertyName, fileName);

                    Resource r = messagePropertiesResource.createRelative(fileName);
                    writeByteArrayToResource(r, confirmation.getEvidence());
                }
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }

        }
    }


    private void storeMessageData(DC5Message message) {
        messageProperties.put(MESSAGE_TARGET_PROP_NAME, message.getTarget().name());
        messageProperties.put(MESSAGE_SOURCE_PROP_NAME, message.getSource().name());
        if (message.getConnectorMessageId() != null) {
            messageProperties.put(CONNECTOR_MESSAGE_ID_PROP_NAME, message.getConnectorMessageId().getConnectorMessageId());
        }
        if (message.getRefToConnectorMessageId() != null) {
            messageProperties.put(REF_TO_CONNECTOR_MESSAGE_ID_PROP_NAME, message.getRefToConnectorMessageId().getConnectorMessageId());
        }
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

                Resource r = messagePropertiesResource.createRelative(fileName);
                writeByteArrayToResource(r, confirmation.getEvidence());

            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }

        }
    }


    private void storeMessageAttachments(List<DC5MessageAttachment> attachments, String prefix) {
        if (attachments != null) {
            for (int i = 0; i < attachments.size(); i++) {
                DC5MessageAttachment a = attachments.get(i);
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


    public static DC5Message loadMessageFrom(Resource resource, LargeFilePersistenceService largeFilePersistenceService) throws IOException {
        LoadStoreMessageFromPath loadStoreMessageFromPath = new LoadStoreMessageFromPath(resource, largeFilePersistenceService);
        DC5Message message = loadStoreMessageFromPath.loadMessage();

        return message;
    }

    private DC5Message loadMessage() throws IOException {
        DC5Message.DC5MessageBuilder builder = DC5Message.builder();
        messagePropertiesResource = this.messageFolder;

        this.id = DC5MessageId.ofRandom();
        builder.connectorMessageId(id);

        Resource propertiesResource = messagePropertiesResource.createRelative("message.properties");
        if (!propertiesResource.exists()) {
            throw new IOException("properties " + propertiesResource + " does not exist!");
        }

        messageProperties.load(propertiesResource.getInputStream());

        loadMessageData(builder);


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

        List<DC5Confirmation> list = loadConfirmations();
        if (list.size() == 1) {
            builder.transportedMessageConfirmation(list.get(0));
        }

        String connid = messageProperties.getProperty("message.connector-id", null);
        if (connid != null) {
            builder.connectorMessageId(DC5MessageId.ofString(connid));
        }

        return builder.build();

    }

    private void loadMessageData(DC5Message.DC5MessageBuilder builder) {
        if (messageProperties.get(CONNECTOR_MESSAGE_ID_PROP_NAME) != null) {
            builder.connectorMessageId(DC5MessageId.ofString(messageProperties.get(CONNECTOR_MESSAGE_ID_PROP_NAME).toString()));
        }
        if (messageProperties.get(REF_TO_CONNECTOR_MESSAGE_ID_PROP_NAME) != null) {
            builder.refToConnectorMessageId(DC5MessageId.ofString(messageProperties.get(REF_TO_CONNECTOR_MESSAGE_ID_PROP_NAME).toString()));
        }
        if (messageProperties.get(MESSAGE_TARGET_PROP_NAME) != null) {
            builder.target(MessageTargetSource.valueOf(messageProperties.get(MESSAGE_TARGET_PROP_NAME).toString()));
        }
        if (messageProperties.get(MESSAGE_SOURCE_PROP_NAME) != null) {
            builder.source(MessageTargetSource.valueOf(messageProperties.get(MESSAGE_SOURCE_PROP_NAME).toString()));
        }
        if (messageProperties.get(MESSAGE_PROCESS_PROP_NAME) != null) {
            builder.process(DC5MsgProcess.builder()
                    .processId(MessageProcessId.ofString(messageProperties.get(MESSAGE_PROCESS_PROP_NAME).toString()))
                    .build());
        } else {
            builder.process(DC5MsgProcess.builder().processId(MessageProcessId.ofRandom()).build());
        }
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

        List<DC5MessageAttachment> DC5MessageAttachments = loadAttachments(BACKEND_MESSAGE_ATTACHMENTS_PREFIX);

        return builder.attachments(DC5MessageAttachments);

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

    private void storeIfNotNull(DC5MessageAttachment a, String prefix) {
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


    private List<DC5MessageAttachment> loadAttachments(String attachmentPrefix) {

        return messageProperties.stringPropertyNames()
                .stream()
                .sorted()
                .filter((k) -> k.startsWith(attachmentPrefix))
                .map((k) -> k.split("]")[0] + "]")
                .distinct()
                .map(this::loadResource)
                .map(DC5MessageAttachment.DC5MessageAttachmentBuilder::build)
                .collect(Collectors.toList());
    }

    private @Nullable Resource createRelativeResource(@Nullable String relativePath) {
        if (relativePath == null) {
            return null;
        }
        try {
            Resource contentResource = messagePropertiesResource.createRelative(relativePath);
            return contentResource;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }


    private LargeFileReference loadResourceAsBigDataRef(DC5MessageId id, Resource resource) {
        try {
            InputStream inputStream = resource.getInputStream();
            byte[] bytes = StreamUtils.copyToByteArray(inputStream);
            String hash = DigestUtils.md5DigestAsHex(bytes);
            Digest digest = Digest.builder()
                    .digestAlgorithm("md5")
                    .digestValue(hash)
                    .build();

            String filename = resource.getFilename();
            if (largeFilePersistenceService != null) {
                LargeFileReference domibusConnectorBigDataReference = largeFilePersistenceService.createDomibusConnectorBigDataReference(id, filename, "application/octet-stream");
                OutputStream os = domibusConnectorBigDataReference.getOutputStream();
                StreamUtils.copy(bytes, os);
                os.close();
                return domibusConnectorBigDataReference;
            } else {
                LargeFileReferenceGetSetBased inMemory = new LargeFileReferenceGetSetBased();
                inMemory.setBytes(bytes);
                inMemory.setReadable(true);
                inMemory.setStorageIdReference(UUID.randomUUID().toString());
                inMemory.setDigest(digest);
                return inMemory;
            }
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
    }


    @SneakyThrows
    private DC5MessageAttachment.DC5MessageAttachmentBuilder loadResource(String propertyPrefix) {
        String filename = messageProperties.getProperty(propertyPrefix + ATTACHMENT_FILENAME_POSTFIX);
        if (filename == null) {
            filename = getFileNameFromPrefix(propertyPrefix);
        }

        LargeFileReference largeFileReference = loadResourceAsBigDataRef(id, messagePropertiesResource.createRelative(filename));

        return DC5MessageAttachment.builder()
                .name(messageProperties.getProperty(propertyPrefix + ATTACHMENT_NAME_POSTFIX))
                .identifier(messageProperties.getProperty(propertyPrefix + ATTACHMENT_IDENTIFIER_POSTFIX))
                .digest(Digest.ofString(messageProperties.getProperty(propertyPrefix + ATTACHMENT_DIGEST_POSTFIX)))
                .attachment(largeFileReference);
    }

    private static String getFileNameFromPrefix(String propertyPrefix) {
        String filename = propertyPrefix.replaceAll("(\\[\\])", "_");
        return filename;
    }

    private void storeResource(DC5MessageAttachment a, String propertyPrefix) {
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
            file = messagePropertiesResource.createRelative(filename).getFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LargeFileReference readableDataSource = a.getAttachment();
        if (this.largeFilePersistenceService != null) {
            readableDataSource = largeFilePersistenceService.getReadableDataSource(readableDataSource);
        }

        try (OutputStream os = Files.newOutputStream(file.toPath()); InputStream is = readableDataSource.getInputStream()) {
            StreamUtils.copy(is, os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private void storeEbmsMessageDetails(DC5Ebms details) {

        if (details.getAction() != null && details.getAction().getAction() != null) {
            messageProperties.put(LoadStoreTransitionMessage.ACTION_PROP_NAME, details.getAction().getAction());
        }

        if (details.getInitiator() != null) {
            Map<String, String> senderProperties = getAddrProperties(details.getInitiator().getPartnerAddress());
            senderProperties.forEach((key, value) ->
                    messageProperties.put(MSG_INITIATOR_PREFIX + key, value));
        }
        if (details.getResponder() != null) {
            Map<String, String> senderProperties = getAddrProperties(details.getResponder().getPartnerAddress());
            senderProperties.forEach((key, value) ->
                    messageProperties.put(MSG_RESPONDER_PREFIX + key, value));
        }
        if (details.getResponder() != null && details.getResponder().getPartnerRole() != null) {
            messageProperties.put(RESPONDER_ROLE_PROP_NAME, details.getResponder().getPartnerRole().getRole());
        }
        if (details.getInitiator() != null && details.getInitiator().getPartnerRole() != null) {
            messageProperties.put(INITIATOR_ROLE_PROP_NAME, details.getInitiator().getPartnerRole().getRole());
        }

        if (details.getService() != null && details.getService() != null) {
            messageProperties.put(LoadStoreTransitionMessage.SERVICE_NAME_PROP_NAME, details.getService().getService());
            messageProperties.put(LoadStoreTransitionMessage.SERVICE_TYPE_PROP_NAME, details.getService().getServiceType());
        }

        if (details.getEbmsMessageId() != null) {
            messageProperties.put(LoadStoreTransitionMessage.EBMS_ID_PROP_NAME, details.getEbmsMessageId().getEbmsMesssageId());
        }
        if (details.getRefToEbmsMessageId() != null) {
            messageProperties.put(LoadStoreTransitionMessage.REF_TO_EBMS_ID_PROP_NAME, details.getRefToEbmsMessageId().getEbmsMesssageId());
        }
        if (details.getConversationId() != null) {
            messageProperties.put(LoadStoreTransitionMessage.CONVERSATION_ID_PROP_NAME, details.getConversationId());
        }

    }


    private Map<String, String> getAddrProperties(DC5EcxAddress addr) {
        Map<String, String> p = new HashMap<>();
        p.put(ECX_ADDRESS_PROP_NAME, addr.getEcxAddress());
        if (addr.getParty() != null) {
            p.put(PARTY_ID_PROP_NAME, addr.getParty().getPartyId());
            p.put(PARTY_ID_TYPE_PROP_NAME, addr.getParty().getPartyIdType());
        }
        return p;
    }


    private DC5Ebms.DC5EbmsBuilder loadEcxDatailsFromProperties() {
        DC5Ebms.DC5EbmsBuilder builder = DC5Ebms.builder();


        DC5Partner.DC5PartnerBuilder initiatorBuilder = DC5Partner.builder().partnerAddress(loadEcxAddress(MSG_INITIATOR_PREFIX));
        DC5Partner.DC5PartnerBuilder responderBuilder = DC5Partner.builder().partnerAddress(loadEcxAddress(MSG_RESPONDER_PREFIX));

        if (messageProperties.getProperty(INITIATOR_ROLE_PROP_NAME) != null) {
            initiatorBuilder.partnerRole(DC5Role.builder()
                            .role(messageProperties.getProperty(INITIATOR_ROLE_PROP_NAME).toString())
                            .roleType(DC5RoleType.INITIATOR)
                            .build());
        }
        builder.initiator(initiatorBuilder.build());
        if (messageProperties.getProperty(RESPONDER_ROLE_PROP_NAME) != null) {
            responderBuilder.partnerRole(DC5Role.builder()
                            .role(messageProperties.getProperty(RESPONDER_ROLE_PROP_NAME).toString())
                            .roleType(DC5RoleType.RESPONDER)
                            .build());
        }
        builder.responder(responderBuilder.build());
        builder.action(DC5Action.builder()
                .action(messageProperties.getProperty(LoadStoreTransitionMessage.ACTION_PROP_NAME))
                .build());
        builder.service(DC5Service.builder()
                .serviceType(messageProperties.getProperty(LoadStoreTransitionMessage.SERVICE_TYPE_PROP_NAME))
                .service(messageProperties.getProperty(LoadStoreTransitionMessage.SERVICE_NAME_PROP_NAME))
                .build());
        if (messageProperties.getProperty(LoadStoreTransitionMessage.EBMS_ID_PROP_NAME) != null) {
            builder.ebmsMessageId(EbmsMessageId.ofString(messageProperties.getProperty(LoadStoreTransitionMessage.EBMS_ID_PROP_NAME)));
        }
        if (messageProperties.getProperty(LoadStoreTransitionMessage.REF_TO_EBMS_ID_PROP_NAME) != null) {
            builder.refToEbmsMessageId(EbmsMessageId.ofString(messageProperties.getProperty(LoadStoreTransitionMessage.REF_TO_EBMS_ID_PROP_NAME)));
        }
        builder.refToEbmsMessageId(EbmsMessageId.ofString(messageProperties.getProperty(LoadStoreTransitionMessage.CONVERSATION_ID_PROP_NAME)));

        return builder;
    }

    private DC5BackendData.DC5BackendDataBuilder loadBackendDetailsFromProperties() {
        DC5BackendData.DC5BackendDataBuilder builder = DC5BackendData.builder();
        if (messageProperties.getProperty(LoadStoreTransitionMessage.BACKEND_MESSAGE_ID_PROP_NAME) != null) {
            builder.backendMessageId(BackendMessageId.ofString(messageProperties.getProperty(LoadStoreTransitionMessage.BACKEND_MESSAGE_ID_PROP_NAME)));
        }
        if (messageProperties.getProperty(LoadStoreTransitionMessage.BACKEND_REF_BACKEND_MESSAGE_ID_PROP_NAME) != null) {
            builder.refToBackendMessageId(BackendMessageId.ofString(messageProperties.getProperty(LoadStoreTransitionMessage.BACKEND_REF_BACKEND_MESSAGE_ID_PROP_NAME)));
        }
        return builder;
    }


    private DC5EcxAddress loadEcxAddress(String senderPrefix) {

        return DC5EcxAddress.builder()
                .ecxAddress(messageProperties.getProperty(senderPrefix + ECX_ADDRESS_PROP_NAME))
                .party(DC5Party.builder()
                        .partyId(messageProperties.getProperty(senderPrefix + PARTY_ID_PROP_NAME))
                        .partyIdType(messageProperties.getProperty(senderPrefix + PARTY_ID_TYPE_PROP_NAME))
                        .build()
                )
                .build();

    }


}
