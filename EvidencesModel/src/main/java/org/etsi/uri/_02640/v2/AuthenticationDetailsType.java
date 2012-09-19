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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import oasis.names.tc.saml._2_0.assertion.AssertionType;
import org.etsi.uri._01903.v1_3.AnyType;


/**
 * <p>Java-Klasse für AuthenticationDetailsType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AuthenticationDetailsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element name="AuthenticationTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *             &lt;element name="AuthenticationMethod" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *           &lt;/sequence>
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}Assertion"/>
 *         &lt;/choice>
 *         &lt;element name="AdditionalDetails" type="{http://uri.etsi.org/01903/v1.3.2#}AnyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuthenticationDetailsType", propOrder = {
    "authenticationTime",
    "authenticationMethod",
    "assertion",
    "additionalDetails"
})
public class AuthenticationDetailsType {

    @XmlElement(name = "AuthenticationTime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar authenticationTime;
    @XmlElement(name = "AuthenticationMethod")
    @XmlSchemaType(name = "anyURI")
    protected String authenticationMethod;
    @XmlElement(name = "Assertion", namespace = "urn:oasis:names:tc:SAML:2.0:assertion")
    protected AssertionType assertion;
    @XmlElement(name = "AdditionalDetails")
    protected AnyType additionalDetails;

    /**
     * Ruft den Wert der authenticationTime-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAuthenticationTime() {
        return authenticationTime;
    }

    /**
     * Legt den Wert der authenticationTime-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAuthenticationTime(XMLGregorianCalendar value) {
        this.authenticationTime = value;
    }

    /**
     * Ruft den Wert der authenticationMethod-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthenticationMethod() {
        return authenticationMethod;
    }

    /**
     * Legt den Wert der authenticationMethod-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthenticationMethod(String value) {
        this.authenticationMethod = value;
    }

    /**
     * Ruft den Wert der assertion-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AssertionType }
     *     
     */
    public AssertionType getAssertion() {
        return assertion;
    }

    /**
     * Legt den Wert der assertion-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AssertionType }
     *     
     */
    public void setAssertion(AssertionType value) {
        this.assertion = value;
    }

    /**
     * Ruft den Wert der additionalDetails-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AnyType }
     *     
     */
    public AnyType getAdditionalDetails() {
        return additionalDetails;
    }

    /**
     * Legt den Wert der additionalDetails-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AnyType }
     *     
     */
    public void setAdditionalDetails(AnyType value) {
        this.additionalDetails = value;
    }

}
