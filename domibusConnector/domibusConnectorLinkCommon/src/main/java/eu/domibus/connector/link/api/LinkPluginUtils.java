package eu.domibus.connector.link.api;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.exception.LinkPluginException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.*;

import java.util.*;


public class LinkPluginUtils {

    private static final Logger LOGGER = LogManager.getLogger(LinkPluginUtils.class);

    public static class ChildContextBuilder {
        Map<String, Object> addedSingeltons = new HashMap<>();
        SpringApplicationBuilder builder = new SpringApplicationBuilder();

        private ChildContextBuilder(ConfigurableApplicationContext parent) {
            builder.parent(parent);
            builder.bannerMode(Banner.Mode.OFF);
            builder.web(WebApplicationType.NONE);
        }

        public ChildContextBuilder addSingelton(Object bean) {
            this.addedSingeltons.put(bean.getClass().getName(), bean);
            return this;
        }

        public ChildContextBuilder addSingelton(String name, Object bean) {
            this.addedSingeltons.put(name, bean);
            return this;
        }

        public ChildContextBuilder withSources(Class<?> ... sources) {
            builder.sources(sources);
            return this;
        }

        public ChildContextBuilder withDomibusConnectorLinkConfiguration(DomibusConnectorLinkConfiguration linkConfig) {
            builder.properties(linkConfig.getProperties());
            return this.addSingelton("linkConfig", linkConfig);
        }

        public ChildContextBuilder withDomibusConnectorLinkPartner(DomibusConnectorLinkPartner linkPartner) {
            builder.properties(linkPartner.getProperties());
            return this.addSingelton("linkPartner", linkPartner);
        }

        public ChildContextBuilder withProfiles(String... profiles) {
            builder.profiles(profiles);
            return this;
        }


        public ChildContextBuilder withProperties(String... properties) {
            builder.properties(properties);
            return this;
        }

        public ChildContextBuilder withProperties(Properties properties) {
            builder.properties(properties);
            return this;
        }



        public ConfigurableApplicationContext run(String... args) {
            builder.initializers((ApplicationContextInitializer<ConfigurableApplicationContext>) applicationContext -> addedSingeltons.entrySet().forEach(entry -> {
                LOGGER.trace("Adding singelton with name [{}] as bean [{}]", entry.getKey(), entry.getValue());
                applicationContext.getBeanFactory().registerSingleton(entry.getKey(), entry.getValue());
            }));

            return builder.run(args);
        }

    }

    public static ChildContextBuilder getChildContextBuilder(ConfigurableApplicationContext ctx) {
        return new ChildContextBuilder(ctx);
    }


//    public static ConfigurableApplicationContext createSpringChildContext(DomibusConnectorLinkConfiguration linkConfiguration, DomibusConnectorLinkPartner linkPartner, ConfigurableApplicationContext applicationContext, List<Class> sources, List<String> profiles) {
//        try {
//            SpringApplicationBuilder builder = new SpringApplicationBuilder();
//            builder.parent(applicationContext);
//            builder.sources(sources.toArray(new Class[]{}));
//            builder.lazyInitialization(true);
//            Properties p = linkConfiguration.getProperties();
//            if (linkPartner != null) {
//                p.putAll(linkPartner.getProperties()); //put linkPartner properties into linkConfig properties
//            }
//            LOGGER.trace("Passing properties to link activation [{}]", p);
//            builder.properties(p);
//            builder.profiles(profiles.toArray(new String[]{}));
//            builder.bannerMode(Banner.Mode.OFF);
//            builder.web(WebApplicationType.NONE);
//
//
//
//            ConfigurableApplicationContext linkModuleApplicationContext = builder.run();
//            linkModuleApplicationContext.getBeanFactory().registerSingleton("linkConfiguration", linkConfiguration);
//            return linkModuleApplicationContext;
//        } catch (Exception e) {
//            LOGGER.error("Exception ", e);
//            throw new LinkPluginException(e);
//        }
//    }

}
