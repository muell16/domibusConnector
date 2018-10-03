package eu.domibus.connector.persistence.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DOMIBUS_CONNECTOR_PROPERTY")
public class PDomibusConnectorProperties implements Serializable {

    @Id
    @Column(name = "PROPERTY_NAME")
    private String propertyName;

    @Column(name = "PROPERTY_VALUE")
    private String propertyValue;



    public String getPropertyName() {
		return propertyName;
	}



	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}



	public String getPropertyValue() {
		return propertyValue;
	}



	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}



	@Override
    public String toString() {
        ToStringBuilder toString = new ToStringBuilder(this);
        toString.append("propertyName", propertyName);
        toString.append("propertyValue", propertyValue);
        return toString.build();
    }

}
