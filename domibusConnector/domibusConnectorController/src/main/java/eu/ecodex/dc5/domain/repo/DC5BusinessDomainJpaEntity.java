package eu.ecodex.dc5.domain.repo;

import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.persistence.model.PDomibusConnectorPersistenceModel;
import eu.ecodex.dc5.domain.model.BusinessDomainIdConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.util.HashMap;
import java.util.Map;

/**
 * In the future the connector will supoort multiple UseCases, Backends, Gateways
 * This Entity describes a configuration set for message processing and will be used
 * to process the message
 */
@Entity
@Table(name = DC5BusinessDomainJpaEntity.TABLE_NAME)

@Getter
@Setter
public class DC5BusinessDomainJpaEntity {

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

    @Column(name= "NAME", unique = true, nullable = false, length = 255)
    @Convert(converter = BusinessDomainIdConverter.class)
    private DomibusConnectorBusinessDomain.BusinessDomainId name;

    @Lob
    @Column(name = "DESCRIPTION")
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "DC5_DOMAIN_PROPERTY", joinColumns=@JoinColumn(name="DC5_DOMAIN_ID", referencedColumnName = "ID"))
    @MapKeyColumn (name="PROPERTY_NAME", nullable = false)
    @Column(name="PROPERTY_VALUE", length = 2048)
    private Map<String, String> properties = new HashMap<String, String>();

}
