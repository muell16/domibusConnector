package eu.domibus.connector.link.api;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;

import java.util.List;

/**
 * Must be implemented by a link plugin
 */
public interface LinkPlugin {

    /**
     *
     * @param implementation - the implementation name
     * @return true if the PluginFactory can handle provide the implementation
     */
    boolean canHandle(String implementation);

    /**
     *
     * @param linkConfiguration - the link configuration
     * @return the active Plugin
     */
    ActiveLink startConfiguration(DomibusConnectorLinkConfiguration linkConfiguration);

    /**
     *
     * @return a list of the supported Features of this plugin
     */
    List<PluginFeature> getFeatures();

    

    /**
     *
     * @return a list of with @ConfigurationProperties annotated classes
     * which represents the plugin properties
     *
     */
    List<Class> getPluginConfigurationProperties();


}
