//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2012.08.21 um 12:08:20 PM CEST 
//


package org.etsi.uri._02231.v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.w3._2000._09.xmldsig.SignatureType;


/**
 * <p>Java-Klasse für TrustStatusListType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="TrustStatusListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}SchemeInformation"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}TrustServiceProviderList" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="TSLTag" use="required" type="{http://uri.etsi.org/02231/v2#}TSLTagType" />
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrustStatusListType", propOrder = {
    "schemeInformation",
    "trustServiceProviderList",
    "signature"
})
public class TrustStatusListType {

    @XmlElement(name = "SchemeInformation", required = true)
    protected TSLSchemeInformationType schemeInformation;
    @XmlElement(name = "TrustServiceProviderList")
    protected TrustServiceProviderListType trustServiceProviderList;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected SignatureType signature;
    @XmlAttribute(name = "TSLTag", required = true)
    protected String tslTag;
    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Ruft den Wert der schemeInformation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link TSLSchemeInformationType }
     *     
     */
    public TSLSchemeInformationType getSchemeInformation() {
        return schemeInformation;
    }

    /**
     * Legt den Wert der schemeInformation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link TSLSchemeInformationType }
     *     
     */
    public void setSchemeInformation(TSLSchemeInformationType value) {
        this.schemeInformation = value;
    }

    /**
     * Ruft den Wert der trustServiceProviderList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link TrustServiceProviderListType }
     *     
     */
    public TrustServiceProviderListType getTrustServiceProviderList() {
        return trustServiceProviderList;
    }

    /**
     * Legt den Wert der trustServiceProviderList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link TrustServiceProviderListType }
     *     
     */
    public void setTrustServiceProviderList(TrustServiceProviderListType value) {
        this.trustServiceProviderList = value;
    }

    /**
     * Ruft den Wert der signature-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SignatureType }
     *     
     */
    public SignatureType getSignature() {
        return signature;
    }

    /**
     * Legt den Wert der signature-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureType }
     *     
     */
    public void setSignature(SignatureType value) {
        this.signature = value;
    }

    /**
     * Ruft den Wert der tslTag-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTSLTag() {
        return tslTag;
    }

    /**
     * Legt den Wert der tslTag-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTSLTag(String value) {
        this.tslTag = value;
    }

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
