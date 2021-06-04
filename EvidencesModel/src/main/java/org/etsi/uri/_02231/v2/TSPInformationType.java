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
 * <p>Java-Klasse für TSPInformationType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="TSPInformationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TSPName" type="{http://uri.etsi.org/02231/v2#}InternationalNamesType"/>
 *         &lt;element name="TSPTradeName" type="{http://uri.etsi.org/02231/v2#}InternationalNamesType" minOccurs="0"/>
 *         &lt;element name="TSPAddress" type="{http://uri.etsi.org/02231/v2#}AddressType"/>
 *         &lt;element name="TSPInformationURI" type="{http://uri.etsi.org/02231/v2#}NonEmptyMultiLangURIListType"/>
 *         &lt;element name="TSPInformationExtensions" type="{http://uri.etsi.org/02231/v2#}ExtensionsListType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TSPInformationType", propOrder = {
    "tspName",
    "tspTradeName",
    "tspAddress",
    "tspInformationURI",
    "tspInformationExtensions"
})
public class TSPInformationType {

    @XmlElement(name = "TSPName", required = true)
    protected InternationalNamesType tspName;
    @XmlElement(name = "TSPTradeName")
    protected InternationalNamesType tspTradeName;
    @XmlElement(name = "TSPAddress", required = true)
    protected AddressType tspAddress;
    @XmlElement(name = "TSPInformationURI", required = true)
    protected NonEmptyMultiLangURIListType tspInformationURI;
    @XmlElement(name = "TSPInformationExtensions")
    protected ExtensionsListType tspInformationExtensions;

    /**
     * Ruft den Wert der tspName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link InternationalNamesType }
     *     
     */
    public InternationalNamesType getTSPName() {
        return tspName;
    }

    /**
     * Legt den Wert der tspName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link InternationalNamesType }
     *     
     */
    public void setTSPName(InternationalNamesType value) {
        this.tspName = value;
    }

    /**
     * Ruft den Wert der tspTradeName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link InternationalNamesType }
     *     
     */
    public InternationalNamesType getTSPTradeName() {
        return tspTradeName;
    }

    /**
     * Legt den Wert der tspTradeName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link InternationalNamesType }
     *     
     */
    public void setTSPTradeName(InternationalNamesType value) {
        this.tspTradeName = value;
    }

    /**
     * Ruft den Wert der tspAddress-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AddressType }
     *     
     */
    public AddressType getTSPAddress() {
        return tspAddress;
    }

    /**
     * Legt den Wert der tspAddress-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressType }
     *     
     */
    public void setTSPAddress(AddressType value) {
        this.tspAddress = value;
    }

    /**
     * Ruft den Wert der tspInformationURI-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link NonEmptyMultiLangURIListType }
     *     
     */
    public NonEmptyMultiLangURIListType getTSPInformationURI() {
        return tspInformationURI;
    }

    /**
     * Legt den Wert der tspInformationURI-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link NonEmptyMultiLangURIListType }
     *     
     */
    public void setTSPInformationURI(NonEmptyMultiLangURIListType value) {
        this.tspInformationURI = value;
    }

    /**
     * Ruft den Wert der tspInformationExtensions-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ExtensionsListType }
     *     
     */
    public ExtensionsListType getTSPInformationExtensions() {
        return tspInformationExtensions;
    }

    /**
     * Legt den Wert der tspInformationExtensions-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionsListType }
     *     
     */
    public void setTSPInformationExtensions(ExtensionsListType value) {
        this.tspInformationExtensions = value;
    }

}
