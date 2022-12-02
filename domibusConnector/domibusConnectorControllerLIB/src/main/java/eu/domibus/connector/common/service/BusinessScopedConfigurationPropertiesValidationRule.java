package eu.domibus.connector.common.service;

import eu.domibus.connector.common.ConfigurationPropertyManagerService;
import eu.domibus.connector.common.annotations.BusinessDomainScoped;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import eu.ecodex.dc5.domain.DomainValidationRule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class BusinessScopedConfigurationPropertiesValidationRule implements DomainValidationRule {

//    private final BusinessScopedConfigurationPropertiesRegistrar configRegistrar;
    private final ConfigurationPropertyManagerService configurationPropertyManagerService;
//    private final ApplicationContext applicationContext;
//    private final BeanDefinitionRegistry beanDefinitionRegistry;
    private final BusinessScopedConfigurationPropertiesRegistrar.BusinessScopedConfigurationPropertiesListHolder holder;

    public DCBusinessDomainManager.DomainValidResult validate(DomibusConnectorBusinessDomain.BusinessDomainId id) {


//        String[] beanNamesForAnnotation = applicationContext.getBeanNamesForAnnotation(BusinessDomainScoped.class);
//        String[] configurationProperties = applicationContext.getBeanNamesForAnnotation(ConfigurationProperties.class);
//
//        Set<String> s1 = new HashSet<>();
//        Collections.addAll(s1, beanNamesForAnnotation);
//        Set<String> s2 = new HashSet<>();
//        Collections.addAll(s2, configurationProperties);
//        s1.retainAll(s2);

//        s1.addAll(Collections.add)
//
//        s1.stream()
//                .map(beanName -> beanDefinitionRegistry.getBeanDefinition(beanName))
//                .map(beanDefinition -> beanDefinition.getBeanClassName())
        holder.getBusinessScopeConfigurationPropertyBeanNames().stream()
                .map((s) -> {
                    try {
                        return Class.forName(s);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(clazz -> configurationPropertyManagerService.loadConfiguration(id, clazz))
                .map(obj -> configurationPropertyManagerService.validateConfiguration(id, obj))
                .collect(Collectors.toList());


//                .filter(bd -> bd.getBeanClassName())

//        applicationContext.getBeanDefinitionCount()

//        List<Set<? extends ConstraintViolation<?>>> collect1 = configRegistrar.getConfigClassNames()
//                .stream()
//                .map((s) -> {
//                    try {
//                        return Class.forName(s);
//                    } catch (ClassNotFoundException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//                .map(clazz -> configurationPropertyManagerService.loadConfiguration(id, clazz))
//                .map(obj -> configurationPropertyManagerService.validateConfiguration(id, obj))
//                .collect(Collectors.toList());

        //TODO: convert collect1 into DomainValidResult
        return null;
    }
}
