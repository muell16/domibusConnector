package eu.domibus.connector.common.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class ResourceToStringConverter implements Converter<Resource, String> {

    @Override
    public String convert(Resource source) {
        try {

            return source.getURL().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
