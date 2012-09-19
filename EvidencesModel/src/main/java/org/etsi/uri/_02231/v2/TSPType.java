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
 * <p>Java-Klasse für TSPType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="TSPType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}TSPInformation"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}TSPServices"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TSPType", propOrder = {
    "tspInformation",
    "tspServices"
})
public class TSPType {

    @XmlElement(name = "TSPInformation", required = true)
    protected TSPInformationType tspInformation;
    @XmlElement(name = "TSPServices", required = true)
    protected TSPServicesListType tspServices;

    /**
     * Ruft den Wert der tspInformation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link TSPInformationType }
     *     
     */
    public TSPInformationType getTSPInformation() {
        return tspInformation;
    }

    /**
     * Legt den Wert der tspInformation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link TSPInformationType }
     *     
     */
    public void setTSPInformation(TSPInformationType value) {
        this.tspInformation = value;
    }

    /**
     * Ruft den Wert der tspServices-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link TSPServicesListType }
     *     
     */
    public TSPServicesListType getTSPServices() {
        return tspServices;
    }

    /**
     * Legt den Wert der tspServices-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link TSPServicesListType }
     *     
     */
    public void setTSPServices(TSPServicesListType value) {
        this.tspServices = value;
    }

}
