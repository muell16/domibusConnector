package eu.domibus.connector.configuration;

import eu.domibus.connector.configuration.domain.ConfigurationProperty;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;

import java.util.List;

public interface ConfigurationPropertyManager {

    /**
     * Returns a list of all Properties (within with {@link org.springframework.boot.context.properties.ConfigurationProperties} annotated Classes)
     * in the provided basePackage path
     * @param basePackage
     * @return the list of Properties
     */
    List<ConfigurationProperty> getAll(String basePackage);

    /**
     *
     * @param configurationPropertySource - the property source which provides the properties to check
     * @param basePackageFilter - the filter under which all with with @see {@link org.springframework.boot.context.properties.ConfigurationProperties}
     *                          annotated Properties are bound and checked within this binding
     * @throws org.springframework.boot.context.properties.bind.BindException in case of an failure during binding
     */
    void isConfigurationValid(ConfigurationPropertySource configurationPropertySource, String basePackageFilter);



}
