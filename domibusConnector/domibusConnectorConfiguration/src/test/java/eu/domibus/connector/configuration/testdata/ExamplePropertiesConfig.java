package eu.domibus.connector.configuration.testdata;

import com.sun.istack.internal.NotNull;
import eu.domibus.connector.configuration.annotation.ConfigurationDescription;
import eu.domibus.connector.configuration.annotation.ConfigurationLabel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;

@Component
@ConfigurationProperties(prefix="connector.configuration")
public class ExamplePropertiesConfig {

    @NotNull
    @ConfigurationLabel("A text")
    private String text;

    @NotNull
    @ConfigurationDescription("A number.........")
    @Max(60)
    private Integer number;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
