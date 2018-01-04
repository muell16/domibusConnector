package eu.domibus.connector.controller.service;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;

public interface EvidenceService {

    void handleEvidence(DomibusConnectorMessage confirmationMessage) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException;
}
