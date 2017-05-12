
package connector.domibus.eu.domibusconnectorgatewayservice._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für MessageDetailsType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
     * Ruft den Wert der messageId-Eigenschaft ab.
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
     * Legt den Wert der messageId-Eigenschaft fest.
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
     * Ruft den Wert der refToMessageId-Eigenschaft ab.
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
     * Legt den Wert der refToMessageId-Eigenschaft fest.
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
     * Ruft den Wert der partyInfo-Eigenschaft ab.
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
     * Legt den Wert der partyInfo-Eigenschaft fest.
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
     * Ruft den Wert der collaborationInfo-Eigenschaft ab.
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
     * Legt den Wert der collaborationInfo-Eigenschaft fest.
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
     * Ruft den Wert der messageProperties-Eigenschaft ab.
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
     * Legt den Wert der messageProperties-Eigenschaft fest.
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
