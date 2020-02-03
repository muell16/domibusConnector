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
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static eu.domibus.connector.link.impl.wsplugin.DCWsPluginConfiguration.DC_WS_BACKEND_PLUGIN_PROFILE_NAME;
import static eu.domibus.connector.link.impl.wsplugin.DCWsPluginConfiguration.DC_WS_PLUGIN_PROFILE_NAME;
import static eu.domibus.connector.link.service.DCLinkPluginConfiguration.LINK_PLUGIN_PROFILE_NAME;

@Component
@Profile(LINK_PLUGIN_PROFILE_NAME)
public class DCWsBackendPlugin extends  AbstractDCWsPlugin implements LinkPlugin {

    private static final Logger LOGGER = LogManager.getLogger(DCWsBackendPlugin.class);
    public static final String IMPL_NAME = "wsbackendplugin";

    @Autowired
    ConfigurableApplicationContext applicationContext;

    @Override
    public boolean canHandle(String implementation) {
        return IMPL_NAME.equals(implementation);
    }


    protected String[] getProfiles() {

        return new String[]{
                DC_WS_BACKEND_PLUGIN_PROFILE_NAME,
                DC_WS_PLUGIN_PROFILE_NAME
        };
    }




}
