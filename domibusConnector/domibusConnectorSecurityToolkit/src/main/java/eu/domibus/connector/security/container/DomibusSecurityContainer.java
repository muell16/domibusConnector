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


import eu.domibus.connector.domain.model.DetachedSignatureMimeType;
import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageAttachment;
import eu.domibus.connector.domain.model.DomibusConnectorMessageContent;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDocument;
import eu.domibus.connector.domain.model.builder.DetachedSignatureBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageDocumentBuilder;
import eu.domibus.connector.persistence.service.DomibusConnectorBigDataPersistenceService;
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
import java.io.OutputStream;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;

/**
 * TODO: documentation needed
 * 
 */
@Component("domibusConnectorSecurityContainer")
public class DomibusSecurityContainer implements InitializingBean {
   
    public static final String MAIN_DOCUMENT_NAME = "mainDocument";
    
    public static final String ASICS_CONTAINER_IDENTIFIER = "ASIC-S";

    public static final String TOKEN_XML_IDENTIFIER = "tokenXML";

    public static final String TOKEN_PDF_IDENTIFIER = "tokenPDF";

    public static final String CONTENT_PDF_IDENTIFIER = "ContentPDF";

    public static final String CONTENT_XML_IDENTIFIER = "ContentXML";
    
    private static final String TOKEN_XML_FILE_NAME = "Token.xml";

    private static final String TOKEN_PDF_FILE_NAME = "Token.pdf";

    private static final String DETACHED_SIGNATURE_DOCUMENT_NAME = "detachedSignature";

    static Logger LOGGER = LoggerFactory.getLogger(DomibusSecurityContainer.class);
    
    static org.slf4j.Marker CONFIDENTAL_MARKER = org.slf4j.MarkerFactory.getMarker("CONFIDENTAL");

    @Resource(name="domibusConnectorContainerService")
    ECodexContainerService containerService;
            
    @Autowired
    DomibusConnectorBigDataPersistenceService bigDataPersistenceService;
    
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
        LOGGER.debug("tokenIssuer initialized with country [{}], serviceProvide [{}] and advancedElectronicSystem [{}] ",
                country, serviceProvider, advancedElectronicSystem);
        
