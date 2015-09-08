package eu.domibus.connector.nbc;

import eu.domibus.connector.common.exception.ImplementationMissingException;
import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.nbc.exception.DomibusConnectorNationalBackendClientException;

public class DomibusConnectorNationalBackendClientDefaultImpl implements DomibusConnectorNationalBackendClient {

    @Override
    public void deliverMessage(Message message) throws DomibusConnectorNationalBackendClientException,
            ImplementationMissingException {
        throw new ImplementationMissingException("DomibusConnectorNationalBackendClient", "deliverMessage");

    }

    @Override
    public String[] requestMessagesUnsent() throws DomibusConnectorNationalBackendClientException,
            ImplementationMissingException {
        throw new ImplementationMissingException("DomibusConnectorNationalBackendClient", "requestMessagesUnsent");
    }

    @Override
    public void requestMessage(Message message) throws DomibusConnectorNationalBackendClientException,
            ImplementationMissingException {
        throw new ImplementationMissingException("DomibusConnectorNationalBackendClient", "requestMessage");
    }

    @Override
    public Message[] requestConfirmations() throws DomibusConnectorNationalBackendClientException,
            ImplementationMissingException {
        throw new ImplementationMissingException("DomibusConnectorNationalBackendClient", "requestConfirmations");
    }

    @Override
    public void deliverLastEvidenceForMessage(Message confirmationMessage)
            throws DomibusConnectorNationalBackendClientException, ImplementationMissingException {
        throw new ImplementationMissingException("DomibusConnectorNationalBackendClient",
                "deliverLastEvidenceForMessage");
    }

}
