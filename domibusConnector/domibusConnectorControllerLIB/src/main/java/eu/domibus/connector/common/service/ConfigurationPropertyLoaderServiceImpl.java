package eu.domibus.connector.common.service;

import eu.domibus.connector.common.annotations.ConnectorConversationService;
import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class ConfigurationPropertyLoaderServiceImpl implements ConfigurationPropertyLoaderService {

    private static final Logger LOGGER = LogManager.getLogger(ConfigurationPropertyLoaderServiceImpl.class);

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    @ConnectorConversationService
    private ConversionService conversionService;

    @Override
    @Cacheable //TODO: evict cache if message lane is updated!
    public <T> T loadConfiguration(DomibusConnectorMessageLane.MessageLaneId laneId, Class<T> clazz) {
        if (!AnnotatedElementUtils.hasAnnotation(clazz, ConfigurationProperties.class)) {
            throw new IllegalArgumentException("clazz must be annotated with " + ConfigurationProperties.class);
        }
        LOGGER.debug("Loading property class [{}]", clazz);

        ConfigurationProperties annotation = clazz.getAnnotation(ConfigurationProperties.class);
        String prefix = annotation.prefix();

        return this.loadConfiguration(laneId, clazz, prefix);
    }

    @Cacheable //TODO: evict cache if message lane is updated!
    public <T> T loadConfiguration(@Nullable DomibusConnectorMessageLane.MessageLaneId laneId, Class<T> clazz, String prefix) {
        if (clazz == null) {
            throw new IllegalArgumentException("Clazz is not allowed to be null!");
        }
        if (StringUtils.isEmpty(prefix)) {
            throw new IllegalArgumentException("Prefix is not allowed to be null!");
        }
        LOGGER.debug("Loading property class [{}]", clazz);

        //todo: load message lane properties here, be carefull laneId can be null!
        MapConfigurationPropertySource messageLaneProperties = new MapConfigurationPropertySource(new Properties()) {
            public Object getUnderlyingSource() {
                return String.format("MessageLane [%s] Properties", laneId);
            }
        };

        Environment environment = ctx.getEnvironment();
        Iterable<ConfigurationPropertySource> sources = ConfigurationPropertySources.get(environment);
        PropertySourcesPlaceholdersResolver placeholdersResolver = new PropertySourcesPlaceholdersResolver(environment);

        List<ConfigurationPropertySource> configSources = new ArrayList<>();
        configSources.add(messageLaneProperties);
        sources.forEach(s -> configSources.add(s));

        Binder binder = new Binder(sources, placeholdersResolver, conversionService, null);

        Bindable<T> bindable = Bindable.of(clazz);
        T t = binder.bindOrCreate(prefix, bindable);

        return t;
    }


}
