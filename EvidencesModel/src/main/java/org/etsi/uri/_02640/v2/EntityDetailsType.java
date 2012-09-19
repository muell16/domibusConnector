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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;
import org.etsi.uri._01903.v1_3.AnyType;
import org.etsi.uri._02231.v2.ElectronicAddressType;


/**
 * <p>Java-Klasse für EntityDetailsType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="EntityDetailsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://uri.etsi.org/02640/v2#}NamesPostalAddresses" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://uri.etsi.org/02640/v2#}AttributedElectronicAddress"/>
 *           &lt;element ref="{http://uri.etsi.org/02231/v2#}ElectronicAddress"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://uri.etsi.org/02640/v2#}CertificateDetails" minOccurs="0"/>
 *         &lt;element ref="{http://uri.etsi.org/01903/v1.3.2#}Any" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntityDetailsType", propOrder = {
    "namesPostalAddresses",
    "attributedElectronicAddressOrElectronicAddress",
    "certificateDetails",
    "any"
})
public class EntityDetailsType {

    @XmlElement(name = "NamesPostalAddresses")
    protected NamesPostalAddressListType namesPostalAddresses;
    @XmlElements({
        @XmlElement(name = "AttributedElectronicAddress", type = AttributedElectronicAddressType.class),
        @XmlElement(name = "ElectronicAddress", namespace = "http://uri.etsi.org/02231/v2#", type = ElectronicAddressType.class)
    })
    protected List<Object> attributedElectronicAddressOrElectronicAddress;
    @XmlElement(name = "CertificateDetails")
    protected CertificateDetailsType certificateDetails;
    @XmlElement(name = "Any", namespace = "http://uri.etsi.org/01903/v1.3.2#")
    protected AnyType any;

    /**
     * Ruft den Wert der namesPostalAddresses-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link NamesPostalAddressListType }
     *     
     */
    public NamesPostalAddressListType getNamesPostalAddresses() {
        return namesPostalAddresses;
    }

    /**
     * Legt den Wert der namesPostalAddresses-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link NamesPostalAddressListType }
     *     
     */
    public void setNamesPostalAddresses(NamesPostalAddressListType value) {
        this.namesPostalAddresses = value;
    }

    /**
     * Gets the value of the attributedElectronicAddressOrElectronicAddress property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attributedElectronicAddressOrElectronicAddress property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttributedElectronicAddressOrElectronicAddress().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttributedElectronicAddressType }
     * {@link ElectronicAddressType }
     * 
     * 
     */
    public List<Object> getAttributedElectronicAddressOrElectronicAddress() {
        if (attributedElectronicAddressOrElectronicAddress == null) {
            attributedElectronicAddressOrElectronicAddress = new ArrayList<Object>();
        }
        return this.attributedElectronicAddressOrElectronicAddress;
    }

    /**
     * Ruft den Wert der certificateDetails-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CertificateDetailsType }
     *     
     */
    public CertificateDetailsType getCertificateDetails() {
        return certificateDetails;
    }

    /**
     * Legt den Wert der certificateDetails-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CertificateDetailsType }
     *     
     */
    public void setCertificateDetails(CertificateDetailsType value) {
        this.certificateDetails = value;
    }

    /**
     * Ruft den Wert der any-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AnyType }
     *     
     */
    public AnyType getAny() {
        return any;
    }

    /**
     * Legt den Wert der any-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AnyType }
     *     
     */
    public void setAny(AnyType value) {
        this.any = value;
    }

}
