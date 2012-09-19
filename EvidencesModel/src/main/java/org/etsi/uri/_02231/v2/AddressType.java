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
 * <p>Java-Klasse für AddressType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AddressType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}PostalAddresses"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}ElectronicAddress"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddressType", propOrder = {
    "postalAddresses",
    "electronicAddress"
})
public class AddressType {

    @XmlElement(name = "PostalAddresses", required = true)
    protected PostalAddressListType postalAddresses;
    @XmlElement(name = "ElectronicAddress", required = true)
    protected ElectronicAddressType electronicAddress;

    /**
     * Ruft den Wert der postalAddresses-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PostalAddressListType }
     *     
     */
    public PostalAddressListType getPostalAddresses() {
        return postalAddresses;
    }

    /**
     * Legt den Wert der postalAddresses-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PostalAddressListType }
     *     
     */
    public void setPostalAddresses(PostalAddressListType value) {
        this.postalAddresses = value;
    }

    /**
     * Ruft den Wert der electronicAddress-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ElectronicAddressType }
     *     
     */
    public ElectronicAddressType getElectronicAddress() {
        return electronicAddress;
    }

    /**
     * Legt den Wert der electronicAddress-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ElectronicAddressType }
     *     
     */
    public void setElectronicAddress(ElectronicAddressType value) {
        this.electronicAddress = value;
    }

}
