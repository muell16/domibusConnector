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

    private static final Logger LOGGER = LogManager.getLogger(GwJmsPlugin.class);

    public static final String LINK_IMPL_NAME = "gwjmsplugin";
    private final DomibusConnectorLinkConfiguration linkConfiguration;
    private final GwJmsPlugin pluginManager;


    ConfigurableApplicationContext applicationContext;

    private Map<DomibusConnectorLinkPartner, ActiveLinkPartner> linkPartnerToActiveLinkPartner = new HashMap<>();

    public GwJmsPluginActiveLink(
            GwJmsPlugin gwJmsPlugin,
            ConfigurableApplicationContext applicationContext,
            DomibusConnectorLinkConfiguration linkConfiguration) {
        this.pluginManager = gwJmsPlugin;
        this.applicationContext = applicationContext;
        this.linkConfiguration = linkConfiguration;
    }

//    private ActiveLinkPartner activeLinkPartner;
//    private DomibusConnectorLinkConfiguration linkConfiguration;
//    private DomibusConnectorLinkPartner linkPartner;

    public List<Class> getSources() {
        return Stream
                .of(GwJmsPluginConfiguration.class)
                .collect(Collectors.toList());
    }

    public List<String> getProfiles() {
        return Stream
                .of(GwJmsPluginConfiguration.GW_JMS_PLUGIN_PROFILE)
                .collect(Collectors.toList());
    }




    @Override
    public ActiveLinkPartner getActiveLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {
        Optional<DomibusConnectorLinkPartner> linkPartner = getLinkPartnerByName(linkPartnerName);
        if (linkPartner.isPresent()) {
            return linkPartnerToActiveLinkPartner.get(linkPartner);
        }
        LOGGER.error("No Link partner with name [{}] found", linkPartnerName);
        return null;
    }

    private Optional<DomibusConnectorLinkPartner> getLinkPartnerByName(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {
        return linkPartnerToActiveLinkPartner.keySet().stream().filter(lp -> lp.getLinkPartnerName().getLinkName().equals(linkPartnerName)).findAny();
    }

    @Override
    public synchronized ActiveLinkPartner activateLinkPartner(DomibusConnectorLinkPartner linkPartner) {
        try (MDC.MDCCloseable mdc1 = MDC.putCloseable(LoggingMDCPropertyNames.MDC_LINK_CONFIG_NAME, linkConfiguration.getConfigName().getConfigName());
                MDC.MDCCloseable mdc2 = MDC.putCloseable(LoggingMDCPropertyNames.MDC_LINK_PARTNER_NAME, linkPartner.getLinkPartnerName().getLinkName())) {
            ActiveLinkPartner activePartner = getActiveLinkPartner(linkPartner.getLinkPartnerName());
            if (activePartner != null) {
                throw new LinkPluginException("Link is already active! Use shutdown first!");
            }

            ConfigurableApplicationContext springChildContext = LinkPluginUtils.createSpringChildContext(linkPartner.getLinkConfiguration(), linkPartner, applicationContext, getSources(), getProfiles());
            GwJmsPluginActiveLinkPartner activeLinkPartner = springChildContext.getBean(GwJmsPluginActiveLinkPartner.class);
            activeLinkPartner.setLinkPartner(linkPartner);
//        this.activeLinkPartner = activeLinkPartner;
            LOGGER.info("Activated LinkPartner [{}] to [{}]", linkPartner, activeLinkPartner);
            this.linkPartnerToActiveLinkPartner.put(linkPartner, activePartner);
            return activeLinkPartner;
        }

    }

    @Override
    public void shutdownLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartner) {
        ActiveLinkPartner activeLinkPartner = getActiveLinkPartner(linkPartner);
        Optional<DomibusConnectorLinkPartner> linkPartnerByName = getLinkPartnerByName(linkPartner);
        if (activeLinkPartner != null) {
            activeLinkPartner.shutdown();
        }
        if (linkPartnerByName.isPresent()) {
            linkPartnerToActiveLinkPartner.remove(linkPartner);
        }
    }

    @Override
    public void shutdownLinkConfig() {
        this.linkPartnerToActiveLinkPartner.keySet().stream().forEach(lp -> this.shutdownLinkPartner(lp.getLinkPartnerName()));
    }

    @Override
    public LinkPlugin getPluginManager() {
        return this.pluginManager;
    }


    @Override
    public DomibusConnectorLinkConfiguration getConfiguration() {
        return this.linkConfiguration;
    }


}
