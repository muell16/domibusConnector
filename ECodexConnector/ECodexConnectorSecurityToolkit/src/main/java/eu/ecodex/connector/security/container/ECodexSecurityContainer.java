package eu.ecodex.connector.security.container;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageAttachment;
import eu.ecodex.connector.security.exception.ECodexConnectorSecurityException;
import eu.ecodex.dss.model.BusinessContent;
import eu.ecodex.dss.model.Document;
import eu.ecodex.dss.model.ECodexContainer;
import eu.ecodex.dss.model.MemoryDocument;
import eu.ecodex.dss.model.MimeType;
import eu.ecodex.dss.model.SignatureParameters;
import eu.ecodex.dss.model.token.TokenIssuer;
import eu.ecodex.dss.service.ECodexContainerService;
import eu.ecodex.dss.service.ECodexException;
import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.SignatureAlgorithm;

public class ECodexSecurityContainer implements InitializingBean {

    static Logger LOGGER = LoggerFactory.getLogger(ECodexSecurityContainer.class);

    ECodexContainerService containerService;
    TokenIssuer tokenIssuer;

    private String javaKeyStorePath;
    private String javaKeyStorePassword;
    private String keyAlias;
    private String keyPassword;

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

        KeyStore ks = KeyStore.getInstance("JKS");
        FileInputStream kfis = new FileInputStream(javaKeyStorePath);
        ks.load(kfis, javaKeyStorePassword.toCharArray());

        PrivateKey privKey = (PrivateKey) ks.getKey(keyAlias, keyPassword.toCharArray());
        java.security.cert.X509Certificate cert = (java.security.cert.X509Certificate) ks.getCertificate(keyAlias);
        Certificate[] certs = ks.getCertificateChain(keyAlias);

        final SignatureParameters mySignatureParameters = new SignatureParameters();

        mySignatureParameters.setPrivateKey(privKey);

        mySignatureParameters.setCertificate(cert);

        final List<X509Certificate> x509Certs = new ArrayList<X509Certificate>();

        for (final Certificate certificate : certs) {
            if (certificate instanceof X509Certificate) {
                x509Certs.add((X509Certificate) certificate);
            }
        }

        mySignatureParameters.setCertificateChain(x509Certs.toArray(new X509Certificate[x509Certs.size()]));

        mySignatureParameters.setDigestAlgorithm(DigestAlgorithm.SHA1.getName());
        mySignatureParameters.setSignatureAlgorithm(SignatureAlgorithm.RSA.getName());

        return mySignatureParameters;

    }

    private BusinessContent buildBusinessContent(Message message) {
        BusinessContent businessContent = new BusinessContent();

        Document document = new MemoryDocument(message.getMessageContent().getPdfDocument(), "mainDocument",
                MimeType.PDF);
        businessContent.setDocument(document);

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
                        MessageAttachment tokenAttachment = convertDocumentToMessageAttachment(tokenXML, "tokenXML",
                                MimeType.XML.getCode());
                        message.addAttachment(tokenAttachment);
                    } catch (IOException e) {
                        throw new ECodexConnectorSecurityException(e);
                    }
                }
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
                attachment.getName().equals("tokenXML")) {
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
                if (container != null) {
                    if (container.getBusinessDocument() != null) {
                        try {
                            InputStream is = container.getBusinessDocument().openStream();
                            byte[] docAsBytes = new byte[is.available()];
                            is.read(docAsBytes);
                            message.getMessageContent().setPdfDocument(docAsBytes);
                        } catch (IOException e) {
                            throw new ECodexConnectorSecurityException("Could not read business document!");
                        }

                    }
                    if (container.getBusinessAttachments() != null && !container.getBusinessAttachments().isEmpty()) {
                        for (Document businessAttachment : container.getBusinessAttachments()) {
                            try {
                                MessageAttachment attachment = convertDocumentToMessageAttachment(businessAttachment,
                                        businessAttachment.getName(), businessAttachment.getMimeType().getCode());
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
                            MessageAttachment attachment = convertDocumentToMessageAttachment(tokenPDF, "Token.pdf",
                                    MimeType.PDF.getCode());
                            message.addAttachment(attachment);
                        } catch (IOException e) {
                            LOGGER.error("Could not read Token PDF!", e);
                        }
                    }

                    Document tokenXML = container.getTokenXML();
                    if (tokenXML != null) {
                        try {
                            MessageAttachment attachment = convertDocumentToMessageAttachment(tokenXML, "Token.xml",
                                    MimeType.XML.getCode());
                            message.addAttachment(attachment);
                        } catch (IOException e) {
                            LOGGER.error("Could not read Token XML!", e);
                        }
                    }
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
