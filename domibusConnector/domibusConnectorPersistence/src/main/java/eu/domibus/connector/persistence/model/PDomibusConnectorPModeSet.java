package eu.domibus.connector.persistence.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = PDomibusConnectorPModeSet.TABLE_NAME)
public class PDomibusConnectorPModeSet {

    public static final String TABLE_NAME = "DC_PMODE_SET";


    @Id
    @Column(name = "ID")
    @TableGenerator(name = "seq" + TABLE_NAME,
            table = PDomibusConnectorPersistenceModel.SEQ_STORE_TABLE_NAME,
            pkColumnName = PDomibusConnectorPersistenceModel.SEQ_NAME_COLUMN_NAME,
            pkColumnValue = TABLE_NAME + ".ID",
            valueColumnName = PDomibusConnectorPersistenceModel.SEQ_VALUE_COLUMN_NAME,
            initialValue = PDomibusConnectorPersistenceModel.INITIAL_VALUE,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq" + TABLE_NAME)
    private Long id;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATED")
    private Timestamp created;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "pModeSet")
    private List<PDomibusConnectorParty> parties = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "pModeSet")
    private List<PDomibusConnectorAction> actions = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "pModeSet")
    private List<PDomibusConnectorService> services = new ArrayList<>();

    @PrePersist
    public void prePersiste() {
        this.created = Timestamp.from(Instant.now());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public List<PDomibusConnectorParty> getParties() {
        return parties;
    }

    public void setParties(List<PDomibusConnectorParty> parties) {
        this.parties = parties;
    }
}

