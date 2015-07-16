package eu.ecodex.connector.mapping;

import eu.ecodex.connector.common.exception.ImplementationMissingException;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.mapping.exception.ECodexConnectorContentMapperException;

public class ECodexConnectorContentMapperImpl implements ECodexConnectorContentMapper {

    @Override
    public void mapInternationalToNational(Message message) throws ECodexConnectorContentMapperException,
            ImplementationMissingException {
        throw new ImplementationMissingException("ECodexConnectorContentMapper", "mapInternationalToNational");

    }

    @Override
    public void mapNationalToInternational(Message message) throws ECodexConnectorContentMapperException,
            ImplementationMissingException {
        throw new ImplementationMissingException("ECodexConnectorContentMapper", "mapNationalToInternational");

    }

}
