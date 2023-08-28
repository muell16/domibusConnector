package eu.domibus.connectorplugins.link.gwwspullplugin.service;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connectorplugins.link.gwwspullplugin.DCGatewayPullPluginConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleGatewayDomainService implements GatewayDomainService {


    @Autowired
    DCGatewayPullPluginConfigurationProperties configurationProperties;

    @Override
    public String discoverDomain(DomibusConnectorMessage message) {
        String[] partyIdSplit = message.getMessageDetails().getFromParty().getPartyIdType().split(":");
        String domain = configurationProperties.getDomainAssignment().get(partyIdSplit[partyIdSplit.length-1]);
        return domain;
    }

    /**
     * Returns the domain as the plugin user
     * @param domain the domain name
     * @return the domain name as the plugin user
     */
    @Override
    public String discoverPluginUserForDomain(String domain) {
        return domain;
    }
}
