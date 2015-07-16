package eu.ecodex.connector.common.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ECODEX_ACTION")
public class ECodexAction {

    @Id
    @Column(name = "ECDX_ACTION")
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
