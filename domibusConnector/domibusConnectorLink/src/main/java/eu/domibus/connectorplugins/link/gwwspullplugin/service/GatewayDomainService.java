package eu.domibus.connectorplugins.link.gwwspullplugin.service;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

public interface GatewayDomainService {

    /**
     * This method decides which gateway domain is associated with a given message
     * @param message the message
     * @return the domain name
     */
    String discoverDomain(DomibusConnectorMessage message);

    /**
     * This method returns the plugin user that will be used for the authentication at the given domain on the gateway plugin side
     * @param domain the domain name
     * @return the plugin user
     */
    String discoverPluginUserForDomain(String domain);

}
