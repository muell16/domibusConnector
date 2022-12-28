package eu.ecodex.dc5.link.model;

import eu.domibus.connector.persistence.model.PDomibusConnectorPersistenceModel;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Table(name = DC5LinkConfigJpaEntity.TABLE_NAME)
@Entity
public class DC5LinkConfigJpaEntity {

    public static final String TABLE_NAME = "DC5_LINK_CONFIG";

    @Id
    @Column(name="ID")
    @TableGenerator(name = "seq" + TABLE_NAME,
            table = PDomibusConnectorPersistenceModel.SEQ_STORE_TABLE_NAME,
            pkColumnName = PDomibusConnectorPersistenceModel.SEQ_NAME_COLUMN_NAME,
            pkColumnValue = TABLE_NAME + ".ID",
            valueColumnName = PDomibusConnectorPersistenceModel.SEQ_VALUE_COLUMN_NAME,
            initialValue = PDomibusConnectorPersistenceModel.INITIAL_VALUE,
            allocationSize = PDomibusConnectorPersistenceModel.ALLOCATION_SIZE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq" + TABLE_NAME)
    private Long id;

    @Column(name = "LINK_IMPL")
    private String linkImpl;

    @Column(name = "CONFIG_NAME", nullable = false)
    private String configName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "DC_LINK_CONFIG_PROPERTY", joinColumns=@JoinColumn(name="DC_LINK_CONFIGURATION_ID", referencedColumnName = "ID"))
    @MapKeyColumn (name="PROPERTY_NAME", nullable = false)
    @Column(name="PROPERTY_VALUE", length = 2048)
    private Map<String, String> properties = new HashMap<String, String>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLinkImpl() {
        return linkImpl;
    }

    public void setLinkImpl(String linkImpl) {
        this.linkImpl = linkImpl;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }
}
