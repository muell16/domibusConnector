package eu.domibus.connector.backend.domain.model;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.springframework.core.style.ToStringCreator;

import java.io.Serializable;

public class DomibusConnectorBackendMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private DomibusConnectorMessage domibusConnectorMessage;

    private DomibusConnectorBackendClientInfo backendClientInfo;

    public DomibusConnectorBackendMessage() {}

    public DomibusConnectorBackendMessage(DomibusConnectorMessage message, DomibusConnectorBackendClientInfo clientInfo) {
        this.domibusConnectorMessage = message;
        this.backendClientInfo = clientInfo;
    }

    public DomibusConnectorMessage getDomibusConnectorMessage() {
        return domibusConnectorMessage;
    }

    public void setDomibusConnectorMessage(DomibusConnectorMessage domibusConnectorMessage) {
        this.domibusConnectorMessage = domibusConnectorMessage;
    }

    public DomibusConnectorBackendClientInfo getBackendClientInfo() {
        return backendClientInfo;
    }

    public void setBackendClientInfo(DomibusConnectorBackendClientInfo backendClientInfo) {
        this.backendClientInfo = backendClientInfo;
    }

    public String toString() {
        ToStringCreator builder = new ToStringCreator(this);
        builder.append("message", this.domibusConnectorMessage);
        builder.append("backendClient", this.backendClientInfo);
        return builder.toString();
    }
}
