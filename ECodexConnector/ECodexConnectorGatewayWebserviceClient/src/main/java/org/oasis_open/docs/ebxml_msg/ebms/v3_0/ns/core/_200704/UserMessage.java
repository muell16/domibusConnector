
package org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für UserMessage complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="UserMessage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MessageInfo" type="{http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/}MessageInfo"/>
 *         &lt;element name="PartyInfo" type="{http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/}PartyInfo"/>
 *         &lt;element name="CollaborationInfo" type="{http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/}CollaborationInfo"/>
 *         &lt;element name="MessageProperties" type="{http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/}MessageProperties" minOccurs="0"/>
 *         &lt;element name="PayloadInfo" type="{http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/}PayloadInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="mpc" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserMessage", propOrder = {
    "messageInfo",
    "partyInfo",
    "collaborationInfo",
    "messageProperties",
    "payloadInfo"
})
public class UserMessage {

    @XmlElement(name = "MessageInfo", required = true)
    protected MessageInfo messageInfo;
    @XmlElement(name = "PartyInfo", required = true)
    protected PartyInfo partyInfo;
    @XmlElement(name = "CollaborationInfo", required = true)
    protected CollaborationInfo collaborationInfo;
    @XmlElement(name = "MessageProperties")
    protected MessageProperties messageProperties;
    @XmlElement(name = "PayloadInfo")
    protected PayloadInfo payloadInfo;
    @XmlAttribute(name = "mpc")
    @XmlSchemaType(name = "anyURI")
    protected String mpc;

    /**
     * Ruft den Wert der messageInfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MessageInfo }
     *     
     */
    public MessageInfo getMessageInfo() {
        return messageInfo;
    }

    /**
     * Legt den Wert der messageInfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageInfo }
     *     
     */
    public void setMessageInfo(MessageInfo value) {
        this.messageInfo = value;
    }

    /**
     * Ruft den Wert der partyInfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PartyInfo }
     *     
     */
    public PartyInfo getPartyInfo() {
        return partyInfo;
    }

    /**
     * Legt den Wert der partyInfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PartyInfo }
     *     
     */
    public void setPartyInfo(PartyInfo value) {
        this.partyInfo = value;
    }

    /**
     * Ruft den Wert der collaborationInfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CollaborationInfo }
     *     
     */
    public CollaborationInfo getCollaborationInfo() {
        return collaborationInfo;
    }

    /**
     * Legt den Wert der collaborationInfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CollaborationInfo }
     *     
     */
    public void setCollaborationInfo(CollaborationInfo value) {
        this.collaborationInfo = value;
    }

    /**
     * Ruft den Wert der messageProperties-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MessageProperties }
     *     
     */
    public MessageProperties getMessageProperties() {
        return messageProperties;
    }

    /**
     * Legt den Wert der messageProperties-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageProperties }
     *     
     */
    public void setMessageProperties(MessageProperties value) {
        this.messageProperties = value;
    }

    /**
     * Ruft den Wert der payloadInfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PayloadInfo }
     *     
     */
    public PayloadInfo getPayloadInfo() {
        return payloadInfo;
    }

    /**
     * Legt den Wert der payloadInfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PayloadInfo }
     *     
     */
    public void setPayloadInfo(PayloadInfo value) {
        this.payloadInfo = value;
    }

    /**
     * Ruft den Wert der mpc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMpc() {
        return mpc;
    }

    /**
     * Legt den Wert der mpc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMpc(String value) {
        this.mpc = value;
    }

}
