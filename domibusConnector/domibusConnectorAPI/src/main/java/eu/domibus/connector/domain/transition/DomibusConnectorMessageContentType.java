
package eu.domibus.connector.domain.transition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.Source;


/**
 * <p>Java-Klasse für DomibusConnectorMessageContentType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="DomibusConnectorMessageContentType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="xmlContent" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *         &lt;element name="document" type="{transition.domain.connector.domibus.eu}DomibusConnectorMessageDocumentType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DomibusConnectorMessageContentType", propOrder = {
    "xmlContent",
    "document"
})
public class DomibusConnectorMessageContentType {

    @XmlElement(required = true)
    @XmlMimeType("application/xml")
    protected Source xmlContent;
    protected DomibusConnectorMessageDocumentType document;

    /**
     * Ruft den Wert der xmlContent-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Source }
     *     
     */
    public Source getXmlContent() {
        return xmlContent;
    }

    /**
     * Legt den Wert der xmlContent-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Source }
     *     
     */
    public void setXmlContent(Source value) {
        this.xmlContent = value;
    }

    /**
     * Ruft den Wert der document-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DomibusConnectorMessageDocumentType }
     *     
     */
    public DomibusConnectorMessageDocumentType getDocument() {
        return document;
    }

    /**
     * Legt den Wert der document-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DomibusConnectorMessageDocumentType }
     *     
     */
    public void setDocument(DomibusConnectorMessageDocumentType value) {
        this.document = value;
    }

}
