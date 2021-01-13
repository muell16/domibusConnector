package eu.domibus.connector.link.service;

import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static eu.domibus.connector.link.service.DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME;

//@Service
//@Profile(LINK_PLUGIN_PROFILE_NAME)
public class DCPluginBasedGatewaySubmissionService implements DomibusConnectorGatewaySubmissionService {

    private static final Logger LOGGER = LogManager.getLogger(DCPluginBasedGatewaySubmissionService.class);

    @Autowired
    DCActiveLinkManagerService linkManager;


    @Override
    public void submitToGateway(DomibusConnectorMessage message) throws DomibusConnectorGatewaySubmissionException {

        SubmitToLink submitToLinkPartner = linkManager.getSubmitToLinkPartner(DCActiveLinkManagerService.DEFAULT_GW_LINK_NAME);
        if (submitToLinkPartner == null) {
            throw new DomibusConnectorGatewaySubmissionException("No Gateway Link available or configured!");
        }
        try {
            submitToLinkPartner.submitToLink(message, new DomibusConnectorLinkPartner.LinkPartnerName(DCActiveLinkManagerService.DEFAULT_GW_LINK_NAME));
        } catch (Exception e) {
            throw new DomibusConnectorGatewaySubmissionException("Submission failed!", e);
        }

    }
}
