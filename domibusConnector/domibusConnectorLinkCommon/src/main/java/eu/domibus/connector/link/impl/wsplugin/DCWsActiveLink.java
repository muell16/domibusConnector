package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.ActiveLink;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import eu.domibus.connector.link.api.LinkPlugin;

public class DCWsActiveLink implements ActiveLink {

    @Override
    public ActiveLinkPartner getActiveLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {
        return null;
    }

    @Override
    public ActiveLinkPartner activateLinkPartner(DomibusConnectorLinkPartner linkPartner) {
        return null;
    }

    @Override
    public void shutdownLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartner) {

    }

    @Override
    public void shutdownLinkConfig() {

    }

    @Override
    public LinkPlugin getPluginManager() {
        return null;
    }

    @Override
    public DomibusConnectorLinkConfiguration getConfiguration() {
        return null;
    }
}
