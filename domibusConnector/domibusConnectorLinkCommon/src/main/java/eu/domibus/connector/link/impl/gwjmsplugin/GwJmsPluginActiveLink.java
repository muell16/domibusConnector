package eu.domibus.connector.link.impl.gwjmsplugin;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.*;
import eu.domibus.connector.link.api.exception.LinkPluginException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.*;

//@Scope("prototype")
//@Component
//@Profile(GwJmsPluginConfiguration.GW_JMS_PLUGIN_PROFILE)
public class GwJmsPluginActiveLink { //implements ActiveLinkManager {

    private static final Logger LOGGER = LogManager.getLogger(GwJmsPluginActiveLink.class);


    private final DomibusConnectorLinkConfiguration linkConfiguration;
    private final GwJmsPlugin pluginManager;
    private final ConfigurableApplicationContext applicationContext;

    private ActiveLinkPartnerManager activeLinkPartner;
    private ConfigurableApplicationContext linkPartnerSpringContext;


    public GwJmsPluginActiveLink(
            DomibusConnectorLinkConfiguration linkConfiguration,
            GwJmsPlugin pluginManager,
            ConfigurableApplicationContext applicationContext
    ) {
        this.linkConfiguration = linkConfiguration;
        this.pluginManager = pluginManager;
        this.applicationContext = applicationContext;
    }

    public Class[] getSources() {
        return new Class[]{GwJmsPluginConfiguration.class};
    }

    public String[] getProfiles() {
        return new String[]{GwJmsPluginConfiguration.GW_JMS_PLUGIN_PROFILE};
    }


//    @Override
    public Optional<ActiveLinkPartnerManager> getActiveLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {
        if (activeLinkPartner != null && activeLinkPartner.getLinkPartnerName().equals(linkPartnerName)) {
            return Optional.of(activeLinkPartner);
        }
//        LOGGER.error("No Link partner with name [{}] found", linkPartnerName);
        return Optional.empty();
    }


//    @Override
    public synchronized Optional<ActiveLinkPartnerManager> activateLinkPartner(DomibusConnectorLinkPartner linkPartner) {
        if (this.activeLinkPartner != null) {
            throw new LinkPluginException("A link partner is already active for this configuration! This link config only supports ONE active link partner at once!");
        }
        Optional<ActiveLinkPartnerManager> activePartner = getActiveLinkPartner(linkPartner.getLinkPartnerName());
        if (activePartner != null) {
            throw new LinkPluginException("Link is already active! Use shutdown first!");
        }


        ConfigurableApplicationContext springChildContext = LinkPluginUtils
                .getChildContextBuilder(applicationContext)
                .withDomibusConnectorLinkConfiguration(linkConfiguration)
                .withDomibusConnectorLinkPartner(linkPartner)
                .addSingelton("gwJmsActiveLink", this)
                .withProfiles(getProfiles())
                .withSources(getSources())
                .run();
        this.linkPartnerSpringContext = springChildContext;

        ActiveLinkPartnerManager activeLinkPartner = springChildContext.getBean(GwJmsPluginActiveLinkPartner.class);

        LOGGER.info("Activated LinkPartner [{}] to [{}]", linkPartner, activeLinkPartner);
        this.activeLinkPartner = activeLinkPartner;
        return Optional.of(activeLinkPartner);


    }

//    @Override
    public void shutdownLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartner) {
        if (this.activeLinkPartner.getLinkPartnerName().equals(linkPartner)) {
            linkPartnerSpringContext.close();
        }
        this.activeLinkPartner = null;
        this.linkPartnerSpringContext = null;
    }

//    @Override
    public void shutdown() {
        if (this.activeLinkPartner != null) {
            this.shutdownLinkPartner(this.activeLinkPartner.getLinkPartnerName());
        } else {
            LOGGER.warn("Cannot shutdown link, because link is not active!");
        }
    }

//    @Override
//    public LinkPlugin getPluginManager() {
//        return this.pluginManager;
//    }


//    @Override
    public DomibusConnectorLinkConfiguration getConfiguration() {
        return this.linkConfiguration;
    }

}
