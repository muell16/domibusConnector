package eu.dc5.domain.model;

import javax.persistence.*;

@Entity(name = DC5ContentEcodex.TABLE_NAME)
public class DC5ContentEcodex {

    public static final String TABLE_NAME = "DC5_ECODEX_CONTENT";

    @Id
    @Column(name = "ID", nullable = false)
    @TableGenerator(name = "seq" + TABLE_NAME,
            table = DC5PersistenceSettings.SEQ_STORE_TABLE_NAME,
            pkColumnName = DC5PersistenceSettings.SEQ_NAME_COLUMN_NAME,
            pkColumnValue = TABLE_NAME + ".ID",
            valueColumnName = DC5PersistenceSettings.SEQ_VALUE_COLUMN_NAME,
            initialValue = DC5PersistenceSettings.INITIAL_VALUE,
            allocationSize = DC5PersistenceSettings.ALLOCATION_SIZE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq" + TABLE_NAME)
    private Long id;

    @OneToOne(targetEntity = DC5Payload.class, cascade = CascadeType.ALL, optional = true) // unidirectional mapping
    private DC5Payload asics;

    @OneToOne(targetEntity = DC5Payload.class, cascade = CascadeType.ALL, optional = true) // unidirectional mapping
    private DC5Payload businessXml;

    @OneToOne(targetEntity = DC5Payload.class, cascade = CascadeType.ALL, optional = true) // unidirectional mapping
    private DC5Payload tokenXml;
}
