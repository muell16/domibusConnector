package eu.ecodex.webadmin.model.connector;

import java.io.Serializable;
import java.util.List;

import eu.domibus.connector.common.db.model.DomibusConnectorEvidence;
import eu.domibus.connector.common.db.model.DomibusConnectorMessageInfo;

public class MessageReportDO extends DomibusConnectorMessageInfo implements Serializable {

    private static final long serialVersionUID = -5053455211577710753L;

    public MessageReportDO() {
        super();
    }

    public MessageReportDO(DomibusConnectorMessageInfo eCodexMessageInfo) {
        this.setAction(eCodexMessageInfo.getAction());
        this.setCreated(eCodexMessageInfo.getCreated());
        this.setFinalRecipient(eCodexMessageInfo.getFinalRecipient());
        this.setFrom(eCodexMessageInfo.getFrom());
        this.setId(eCodexMessageInfo.getId());
        this.setMessage(eCodexMessageInfo.getMessage());
        this.setOriginalSender(eCodexMessageInfo.getOriginalSender());
        this.setService(eCodexMessageInfo.getService());
        this.setTo(eCodexMessageInfo.getTo());
        this.setUpdated(eCodexMessageInfo.getUpdated());
    }

    private String lastEvidenceType;

    private String evidenceHistory;

    private List<DomibusConnectorEvidence> evidenceList;

    public String getEvidenceHistory() {
        return evidenceHistory;
    }

    public void setEvidenceHistory(String evidenceHistory) {
        this.evidenceHistory = evidenceHistory;
    }

    public List<DomibusConnectorEvidence> getEvidenceList() {
        return evidenceList;
    }

    public void setEvidenceList(List<DomibusConnectorEvidence> evidenceList) {
        this.evidenceList = evidenceList;
    }

    public String getLastEvidenceType() {
        return lastEvidenceType;
    }

    public void setLastEvidenceType(String lastEvidenceType) {
        this.lastEvidenceType = lastEvidenceType;
    }

}
