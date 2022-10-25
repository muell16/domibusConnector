package eu.ecodex.dc.core.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;


@Entity(name = DC5MsgProcess.TABLE_NAME)
public class DC5MsgProcess {

    public static final String TABLE_NAME = "DC5_MSG_PROCESS";

    @Id
    @Column(name = "ID", nullable = false)
    @TableGenerator(name = "seq" + TABLE_NAME,
            table = DC5PersistenceSettings.SEQ_STORE_TABLE_NAME,
            pkColumnName = DC5PersistenceSettings.SEQ_NAME_COLUMN_NAME,
            pkColumnValue = TABLE_NAME + ".ID",
            valueColumnName = DC5PersistenceSettings.SEQ_VALUE_COLUMN_NAME,
            initialValue = DC5PersistenceSettings.INITIAL_VALUE,
            allocationSize = DC5PersistenceSettings.ALLOCATION_SIZE)
    private Long id;

    // TODO: why not this
//    @OneToOne(fetch = FetchType.LAZY)
//    @MapsId // use the message ids as process ids.
//    @JoinColumn(name = "ID") // rename the colum from message_id to id
    @OneToOne(targetEntity = DC5Msg.class, cascade = CascadeType.ALL, optional = true) // TODO: cascade ?
    private DC5Msg message;

    @OneToOne(targetEntity = DC5Domain.class, cascade = CascadeType.ALL, optional = true) // TODO: cascade ?
    private DC5Domain domain;

    @Column(name = "PROCESS_ID")
    private String processId;

    @Column(name = "CREATED")
    private LocalDateTime created;

    @Column(name = "FINISHED")
    private LocalDateTime finished;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = TABLE_NAME + DC5PersistenceSettings.PROPERTY_SUFFIX,
            joinColumns = @JoinColumn(name = TABLE_NAME,
                    referencedColumnName = "ID"))
    @MapKeyColumn(name = "PROPERTY_NAME", nullable = false)
    @Column(name = "PROPERTY_VALUE", length = 2048)
    private Map<String, String> properties; // TODO: does DC5MsgProcessProperty really need to be a class?

    //    private Map<DC5ProcStep, ZonedDateTime> currProcStep;
//    private DC5Domain optionalDomain;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DC5MsgProcess)) return false;
        return id != null && id.equals(((DC5MsgProcess) o).getId());
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

    public DC5Msg getMessage() {
        return message;
    }

    public void setMessage(DC5Msg message) {
        this.message = message;
    }

    public DC5Domain getDomain() {
        return domain;
    }

    public void setDomain(DC5Domain domain) {
        this.domain = domain;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getFinished() {
        return finished;
    }

    public void setFinished(LocalDateTime finished) {
        this.finished = finished;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
