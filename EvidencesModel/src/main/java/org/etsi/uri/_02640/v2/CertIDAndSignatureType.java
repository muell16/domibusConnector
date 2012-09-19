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
import org.etsi.uri._01903.v1_3.DigestAlgAndValueType;


/**
 * <p>Java-Klasse für CertIDAndSignatureType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CertIDAndSignatureType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IssuerSerial" type="{http://uri.etsi.org/01903/v1.3.2#}DigestAlgAndValueType"/>
 *         &lt;element name="tbsCertificateDigestDetails" type="{http://uri.etsi.org/01903/v1.3.2#}DigestAlgAndValueType"/>
 *         &lt;element ref="{http://uri.etsi.org/02640/v2#}CertSignatureDetails"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CertIDAndSignatureType", propOrder = {
    "issuerSerial",
    "tbsCertificateDigestDetails",
    "certSignatureDetails"
})
public class CertIDAndSignatureType {

    @XmlElement(name = "IssuerSerial", required = true)
    protected DigestAlgAndValueType issuerSerial;
    @XmlElement(required = true)
    protected DigestAlgAndValueType tbsCertificateDigestDetails;
    @XmlElement(name = "CertSignatureDetails", required = true)
    protected CertSignatureDetailsType certSignatureDetails;

    /**
     * Ruft den Wert der issuerSerial-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DigestAlgAndValueType }
     *     
     */
    public DigestAlgAndValueType getIssuerSerial() {
        return issuerSerial;
    }

    /**
     * Legt den Wert der issuerSerial-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DigestAlgAndValueType }
     *     
     */
    public void setIssuerSerial(DigestAlgAndValueType value) {
        this.issuerSerial = value;
    }

    /**
     * Ruft den Wert der tbsCertificateDigestDetails-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DigestAlgAndValueType }
     *     
     */
    public DigestAlgAndValueType getTbsCertificateDigestDetails() {
        return tbsCertificateDigestDetails;
    }

    /**
     * Legt den Wert der tbsCertificateDigestDetails-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DigestAlgAndValueType }
     *     
     */
    public void setTbsCertificateDigestDetails(DigestAlgAndValueType value) {
        this.tbsCertificateDigestDetails = value;
    }

    /**
     * Ruft den Wert der certSignatureDetails-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CertSignatureDetailsType }
     *     
     */
    public CertSignatureDetailsType getCertSignatureDetails() {
        return certSignatureDetails;
    }

    /**
     * Legt den Wert der certSignatureDetails-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CertSignatureDetailsType }
     *     
     */
    public void setCertSignatureDetails(CertSignatureDetailsType value) {
        this.certSignatureDetails = value;
    }

}
