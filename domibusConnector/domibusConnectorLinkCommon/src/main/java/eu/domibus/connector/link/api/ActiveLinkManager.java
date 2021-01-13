package eu.domibus.connector.link.api;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;

import java.util.Optional;

/**
 * Must be implemented by an link plugin
 */
public interface ActiveLinkManager {

    /**
     * concrete bean to submit a message to
     * the specific link partner
     * @return the active link partner or null if none found!
     */
    Optional<ActiveLinkPartner> getActiveLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName);


    /**
     * activates the link partner
     *  - timer jobs are created and started
     *  - start listening on queues, accepts web requests on web interface, ...
     *  - push requests are accepted
     * @param linkPartner
     * @return
     */
    Optional<ActiveLinkPartner> activateLinkPartner(DomibusConnectorLinkPartner linkPartner);

    /**
     * shut down the link connection
     *  - if timer job based timer jobs are disabled
     *  - if listens on queue, webservice - this also gets shutdown
     *  - if push based push interface is closed - further pushes are denied
     * @param linkPartner
     */
    void shutdownLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartner);

    /**
     * closes the current linkConfiguration
     *  - by shutdown of all active link partners on this link configuration
     *  - closing all allocated resources, contexts, ...
     */
//    default void shutdown() {
//        getPluginManager().shutdownConfiguration(getConfigurationName());
//    }
//    void shutdown();


    LinkPlugin getPluginManager();

    default DomibusConnectorLinkConfiguration.LinkConfigName getConfigurationName() {
        return this.getConfiguration().getConfigName();
    }

    DomibusConnectorLinkConfiguration getConfiguration();

    /**
     *
     * @return the current link state
     *  eg. for JMS connected to queue
     */
    default LINK_STATE getLinkState() {
        return LINK_STATE.NO_SUPPORTED;
    }

    public enum LINK_STATE {
        UP, DOWN, NO_SUPPORTED, UNKNOWN;
    }
}
