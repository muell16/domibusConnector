package eu.domibus.connector.persistence.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.sql.Blob;
import java.util.Date;

@Entity
@Table(name = "DOMIBUS_CONNECTOR_BIGDATA")
public class PDomibusConnectorBigData {

    @Id
    @Column(name="ID")
    @TableGenerator(name = "seqStoreBigData", table = "DOMIBUS_CONNECTOR_SEQ_STORE", pkColumnName = "SEQ_NAME", pkColumnValue = "DOMIBUS_CONNECTOR_BIGDATA.ID", valueColumnName = "SEQ_VALUE", initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seqStoreBigData")
    private Long id;

    @Column(name="NAME")
    private String name;

    @Column(name="LAST_ACCESS")
    private Date lastAccess;

    @Column(name="MIMETYPE")
    private String mimeType;

    @Column(name="CONTENT")
    private Blob content;

//    @ManyToOne(fetch=FetchType.LAZY)
//    @JoinColumn(name="MESSAGE_ID", referencedColumnName="ID")
//    private PDomibusConnectorMessage message;
    @Column(name="MESSAGE_ID")
    private Long message;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Blob getContent() {
        return content;
    }

    public void setContent(Blob content) {
        this.content = content;
    }

    public Long getMessage() {
        return message;
    }

    public void setMessage(Long message) {
        this.message = message;
    }

    @Override
    public String toString() {
        ToStringBuilder toString = new ToStringBuilder(this);
        toString.append("id", this.id);
        toString.append("referencedMessage", this.message);
        return toString.build();
    }
}
