package eu.domibus.connector.link.api;

import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;

public interface ActiveLinkPartnerManager {

    ActiveLinkManager getActiveLink();

    default void shutdown() {
        getActiveLink().shutdownLinkPartner(getLinkPartnerName());
    }

    default DomibusConnectorLinkPartner.LinkPartnerName getLinkPartnerName() {
        return getLinkPartner().getLinkPartnerName();
    }

    DomibusConnectorLinkPartner getLinkPartner();

    SubmitToLink getSubmitToLinkBean();

}
