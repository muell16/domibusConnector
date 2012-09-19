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


/**
 * <p>Java-Klasse für SubjectConfirmationType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="SubjectConfirmationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice minOccurs="0">
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}BaseID"/>
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}NameID"/>
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}EncryptedID"/>
 *         &lt;/choice>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}SubjectConfirmationData" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Method" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubjectConfirmationType", propOrder = {
    "baseID",
    "nameID",
    "encryptedID",
    "subjectConfirmationData"
})
public class SubjectConfirmationType {

    @XmlElement(name = "BaseID")
    protected BaseIDAbstractType baseID;
    @XmlElement(name = "NameID")
    protected NameIDType nameID;
    @XmlElement(name = "EncryptedID")
    protected EncryptedElementType encryptedID;
    @XmlElement(name = "SubjectConfirmationData")
    protected SubjectConfirmationDataType subjectConfirmationData;
    @XmlAttribute(name = "Method", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String method;

    /**
     * Ruft den Wert der baseID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BaseIDAbstractType }
     *     
     */
    public BaseIDAbstractType getBaseID() {
        return baseID;
    }

    /**
     * Legt den Wert der baseID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BaseIDAbstractType }
     *     
     */
    public void setBaseID(BaseIDAbstractType value) {
        this.baseID = value;
    }

    /**
     * Ruft den Wert der nameID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link NameIDType }
     *     
     */
    public NameIDType getNameID() {
        return nameID;
    }

    /**
     * Legt den Wert der nameID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link NameIDType }
     *     
     */
    public void setNameID(NameIDType value) {
        this.nameID = value;
    }

    /**
     * Ruft den Wert der encryptedID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link EncryptedElementType }
     *     
     */
    public EncryptedElementType getEncryptedID() {
        return encryptedID;
    }

    /**
     * Legt den Wert der encryptedID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link EncryptedElementType }
     *     
     */
    public void setEncryptedID(EncryptedElementType value) {
        this.encryptedID = value;
    }

    /**
     * Ruft den Wert der subjectConfirmationData-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SubjectConfirmationDataType }
     *     
     */
    public SubjectConfirmationDataType getSubjectConfirmationData() {
        return subjectConfirmationData;
    }

    /**
     * Legt den Wert der subjectConfirmationData-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SubjectConfirmationDataType }
     *     
     */
    public void setSubjectConfirmationData(SubjectConfirmationDataType value) {
        this.subjectConfirmationData = value;
    }

    /**
     * Ruft den Wert der method-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMethod() {
        return method;
    }

    /**
     * Legt den Wert der method-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMethod(String value) {
        this.method = value;
    }

}
