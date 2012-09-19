//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2012.08.21 um 12:08:20 PM CEST 
//


package org.etsi.uri._02231.v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.w3._2000._09.xmldsig.KeyValueType;


/**
 * <p>Java-Klasse für DigitalIdentityType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="DigitalIdentityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="X509Certificate" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="X509SubjectName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}KeyValue"/>
 *         &lt;element name="X509SKI" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="Other" type="{http://uri.etsi.org/02231/v2#}AnyType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DigitalIdentityType", propOrder = {
    "x509Certificate",
    "x509SubjectName",
    "keyValue",
    "x509SKI",
    "other"
})
public class DigitalIdentityType {

    @XmlElement(name = "X509Certificate")
    protected byte[] x509Certificate;
    @XmlElement(name = "X509SubjectName")
    protected String x509SubjectName;
    @XmlElement(name = "KeyValue", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected KeyValueType keyValue;
    @XmlElement(name = "X509SKI")
    protected byte[] x509SKI;
    @XmlElement(name = "Other")
    protected AnyType other;

    /**
     * Ruft den Wert der x509Certificate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getX509Certificate() {
        return x509Certificate;
    }

    /**
     * Legt den Wert der x509Certificate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setX509Certificate(byte[] value) {
        this.x509Certificate = value;
    }

    /**
     * Ruft den Wert der x509SubjectName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getX509SubjectName() {
        return x509SubjectName;
    }

    /**
     * Legt den Wert der x509SubjectName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setX509SubjectName(String value) {
        this.x509SubjectName = value;
    }

    /**
     * Ruft den Wert der keyValue-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KeyValueType }
     *     
     */
    public KeyValueType getKeyValue() {
        return keyValue;
    }

    /**
     * Legt den Wert der keyValue-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KeyValueType }
     *     
     */
    public void setKeyValue(KeyValueType value) {
        this.keyValue = value;
    }

    /**
     * Ruft den Wert der x509SKI-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getX509SKI() {
        return x509SKI;
    }

    /**
     * Legt den Wert der x509SKI-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setX509SKI(byte[] value) {
        this.x509SKI = value;
    }

    /**
     * Ruft den Wert der other-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AnyType }
     *     
     */
    public AnyType getOther() {
        return other;
    }

    /**
     * Legt den Wert der other-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AnyType }
     *     
     */
    public void setOther(AnyType value) {
        this.other = value;
    }

}
