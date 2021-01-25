package eu.domibus.connector.link.impl.domibusgwwsplugin;

import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.ActiveLink;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.api.PluginFeature;
import eu.domibus.connector.link.impl.domibusgwwsplugin.childctx.DomibusGwWsPluginConfigurationProperties;
import eu.domibus.connector.link.impl.domibusgwwsplugin.childctx.DomibusGwWsPluginPartnerConfigurationProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.domibus.connector.link.service.DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME;

@Component
@Profile(LINK_PLUGIN_PROFILE_NAME)
public class DomibusGwWsPlugin implements LinkPlugin {

    private static final Logger LOGGER = LogManager.getLogger(DomibusGwWsPlugin.class);

    public static final String IMPL_NAME = "domibusgwwsplugin";


    @Override
    public String getPluginName() {
        return IMPL_NAME;
    }

    @Override
    public String getPluginDescription() {
        return "Domibus AS4 Gateway Webservice-Plugin-Connector";
    }

//    @Override
//    public boolean canHandle(String implementation) {
//        return IMPL_NAME.equals(implementation);
//    }

    @Override
    public ActiveLink startConfiguration(DomibusConnectorLinkConfiguration linkConfiguration) {
        return null;
    }

    @Override
    public void shutdownConfiguration(ActiveLink activeLink) {

    }

    @Override
    public ActiveLinkPartner enableLinkPartner(DomibusConnectorLinkPartner linkPartner, ActiveLink activeLink) {
        return null;
    }

    @Override
    public void shutdownActiveLinkPartner(ActiveLinkPartner linkPartner) {

    }

    @Override
    public SubmitToLink getSubmitToLink(ActiveLinkPartner linkPartner) {
        return null;
    }


////    @Override
//    public ActiveLinkManager startConfiguration(DomibusConnectorLinkConfiguration linkConfiguration) {
//        GwWsPluginActiveLink activeLink = new GwWsPluginActiveLink(this);
////        activeLinkMap.put(linkConfiguration.getConfigName(), activeLink);
////        return activeLink;
//        return null;
//    }
//
////    @Override
//    public void shutdownConfiguration(DomibusConnectorLinkConfiguration.LinkConfigName linkConfigurationName) {
//        ActiveLinkManager activeLink = this.activeLinkMap.get(linkConfigurationName);
////        activeLink.shutdown();
//        activeLinkMap.remove(activeLink);
//    }

//    @Override
    public List<PluginFeature> getFeatures() {
        return Stream.<PluginFeature>of(
                PluginFeature.SUPPORTS_MULTIPLE_PARTNERS,
                PluginFeature.PULL_MODE
        ).collect(Collectors.toList());
    }

//    @Override
    public List<Class> getPluginConfigurationProperties() {
        return Stream.of(DomibusGwWsPluginConfigurationProperties.class).collect(Collectors.toList());
    }

//    @Override
    public List<Class> getPartnerConfigurationProperties() {
        return Stream.of(DomibusGwWsPluginPartnerConfigurationProperties.class).collect(Collectors.toList());
    }
}
