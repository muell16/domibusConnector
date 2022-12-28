package eu.domibus.connector.security;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import eu.domibus.connector.common.annotations.BusinessDomainScoped;
import eu.domibus.connector.persistence.service.LargeFilePersistenceService;
import eu.domibus.connector.security.container.service.ECodexContainerFactoryService;
import eu.domibus.connector.tools.logging.LoggingMarker;
import eu.ecodex.dc5.message.model.*;
import eu.ecodex.dss.model.token.LegalTrustLevel;
import eu.ecodex.dss.model.token.Token;
import eu.ecodex.dss.service.ECodexContainerService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import eu.domibus.connector.domain.model.LargeFileReference;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;
import eu.ecodex.dss.model.BusinessContent;
import eu.ecodex.dss.model.ECodexContainer;
import eu.ecodex.dss.model.checks.CheckProblem;
import eu.ecodex.dss.model.checks.CheckResult;
import eu.ecodex.dss.service.ECodexException;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.MimeType;

import java.io.OutputStream;
import java.util.Objects;
import javax.annotation.Nonnull;

import org.springframework.util.StreamUtils;

/**
 * TODO: documentation needed
 */
@Component
@BusinessDomainScoped
public class DomibusSecurityToolkitImpl implements DomibusConnectorSecurityToolkit {

    public static final String RED_TOKEN_WARNING_MESSAGE = "A RedToken was generated!";

    public static final String MAIN_DOCUMENT_NAME = "mainDocument";

    public static final String ASICS_CONTAINER_IDENTIFIER = "ASIC-S";

    public static final String TOKEN_XML_IDENTIFIER = "tokenXML";

    public static final String TOKEN_PDF_IDENTIFIER = "tokenPDF";

    public static final String CONTENT_PDF_IDENTIFIER = "ContentPDF";

    public static final String CONTENT_XML_IDENTIFIER = "ContentXML";

    private static final String TOKEN_XML_FILE_NAME = "Token.xml";

    private static final String TOKEN_PDF_FILE_NAME = "Token.pdf";

    private static final String DETACHED_SIGNATURE_DOCUMENT_NAME = "detachedSignature";

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusSecurityToolkitImpl.class);

    private final ECodexContainerFactoryService eCodexContainerFactoryService;
    private final LargeFilePersistenceService bigDataPersistenceService;
