
package eu.domibus.connector.domain.transition;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für DomibusConnectorMessageDocumentType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="DomibusConnectorMessageDocumentType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="document" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *         &lt;element name="documentName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="detachedSignature" type="{transition.domain.connector.domibus.eu}DomibusConnectorDetachedSignatureType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DomibusConnectorMessageDocumentType", propOrder = {
    "document",
    "documentName",
    "detachedSignature"
})
public class DomibusConnectorMessageDocumentType {

    @XmlElement(required = true)
    @XmlMimeType("application/octet-stream")
    protected DataHandler document;
    @XmlElement(required = true)
    protected String documentName;
    protected DomibusConnectorDetachedSignatureType detachedSignature;

    /**
     * Ruft den Wert der document-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public DataHandler getDocument() {
        return document;
    }

    /**
     * Legt den Wert der document-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setDocument(DataHandler value) {
        this.document = value;
    }

    /**
     * Ruft den Wert der documentName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentName() {
        return documentName;
    }

    /**
     * Legt den Wert der documentName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentName(String value) {
        this.documentName = value;
    }

    /**
     * Ruft den Wert der detachedSignature-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DomibusConnectorDetachedSignatureType }
     *     
     */
    public DomibusConnectorDetachedSignatureType getDetachedSignature() {
        return detachedSignature;
    }

    /**
     * Legt den Wert der detachedSignature-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DomibusConnectorDetachedSignatureType }
     *     
     */
    public void setDetachedSignature(DomibusConnectorDetachedSignatureType value) {
        this.detachedSignature = value;
    }

}
