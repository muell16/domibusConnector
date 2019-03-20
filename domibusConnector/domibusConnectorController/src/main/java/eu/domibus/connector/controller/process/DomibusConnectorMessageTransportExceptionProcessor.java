package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.exception.DomibusConnectorMessageTransportException;
import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;

/**
 * Connector Controller internal API
 * used to process errors, failures occurred during processing
 * message
 *
 *
 */
public interface DomibusConnectorMessageTransportExceptionProcessor {

    void processMessageTransportException(DomibusConnectorMessageTransportException messageTransportException);

}
