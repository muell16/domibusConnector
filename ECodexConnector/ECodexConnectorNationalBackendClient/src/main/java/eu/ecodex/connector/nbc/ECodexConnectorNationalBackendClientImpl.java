package eu.ecodex.connector.nbc;

import eu.ecodex.connector.common.EncryptedDocumentPackage;
import eu.ecodex.connector.common.MessageState;
import eu.ecodex.connector.common.exception.ImplementationMissingException;
import eu.ecodex.connector.nbc.exception.ECodexConnectorNationalBackendClientException;

public class ECodexConnectorNationalBackendClientImpl implements ECodexConnectorNationalBackendClient {

    @Override
    public MessageState deliverMessage(EncryptedDocumentPackage documentPackage)
            throws ECodexConnectorNationalBackendClientException, ImplementationMissingException {
        throw new ImplementationMissingException("ECodexConnectorNationalBackendClient", "deliverMessage");
    }

    @Override
    public MessageState requestState(String messageId) throws ECodexConnectorNationalBackendClientException,
            ImplementationMissingException {
        throw new ImplementationMissingException("ECodexConnectorNationalBackendClient", "requestState");
    }

    @Override
    public EncryptedDocumentPackage requestMessage(String messageId)
            throws ECodexConnectorNationalBackendClientException, ImplementationMissingException {
        throw new ImplementationMissingException("ECodexConnectorNationalBackendClient", "requestMessage");
    }

    @Override
    public EncryptedDocumentPackage[] requestAllPendingMessages() throws ECodexConnectorNationalBackendClientException,
            ImplementationMissingException {
        throw new ImplementationMissingException("ECodexConnectorNationalBackendClient", "requestAllPendingMessages");
    }

}
