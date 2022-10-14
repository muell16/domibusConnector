package eu.dc5.domain.model;


import javax.persistence.*;
import java.util.Optional;


@Entity(name = DC5MsgBusinessConfirmation.TABLE_NAME)
public class DC5MsgBusinessConfirmation {

    public static final String TABLE_NAME = "DC5_BUSINESS_CONFIRMATION";

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

    // TODO: ref message, getter setter

    @OneToOne(targetEntity = DC5Payload.class, cascade = CascadeType.ALL, optional = true)
    private DC5Payload evidenceXml;

    public Optional<DC5Payload> getEvidenceXml() {
        return Optional.ofNullable(evidenceXml);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DC5MsgBusinessConfirmation )) return false;
        return id != null && id.equals(((DC5MsgBusinessConfirmation) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
