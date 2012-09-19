//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2012.08.21 um 12:08:20 PM CEST 
//


package org.etsi.uri._02231.v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ExtensionType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ExtensionType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://uri.etsi.org/02231/v2#}AnyType">
 *       &lt;attribute name="Critical" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
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

    @XmlAttribute(name = "Critical", required = true)
    protected boolean critical;

    /**
     * Ruft den Wert der critical-Eigenschaft ab.
     * 
     */
    public boolean isCritical() {
        return critical;
    }

    /**
     * Legt den Wert der critical-Eigenschaft fest.
     * 
     */
    public void setCritical(boolean value) {
        this.critical = value;
    }

}
