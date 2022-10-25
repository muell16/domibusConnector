package eu.ecodex.dc.core.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity(name = DC5ContentBackend.TABLE_NAME)
public class DC5ContentBackend {

    public static final String TABLE_NAME = "DC5_BACKEND_CONTENT";

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
    private DC5Payload businessXml;

    @OneToOne(targetEntity = DC5Payload.class, cascade = CascadeType.ALL, optional = true) // unidirectional mapping
    private DC5Payload businessDocument;

    @OneToOne(targetEntity = DC5Payload.class, cascade = CascadeType.ALL, optional = true) // unidirectional mapping
    private DC5Payload detachedSignature;

    @OneToMany(
            mappedBy = "",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<DC5Payload> attachments = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DC5ContentBackend )) return false;
        return id != null && id.equals(((DC5ContentBackend) o).getId());
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

    public Optional<DC5Payload> getBusinessXml() {
        return Optional.ofNullable(businessXml);
    }

    public void setBusinessXml(DC5Payload businessXml) {
        this.businessXml = businessXml;
    }

    public Optional<DC5Payload> getBusinessDocument() {
        return Optional.ofNullable(businessDocument);
    }

    public void setBusinessDocument(DC5Payload businessDocument) {
        this.businessDocument = businessDocument;
    }

    public Optional<DC5Payload> getOptionalDetachedSignature() {
        return Optional.ofNullable(detachedSignature);
    }

    public void setOptionalDetachedSignature(DC5Payload detachedSignature) {
        this.detachedSignature = detachedSignature;
    }

    public Set<DC5Payload> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<DC5Payload> attachments) {
        this.attachments = attachments;
    }
}
