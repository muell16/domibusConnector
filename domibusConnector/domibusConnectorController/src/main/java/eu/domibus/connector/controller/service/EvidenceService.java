package eu.domibus.connector.controller.service;

import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;

public interface EvidenceService {

    void handleEvidence(Message confirmationMessage) throws DomibusConnectorControllerException;
}
