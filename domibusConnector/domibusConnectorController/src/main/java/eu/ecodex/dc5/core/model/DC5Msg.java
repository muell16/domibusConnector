package eu.ecodex.dc5.core.model;


import eu.ecodex.dc5.message.model.DC5Ebms;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Entity
@Table(name = DC5Msg.TABLE_NAME)
public class DC5Msg implements Serializable {

    public static final String TABLE_NAME = "DC5_Message";

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

//    @OneToOne(mappedBy = "refMessage", cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY, optional = false) // even with lazy this is still loaded eagerly in the non-owning side ...
//    @LazyToOne(LazyToOneOption.NO_PROXY) // to prevent that (n+1 query problem), one needs bytecode enhancement which requires hibernate.
//    private DC5Ebms ebmsSegment;

    @NotNull
    @OneToOne(targetEntity = DC5Ebms.class, cascade = CascadeType.ALL, optional = false) // unidirectional mapping
    private DC5Ebms ebmsSegment;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private DC5Ebms refEbms;

    @OneToOne(targetEntity = DC5TransportRequest.class, cascade = CascadeType.ALL, optional = true) // unidirectional mapping
    private DC5TransportRequest transportRequest;

    @OneToOne(mappedBy = "refMsg", cascade = CascadeType.ALL) // TODO: orphanRemoval = true?
    @Nullable
    private DC5ContentBusinessDocument content;

    @OneToMany(mappedBy = "refMsg", cascade = CascadeType.ALL) // TODO: should a confirmation be deleted if, no Msg references it? orphanRemoval=true?
    private List<DC5MsgBusinessConfirmation> confirmations;

    @Column(name = "DC5_BACKEND_LINK", length = 255)
    private String backendLink;

    @Column(name = "DC5_GATEWAY_LINK", length = 255)
    private String gwLink;

    @Column(name = "DC5_MESSAGE_SOURCE", length = 255)
    private String source;
    @Column(name = "DC5_MESSAGE_TARGET", length = 255)
    private String target;

    @ManyToOne
    private DC5Domain businessDomain;

    public DC5Msg() {
        confirmations = new ArrayList<>();
    }

    public List<DC5MsgBusinessConfirmation> getConfirmations() {
        return confirmations;
    }

    public void addConfirmation(DC5MsgBusinessConfirmation confirmation) {
        confirmations.add(confirmation);
        confirmation.setRefMsg(this);
    }

    public void removeConfirmation(DC5MsgBusinessConfirmation confirmation) {
        confirmations.remove(confirmation);
        confirmation.setRefMsg(null);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        // if there is no id, fallback to comparing by reference
        if (this == o) return true;

        if (!(o instanceof DC5Msg))
            return false;

        DC5Msg other = (DC5Msg) o;

        return id != null && id.equals(other.getId());
    }

    // just getter & setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DC5Ebms getEbmsSegment() {
        return ebmsSegment;
    }

    public Optional<DC5ContentBusinessDocument> getContent() {
        return Optional.ofNullable(content);
    }

    public void setContent(DC5ContentBusinessDocument content) {
        this.content = content;
    }

    public void setEbmsSegment(DC5Ebms ebmsSegment) {
        this.ebmsSegment = ebmsSegment;
    }

    public DC5TransportRequest getTransportRequest() {
        return transportRequest;
    }

    public void setTransportRequest(DC5TransportRequest transportRequest) {
        this.transportRequest = transportRequest;
    }

    public String getBackendLink() {
        return backendLink;
    }

    public void setBackendLink(String backendLink) {
        this.backendLink = backendLink;
    }

    public String getGwLink() {
        return gwLink;
    }

    public void setGwLink(String gwLink) {
        this.gwLink = gwLink;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setDomain(DC5Domain domain) {
        this.businessDomain = domain;
    }

    public DC5Domain getBusinessDomain() {
        return businessDomain;
    }
}
