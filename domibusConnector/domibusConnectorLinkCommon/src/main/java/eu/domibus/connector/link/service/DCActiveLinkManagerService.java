package eu.domibus.connector.link.service;

import eu.domibus.connector.controller.service.PullFromLink;
import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.enums.LinkMode;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.*;
import eu.domibus.connector.link.api.exception.LinkPluginException;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class manages the lifecycle of the connector links
 */
//@Service
//@Profile(LINK_PLUGIN_PROFILE_NAME)
public class DCActiveLinkManagerService {

    private final static Logger LOGGER = LogManager.getLogger(DCActiveLinkManagerService.class);

    public static final String DEFAULT_GW_LINK_NAME = "default_GW";


    @Autowired(required = false)
    List<LinkPlugin> linkPluginFactories = new ArrayList<>();

    @Autowired
    ConfigurableApplicationContext applicationContext;

    @Autowired
    Scheduler scheduler;


    private Map<DomibusConnectorLinkPartner.LinkPartnerName, ActiveLinkPartner> activeLinkPartners = new ConcurrentHashMap<>();
    private Map<DomibusConnectorLinkConfiguration.LinkConfigName, ActiveLink> activeLinkConfigurations = new ConcurrentHashMap<>();
//    private Map<DomibusConnectorLinkPartner.LinkPartnerName, String> activeLinkPartnersToPlugin = new ConcurrentHashMap<>();

    SubmitToLink getSubmitToLinkPartner(String linkName) {
        if (StringUtils.isEmpty(linkName)) {
            throw new IllegalArgumentException("Provided link name is empty!");
        }
        return getSubmitToLinkPartner(new DomibusConnectorLinkPartner.LinkPartnerName(linkName));
    }

    SubmitToLink getSubmitToLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {
        ActiveLinkPartner activeLinkPartner = activeLinkPartners.get(linkPartnerName);
        if (activeLinkPartner == null) {
            String error = String.format("No linkPartner with name %s available", linkPartnerName);
            throw new LinkPluginException(error);
        }
//        DomibusConnectorLinkPartner.LinkPartnerName name = new DomibusConnectorLinkPartner.LinkPartnerName(linkName);
        SubmitToLink submitToLinkBean = activeLinkPartner.getParentLink().getLinkPlugin().getSubmitToLink(activeLinkPartner);
        return submitToLinkBean;
    }

    public Optional<PullFromLink> getPullFromLinkPartner(String linkName) {
        DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName = new DomibusConnectorLinkPartner.LinkPartnerName(linkName);
        ActiveLinkPartner activeLinkPartner = activeLinkPartners.get(linkPartnerName);
        if (activeLinkPartner == null) {
            String error = String.format("No linkPartner with name %s available", linkName);
            throw new LinkPluginException(error);
        }
//        DomibusConnectorLinkPartner.LinkPartnerName name = new DomibusConnectorLinkPartner.LinkPartnerName(linkName);
        Optional<PullFromLink> pullFromLinkBean = activeLinkPartner.getParentLink().getLinkPlugin().getPullFromLink(activeLinkPartner);
        return pullFromLinkBean;
    }


    public List<LinkPlugin> getAvailableLinkPlugins() {
        return this.linkPluginFactories;
    }

    public Optional<LinkPlugin> getLinkPluginByName(String name) {
        return this.linkPluginFactories
                .stream()
                .filter(lp -> lp.getPluginName().equals(name))
                .findFirst();
    }

//    public Optional<ActiveLinkPartnerManager> getActiveLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {
//        return activeLinkPartners.getOrDefault(linkPartnerName, Optional.empty());
//    }

