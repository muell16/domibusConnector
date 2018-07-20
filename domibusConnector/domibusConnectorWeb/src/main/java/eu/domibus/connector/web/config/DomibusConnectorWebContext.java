package eu.domibus.connector.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.vaadin.flow.spring.annotation.EnableVaadin;

@Configuration
@EnableWebMvc
@EnableVaadin
@ComponentScan("eu.domibus.connector")
public class DomibusConnectorWebContext {



}
