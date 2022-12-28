package eu.domibus.connector.persistence.model;

import eu.ecodex.dc5.util.model.DC5ConfigItem;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;


//@Entity
//@Table(name = PDomibusConnectorPModeSet.TABLE_NAME)
//
//@Setter
//@Getter
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
            allocationSize = PDomibusConnectorPersistenceModel.ALLOCATION_SIZE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq" + TABLE_NAME)
    private Long id;

    @Lob
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATED")
    private Timestamp created;

//    @ManyToOne
//    @JoinColumn(name = "FK_MESSAGE_LANE", referencedColumnName = "ID")
//    private PDomibusConnectorMessageLane messageLane;

    @Column(name = "ACTIVE")
    private boolean active;

    @Lob
    @Column(name = "PMODES")
    private byte[] pmodes;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "pModeSet", fetch = FetchType.EAGER)
    private Set<PDomibusConnectorParty> parties = new HashSet<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "pModeSet", fetch = FetchType.EAGER)
    private Set<PDomibusConnectorAction> actions = new HashSet<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "pModeSet", fetch = FetchType.EAGER)
    private Set<PDomibusConnectorService> services = new HashSet<>();
    
    @ManyToOne
    @JoinColumn(name = "FK_CONNECTORSTORE", referencedColumnName = "ID")
    private DC5ConfigItem connectorstore;


	@PrePersist
    public void prePersist() {
        this.created = Timestamp.from(Instant.now());
        this.parties.forEach(p -> p.setpModeSet(this));
        this.actions.forEach(a -> a.setpModeSet(this));
        this.services.forEach(s -> s.setpModeSet(this));
    }

}

