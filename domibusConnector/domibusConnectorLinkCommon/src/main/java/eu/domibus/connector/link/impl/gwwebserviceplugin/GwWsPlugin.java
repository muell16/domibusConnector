package eu.domibus.connector.link.impl.gwwebserviceplugin;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.link.api.PluginFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.domibus.connector.link.service.DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME;

@Component
@Profile(LINK_PLUGIN_PROFILE_NAME)
public class GwWsPlugin {

    public static final String DC_DOMIBUSGW_WS_PLUGIN_PROFILE_NAME = "link.domibusgwwsplugin";
    private static final Logger LOGGER = LogManager.getLogger(GwWsPlugin.class);
    public static final String IMPL_NAME = "domibusgatewayplugin";

//    private Map<DomibusConnectorLinkConfiguration.LinkConfigName, ActiveLinkManager> activeLinkMap = new HashMap<>();

//    @Override
    public String getPluginName() {
        return "DefaultDomibusGatewayWebservicePlugin";
    }

//    @Override
    public String getPluginDescription() {
        return "Domibus AS4 Gateway Webservice-Plugin-Connector";
    }

//    @Override
    public boolean canHandle(String implementation) {
        return IMPL_NAME.equals(implementation);
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
        return Stream.of(GwWsPluginConfigurationProperties.class).collect(Collectors.toList());
    }

//    @Override
    public List<Class> getPartnerConfigurationProperties() {
        return Stream.of(GwWsPluginPartnerConfigurationProperties.class).collect(Collectors.toList());
    }
}
