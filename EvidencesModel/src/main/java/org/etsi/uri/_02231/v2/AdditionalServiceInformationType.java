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
 * <p>Java-Klasse für AdditionalServiceInformationType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AdditionalServiceInformationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="URI" type="{http://uri.etsi.org/02231/v2#}NonEmptyMultiLangURIType"/>
 *         &lt;element name="InformationValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OtherInformation" type="{http://uri.etsi.org/02231/v2#}AnyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdditionalServiceInformationType", propOrder = {
    "uri",
    "informationValue",
    "otherInformation"
})
public class AdditionalServiceInformationType {

    @XmlElement(name = "URI", required = true)
    protected NonEmptyMultiLangURIType uri;
    @XmlElement(name = "InformationValue")
    protected String informationValue;
    @XmlElement(name = "OtherInformation")
    protected AnyType otherInformation;

    /**
     * Ruft den Wert der uri-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link NonEmptyMultiLangURIType }
     *     
     */
    public NonEmptyMultiLangURIType getURI() {
        return uri;
    }

    /**
     * Legt den Wert der uri-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link NonEmptyMultiLangURIType }
     *     
     */
    public void setURI(NonEmptyMultiLangURIType value) {
        this.uri = value;
    }

    /**
     * Ruft den Wert der informationValue-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInformationValue() {
        return informationValue;
    }

    /**
     * Legt den Wert der informationValue-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInformationValue(String value) {
        this.informationValue = value;
    }

    /**
     * Ruft den Wert der otherInformation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AnyType }
     *     
     */
    public AnyType getOtherInformation() {
        return otherInformation;
    }

    /**
     * Legt den Wert der otherInformation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AnyType }
     *     
     */
    public void setOtherInformation(AnyType value) {
        this.otherInformation = value;
    }

}
