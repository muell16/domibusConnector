package eu.domibus.connector.persistence.model.converter;

import eu.ecodex.dc5.message.model.DomibusConnectorParty.PartyRoleType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PartyRoleTypeJpaConverter implements AttributeConverter<PartyRoleType, String> {


    @Override
    public String convertToDatabaseColumn(PartyRoleType attribute) {
        return attribute.getDbName();
    }


    @Override
    public PartyRoleType convertToEntityAttribute(String dbData) {
        return PartyRoleType.ofDbName(dbData);
    }


}
