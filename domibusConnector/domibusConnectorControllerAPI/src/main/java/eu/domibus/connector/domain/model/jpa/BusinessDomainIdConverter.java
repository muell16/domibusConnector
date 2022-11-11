package eu.domibus.connector.domain.model.jpa;

import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

//@Converter(autoApply = true)
public class BusinessDomainIdConverter implements AttributeConverter<DomibusConnectorBusinessDomain.BusinessDomainId, String> {

    @Override
    public String convertToDatabaseColumn(DomibusConnectorBusinessDomain.BusinessDomainId attribute) {
        return attribute.getMessageLaneId();
    }

    @Override
    public DomibusConnectorBusinessDomain.BusinessDomainId convertToEntityAttribute(String dbData) {
        return new DomibusConnectorBusinessDomain.BusinessDomainId(dbData);
    }
}
