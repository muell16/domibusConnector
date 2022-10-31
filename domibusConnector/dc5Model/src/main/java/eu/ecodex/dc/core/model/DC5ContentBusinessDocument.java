package eu.ecodex.dc.core.model;

import lombok.Data;

import javax.annotation.Generated;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity(name = DC5ContentBusinessDocument.TABLE_NAME)
@Data
public class DC5ContentBusinessDocument {
    public static final String TABLE_NAME = "DC5_BUSINESS_DOCUMENT_CONTENT";

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID")
    private DC5Msg refMsg;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DC5BusinessDocumentState> states = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull
    private DC5BusinessDocumentState currentState;

    @OneToOne(cascade = CascadeType.ALL)
    private DC5ContentBackend backendContent;

    @OneToOne(cascade = CascadeType.ALL)
    private DC5ContentEcodex ecodexContent;

    public DC5ContentBusinessDocument() {
    }

    public void setCurrentState(DC5BusinessDocumentState state) {
        states.add(state);
        this.currentState = state;
        //TODO: maybe issue event?
    }


}
