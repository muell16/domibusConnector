
package org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java-Klasse für CollaborationInfo complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CollaborationInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AgreementRef" type="{http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/}AgreementRef" minOccurs="0"/>
 *         &lt;element name="Service" type="{http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/}Service"/>
 *         &lt;element name="Action" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *         &lt;element name="ConversationId" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CollaborationInfo", propOrder = {
    "agreementRef",
    "service",
    "action",
    "conversationId"
})
public class CollaborationInfo {

    @XmlElement(name = "AgreementRef")
    protected AgreementRef agreementRef;
    @XmlElement(name = "Service", required = true)
    protected Service service;
    @XmlElement(name = "Action", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String action;
    @XmlElement(name = "ConversationId", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String conversationId;

    /**
     * Ruft den Wert der agreementRef-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AgreementRef }
     *     
     */
    public AgreementRef getAgreementRef() {
        return agreementRef;
    }

    /**
     * Legt den Wert der agreementRef-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AgreementRef }
     *     
     */
    public void setAgreementRef(AgreementRef value) {
        this.agreementRef = value;
    }

    /**
     * Ruft den Wert der service-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Service }
     *     
     */
    public Service getService() {
        return service;
    }

    /**
     * Legt den Wert der service-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Service }
     *     
     */
    public void setService(Service value) {
        this.service = value;
    }

    /**
     * Ruft den Wert der action-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAction() {
        return action;
    }

    /**
     * Legt den Wert der action-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAction(String value) {
        this.action = value;
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

}
