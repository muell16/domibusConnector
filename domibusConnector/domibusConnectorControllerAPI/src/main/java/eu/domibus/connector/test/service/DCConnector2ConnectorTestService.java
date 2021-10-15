package eu.domibus.connector.test.service;

import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;

public interface DCConnector2ConnectorTestService {


    /**
     * @param testMessage - a test message
     */
    public void submitTestMessage(DomibusConnectorMessageType testMessage);

    /**
     * returns the configured name of the test backend, every message sent to this
     * backend is a connector to connector test message
     *
     * @return the backend name
     */
    public String getTestBackendName();

}
