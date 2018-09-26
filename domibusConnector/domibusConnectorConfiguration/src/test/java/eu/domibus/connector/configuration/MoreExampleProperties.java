package eu.domibus.connector.configuration;

import eu.domibus.connector.configuration.annotation.ConfigurationLabel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@ConfigurationProperties(prefix="connector.abc")
public class MoreExampleProperties {

    private String address;

    @ConfigurationLabel("application-test.properties path")
    private Path path;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