//    private final TokenIssuerFactory tokenIssuerFactory;

    public DomibusSecurityToolkitImpl(ECodexContainerFactoryService eCodexContainerFactoryService,
                                      LargeFilePersistenceService bigDataPersistenceService) {
        this.eCodexContainerFactoryService = eCodexContainerFactoryService;
        this.bigDataPersistenceService = bigDataPersistenceService;
//        this.tokenIssuerFactory = tokenIssuerFactory;
    }

    @Override
    public DC5Message validateContainer(DC5Message message) throws DomibusConnectorSecurityException {
        recieveContainerContents(message);
        return message;
    }

    @Override
    public DC5Message buildContainer(DC5Message message) throws DomibusConnectorSecurityException {
        return this.createContainer(message);
    }

    BusinessContent buildBusinessContent(@Nonnull DC5Message message) {
        if (message.getMessageContent() == null) {
            throw new IllegalArgumentException("messageContent is null!");
        }
        @Nonnull DC5MessageContent messageContent = message.getMessageContent();

        BusinessContent businessContent = new BusinessContent();

        DSSDocument dssDocument = null;


        if (message.getMessageContent().getBusinessContent().getBusinessDocument() != null) {
            //
//            DomibusConnectorMessageDocument document = messageContent.getDocument();
            DC5MessageAttachment businessPdf = message.getMessageContent().getBusinessContent().getBusinessDocument();
            //TODO add MimeType to Document!
            //we are still assuming that the bussiness document is always a pdf!
            String pdfName = StringUtils.isEmpty(businessPdf.getName()) ? MAIN_DOCUMENT_NAME
                    + ".pdf"
                    : businessPdf.getName();
            dssDocument = createLargeFileBasedDssDocument(businessPdf, pdfName, MimeType.PDF);


            // no business document - make xml to main document
        } else if (message.getMessageContent().getBusinessContent().getBusinessXml() != null) {
//            byte[] content = message.getMessageContent().getBusinessContent().getBusinessXml();
//            dssDocument = new InMemoryDocument(content, MAIN_DOCUMENT_NAME + ".xml", MimeType.XML);
            DC5MessageAttachment businessXml = message.getMessageContent().getBusinessContent().getBusinessXml();
            dssDocument = createLargeFileBasedDssDocument(businessXml, MAIN_DOCUMENT_NAME + ".xml", MimeType.XML);
        } else {
            LOGGER.error("No content found for container!");
            throw new RuntimeException("not valid without document!");
        }

        businessContent.setDocument(dssDocument);

//        DomibusConnectorMessageDocument msgDocument = messageContent.getDocument();

//        if (msgDocument != null &&
//                msgDocument.getDetachedSignature() != null
//                && msgDocument.getDetachedSignature().getMimeType() != null) {
//
//            String detachedSignatureName = msgDocument.getDetachedSignature().getDetachedSignatureName() != null ? msgDocument
//                    .getDetachedSignature().getDetachedSignatureName() : DETACHED_SIGNATURE_DOCUMENT_NAME;
//
//
//            DSSDocument detachedSignature = new InMemoryDocument(
//                    msgDocument.getDetachedSignature().getDetachedSignature(),
//                    detachedSignatureName,
//                    MimeType.fromMimeTypeString(msgDocument.getDetachedSignature().getMimeType().getCode()));
//            businessContent.setDetachedSignature(detachedSignature);
//        }

        for (DC5MessageAttachment attachment : message.getMessageContent().getBusinessContent().getAttachments()) {


            MimeType mimeType = MimeType.fromMimeTypeString(attachment.getMimeType());
            LOGGER.debug("buildBusinessContent: detected mimeType [{}] in attachment [{}]", mimeType.getMimeTypeString(), attachment);

            DSSDocument dssInMemoryDoc = createLargeFileBasedDssDocument(attachment, attachment.getName(), mimeType);

            businessContent.addAttachment(dssInMemoryDoc);
        }

        return businessContent;
    }


    /**
     * Takes the messageContent (xmlDocument + document)
     * and all messageAttachments and wraps them into a asic container
     * the generated token xml and the asic container
     * are attached as messageAttachments again
     * all other attachments are removed from the message!
     * <p>
     * the messageContent of the message must not be null!
     *
     * @param message the message to process
     * @return - the processed message (same object as passed by param message)
     */
    public DC5Message createContainer(@Nonnull DC5Message message) {

        ECodexContainerService containerService = eCodexContainerFactoryService.createECodexContainerService(message);

        try {
            LOGGER.trace("createContainer: for message [{}]", message);
            BusinessContent businessContent = buildBusinessContent(message);

//            message.getMessageAttachments().clear();

            ECodexContainer container = containerService.create(businessContent);


            Token token = container.getToken();
            if (LegalTrustLevel.NOT_SUCCESSFUL.equals(token.getValidation().getLegalResult().getTrustLevel())) {
                LOGGER.warn(LoggingMarker.BUSINESS_CERT_LOG, "a RedToken was generated!");
            }


            // KlarA: Added check of the container and the respective
            // error-handling
            CheckResult results = containerService.check(container);


            if (results.isSuccessful()) {
                DC5EcodexContent.DC5EcodexContentBuilder eCodexContentBuilder = DC5EcodexContent.builder();

                DSSDocument asicDocument = container.getAsicDocument();
                if (asicDocument != null) {
                    LOGGER.trace("converting asicDocument [{}] to asic message attachment and appending it to message", asicDocument);
                    DC5MessageAttachment asicAttachment = convertDocumentToMessageAttachment(message, asicDocument, ASICS_CONTAINER_IDENTIFIER);
                    eCodexContentBuilder.asicContainer(asicAttachment);

                }
                DSSDocument tokenXML = container.getTokenXML();

                if (tokenXML != null) {
                    LOGGER.trace("converting tokenXml {[{}] to message attachment and appending it to message", tokenXML);
                    tokenXML.setName(TOKEN_XML_FILE_NAME);
                    tokenXML.setMimeType(MimeType.XML);
                    DC5MessageAttachment tokenAttachment = convertDocumentToMessageAttachment(message, tokenXML, TOKEN_XML_IDENTIFIER);
                    eCodexContentBuilder.trustTokenXml(tokenAttachment);
                }
                message.getMessageContent().setEcodexContent(eCodexContentBuilder.build());
                return message;
            } else {
                String errormessage = "\nSeveral problems prevented the container from being created:";
                List<CheckProblem> problems = results.getProblems();
                for (CheckProblem curProblem : problems) {
                    errormessage += "\n-" + curProblem.getMessage();
                }
                throw new DomibusConnectorSecurityException(errormessage);
            }
        } catch (ECodexException e) {
            throw new DomibusConnectorSecurityException("ECodex exception occured while creating container", e);
        } catch (IOException ioe) {
            throw new DomibusConnectorSecurityException("IOException occured while creating container", ioe);
        }
    }

    /**
     * Unpacks the asic container from the message
     * and puts the businessDocument, xmlContent into MessageContent
     * and other attachments are added to the messageAttachments
     * the asicAttachment and xmlToken attachment are removed from the message
     *
     * @param message - the message to process
     */
    public void recieveContainerContents(DC5Message message) {
        ECodexContainerService containerService = eCodexContainerFactoryService.createECodexContainerService(message);

        try {
            Objects.requireNonNull(message.getMessageContent(), "Message content cannot be null!");
            Objects.requireNonNull(message.getMessageContent().getEcodexContent(), "Message must have a eCodexContent!");
            Objects.requireNonNull(message.getMessageContent().getEcodexContent().getAsicContainer(), "Message must have a ASIC-S container");
            Objects.requireNonNull(message.getMessageContent().getEcodexContent().getTrustTokenXml(), "Message must have a TrustToken XML");
            Objects.requireNonNull(message.getMessageContent().getEcodexContent().getBusinessXml(), "Message must have a Business XML");
        } catch (NullPointerException npe) {
            throw new DomibusConnectorSecurityException("The provided message is incomplete!", npe);
        }

        DC5MessageAttachment asicsAttachment = message.getMessageContent().getEcodexContent().getAsicContainer();
        DC5MessageAttachment tokenXMLAttachment = message.getMessageContent().getEcodexContent().getTrustTokenXml();

        try (
                InputStream asicInputStream = getAsicsContainerInputStream(asicsAttachment);
                InputStream tokenStream = getTokenXmlStream(tokenXMLAttachment);
        ) {
            ECodexContainer container = containerService.receive(asicInputStream, tokenStream);

            // KlarA: Added check of the container and the respective
            // error-handling
            CheckResult results = containerService.check(container);

            DC5BackendContent.DC5BackendContentBuilder backendContentBuilder = DC5BackendContent.builder();

            if (!results.isSuccessful()) {
                StringBuilder errorStringBuilder = new StringBuilder();
                errorStringBuilder.append("\nSeveral problems prevented the container from being created:");
                List<CheckProblem> problems = results.getProblems();
                for (CheckProblem curProblem : problems) {
                    errorStringBuilder.append("\n-").append(curProblem.getMessage());
                }
                throw new DomibusConnectorSecurityException(errorStringBuilder.toString());
            }

            if (container == null) {
                throw new DomibusConnectorSecurityException("The resolved container is null!");
            }

            DC5MessageAttachment.DC5MessageAttachmentBuilder businessDocumentFromContainer
                    = retreiveBusinessDocumentFromContainer(message.getConnectorMessageId(), container);
            backendContentBuilder.businessDocument(businessDocumentFromContainer.build());


            if (container.getBusinessAttachments() != null && !container.getBusinessAttachments().isEmpty()) {
                for (DSSDocument businessAttachment : container.getBusinessAttachments()) {
                    try {
                        DC5MessageAttachment attachment = convertDocumentToMessageAttachment(
                                message,
                                businessAttachment,
                                businessAttachment.getName());
                        backendContentBuilder.attachment(attachment);
                    } catch (IOException e) {
                        LOGGER.error("Could not read attachment!", e);
                    }
                }
            }

            DSSDocument tokenPDF = container.getTokenPDF();
            if (tokenPDF != null) {
                try {
                    tokenPDF.setMimeType(MimeType.PDF);
                    tokenPDF.setName(TOKEN_PDF_FILE_NAME);
                    DC5MessageAttachment pdfTokenAttachment = convertDocumentToMessageAttachment(message, tokenPDF, TOKEN_PDF_IDENTIFIER);
                    backendContentBuilder.trustTokenPDF(pdfTokenAttachment);
                } catch (IOException e) {
                    LOGGER.error("Could not read Token PDF!", e);
                }
            }

            DSSDocument tokenXML = container.getTokenXML();
            if (tokenXML != null) {
                try {
                    tokenXML.setMimeType(MimeType.XML);
                    tokenXML.setName(TOKEN_XML_FILE_NAME);
                    DC5MessageAttachment attachment = convertDocumentToMessageAttachment(message, tokenXML, TOKEN_XML_IDENTIFIER);
                    backendContentBuilder.trustTokenXml(attachment);
                } catch (IOException e) {
                    LOGGER.error("Could not read Token XML!", e);
                }
            }
            message.getMessageContent().setBusinessContent(backendContentBuilder.build());


        } catch (ECodexException | IOException e) {
            throw new DomibusConnectorSecurityException(e);
        }

    }

    private DC5MessageAttachment.DC5MessageAttachmentBuilder retreiveBusinessDocumentFromContainer(DC5MessageId messageMessageId, ECodexContainer container) {
        LOGGER.trace("recieveContainerContents: check if businessContent contains detachedSignature [{}]",
                container.getBusinessContent().getDetachedSignature() != null);

        DC5MessageAttachment.DC5MessageAttachmentBuilder businessDocumentBuilder = DC5MessageAttachment.builder();
        DetachedSignature.DetachedSignatureBuilder detachedSignatureBuilder = DetachedSignature.builder();

        if (container.getBusinessContent().getDetachedSignature() != null) {
            try (InputStream is = container.getBusinessContent().getDetachedSignature().openStream()) {
                byte[] docAsBytes = IOUtils.toByteArray(is);
                detachedSignatureBuilder.detachedSignature(docAsBytes);
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("recieveContainerContents: Writing detachedSignature [{}]", new String(docAsBytes));
                }
            } catch (IOException e) {
                throw new DomibusConnectorSecurityException("Could not read detached signature!");
            }
            if (!StringUtils.isEmpty(container.getBusinessContent().getDetachedSignature().getName())) {
                detachedSignatureBuilder.detachedSignatureName(container.getBusinessContent().getDetachedSignature().getName());
                LOGGER.trace("recieveContainerContents: detachedSignature has name [{}]", container.getBusinessContent().getDetachedSignature().getName());
            }
            try {
                LOGGER.trace("recieveContainerContents: detachedSignature has mimeType [{}]",
                        container.getBusinessContent().getDetachedSignature().getMimeType().getMimeTypeString());

                detachedSignatureBuilder.mimeType(DetachedSignatureMimeType.valueOf(container.getBusinessContent()
                        .getDetachedSignature().getMimeType().getMimeTypeString()));

            } catch (IllegalArgumentException e) {
                LOGGER.warn("recieveContainerContents: No DetachedSignatureMimeType could be resolved of MimeType [{}], using default MimeType [{}]",
                        container.getBusinessContent().getDetachedSignature().getMimeType().getMimeTypeString(),
                        DetachedSignatureMimeType.BINARY.getCode());
                detachedSignatureBuilder.mimeType(DetachedSignatureMimeType.BINARY);
            }
            //set detached signature
//                        businessDocBuilder.withDetachedSignature(detachedSignatureBuilder.build());
            return businessDocumentBuilder.detachedSignature(DetachedSignature.builder().build());
        }


        if (container.getBusinessDocument() != null) {
            LOGGER.debug("The business document received from the container is of Mime Type {}",
                    container.getBusinessDocument().getMimeType().getMimeTypeString());
//                            try {

            //LOGGER.trace("recieveContainerContents: Read following byte content [{}]", IOUtils.toString(docAsBytes, "UTF8"));
            LargeFileReference bigDataRef = this.bigDataPersistenceService.createDomibusConnectorBigDataReference(messageMessageId, container.getBusinessDocument().getName(), container.getBusinessDocument().getMimeType().getMimeTypeString());

            LOGGER.trace("copying businessDocument input stream to bigDataReference output Stream");
            MessageDigest digest = getDigest();
            try (InputStream inputStream = container.getBusinessDocument().openStream();
                 DigestInputStream digestInputStream = new DigestInputStream(inputStream, digest);
                 OutputStream outputStream = bigDataRef.getOutputStream()) {
                StreamUtils.copy(digestInputStream, outputStream);
                businessDocumentBuilder.attachment(bigDataRef);
                businessDocumentBuilder.digest(Digest.ofMessageDigest(digestInputStream.getMessageDigest()));
            } catch (IOException ioe) {
                throw new DomibusConnectorSecurityException("Could not read business document!", ioe);
            }

//                                documentBuilder.setContent(bigDataRef);
//                        businessDocBuilder.setAttachment(bigDataRef);

//                        businessDocumentBuilder.digest()

            LOGGER.trace("recieveContainerContents: check if MimeType.PDF [{}] equals to [{}]",
                    MimeType.PDF.getMimeTypeString(), container.getToken().getDocumentType());

            if (MimeType.PDF.getMimeTypeString().equals(container.getToken().getDocumentType())) {
                String docName = MAIN_DOCUMENT_NAME;
                if (!StringUtils.isEmpty(container.getBusinessDocument().getName())) {
                    docName = container.getBusinessDocument().getName();
                }
                businessDocumentBuilder.identifier(docName);

//                                    documentBuilder.setName(docName);
//                                    message.getMessageContent().setDocument(documentBuilder.build());
            }

//                                LOGGER.trace("recieveContainerContents: check if MimeType.XML [{}] equals to [{}]",
//                                        MimeType.XML.getMimeTypeString(), container.getToken().getDocumentType());

//                                if (MimeType.XML.getMimeTypeString().equals(container.getToken().getDocumentType())
////                                        && !message.getMessageDetails().getAction().isDocumentRequired()
//                                ) {
//                                    LOGGER.trace("recieveContainerContents: Writing byteContent into MessageContent.setXmlContent");
//                                    try (InputStream businessContent = container.getBusinessDocument().openStream();) {
////                                        message.getMessageContent().setXmlContent(IOUtils.toByteArray(businessContent));
//                                        businessContent.close();
////                                        message.getMessageContent().getBusinessContent().setBusinessXml();
//                                    }
//                                }
//                            } catch (IOException e) {
//                                throw new DomibusConnectorSecurityException("Could not read business document!");
//                            }

        } else {
            LOGGER.debug("The business document received from the container is null!");
        }
        return businessDocumentBuilder;
    }

    private MessageDigest getDigest() {
        try {
            //TODO: use configuration here!
            return MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream getAsicsContainerInputStream(DC5MessageAttachment asicsAttachment) {
        LargeFileReference asicContainerDataRef = bigDataPersistenceService.getReadableDataSource(asicsAttachment.getAttachment());
        InputStream asicInputStream;
        try {
            asicInputStream = asicContainerDataRef.getInputStream();
            if (LOGGER.isTraceEnabled()) {
                asicInputStream = logTraceStream(asicInputStream, "asicInputStream", true);
            }
            return asicInputStream;
        } catch (IOException ioe) {
            throw new DomibusConnectorSecurityException(String.format("error while initializing asicInputStream from big data reference %s",
                    asicContainerDataRef.getStorageIdReference()), ioe);
        }
    }

    private InputStream getTokenXmlStream(DC5MessageAttachment tokenXMLAttachment) {
        LargeFileReference xmlTokenDataRef = bigDataPersistenceService.getReadableDataSource(tokenXMLAttachment.getAttachment());
        InputStream tokenStream;
        try {
            tokenStream = xmlTokenDataRef.getInputStream();
            if (LOGGER.isTraceEnabled()) {
                tokenStream = logTraceStream(tokenStream, "tokenStream", true);
            }
            return tokenStream;
        } catch (IOException ioe) {
            throw new DomibusConnectorSecurityException(String.format("error while initializing xmlTokenDataRef get input stream from %s",
                    xmlTokenDataRef.getStorageIdReference()), ioe);
        }
    }

    private InputStream logTraceStream(InputStream inputStream, String name, boolean logStreamContent) {
        //TODO: use teeStream! avoid copy into byte array!
        LOGGER.debug("#logTraceStream [{}]: into byte[]", name);
        try {
            byte[] byteArray = StreamUtils.copyToByteArray(inputStream);
            LOGGER.debug("#logTraceStream [{}]: read [{}] bytes", name, byteArray.length);
            if (logStreamContent) {
                LOGGER.trace("#logTraceStream [{}]: content: [{}]", name, new String(byteArray));
            }
            return new ByteArrayInputStream(byteArray);
        } catch (IOException ioe) {
            LOGGER.debug("#logTraceStream [{}]: Error while reading from stream!", name);
            throw new RuntimeException(ioe);
        }
    }


    private DC5MessageAttachment convertDocumentToMessageAttachment(DC5Message message, DSSDocument document, String identifier) //, String name, String mimeType)
            throws IOException {
        LOGGER.trace("convertDocumentToMessageAttachment: called with message [{}], document [{}], identifier [{}]", message, document, identifier);

        LargeFileReference bigDataRef = bigDataPersistenceService.createDomibusConnectorBigDataReference(message.getConnectorMessageIdAsString(), document.getName(), document.getMimeType().getMimeTypeString());

        String documentName = document.getName();
        String mimeTypeString = MimeType.BINARY.getMimeTypeString();
        if (document.getMimeType() != null) {
            mimeTypeString = document.getMimeType().getMimeTypeString();
        }

        bigDataRef.setName(documentName);
        bigDataRef.setMimetype(mimeTypeString);

//        LOGGER.trace("copied: [{}]", IOUtils.toString(document.openStream()));
//        
        LOGGER.debug("Copy input stream from dss document to output stream of big data reference");
        try (InputStream inputStream = document.openStream();
             DigestOutputStream digestOutputStream = new DigestOutputStream(bigDataRef.getOutputStream(), getDigest()) ) {
            long bytesCopied = StreamUtils.copy(inputStream, digestOutputStream);
            bigDataRef.setDigest(Digest.ofMessageDigest(digestOutputStream.getMessageDigest()));
            bigDataRef.setSize(bytesCopied);
            if (bytesCopied == 0) {
                throw new DomibusConnectorSecurityException("Cannot create attachment with empty content!");
                //TODO: delete bigDataRef from database!
            }
        } catch (IOException ioe) {
            throw new DomibusConnectorSecurityException("Error while writing attachment to storage!", ioe);
        }


        if (StringUtils.isEmpty(identifier)) {
            throw new DomibusConnectorSecurityException("Cannot create attachment without identifier!");
        }

        DC5MessageAttachment attachment = DC5MessageAttachment
                .builder()
                .identifier(identifier)
                .attachment(bigDataRef)
                .digest(bigDataRef.getDigest())
                .size(bigDataRef.getSize())
                .name(documentName)
                .mimeType(mimeTypeString)
                .build();


        LOGGER.trace("attachment created with bigDataRef [{}], and identifier [{}], name [{}], mimeTypeString [{}]", bigDataRef, identifier, documentName, mimeTypeString);

        return attachment;
    }


    /**
     * in later versions this method should return a streaming based DSSDocument
     * so no conversion to an byte[] is necessary
     *
     * @param name     name of the dssDocument
     * @param mimeType mimeType of the dssDocument
     * @return the created InMemoryDocument
     */
    @Deprecated
    DSSDocument createLargeFileBasedDssDocument(DC5MessageAttachment attachment, String name, MimeType mimeType) {
        LargeFileReference dataRef = attachment.getAttachment();
        LargeFileReference readableDataSource = bigDataPersistenceService.getReadableDataSource(dataRef);
        try (InputStream inputStream = readableDataSource.getInputStream()) {
            byte[] content = StreamUtils.copyToByteArray(inputStream);
            inputStream.close();
            InMemoryDocument dssDocument = new InMemoryDocument(content, name, MimeType.PDF);
            return dssDocument;
        } catch (IOException ioe) {
            throw new RuntimeException("error while loading data from bigDataPersistenceService", ioe);
        }
    }


//    DSSDocument createLargeFileBasedDssDocument(LargeFileReference dataRef, String name, MimeType mimeType) {
//        return new LargeFileBasedDssDocument(bigDataPersistenceService, dataRef);
//    }


}
