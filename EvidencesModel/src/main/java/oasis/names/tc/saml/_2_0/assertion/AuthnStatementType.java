//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2012.08.21 um 12:08:20 PM CEST 
//


package oasis.names.tc.saml._2_0.assertion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse für AuthnStatementType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AuthnStatementType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:SAML:2.0:assertion}StatementAbstractType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}SubjectLocality" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}AuthnContext"/>
 *       &lt;/sequence>
 *       &lt;attribute name="AuthnInstant" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="SessionIndex" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="SessionNotOnOrAfter" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuthnStatementType", propOrder = {
    "subjectLocality",
    "authnContext"
})
public class AuthnStatementType
    extends StatementAbstractType
{

    @XmlElement(name = "SubjectLocality")
    protected SubjectLocalityType subjectLocality;
    @XmlElement(name = "AuthnContext", required = true)
    protected AuthnContextType authnContext;
    @XmlAttribute(name = "AuthnInstant", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar authnInstant;
    @XmlAttribute(name = "SessionIndex")
    protected String sessionIndex;
    @XmlAttribute(name = "SessionNotOnOrAfter")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar sessionNotOnOrAfter;

    /**
     * Ruft den Wert der subjectLocality-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SubjectLocalityType }
     *     
     */
    public SubjectLocalityType getSubjectLocality() {
        return subjectLocality;
    }

    /**
     * Legt den Wert der subjectLocality-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SubjectLocalityType }
     *     
     */
    public void setSubjectLocality(SubjectLocalityType value) {
        this.subjectLocality = value;
    }

    /**
     * Ruft den Wert der authnContext-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AuthnContextType }
     *     
     */
    public AuthnContextType getAuthnContext() {
        return authnContext;
    }

    /**
     * Legt den Wert der authnContext-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthnContextType }
     *     
     */
    public void setAuthnContext(AuthnContextType value) {
        this.authnContext = value;
    }

    /**
     * Ruft den Wert der authnInstant-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAuthnInstant() {
        return authnInstant;
    }

    /**
     * Legt den Wert der authnInstant-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAuthnInstant(XMLGregorianCalendar value) {
        this.authnInstant = value;
    }

    /**
     * Ruft den Wert der sessionIndex-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionIndex() {
        return sessionIndex;
    }

    /**
     * Legt den Wert der sessionIndex-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionIndex(String value) {
        this.sessionIndex = value;
    }

    /**
     * Ruft den Wert der sessionNotOnOrAfter-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSessionNotOnOrAfter() {
        return sessionNotOnOrAfter;
    }

    /**
     * Legt den Wert der sessionNotOnOrAfter-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSessionNotOnOrAfter(XMLGregorianCalendar value) {
        this.sessionNotOnOrAfter = value;
    }

}
