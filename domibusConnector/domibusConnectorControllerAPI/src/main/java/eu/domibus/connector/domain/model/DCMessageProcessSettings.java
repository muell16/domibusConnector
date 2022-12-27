package eu.domibus.connector.domain.model;

import eu.domibus.connector.domain.enums.AdvancedElectronicSystemType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class DCMessageProcessSettings {

    public static final String VALIDATION_SERVICE_NAME_PROPERTY_NAME = "connector.business-document-sent.validation-service-name";

    private AdvancedElectronicSystemType validationServiceName = null;


    public static DCMessageProcessSettings of(Map<String, String> properties) {
        DCMessageProcessSettings settings = new DCMessageProcessSettings();
        if (properties.containsKey(VALIDATION_SERVICE_NAME_PROPERTY_NAME)) {
            AdvancedElectronicSystemType advancedElectronicSystemType = AdvancedElectronicSystemType.valueOf(properties.get(VALIDATION_SERVICE_NAME_PROPERTY_NAME));
            settings.setValidationServiceName(advancedElectronicSystemType);
        }
        return settings;
    }

    public Map<String, String> toProperties() {
        Map<String, String> properties = new HashMap<>();
        if (validationServiceName != null) {
            properties.put(VALIDATION_SERVICE_NAME_PROPERTY_NAME, validationServiceName.name());
        }
        return properties;
    }

}
