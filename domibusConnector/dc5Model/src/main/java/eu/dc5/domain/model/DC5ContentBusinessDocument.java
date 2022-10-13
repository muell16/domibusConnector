package eu.dc5.domain.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = DC5ContentBusinessDocument.TABLE_NAME)
public class DC5ContentBusinessDocument {
    public static final String TABLE_NAME = "DC5_BUSINESS_DOCUMENT_CONTENT";

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

    @OneToOne
    private DC5BusinessDocumentState state;


    @OneToMany(mappedBy = "refContent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DC5BusinessDocumentState> prvStates = new ArrayList<>();

    public void addPrvState(DC5BusinessDocumentState state) {
        prvStates.add(state);
        state.setRefContent(this);
    }

    public void removePrvState(DC5BusinessDocumentState state) {
        prvStates.remove(state);
        state.setRefContent(null);
    }

}
