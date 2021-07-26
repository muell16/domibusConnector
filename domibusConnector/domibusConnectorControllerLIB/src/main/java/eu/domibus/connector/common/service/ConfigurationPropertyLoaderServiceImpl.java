package eu.domibus.connector.common.service;

import com.google.common.base.CaseFormat;
import eu.domibus.connector.common.annotations.ConnectorConversationService;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.ecodex.utils.configuration.domain.ConfigurationProperty;
import eu.ecodex.utils.configuration.service.ConfigurationPropertyCollector;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ConfigurationPropertyLoaderServiceImpl implements ConfigurationPropertyManagerService {

    private static final Logger LOGGER = LogManager.getLogger(ConfigurationPropertyLoaderServiceImpl.class);


    private final ApplicationContext ctx;
    private final ConversionService conversionService;
    private final DCBusinessDomainManager businessDomainManager;
    private final ConfigurationPropertyCollector configurationPropertyCollector;



    public ConfigurationPropertyLoaderServiceImpl(ApplicationContext ctx,
                                                  @ConnectorConversationService ConversionService conversionService,
                                                  DCBusinessDomainManager businessDomainManager,
                                                  ConfigurationPropertyCollector configurationPropertyCollector) {
        this.ctx = ctx;
        this.conversionService = conversionService;
        this.businessDomainManager = businessDomainManager;
        this.configurationPropertyCollector = configurationPropertyCollector;
    }

    @Override
    public <T> T loadConfiguration(DomibusConnectorBusinessDomain.BusinessDomainId laneId, Class<T> clazz) {
        String prefix = getPrefixFromAnnotation(clazz);

        return this.loadConfiguration(laneId, clazz, prefix);
    }

    private String getPrefixFromAnnotation(Class<?> clazz) {
        if (!AnnotatedElementUtils.hasAnnotation(clazz, ConfigurationProperties.class)) {
            throw new IllegalArgumentException("clazz must be annotated with " + ConfigurationProperties.class);
        }
        LOGGER.debug("Loading property class [{}]", clazz);

        ConfigurationProperties annotation = clazz.getAnnotation(ConfigurationProperties.class);
        String prefix = annotation.prefix();
        return prefix;
    }

    public <T> T loadConfiguration(@Nullable DomibusConnectorBusinessDomain.BusinessDomainId laneId, Class<T> clazz, String prefix) {
        if (clazz == null) {
            throw new IllegalArgumentException("Clazz is not allowed to be null!");
        }
        if (StringUtils.isEmpty(prefix)) {
            throw new IllegalArgumentException("Prefix is not allowed to be null!");
        }
        LOGGER.debug("Loading property class [{}]", clazz);

        MapConfigurationPropertySource messageLaneProperties = loadLaneProperties(laneId);

        Environment environment = ctx.getEnvironment();
        Iterable<ConfigurationPropertySource> sources = ConfigurationPropertySources.get(environment);
        List<ConfigurationPropertySource> configSources = new ArrayList<>();
        configSources.add(messageLaneProperties);
        sources.forEach(s -> configSources.add(s));

        PropertySourcesPlaceholdersResolver placeholdersResolver = new PropertySourcesPlaceholdersResolver(environment);

        Binder binder = new Binder(sources, placeholdersResolver, conversionService, null);

        Bindable<T> bindable = Bindable.of(clazz);
        T t = binder.bindOrCreate(prefix, bindable);

        return t;
    }


    private MapConfigurationPropertySource loadLaneProperties(DomibusConnectorBusinessDomain.BusinessDomainId laneId) {
        Optional<DomibusConnectorBusinessDomain> businessDomain = businessDomainManager.getBusinessDomain(laneId);
        if (businessDomain.isPresent()) {
            MapConfigurationPropertySource mapConfigurationPropertySource = new MapConfigurationPropertySource(businessDomain.get().getMessageLaneProperties());
            return mapConfigurationPropertySource;
        } else {
            throw new IllegalArgumentException(String.format("No active business domain for id [%s]", laneId));
        }
    }

    /**
     *
     * A {@link BusinessDomainConfigurationChange} event is fired with the changed properties
     * and affected BusinessDomain
     * So factories, Scopes can react to this event and refresh the settings
     *
     * @param laneId the laneId, if null defaultLaneId is used
     * @param updatedConfigClazz - the configurationClazz which has been altered, updated
     *                           only the changed properties are updated at the configuration source
     *
     *
     */
    @Override
    public void updateConfiguration(DomibusConnectorBusinessDomain.BusinessDomainId laneId, Object updatedConfigClazz) {
        if (laneId == null) {
            throw new IllegalArgumentException("LaneId is not allowed to be null!");
        }

        Object currentConfig = this.loadConfiguration(laneId, updatedConfigClazz.getClass());
        Map<String, String> previousProps = createPropertyMap(laneId, currentConfig); //collect current active properties
        Map<String, String> props = createPropertyMap(laneId, updatedConfigClazz); //collect updated properties

        //only collect differences
        Map<String, String> diffProps = new HashMap<>();
        props.entrySet().stream()
                .filter(entry -> !Objects.equals(previousProps.get(entry.getKey()), entry.getValue()))
                .forEach(e -> diffProps.put(e.getKey().toString(), e.getValue()));

        LOGGER.debug("Updating of [{}] the following properties [{}]", updatedConfigClazz.getClass(), diffProps);

        businessDomainManager.updateConfig(laneId, diffProps);
        ctx.publishEvent(new BusinessDomainConfigurationChange(this, laneId, diffProps));
    }

    Map<String, String> createPropertyMap(DomibusConnectorBusinessDomain.BusinessDomainId laneId, Object configurationClazz) {
        String prefix = getPrefixFromAnnotation(configurationClazz.getClass());
        Map<String, String> propertyMap = readBeanPropertiesToStringMap(configurationClazz, prefix);
        return propertyMap;
//        return mapCamelCaseToLowerHyphen(propertyMap);
    }

    //convert property names from KebabCase to CamelCase
    // eg.:  cn-name to cnName
//    private Map<String, String> mapCamelCaseToLowerHyphen(Map<String, String> properties) {
//        Map<String, String> map = new HashMap<>();
//        properties.forEach((key, value) -> map.put(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, key), value));
////        properties.forEach((key, value) -> map.put(key.replace("-", ""), value));
//        return map;
//    }

    private Map<String, String> readBeanPropertiesToStringMap(Object configurationClazz, String prefix) {
        HashMap<String, String> properties = new HashMap<>();
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(configurationClazz);
        Collection<ConfigurationProperty> configurationPropertyFromClazz = configurationPropertyCollector.getConfigurationPropertyFromClazz(configurationClazz.getClass());
        configurationPropertyFromClazz.stream()
                .map(ConfigurationProperty::getPropertyName) // get property name
                .map(name -> name.substring(prefix.length() + 1)) // strip prefix
                .forEach( name -> properties.put(createPropertyName(prefix, name), getToStringConvertedPropertyValue(beanWrapper, name))
                );
        return properties;
    }

    private String createPropertyName(String prefix, String name) {
        name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, name);
        return ConfigurationPropertyName.of(prefix + "." + name).toString();
    }

    private String getToStringConvertedPropertyValue(BeanWrapper beanWrapper, String name) {
        Object propertyValue = beanWrapper.getPropertyValue(name);
        if (propertyValue == null) {
            return null;
        }
        if (conversionService.canConvert(propertyValue.getClass(), String.class)) {
            String stringValue = conversionService.convert(propertyValue, String.class);
            return stringValue;
        }
        return propertyValue.toString();
//        throw new RuntimeException(String.format("Cannot convert Property of type [%s] to String!", propertyValue.getClass()));
    }


}
