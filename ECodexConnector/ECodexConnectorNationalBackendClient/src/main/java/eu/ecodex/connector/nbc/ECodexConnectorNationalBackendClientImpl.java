package eu.ecodex.connector.nbc;

import eu.ecodex.connector.common.EncryptedDocumentPackage;
import eu.ecodex.connector.common.MessageState;
import eu.ecodex.connector.nbc.exception.ECodexConnectorNationalBackendClientException;

public abstract class ECodexConnectorNationalBackendClientImpl implements ECodexConnectorNationalBackendClient {

    @Override
    public MessageState deliverMessage(EncryptedDocumentPackage documentPackage)
            throws ECodexConnectorNationalBackendClientException {
        throw new ECodexConnectorNationalBackendClientException("Method must be overridden by national implementation!");
    }

    @Override
    public MessageState requestState(String messageId) throws ECodexConnectorNationalBackendClientException {
        throw new ECodexConnectorNationalBackendClientException("Method must be overridden by national implementation!");
    }

    @Override
    public String[] checkPendingMessages() throws ECodexConnectorNationalBackendClientException {
        throw new ECodexConnectorNationalBackendClientException("Method must be overridden by national implementation!");
    }

    @Override
    public EncryptedDocumentPackage requestMessage(String messageId)
            throws ECodexConnectorNationalBackendClientException {
        throw new ECodexConnectorNationalBackendClientException("Method must be overridden by national implementation!");
    }

}
