package eu.domibus.connector.common.service;

import eu.domibus.connector.common.annotations.BusinessDomainScoped;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@BusinessDomainScoped
@ConfigurationProperties(prefix = "test.example2")
public class MyTestProperties2 {

    private String prop1;

    private Integer prop2;

    @NestedConfigurationProperty
    private NestedProp nested = new NestedProp();

    public static class NestedProp {
        private String abc;
        private Duration duration;
        private String aVeryLongPropertyName;

        public String getAbc() {
            return abc;
        }

        public void setAbc(String abc) {
            this.abc = abc;
        }

        public Duration getDuration() {
            return duration;
        }

        public void setDuration(Duration duration) {
            this.duration = duration;
        }

        public String getaVeryLongPropertyName() {
            return aVeryLongPropertyName;
        }

        public void setaVeryLongPropertyName(String aVeryLongPropertyName) {
            this.aVeryLongPropertyName = aVeryLongPropertyName;
        }
    }

    public String getProp1() {
        return prop1;
    }

    public void setProp1(String prop1) {
        this.prop1 = prop1;
    }

    public Integer getProp2() {
        return prop2;
    }

    public void setProp2(Integer prop2) {
        this.prop2 = prop2;
    }

    public NestedProp getNested() {
        return nested;
    }

    public void setNested(NestedProp nested) {
        this.nested = nested;
    }

    public String getProp1AsAnotherString() {
        return prop1;
    }
}