package eu.ecodex.connector.nbc;

import eu.ecodex.connector.common.exception.ImplementationMissingException;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.nbc.exception.ECodexConnectorNationalBackendClientException;

public class ECodexConnectorNationalBackendClientImpl implements ECodexConnectorNationalBackendClient {

    @Override
    public void deliverMessage(Message message) throws ECodexConnectorNationalBackendClientException,
            ImplementationMissingException {
        throw new ImplementationMissingException("ECodexConnectorNationalBackendClient", "deliverMessage");

    }

    @Override
    public String[] requestMessagesUnsent() throws ECodexConnectorNationalBackendClientException,
            ImplementationMissingException {
        throw new ImplementationMissingException("ECodexConnectorNationalBackendClient", "requestMessagesUnsent");
    }

    @Override
    public void requestMessage(Message message) throws ECodexConnectorNationalBackendClientException,
            ImplementationMissingException {
        throw new ImplementationMissingException("ECodexConnectorNationalBackendClient", "requestMessage");
    }

    @Override
    public MessageConfirmation[] requestConfirmations() throws ECodexConnectorNationalBackendClientException,
            ImplementationMissingException {
        throw new ImplementationMissingException("ECodexConnectorNationalBackendClient", "requestConfirmations");
    }

    @Override
    public void deliverLastEvidenceForMessage(MessageConfirmation confirmation)
            throws ECodexConnectorNationalBackendClientException, ImplementationMissingException {
        throw new ImplementationMissingException("ECodexConnectorNationalBackendClient",
                "deliverLastEvidenceForMessage");
    }

}
