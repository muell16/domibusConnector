package eu.ecodex.dc5.message.model;

import javax.persistence.AttributeConverter;

public class EbmsMessageIdConverter implements AttributeConverter<EbmsMessageId, String> {

    @Override
    public String convertToDatabaseColumn(EbmsMessageId attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getEbmsMesssageId();
    }

    @Override
    public EbmsMessageId convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return EbmsMessageId.ofString(dbData);
    }
}
