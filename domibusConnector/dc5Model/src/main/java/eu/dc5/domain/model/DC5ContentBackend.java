package eu.dc5.domain.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = DC5ContentBackend.TABLE_NAME)
public class DC5ContentBackend {

    public static final String TABLE_NAME = "DC5_BACKEND_CONTENT";

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
    private DC5Payload businessXml;

    @OneToOne(targetEntity = DC5Payload.class, cascade = CascadeType.ALL, optional = true) // unidirectional mapping
    private DC5Payload businessDocument;

    @OneToOne(targetEntity = DC5Payload.class, cascade = CascadeType.ALL, optional = true) // unidirectional mapping
    private DC5Payload optionalDetachedSignature;

    @OneToMany(
            mappedBy = "",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<DC5Payload> attachments = new HashSet<>();
}
