package eu.domibus.connector.persistence.model;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

import static eu.domibus.connector.persistence.model.PDomibusConnectorPersistenceModel.SEQ_STORE_TABLE_NAME;

@Table(name = "DC_LINK_CONFIGURATION")
@Entity
public class PDomibusConnectorLinkConfiguration {

    @Id
    @Column(name="ID")
    @TableGenerator(name = "seqStoreLinkConfig", table = SEQ_STORE_TABLE_NAME, pkColumnName = "SEQ_NAME", pkColumnValue = "DC_LINK_CONFIGURATION.ID", valueColumnName = "SEQ_VALUE", initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seqStoreLinkConfig")
    private Long id;

    @Column(name = "LINK_IMPL")
    private String linkImpl;

    @Column(name = "CONFIG_NAME")
    private String configName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "DC_LINK_CONFIG_PROPERTY", joinColumns=@JoinColumn(name="DC_LINK_CONFIGURATION_ID", referencedColumnName = "ID"))
    @MapKeyColumn (name="PROPERTY_NAME")
    @Column(name="PROPERTY_VALUE")
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
