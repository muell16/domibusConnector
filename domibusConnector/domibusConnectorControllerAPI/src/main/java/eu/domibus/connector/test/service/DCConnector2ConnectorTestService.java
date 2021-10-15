package eu.domibus.connector.test.service;

import eu.domibus.connector.common.DomibusConnectorDefaults;
import eu.domibus.connector.domain.model.*;

import java.util.List;

/**
 * This interface allowes the UI to access the connector2connector test service
 *  Trigger a test message
 *  List all test messages
 */
public interface DCConnector2ConnectorTestService {


    /**
     * Will create a new DomibusConnectorMessage
     *  will set according to the provided businessDomain the attributes
     *  <ul>
     *      <li>Action</li>
     *      <li>Service</li>
     *      <li>ServiceType</li>
     *  </ul>
     *  will also set a example XML and example pdf for testing
     *  will also set a domibusConnectorMessageId for the test message
     *
     *  Note: the message will not be persisted!
     *
     * @param businessDomain the provided businessDomain
     * @return the example test message
     */
    public DomibusConnectorMessage createTestMessage(DomibusConnectorBusinessDomain.BusinessDomainId businessDomain);

    /**
     * Will take the provided testMessage and
     *  submit it to the connector (put it on toConnectorQueue)
     *
     *  The message itself must be a valid message! Use {@link #createTestMessage} to get a valid test message.
     *
     * @param testMessage - a test message
     */
    public void submitTestMessage(DomibusConnectorMessage testMessage);

    /**
     * List all messages with backend name equals {@link DomibusConnectorDefaults#DEFAULT_TEST_BACKEND} and
     * business domain equals businessDomain
     *
     * @param businessDomain - business Domain
     * @return - list of connector messages
     *
     * Question?: pagination?
     *
     */
    public List<DomibusConnectorMessage> getTestMessages(DomibusConnectorBusinessDomain.BusinessDomainId businessDomain);

}
