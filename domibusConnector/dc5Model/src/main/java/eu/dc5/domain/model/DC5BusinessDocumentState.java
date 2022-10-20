package eu.dc5.domain.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = DC5BusinessDocumentState.TABLE_NAME)
public class DC5BusinessDocumentState {

    public static final String TABLE_NAME = "DC5_DOC_STATE_TRANS";

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
    private DC5ContentBusinessDocument refContent;

    @Column(name = "STATE", length = 1)
    private DC5BusinessDocumentStates state = DC5BusinessDocumentStates.CREATED; // TODO: discuss

    @Column(name = "CREATED")
    private LocalDateTime created;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "USER_PRINCIPAL")
    private String userPrincipal;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DC5BusinessDocumentState )) return false;
        return id != null && id.equals(((DC5BusinessDocumentState) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    // just getter & setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DC5ContentBusinessDocument getRefContent() {
        return refContent;
    }

    public void setRefContent(DC5ContentBusinessDocument refContent) {
        this.refContent = refContent;
    }

    public DC5BusinessDocumentStates getState() {
        return state;
    }

    public void setState(DC5BusinessDocumentStates state) {
        this.state = state;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserPrincipal() {
        return userPrincipal;
    }

    public void setUserPrincipal(String userPrincipal) {
        this.userPrincipal = userPrincipal;
    }
}
