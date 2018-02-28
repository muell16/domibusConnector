
package eu.domibus.connector.domain.transition;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für DomibusConnectorMessageType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="DomibusConnectorMessageType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="messageContent" type="{transition.domain.connector.domibus.eu}DomibusConnectorMessageContentType" minOccurs="0"/&gt;
 *         &lt;element name="messageDetails" type="{transition.domain.connector.domibus.eu}DomibusConnectorMessageDetailsType"/&gt;
 *         &lt;element name="messageConfirmations" type="{transition.domain.connector.domibus.eu}DomibusConnectorMessageConfirmationType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="messageAttachments" type="{transition.domain.connector.domibus.eu}DomibusConnectorMessageAttachmentType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="messageErrors" type="{transition.domain.connector.domibus.eu}DomibusConnectorMessageErrorType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DomibusConnectorMessageType", propOrder = {
    "messageContent",
    "messageDetails",
    "messageConfirmations",
    "messageAttachments",
    "messageErrors"
})
public class DomibusConnectorMessageType {

    protected DomibusConnectorMessageContentType messageContent;
    @XmlElement(required = true)
    protected DomibusConnectorMessageDetailsType messageDetails;
    protected List<DomibusConnectorMessageConfirmationType> messageConfirmations;
    protected List<DomibusConnectorMessageAttachmentType> messageAttachments;
    protected List<DomibusConnectorMessageErrorType> messageErrors;

    /**
     * Ruft den Wert der messageContent-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DomibusConnectorMessageContentType }
     *     
     */
    public DomibusConnectorMessageContentType getMessageContent() {
        return messageContent;
    }

    /**
     * Legt den Wert der messageContent-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DomibusConnectorMessageContentType }
     *     
     */
    public void setMessageContent(DomibusConnectorMessageContentType value) {
        this.messageContent = value;
    }

    /**
     * Ruft den Wert der messageDetails-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DomibusConnectorMessageDetailsType }
     *     
     */
    public DomibusConnectorMessageDetailsType getMessageDetails() {
        return messageDetails;
    }

    /**
     * Legt den Wert der messageDetails-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DomibusConnectorMessageDetailsType }
     *     
     */
    public void setMessageDetails(DomibusConnectorMessageDetailsType value) {
        this.messageDetails = value;
    }

    /**
     * Gets the value of the messageConfirmations property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the messageConfirmations property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessageConfirmations().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DomibusConnectorMessageConfirmationType }
     * 
     * 
     */
    public List<DomibusConnectorMessageConfirmationType> getMessageConfirmations() {
        if (messageConfirmations == null) {
            messageConfirmations = new ArrayList<DomibusConnectorMessageConfirmationType>();
        }
        return this.messageConfirmations;
    }

    /**
     * Gets the value of the messageAttachments property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the messageAttachments property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessageAttachments().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DomibusConnectorMessageAttachmentType }
     * 
     * 
     */
    public List<DomibusConnectorMessageAttachmentType> getMessageAttachments() {
        if (messageAttachments == null) {
            messageAttachments = new ArrayList<DomibusConnectorMessageAttachmentType>();
        }
        return this.messageAttachments;
    }

    /**
     * Gets the value of the messageErrors property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the messageErrors property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessageErrors().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DomibusConnectorMessageErrorType }
     * 
     * 
     */
    public List<DomibusConnectorMessageErrorType> getMessageErrors() {
        if (messageErrors == null) {
            messageErrors = new ArrayList<DomibusConnectorMessageErrorType>();
        }
        return this.messageErrors;
    }

}
