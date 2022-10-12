package eu.dc5.domain.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = DC5Msg.TABLE_NAME)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DC5_MESSAGE_TYPE_ID",
        discriminatorType = DiscriminatorType.INTEGER,
        length = 1
)
public abstract class DC5Msg implements Serializable {

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

    @OneToOne(targetEntity = DC5Ebms.class, cascade = CascadeType.ALL, optional = false) // unidirectional mapping
    private DC5Ebms ebmsSegment;

    @OneToOne(targetEntity = DC5TransportRequest.class, cascade = CascadeType.ALL, optional = true) // unidirectional mapping
    private DC5TransportRequest transportRequest;

    @Column(name = "DC5_BACKEND_LINK", length = 255)
    private String backendLink;

    @Column(name = "DC5_GATEWAY_LINK", length = 255)
    private String gwLink;

    @Column(name = "DC5_MESSAGE_SOURCE", length = 255)
    private String source;
    @Column(name = "DC5_MESSAGE_TARGET", length = 255)
    private String target;


    @OneToMany(mappedBy = "message", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<DC5Payload> payload = new HashSet<>();

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
}
