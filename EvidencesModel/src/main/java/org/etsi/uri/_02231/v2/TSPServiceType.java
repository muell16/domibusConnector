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
 * <p>Java-Klasse für TSPServiceType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="TSPServiceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}ServiceInformation"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}ServiceHistory" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TSPServiceType", propOrder = {
    "serviceInformation",
    "serviceHistory"
})
public class TSPServiceType {

    @XmlElement(name = "ServiceInformation", required = true)
    protected TSPServiceInformationType serviceInformation;
    @XmlElement(name = "ServiceHistory")
    protected ServiceHistoryType serviceHistory;

    /**
     * Ruft den Wert der serviceInformation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link TSPServiceInformationType }
     *     
     */
    public TSPServiceInformationType getServiceInformation() {
        return serviceInformation;
    }

    /**
     * Legt den Wert der serviceInformation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link TSPServiceInformationType }
     *     
     */
    public void setServiceInformation(TSPServiceInformationType value) {
        this.serviceInformation = value;
    }

    /**
     * Ruft den Wert der serviceHistory-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ServiceHistoryType }
     *     
     */
    public ServiceHistoryType getServiceHistory() {
        return serviceHistory;
    }

    /**
     * Legt den Wert der serviceHistory-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceHistoryType }
     *     
     */
    public void setServiceHistory(ServiceHistoryType value) {
        this.serviceHistory = value;
    }

}
