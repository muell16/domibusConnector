package eu.domibus.connector.common.converters;

import eu.domibus.connector.domain.configuration.EvidenceActionServiceConfigurationProperties;
import eu.domibus.connector.domain.model.DomibusConnectorAction;
import org.springframework.core.convert.converter.Converter;

public class EvidenceActionConverter implements Converter<String, EvidenceActionServiceConfigurationProperties.EvidenceAction> {

    @Override
    public EvidenceActionServiceConfigurationProperties.EvidenceAction convert(String source) {
        if (source == null) {
            return null;
        }
        return new EvidenceActionServiceConfigurationProperties.EvidenceAction(source);
    }

}