        LOGGER.info("Finished initializing security container!");
    }

    protected SignatureParameters createSignatureParameters() throws Exception {
        LOGGER.debug("creatingSignatureParameters");
        // KlarA: Changed the functionality of this method to use the methods
        // that have been ordered by Austria
        // and realized by Arhs.

        CertificateStoreInfo certStore = new CertificateStoreInfo();
        certStore.setLocation(javaKeyStorePath);
        certStore.setPassword(javaKeyStorePassword);

        EncryptionAlgorithm encryptionAlgorithm = EncryptionAlgorithm.RSA;
        DigestAlgorithm digestAlgorithm = DigestAlgorithm.SHA1;
        
        final SignatureParameters mySignatureParameters = SignatureParametersFactory.create(certStore, keyAlias,
                keyPassword, encryptionAlgorithm, digestAlgorithm);
        LOGGER.info("SignatureParameters are certStore [{}], keyAlias [{}], encryptionAlgorithm [{}], digestAlgorithm [{}]",
                certStore.getLocation(), keyAlias, encryptionAlgorithm, digestAlgorithm);        
        
        
        return mySignatureParameters;

    }

    BusinessContent buildBusinessContent(@Nonnull DomibusConnectorMessage message) {
        if (message.getMessageContent() == null) {
            throw new IllegalArgumentException("messageContent is null!");
        }
        @Nonnull DomibusConnectorMessageContent messageContent = message.getMessageContent();
        
        BusinessContent businessContent = new BusinessContent();

        DSSDocument dssDocument = null;

        
        
        if (messageContent.getDocument() != null) {
            //
            DomibusConnectorMessageDocument document = messageContent.getDocument();
            
            
            //we are still assuming that the bussiness document is always a pdf!
            String pdfName = StringUtils.isEmpty(document.getDocumentName()) ? MAIN_DOCUMENT_NAME
                    + ".pdf"
                    : messageContent.getDocument().getDocumentName();            
            dssDocument = createInMemoryDocument(document.getDocument(), pdfName, MimeType.PDF);
        
        // message action does not require a document, make xml to main document
        } else if (!message.getMessageDetails().getAction().isDocumentRequired() && message.getMessageContent().getXmlContent() != null) {
            byte[] content = message.getMessageContent().getXmlContent();
            dssDocument = new InMemoryDocument(content, MAIN_DOCUMENT_NAME + ".xml", MimeType.XML);
        } else {            
            LOGGER.error("No content found for container!");
            throw new RuntimeException("not valid without document!");
        }
            
        businessContent.setDocument(dssDocument);

        DomibusConnectorMessageDocument msgDocument = messageContent.getDocument();
        
        if (msgDocument != null && 
                msgDocument.getDetachedSignature() != null
                && msgDocument.getDetachedSignature().getMimeType() != null) {
            
            String detachedSignatureName = msgDocument.getDetachedSignature().getDetachedSignatureName() != null ? msgDocument
                    .getDetachedSignature().getDetachedSignatureName() : DETACHED_SIGNATURE_DOCUMENT_NAME;
            
                    
            
                    DSSDocument detachedSignature = new InMemoryDocument(
                            msgDocument.getDetachedSignature().getDetachedSignature(),
                            detachedSignatureName,
                            MimeType.fromMimeTypeString(msgDocument.getDetachedSignature().getMimeType().getCode()));
            businessContent.setDetachedSignature(detachedSignature);
        }

        for (DomibusConnectorMessageAttachment attachment : message.getMessageAttachments()) {
            
            
            MimeType mimeType = MimeType.fromMimeTypeString(attachment.getMimeType());
            LOGGER.debug("buildBusinessContent: detected mimeType [{}] in attachment [{}]", mimeType.getMimeTypeString(), attachment);
            
            DSSDocument dssInMemoryDoc = createInMemoryDocument(attachment.getAttachment(), attachment.getName(), mimeType);
            
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
     * 
     * the messageContent of the message must not be null!
     * 
     * @param message the message to process
     */
    public void createContainer(@Nonnull DomibusConnectorMessage message) {
        BusinessContent businessContent = buildBusinessContent(message);

        message.getMessageAttachments().clear();

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
                            DomibusConnectorMessageAttachment asicAttachment = convertDocumentToMessageAttachment(message, asicDocument, ASICS_CONTAINER_IDENTIFIER);
                            message.addAttachment(asicAttachment);
                        } catch (IOException e) {
                            throw new DomibusConnectorSecurityException(e);
                        }
                    }
                    DSSDocument tokenXML = container.getTokenXML();
                    
                    if (tokenXML != null) {
                        try {
                            tokenXML.setName(TOKEN_XML_FILE_NAME);
                            DomibusConnectorMessageAttachment tokenAttachment = convertDocumentToMessageAttachment(message, tokenXML, TOKEN_XML_IDENTIFIER);
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

    /**
     * Unpacks the asic container from the message
     * and puts the businessDocument, xmlContent into MessageContent
     * and other attachments are added to the messageAttachments
     * the asicAttachment and xmlToken attachment are removed from the message
     * @param message - the message to process
     */
    public void recieveContainerContents(DomibusConnectorMessage message) {
        if (message.getMessageAttachments() != null && !message.getMessageAttachments().isEmpty()) {
            DomibusConnectorMessageAttachment asicsAttachment = null;
            DomibusConnectorMessageAttachment tokenXMLAttachment = null;
            for (DomibusConnectorMessageAttachment attachment : message.getMessageAttachments()) {
            	
                if (attachment.getIdentifier().equals(ASICS_CONTAINER_IDENTIFIER) || attachment.getIdentifier().endsWith(".asics")) {
                    asicsAttachment = attachment;
                } else if (attachment.getIdentifier().equals(TOKEN_XML_IDENTIFIER) || attachment.getIdentifier().equals(TOKEN_XML_FILE_NAME)) {
                    tokenXMLAttachment = attachment;
                }
            }
            if (asicsAttachment == null) {
                throw new DomibusConnectorSecurityException("Could not find ASICS container in message attachments!");
            }
            if (tokenXMLAttachment == null) {
                throw new DomibusConnectorSecurityException("Could not find token XML in message attachments!");
            }
            message.getMessageAttachments().remove(asicsAttachment);
            message.getMessageAttachments().remove(tokenXMLAttachment);

            
            DomibusConnectorBigDataReference asicContainerDataRef = bigDataPersistenceService.getReadableDataSource(asicsAttachment.getAttachment());
            InputStream asicInputStream;
            try {                
                asicInputStream = asicContainerDataRef.getInputStream();
            } catch (IOException ioe) {                
                throw new RuntimeException(String.format("error while initializing asicInputStream from big data reference %s", 
                        asicContainerDataRef.getStorageIdReference()));
            }
            
            DomibusConnectorBigDataReference xmlTokenDataRef = bigDataPersistenceService.getReadableDataSource(asicsAttachment.getAttachment());
            InputStream tokenStream; /// = xmlTokenDataRef.getInputStream();
            try {
                tokenStream = xmlTokenDataRef.getInputStream();
            } catch (IOException ioe) {
                throw new RuntimeException(String.format("error while initializing xmlTokenDataRef get input stream from %s",
                        xmlTokenDataRef.getStorageIdReference()));
            }
                    
//                    
//            DomibusConnectorBigDataReference xmlTokenDataRef = bigDataPersistenceService.getReadableDataSource(asicsAttachment.getAttachment());
//            InputStream tokenStream = xmlTokenDataRef.getInputStream();
//            
//            InputStream asicInputStream = new ByteArrayInputStream(asicsAttachment.getAttachment());
//            InputStream tokenStream = new ByteArrayInputStream(tokenXMLAttachment.getAttachment());

            try {
                ECodexContainer container = containerService.receive(asicInputStream, tokenStream);

                // KlarA: Added check of the container and the respective
                // error-handling
                CheckResult results = containerService.check(container);

                DomibusConnectorMessageDocumentBuilder documentBuilder = DomibusConnectorMessageDocumentBuilder.createBuilder();
                DetachedSignatureBuilder detachedSignatureBuilder = DetachedSignatureBuilder.createBuilder();
                if (results.isSuccessful()) {
                    if (container != null) {
                        
                        LOGGER.trace("recieveContainerContents: check if businessContent contains detachedSignature [{}]", 
                                container.getBusinessContent().getDetachedSignature() != null);
                        if (container.getBusinessContent().getDetachedSignature() != null) {
                            try {
                                InputStream is = container.getBusinessContent().getDetachedSignature().openStream();
                                byte[] docAsBytes = new byte[is.available()];
                                is.read(docAsBytes);                                
                                detachedSignatureBuilder.setSignature(docAsBytes);
                                LOGGER.trace("recieveContainerContents: Writing detachedSignature [{}]", IOUtils.toString(docAsBytes, "UTF8"));
                            } catch (IOException e) {
                                throw new DomibusConnectorSecurityException("Could not read detached signature!");
                            }
                            if (!StringUtils.isEmpty(container.getBusinessContent().getDetachedSignature().getName())) {                                
                                detachedSignatureBuilder.setName(container.getBusinessContent().getDetachedSignature().getName());
                                LOGGER.trace("recieveContainerContents: detachedSignature has name [{}]", container.getBusinessContent().getDetachedSignature().getName());
                            }
                            try {
                                LOGGER.trace("recieveContainerContents: detachedSignature has mimeType [{}]", 
                                        container.getBusinessContent().getDetachedSignature().getMimeType().getMimeTypeString());
                                
                                detachedSignatureBuilder.setMimeType(DetachedSignatureMimeType.valueOf(container.getBusinessContent()
                                                .getDetachedSignature().getMimeType().getMimeTypeString()));
                                
                            } catch (IllegalArgumentException e) {
                                LOGGER.error("recieveContainerContents: No DetachedSignatureMimeType could be resolved of MimeType [{}], using default MimeType [{}]", 
                                        container.getBusinessContent().getDetachedSignature().getMimeType().getMimeTypeString(),
                                        DetachedSignatureMimeType.BINARY.getCode());
                                detachedSignatureBuilder.setMimeType(DetachedSignatureMimeType.BINARY);
                            }
                            //set detached signature                            
                            documentBuilder.withDetachedSignature(detachedSignatureBuilder.build());
                        }
                        

                        if (container.getBusinessDocument() != null) {
                            LOGGER.debug("The business document received from the container is of Mime Type {}",
                                    container.getBusinessDocument().getMimeType());
                            try {
                            	byte[] docAsBytes = IOUtils.toByteArray(container.getBusinessDocument().openStream());
                                //LOGGER.trace("recieveContainerContents: Read following byte content [{}]", IOUtils.toString(docAsBytes, "UTF8"));
                                DomibusConnectorBigDataReference bigDataRef = this.bigDataPersistenceService.createDomibusConnectorBigDataReference(message);
                                
                                LOGGER.trace("copying businessDocument input stream to bigDataReference output Stream");
                                InputStream inputStream = container.getBusinessDocument().openStream();
                                OutputStream outputStream = bigDataRef.getOutputStream();
                                StreamUtils.copy(inputStream, outputStream);
                                
                                documentBuilder.setContent(bigDataRef);
                                
                                LOGGER.trace("recieveContainerContents: check if MimeType.PDF [{}] equals to [{}]", 
                                        MimeType.PDF.getMimeTypeString(), container.getToken().getDocumentType());
                                                                
                                if (MimeType.PDF.getMimeTypeString().equals(container.getToken().getDocumentType())) {                                                                        
                                    String docName = MAIN_DOCUMENT_NAME;
                                    if (!StringUtils.isEmpty(container.getBusinessDocument().getName())) {
                                        docName = container.getBusinessDocument().getName();
                                    }
                                    documentBuilder.setName(docName);
                                    message.getMessageContent().setDocument(documentBuilder.build());
                                }
                                
                                LOGGER.trace("recieveContainerContents: check if MimeType.XML [{}] equals to [{}]", 
                                        MimeType.XML.getMimeTypeString(), container.getToken().getDocumentType());
                                
                                if (MimeType.XML.getMimeTypeString().equals(container.getToken().getDocumentType())
                                        && !message.getMessageDetails().getAction().isDocumentRequired()) {
                                    LOGGER.trace("recieveContainerContents: Writing byteContent into MessageContent.setXmlContent");
                                    message.getMessageContent().setXmlContent(docAsBytes);
                                }
                            } catch (IOException e) {
                                throw new DomibusConnectorSecurityException("Could not read business document!");
                            }

                        } else {
                            LOGGER.debug("The business document received from the container is null!");
                        }
                        
                        

                        if (container.getBusinessAttachments() != null && !container.getBusinessAttachments().isEmpty()) {
                            for (DSSDocument businessAttachment : container.getBusinessAttachments()) {
                                try {
                                    DomibusConnectorMessageAttachment attachment = convertDocumentToMessageAttachment(
                                            message, 
                                            businessAttachment, 
                                            businessAttachment.getName());

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
                                tokenPDF.setMimeType(MimeType.PDF);
                                tokenPDF.setName(TOKEN_PDF_FILE_NAME);
                                DomibusConnectorMessageAttachment attachment = convertDocumentToMessageAttachment(message, tokenPDF, TOKEN_PDF_IDENTIFIER);
                                message.addAttachment(attachment);
                            } catch (IOException e) {
                                LOGGER.error("Could not read Token PDF!", e);
                            }
                        }

                        DSSDocument tokenXML = container.getTokenXML();
                        if (tokenXML != null) {
                            try {
                                tokenXML.setMimeType(MimeType.XML);
                                tokenXML.setName(TOKEN_XML_FILE_NAME);
                                DomibusConnectorMessageAttachment attachment = convertDocumentToMessageAttachment(message, tokenXML, TOKEN_XML_IDENTIFIER); 
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

    private DomibusConnectorMessageAttachment convertDocumentToMessageAttachment(DomibusConnectorMessage message, DSSDocument document, String identifier) //, String name, String mimeType)
            throws IOException {
        LOGGER.trace("convertDocumentToMessageAttachment: called");
        
        DomibusConnectorBigDataReference bigDataRef = bigDataPersistenceService.createDomibusConnectorBigDataReference(message);
        
        String documentName = document.getName();
        String mimeTypeString = document.getMimeType().getMimeTypeString();
        
        bigDataRef.setName(documentName);
        bigDataRef.setMimetype(mimeTypeString);
        
        LOGGER.debug("Copy input stream from dss document to output stream of big data reference");
        InputStream inputStream = document.openStream();
        OutputStream outputStream = bigDataRef.getOutputStream();
        StreamUtils.copy(inputStream, outputStream);
        
//        document.getMimeType();
//        document.getName();
//    	
//    	byte[] byteArray = IOUtils.toByteArray(document.openStream());
//    	
//    	if(ArrayUtils.isEmpty(byteArray)){
//    		throw new DomibusConnectorSecurityException("Cannot create attachment without content data!");
//    	}
    	if(StringUtils.isEmpty(identifier)){
    		throw new DomibusConnectorSecurityException("Cannot create attachment without identifier!");
    	}
    	
        DomibusConnectorMessageAttachment attachment = new DomibusConnectorMessageAttachment(bigDataRef, identifier);
        
		attachment.setName(documentName);
        attachment.setMimeType(mimeTypeString);

        return attachment;
    }

    
    /**
     * in later versions this method should return a streaming based DSSDocument
     * so no conversion to an byte[] is necessary
     * @param dataRef - the reference to the data, the reference id must be set!
     * @param name name of the dssDocument
     * @param mimeType mimeType of the dssDocument
     * @return the created InMemoryDocument
     */
    DSSDocument createInMemoryDocument(DomibusConnectorBigDataReference dataRef, String name, MimeType mimeType) {
        try {
            DomibusConnectorBigDataReference readableDataSource = bigDataPersistenceService.getReadableDataSource(dataRef);
            InputStream inputStream = readableDataSource.getInputStream();
            byte[] content = StreamUtils.copyToByteArray(inputStream);
            InMemoryDocument dssDocument = new InMemoryDocument(content, name, MimeType.PDF);
            return dssDocument;
        } catch (IOException ioe) {
            throw new RuntimeException("error while loading data from bigDataPersistenceService", ioe);
        }
    }
    
    
}
