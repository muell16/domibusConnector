package eu.ecodex.dc5.link.model;

import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.persistence.model.PDomibusConnectorPersistenceModel;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Table(name = DC5LinkPartnerJpaEntity.TABLE_NAME)
@Entity
public class DC5LinkPartnerJpaEntity {

    public static final String TABLE_NAME = "DC5_LINK_PARTNER";

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

    @Column(name= "NAME", unique = true, nullable = false, length = 255)
    private String linkName;

    @Lob
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name="ENABLED", nullable = false)
    private Boolean enabled = Boolean.FALSE;

    @Column(name = "LINK_TYPE", length = 20)
    private LinkType linkType;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "DC_LINK_PROP")
    @MapKeyColumn (name="KEY", nullable = false)
    @Column(name="VALUE", length = 2048)
    private Map<String, String> properties = new HashMap<String, String>();

    @ManyToOne
    @JoinColumn(name = "LINK_CONFIG_ID", referencedColumnName = "ID")
    private DC5LinkConfigJpaEntity linkConfiguration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isEnabled() {
        return enabled;
    }
    public Boolean getEnabled() { return enabled;}

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
    }

    public DC5LinkConfigJpaEntity getLinkConfiguration() {
        return linkConfiguration;
    }

    public void setLinkConfiguration(DC5LinkConfigJpaEntity linkConfiguration) {
        this.linkConfiguration = linkConfiguration;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

}
