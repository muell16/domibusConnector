package eu.domibus.connector.controller.process.util;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;

public interface ConfirmationMessageBuilderFactory {

    CreateConfirmationMessageBuilderFactoryImpl.ConfirmationMessageBuilder createConfirmationMessageBuilder(DomibusConnectorMessage message, DomibusConnectorEvidenceType evidenceType);

    DomibusConnectorAction createEvidenceAction(DomibusConnectorEvidenceType type) throws DomibusConnectorControllerException;

    public interface ConfirmationMessageBuilder {

    }

}
