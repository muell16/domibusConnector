/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.domain.model.builder;

import eu.domibus.connector.domain.model.DetachedSignature;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDocument;
import javax.annotation.Nonnull;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DomibusConnectorMessageDocumentBuilder {


    private byte[] documentContent;
    private String documentName;
    
    private DetachedSignature detachedSignature = null;
    
    public static DomibusConnectorMessageDocumentBuilder createBuilder() {
        return new DomibusConnectorMessageDocumentBuilder();
    }
    
    private DomibusConnectorMessageDocumentBuilder() {}


    public DomibusConnectorMessageDocumentBuilder setName(@Nonnull String documentName) {
        this.documentName = documentName;
        return this;
    }
    
    public DomibusConnectorMessageDocumentBuilder setContent(@Nonnull byte[] documentContent) {
        this.documentContent = documentContent;
        return this;
    }
    
    public DomibusConnectorMessageDocumentBuilder withDetachedSignature(DetachedSignature signature) {
        this.detachedSignature = signature;
        return this;
    }
    
        
    public DomibusConnectorMessageDocument build() {                
        if (documentName == null) {
            throw new IllegalArgumentException("documentName must not be null!");
        }
        if (documentContent == null || documentContent.length < 1) {
            throw new IllegalArgumentException("documentContent must not be null or empty!");
        }
        DomibusConnectorMessageDocument doc = new DomibusConnectorMessageDocument(documentContent, documentName, detachedSignature);
        return doc;
    }
    
}