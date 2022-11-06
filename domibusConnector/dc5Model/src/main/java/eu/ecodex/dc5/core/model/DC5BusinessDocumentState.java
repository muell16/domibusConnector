package eu.ecodex.dc5.core.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity(name = DC5BusinessDocumentState.TABLE_NAME)
@Data
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

    public DC5BusinessDocumentState() {}

    public DC5BusinessDocumentState(DC5BusinessDocumentStatesEnum state) {
        this.created = LocalDateTime.now();
        this.state = state;
    }

    //TODO: add converter...
    @NotNull
    @Column(name = "STATE", length = 1)
    private DC5BusinessDocumentStatesEnum state;

    @Column(name = "CREATED")
    private LocalDateTime created;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "USER_PRINCIPAL")
    private String userPrincipal;

}
