
package connector.domibus.eu.domibusconnectorgatewayservice._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageDetailsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessageDetailsType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="messageId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="refToMessageId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="partyInfo" type="{http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/}MessagePartyInfoType"/&gt;
 *         &lt;element name="collaborationInfo" type="{http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/}MessageCollaborationInfoType"/&gt;
 *         &lt;element name="messageProperties" type="{http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/}MessagePropertiesType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageDetailsType", propOrder = {
    "messageId",
    "refToMessageId",
    "partyInfo",
    "collaborationInfo",
    "messageProperties"
})
public class MessageDetailsType {

    protected String messageId;
    protected String refToMessageId;
    @XmlElement(required = true)
    protected MessagePartyInfoType partyInfo;
    @XmlElement(required = true)
    protected MessageCollaborationInfoType collaborationInfo;
    @XmlElement(required = true)
    protected MessagePropertiesType messageProperties;

    /**
     * Gets the value of the messageId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the value of the messageId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageId(String value) {
        this.messageId = value;
    }

    /**
     * Gets the value of the refToMessageId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefToMessageId() {
        return refToMessageId;
    }

    /**
     * Sets the value of the refToMessageId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefToMessageId(String value) {
        this.refToMessageId = value;
    }

    /**
     * Gets the value of the partyInfo property.
     * 
     * @return
     *     possible object is
     *     {@link MessagePartyInfoType }
     *     
     */
    public MessagePartyInfoType getPartyInfo() {
        return partyInfo;
    }

    /**
     * Sets the value of the partyInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessagePartyInfoType }
     *     
     */
    public void setPartyInfo(MessagePartyInfoType value) {
        this.partyInfo = value;
    }

    /**
     * Gets the value of the collaborationInfo property.
     * 
     * @return
     *     possible object is
     *     {@link MessageCollaborationInfoType }
     *     
     */
    public MessageCollaborationInfoType getCollaborationInfo() {
        return collaborationInfo;
    }

    /**
     * Sets the value of the collaborationInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageCollaborationInfoType }
     *     
     */
    public void setCollaborationInfo(MessageCollaborationInfoType value) {
        this.collaborationInfo = value;
    }

    /**
     * Gets the value of the messageProperties property.
     * 
     * @return
     *     possible object is
     *     {@link MessagePropertiesType }
     *     
     */
    public MessagePropertiesType getMessageProperties() {
        return messageProperties;
    }

    /**
     * Sets the value of the messageProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessagePropertiesType }
     *     
     */
    public void setMessageProperties(MessagePropertiesType value) {
        this.messageProperties = value;
    }

}
