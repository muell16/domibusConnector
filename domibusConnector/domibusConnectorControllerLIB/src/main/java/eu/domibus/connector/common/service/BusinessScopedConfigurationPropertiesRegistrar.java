package eu.domibus.connector.common.service;

import eu.domibus.connector.common.annotations.BusinessDomainScoped;
import lombok.Getter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BusinessScopedConfigurationPropertiesRegistrar implements ImportBeanDefinitionRegistrar {

    public static final String BEAN_DEFINITION_NAME = "BusinessScopedConfigurationPropertiesRegistrarHolder";

    public BusinessScopedConfigurationPropertiesListHolder businessScopedConfigurationProperties() {
        return new BusinessScopedConfigurationPropertiesListHolder();
    }

    private final List<String> beanNames = new ArrayList<>();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        this.registerBeanDefinitions(importingClassMetadata, registry);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        if( importingClassMetadata.hasAnnotation(BusinessDomainScoped.class.getName()) && importingClassMetadata.hasAnnotation(ConfigurationProperties.class.getName())) {
            beanNames.add(importingClassMetadata.getClassName());
        }
        if (!registry.containsBeanDefinition(BEAN_DEFINITION_NAME)) {
            GenericBeanDefinition definition = new GenericBeanDefinition();
            definition.setRole(BeanDefinition.ROLE_SUPPORT);
            definition.setBeanClass(BusinessScopedConfigurationPropertiesListHolder.class);
            definition.setInstanceSupplier(this::businessScopedConfigurationProperties);
            registry.registerBeanDefinition(BEAN_DEFINITION_NAME, definition);
        }
    }

    public class BusinessScopedConfigurationPropertiesListHolder {
        public List<String> getBusinessScopeConfigurationPropertyBeanNames() {
            return BusinessScopedConfigurationPropertiesRegistrar.this.beanNames;
        }
    }
}
