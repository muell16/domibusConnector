package eu.domibus.connector.link.service;

import eu.domibus.connector.controller.exception.DomibusConnectorBackendException;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import liquibase.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static eu.domibus.connector.link.service.DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME;


public class DCPluginBasedBackendDeliveryService implements DomibusConnectorBackendDeliveryService {

    private static final Logger LOGGER = LogManager.getLogger(DomibusConnectorBackendDeliveryService.class);

    @Autowired
    DCActiveLinkManagerService linkManager;

    @Override
    public void deliverMessageToBackend(DomibusConnectorMessage message) throws DomibusConnectorBackendException {
        String backendName = message.getMessageDetails().getConnectorBackendClientName();
        if (StringUtils.isEmpty(backendName)) {
            throw new DomibusConnectorBackendException("No BackendName provided!");
        }
        DomibusConnectorLinkPartner.LinkPartnerName backendLinkPartnerName = new DomibusConnectorLinkPartner.LinkPartnerName(backendName);

        SubmitToLink submitToLinkPartner = linkManager.getSubmitToLinkPartner(backendName);
        if (submitToLinkPartner == null) {
            throw new DomibusConnectorControllerException("No Gateway Link available or configured!");
        }
        try {
            submitToLinkPartner.submitToLink(message, backendLinkPartnerName);
        } catch (Exception e) {
            throw new DomibusConnectorControllerException("Submission failed!", e);
        }
    }
}
