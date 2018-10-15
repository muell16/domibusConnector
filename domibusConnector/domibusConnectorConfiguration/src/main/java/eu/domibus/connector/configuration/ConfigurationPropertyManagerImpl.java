package eu.domibus.connector.configuration;


import eu.domibus.connector.configuration.annotation.ConfigurationDescription;
import eu.domibus.connector.configuration.annotation.ConfigurationLabel;
import eu.domibus.connector.configuration.domain.ConfigurationProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.validation.ValidationBindHandler;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.PropertySources;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ConfigurationPropertyManagerImpl implements ConfigurationPropertyManager {

    private static final Logger LOGGER = LogManager.getLogger(ConfigurationPropertyManager.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Validator validator;

    //TODO: isPropertyValid function....

    /**
     * Tests if the configuration is valid, all properties are loaded from the
     * provided configuration source
     * @param configurationPropertySource - the propertySources
     * @param basePackageFilter - is only scanning with @ConfigurationProperties annotated classes under the specified package
     */
    public void isConfigurationValid(ConfigurationPropertySource configurationPropertySource, String basePackageFilter) {
        Map<String, Object> configurationBeans = applicationContext.getBeansWithAnnotation(ConfigurationProperties.class);

        configurationBeans.entrySet().stream()
                .filter(new PackageFilter(basePackageFilter))
                .forEach(entry -> {

            Class<?> configClass = entry.getValue().getClass();
            try {
                Object config = configClass.newInstance();
                ConfigurationProperties annotation = AnnotationUtils.getAnnotation(configClass, ConfigurationProperties.class);
                String prefix = (String) AnnotationUtils.getValue(annotation);

                Bindable<?> bindable = Bindable.of(configClass).withAnnotations(annotation);
                Binder b = new Binder(configurationPropertySource);
                LOGGER.debug("Binding class [{}] with prefix [{}]", configClass, prefix);

                ValidationBindHandler validationBindHandler = new ValidationBindHandler(validator);

                BindResult<?> bind = b.bind(prefix, bindable, validationBindHandler);


                if (bind.isBound()) {
                    LOGGER.trace("is bound!");
                }
                //TODO: validate bounded variables


            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


        });
    }

    /**
     * returns a list of all configuration properties
     *  each ConfigurationProperty object holds the configuration property key, optional if set a description and label name
     *  for further information see {@link ConfigurationProperty}
     *
     * @param basePackageFilter - the provided string is used as a filter, only with {@link ConfigurationProperties} annotated classes
     *                          under this package path are scanned and returned
     *
     * @return a list of ConfigurationProperty objects
     */
    @Override
    public List<ConfigurationProperty> getAll(String basePackageFilter) {
        Map<String, Object> configurationBeans = applicationContext.getBeansWithAnnotation(ConfigurationProperties.class);

        List<ConfigurationProperty> collect = configurationBeans.entrySet()
                .stream()
                //filter out classes which aren't in package path basePackageFilter
                .filter(new PackageFilter(basePackageFilter))
                .map(this::processBean)
                .flatMap(Function.identity())
                .collect(Collectors.toList());

        return collect;
    }

    private Stream<ConfigurationProperty> processBean(Map.Entry<String, Object> entry) {
        LOGGER.trace("processing config bean with name: [{}]", entry.getKey());
        Object bean = entry.getValue();

        Class<?> beanClass = bean.getClass();

//        if (!beanClass.getPackage().getName().startsWith(basePackageFilter)) {
//            LOGGER.debug("ignore bean [{}] because its not in the scanning package path [{}]", beanClass, basePackageFilter);
//            return Stream.empty();
//        }
        ConfigurationProperties configurationProperties = beanClass.getAnnotation(ConfigurationProperties.class);
        String pPrefix = configurationProperties.prefix();
        if (pPrefix.length() > 0) {
            pPrefix = pPrefix + ".";
        }
        final String propertyPrefix = pPrefix;


        Field[] fields = beanClass.getDeclaredFields(); //TODO: also scan inherited fields...
        return Stream.of(fields)
                .map(this::processFieldOfBean)
                .map(c -> {c.setPropertyName(propertyPrefix + c.getPropertyName()); return c;})
                ;
    }

    private ConfigurationProperty processFieldOfBean(Field field) {
        LOGGER.trace("processing field [{}]", field);
        ConfigurationProperty c = new ConfigurationProperty();
        c.setPropertyName(field.getName());

        ConfigurationDescription descriptionAnnotation = AnnotationUtils.getAnnotation(field, ConfigurationDescription.class);
        if (descriptionAnnotation != null) {
            String description = (String) AnnotationUtils.getValue(descriptionAnnotation, "description");
            c.setDescription(description);
        }

        ConfigurationLabel configLabelAnnotation = AnnotationUtils.getAnnotation(field, ConfigurationLabel.class);
        if (configLabelAnnotation != null) {
            String label = (String) AnnotationUtils.getValue(configLabelAnnotation);
            c.setLabel(label);
        }

        return c;
    }

    private static class PackageFilter implements Predicate<Map.Entry<String, Object>> {

        private final String basePackageFilter;

        public PackageFilter(String basePackageFilter) {
            this.basePackageFilter = basePackageFilter;
        }

        @Override
        public boolean test( Map.Entry<String, Object> entry) {
            return entry.getValue().getClass().getPackage().getName().startsWith(basePackageFilter);
        }
    }

}
