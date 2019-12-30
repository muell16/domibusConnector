package eu.domibus.connector.link.api;

import eu.domibus.connector.domain.model.DomibusConnectorLinkInfo;
import eu.domibus.connector.link.service.ActiveLink;

import java.util.List;

public interface LinkPluginFactory {

    boolean canHandle(String implementation);

    ActiveLink createLink(DomibusConnectorLinkInfo linkInfo);
}
