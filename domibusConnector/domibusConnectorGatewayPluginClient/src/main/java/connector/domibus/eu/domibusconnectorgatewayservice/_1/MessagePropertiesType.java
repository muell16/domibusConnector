
package connector.domibus.eu.domibusconnectorgatewayservice._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für MessagePropertiesType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="MessagePropertiesType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="messageProperties" type="{http://eu.domibus.connector/DomibusConnectorGatewayService/1.0/}MessagePropertyType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessagePropertiesType", propOrder = {
    "messageProperties"
})
public class MessagePropertiesType {

    protected List<MessagePropertyType> messageProperties;

    /**
     * Gets the value of the messageProperties property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the messageProperties property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessageProperties().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MessagePropertyType }
     * 
     * 
     */
    public List<MessagePropertyType> getMessageProperties() {
        if (messageProperties == null) {
            messageProperties = new ArrayList<MessagePropertyType>();
        }
        return this.messageProperties;
    }

}
