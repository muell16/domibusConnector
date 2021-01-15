package eu.domibus.connector.link.impl.gwwspullplugin;

import eu.domibus.connector.controller.service.PullFromLink;
import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.*;

import eu.domibus.connector.link.impl.gwwspullplugin.childctx.DCGatewayPullPluginConfiguration;
import eu.domibus.connector.link.impl.gwwspullplugin.childctx.DCGatewayPullPluginConfigurationProperties;
import eu.domibus.connector.link.utils.LinkPluginUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DCGatewayPullPlugin implements LinkPlugin {

    private final static Logger LOGGER = LogManager.getLogger(DCGatewayPullPlugin.class);

    public static final String IMPL_NAME = "gwwspullplugin";

    @Autowired
    ConfigurableApplicationContext applicationContext;

    @Autowired
    Scheduler scheduler;

    private SubmitToLink submitToLink;

    private PullFromLink pullFromLink;

    @Override
    public boolean canHandle(String implementation) {
        return getPluginName().equals(implementation);
    }

    @Override
    public String getPluginName() {
        return IMPL_NAME;
    }

    @Override
    public ActiveLink startConfiguration(DomibusConnectorLinkConfiguration linkConfiguration) {

        LOGGER.info("Starting Configuration for [{}]", linkConfiguration);
//        LOGGER.debug("Using Properties")

        ConfigurableApplicationContext childCtx = LinkPluginUtils.getChildContextBuilder(applicationContext)
                .withDomibusConnectorLinkConfiguration(linkConfiguration)
                .withSources(DCGatewayPullPluginConfiguration.class)
                .withProfiles(DCGatewayPullPluginConfiguration.DC_GATEWAY_PULL_PLUGIN_PROFILE)
                .run();


        ActiveLink activeLink = new ActiveLink();
        activeLink.setLinkConfiguration(linkConfiguration);
        activeLink.setChildContext(childCtx);

        SubmitToLink bean = childCtx.getBean(SubmitToLink.class);
        this.submitToLink = bean;

        PullFromLink pullFromLink = childCtx.getBean(PullFromLink.class);
        this.pullFromLink = pullFromLink;

        return activeLink;
    }

    @Override
    public void shutdownConfiguration(ActiveLink activeLink) {
//        ConfigurableApplicationContext childContext = activeLink.getChildContext();
//        if (childContext != null) {
//            childContext.close();
//        }
        throw new RuntimeException("Not supported yet!");
    }

    @Override
    public ActiveLinkPartner enableLinkPartner(DomibusConnectorLinkPartner linkPartner, ActiveLink activeLink) {

        DomibusConnectorLinkConfiguration linkConfiguration = activeLink.getLinkConfiguration();

        //set link partner for the pull job, so pull jobs knows where to pull the messages from, or more important
        //it can pass this information back to the connector core, so the connector knows the source of the messages
        //see SubmitToConnector interface
//            ConfigurableApplicationContext childContext = activeLink.getChildContext();
//            DCGatewayPullMessagesJob job = childContext.getBean(DCGatewayPullMessagesJob.class);
//            job.setLinkPartner(linkPartner);

//            DCGatewayPullPluginConfiguration ctxConfig = childContext.getBean(DCGatewayPullPluginConfiguration.class);

        //register job class in parent context so quartz which is running
        // in parent context can create and call it
//            applicationContext.getBeanFactory().registerSingleton("dcGatewayPullMessagesJob", job);
//            scheduler.scheduleJob(ctxConfig.pullMessagesJobDetail().getObject(), ctxConfig.pullMessagesJobTrigger().getObject());


        ActiveLinkPartner activeLinkPartner = new ActiveLinkPartner();
        activeLinkPartner.setParentLink(activeLink);
        activeLinkPartner.setLinkPartner(linkPartner);

        return activeLinkPartner;

    }

    @Override
    public void shutdownActiveLinkPartner(ActiveLinkPartner linkPartner) {
        //this.shutdownConfiguration(linkPartner.getParentLink());
        throw new RuntimeException("Not supported yet!");
    }

    @Override
    public SubmitToLink getSubmitToLink(ActiveLinkPartner linkPartner) {
        return this.submitToLink;
    }

    @Override
    public Optional<PullFromLink> getPullFromLink(ActiveLinkPartner activeLinkPartner) {
        return Optional.of(this.pullFromLink);
    }

    @Override
    public List<PluginFeature> getFeatures() {
        return Stream.of(PluginFeature.PULL_MODE).collect(Collectors.toList());
    }

    @Override
    public List<Class> getPluginConfigurationProperties() {
        return Stream.of(DCGatewayPullPluginConfigurationProperties.class).collect(Collectors.toList());
    }

    @Override
    public List<Class> getPartnerConfigurationProperties() {
        return Stream.of(DCGatewayPullPluginConfigurationProperties.class).collect(Collectors.toList());
    }


}
