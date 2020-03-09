package eu.domibus.connector.link.util;

import eu.domibus.connector.controller.service.SubmitToLink;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.link.api.ActiveLink;
import eu.domibus.connector.link.api.ActiveLinkPartner;

public class DummyActiveLinkPartner implements ActiveLinkPartner {

    @Override
    public ActiveLink getActiveLink() {
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
