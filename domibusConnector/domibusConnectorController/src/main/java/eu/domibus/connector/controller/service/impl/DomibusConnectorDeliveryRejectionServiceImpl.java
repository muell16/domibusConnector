package eu.domibus.connector.controller.service.impl;

import eu.domibus.connector.controller.exception.DomibusConnectorRejectDeliveryException;
import eu.domibus.connector.controller.process.DomibusConnectorDeliveryRejectionProcessor;
import eu.domibus.connector.controller.service.DomibusConnectorDeliveryRejectionService;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomibusConnectorDeliveryRejectionServiceImpl implements DomibusConnectorDeliveryRejectionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorDeliveryRejectionServiceImpl.class);

    @Autowired
    DomibusConnectorDeliveryRejectionProcessor domibusConnectorDeliveryRejectionProcessor;

    /**
     * Handle a by the backend rejected message
     * @param cause
     */
    @Override
    public void rejectDelivery(DomibusConnectorRejectDeliveryException cause) {
        LOGGER.info(LoggingMarker.BUSINESS_LOG, "Message [{}] has been rejected by the backend! Because of [{}]", cause.getConnectorMessage().getConnectorMessageId(), cause.getMessage());
        domibusConnectorDeliveryRejectionProcessor.processMessage(cause.getConnectorMessage());
    }

}
