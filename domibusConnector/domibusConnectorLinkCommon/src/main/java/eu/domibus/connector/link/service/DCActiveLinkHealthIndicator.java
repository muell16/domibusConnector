package eu.domibus.connector.link.service;

import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.ActiveLink;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static eu.domibus.connector.link.service.DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME;

/**
 * Provides a health indicator for the connector link plugins
 *  the health goes to down if a plugin which should be UP
 *  is actually not running!
 *
 *
 */
@Component
@Profile(LINK_PLUGIN_PROFILE_NAME)
public class DCActiveLinkHealthIndicator extends AbstractHealthIndicator {

    @Autowired
    DCActiveLinkManagerService dcActiveLinkManagerService;

    @Autowired
    DCLinkPersistenceService dcLinkPersistenceService;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        builder.up();
        dcLinkPersistenceService.getAllEnabledLinks()
                .stream()
                .forEach(enabledLink -> this.checkLink(builder, enabledLink));
    }

    private void checkLink(Health.Builder builder, DomibusConnectorLinkPartner enabledLink) {
        DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName = enabledLink.getLinkPartnerName();

        Optional<ActiveLinkPartner> activeLinkPartnerOptional = dcActiveLinkManagerService.getActiveLinkPartner(linkPartnerName);
        if (activeLinkPartnerOptional.isPresent()) {
            ActiveLinkPartner activeLinkPartner = activeLinkPartnerOptional.get();
            ActiveLink activeLink = activeLinkPartner.getActiveLink();
            if (activeLink.isUp()) {
                builder.withDetail("linkpartner_" + linkPartnerName, Status.UP);
            } else {
                builder.withDetail("linkpartner_" + linkPartnerName, Status.DOWN);
            }
        } else {
            builder.down();
            builder.withDetail("linkpartner_" + linkPartnerName, Status.DOWN);
        }
    }
}
