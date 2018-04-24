package eu.domibus.connector.controller.spring;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 *  This class provides a conversion service from String to Duration
 *
 */
@Component
@ConfigurationPropertiesBinding
public class StringToMillicsecondsConverter implements Converter<String, Duration> {

    @Override
    public Duration convert(String source) {
        return Duration.valueOf(source);
    }

}
