
package eu.domibus.connector.security.spring;

import eu.ecodex.dss.model.token.AdvancedSystemType;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
//@Component
//@ConfigurationPropertiesBinding
public class AdvancedSystemTypeConverter implements Converter<String, AdvancedSystemType> {

    @Override
    public AdvancedSystemType convert(String source) {
        if (source == null) {
            return null;
        }
        return AdvancedSystemType.valueOf(source);
    }

}
