package eu.domibus.connector.link.impl.gwjmsplugin;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.*;
import eu.domibus.connector.link.api.exception.LinkPluginException;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@Scope("prototype")
//@Component
//@Profile(GwJmsPluginConfiguration.GW_JMS_PLUGIN_PROFILE)
public class GwJmsPluginActiveLink implements ActiveLink {

    private static final Logger LOGGER = LogManager.getLogger(GwJmsPluginActiveLink.class);


    private final DomibusConnectorLinkConfiguration linkConfiguration;
    private final GwJmsPlugin pluginManager;
    private final ConfigurableApplicationContext applicationContext;

    private GwJmsPluginActiveLinkPartner activeLinkPartner;
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


    @Override
    public ActiveLinkPartner getActiveLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {
        if (activeLinkPartner != null && activeLinkPartner.getLinkPartnerName().equals(linkPartnerName)) {
            return activeLinkPartner;
        }
//        LOGGER.error("No Link partner with name [{}] found", linkPartnerName);
        return null;
    }


    @Override
    public synchronized ActiveLinkPartner activateLinkPartner(DomibusConnectorLinkPartner linkPartner) {
        if (this.activeLinkPartner != null) {
            throw new LinkPluginException("A link partner is already active for this configuration! This link config only supports ONE active link partner at once!");
        }
        ActiveLinkPartner activePartner = getActiveLinkPartner(linkPartner.getLinkPartnerName());
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

        GwJmsPluginActiveLinkPartner activeLinkPartner = springChildContext.getBean(GwJmsPluginActiveLinkPartner.class);

        LOGGER.info("Activated LinkPartner [{}] to [{}]", linkPartner, activeLinkPartner);
        this.activeLinkPartner = activeLinkPartner;
        return activeLinkPartner;


    }

    @Override
    public void shutdownLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartner) {
        if (this.activeLinkPartner.getLinkPartnerName().equals(linkPartner)) {
            linkPartnerSpringContext.close();
        }
        this.activeLinkPartner = null;
        this.linkPartnerSpringContext = null;
    }

    @Override
    public void shutdown() {
        if (this.activeLinkPartner != null) {
            this.shutdownLinkPartner(this.activeLinkPartner.getLinkPartnerName());
        } else {
            LOGGER.warn("Cannot shutdown link, because link is not active!");
        }
    }

    @Override
    public LinkPlugin getPluginManager() {
        return this.pluginManager;
    }


    @Override
    public DomibusConnectorLinkConfiguration getConfiguration() {
        return this.linkConfiguration;
    }

    @Override
    public boolean isUp() {
        return linkPartnerSpringContext.isActive();
    }


}
