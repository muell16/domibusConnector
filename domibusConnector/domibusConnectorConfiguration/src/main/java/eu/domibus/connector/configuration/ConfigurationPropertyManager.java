package eu.domibus.connector.configuration;

import eu.domibus.connector.configuration.domain.ConfigurationProperty;

import java.util.List;

public interface ConfigurationPropertyManager {

    List<ConfigurationProperty> getAll(String basePackage);

}
