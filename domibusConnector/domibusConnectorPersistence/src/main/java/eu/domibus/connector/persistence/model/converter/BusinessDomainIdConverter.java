package eu.domibus.connector.persistence.model.converter;

import eu.domibus.connector.domain.model.DC5BusinessDomain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class BusinessDomainIdConverter implements AttributeConverter<DC5BusinessDomain.BusinessDomainId, String> {

    @Override
    public String convertToDatabaseColumn(DC5BusinessDomain.BusinessDomainId attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getBusinessDomainId();
    }

    @Override
    public DC5BusinessDomain.BusinessDomainId convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return new DC5BusinessDomain.BusinessDomainId(dbData);
    }
}
