package eu.domibus.connector.domain.model.jpa;

import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;

import javax.annotation.Nullable;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;


public class BusinessDomainIdConverter implements AttributeConverter<DomibusConnectorBusinessDomain.BusinessDomainId, String> {

    @Override
    @Nullable
    public String convertToDatabaseColumn(DomibusConnectorBusinessDomain.BusinessDomainId attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getMessageLaneId();
    }

    @Override
    @Nullable
    public DomibusConnectorBusinessDomain.BusinessDomainId convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return new DomibusConnectorBusinessDomain.BusinessDomainId(dbData);
    }
}
