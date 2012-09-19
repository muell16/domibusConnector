//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2012.08.21 um 12:08:20 PM CEST 
//


package org.etsi.uri._02640.v2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für PostalAddressType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PostalAddressType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StreetAddress" type="{http://uri.etsi.org/02231/v2#}NonEmptyString" maxOccurs="unbounded"/>
 *         &lt;element name="Locality" type="{http://uri.etsi.org/02231/v2#}NonEmptyString"/>
 *         &lt;element name="StateOrProvince" type="{http://uri.etsi.org/02231/v2#}NonEmptyString" minOccurs="0"/>
 *         &lt;element name="PostalCode" type="{http://uri.etsi.org/02231/v2#}NonEmptyString"/>
 *         &lt;element name="CountryName" type="{http://uri.etsi.org/02231/v2#}NonEmptyString"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}lang"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PostalAddressType", propOrder = {
    "streetAddress",
    "locality",
    "stateOrProvince",
    "postalCode",
    "countryName"
})
public class PostalAddressType {

    @XmlElement(name = "StreetAddress", required = true)
    protected List<String> streetAddress;
    @XmlElement(name = "Locality", required = true)
    protected String locality;
    @XmlElement(name = "StateOrProvince")
    protected String stateOrProvince;
    @XmlElement(name = "PostalCode", required = true)
    protected String postalCode;
    @XmlElement(name = "CountryName", required = true)
    protected String countryName;
    @XmlAttribute(name = "lang", namespace = "http://www.w3.org/XML/1998/namespace")
    protected String lang;

    /**
     * Gets the value of the streetAddress property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the streetAddress property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStreetAddress().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getStreetAddress() {
        if (streetAddress == null) {
            streetAddress = new ArrayList<String>();
        }
        return this.streetAddress;
    }

    /**
     * Ruft den Wert der locality-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocality() {
        return locality;
    }

    /**
     * Legt den Wert der locality-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocality(String value) {
        this.locality = value;
    }

    /**
     * Ruft den Wert der stateOrProvince-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateOrProvince() {
        return stateOrProvince;
    }

    /**
     * Legt den Wert der stateOrProvince-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateOrProvince(String value) {
        this.stateOrProvince = value;
    }

    /**
     * Ruft den Wert der postalCode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Legt den Wert der postalCode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostalCode(String value) {
        this.postalCode = value;
    }

    /**
     * Ruft den Wert der countryName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Legt den Wert der countryName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryName(String value) {
        this.countryName = value;
    }

    /**
     * Ruft den Wert der lang-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLang() {
        return lang;
    }

    /**
     * Legt den Wert der lang-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLang(String value) {
        this.lang = value;
    }

}
