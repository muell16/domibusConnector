package eu.dc5.domain.model;

import javax.persistence.*;
import java.util.Optional;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DC5ContentEcodex )) return false;
        return id != null && id.equals(((DC5ContentEcodex) o).getId());
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

    public Optional<DC5Payload> getAsics() {
        return Optional.ofNullable(asics);
    }

    public void setAsics(DC5Payload asics) {
        this.asics = asics;
    }

    public Optional<DC5Payload> getBusinessXml() {
        return Optional.ofNullable(businessXml);
    }

    public void setBusinessXml(DC5Payload businessXml) {
        this.businessXml = businessXml;
    }

    public Optional<DC5Payload> getTokenXml() {
        return Optional.ofNullable(tokenXml);
    }

    public void setTokenXml(DC5Payload tokenXml) {
        this.tokenXml = tokenXml;
    }
}
