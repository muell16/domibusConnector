package eu.domibus.connector.mapping;

import eu.domibus.connector.common.CommonConnectorProperties;
import eu.domibus.connector.common.NationalImplementationFactory;
import eu.domibus.connector.mapping.exception.DomibusConnectorContentMapperException;

public class DomibusConnectorContentMapperFactory extends NationalImplementationFactory {

    CommonConnectorProperties connectorProperties;

    public void setConnectorProperties(CommonConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

    public DomibusConnectorContentMapper createDomibusConnectorContentMapper()
            throws DomibusConnectorContentMapperException {

        if (connectorProperties.isUseContentMapper()) {

            try {
                DomibusConnectorContentMapper mapper = (DomibusConnectorContentMapper) super
                        .createNationalImplementation();
                return mapper;
            } catch (Exception e) {
                throw new DomibusConnectorContentMapperException(
                        "Could not create an instance for DomibusContentMapper implementation with full qualified class name "
                                + implementationClassName, e);
            }
        }

        return new DomibusConnectorContentMapperImpl();
    }
}
