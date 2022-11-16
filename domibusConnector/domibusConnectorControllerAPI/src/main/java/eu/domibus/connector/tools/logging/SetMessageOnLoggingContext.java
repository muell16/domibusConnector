package eu.domibus.connector.tools.logging;

import eu.ecodex.dc5.message.model.DC5Message;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import org.slf4j.MDC;

import org.springframework.lang.Nullable;

public class SetMessageOnLoggingContext {


    /**
     * puts the connector message id of the passed message
     * in the logging MDC context
     *  does nothing if domibusConnectorMessage parameter is null
     * @param DC5Message the message
     */
    public static void putConnectorMessageIdOnMDC(@Nullable DC5Message DC5Message) {
        if (DC5Message != null) {
            String connectorMessageId = DC5Message.getConnectorMessageIdAsString();
            putConnectorMessageIdOnMDC(connectorMessageId);
        } else {
            putConnectorMessageIdOnMDC((String)null);
        }
    }

    /**
     * puts the connector message id provided as string
     * in the logging MDC context
     * @param domibusConnectorMessageId - the String to set on MDC
     */
    public static void putConnectorMessageIdOnMDC(@Nullable String domibusConnectorMessageId) {
        if (domibusConnectorMessageId != null) {
            MDC.put(LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_ID_PROPERTY_NAME, domibusConnectorMessageId);
        } else {
            MDC.remove(LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_ID_PROPERTY_NAME);
        }
    }

}
