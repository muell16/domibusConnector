package eu.domibus.connector.link.impl.gwjmsplugin;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
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
public class GwJmsPlugin implements LinkPlugin {

    private static final Logger LOGGER = LogManager.getLogger(GwJmsPlugin.class);

    public static final String LINK_IMPL_NAME = "gwjmsplugin";

    @Autowired
    ConfigurableApplicationContext applicationContext;

    private Map<DomibusConnectorLinkConfiguration, ActiveLink> linkConfigToActiveLink = new HashMap<>();

//    private ActiveLinkPartner activeLinkPartner;
//    private DomibusConnectorLinkConfiguration linkConfiguration;
//    private DomibusConnectorLinkPartner linkPartner;

    /**
     *   Tell the world if we are responsible for the implementation of
     *   @param implementation
    **/
    @Override
    public boolean canHandle(String implementation) {
        return LINK_IMPL_NAME.equals(implementation);
    }

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
    public ActiveLink startConfiguration(DomibusConnectorLinkConfiguration linkInfo) {
        GwJmsPluginActiveLink activeLink = new GwJmsPluginActiveLink(
                this,
                applicationContext,
                linkInfo
        );
        linkConfigToActiveLink.put(linkInfo, activeLink);
        return activeLink;
    }

    @Override
    public List<PluginFeature> getFeatures() {
        return Stream.<PluginFeature>of().collect(Collectors.toList());
    }

    @Override
    public List<Class> getPluginConfigurationProperties() {
        return Stream.of(GwJmsPluginConfigurationProperties.class)
                .collect(Collectors.toList());
    }

    @Override
    public List<Class> getPartnerConfigurationProperties() {
        return new ArrayList<>(); //partner itself is not configureable...
    }

    @Override
    public String getPluginName() {
        return LINK_IMPL_NAME;
    }

    @Override
    public String getPluginDescription() {
        return "A plugin to establish connection to domibus gateway jms plugin";
    }
}
