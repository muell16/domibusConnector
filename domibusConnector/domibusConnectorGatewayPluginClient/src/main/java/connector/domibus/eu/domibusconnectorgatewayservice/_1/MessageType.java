
package connector.domibus.eu.domibusconnectorgatewayservice._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessageType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="messageDetails" type="{http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/}MessageDetailsType"/&gt;
 *         &lt;element name="messageContent" type="{http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/}MessageContentType"/&gt;
 *         &lt;element name="messageAttachments" type="{http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/}MessageAttachmentType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageType", propOrder = {
    "messageDetails",
    "messageContent",
    "messageAttachments"
})
public class MessageType {

    @XmlElement(required = true)
    protected MessageDetailsType messageDetails;
    @XmlElement(required = true)
    protected MessageContentType messageContent;
    protected List<MessageAttachmentType> messageAttachments;

    /**
     * Gets the value of the messageDetails property.
     * 
     * @return
     *     possible object is
     *     {@link MessageDetailsType }
     *     
     */
    public MessageDetailsType getMessageDetails() {
        return messageDetails;
    }

    /**
     * Sets the value of the messageDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageDetailsType }
     *     
     */
    public void setMessageDetails(MessageDetailsType value) {
        this.messageDetails = value;
    }

    /**
     * Gets the value of the messageContent property.
     * 
     * @return
     *     possible object is
     *     {@link MessageContentType }
     *     
     */
    public MessageContentType getMessageContent() {
        return messageContent;
    }

    /**
     * Sets the value of the messageContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageContentType }
     *     
     */
    public void setMessageContent(MessageContentType value) {
        this.messageContent = value;
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
     * {@link MessageAttachmentType }
     * 
     * 
     */
    public List<MessageAttachmentType> getMessageAttachments() {
        if (messageAttachments == null) {
            messageAttachments = new ArrayList<MessageAttachmentType>();
        }
        return this.messageAttachments;
    }

}
