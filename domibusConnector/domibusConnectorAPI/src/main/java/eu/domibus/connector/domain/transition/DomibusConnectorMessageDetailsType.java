
package eu.domibus.connector.domain.transition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für DomibusConnectorMessageDetailsType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="DomibusConnectorMessageDetailsType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="backendMessageId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="refToMessageId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="conversationId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="originalSender" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="finalRecipient" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="service" type="{transition.domain.connector.domibus.eu}DomibusConnectorServiceType"/&gt;
 *         &lt;element name="action" type="{transition.domain.connector.domibus.eu}DomibusConnectorActionType"/&gt;
 *         &lt;element name="fromParty" type="{transition.domain.connector.domibus.eu}DomibusConnectorPartyType"/&gt;
 *         &lt;element name="toParty" type="{transition.domain.connector.domibus.eu}DomibusConnectorPartyType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DomibusConnectorMessageDetailsType", propOrder = {
    "backendMessageId",
    "refToMessageId",
    "conversationId",
    "originalSender",
    "finalRecipient",
    "service",
    "action",
    "fromParty",
    "toParty"
})
public class DomibusConnectorMessageDetailsType {

    protected String backendMessageId;
    protected String refToMessageId;
    protected String conversationId;
    @XmlElement(required = true)
    protected String originalSender;
    @XmlElement(required = true)
    protected String finalRecipient;
    @XmlElement(required = true)
    protected DomibusConnectorServiceType service;
    @XmlElement(required = true)
    protected DomibusConnectorActionType action;
    @XmlElement(required = true)
    protected DomibusConnectorPartyType fromParty;
    @XmlElement(required = true)
    protected DomibusConnectorPartyType toParty;

    /**
     * Ruft den Wert der backendMessageId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBackendMessageId() {
        return backendMessageId;
    }

    /**
     * Legt den Wert der backendMessageId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBackendMessageId(String value) {
        this.backendMessageId = value;
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
     * Ruft den Wert der conversationId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConversationId() {
        return conversationId;
    }

    /**
     * Legt den Wert der conversationId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConversationId(String value) {
        this.conversationId = value;
    }

    /**
     * Ruft den Wert der originalSender-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalSender() {
        return originalSender;
    }

    /**
     * Legt den Wert der originalSender-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalSender(String value) {
        this.originalSender = value;
    }

    /**
     * Ruft den Wert der finalRecipient-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinalRecipient() {
        return finalRecipient;
    }

    /**
     * Legt den Wert der finalRecipient-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinalRecipient(String value) {
        this.finalRecipient = value;
    }

    /**
     * Ruft den Wert der service-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DomibusConnectorServiceType }
     *     
     */
    public DomibusConnectorServiceType getService() {
        return service;
    }

    /**
     * Legt den Wert der service-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DomibusConnectorServiceType }
     *     
     */
    public void setService(DomibusConnectorServiceType value) {
        this.service = value;
    }

    /**
     * Ruft den Wert der action-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DomibusConnectorActionType }
     *     
     */
    public DomibusConnectorActionType getAction() {
        return action;
    }

    /**
     * Legt den Wert der action-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DomibusConnectorActionType }
     *     
     */
    public void setAction(DomibusConnectorActionType value) {
        this.action = value;
    }

    /**
     * Ruft den Wert der fromParty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DomibusConnectorPartyType }
     *     
     */
    public DomibusConnectorPartyType getFromParty() {
        return fromParty;
    }

    /**
     * Legt den Wert der fromParty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DomibusConnectorPartyType }
     *     
     */
    public void setFromParty(DomibusConnectorPartyType value) {
        this.fromParty = value;
    }

    /**
     * Ruft den Wert der toParty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DomibusConnectorPartyType }
     *     
     */
    public DomibusConnectorPartyType getToParty() {
        return toParty;
    }

    /**
     * Legt den Wert der toParty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DomibusConnectorPartyType }
     *     
     */
    public void setToParty(DomibusConnectorPartyType value) {
        this.toParty = value;
    }

}
