package eu.ecodex.connector.security.container;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import eu.ecodex.connector.common.ECodexConnectorGlobalConstants;
import eu.ecodex.connector.common.enums.DetachedSignatureMimeType;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageAttachment;
import eu.ecodex.connector.security.exception.ECodexConnectorSecurityException;
import eu.ecodex.dss.model.BusinessContent;
import eu.ecodex.dss.model.CertificateStoreInfo;
import eu.ecodex.dss.model.Document;
import eu.ecodex.dss.model.ECodexContainer;
import eu.ecodex.dss.model.MemoryDocument;
import eu.ecodex.dss.model.MimeType;
import eu.ecodex.dss.model.SignatureParameters;
import eu.ecodex.dss.model.checks.CheckProblem;
import eu.ecodex.dss.model.checks.CheckResult;
import eu.ecodex.dss.model.token.TokenIssuer;
import eu.ecodex.dss.service.ECodexContainerService;
import eu.ecodex.dss.service.ECodexException;
import eu.ecodex.dss.util.SignatureParametersFactory;
import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.EncryptionAlgorithm;

public class ECodexSecurityContainer implements InitializingBean {

    private static final String TOKEN_XML_FILE_NAME = "Token.xml";

    private static final String TOKEN_PDF_FILE_NAME = "Token.pdf";

    private static final String TOKEN_XML_DOCUMENT_NAME = "tokenXML";

    private static final String DETACHED_SIGNATURE_DOCUMENT_NAME = "detachedSignature";

    static Logger LOGGER = LoggerFactory.getLogger(ECodexSecurityContainer.class);

    ECodexContainerService containerService;
    TokenIssuer tokenIssuer;

    private String javaKeyStorePath;
    private String javaKeyStorePassword;
    private String keyAlias;
    private String keyPassword;

    public ECodexContainerService getContainerService() {
        return containerService;
    }

    public void setContainerService(ECodexContainerService containerService) {
        this.containerService = containerService;
    }

    public void setTokenIssuer(TokenIssuer tokenIssuer) {
        this.tokenIssuer = tokenIssuer;
    }

    public void setJavaKeyStorePath(String javaKeyStorePath) {
        this.javaKeyStorePath = javaKeyStorePath;
    }

    public void setJavaKeyStorePassword(String javaKeyStorePassword) {
        this.javaKeyStorePassword = javaKeyStorePassword;
    }

    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("Initializing security container!");

        Security.addProvider(new BouncyCastleProvider());

        final SignatureParameters params = createSignatureParameters();

        containerService.setContainerSignatureParameters(params);

