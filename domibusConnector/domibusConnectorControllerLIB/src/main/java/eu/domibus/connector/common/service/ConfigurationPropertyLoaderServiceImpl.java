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
import java.util.stream.Collectors;

@Service
public class ConfigurationPropertyLoaderServiceImpl implements ConfigurationPropertyManagerService {

    private static final Logger LOGGER = LogManager.getLogger(ConfigurationPropertyLoaderServiceImpl.class);


    private final ApplicationContext ctx;
    private final ConversionService conversionService;
    private final DCBusinessDomainManagerImpl businessDomainManager;
    private final ConfigurationPropertyCollector configurationPropertyCollector;



    public ConfigurationPropertyLoaderServiceImpl(ApplicationContext ctx,
                                                  @ConnectorConversationService ConversionService conversionService,
                                                  DCBusinessDomainManagerImpl businessDomainManager,
                                                  ConfigurationPropertyCollector configurationPropertyCollector) {
        this.ctx = ctx;
        this.conversionService = conversionService;
        this.businessDomainManager = businessDomainManager;
        this.configurationPropertyCollector = configurationPropertyCollector;
    }

    @Override
    @Cacheable //TODO: evict cache if message lane is updated!
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

    @Cacheable //TODO: evict cache if message lane is updated!
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

//    public <T> T loadConfigurationWithChangeRecorder(@Nullable DomibusConnectorMessageLane.MessageLaneId laneId, Class<T> clazz, String prefix) {
//        T t = loadConfiguration(laneId, clazz, prefix);
////        Proxy p = new Proxy
//        ProxyFactory pf = new ProxyFactory(t);
//
//        return (T) pf.getProxy();
//
//    }

    private MapConfigurationPropertySource loadLaneProperties(DomibusConnectorBusinessDomain.BusinessDomainId laneId) {
        Optional<DomibusConnectorBusinessDomain> businessDomain = businessDomainManager.getBusinessDomain(laneId);
        if (businessDomain.isPresent()) {
            MapConfigurationPropertySource mapConfigurationPropertySource = new MapConfigurationPropertySource(businessDomain.get().getMessageLaneProperties());
            return mapConfigurationPropertySource;
        } else {
            throw new IllegalArgumentException(String.format("No active business domain for id [%s]", laneId));
        }
    }

    @Override
    public void updateConfiguration(DomibusConnectorBusinessDomain.BusinessDomainId laneId, Object configurationClazz) {
        if (laneId == null) {
            throw new IllegalArgumentException("LaneId is not allowed to be null!");
        }

        Map<String, String> props = createPropertyMap(laneId, configurationClazz);
        businessDomainManager.updateConfig(laneId, props);

        ctx.publishEvent(new BusinessDomainConfigurationChange(this, laneId, props));
    }

    Map<String, String> createPropertyMap(DomibusConnectorBusinessDomain.BusinessDomainId laneId, Object configurationClazz) {
        HashMap<String, String> propertyMap = new HashMap<>();
        String prefix = getPrefixFromAnnotation(configurationClazz.getClass());
        readBeanPropertiesToStringMap(configurationClazz, propertyMap, prefix);
        return mapCamelCaseToLowerHyphen(propertyMap);
    }

    //convert property names from KebabCase to CamelCase
    // eg.:  cn-name to cnName
    private Map<String, String> mapCamelCaseToLowerHyphen(Map<String, String> properties) {
        Map<String, String> map = properties
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, e.getKey()), e -> e.getValue()));
        return map;
    }

    private void readBeanPropertiesToStringMap(Object configurationClazz, HashMap<String, String> properties, String prefix) {

//        try {
//            Map<String, String> describe = BeanUtils.describe(configurationClazz);
//            describe.entrySet()
//                    .stream()
//                    .fil
//            PropertyUtils.isWriteable();
//        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            throw new RuntimeException(e);
//        }

        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(configurationClazz);
//        ConfigurablePropertyAccessor configurablePropertyAccessor = PropertyAccessorFactory.forDirectFieldAccess(configurationClazz);
//
//        TypeDescriptor hallo = configurablePropertyAccessor.getPropertyTypeDescriptor("hallo");
//
//
//
        Collection<ConfigurationProperty> configurationPropertyFromClazz = configurationPropertyCollector.getConfigurationPropertyFromClazz(configurationClazz.getClass());
        Map<String, String> collect = configurationPropertyFromClazz.stream()
                .map(ConfigurationProperty::getPropertyName)
                .collect(Collectors.toMap(name -> name, name -> String.valueOf(beanWrapper.getPropertyValue(name.substring(prefix.length() + 1)))));

        properties.putAll(collect);

//        Arrays.stream(beanWrapper.getPropertyDescriptors())
//                .filter(pd -> !"class".matches(pd.getName()))
//                .forEach(
//                pd -> {
//                    TypeDescriptor propertyTypeDescriptor = beanWrapper.getPropertyTypeDescriptor(pd.getName());
//                    Object propertyValue = beanWrapper.getPropertyValue(pd.getName());
//                    if (propertyTypeDescriptor == null) {
//                        throw new IllegalArgumentException("Property type descriptor is null!");
//                    } else if (propertyTypeDescriptor.hasAnnotation(NestedConfigurationProperty.class)) {
//                        readBeanPropertiesToStringMap(propertyValue, properties, prefix + "." + pd.getName());
//                    } else {
//                        if (propertyValue != null) {
//                            properties.put(prefix + "." + pd.getName(), propertyValue.toString());
//                        }
//                    }
//                }
//        );

    }


}
