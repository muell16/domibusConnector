
package connector.domibus.eu.domibusconnectorgatewayservice._1;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für MessageAttachmentType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="MessageAttachmentType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="attachmentName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="attachmentData" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *         &lt;element name="attachmentIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="attachmentDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="attachmentMimeType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageAttachmentType", propOrder = {
    "attachmentName",
    "attachmentData",
    "attachmentIdentifier",
    "attachmentDescription",
    "attachmentMimeType"
})
public class MessageAttachmentType {

    @XmlElement(required = true)
    protected String attachmentName;
    @XmlElement(required = true)
    @XmlMimeType("application/octet-stream")
    protected DataHandler attachmentData;
    protected String attachmentIdentifier;
    protected String attachmentDescription;
    @XmlElement(required = true)
    protected String attachmentMimeType;

    /**
     * Ruft den Wert der attachmentName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttachmentName() {
        return attachmentName;
    }

    /**
     * Legt den Wert der attachmentName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttachmentName(String value) {
        this.attachmentName = value;
    }

    /**
     * Ruft den Wert der attachmentData-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public DataHandler getAttachmentData() {
        return attachmentData;
    }

    /**
     * Legt den Wert der attachmentData-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setAttachmentData(DataHandler value) {
        this.attachmentData = value;
    }

    /**
     * Ruft den Wert der attachmentIdentifier-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttachmentIdentifier() {
        return attachmentIdentifier;
    }

    /**
     * Legt den Wert der attachmentIdentifier-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttachmentIdentifier(String value) {
        this.attachmentIdentifier = value;
    }

    /**
     * Ruft den Wert der attachmentDescription-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttachmentDescription() {
        return attachmentDescription;
    }

    /**
     * Legt den Wert der attachmentDescription-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttachmentDescription(String value) {
        this.attachmentDescription = value;
    }

    /**
     * Ruft den Wert der attachmentMimeType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttachmentMimeType() {
        return attachmentMimeType;
    }

    /**
     * Legt den Wert der attachmentMimeType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttachmentMimeType(String value) {
        this.attachmentMimeType = value;
    }

}
