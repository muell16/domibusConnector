package eu.domibus.connector.persistence.model;

import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;

import javax.persistence.*;

import static eu.domibus.connector.persistence.model.PDomibusConnectorPersistenceModel.SEQ_STORE_TABLE_NAME;

/**
 * In the future the connector will supoort multiple UseCases, Backends, Gateways
 * This Entity describes a configuration set for message processing and will be used
 * to process the message
 */
@Entity
@Table(name = PDomibusConnectorMessageLane.TABLE_NAME)
public class PDomibusConnectorMessageLane {

    public static final String TABLE_NAME = "DC_MESSAGE_LANE";

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

    @Column(name= "NAME", unique = true, length = 255)
    private DomibusConnectorMessageLane.MessageLaneId name;

    @Column(name = "DESCRIPTION")
    @Lob
    private String description;

//    @ManyToOne(optional = true)
//    private PDomibusConnectorMessageLane parent;

    //TODO: add properties...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DomibusConnectorMessageLane.MessageLaneId getName() {
        return name;
    }

    public void setName(DomibusConnectorMessageLane.MessageLaneId name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
