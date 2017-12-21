package eu.domibus.connector.persistence.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DOMIBUS_CONNECTOR_ACTION")
public class DomibusConnectorAction implements Serializable {

    @Id
    @Column(name = "ACTION")
    private String action;

    @Column(name = "PDF_REQUIRED")
    private boolean pdfRequired;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isPdfRequired() {
        return pdfRequired;
    }

    public void setPdfRequired(boolean pdfRequired) {
        this.pdfRequired = pdfRequired;
    }

}
