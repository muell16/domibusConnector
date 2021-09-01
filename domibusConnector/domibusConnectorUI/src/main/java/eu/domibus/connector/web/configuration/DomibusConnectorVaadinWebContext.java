package eu.domibus.connector.web.configuration;

import javax.servlet.annotation.MultipartConfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.vaadin.flow.spring.annotation.EnableVaadin;

@Configuration
@EnableWebMvc
@EnableVaadin("eu.domibus.connector.web")
@MultipartConfig
public class DomibusConnectorVaadinWebContext {

}
