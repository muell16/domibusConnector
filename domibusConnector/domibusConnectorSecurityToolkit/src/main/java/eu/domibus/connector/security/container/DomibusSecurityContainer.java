package eu.domibus.connector.security.container;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import eu.domibus.connector.common.CommonConnectorGlobalConstants;

import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.MessageAttachment;
import eu.domibus.connector.domain.enums.DetachedSignatureMimeType;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;
import eu.ecodex.dss.model.BusinessContent;
import eu.ecodex.dss.model.CertificateStoreInfo;
import eu.ecodex.dss.model.ECodexContainer;
import eu.ecodex.dss.model.SignatureParameters;
import eu.ecodex.dss.model.checks.CheckProblem;
import eu.ecodex.dss.model.checks.CheckResult;
import eu.ecodex.dss.model.token.AdvancedSystemType;
import eu.ecodex.dss.model.token.TokenIssuer;
import eu.ecodex.dss.service.ECodexContainerService;
import eu.ecodex.dss.service.ECodexException;
import eu.ecodex.dss.util.SignatureParametersFactory;
import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.DigestAlgorithm;
import eu.europa.esig.dss.EncryptionAlgorithm;
import eu.europa.esig.dss.InMemoryDocument;
import eu.europa.esig.dss.MimeType;

@Component("domibusConnectorSecurityContainer")
public class DomibusSecurityContainer implements InitializingBean {

    private static final String TOKEN_XML_FILE_NAME = "Token.xml";

    private static final String TOKEN_PDF_FILE_NAME = "Token.pdf";

    private static final String DETACHED_SIGNATURE_DOCUMENT_NAME = "detachedSignature";

    static Logger LOGGER = LoggerFactory.getLogger(DomibusSecurityContainer.class);

    @Resource(name="domibusConnectorContainerService")
    ECodexContainerService containerService;
    
    TokenIssuer tokenIssuer;

    @Value("${connector.security.keystore.path:null}")
    String javaKeyStorePath;
    @Value("${connector.security.keystore.password:null}")
    String javaKeyStorePassword;
    @Value("${connector.security.key.alias:null}")
    String keyAlias;
    @Value("${connector.security.key.password:null}")
    String keyPassword;
    
    @Value("${token.issuer.country:null}")
    String country;
    
    @Value("${token.issuer.service.provider:null}")
    String serviceProvider;
    
    @Value("${token.issuer.aes.value:null}")
    AdvancedSystemType advancedElectronicSystem;

    public ECodexContainerService getContainerService() {
        return containerService;
    }

