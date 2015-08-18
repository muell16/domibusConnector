package eu.ecodex.connector.nbc;

import eu.domibus.connector.common.NationalImplementationFactory;
import eu.ecodex.connector.nbc.exception.ECodexConnectorNationalBackendClientException;

public class ECodexConnectorNationalBackendClientFactory extends NationalImplementationFactory {

    public ECodexConnectorNationalBackendClient createECodexConnectorNationalBackendClient()
            throws ECodexConnectorNationalBackendClientException {

        try {
            ECodexConnectorNationalBackendClient client = (ECodexConnectorNationalBackendClient) super
                    .createNationalImplementation();
            return client;
        } catch (Exception e) {
            throw new ECodexConnectorNationalBackendClientException(
                    "Could not create an instance for ECodexContentMapper implementation with full qualified class name "
                            + implementationClassName, e);
        }
    }

}
