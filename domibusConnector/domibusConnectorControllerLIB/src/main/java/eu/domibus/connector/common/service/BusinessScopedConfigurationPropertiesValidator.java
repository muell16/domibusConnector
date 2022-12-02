package eu.domibus.connector.common.service;

import eu.domibus.connector.common.annotations.BusinessDomainScoped;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import lombok.Getter;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//TOOD: import this in starter

@Getter
public class BusinessScopedConfigurationPropertiesValidator implements ImportBeanDefinitionRegistrar {

    private final List<String> configClassNames = new ArrayList<>();

//    @Override
//    public DCBusinessDomainManager.DomainValidResult validate(DomibusConnectorBusinessDomain.BusinessDomainId id) {
//
//        return null;
//    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        if( importingClassMetadata.hasAnnotation(BusinessDomainScoped.class.getName()) && importingClassMetadata.hasAnnotation(ConfigurationProperties.class.getName())) {
            configClassNames.add(importingClassMetadata.getClassName());
        }

    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

    }
}
