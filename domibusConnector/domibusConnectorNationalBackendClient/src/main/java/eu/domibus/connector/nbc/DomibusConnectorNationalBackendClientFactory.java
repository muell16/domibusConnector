package eu.domibus.connector.nbc;

import eu.domibus.connector.common.NationalImplementationFactory;
import eu.domibus.connector.nbc.exception.DomibusConnectorNationalBackendClientException;

public class DomibusConnectorNationalBackendClientFactory extends NationalImplementationFactory {

    public DomibusConnectorNationalBackendClient createDomibusConnectorNationalBackendClient()
            throws DomibusConnectorNationalBackendClientException {

        try {
            DomibusConnectorNationalBackendClient client = (DomibusConnectorNationalBackendClient) super
                    .createNationalImplementation();
            return client;
        } catch (Exception e) {
            throw new DomibusConnectorNationalBackendClientException(
                    "Could not create an instance for DomibusContentMapper implementation with full qualified class name "
                            + implementationClassName, e);
        }
    }

}
