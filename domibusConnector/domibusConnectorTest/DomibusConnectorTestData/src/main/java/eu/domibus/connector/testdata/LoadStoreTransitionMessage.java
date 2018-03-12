package eu.domibus.connector.testdata;


import eu.domibus.connector.domain.transition.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import javax.activation.DataHandler;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class LoadStoreTransitionMessage {

    public static final String SERVICE_PROP_NAME = "service";
    public static final String NATIONAL_ID_PROP_NAME = "message.nation-id";
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


    private static final Logger LOGGER = LoggerFactory.getLogger(LoadStoreTransitionMessage.class);
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

    public void storeMessageTo(String path) {
        //TODO: implement!
    }

    private LoadStoreTransitionMessage() {
    }

    private LoadStoreTransitionMessage(Resource basicFolder) {
        this.basicFolder = basicFolder;
    }

    private DomibusConnectorMessageType loadMessage() throws IOException {
        DomibusConnectorMessageType message = new DomibusConnectorMessageType();

        Resource propertiesResource = basicFolder.createRelative("message.properties");
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
                String signatureFileName = messageProperties.getProperty("MESSAGE_DOCUMENT_SIGNATURE_FILE_PROP_NAME");
                if (signatureFileName != null) {
                    DomibusConnectorDetachedSignatureType detachedSignature = new DomibusConnectorDetachedSignatureType();


                    String mimeTypeString = messageProperties.getProperty("MESSAGE_DOCUMENT_SIGNATURE_TYPE_PROP_NAME");
                    DomibusConnectorDetachedSignatureMimeType detachedSignatureMimeType =
                            DomibusConnectorDetachedSignatureMimeType.fromValue(mimeTypeString);


//                    DetachedSignatureMimeType mimeType = DetachedSignatureMimeType.valueOf(messageProperties.getProperty("MESSAGE_DOCUMENT_SIGNATURE_TYPE_PROP_NAME"));
                    String name = messageProperties.getProperty("MESSAGE_DOCUMENT_SIGNATURE_NAME_PROP_NAME");
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
                .filter( (k) -> k.startsWith(LoadStoreTransitionMessage.MESSAGE_CONFIRMATIONS_PREFIX))
                .map( (k) -> k.split("\\.")[2])
                .distinct()
                .map( (k) -> {


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
                .filter( (k) -> k.startsWith(LoadStoreTransitionMessage.MESSAGE_ATTACHMENT_PREFIX) )
                .map( (k) -> k.split("\\.")[2])
                .distinct()
                .map( (k) -> {
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
            byte[] bytes = StreamUtils.copyToByteArray(res.getInputStream());
            
            DataHandler dh = new DataHandler(bytes, "application/octet-stream");
            
            return dh;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
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

    private byte[] loadResourceAsByteArray(Resource res) {
        try {
            InputStream inputStream = res.getInputStream();
            return StreamUtils.copyToByteArray(inputStream);
        } catch(IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private DomibusConnectorMessageDetailsType loadMessageDetails() {
        DomibusConnectorMessageDetailsType messageDetails = new DomibusConnectorMessageDetailsType();

        DomibusConnectorActionType domibusConnectorActionType = new DomibusConnectorActionType();
        domibusConnectorActionType.setAction(messageProperties.getProperty("action"));
        messageDetails.setAction(domibusConnectorActionType);

        DomibusConnectorPartyType fromParty = new DomibusConnectorPartyType();
        fromParty.setPartyId(messageProperties.getProperty("from.party.id"));
        fromParty.setRole(messageProperties.getProperty("from.party.role"));
        messageDetails.setFromParty(fromParty);


        DomibusConnectorPartyType toParty = new DomibusConnectorPartyType();
        toParty.setPartyId(messageProperties.getProperty("to.party.id"));
        toParty.setRole(messageProperties.getProperty("to.party.role"));
        messageDetails.setToParty(toParty);


        DomibusConnectorServiceType service = new DomibusConnectorServiceType();
        service.setService(messageProperties.getProperty("service"));
        messageDetails.setService(service);

        messageDetails.setConversationId(messageProperties.getProperty("message.conversation-id"));

        messageDetails.setEbmsMessageId(messageProperties.getProperty("message.ebms-id"));

        messageDetails.setBackendMessageId(messageProperties.getProperty("message.national-id"));

        return messageDetails;
    }






}
