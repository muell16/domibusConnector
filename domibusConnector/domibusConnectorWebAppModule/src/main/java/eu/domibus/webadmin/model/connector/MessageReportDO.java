package eu.domibus.webadmin.model.connector;

import java.io.Serializable;
import java.util.List;

import eu.domibus.connector.persistence.model.PDomibusConnectorEvidence;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessageInfo;

public class MessageReportDO extends PDomibusConnectorMessageInfo implements Serializable {

    private static final long serialVersionUID = -5053455211577710753L;

    public MessageReportDO() {
        super();
    }

    public MessageReportDO(PDomibusConnectorMessageInfo eCodexMessageInfo) {
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

    private List<PDomibusConnectorEvidence> evidenceList;

    public String getEvidenceHistory() {
        return evidenceHistory;
    }

    public void setEvidenceHistory(String evidenceHistory) {
        this.evidenceHistory = evidenceHistory;
    }

    public List<PDomibusConnectorEvidence> getEvidenceList() {
        return evidenceList;
    }

    public void setEvidenceList(List<PDomibusConnectorEvidence> evidenceList) {
        this.evidenceList = evidenceList;
    }

    public String getLastEvidenceType() {
        return lastEvidenceType;
    }

    public void setLastEvidenceType(String lastEvidenceType) {
        this.lastEvidenceType = lastEvidenceType;
    }

}
