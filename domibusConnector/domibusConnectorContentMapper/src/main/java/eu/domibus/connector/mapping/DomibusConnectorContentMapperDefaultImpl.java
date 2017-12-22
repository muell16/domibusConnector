package eu.domibus.connector.mapping;

import eu.domibus.connector.common.exception.ImplementationMissingException;
import eu.domibus.connector.domain.Message;
import eu.domibus.connector.mapping.exception.DomibusConnectorContentMapperException;

public class DomibusConnectorContentMapperDefaultImpl implements DomibusConnectorContentMapper {

    @Override
    public void mapInternationalToNational(Message message) throws DomibusConnectorContentMapperException,
            ImplementationMissingException {
        throw new ImplementationMissingException("DomibusConnectorContentMapper", "mapInternationalToNational");

    }

    @Override
    public void mapNationalToInternational(Message message) throws DomibusConnectorContentMapperException,
            ImplementationMissingException {
        throw new ImplementationMissingException("DomibusConnectorContentMapper", "mapNationalToInternational");

    }

}
