
package eu.domibus.connector.domain.transition;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für DomibusConnectorDetachedSignatureMimeType.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="DomibusConnectorDetachedSignatureMimeType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="BINARY"/&gt;
 *     &lt;enumeration value="XML"/&gt;
 *     &lt;enumeration value="PKCS7"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "DomibusConnectorDetachedSignatureMimeType")
@XmlEnum
public enum DomibusConnectorDetachedSignatureMimeType {

    BINARY("BINARY"),
    XML("XML"),
    @XmlEnumValue("PKCS7")
    PKCS_7("PKCS7");
    private final String value;

    DomibusConnectorDetachedSignatureMimeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DomibusConnectorDetachedSignatureMimeType fromValue(String v) {
        for (DomibusConnectorDetachedSignatureMimeType c: DomibusConnectorDetachedSignatureMimeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
