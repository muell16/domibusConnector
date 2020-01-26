package eu.domibus.connector.link.api;

import eu.domibus.connector.domain.model.DomibusConnectorLinkConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;

import java.util.Optional;

/**
 * Must be implemented by an link plugin
 */
public interface ActiveLink {

    /**
     * concrete bean to submit a message to
     * the specific link partner
     * @return
     */
    ActiveLinkPartner getActiveLinkPartner(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName);


    /**
     * activates the link partner
     *  - timer jobs are created and started
     *  - start listening on queues, accepts web requests on web interface, ...
     *  - push requests are accepted
     * @param linkPartner
     * @return
     */
    ActiveLinkPartner activateLinkPartner(DomibusConnectorLinkPartner linkPartner);

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
    void shutdownLinkConfig();

    LinkPlugin getPluginManager();

    DomibusConnectorLinkConfiguration getConfiguration();

}
