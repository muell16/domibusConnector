package eu.domibus.webadmin.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan("eu.domibus")
//@Import({WebMvcConfig.class, SpringSecurityConfig.class})
public class DomibusConnectorWebContext {

	
	public DomibusConnectorWebContext() {
		// TODO Auto-generated constructor stub
	}

}
