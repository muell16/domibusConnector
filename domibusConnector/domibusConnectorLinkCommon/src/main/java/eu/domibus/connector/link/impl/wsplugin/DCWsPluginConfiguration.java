package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.link.common.WsPolicyLoader;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;


@Configuration
@Profile(DCWsPluginConfiguration.DC_WS_PLUGIN_PROFILE_NAME)
public class DCWsPluginConfiguration {

    private static final Logger LOGGER = LogManager.getLogger(DCWsPluginConfiguration.class);

    public static final String DC_WS_PLUGIN_PROFILE_NAME = "link.wsplugin";

    @Autowired
    DCWsPluginConfigurationProperties configurationProperties;

    @Autowired
    DomibusConnectorLinkConfiguration linkConfiguration;

    @Bean
    DCWsActiveLink dcWsActiveLink() {
        return new DCWsActiveLink();
    }


    @Bean
    public WsPolicyLoader wsPolicyLoader() {
        WsPolicyLoader wsPolicyLoader = new WsPolicyLoader(configurationProperties.getWsPolicy());
        LOGGER.info("Registered wsPolicyLoader [{}]");
        return wsPolicyLoader;
    }




}
