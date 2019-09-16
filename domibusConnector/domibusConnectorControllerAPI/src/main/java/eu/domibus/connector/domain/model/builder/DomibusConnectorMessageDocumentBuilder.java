
package eu.domibus.connector.domain.model.builder;

import eu.domibus.connector.domain.model.DetachedSignature;
import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDocument;
import javax.annotation.Nonnull;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public final class DomibusConnectorMessageDocumentBuilder {


    private DomibusConnectorBigDataReference documentContent;
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
    
    public DomibusConnectorMessageDocumentBuilder setContent(@Nonnull DomibusConnectorBigDataReference documentContent) {
        this.documentContent = documentContent;
        return this;
    }
    
    public DomibusConnectorMessageDocumentBuilder withDetachedSignature(DetachedSignature signature) {
        this.detachedSignature = signature;
        return this;
    }
    
    public DomibusConnectorMessageDocumentBuilder copyPropertiesFrom(DomibusConnectorMessageDocument doc) {
        this.detachedSignature = doc.getDetachedSignature();
        this.documentContent = doc.getDocument();
        this.documentName = doc.getDocumentName();
        return this;
    }
        
    public DomibusConnectorMessageDocument build() {                
        if (documentName == null) {
            throw new IllegalArgumentException("documentName can not be null!");
        }
        if (documentContent == null) {
            throw new IllegalArgumentException("documentContent can not be null!");
        }
        DomibusConnectorMessageDocument doc = new DomibusConnectorMessageDocument(documentContent, documentName, detachedSignature);
        return doc;
    }
    
}
