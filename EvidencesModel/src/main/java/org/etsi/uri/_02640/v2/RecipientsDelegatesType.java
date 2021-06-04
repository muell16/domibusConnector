//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2012.08.21 um 12:08:20 PM CEST 
//


package org.etsi.uri._02640.v2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für RecipientsDelegatesType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="RecipientsDelegatesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element name="DelegateDetails" type="{http://uri.etsi.org/02640/v2#}EntityDetailsType"/>
 *         &lt;element name="DelegatingRecipients" type="{http://uri.etsi.org/02640/v2#}ListOfIntegers"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RecipientsDelegatesType", propOrder = {
    "delegateDetailsAndDelegatingRecipients"
})
public class RecipientsDelegatesType {

    @XmlElementRefs({
        @XmlElementRef(name = "DelegatingRecipients", namespace = "http://uri.etsi.org/02640/v2#", type = JAXBElement.class),
        @XmlElementRef(name = "DelegateDetails", namespace = "http://uri.etsi.org/02640/v2#", type = JAXBElement.class)
    })
    protected List<JAXBElement<?>> delegateDetailsAndDelegatingRecipients;

    /**
     * Gets the value of the delegateDetailsAndDelegatingRecipients property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the delegateDetailsAndDelegatingRecipients property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDelegateDetailsAndDelegatingRecipients().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link List }{@code <}{@link BigInteger }{@code >}{@code >}
     * {@link JAXBElement }{@code <}{@link EntityDetailsType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getDelegateDetailsAndDelegatingRecipients() {
        if (delegateDetailsAndDelegatingRecipients == null) {
            delegateDetailsAndDelegatingRecipients = new ArrayList<JAXBElement<?>>();
        }
        return this.delegateDetailsAndDelegatingRecipients;
    }

}
