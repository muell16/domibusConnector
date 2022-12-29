package eu.domibus.connector.common.converters;

import eu.domibus.connector.domain.model.DC5BusinessDomain;
import org.springframework.core.convert.converter.Converter;

public class BusinessDomainIdConverter implements Converter<String, DC5BusinessDomain.BusinessDomainId> {

    @Override
    public DC5BusinessDomain.BusinessDomainId convert(String source) {
        if (source == null) {
            return null;
        }
        return new DC5BusinessDomain.BusinessDomainId(source);
    }

}
