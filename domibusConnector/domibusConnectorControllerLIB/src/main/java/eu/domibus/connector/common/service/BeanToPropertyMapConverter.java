package eu.domibus.connector.common.service;

import com.google.common.base.CaseFormat;
import eu.ecodex.utils.configuration.domain.ConfigurationProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.core.convert.ConversionService;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BeanToPropertyMapConverter {

    private static final Logger LOGGER = LogManager.getLogger(BeanToPropertyMapConverter.class);

    private final ConversionService conversionService;

    public BeanToPropertyMapConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public Map<String, String> readBeanPropertiesToMap(Object configurationClazz, String prefix) {
        ToPropertyConverter converter = new ToPropertyConverter();
        converter.convertToProperties(configurationClazz, prefix);
        return converter.getProperties();
    }

    private class ToPropertyConverter {
        HashMap<String, String> properties = new HashMap<>();

        void convertToProperties(Object bean, String prefix) {

        }

        void convertToProperties(Object bean, String prefix, boolean nested) {
            Class beanType = bean.getClass();
            if (Collection.class.isAssignableFrom(bean.getClass())) {
                //is collection
                convertCollectionToProperties(bean, prefix);
            } else if (!nested && conversionService.canConvert(beanType, String.class)) {
                //TODO: run conversion...

            } else {
                //nested bean...
                convertNestedToProperties(bean, prefix);
            }

        }

        private void convertNestedToProperties(Object bean, String prefix) {

        }

        private void convertCollectionToProperties(Object bean, String prefix) {

        }

        public HashMap<String, String> getProperties() {
            return properties;
        }

    }

//    private static class ToPropertyConverter {
//        HashMap<String, String> properties = new HashMap<>();
//        Object bean;
//        String currentPropertyName;
//    }

    private Map<String, String> readBeanPropertiesToStringMap(Object configurationClazz, String prefix) {
        HashMap<String, String> properties = new HashMap<>();
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(configurationClazz);
        Collection<ConfigurationProperty> configurationPropertyFromClazz = configurationPropertyCollector.getConfigurationPropertyFromClazz(configurationClazz.getClass());




        configurationPropertyFromClazz.stream()
                .map(ConfigurationProperty::getPropertyName) // get property name
                .map(name -> name.substring(prefix.length() + 1)) // strip prefix
                .forEach( name -> {
                            ToPropertyConverterObject o = new ConfigurationPropertyLoaderServiceImpl.ToPropertyConverterObject();
                            o.bean = configurationClazz;
                            o.properties = properties;
                            o.currentPropertyName = createPropertyName(prefix, name);
                            addToStringConvertedPropertyToPropMap(o, beanWrapper);
//                           properties.put(createPropertyName(prefix, name), getToStringConvertedPropertyValue(beanWrapper, name));
                        }

                );
        return properties;
    }

    private String createPropertyName(String prefix, String name) {
        name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, name);
        return ConfigurationPropertyName.of(prefix + "." + name).toString();
    }

    private void addToStringConvertedPropertyToPropMap(ConfigurationPropertyLoaderServiceImpl.ToPropertyConverterObject o, BeanWrapper beanWrapper) {
        String name = o.currentPropertyName;
        try {
            Class propertyType = beanWrapper.getPropertyType(name);
            Object propertyValue = beanWrapper.getPropertyValue(name);
            if (propertyValue == null) {
//                return null;
                return;
            }
            //handle lists
            if (Collection.class.isAssignableFrom(propertyType)) {
                Collection c = (Collection) propertyValue;
                ConfigurationPropertyLoaderServiceImpl.ToPropertyConverterObject obj = new ConfigurationPropertyLoaderServiceImpl.ToPropertyConverterObject();
                obj.bean = propertyValue;
                obj.properties = o.properties;
                obj.currentPropertyName = name;
                addToPropMap(o, c);
//                return null; // TODO: handle lists...
                //TODO: map collection
                return;
            }
            if (conversionService.canConvert(propertyValue.getClass(), String.class)) {
                String stringValue = conversionService.convert(propertyValue, String.class);
//                return stringValue;
                o.properties.put(name, stringValue);
            } else {
                o.properties.put(name, propertyValue.toString());
            }
//            return propertyValue.toString();
        } catch (NullValueInNestedPathException nullValueInNestedPathException) {
            LOGGER.error("Exception occured", nullValueInNestedPathException);
            return;
        }
    }

    private void addToPropMap(ConfigurationPropertyLoaderServiceImpl.ToPropertyConverterObject o, Collection c) {
        int i = 0;
        for (Object item : c) {
            String propName = o.currentPropertyName +
                    "[" +
                    i +
                    "]";
            ConfigurationPropertyLoaderServiceImpl.ToPropertyConverterObject m = new ConfigurationPropertyLoaderServiceImpl.ToPropertyConverterObject();
            m.currentPropertyName = propName;
            m.bean = item;
            m.properties = o.properties;

            addToPropMap(m);

            i++;
        };
    }

    private void addToPropMap(ConfigurationPropertyLoaderServiceImpl.ToPropertyConverterObject m) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(m.bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String propName = propertyDescriptor.getName();
                Object property = PropertyUtils.getProperty(m.bean, propName);

            }
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private String getToStringConvertedPropertyValue(BeanWrapper beanWrapper, String name) {

        try {
            Class propertyType = beanWrapper.getPropertyType(name);
            Object propertyValue = beanWrapper.getPropertyValue(name);
            if (propertyValue == null) {
                return null;
            }
            if (Collection.class.isAssignableFrom(propertyType)) {
                //is collection: ignore
                return null;
            }
            if (conversionService.canConvert(propertyValue.getClass(), String.class)) {
                String stringValue = conversionService.convert(propertyValue, String.class);
                return stringValue;
            }
            return propertyValue.toString();
        } catch (NullValueInNestedPathException nullValueInNestedPathException) {
            return null;
        }
//        throw new RuntimeException(String.format("Cannot convert Property of type [%s] to String!", propertyValue.getClass()));
    }


}
