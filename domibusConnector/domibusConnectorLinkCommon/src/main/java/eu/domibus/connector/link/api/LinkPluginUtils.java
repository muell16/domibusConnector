package eu.domibus.connector.link.api;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.exception.LinkPluginException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Optional;
import java.util.Properties;


public class LinkPluginUtils {

    private static final Logger LOGGER = LogManager.getLogger(LinkPluginUtils.class);

    public static ConfigurableApplicationContext createSpringChildContext(DomibusConnectorLinkConfiguration linkConfiguration, DomibusConnectorLinkPartner linkPartner, ConfigurableApplicationContext applicationContext, List<Class> sources, List<String> profiles) {
        try {
            SpringApplicationBuilder builder = new SpringApplicationBuilder();
            builder.parent(applicationContext);
            builder.sources(sources.toArray(new Class[]{}));
            Properties p = linkConfiguration.getProperties();
            p.putAll(linkPartner.getProperties()); //put linkPartner properties into linkConfig properties
            builder.properties(p);
            builder.profiles(profiles.toArray(new String[]{}));
            builder.bannerMode(Banner.Mode.OFF);
            builder.web(WebApplicationType.NONE);
            ConfigurableApplicationContext linkModuleApplicationContext = builder.run();
            return linkModuleApplicationContext;
        } catch (Exception e) {
            LOGGER.error("Exception ", e);
            throw new LinkPluginException(e);
        }
    }

}
