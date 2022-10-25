package eu.ecodex.dc.core.model;


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

    @ManyToOne(fetch = FetchType.LAZY)
    private DC5Msg refMsg;

    // TODO: example
//    public void changeState() {
//        refMsg.getContent().setState();
//    }

    @OneToOne(targetEntity = DC5Payload.class, cascade = CascadeType.ALL, optional = true)
    private DC5Payload evidenceXml;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DC5Msg getRefMsg() {
        return refMsg;
    }

    public void setRefMsg(DC5Msg refMsg) {
        this.refMsg = refMsg;
    }


    public Optional<DC5Payload> getEvidenceXml() {
        return Optional.ofNullable(evidenceXml);
    }

    public void setEvidenceXml(DC5Payload evidenceXml) {
        this.evidenceXml = evidenceXml;
    }
}
