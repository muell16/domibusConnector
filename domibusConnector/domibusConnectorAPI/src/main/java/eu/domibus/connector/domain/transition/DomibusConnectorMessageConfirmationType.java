
package eu.domibus.connector.domain.transition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.Source;


/**
 * <p>Java-Klasse für DomibusConnectorMessageConfirmationType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="DomibusConnectorMessageConfirmationType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="confirmation" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *         &lt;element name="confirmationType" type="{transition.domain.connector.domibus.eu}DomibusConnectorConfirmationType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DomibusConnectorMessageConfirmationType", propOrder = {
    "confirmation",
    "confirmationType"
})
public class DomibusConnectorMessageConfirmationType {

    @XmlElement(required = true)
    @XmlMimeType("application/xml")
    protected Source confirmation;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected DomibusConnectorConfirmationType confirmationType;

    /**
     * Ruft den Wert der confirmation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Source }
     *     
     */
    public Source getConfirmation() {
        return confirmation;
    }

    /**
     * Legt den Wert der confirmation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Source }
     *     
     */
    public void setConfirmation(Source value) {
        this.confirmation = value;
    }

    /**
     * Ruft den Wert der confirmationType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DomibusConnectorConfirmationType }
     *     
     */
    public DomibusConnectorConfirmationType getConfirmationType() {
        return confirmationType;
    }

    /**
     * Legt den Wert der confirmationType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DomibusConnectorConfirmationType }
     *     
     */
    public void setConfirmationType(DomibusConnectorConfirmationType value) {
        this.confirmationType = value;
    }

}
