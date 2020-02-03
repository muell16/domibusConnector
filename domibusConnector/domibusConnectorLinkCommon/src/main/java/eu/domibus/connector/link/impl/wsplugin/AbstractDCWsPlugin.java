package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.link.api.ActiveLink;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.api.LinkPluginUtils;
import eu.domibus.connector.link.api.PluginFeature;
import eu.domibus.connector.link.service.LinkPluginQualifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractDCWsPlugin implements LinkPlugin {

    private static final Logger LOGGER = LogManager.getLogger(AbstractDCWsPlugin.class);

    @Autowired
    ConfigurableApplicationContext applicationContext;


    @Override
    public ActiveLink startConfiguration(DomibusConnectorLinkConfiguration linkConfiguration) {
        ConfigurableApplicationContext springChildContext = LinkPluginUtils.getChildContextBuilder(
                applicationContext
        ).withProfiles(getProfiles())
                .withProperties(linkConfiguration.getProperties())
                .addSingelton(LinkPluginQualifier.LINK_QUALIFIER_NAME, this)
                .withSources(getSources())
                .run();

        DCWsActiveLink activeLink = springChildContext.getBean(DCWsActiveLink.class);
        LOGGER.info("Activated Link Configuration [{}] with activeLink [{}]", linkConfiguration, activeLink);
        return activeLink;
    }

    @Override
    public void shutdownConfiguration(DomibusConnectorLinkConfiguration.LinkConfigName linkConfigurationName) {

    }

    protected Class[] getSources() {
        return new Class[]{DCWsPluginConfiguration.class};
    }


    protected abstract String[] getProfiles();

    protected ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
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
        return Stream.of(
                DCWsLinkPartnerConfigurationProperties.class
        ).collect(Collectors.toList());
    }

}
