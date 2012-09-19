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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse für TSPServiceInformationType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="TSPServiceInformationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}ServiceTypeIdentifier"/>
 *         &lt;element name="ServiceName" type="{http://uri.etsi.org/02231/v2#}InternationalNamesType"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}ServiceDigitalIdentity"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}ServiceStatus"/>
 *         &lt;element name="StatusStartingTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="SchemeServiceDefinitionURI" type="{http://uri.etsi.org/02231/v2#}NonEmptyMultiLangURIListType" minOccurs="0"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}ServiceSupplyPoints" minOccurs="0"/>
 *         &lt;element name="TSPServiceDefinitionURI" type="{http://uri.etsi.org/02231/v2#}NonEmptyMultiLangURIListType" minOccurs="0"/>
 *         &lt;element name="ServiceInformationExtensions" type="{http://uri.etsi.org/02231/v2#}ExtensionsListType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TSPServiceInformationType", propOrder = {
    "serviceTypeIdentifier",
    "serviceName",
    "serviceDigitalIdentity",
    "serviceStatus",
    "statusStartingTime",
    "schemeServiceDefinitionURI",
    "serviceSupplyPoints",
    "tspServiceDefinitionURI",
    "serviceInformationExtensions"
})
public class TSPServiceInformationType {

    @XmlElement(name = "ServiceTypeIdentifier", required = true)
    protected String serviceTypeIdentifier;
    @XmlElement(name = "ServiceName", required = true)
    protected InternationalNamesType serviceName;
    @XmlElement(name = "ServiceDigitalIdentity", required = true)
    protected DigitalIdentityListType serviceDigitalIdentity;
    @XmlElement(name = "ServiceStatus", required = true)
    protected String serviceStatus;
    @XmlElement(name = "StatusStartingTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar statusStartingTime;
    @XmlElement(name = "SchemeServiceDefinitionURI")
    protected NonEmptyMultiLangURIListType schemeServiceDefinitionURI;
    @XmlElement(name = "ServiceSupplyPoints")
    protected ServiceSupplyPointsType serviceSupplyPoints;
    @XmlElement(name = "TSPServiceDefinitionURI")
    protected NonEmptyMultiLangURIListType tspServiceDefinitionURI;
    @XmlElement(name = "ServiceInformationExtensions")
    protected ExtensionsListType serviceInformationExtensions;

    /**
     * Ruft den Wert der serviceTypeIdentifier-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceTypeIdentifier() {
        return serviceTypeIdentifier;
    }

    /**
     * Legt den Wert der serviceTypeIdentifier-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceTypeIdentifier(String value) {
        this.serviceTypeIdentifier = value;
    }

    /**
     * Ruft den Wert der serviceName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link InternationalNamesType }
     *     
     */
    public InternationalNamesType getServiceName() {
        return serviceName;
    }

    /**
     * Legt den Wert der serviceName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link InternationalNamesType }
     *     
     */
    public void setServiceName(InternationalNamesType value) {
        this.serviceName = value;
    }

    /**
     * Ruft den Wert der serviceDigitalIdentity-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DigitalIdentityListType }
     *     
     */
    public DigitalIdentityListType getServiceDigitalIdentity() {
        return serviceDigitalIdentity;
    }

    /**
     * Legt den Wert der serviceDigitalIdentity-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DigitalIdentityListType }
     *     
     */
    public void setServiceDigitalIdentity(DigitalIdentityListType value) {
        this.serviceDigitalIdentity = value;
    }

    /**
     * Ruft den Wert der serviceStatus-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceStatus() {
        return serviceStatus;
    }

    /**
     * Legt den Wert der serviceStatus-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceStatus(String value) {
        this.serviceStatus = value;
    }

    /**
     * Ruft den Wert der statusStartingTime-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStatusStartingTime() {
        return statusStartingTime;
    }

    /**
     * Legt den Wert der statusStartingTime-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStatusStartingTime(XMLGregorianCalendar value) {
        this.statusStartingTime = value;
    }

    /**
     * Ruft den Wert der schemeServiceDefinitionURI-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link NonEmptyMultiLangURIListType }
     *     
     */
    public NonEmptyMultiLangURIListType getSchemeServiceDefinitionURI() {
        return schemeServiceDefinitionURI;
    }

    /**
     * Legt den Wert der schemeServiceDefinitionURI-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link NonEmptyMultiLangURIListType }
     *     
     */
    public void setSchemeServiceDefinitionURI(NonEmptyMultiLangURIListType value) {
        this.schemeServiceDefinitionURI = value;
    }

    /**
     * Ruft den Wert der serviceSupplyPoints-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ServiceSupplyPointsType }
     *     
     */
    public ServiceSupplyPointsType getServiceSupplyPoints() {
        return serviceSupplyPoints;
    }

    /**
     * Legt den Wert der serviceSupplyPoints-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceSupplyPointsType }
     *     
     */
    public void setServiceSupplyPoints(ServiceSupplyPointsType value) {
        this.serviceSupplyPoints = value;
    }

    /**
     * Ruft den Wert der tspServiceDefinitionURI-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link NonEmptyMultiLangURIListType }
     *     
     */
    public NonEmptyMultiLangURIListType getTSPServiceDefinitionURI() {
        return tspServiceDefinitionURI;
    }

    /**
     * Legt den Wert der tspServiceDefinitionURI-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link NonEmptyMultiLangURIListType }
     *     
     */
    public void setTSPServiceDefinitionURI(NonEmptyMultiLangURIListType value) {
        this.tspServiceDefinitionURI = value;
    }

    /**
     * Ruft den Wert der serviceInformationExtensions-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ExtensionsListType }
     *     
     */
    public ExtensionsListType getServiceInformationExtensions() {
        return serviceInformationExtensions;
    }

    /**
     * Legt den Wert der serviceInformationExtensions-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionsListType }
     *     
     */
    public void setServiceInformationExtensions(ExtensionsListType value) {
        this.serviceInformationExtensions = value;
    }

}
