package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.link.api.ActiveLink;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.link.api.LinkPluginUtils;
import eu.domibus.connector.link.service.LinkPluginQualifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import static eu.domibus.connector.link.impl.wsplugin.DCWsPluginConfiguration.DC_WS_PLUGIN_PROFILE_NAME;

@Component
public class DCWsBackendPlugin extends  AbstractDCWsPlugin implements LinkPlugin {

    private static final Logger LOGGER = LogManager.getLogger(DCWsBackendPlugin.class);
    public static final String IMPL_NAME = "wsbackendplugin";

    @Autowired
    ConfigurableApplicationContext applicationContext;

    @Override
    public boolean canHandle(String implementation) {
        return IMPL_NAME.equals(implementation);
    }

    @Override
    public ActiveLink startConfiguration(DomibusConnectorLinkConfiguration linkConfiguration) {
        ConfigurableApplicationContext springChildContext = LinkPluginUtils.getChildContextBuilder(
                applicationContext
        ).withProfiles(getProfiles())
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

    protected String[] getProfiles() {
        return new String[]{DC_WS_PLUGIN_PROFILE_NAME};
    }




}
