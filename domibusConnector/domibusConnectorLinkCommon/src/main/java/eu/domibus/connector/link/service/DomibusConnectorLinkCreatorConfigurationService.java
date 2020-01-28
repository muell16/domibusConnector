package eu.domibus.connector.link.service;

import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Spring configuration class which reads
 * on startup all configured links from database via
 * {@code LinkInfoPersistenceService}
 * and triggers the {@code DomibusConnectorLinkManager}
 * to create the configured links
 */
@Configuration
public class DomibusConnectorLinkCreatorConfigurationService {

    private static final Logger LOGGER = LogManager.getLogger(DomibusConnectorLinkCreatorConfigurationService.class);

    @Autowired
    DCLinkPersistenceService DCLinkPersistenceService;

    @Autowired
    DCActiveLinkManagerService linkManager;

    @PostConstruct
    public void init() {
        DCLinkPersistenceService.getAllEnabledLinks().stream().forEach(this::activateLink);
    }

    private void activateLink(DomibusConnectorLinkPartner linkInfo) {
        try {
            linkManager.activateLinkPartner(linkInfo);
        } catch (Exception e) {
            LOGGER.error("Exception while activating Link [{}]", linkInfo);
        }
    }


}
