package eu.domibus.connector.link.api;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import org.springframework.boot.Banner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Properties;

public class LinkPluginUtils {

    public static ConfigurableApplicationContext createSpringChildContext(DomibusConnectorLinkConfiguration linkConfiguration, DomibusConnectorLinkPartner linkPartner, ConfigurableApplicationContext applicationContext, List<Class> sources, List<String> profiles) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder();
        builder.parent(applicationContext);
        builder.sources(sources.toArray(new Class[]{}));
        Properties p = linkConfiguration.getProperties();
        p.putAll(linkPartner.getProperties()); //put linkPartner properties into linkConfig properties
        builder.properties(p);
        builder.profiles(profiles.toArray(new String[]{}));
        builder.bannerMode(Banner.Mode.OFF);
        ConfigurableApplicationContext linkModuleApplicationContext = builder.run();
        return linkModuleApplicationContext;
    }

}
