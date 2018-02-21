
package eu.domibus.connector.controller.service.impl;

import eu.domibus.connector.controller.service.DomibusConnectorBackendSubmissionService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * 
 */
@Service
public class ReceiveMessageFromBackendService implements DomibusConnectorBackendSubmissionService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ReceiveMessageFromBackendService.class);
    
    @Override
    public void submitToController(DomibusConnectorMessage message) {
        LOGGER.error("not yet implemented!");
    }

}
