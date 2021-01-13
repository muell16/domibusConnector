package eu.domibus.connector.controller.service;

import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;

/**
 * Generic interface to trigger message
 * pull from LinkPartner
 */
public interface PullFromLink {

    void pullMessagesFrom(DomibusConnectorLinkPartner.LinkPartnerName linkPartnerName);

}
