package eu.domibus.connector.persistence.model;

import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.persistence.model.PDomibusConnectorDomain;

import javax.persistence.*;

import java.util.HashMap;
import java.util.Map;

import static eu.domibus.connector.persistence.model.PDomibusConnectorPersistenceModel.SEQ_STORE_TABLE_NAME;

@Table(name = "DC_LINK_INFO")
@Entity
public class PDomibusConnectorLinkInfo {

    @Id
    @Column(name="ID")
    @TableGenerator(name = "seqStoreLinkInfo", table = SEQ_STORE_TABLE_NAME, pkColumnName = "SEQ_NAME", pkColumnValue = "DC_LINK_INFO.ID", valueColumnName = "SEQ_VALUE", initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seqStoreLinkInfo")
    private Long id;

    @Column(name= "NAME", unique = true, length = 255)
    private String linkName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name="ENABLED")
    private boolean enabled;

    @Column(name = "LINK_TYPE")
    private LinkType linkType;

    @ManyToOne
    @JoinColumn(name = "LINK_CONFIG_ID", referencedColumnName = "ID")
    private PDomibusConnectorLinkConfiguration linkConfiguration;

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
    }

    public PDomibusConnectorLinkConfiguration getLinkConfiguration() {
        return linkConfiguration;
    }

    public void setLinkConfiguration(PDomibusConnectorLinkConfiguration linkConfiguration) {
        this.linkConfiguration = linkConfiguration;
    }
}
