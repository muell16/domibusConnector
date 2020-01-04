package eu.domibus.connector.link.api;

import eu.domibus.connector.controller.service.SubmitToLink;

public interface ActiveLinkPartner {

    ActiveLink getActiveLink();

    void shutdown();

    SubmitToLink getSubmitToLinkBean();

}
