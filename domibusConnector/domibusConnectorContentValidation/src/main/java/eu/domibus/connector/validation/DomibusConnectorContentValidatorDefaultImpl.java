package eu.domibus.connector.validation;

import eu.domibus.connector.common.exception.ImplementationMissingException;
import eu.domibus.connector.validation.exception.DomibusConnectorContentValidatorException;

public class DomibusConnectorContentValidatorDefaultImpl implements DomibusConnectorContentValidator {

    @Override
    public void validateInternationalContent(byte[] content) throws DomibusConnectorContentValidatorException,
            ImplementationMissingException {
        throw new ImplementationMissingException("DomibusConnectorContentValidator", "validateInternationalContent");

    }

    @Override
    public void validateNationalContent(byte[] content) throws DomibusConnectorContentValidatorException,
            ImplementationMissingException {
        throw new ImplementationMissingException("DomibusConnectorContentValidator", "validateNationalContent");

    }

}
