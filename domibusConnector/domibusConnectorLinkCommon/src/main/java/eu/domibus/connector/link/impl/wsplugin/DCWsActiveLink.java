package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.ActiveLink;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.service.LinkPluginQualifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


public class DCWsActiveLink implements ActiveLink {

    private static final Logger LOGGER = LogManager.getLogger(DCWsActiveLink.class);

    private Map<DomibusConnectorLinkPartner.LinkPartnerName, DCWsActiveLinkPartner> nameToActiveLink = new HashMap<>();
    private Map<DomibusConnectorLinkPartner.LinkPartnerName, DomibusConnectorLinkPartner> nameToLinkPartner = new HashMap<>();

    @Autowired
    ConfigurableApplicationContext ctx;

    @Autowired
    DCWsSubmitTo submitTo;

    @Autowired
    DomibusConnectorLinkConfiguration linkConfiguration;

    @Autowired
    @LinkPluginQualifier
    LinkPlugin pluginManager;


    @Override
    public ActiveLinkPartner getActiveLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {
        return nameToActiveLink.get(linkPartnerName);
    }

    @Override
    public ActiveLinkPartner activateLinkPartner(DomibusConnectorLinkPartner linkPartner) {



        DCWsActiveLinkPartner dcWsActiveLinkPartner = new DCWsActiveLinkPartner(
                linkPartner,
                submitTo,
                this
        );

        this.nameToLinkPartner.put(linkPartner.getLinkPartnerName(), linkPartner);
        this.nameToActiveLink.put(linkPartner.getLinkPartnerName(), dcWsActiveLinkPartner);

        return dcWsActiveLinkPartner;
    }

    @Override
    public void shutdownLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartner) {
        this.nameToLinkPartner.remove(linkPartner);
        this.nameToActiveLink.remove(linkPartner);
    }

    @Override
    public void shutdown() {
        ctx.close();
    }

    @Override
    public LinkPlugin getPluginManager() {
        return pluginManager;
    }

    @Override
    public DomibusConnectorLinkConfiguration getConfiguration() {
        return linkConfiguration;
    }
}

