//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2012.08.21 um 12:08:20 PM CEST 
//


package oasis.names.tc.saml._2_0.assertion;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für AuthnContextType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AuthnContextType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}AuthnContextClassRef"/>
 *             &lt;choice minOccurs="0">
 *               &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}AuthnContextDecl"/>
 *               &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}AuthnContextDeclRef"/>
 *             &lt;/choice>
 *           &lt;/sequence>
 *           &lt;choice>
 *             &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}AuthnContextDecl"/>
 *             &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}AuthnContextDeclRef"/>
 *           &lt;/choice>
 *         &lt;/choice>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}AuthenticatingAuthority" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuthnContextType", propOrder = {
    "content"
})
public class AuthnContextType {

    @XmlElementRefs({
        @XmlElementRef(name = "AuthenticatingAuthority", namespace = "urn:oasis:names:tc:SAML:2.0:assertion", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AuthnContextClassRef", namespace = "urn:oasis:names:tc:SAML:2.0:assertion", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AuthnContextDecl", namespace = "urn:oasis:names:tc:SAML:2.0:assertion", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AuthnContextDeclRef", namespace = "urn:oasis:names:tc:SAML:2.0:assertion", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> content;

    /**
     * Ruft das restliche Contentmodell ab. 
     * 
     * <p>
     * Sie rufen diese "catch-all"-Eigenschaft aus folgendem Grund ab: 
     * Der Feldname "AuthnContextDecl" wird von zwei verschiedenen Teilen eines Schemas verwendet. Siehe: 
     * Zeile 204 von file:/D:/testworkspace/EvidencesModel/src/main/resources/saml-schema-assertion-2.0.xsd
     * Zeile 199 von file:/D:/testworkspace/EvidencesModel/src/main/resources/saml-schema-assertion-2.0.xsd
     * <p>
     * Um diese Eigenschaft zu entfernen, wenden Sie eine Eigenschaftenanpassung für eine
     * der beiden folgenden Deklarationen an, um deren Namen zu ändern: 
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getContent() {
        if (content == null) {
            content = new ArrayList<JAXBElement<?>>();
        }
        return this.content;
    }

}
