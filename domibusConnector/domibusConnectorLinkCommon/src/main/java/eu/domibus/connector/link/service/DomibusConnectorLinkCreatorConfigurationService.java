package eu.domibus.connector.link.service;

import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

import static eu.domibus.connector.link.service.DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME;

/**
 * Spring configuration class which reads
 * on startup all configured links from database via
 * {@code LinkInfoPersistenceService}
 * and triggers the {@code DomibusConnectorLinkManager}
 * to create the configured links
 */
@Configuration
@Profile(LINK_PLUGIN_PROFILE_NAME)
public class DomibusConnectorLinkCreatorConfigurationService {

    private static final Logger LOGGER = LogManager.getLogger(DomibusConnectorLinkCreatorConfigurationService.class);

    @Autowired
    DCLinkPersistenceService DCLinkPersistenceService;

    @Autowired
    DCActiveLinkManagerService linkManager;


    @Autowired
    DCLinkPluginConfigurationProperties config;

    @PostConstruct
    public void init() {
        if (config.isAutostart()) {
            DCLinkPersistenceService.getAllEnabledLinks().stream().forEach(this::activateLink);
        } else {
            LOGGER.info("Link Autostart is disabled - no links are going to be started during connector start");
        }

    }

    private void activateLink(DomibusConnectorLinkPartner linkInfo) {
        try {
            linkManager.activateLinkPartner(linkInfo);
        } catch (Exception e) {
            LOGGER.error("Exception while activating Link [{}]", linkInfo);
        }
    }


}