    public void setContainerService(ECodexContainerService containerService) {
        this.containerService = containerService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("Initializing security container!");

        Security.addProvider(new BouncyCastleProvider());

        final SignatureParameters params = createSignatureParameters();

        containerService.setContainerSignatureParameters(params);
        
        tokenIssuer = new TokenIssuer();
        tokenIssuer.setCountry(country);
        tokenIssuer.setServiceProvider(serviceProvider);
        tokenIssuer.setAdvancedElectronicSystem(advancedElectronicSystem);

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

        DSSDocument document = null;
        if (ArrayUtils.isNotEmpty(message.getMessageContent().getPdfDocument())) {
            String pdfName = StringUtils.isEmpty(message.getMessageContent().getPdfDocumentName()) ? CommonConnectorGlobalConstants.MAIN_DOCUMENT_NAME
                    + ".pdf"
                    : message.getMessageContent().getPdfDocumentName();
            document = new InMemoryDocument(message.getMessageContent().getPdfDocument(), pdfName, MimeType.PDF);
        } else if (message.getMessageDetails().isValidWithoutPDF()) {
            byte[] content = message.getMessageContent().getInternationalContent() != null ? message
                    .getMessageContent().getInternationalContent() : message.getMessageContent()
                    .getNationalXmlContent();
            document = new InMemoryDocument(content, CommonConnectorGlobalConstants.MAIN_DOCUMENT_NAME + ".xml",
                    MimeType.XML);
        } else
            LOGGER.error("No content found for container!");

        businessContent.setDocument(document);

        if (message.getMessageContent().getDetachedSignature() != null
                && message.getMessageContent().getDetachedSignatureMimeType() != null) {
            String detachedSignatureName = message.getMessageContent().getDetachedSignatureName() != null ? message
                    .getMessageContent().getDetachedSignatureName() : DETACHED_SIGNATURE_DOCUMENT_NAME;
                    DSSDocument detachedSignature = new InMemoryDocument(message.getMessageContent().getDetachedSignature(),
                    detachedSignatureName, MimeType.fromMimeTypeString(message.getMessageContent().getDetachedSignatureMimeType()
                            .name()));
            businessContent.setDetachedSignature(detachedSignature);
        }

        if (message.getAttachments() != null && !message.getAttachments().isEmpty()) {
            for (MessageAttachment attachment : message.getAttachments()) {
                businessContent.addAttachment(new InMemoryDocument(attachment.getAttachment(), attachment.getName(),
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
                	DSSDocument asicDocument = container.getAsicDocument();
                    if (asicDocument != null) {
                        try {
                            MessageAttachment asicAttachment = convertDocumentToMessageAttachment(asicDocument, CommonConnectorGlobalConstants.ASICS_CONTAINER_IDENTIFIER,
                                    asicDocument.getName(), asicDocument.getMimeType().getMimeTypeString());
                            message.addAttachment(asicAttachment);
                        } catch (IOException e) {
                            throw new DomibusConnectorSecurityException(e);
                        }
                    }
                    DSSDocument tokenXML = container.getTokenXML();
                    if (tokenXML != null) {
                        try {
                            MessageAttachment tokenAttachment = convertDocumentToMessageAttachment(tokenXML, CommonConnectorGlobalConstants.TOKEN_XML_IDENTIFIER,
                                    TOKEN_XML_FILE_NAME, MimeType.XML.getMimeTypeString());
                            message.addAttachment(tokenAttachment);
                        } catch (IOException e) {
                            throw new DomibusConnectorSecurityException(e);
                        }
                    }
                }
            } else {
                String errormessage = "\nSeveral problems prevented the container from being created:";
                List<CheckProblem> problems = results.getProblems();
                for (CheckProblem curProblem : problems) {
                    errormessage += "\n-" + curProblem.getMessage();
                }
                throw new DomibusConnectorSecurityException(errormessage);
            }
        } catch (ECodexException e) {
            throw new DomibusConnectorSecurityException(e);
        }
    }

    public void recieveContainerContents(Message message) {
        if (message.getAttachments() != null && !message.getAttachments().isEmpty()) {
            MessageAttachment asicsAttachment = null;
            MessageAttachment tokenXMLAttachment = null;
            for (MessageAttachment attachment : message.getAttachments()) {
            	
                if (attachment.getIdentifier().equals(CommonConnectorGlobalConstants.ASICS_CONTAINER_IDENTIFIER) || attachment.getIdentifier().endsWith(".asics")) {
                    asicsAttachment = attachment;
                } else if (attachment.getIdentifier().equals(CommonConnectorGlobalConstants.TOKEN_XML_IDENTIFIER) || attachment.getIdentifier().equals(TOKEN_XML_FILE_NAME)) {
                    tokenXMLAttachment = attachment;
                }
            }
            if (asicsAttachment == null) {
                throw new DomibusConnectorSecurityException("Could not find ASICS container in message attachments!");
            }
            if (tokenXMLAttachment == null) {
                throw new DomibusConnectorSecurityException("Could not find token XML in message attachments!");
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
                            	byte[] docAsBytes = IOUtils.toByteArray(container.getBusinessDocument().openStream());
                                if (container.getToken().getDocumentType().equals(MimeType.PDF.getMimeTypeString())) {
                                    message.getMessageContent().setPdfDocument(docAsBytes);
                                    if (!StringUtils.isEmpty(container.getBusinessDocument().getName())) {
                                        message.getMessageContent().setPdfDocumentName(
                                                container.getBusinessDocument().getName());
                                    }
                                }
                                if (container.getToken().getDocumentType().equals(MimeType.XML.getMimeTypeString())
                                        && message.getMessageDetails().isValidWithoutPDF())
                                    message.getMessageContent().setInternationalContent(docAsBytes);
                            } catch (IOException e) {
                                throw new DomibusConnectorSecurityException("Could not read business document!");
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
                                throw new DomibusConnectorSecurityException("Could not read detached signature!");
                            }
                            if (!StringUtils.isEmpty(container.getBusinessContent().getDetachedSignature().getName()))
                                message.getMessageContent().setDetachedSignatureName(
                                        container.getBusinessContent().getDetachedSignature().getName());
                            try {
                                message.getMessageContent().setDetachedSignatureMimeType(
                                        DetachedSignatureMimeType.valueOf(container.getBusinessContent()
                                                .getDetachedSignature().getMimeType().getMimeTypeString()));
                            } catch (IllegalArgumentException e) {
                                LOGGER.error("No DetachedSignatureMimeType could be resolved of MimeType {}", container
                                        .getBusinessContent().getDetachedSignature().getMimeType().getMimeTypeString());
                                message.getMessageContent().setDetachedSignatureMimeType(
                                        DetachedSignatureMimeType.BINARY);
                            }
                        }

                        if (container.getBusinessAttachments() != null && !container.getBusinessAttachments().isEmpty()) {
                            for (DSSDocument businessAttachment : container.getBusinessAttachments()) {
                                try {
                                    MessageAttachment attachment = convertDocumentToMessageAttachment(
                                            businessAttachment, businessAttachment.getName(), businessAttachment.getName(), businessAttachment
                                                    .getMimeType().getMimeTypeString());

                                    message.addAttachment(attachment);
                                } catch (IOException e) {
                                    LOGGER.error("Could not read attachment!", e);
                                    continue;
                                }
                            }
                        }
                        DSSDocument tokenPDF = container.getTokenPDF();
                        if (tokenPDF != null) {
                            try {
                                MessageAttachment attachment = convertDocumentToMessageAttachment(tokenPDF, CommonConnectorGlobalConstants.TOKEN_PDF_IDENTIFIER, 
                                        TOKEN_PDF_FILE_NAME, MimeType.PDF.getMimeTypeString());
                                message.addAttachment(attachment);
                            } catch (IOException e) {
                                LOGGER.error("Could not read Token PDF!", e);
                            }
                        }

                        DSSDocument tokenXML = container.getTokenXML();
                        if (tokenXML != null) {
                            try {
                                MessageAttachment attachment = convertDocumentToMessageAttachment(tokenXML, CommonConnectorGlobalConstants.TOKEN_XML_IDENTIFIER, 
                                        TOKEN_XML_FILE_NAME, MimeType.XML.getMimeTypeString());
                                message.addAttachment(attachment);
                            } catch (IOException e) {
                                LOGGER.error("Could not read Token XML!", e);
                            }
                        }
                    } else {
                        throw new DomibusConnectorSecurityException("The resolved business container is null!");
                    }
                } else {
                    String errormessage = "\nSeveral problems prevented the container from being created:";
                    List<CheckProblem> problems = results.getProblems();
                    for (CheckProblem curProblem : problems) {
                        errormessage += "\n-" + curProblem.getMessage();
                    }
                    throw new DomibusConnectorSecurityException(errormessage);
                }
            } catch (ECodexException e) {
                throw new DomibusConnectorSecurityException(e);
            }
        }
    }

    private MessageAttachment convertDocumentToMessageAttachment(DSSDocument document, String identifier, String name, String mimeType)
            throws IOException {
    	
    	byte[] byteArray = IOUtils.toByteArray(document.openStream());
    	
    	if(ArrayUtils.isEmpty(byteArray)){
    		throw new DomibusConnectorSecurityException("Cannot create attachment without content data!");
    	}
    	if(StringUtils.isEmpty(identifier)){
    		throw new DomibusConnectorSecurityException("Cannot create attachment without identifier!");
    	}
    	
        MessageAttachment attachment = new MessageAttachment(byteArray, identifier);
		attachment.setName(name);
        attachment.setMimeType(mimeType);

        return attachment;
    }
}
