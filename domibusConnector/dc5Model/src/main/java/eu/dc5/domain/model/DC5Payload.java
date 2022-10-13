package eu.dc5.domain.model;

import javax.persistence.*;

@Entity(name = DC5Payload.TABLE_NAME)
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

    // TODO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MESSAGE_ID")
    private DC5Msg message;

    @ManyToOne(fetch = FetchType.LAZY)
    private DC5ContentBackend backendContent;

    @Column(name = "TYPE")
    private DC5PayloadType type;

    @Column(name = "HASH")
    private String hash;

    @Column(name = "NAME")
    private String name;

    @Column(name = "STORAGE_REF")
    private String storageRef;

    @Column(name = "SIZE")
    private int size;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DC5Payload )) return false;
        return id != null && id.equals(((DC5Payload) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DC5Msg getMessage() {
        return message;
    }

    public void setMessage(DC5Msg message) {
        this.message = message;
    }

    public DC5ContentBackend getBackendContent() {
        return backendContent;
    }

    public void setBackendContent(DC5ContentBackend backendContent) {
        this.backendContent = backendContent;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStorageRef() {
        return storageRef;
    }

    public void setStorageRef(String storageRef) {
        this.storageRef = storageRef;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
