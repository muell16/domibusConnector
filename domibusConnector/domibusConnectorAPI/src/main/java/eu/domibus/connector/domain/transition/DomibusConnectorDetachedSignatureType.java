
package eu.domibus.connector.domain.transition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für DomibusConnectorDetachedSignatureType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="DomibusConnectorDetachedSignatureType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="detachedSignature" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *         &lt;element name="detachedSignatureName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="mimeType" type="{transition.domain.connector.domibus.eu}DomibusConnectorDetachedSignatureMimeType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DomibusConnectorDetachedSignatureType", propOrder = {
    "detachedSignature",
    "detachedSignatureName",
    "mimeType"
})
public class DomibusConnectorDetachedSignatureType {

    @XmlElement(required = true)
    protected byte[] detachedSignature;
    @XmlElement(required = true)
    protected String detachedSignatureName;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected DomibusConnectorDetachedSignatureMimeType mimeType;

    /**
     * Ruft den Wert der detachedSignature-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getDetachedSignature() {
        return detachedSignature;
    }

    /**
     * Legt den Wert der detachedSignature-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setDetachedSignature(byte[] value) {
        this.detachedSignature = value;
    }

    /**
     * Ruft den Wert der detachedSignatureName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetachedSignatureName() {
        return detachedSignatureName;
    }

    /**
     * Legt den Wert der detachedSignatureName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetachedSignatureName(String value) {
        this.detachedSignatureName = value;
    }

    /**
     * Ruft den Wert der mimeType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DomibusConnectorDetachedSignatureMimeType }
     *     
     */
    public DomibusConnectorDetachedSignatureMimeType getMimeType() {
        return mimeType;
    }

    /**
     * Legt den Wert der mimeType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DomibusConnectorDetachedSignatureMimeType }
     *     
     */
    public void setMimeType(DomibusConnectorDetachedSignatureMimeType value) {
        this.mimeType = value;
    }

}
