package eu.ecodex.dc.core.model;

import lombok.Data;

import javax.persistence.*;

@Entity(name = DC5Payload.TABLE_NAME)
@Data
public class DC5Payload {

    public static final String TABLE_NAME = "DC5_PAYLOAD";

    @Id
    @Column(name="ID", nullable = false)
    @TableGenerator(name = "seq" + TABLE_NAME,
            table = DC5PersistenceSettings.SEQ_STORE_TABLE_NAME,
            pkColumnName = DC5PersistenceSettings.SEQ_NAME_COLUMN_NAME,
            pkColumnValue = TABLE_NAME + ".ID",
            valueColumnName = DC5PersistenceSettings.SEQ_VALUE_COLUMN_NAME,
            initialValue = DC5PersistenceSettings.INITIAL_VALUE,
            allocationSize = DC5PersistenceSettings.ALLOCATION_SIZE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq" + TABLE_NAME)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private DC5MsgProcess process;

    @Column(name = "HASH")
    private String hash;

    @Column(name = "NAME")
    private String name;

    @Column(name = "STORAGE_REF")
    private String storageRef;

    @Column(name = "SIZE")
    private int size;


}
