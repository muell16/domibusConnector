package eu.domibus.connector.backend.domain.model;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

public class DomibusConnectorBackendMessage {

    private DomibusConnectorMessage domibusConnectorMessage;

    private DomibusConnectorBackendClientInfo backendClientInfo;

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

}
