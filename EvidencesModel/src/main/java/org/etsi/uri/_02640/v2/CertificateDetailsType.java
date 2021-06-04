//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2012.08.21 um 12:08:20 PM CEST 
//


package org.etsi.uri._02640.v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.etsi.uri._01903.v1_3.CertIDType;


/**
 * <p>Java-Klasse für CertificateDetailsType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CertificateDetailsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="X509Certificate" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="CertID" type="{http://uri.etsi.org/01903/v1.3.2#}CertIDType"/>
 *         &lt;element ref="{http://uri.etsi.org/02640/v2#}CertIDAndSignature"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CertificateDetailsType", propOrder = {
    "x509Certificate",
    "certID",
    "certIDAndSignature"
})
public class CertificateDetailsType {

    @XmlElement(name = "X509Certificate")
    protected byte[] x509Certificate;
    @XmlElement(name = "CertID")
    protected CertIDType certID;
    @XmlElement(name = "CertIDAndSignature")
    protected CertIDAndSignatureType certIDAndSignature;

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
     * Ruft den Wert der certID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CertIDType }
     *     
     */
    public CertIDType getCertID() {
        return certID;
    }

    /**
     * Legt den Wert der certID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CertIDType }
     *     
     */
    public void setCertID(CertIDType value) {
        this.certID = value;
    }

    /**
     * Ruft den Wert der certIDAndSignature-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CertIDAndSignatureType }
     *     
     */
    public CertIDAndSignatureType getCertIDAndSignature() {
        return certIDAndSignature;
    }

    /**
     * Legt den Wert der certIDAndSignature-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CertIDAndSignatureType }
     *     
     */
    public void setCertIDAndSignature(CertIDAndSignatureType value) {
        this.certIDAndSignature = value;
    }

}
