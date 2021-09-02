package eu.domibus.connector.link.impl.gwjmsplugin;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.link.api.*;
import eu.domibus.connector.link.api.exception.LinkPluginException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GwJmsPlugin {

    private static final Logger LOGGER = LogManager.getLogger(GwJmsPlugin.class);

    public static final String LINK_IMPL_NAME = "gwjmsplugin";

    @Autowired
    ConfigurableApplicationContext applicationContext;

    private Map<DomibusConnectorLinkConfiguration.LinkConfigName, GwJmsPluginActiveLink> linkConfigNameToActiveLink = new HashMap<>();
    private Map<DomibusConnectorLinkConfiguration.LinkConfigName, DomibusConnectorLinkConfiguration> linkConfigNameToLinkConfig = new HashMap<>();


    /**
     * Tell the world if we are responsible for the implementation of
     *
     * @param implementation
     **/
//    @Override
    public boolean canHandle(String implementation) {
        return LINK_IMPL_NAME.equals(implementation);
    }


////    @Override
//    public ActiveLinkManager startConfiguration(DomibusConnectorLinkConfiguration linkConfiguration) {
//        DomibusConnectorLinkConfiguration.LinkConfigName linkConfigName = linkConfiguration.getConfigName();
//
//
//        GwJmsPluginActiveLink activeLink = linkConfigNameToActiveLink.get(linkConfigName);
//        if (activeLink != null) {
//            throw new LinkPluginException("LinkConfig is already active! Use shutdown first!");
//        }
//        activeLink = new GwJmsPluginActiveLink(linkConfiguration, this, applicationContext);
//
//        LOGGER.info("Activated LinkConfig [{}] to [{}]", linkConfiguration, activeLink);
//        linkConfigNameToActiveLink.put(linkConfigName, activeLink);
//        linkConfigNameToLinkConfig.put(linkConfigName, linkConfiguration);
////        return activeLink;
//        return null;
//
//    }
//
////    @Override
//    public void shutdownConfiguration(DomibusConnectorLinkConfiguration.LinkConfigName linkConfigurationName) {
//        GwJmsPluginActiveLink gwJmsPluginActiveLink = linkConfigNameToActiveLink.get(linkConfigurationName);
//        if (gwJmsPluginActiveLink == null) {
//            throw new LinkPluginException("No active link found with name " + linkConfigurationName);
//        }
//        gwJmsPluginActiveLink.shutdown();
//        linkConfigNameToActiveLink.remove(linkConfigurationName);
//        linkConfigNameToLinkConfig.remove(linkConfigurationName);
//    }

//    @Override
    public List<PluginFeature> getFeatures() {
        return Stream.<PluginFeature>of().collect(Collectors.toList());
    }

//    @Override
    public List<Class> getPluginConfigurationProperties() {
        return Stream.of(GwJmsPluginConfigurationProperties.class)
                .collect(Collectors.toList());
    }

//    @Override
    public List<Class> getPartnerConfigurationProperties() {
        return new ArrayList<>(); //partner itself is not configureable...
    }

//    @Override
    public String getPluginName() {
        return LINK_IMPL_NAME;
    }

//    @Override
    public String getPluginDescription() {
        return "A plugin to establish connection to domibus gateway jms plugin";
    }
}
