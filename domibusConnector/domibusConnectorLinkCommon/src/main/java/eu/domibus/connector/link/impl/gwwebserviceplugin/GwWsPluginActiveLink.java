package eu.domibus.connector.link.impl.gwwebserviceplugin;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.ActiveLinkManager;
import eu.domibus.connector.link.api.ActiveLinkPartnerManager;
import eu.domibus.connector.link.api.LinkPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GwWsPluginActiveLink { //implements ActiveLinkManager {

    private final GwWsPlugin pluginManager;

    private Map<DomibusConnectorLinkPartner.LinkPartnerName, ActiveLinkPartnerManager> activeLinkPartnerMap = new HashMap<>();

    public GwWsPluginActiveLink(GwWsPlugin pluginManager) {
        this.pluginManager = pluginManager;
    }

//    @Override
    public Optional<ActiveLinkPartnerManager> getActiveLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {
        return Optional.ofNullable(activeLinkPartnerMap.get(linkPartnerName));
    }

//    @Override
    public Optional<ActiveLinkPartnerManager> activateLinkPartner(DomibusConnectorLinkPartner linkPartner) {
        //linkPartner.getProperties()



        return null;
    }

//    @Override
    public void shutdownLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartner) {

    }

//    @Override
    public void shutdown() {

    }

//    @Override
//    public LinkPlugin getPluginManager() {
//        return null;
//    }

//    @Override
    public DomibusConnectorLinkConfiguration getConfiguration() {
        return null;
    }

}
