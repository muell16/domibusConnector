package eu.domibus.connector.link.api;

import eu.domibus.connector.controller.service.PullFromLink;
import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;

import java.util.List;
import java.util.Optional;

/**
 * Must be implemented by a link plugin
 */
public interface LinkPlugin {

    default String getPluginName() {
        return this.getClass().getSimpleName().toLowerCase();
    }

    default String getPluginDescription() {
        return this.toString();
    }

    /**
     *
     * @param implementation - the implementation name
     * @return true if the PluginFactory can handle provide the implementation
     */
    default boolean canHandle(String implementation) {
        return getPluginName().equals(implementation);
    }

    /**
     *
     * @param linkConfiguration - the link configuration
     * @return the active Plugin
     */
    ActiveLink startConfiguration(DomibusConnectorLinkConfiguration linkConfiguration);


    void shutdownConfiguration(ActiveLink activeLink);


    public ActiveLinkPartner enableLinkPartner(DomibusConnectorLinkPartner linkPartner, ActiveLink activeLink);

    void shutdownActiveLinkPartner(ActiveLinkPartner linkPartner);

    SubmitToLink getSubmitToLink(ActiveLinkPartner linkPartner);

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

    List<Class> getPartnerConfigurationProperties();


    default Optional<PullFromLink> getPullFromLink(ActiveLinkPartner activeLinkPartner) { return Optional.empty();}
}
