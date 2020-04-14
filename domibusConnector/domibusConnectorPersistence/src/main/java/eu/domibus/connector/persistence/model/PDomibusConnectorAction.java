package eu.domibus.connector.persistence.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "DOMIBUS_CONNECTOR_ACTION")
public class PDomibusConnectorAction implements Serializable {

    @Id
    @Column(name = "ACTION")
    private String action;

    @Column(name = "PDF_REQUIRED")
    private boolean documentRequired;

    @ManyToOne
    @JoinColumn(name = "FK_PMODE_SET", referencedColumnName = "ID")
    private PDomibusConnectorPModeSet pModeSet;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isDocumentRequired() {
        return documentRequired;
    }

    public void setDocumentRequired(boolean pdfRequired) {
        this.documentRequired = pdfRequired;
    }

    @Override
    public String toString() {
        ToStringBuilder toString = new ToStringBuilder(this);
        toString.append("action", action);
        toString.append("documentRequired", documentRequired);
        return toString.build();
    }

}
