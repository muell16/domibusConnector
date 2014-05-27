package eu.ecodex.webadmin.model.connector;

import java.io.Serializable;
import java.util.List;

import eu.ecodex.connector.common.db.model.ECodexEvidence;
import eu.ecodex.connector.common.db.model.ECodexMessageInfo;

public class MessageReportDO extends ECodexMessageInfo implements Serializable {

    private static final long serialVersionUID = -5053455211577710753L;

    public MessageReportDO() {
        super();
    }

    public MessageReportDO(ECodexMessageInfo eCodexMessageInfo) {
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

    private List<ECodexEvidence> evidenceList;

    public String getEvidenceHistory() {
        return evidenceHistory;
    }

    public void setEvidenceHistory(String evidenceHistory) {
        this.evidenceHistory = evidenceHistory;
    }

    public List<ECodexEvidence> getEvidenceList() {
        return evidenceList;
    }

    public void setEvidenceList(List<ECodexEvidence> evidenceList) {
        this.evidenceList = evidenceList;
    }

    public String getLastEvidenceType() {
        return lastEvidenceType;
    }

    public void setLastEvidenceType(String lastEvidenceType) {
        this.lastEvidenceType = lastEvidenceType;
    }

}
