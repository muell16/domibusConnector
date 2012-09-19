//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2012.08.21 um 12:08:20 PM CEST 
//


package org.etsi.uri._02640.v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.etsi.uri._01903.v1_3.AnyType;


/**
 * <p>Java-Klasse für ExtensionType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ExtensionType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://uri.etsi.org/01903/v1.3.2#}AnyType">
 *       &lt;attribute name="isCritical" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;anyAttribute/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExtensionType")
public class ExtensionType
    extends AnyType
{

    @XmlAttribute(name = "isCritical")
    protected Boolean isCritical;

    /**
     * Ruft den Wert der isCritical-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCritical() {
        return isCritical;
    }

    /**
     * Legt den Wert der isCritical-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCritical(Boolean value) {
        this.isCritical = value;
    }

}
