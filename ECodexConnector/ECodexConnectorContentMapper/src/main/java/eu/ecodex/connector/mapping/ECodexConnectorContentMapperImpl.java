package eu.ecodex.connector.mapping;

import eu.ecodex.connector.mapping.exception.ECodexConnectorContentMapperException;

public abstract class ECodexConnectorContentMapperImpl implements ECodexConnectorContentMapper {

    @Override
    public byte[] mapInternationalToNational(byte[] internationalContent) throws ECodexConnectorContentMapperException {
        throw new ECodexConnectorContentMapperException("Method must be overridden when used by configuration!");
    }

    @Override
    public byte[] mapNationalToInternational(byte[] nationalContent) throws ECodexConnectorContentMapperException {
        throw new ECodexConnectorContentMapperException("Method must be overridden when used by configuration!");
    }

}
