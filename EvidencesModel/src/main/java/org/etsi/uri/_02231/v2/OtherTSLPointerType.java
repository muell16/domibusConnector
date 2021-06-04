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


/**
 * <p>Java-Klasse für OtherTSLPointerType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="OtherTSLPointerType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}ServiceDigitalIdentities" minOccurs="0"/>
 *         &lt;element name="TSLLocation" type="{http://uri.etsi.org/02231/v2#}NonEmptyURIType"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}AdditionalInformation" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OtherTSLPointerType", propOrder = {
    "serviceDigitalIdentities",
    "tslLocation",
    "additionalInformation"
})
public class OtherTSLPointerType {

    @XmlElement(name = "ServiceDigitalIdentities")
    protected ServiceDigitalIdentityListType serviceDigitalIdentities;
    @XmlElement(name = "TSLLocation", required = true)
    protected String tslLocation;
    @XmlElement(name = "AdditionalInformation")
    protected AdditionalInformationType additionalInformation;

    /**
     * Ruft den Wert der serviceDigitalIdentities-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ServiceDigitalIdentityListType }
     *     
     */
    public ServiceDigitalIdentityListType getServiceDigitalIdentities() {
        return serviceDigitalIdentities;
    }

    /**
     * Legt den Wert der serviceDigitalIdentities-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceDigitalIdentityListType }
     *     
     */
    public void setServiceDigitalIdentities(ServiceDigitalIdentityListType value) {
        this.serviceDigitalIdentities = value;
    }

    /**
     * Ruft den Wert der tslLocation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTSLLocation() {
        return tslLocation;
    }

    /**
     * Legt den Wert der tslLocation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTSLLocation(String value) {
        this.tslLocation = value;
    }

    /**
     * Ruft den Wert der additionalInformation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AdditionalInformationType }
     *     
     */
    public AdditionalInformationType getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     * Legt den Wert der additionalInformation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AdditionalInformationType }
     *     
     */
    public void setAdditionalInformation(AdditionalInformationType value) {
        this.additionalInformation = value;
    }

}
