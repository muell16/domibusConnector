package eu.domibus.connector.testdata;


import eu.domibus.connector.domain.model.DetachedSignatureMimeType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageContent;
import eu.domibus.connector.domain.transition.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class LoadStoreTransitionMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadStoreTransitionMessage.class);

    public static final String MESSAGE_PROPERTIES_PROPERTY_FILE_NAME = "message.properties";
    public static final String SERVICE_PROP_NAME = "service";
    public static final String NATIONAL_ID_PROP_NAME = "message.national-id";
    public static final String EBMS_ID_PROP_NAME = "message.ebms-id";
    public static final String BACKEND_CLIENT_NAME_PROP_NAME = "message.backend-client-name";
    public static final String MESSAGE_CONTENT_XML_PROP_NAME = "message.content.xml";
    public static final String DEFAULT_CONTENT_XML_FILE_NAME = "content.xml";
    public static final String MESSAGE_DOCUMENT_FILE_PROP_NAME = "message.content.document.file";
    public static final String MESSAGE_DOCUMENT_NAME_PROP_NAME = "message.content.document.name";
    public static final String MESSAGE_DOCUMENT_HASH_PROP_NAME = "message.content.document.hash";
    public static final String MESSAGE_DOCUMENT_SIGNATURE_FILE_PROP_NAME = "message.content.document.signature.file";
    public static final String MESSAGE_DOCUMENT_SIGNATURE_TYPE_PROP_NAME = "message.content.document.signature.type";
    public static final String MESSAGE_DOCUMENT_SIGNATURE_NAME_PROP_NAME = "message.content.document.signature.name";
    public static final String FROM_PARTY_ID_PROP_NAME = "from.party.id";
    public static final String FROM_PARTY_ROLE_PROP_NAME = "from.party.role";
    public static final String TO_PARTY_ID_PROP_NAME = "to.party.id";
    public static final String TO_PARTY_ROLE_PROP_NAME = "to.party.role";
    public static final String ACTION_PROP_NAME = "action";
    public static final String CONVERSATION_ID_PROP_NAME = "message.conversation-id";
    public static final String FINAL_RECIPIENT_PROP_NAME = "message.final-recipient";
    public static final String ORIGINAL_SENDER_NAME_PROP_NAME = "message.original-sender";

    public static String MESSAGE_CONFIRMATIONS_PREFIX = "message.confirmation";
    public static String MESSAGE_ATTACHMENT_PREFIX = "message.attachment";

    private Resource basicFolder;

    private Properties messageProperties;

    public static DomibusConnectorMessageType loadMessageFrom(Resource path) {
        try {
            LoadStoreTransitionMessage load = new LoadStoreTransitionMessage(path);
            return load.loadMessage();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }


    public static void storeMessageTo(Resource path, DomibusConnectorMessageType message, boolean overwrite) {
        try {
            LoadStoreTransitionMessage store = new LoadStoreTransitionMessage(path);
            store.storeMessageTo(message, overwrite);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public void storeMessageTo(DomibusConnectorMessageType message, boolean overwrite) throws IOException {
        LOGGER.debug("storeMessage to [{}] with overwrite [{}]", basicFolder, overwrite);
        if (basicFolder.exists() && !overwrite) {
            throw new RuntimeException(String.format("Overwrite is false, cannot overwrite message in folder %s", basicFolder));
        }

        Resource propertiesResource = basicFolder.createRelative(MESSAGE_PROPERTIES_PROPERTY_FILE_NAME);
        File f = propertiesResource.getFile();
        if (!f.exists()) {
            f.createNewFile();
        }

        storeMessageContent(message.getMessageContent());

        storeMessageConfirmations(message.getMessageConfirmations());

        storeMessageAttachments(message.getMessageAttachments());

        try (FileOutputStream fout = new FileOutputStream(f)) {
            messageProperties.store(fout, "");
        } catch (IOException ioe) {

        }

    }

    private void storeMessageAttachments(List<DomibusConnectorMessageAttachmentType> messageAttachments) {
        for (int i = 0; i < messageAttachments.size(); i++) {
            try {
                DomibusConnectorMessageAttachmentType a = messageAttachments.get(i);

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

    private void storeMessageConfirmations(List<DomibusConnectorMessageConfirmationType> messageConfirmations) {
        for (int i = 0; i < messageConfirmations.size(); i++) {
            try {
                DomibusConnectorMessageConfirmationType confirmation = messageConfirmations.get(i);

                String evidenceFilePropertyName = String.format("%s.%s.%s", LoadStoreTransitionMessage.MESSAGE_CONFIRMATIONS_PREFIX, i, "file");
                String evidenceTypePropertyName = String.format("%s.%s.%s", LoadStoreTransitionMessage.MESSAGE_CONFIRMATIONS_PREFIX, i, "type");

                messageProperties.put(evidenceTypePropertyName, confirmation.getConfirmationType().name());

                String fileName = confirmation.getConfirmationType().name() + ".xml";
                messageProperties.put(evidenceFilePropertyName, confirmation.getConfirmationType().name() + ".xml");

                Resource r = basicFolder.createRelative(fileName);
                writeXmlSourceToResource(r, confirmation.getConfirmation());

            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
    }

    private void storeMessageContent(DomibusConnectorMessageContentType content) throws IOException {
        if (content == null) {
            return;
        }
        Source xmlContent = content.getXmlContent();
        Resource r = basicFolder.createRelative(DEFAULT_CONTENT_XML_FILE_NAME);
        messageProperties.put(LoadStoreTransitionMessage.MESSAGE_CONTENT_XML_PROP_NAME, LoadStoreTransitionMessage.DEFAULT_CONTENT_XML_FILE_NAME);

        writeXmlSourceToResource(r, xmlContent);

        //store message document
        DomibusConnectorMessageDocumentType messageDocument = content.getDocument();
        if (messageDocument != null) {
            messageDocument.getDocument();
            String fileName = messageDocument.getDocumentName() == null ? "document.pdf" : messageDocument.getDocumentName();
            Resource d = basicFolder.createRelative(fileName);
            writeBigDataReferenceToResource(d, messageDocument.getDocument());

            messageProperties.put(LoadStoreTransitionMessage.MESSAGE_DOCUMENT_FILE_PROP_NAME, fileName);

            if (messageDocument.getDocumentName() != null) {
                messageProperties.put(LoadStoreTransitionMessage.MESSAGE_DOCUMENT_NAME_PROP_NAME, messageDocument.getDocumentName());
            }

            DomibusConnectorDetachedSignatureType detachedSignature = messageDocument.getDetachedSignature();
            if (detachedSignature != null) {
                byte[] detachedSignatureBytes = detachedSignature.getDetachedSignature();
                DomibusConnectorDetachedSignatureMimeType detachedSignatureMimeType = detachedSignature.getMimeType();
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

    private void writeByteArrayToResource(Resource res, byte[] bytes) {
        try (FileOutputStream fout = new FileOutputStream(res.getFile())) {
            StreamUtils.copy(bytes, fout);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeBigDataReferenceToResource(Resource d, DataHandler document) {
        try (FileOutputStream fout = new FileOutputStream(d.getFile())) {
            StreamUtils.copy(document.getInputStream(), fout);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeXmlSourceToResource(Resource r, Source xmlContent) throws IOException {
        File f = r.getFile();
        try ( FileOutputStream fout = new FileOutputStream(f)) {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamResult xmlOutput = new StreamResult(new OutputStreamWriter(fout));
            transformer.transform(xmlContent, xmlOutput);
        } catch (TransformerConfigurationException e) {
            LOGGER.error("Exception occured", e);
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }


    private LoadStoreTransitionMessage() {
    }

    private LoadStoreTransitionMessage(Resource basicFolder) {
        messageProperties = new Properties();
        this.basicFolder = basicFolder;
    }

    private DomibusConnectorMessageType loadMessage() throws IOException {
        DomibusConnectorMessageType message = new DomibusConnectorMessageType();

        Resource propertiesResource = basicFolder.createRelative(MESSAGE_PROPERTIES_PROPERTY_FILE_NAME);
        if (!propertiesResource.exists()) {
            throw new IOException("properties " + propertiesResource + " does not exist!");
        }

        messageProperties.load(propertiesResource.getInputStream());


        message.setMessageDetails(loadMessageDetails());


        Resource contentResource = createRelativeResource(messageProperties.getProperty(MESSAGE_CONTENT_XML_PROP_NAME));
        if (contentResource != null && contentResource.exists()) {
            DomibusConnectorMessageContentType content = new DomibusConnectorMessageContentType();
            content.setXmlContent(loadResourceAsSource(contentResource));

            message.setMessageContent(content);
            //load document
            String docFileName = messageProperties.getProperty(MESSAGE_DOCUMENT_FILE_PROP_NAME);
            if (docFileName != null) {
                DomibusConnectorMessageDocumentType messageDoc = new DomibusConnectorMessageDocumentType();
                Resource r = basicFolder.createRelative(docFileName);

                messageDoc.setDocument(loadResourceAsDataHandler(r));
                String docName = messageProperties.getProperty(MESSAGE_DOCUMENT_NAME_PROP_NAME);
                messageDoc.setDocumentName(docName);

                //TODO: load signature
                String signatureFileName = messageProperties.getProperty(MESSAGE_DOCUMENT_SIGNATURE_FILE_PROP_NAME);
                if (signatureFileName != null) {
                    DomibusConnectorDetachedSignatureType detachedSignature = new DomibusConnectorDetachedSignatureType();


                    String mimeTypeString = messageProperties.getProperty(MESSAGE_DOCUMENT_SIGNATURE_TYPE_PROP_NAME);
                    DomibusConnectorDetachedSignatureMimeType detachedSignatureMimeType =
                            DomibusConnectorDetachedSignatureMimeType.fromValue(mimeTypeString);


//                    DetachedSignatureMimeType mimeType = DetachedSignatureMimeType.valueOf(messageProperties.getProperty("MESSAGE_DOCUMENT_SIGNATURE_TYPE_PROP_NAME"));
                    String name = messageProperties.getProperty(MESSAGE_DOCUMENT_SIGNATURE_NAME_PROP_NAME);
                    Resource fResource = basicFolder.createRelative(signatureFileName);
                    byte[] signatureBytes = loadResourceAsByteArray(fResource);

                    detachedSignature.setDetachedSignature(signatureBytes);
                    detachedSignature.setDetachedSignatureName(name);
                    detachedSignature.setMimeType(detachedSignatureMimeType);

                    messageDoc.setDetachedSignature(detachedSignature);
                }

                content.setDocument(messageDoc);

            }

        }


        //load attachments
        message.getMessageAttachments().addAll(loadMessageAttachments());

        //load confirmations
        message.getMessageConfirmations().addAll(loadMessageConfirmations());

        return message;
    }

    private List<? extends DomibusConnectorMessageConfirmationType> loadMessageConfirmations() {
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

                    //TODO: determine evidence type!
                    //DomibusConnectorMessageC domibusConnectorEvidenceType = DomibusConnectorEvidenceType.valueOf(messageProperties.getProperty(evidenceTypePropertyName));

                    DomibusConnectorConfirmationType domibusConnectorConfirmationType = DomibusConnectorConfirmationType.fromValue(messageProperties.getProperty(evidenceTypePropertyName));

                    DomibusConnectorMessageConfirmationType confirmation = new DomibusConnectorMessageConfirmationType();
                    confirmation.setConfirmation(loadResourceAsSource(resEvidenceFile));
                    confirmation.setConfirmationType(domibusConnectorConfirmationType);
                    //confirmation.setEvidenceType(domibusConnectorEvidenceType);
                    //confirmation.setConfirmationType(DomibusConnectorConfirmationType.valueOf());

                    return confirmation;

                })
                .collect(Collectors.toList());
    }

    private Source loadResourceAsSource(Resource resEvidenceFile) {
        try {
            return new StreamSource(resEvidenceFile.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DomibusConnectorMessageAttachmentType> loadMessageAttachments() {


        return messageProperties.stringPropertyNames()
                .stream()
                .sorted()
                .filter((k) -> k.startsWith(LoadStoreTransitionMessage.MESSAGE_ATTACHMENT_PREFIX))
                .map((k) -> k.split("\\.")[2])
                .distinct()
                .map((k) -> {
                    try {
                        DomibusConnectorMessageAttachmentType attachment = new DomibusConnectorMessageAttachmentType();
                        String filePropertyName = String.format("%s.%s.%s", LoadStoreTransitionMessage.MESSAGE_ATTACHMENT_PREFIX, k, "file");
                        String identifierPropertyName = String.format("%s.%s.%s", LoadStoreTransitionMessage.MESSAGE_ATTACHMENT_PREFIX, k, "identifier");
                        Resource res = basicFolder.createRelative(messageProperties.getProperty(filePropertyName));
                        attachment.setAttachment(loadResourceAsDataHandler(res));
                        attachment.setIdentifier(messageProperties.getProperty(identifierPropertyName));
                        return attachment;

                    } catch (IOException ioe) {
                        throw new RuntimeException(ioe);
                    }
                })
                .collect(Collectors.toList());

    }

    private DataHandler loadResourceAsDataHandler(Resource res) {
        try {
            DataHandler dh = new DataHandler(new InputStreamDataSource(res.getInputStream()));
            return dh;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private @Nullable
    Resource createRelativeResource(@Nullable String relativePath) {
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

    private byte[] loadResourceAsByteArray(Resource res) {
        try {
            InputStream inputStream = res.getInputStream();
            return StreamUtils.copyToByteArray(inputStream);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private void saveMessageDetails(DomibusConnectorMessageDetailsType messageDetails) {

        DomibusConnectorActionType action = messageDetails.getAction();
        if (action != null) {
            setPropertyOrEmptyStringWhenNull(ACTION_PROP_NAME, action.getAction());
        } else {
            setPropertyOrEmptyStringWhenNull(ACTION_PROP_NAME, "");
        }

        DomibusConnectorPartyType fromParty = messageDetails.getFromParty();
        if (fromParty != null) {
            setPropertyOrEmptyStringWhenNull(FROM_PARTY_ID_PROP_NAME, fromParty.getPartyId());
            setPropertyOrEmptyStringWhenNull(FROM_PARTY_ROLE_PROP_NAME, fromParty.getRole());
        }

        DomibusConnectorPartyType toParty = messageDetails.getToParty();
        if (toParty != null) {
            setPropertyOrEmptyStringWhenNull(TO_PARTY_ID_PROP_NAME, toParty.getPartyId());
            setPropertyOrEmptyStringWhenNull(TO_PARTY_ROLE_PROP_NAME, toParty.getRole());
        }

        DomibusConnectorServiceType service = messageDetails.getService();
        if (service != null) {
            setPropertyOrEmptyStringWhenNull(SERVICE_PROP_NAME, service.getService());
        } else {
            setPropertyOrEmptyStringWhenNull(SERVICE_PROP_NAME, "");
        }

        setPropertyOrEmptyStringWhenNull(CONVERSATION_ID_PROP_NAME, messageDetails.getConversationId());
        setPropertyOrEmptyStringWhenNull(EBMS_ID_PROP_NAME, messageDetails.getEbmsMessageId());
        setPropertyOrEmptyStringWhenNull(NATIONAL_ID_PROP_NAME, messageDetails.getBackendMessageId());
        setPropertyOrEmptyStringWhenNull(ORIGINAL_SENDER_NAME_PROP_NAME, messageDetails.getOriginalSender());
        setPropertyOrEmptyStringWhenNull(FINAL_RECIPIENT_PROP_NAME, messageDetails.getFinalRecipient());

    }

    private void setPropertyOrEmptyStringWhenNull(String key, String value) {
        if (value == null) {
            messageProperties.setProperty(key, "");
        } else {
            messageProperties.setProperty(key, value);
        }
    }


    private DomibusConnectorMessageDetailsType loadMessageDetails() {
        DomibusConnectorMessageDetailsType messageDetails = new DomibusConnectorMessageDetailsType();

        DomibusConnectorActionType domibusConnectorActionType = new DomibusConnectorActionType();
        domibusConnectorActionType.setAction(messageProperties.getProperty(ACTION_PROP_NAME));
        messageDetails.setAction(domibusConnectorActionType);

        DomibusConnectorPartyType fromParty = new DomibusConnectorPartyType();
        fromParty.setPartyId(messageProperties.getProperty(FROM_PARTY_ID_PROP_NAME));
        fromParty.setRole(messageProperties.getProperty(FROM_PARTY_ROLE_PROP_NAME));
        messageDetails.setFromParty(fromParty);

        DomibusConnectorPartyType toParty = new DomibusConnectorPartyType();
        toParty.setPartyId(messageProperties.getProperty(TO_PARTY_ID_PROP_NAME));
        toParty.setRole(messageProperties.getProperty(TO_PARTY_ROLE_PROP_NAME));
        messageDetails.setToParty(toParty);

        DomibusConnectorServiceType service = new DomibusConnectorServiceType();
        service.setService(messageProperties.getProperty(SERVICE_PROP_NAME));
        messageDetails.setService(service);

        messageDetails.setConversationId(messageProperties.getProperty(CONVERSATION_ID_PROP_NAME));

        messageDetails.setEbmsMessageId(messageProperties.getProperty(EBMS_ID_PROP_NAME));

        messageDetails.setBackendMessageId(messageProperties.getProperty(NATIONAL_ID_PROP_NAME));

        messageDetails.setOriginalSender(messageProperties.getProperty(ORIGINAL_SENDER_NAME_PROP_NAME));
        messageDetails.setFinalRecipient(messageProperties.getProperty(FINAL_RECIPIENT_PROP_NAME));

        return messageDetails;
    }


    public static class InputStreamDataSource implements DataSource {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        public InputStreamDataSource(InputStream inputStream) {
            try {
                int nRead;
                StreamUtils.copy(inputStream, buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String getContentType() {
            //return new MimetypesFileTypeMap().getContentType(name);
            return "application/octet-stream";
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(buffer.toByteArray());
        }

        @Override
        public String getName() {
            return "";
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Read-only data");
        }

    }


}
