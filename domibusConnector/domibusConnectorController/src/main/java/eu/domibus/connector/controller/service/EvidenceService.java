package eu.domibus.connector.controller.service;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.domain.Message;

public interface EvidenceService {

    void handleEvidence(Message confirmationMessage) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException;
}
