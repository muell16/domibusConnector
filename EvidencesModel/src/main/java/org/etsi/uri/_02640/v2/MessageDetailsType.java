//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2012.08.21 um 12:08:20 PM CEST 
//


package org.etsi.uri._02640.v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.w3._2000._09.xmldsig.DigestMethodType;


/**
 * <p>Java-Klasse für MessageDetailsType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="MessageDetailsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MessageSubject" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UAMessageIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MessageIdentifierByREMMD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}DigestMethod" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}DigestValue" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="isNotification" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageDetailsType", propOrder = {
    "messageSubject",
    "uaMessageIdentifier",
    "messageIdentifierByREMMD",
    "digestMethod",
    "digestValue"
})
public class MessageDetailsType {

    @XmlElement(name = "MessageSubject", required = true)
    protected String messageSubject;
    @XmlElement(name = "UAMessageIdentifier")
    protected String uaMessageIdentifier;
    @XmlElement(name = "MessageIdentifierByREMMD", required = true)
    protected String messageIdentifierByREMMD;
    @XmlElement(name = "DigestMethod", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected DigestMethodType digestMethod;
    @XmlElement(name = "DigestValue", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected byte[] digestValue;
    @XmlAttribute(name = "isNotification", required = true)
    protected boolean isNotification;

    /**
     * Ruft den Wert der messageSubject-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageSubject() {
        return messageSubject;
    }

    /**
     * Legt den Wert der messageSubject-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageSubject(String value) {
        this.messageSubject = value;
    }

    /**
     * Ruft den Wert der uaMessageIdentifier-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUAMessageIdentifier() {
        return uaMessageIdentifier;
    }

    /**
     * Legt den Wert der uaMessageIdentifier-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUAMessageIdentifier(String value) {
        this.uaMessageIdentifier = value;
    }

    /**
     * Ruft den Wert der messageIdentifierByREMMD-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageIdentifierByREMMD() {
        return messageIdentifierByREMMD;
    }

    /**
     * Legt den Wert der messageIdentifierByREMMD-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageIdentifierByREMMD(String value) {
        this.messageIdentifierByREMMD = value;
    }

    /**
     * Ruft den Wert der digestMethod-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DigestMethodType }
     *     
     */
    public DigestMethodType getDigestMethod() {
        return digestMethod;
    }

    /**
     * Legt den Wert der digestMethod-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DigestMethodType }
     *     
     */
    public void setDigestMethod(DigestMethodType value) {
        this.digestMethod = value;
    }

    /**
     * Ruft den Wert der digestValue-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getDigestValue() {
        return digestValue;
    }

    /**
     * Legt den Wert der digestValue-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setDigestValue(byte[] value) {
        this.digestValue = value;
    }

    /**
     * Ruft den Wert der isNotification-Eigenschaft ab.
     * 
     */
    public boolean isIsNotification() {
        return isNotification;
    }

    /**
     * Legt den Wert der isNotification-Eigenschaft fest.
     * 
     */
    public void setIsNotification(boolean value) {
        this.isNotification = value;
    }

}
