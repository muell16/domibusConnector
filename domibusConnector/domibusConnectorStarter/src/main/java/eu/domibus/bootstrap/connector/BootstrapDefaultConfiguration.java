package eu.domibus.bootstrap.connector;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
@PropertySource("classpath:/default-bootstrap.properties")
public class BootstrapDefaultConfiguration {


}
