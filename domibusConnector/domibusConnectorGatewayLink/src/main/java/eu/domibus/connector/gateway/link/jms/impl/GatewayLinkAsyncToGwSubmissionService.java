package eu.domibus.connector.gateway.link.jms.impl;

import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("gwlink-jms")
public class GatewayLinkAsyncToGwSubmissionService implements DomibusConnectorGatewaySubmissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorGatewaySubmissionService.class);


    @Override
    public void submitToGateway(DomibusConnectorMessage message) throws DomibusConnectorGatewaySubmissionException {
        LOGGER.debug("submitToGateway...");
    }


}
