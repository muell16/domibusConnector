package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.link.api.ActiveLink;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.api.LinkPluginUtils;
import eu.domibus.connector.link.api.PluginFeature;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.MDCHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.domibus.connector.link.impl.wsplugin.DCWsPluginConfiguration.DC_WS_PLUGIN_PROFILE_NAME;

@Component
public class DCWsPlugin implements LinkPlugin {

    private static final Logger LOGGER = LogManager.getLogger(DCWsPlugin.class);
    public static final String IMPL_NAME = "wsplugin";

    @Autowired
    ConfigurableApplicationContext applicationContext;

    @Override
    public boolean canHandle(String implementation) {
        return IMPL_NAME.equals(implementation);
    }

    @Override
    public ActiveLink startConfiguration(DomibusConnectorLinkConfiguration linkConfiguration) {
        try (MDC.MDCCloseable mdc = MDC.putCloseable(LoggingMDCPropertyNames.MDC_LINK_CONFIG_NAME, linkConfiguration.getConfigName().getConfigName())) {
            ConfigurableApplicationContext springChildContext = LinkPluginUtils.createSpringChildContext(linkConfiguration, null, applicationContext, getSources(), getProfiles());
            DCWsActiveLink activeLink = springChildContext.getBean(DCWsActiveLink.class);
            LOGGER.info("Activated Link Configuration [{}] with activeLink [{}]", linkConfiguration, activeLink);
            return activeLink;
        }
    }

    private List<String> getProfiles() {
        return Stream.of(DC_WS_PLUGIN_PROFILE_NAME).collect(Collectors.toList());
    }

    private List<Class> getSources() {
        return Stream.of(DCWsPluginConfiguration.class).collect(Collectors.toList());
    }

    @Override
    public List<PluginFeature> getFeatures() {
        return Stream.<PluginFeature>of(
                PluginFeature.SUPPORTS_MULTIPLE_PARTNERS,
                PluginFeature.PUSH_MODE,
                PluginFeature.PULL_MODE
        ).collect(Collectors.toList());
    }

    @Override
    public List<Class> getPluginConfigurationProperties() {
        return Stream.of(
                DCWsPluginConfigurationProperties.class
        ).collect(Collectors.toList());
    }

    @Override
    public List<Class> getPartnerConfigurationProperties() {
//        return Stream.of(
//
//        ).collect(Collectors.toList());
        return new ArrayList<>();
    }
}
