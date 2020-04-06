package eu.domibus.connector.domain.model;

public class DomibusConnectorMessageProcessId {

    private String processId;

    public DomibusConnectorMessageProcessId(String processId) {
        this.processId = processId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }
}
