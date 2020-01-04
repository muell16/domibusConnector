package eu.domibus.connector.link.service;

import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.ActiveLink;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.api.exception.LinkPluginException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class manages the lifecycle of the connector links
 */
@Service
public class DomibusConnectorLinkManager {

    private final static Logger LOGGER = LogManager.getLogger(DomibusConnectorLinkManager.class);

    @Autowired(required = false)
    List<LinkPlugin> linkPluginFactories = new ArrayList<>();

    @Autowired
    ConfigurableApplicationContext applicationContext;



    private Map<DomibusConnectorLinkPartner.LinkPartnerName, ActiveLinkPartner> activeLinkPartners = new ConcurrentHashMap<>();
    private Map<DomibusConnectorLinkConfiguration.LinkConfigName, ActiveLink> activeLinkConfigurations = new ConcurrentHashMap<>();


    SubmitToLink getLinkPartner(String linkName) {
        ActiveLinkPartner activeLinkPartner = activeLinkPartners.get(new DomibusConnectorLinkPartner.LinkPartnerName(linkName));
        if(activeLinkPartner == null) {
            String error = String.format("No linkPartner with name %s available", linkName);
            throw new LinkPluginException(error);
        }
//        DomibusConnectorLinkPartner.LinkPartnerName name = new DomibusConnectorLinkPartner.LinkPartnerName(linkName);
        SubmitToLink submitToLinkBean = activeLinkPartner.getSubmitToLinkBean();
        return submitToLinkBean;
    }

    public List<LinkPlugin> getAvailableLinkPlugins() {
        return this.linkPluginFactories;
    }

    public synchronized void activateLinkPartner(DomibusConnectorLinkPartner linkInfo) {
        DomibusConnectorLinkConfiguration linkConfiguration = linkInfo.getLinkConfiguration();
        DomibusConnectorLinkConfiguration.LinkConfigName configName = linkConfiguration.getConfigName();
        final DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName = linkInfo.getLinkPartnerName();

        if (linkPartnerName == null) {
            throw new IllegalArgumentException("LinkPartnerName of LinkPartner is not allowed to be null!");
        }
        if (configName == null) {
            throw new IllegalArgumentException("ConfigName of LinkConfiguration is not allowed to be null!");
        }

        ActiveLink activeLink = activeLinkConfigurations.get(configName);
        if (activeLink == null) {
            activeLink = startLinkConfiguration(linkInfo.getLinkConfiguration());
        }
        if (activeLink == null) {
            LOGGER.warn("No link configuration for link partner available [{}]!", linkInfo);
            return;
        }
        try {
            ActiveLinkPartner activeLinkPartner = activeLink.activateLinkPartner(linkInfo);
            activeLinkPartners.put(linkPartnerName, activeLinkPartner);
            LOGGER.info("Activated Link partner [{}] ", activeLinkPartner);
        } catch (LinkPluginException e) {
            LOGGER.warn("Link partner could not be activated", e);
        }

    }

    private synchronized ActiveLink startLinkConfiguration(DomibusConnectorLinkConfiguration linkConfiguration) {
        String linkImpl = linkConfiguration.getLinkImpl();
        if (StringUtils.isEmpty(linkImpl)) {
            LOGGER.warn("link impl of [{}] is empty! No link configuration can be created!", linkConfiguration);
            return null;
        }
        Optional<LinkPlugin> first = linkPluginFactories.stream().filter(l -> l.canHandle(linkImpl)).findFirst();
        if (!first.isPresent()) {
            LOGGER.warn("No link factory for linkImpl [{}] found! No link configuration will be created!", linkImpl);
            return null;
        }
        LinkPlugin linkPlugin = first.get();

        ActiveLink link = linkPlugin.startConfiguration(linkConfiguration);

        activeLinkConfigurations.put(linkConfiguration.getConfigName(), link);
        return link;
    }

    public void shutdownLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {
        ActiveLinkPartner activeLinkPartner = activeLinkPartners.get(linkPartnerName);
        if (activeLinkPartner == null) {
            throw new IllegalArgumentException(String.format("No linkPartner with name %s found!", linkPartnerName.toString()));
        }
//        if (activeLinkPartner() == null) {
//            throw new RuntimeException(String.format("ActiveLinkConfiguration %s has no LinkPartner Manager!", activeLinkPartner));
//        }
//        activeLinkPartner.getLinkManager().shutdownLinkPartner(linkPartnerName);


        activeLinkPartner.shutdown();
        activeLinkPartners.remove(linkPartnerName);

    }

    //TODO: create destroy or/and reset link Configuration routine

    @PreDestroy
    public void preDestroy() {
        this.activeLinkConfigurations.forEach((key, value) -> {
            try {
                LOGGER.info("Invoking shutdown on LinkConfig [{}]", value);
                value.shutdownLinkConfig();
            } catch (Exception e) {
                LOGGER.error("Exception occured during shutdown LinkConfig", e);
            }
        });
    }

}
