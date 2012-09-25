package eu.ecodex.connector.mapping;

import eu.ecodex.connector.common.exception.ImplementationMissingException;
import eu.ecodex.connector.mapping.exception.ECodexConnectorContentMapperException;

public class ECodexConnectorContentMapperImpl implements ECodexConnectorContentMapper {

    @Override
    public byte[] mapInternationalToNational(byte[] internationalContent) throws ECodexConnectorContentMapperException,
            ImplementationMissingException {
        throw new ImplementationMissingException("ECodexConnectorContentMapper", "mapInternationalToNational");
    }

    @Override
    public byte[] mapNationalToInternational(byte[] nationalContent) throws ECodexConnectorContentMapperException,
            ImplementationMissingException {
        throw new ImplementationMissingException("ECodexConnectorContentMapper", "mapNationalToInternational");
    }

}
