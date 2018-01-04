/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.persistence.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Entity
@Table(name="DOMIBUS_CONNECTOR_MESSAGE_CONTENT")
public class PersistedMessageContent implements Serializable {

    @Id
    @TableGenerator(name = "seqStoreMessageContent", table = "DOMIBUS_CONNECTOR_MESSAGE_CONTENT", pkColumnName = "SEQ_NAME", pkColumnValue = "DOMIBUS_CONNECTOR_MESSAGE_CONTENT.ID", valueColumnName = "SEQ_VALUE", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seqStoreMessageContent")
    @Column(name="ID")
    private Long id;
    
    @Column(name = "XML_CONTENT")
    @Lob
    private byte[] xmlContent;
    
    @Column(name = "DOC_DOCUMENT_CONTENT")
    @Lob
    private byte[] document;
    
    @Column(name = "DOC_DOCUMENT_NAME")
    private String documentName;
    
    @Column(name = "DOC_HASH_VALUE")
    private String hashValue;
    
    @Column(name = "SIG_DETACHED_SIGNATURE")
    @Lob
    private byte[] detachedSignature;
    
    @Column(name = "SIG_DETACHED_SIGNATURE_NAME")
    private String detachedSignatureName;
    
    @Column(name = "SIG_MIME_TYPE")
    private String detachedSignatureMimeType;

    @OneToOne(optional = false)
    @JoinColumn(name = "MESSAGE_ID")
    private DomibusConnectorMessage message;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getXmlContent() {
        return xmlContent;
    }

    public void setXmlContent(byte[] xmlContent) {
        this.xmlContent = xmlContent;
    }

    public byte[] getDocument() {
        return document;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    public byte[] getDetachedSignature() {
        return detachedSignature;
    }

    public void setDetachedSignature(byte[] detachedSignature) {
        this.detachedSignature = detachedSignature;
    }

    public String getDetachedSignatureName() {
        return detachedSignatureName;
    }

    public void setDetachedSignatureName(String detachedSignatureName) {
        this.detachedSignatureName = detachedSignatureName;
    }

    public String getDetachedSignatureMimeType() {
        return detachedSignatureMimeType;
    }

    public void setDetachedSignatureMimeType(String detachedSignatureMimeType) {
        this.detachedSignatureMimeType = detachedSignatureMimeType;
    }
    
    
    
}
