package eu.domibus.connector.persistence.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "DOMIBUS_CONNECTOR_MSG_ERROR")
public class PDomibusConnectorMessageError {

    private static final Logger LOGGER = LoggerFactory.getLogger(PDomibusConnectorMessageError.class);

    @Id
    @TableGenerator(name = "seqStoreMsgError", table = "DOMIBUS_CONNECTOR_SEQ_STORE", pkColumnName = "SEQ_NAME", pkColumnValue = "DOMIBUS_CONNECTOR_MSG_ERROR.ID", valueColumnName = "SEQ_VALUE", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seqStoreMsgError")
    private Long id;

    @OneToOne
    @JoinColumn(name = "MESSAGE_ID", nullable = false)
    private PDomibusConnectorMessage message;

    /**
     * The short message of the exception
     */
    @Column(name = "ERROR_MESSAGE", length = 2048)
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

    @PrePersist
    public void prePersist() {
        if (this.created == null) {
            this.created = new Date();
        }
        truncateErrorMessage();
    }

    @PreUpdate
    public void preUpdate() {
        truncateErrorMessage();
    }

    private void truncateErrorMessage() {
        if (this.errorMessage.length() > 2047) {
            LOGGER.warn("error message exceeded maximum length -> truncated to length of 2047");
            this.errorMessage = this.errorMessage.substring(0, 2047);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PDomibusConnectorMessage getMessage() {
        return message;
    }

    public void setMessage(PDomibusConnectorMessage message) {
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

    @Override
    public String toString() {
        ToStringBuilder toString = new ToStringBuilder(this);
        toString.append("message", message);
        toString.append("errorMessage", errorMessage);
        toString.append("detailedText", detailedText);
        toString.append("errorSource", errorSource);
        return toString.build();
    }

}
