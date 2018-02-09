
package eu.domibus.connector.security.spring;

import eu.ecodex.dss.model.token.AdvancedSystemType;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
public class AdvancedSystemTypeConverterTest {

    AdvancedSystemTypeConverter converter = new AdvancedSystemTypeConverter();

    @Test
    public void testConvert_withAuthenticationBasedType() {
        String type = "AUTHENTICATION_BASED";
        AdvancedSystemType convert = converter.convert(type);
        assertThat(convert).isEqualTo(AdvancedSystemType.AUTHENTICATION_BASED);
    }
    
    @Test(expected=Exception.class)
    public void testConvert_withIllegalValue_shouldThrowException() {
        converter.convert("dfjadölas");
    }
    
    @Test
    public void testConvert_wihNull_shouldReturnNull() {             
        AdvancedSystemType convert = converter.convert(null);
        assertThat(convert).isNull();
    }

}