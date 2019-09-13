package eu.domibus.connector.persistence.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "DOMIBUS_CONNECTOR_PROPERTY")
public class PDomibusConnectorProperties implements Serializable {


    @Id
    @Column(name = "ID")
    @TableGenerator(name = "propSeqStore", table = "DOMIBUS_CONNECTOR_SEQ_STORE", pkColumnName = "SEQ_NAME", pkColumnValue = "DOMIBUS_CONNECTOR_PROPERTY.ID", valueColumnName = "SEQ_VALUE", initialValue = 1000, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "propSeqStore")
    private int id;

    @Column(name = "PROPERTY_NAME")
    private String propertyName;

    @Column(name = "PROPERTY_VALUE")
    private String propertyValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