        LOGGER.info("Finished initializing security container!");
    }

    protected SignatureParameters createSignatureParameters() throws Exception {

        // KlarA: Changed the functionality of this method to use the methods
        // that have been ordered by Austria
        // and realized by Arhs.

        CertificateStoreInfo certStore = new CertificateStoreInfo();
        certStore.setLocation(javaKeyStorePath);
        certStore.setPassword(javaKeyStorePassword);

        final SignatureParameters mySignatureParameters = SignatureParametersFactory.create(certStore, keyAlias,
                keyPassword, EncryptionAlgorithm.RSA, DigestAlgorithm.SHA1);

        return mySignatureParameters;

    }

    private BusinessContent buildBusinessContent(Message message) {
        BusinessContent businessContent = new BusinessContent();

        Document document = null;
        if (ArrayUtils.isNotEmpty(message.getMessageContent().getPdfDocument())) {
            document = new MemoryDocument(message.getMessageContent().getPdfDocument(),
                    ECodexConnectorGlobalConstants.MAIN_DOCUMENT_NAME, MimeType.PDF);
        } else if (message.getMessageDetails().isValidWithoutPDF()) {
            document = new MemoryDocument(message.getMessageContent().getECodexContent(),
                    ECodexConnectorGlobalConstants.MAIN_DOCUMENT_NAME, MimeType.XML);
        } else
            LOGGER.error("No content found for container!");

        businessContent.setDocument(document);

        if (message.getMessageContent().getDetachedSignature() != null
                && message.getMessageContent().getDetachedSignatureMimeType() != null) {
            Document detachedSignature = new MemoryDocument(message.getMessageContent().getDetachedSignature(),
                    DETACHED_SIGNATURE_DOCUMENT_NAME, MimeType.valueOf(message.getMessageContent()
                            .getDetachedSignatureMimeType().name()));
            businessContent.setDetachedSignature(detachedSignature);
        }

        if (message.getAttachments() != null && !message.getAttachments().isEmpty()) {
            for (MessageAttachment attachment : message.getAttachments()) {
                businessContent.addAttachment(new MemoryDocument(attachment.getAttachment(), attachment.getName(),
                        MimeType.fromFileName(attachment.getName())));
            }
        }

        return businessContent;
    }

    public void createContainer(Message message) {
        BusinessContent businessContent = buildBusinessContent(message);

        if (message.getAttachments() != null && !message.getAttachments().isEmpty()) {
            message.getAttachments().clear();
        }
        try {
            ECodexContainer container = containerService.create(businessContent, tokenIssuer);

            // KlarA: Added check of the container and the respective
            // error-handling
            CheckResult results = containerService.check(container);

            if (results.isSuccessful()) {
                if (container != null) {
                    Document asicDocument = container.getAsicDocument();
                    if (asicDocument != null) {
                        try {
                            MessageAttachment asicAttachment = convertDocumentToMessageAttachment(asicDocument,
                                    asicDocument.getName(), asicDocument.getMimeType().getCode());
                            message.addAttachment(asicAttachment);
                        } catch (IOException e) {
                            throw new ECodexConnectorSecurityException(e);
                        }
                    }
                    Document tokenXML = container.getTokenXML();
                    if (tokenXML != null) {
                        try {
                            MessageAttachment tokenAttachment = convertDocumentToMessageAttachment(tokenXML,
                                    TOKEN_XML_DOCUMENT_NAME, MimeType.XML.getCode());
                            message.addAttachment(tokenAttachment);
                        } catch (IOException e) {
                            throw new ECodexConnectorSecurityException(e);
                        }
                    }
                }
            } else {
                String errormessage = "\nSeveral problems prevented the container from being created:";
                List<CheckProblem> problems = results.getProblems();
                for (CheckProblem curProblem : problems) {
                    errormessage += "\n-" + curProblem.getMessage();
                }
                throw new ECodexConnectorSecurityException(errormessage);
            }
        } catch (ECodexException e) {
            throw new ECodexConnectorSecurityException(e);
        }
    }

    public void recieveContainerContents(Message message) {
        if (message.getAttachments() != null && !message.getAttachments().isEmpty()) {
            MessageAttachment asicsAttachment = null;
            MessageAttachment tokenXMLAttachment = null;
            for (MessageAttachment attachment : message.getAttachments()) {
                if (
                // attachment.getMimeType().equals(MimeType.ASICS.getCode()) &&
                attachment.getName().endsWith(".asics")) {
                    asicsAttachment = attachment;
                } else if (
                // attachment.getMimeType().equals(MimeType.XML.getCode()) &&
                attachment.getName().equals(TOKEN_XML_DOCUMENT_NAME)) {
                    tokenXMLAttachment = attachment;
                }
            }
            if (asicsAttachment == null) {
                throw new ECodexConnectorSecurityException("Could not find ASICS container in message attachments!");
            }
            if (tokenXMLAttachment == null) {
                throw new ECodexConnectorSecurityException("Could not find token XML in message attachments!");
            }
            message.getAttachments().remove(asicsAttachment);
            message.getAttachments().remove(tokenXMLAttachment);

            InputStream asicInputStream = new ByteArrayInputStream(asicsAttachment.getAttachment());
            InputStream tokenStream = new ByteArrayInputStream(tokenXMLAttachment.getAttachment());
            try {
                ECodexContainer container = containerService.receive(asicInputStream, tokenStream);

                // KlarA: Added check of the container and the respective
                // error-handling
                CheckResult results = containerService.check(container);

                if (results.isSuccessful()) {
                    if (container != null) {
                        if (container.getBusinessDocument() != null) {
                            LOGGER.debug("The business document received from the container is of Mime Type {}",
                                    container.getBusinessDocument().getMimeType());
                            try {
                                InputStream is = container.getBusinessDocument().openStream();
                                byte[] docAsBytes = new byte[is.available()];
                                is.read(docAsBytes);
                                if (container.getToken().getDocumentType().equals(MimeType.PDF.name()))
                                    message.getMessageContent().setPdfDocument(docAsBytes);
                                if (container.getToken().getDocumentType().equals(MimeType.XML.name())
                                        && message.getMessageDetails().isValidWithoutPDF())
                                    message.getMessageContent().setECodexContent(docAsBytes);
                            } catch (IOException e) {
                                throw new ECodexConnectorSecurityException("Could not read business document!");
                            }

                        } else {
                            LOGGER.debug("The business document received from the container is null!");
                        }

                        if (container.getBusinessContent().getDetachedSignature() != null) {
                            try {
                                InputStream is = container.getBusinessContent().getDetachedSignature().openStream();
                                byte[] docAsBytes = new byte[is.available()];
                                is.read(docAsBytes);
                                message.getMessageContent().setDetachedSignature(docAsBytes);
                            } catch (IOException e) {
                                throw new ECodexConnectorSecurityException("Could not read detached signature!");
                            }
                            message.getMessageContent().setDetachedSignatureMimeType(
                                    DetachedSignatureMimeType.valueOf(container.getBusinessContent()
                                            .getDetachedSignature().getMimeType().name()));
                        }

                        if (container.getBusinessAttachments() != null && !container.getBusinessAttachments().isEmpty()) {
                            for (Document businessAttachment : container.getBusinessAttachments()) {
                                try {
                                    MessageAttachment attachment = convertDocumentToMessageAttachment(
                                            businessAttachment, businessAttachment.getName(), businessAttachment
                                                    .getMimeType().getCode());

                                    message.addAttachment(attachment);
                                } catch (IOException e) {
                                    LOGGER.error("Could not read attachment!", e);
                                    continue;
                                }
                            }
                        }
                        Document tokenPDF = container.getTokenPDF();
                        if (tokenPDF != null) {
                            try {
                                MessageAttachment attachment = convertDocumentToMessageAttachment(tokenPDF,
                                        TOKEN_PDF_FILE_NAME, MimeType.PDF.getCode());
                                message.addAttachment(attachment);
                            } catch (IOException e) {
                                LOGGER.error("Could not read Token PDF!", e);
                            }
                        }

                        Document tokenXML = container.getTokenXML();
                        if (tokenXML != null) {
                            try {
                                MessageAttachment attachment = convertDocumentToMessageAttachment(tokenXML,
                                        TOKEN_XML_FILE_NAME, MimeType.XML.getCode());
                                message.addAttachment(attachment);
                            } catch (IOException e) {
                                LOGGER.error("Could not read Token XML!", e);
                            }
                        }
                    } else {
                        throw new ECodexConnectorSecurityException("The resolved business container is null!");
                    }
                } else {
                    String errormessage = "\nSeveral problems prevented the container from being created:";
                    List<CheckProblem> problems = results.getProblems();
                    for (CheckProblem curProblem : problems) {
                        errormessage += "\n-" + curProblem.getMessage();
                    }
                    throw new ECodexConnectorSecurityException(errormessage);
                }
            } catch (ECodexException e) {
                throw new ECodexConnectorSecurityException(e);
            }
        }
    }

    private MessageAttachment convertDocumentToMessageAttachment(Document document, String name, String mimeType)
            throws IOException {
        MessageAttachment attachment = new MessageAttachment();
        attachment.setAttachment(IOUtils.toByteArray(document.openStream()));
        attachment.setName(name);
        attachment.setMimeType(document.getMimeType().toString());

        return attachment;
    }
}
