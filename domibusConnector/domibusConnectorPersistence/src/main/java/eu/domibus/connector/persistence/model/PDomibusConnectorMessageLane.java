package eu.domibus.connector.persistence.model;

import javax.persistence.*;

import static eu.domibus.connector.persistence.model.PDomibusConnectorPersistenceModel.SEQ_STORE_TABLE_NAME;

/**
 * In the future the connector will supoort multiple UseCases, Backends, Gateways
 * This Entity describes a configuration set for message processing and will be used
 * to process the message
 */
@Entity
@Table(name = "DC_MESSAGE_LANE")
public class PDomibusConnectorMessageLane {

    @Id
    @Column(name="ID")
    @TableGenerator(name = "seqStoreLinkInfo", table = SEQ_STORE_TABLE_NAME, pkColumnName = "SEQ_NAME", pkColumnValue = "DC_DOMAIN.ID", valueColumnName = "SEQ_VALUE", initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seqStoreLinkInfo")
    private Long id;

    @Column(name= "NAME", unique = true, length = 255)
    private String name;

    @Column(name = "DESCRIPTION")
    @Lob
    private String description;

    @ManyToOne(optional = true)
    private PDomibusConnectorMessageLane parent;

    //TODO: add properties...
}
