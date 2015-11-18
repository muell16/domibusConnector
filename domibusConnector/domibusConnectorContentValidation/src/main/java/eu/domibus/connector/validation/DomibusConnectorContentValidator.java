package eu.domibus.connector.validation;

import eu.domibus.connector.common.exception.ImplementationMissingException;
import eu.domibus.connector.validation.exception.DomibusConnectorContentValidatorException;

public interface DomibusConnectorContentValidator {

    public void validateInternationalContent(byte[] content) throws DomibusConnectorContentValidatorException,
            ImplementationMissingException;

    public void validateNationalContent(byte[] content) throws DomibusConnectorContentValidatorException,
            ImplementationMissingException;

}
