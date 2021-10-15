package eu.domibus.connector.common.service;

import eu.domibus.connector.common.configuration.ConnectorConfigurationProperties;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        "debug=true"
})
@EnableConfigurationProperties(ConnectorConfigurationProperties.class)
class BeanToPropertyMapConverterTest {

    private static final Logger LOGGER = LogManager.getLogger(BeanToPropertyMapConverterTest.class);

    @Autowired
    BeanToPropertyMapConverter beanToPropertyMapConverter;

    @MockBean
    DCBusinessDomainManagerImpl dcBusinessDomainManagerImpl;

    @Test
    void readBeanPropertiesToMap() {

        MyTestProperties myTestProperties = new MyTestProperties();
        myTestProperties.setProp1("prop1");
        myTestProperties.setProp2(23);
        myTestProperties.getNested().setAbc("abc");
        myTestProperties.getNested().setDuration(Duration.ofDays(23));
        myTestProperties.getNested().setaVeryLongPropertyName("propLong");

        MyTestProperties.NestedProp n1 = new MyTestProperties.NestedProp();
        n1.setAbc("abc");
        n1.setaVeryLongPropertyName("verylongprop");

        myTestProperties.getNestedPropList().add(n1);

        Map<String, String> propertyMap = beanToPropertyMapConverter.readBeanPropertiesToMap(myTestProperties, "test.example");

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("test.example.prop1", "prop1");
        expectedMap.put("test.example.prop2", "23");
        expectedMap.put("test.example.nested.abc", "abc");
        expectedMap.put("test.example.nested.duration", "PT552H");
        expectedMap.put("test.example.nested.a-very-long-property-name", "propLong");
        expectedMap.put("test.example.nested-prop-list[0].a-very-long-property-name", "verylongprop");
        expectedMap.put("test.example.nested-prop-list[0].abc", "abc");

        assertThat(propertyMap).containsExactlyEntriesOf(expectedMap);

        LOGGER.info("Mapped properties are: [{}]", propertyMap);
    }

}