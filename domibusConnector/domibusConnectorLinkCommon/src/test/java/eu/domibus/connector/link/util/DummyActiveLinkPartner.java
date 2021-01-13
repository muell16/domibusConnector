package eu.domibus.connector.link.util;

import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.ActiveLinkManager;
import eu.domibus.connector.link.api.ActiveLinkPartnerManager;

public class DummyActiveLinkPartner implements ActiveLinkPartnerManager {

    @Override
    public ActiveLinkManager getActiveLink() {
        return null;
    }

    @Override
    public DomibusConnectorLinkPartner getLinkPartner() {
        return null;
    }

    @Override
    public SubmitToLink getSubmitToLinkBean() {
        return null;
    }

}
