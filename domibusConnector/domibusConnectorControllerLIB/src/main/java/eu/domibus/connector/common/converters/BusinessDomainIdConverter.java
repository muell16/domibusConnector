package eu.domibus.connector.common.converters;

import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;
import org.springframework.core.convert.converter.Converter;

public class BusinessDomainIdConverter implements Converter<String, DomibusConnectorMessageLane.MessageLaneId> {

    @Override
    public DomibusConnectorMessageLane.MessageLaneId convert(String source) {
        if (source == null) {
            return null;
        }
        return new DomibusConnectorMessageLane.MessageLaneId(source);
    }

}
