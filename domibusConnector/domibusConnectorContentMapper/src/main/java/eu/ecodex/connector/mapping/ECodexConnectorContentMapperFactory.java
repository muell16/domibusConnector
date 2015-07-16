package eu.ecodex.connector.mapping;

import eu.domibus.connector.common.CommonConnectorProperties;
import eu.domibus.connector.common.NationalImplementationFactory;
import eu.ecodex.connector.mapping.exception.ECodexConnectorContentMapperException;

public class ECodexConnectorContentMapperFactory extends NationalImplementationFactory {

    CommonConnectorProperties connectorProperties;

    public void setConnectorProperties(CommonConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

    public ECodexConnectorContentMapper createECodexConnectorContentMapper()
            throws ECodexConnectorContentMapperException {

        if (connectorProperties.isUseContentMapper()) {

            try {
                ECodexConnectorContentMapper mapper = (ECodexConnectorContentMapper) super
                        .createNationalImplementation();
                return mapper;
            } catch (Exception e) {
                throw new ECodexConnectorContentMapperException(
                        "Could not create an instance for ECodexContentMapper implementation with full qualified class name "
                                + implementationClassName, e);
            }
        }

        return new ECodexConnectorContentMapperImpl();
    }
}
