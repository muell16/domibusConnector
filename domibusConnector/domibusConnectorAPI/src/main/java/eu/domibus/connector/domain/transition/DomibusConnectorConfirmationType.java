
package eu.domibus.connector.domain.transition;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für DomibusConnectorConfirmationType.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="DomibusConnectorConfirmationType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="SUBMISSION_ACCEPTANCE"/&gt;
 *     &lt;enumeration value="SUBMISSION_REJECTION"/&gt;
 *     &lt;enumeration value="RELAY_REMMD_ACCEPTANCE"/&gt;
 *     &lt;enumeration value="RELAY_REMMD_REJECTION"/&gt;
 *     &lt;enumeration value="RELAY_REMMD_FAILURE"/&gt;
 *     &lt;enumeration value="DELIVERY"/&gt;
 *     &lt;enumeration value="NON_DELIVERY"/&gt;
 *     &lt;enumeration value="RETRIEVAL"/&gt;
 *     &lt;enumeration value="NON_RETRIEVAL"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "DomibusConnectorConfirmationType")
@XmlEnum
public enum DomibusConnectorConfirmationType {

    SUBMISSION_ACCEPTANCE,
    SUBMISSION_REJECTION,
    RELAY_REMMD_ACCEPTANCE,
    RELAY_REMMD_REJECTION,
    RELAY_REMMD_FAILURE,
    DELIVERY,
    NON_DELIVERY,
    RETRIEVAL,
    NON_RETRIEVAL;

    public String value() {
        return name();
    }

    public static DomibusConnectorConfirmationType fromValue(String v) {
        return valueOf(v);
    }

}