    public synchronized Optional<ActiveLinkPartner> activateLinkPartner(DomibusConnectorLinkPartner linkInfo) {
        try (MDC.MDCCloseable li = MDC.putCloseable(LoggingMDCPropertyNames.MDC_LINK_PARTNER_NAME, linkInfo.getLinkPartnerName().toString())) {
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
                return Optional.empty();
            }
            activeLinkConfigurations.put(configName, activeLink);


            LinkPlugin linkPlugin = activeLink.getLinkPlugin();

            ActiveLinkPartner activeLinkPartner = null;
            try {
                activeLinkPartner = linkPlugin.enableLinkPartner(linkInfo, activeLink);
                activeLinkPartners.put(linkPartnerName, activeLinkPartner);
                configurePull(linkInfo, activeLinkPartner);
            } catch (LinkPluginException e) {
                LOGGER.warn("Link partner could not be activated", e);
            }


            return Optional.ofNullable(activeLinkPartner);
        }
    }

    /**
     * configure scheduler for pull mode...
     * @param linkInfo
     * @param activeLinkPartner
     */
    private void configurePull(DomibusConnectorLinkPartner linkInfo, ActiveLinkPartner activeLinkPartner) {
        if (linkInfo.getLinkMode() != LinkMode.PULL) {
            return;
        }
        Optional<PullFromLink> pullFromBean = getPullFromLinkPartner(activeLinkPartner.getLinkPartner().getLinkPartnerName().getLinkName());
        if (!pullFromBean.isPresent()) {
            LOGGER.warn("PULL MODE activated but NO pull bean found!");
            return;
        }
        try {
//            PullFromLink pullFromLink = pullFromBean.get();

            String linkPartnerName = linkInfo.getLinkPartnerName().toString();

            //Delete Job and recreate it...
            JobKey jobKey = new JobKey("pull_from_" + linkPartnerName, "LINK_PULL_JOBS");
            scheduler.deleteJob(jobKey);
            JobDetail link_pulls = JobBuilder.newJob(DCLinkPullJob.class)
                    .storeDurably(true)
                    .withIdentity(jobKey)
                    .build();

            int pullIntervalSeconds = (int) linkInfo.getPullInterval().get(ChronoUnit.SECONDS);

            //same for the trigger...
            TriggerKey triggerKey = new TriggerKey("pull_trigger_" + linkPartnerName, "LINK_PULL_TRIGGER");
            scheduler.unscheduleJob(triggerKey);
            SimpleTrigger link_pull_trigger = TriggerBuilder.newTrigger().forJob(link_pulls)

                    .withIdentity(triggerKey)
                    .usingJobData(DCLinkPullJob.LINK_PARTNER_NAME_PROPERTY_NAME, linkInfo.getLinkPartnerName().toString())
                    .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(pullIntervalSeconds))
                    .build();

            LOGGER.info(LoggingMarker.Log4jMarker.CONFIG, "Setting up trigger with pull intervall [{} seconds] to pull from [{}]", pullIntervalSeconds, linkPartnerName);

            scheduler.scheduleJob(link_pulls, link_pull_trigger);

        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }

    }

    private synchronized ActiveLink startLinkConfiguration(DomibusConnectorLinkConfiguration linkConfiguration) {
        try (MDC.MDCCloseable lc = MDC.putCloseable(LoggingMDCPropertyNames.MDC_LINK_CONFIG_NAME, linkConfiguration.getConfigName().toString())) {
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
            if (link == null) {
                throw new LinkPluginException(String.format("Failed to start configuration [%s]", linkConfiguration));
            }
            link.setLinkPlugin(linkPlugin);

            return link;
        }
    }

    public void shutdownLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {
        ActiveLinkPartner activeLinkPartner = activeLinkPartners.get(linkPartnerName);
        if (activeLinkPartner == null) {
            throw new IllegalArgumentException(String.format("No active linkPartner with name %s found!", linkPartnerName.toString()));
        }

//        if (activeLinkPartner() == null) {
//            throw new RuntimeException(String.format("ActiveLinkConfiguration %s has no LinkPartner Manager!", activeLinkPartner));
//        }
//        activeLinkPartner.getLinkManager().shutdownLinkPartner(linkPartnerName);

//        activeLinkPartner.ifPresent(ActiveLinkPartnerManager::shutdown);
        activeLinkPartners.remove(linkPartnerName);

    }

    @PreDestroy
    public void preDestroy() {
        this.activeLinkConfigurations.forEach((key, value) -> {
            try {
                LOGGER.info("Invoking shutdown on LinkConfig [{}]", value);
//                value.shutdown();
            } catch (Exception e) {
                LOGGER.error("Exception occured during shutdown LinkConfig", e);
            }
        });
    }

    public Collection<ActiveLinkPartner> getActiveLinkPartners() {
        return activeLinkPartners.values();
    }

    public Optional<ActiveLinkPartner> getActiveLinkPartnerByName(String lp) {
        DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName = new DomibusConnectorLinkPartner.LinkPartnerName(lp);
        return getActiveLinkPartnerByName(linkPartnerName);
    }

    public Optional<ActiveLinkPartner> getActiveLinkPartnerByName(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName) {
        ActiveLinkPartner activeLinkPartner = activeLinkPartners.get(linkPartnerName);
        return Optional.ofNullable(activeLinkPartner);
    }


}
