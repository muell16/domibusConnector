package eu.domibus.connector.link.impl.gwjmsplugin;

import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import eu.domibus.connector.link.api.ActiveLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//@Component
//@Profile(GwJmsPluginConfiguration.GW_JMS_PLUGIN_PROFILE)
public class GwJmsPluginActiveLinkPartner implements ActiveLinkPartner {


    @Autowired
    private SubmitToGwJmsPlugin submitToGwJmsPlugin;

    @Autowired
    private GwJmsPluginActiveLink activeLink;

    @Autowired
    private DomibusConnectorLinkPartner linkPartner;

    @Override
    public ActiveLink getActiveLink() {
        return activeLink;
    }

    @Override
    public void shutdown() {
        activeLink.shutdownLinkPartner(linkPartner.getLinkPartnerName());
    }

    @Override
    public SubmitToLink getSubmitToLinkBean() {
        return submitToGwJmsPlugin;
    }

    public void setLinkPartner(DomibusConnectorLinkPartner linkPartner) {
        this.linkPartner = linkPartner;
    }

    public DomibusConnectorLinkPartner getLinkPartner() {
        return linkPartner;
    }
}


