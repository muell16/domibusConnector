package eu.domibus.connector.common.db.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "DOMIBUS_CONNECTOR_MSG_ERROR")
public class DomibusConnectorMessageError {

    @Id
    @TableGenerator(name = "seqStore", table = "DOMIBUS_CONNECTOR_SEQ_STORE", pkColumnName = "SEQ_NAME", pkColumnValue = "DOMIBUS_CONNECTOR_MSG_ERROR.ID", valueColumnName = "SEQ_VALUE", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seqStore")
    private Long id;

    @OneToOne
    @JoinColumn(name = "MESSAGE_ID", nullable = false)
    private DomibusConnectorMessage message;

    /**
     * The short message of the exception
     */
    @Column(name = "ERROR_MESSAGE")
    private String errorMessage;

    /**
     * contains the stack trace, if given
     */
    @Column(name = "DETAILED_TEXT")
    private String detailedText;

    /**
     * usually contains the full qualified class name where error happened
     */
    @Column(name = "ERROR_SOURCE")
    private String errorSource;

    @Column(name = "CREATED", nullable = false)
    private Date created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DomibusConnectorMessage getMessage() {
        return message;
    }

    public void setMessage(DomibusConnectorMessage message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getDetailedText() {
        return detailedText;
    }

    public void setDetailedText(String detailedText) {
        this.detailedText = detailedText;
    }

    public String getErrorSource() {
        return errorSource;
    }

    public void setErrorSource(String errorSource) {
        this.errorSource = errorSource;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

}
