package eu.dc5.domain.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = DC5ContentBusinessDocument.TABLE_NAME)
public class DC5ContentBusinessDocument {
    public static final String TABLE_NAME = "DC5_BUSINESS_DOCUMENT_CONTENT";

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "ID")
    private DC5Msg refMsg;

    // idx 0 is current state.
    @OneToMany(mappedBy = "refContent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DC5BusinessDocumentState> state;

    public DC5ContentBusinessDocument() {
        state = new ArrayList<>();
        state.add(new DC5BusinessDocumentState()); // TODO: discuss
    }

    public void addState(DC5BusinessDocumentState state) {
        this.state.add(state);
        state.setRefContent(this);
    }

    public void removeState(DC5BusinessDocumentState state) {
        this.state.remove(state);
        state.setRefContent(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DC5ContentBusinessDocument )) return false;
        return id != null && id.equals(((DC5ContentBusinessDocument) o).getId());
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

    public DC5BusinessDocumentState getState() {
        return state.get(0);
    }

    public void setState(DC5BusinessDocumentState state) {
        addState(state);
    }

    public DC5Msg getRefMsg() {
        return refMsg;
    }

    public void setRefMsg(DC5Msg refMsg) {
        this.refMsg = refMsg;
        this.id = refMsg.getId();
    }
}
