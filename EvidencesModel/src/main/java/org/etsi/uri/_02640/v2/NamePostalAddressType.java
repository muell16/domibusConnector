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


/**
 * <p>Java-Klasse für NamePostalAddressType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="NamePostalAddressType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://uri.etsi.org/02640/v2#}EntityName" minOccurs="0"/>
 *         &lt;element ref="{http://uri.etsi.org/02640/v2#}PostalAddress" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NamePostalAddressType", propOrder = {
    "entityName",
    "postalAddress"
})
public class NamePostalAddressType {

    @XmlElement(name = "EntityName")
    protected EntityNameType entityName;
    @XmlElement(name = "PostalAddress")
    protected PostalAddressType postalAddress;

    /**
     * Ruft den Wert der entityName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link EntityNameType }
     *     
     */
    public EntityNameType getEntityName() {
        return entityName;
    }

    /**
     * Legt den Wert der entityName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link EntityNameType }
     *     
     */
    public void setEntityName(EntityNameType value) {
        this.entityName = value;
    }

    /**
     * Ruft den Wert der postalAddress-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PostalAddressType }
     *     
     */
    public PostalAddressType getPostalAddress() {
        return postalAddress;
    }

    /**
     * Legt den Wert der postalAddress-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PostalAddressType }
     *     
     */
    public void setPostalAddress(PostalAddressType value) {
        this.postalAddress = value;
    }

}
