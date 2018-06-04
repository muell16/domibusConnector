package eu.domibus.webadmin.spring;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;


/**
 * "Main" class / context
 * extends SpringBootServletInitializer to initalize the spring context
 *  
 *  for further doc see {@link SpringBootServletInitializer}
 *  
 * 
 * @author spindlest
 *
 */
@SpringBootApplication(scanBasePackages = "eu.domibus")
@EnableTransactionManagement
@EnableAutoConfiguration
@PropertySource("classpath:/default.properties")
public class WebRunner extends SpringBootServletInitializer implements WebApplicationInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(WebRunner.class);

    private static final String CONNECTOR_CONFIG_LOCATION_PROPERTY_NAME = "connector.config.file";
    private static final String CONNECTOR_LOGGING_CONFIG_LOCATION_PROPERTY_NAME = "connector.logging.config";

    private ServletContext servletContext;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        this.servletContext = servletContext;

        //copy connector.logging.config from systemProperty, then from servletContext into logging.config of servletContext
        setFromSystemPropertyIfNotNull(CONNECTOR_LOGGING_CONFIG_LOCATION_PROPERTY_NAME, "logging.config");
        setFromServletContextIfNotNull(CONNECTOR_LOGGING_CONFIG_LOCATION_PROPERTY_NAME, "logging.config");

        //copy connector.config.file from systemProperty, then from servletContext into spring.config.location of servletContext
        setFromSystemPropertyIfNotNull(CONNECTOR_CONFIG_LOCATION_PROPERTY_NAME, "spring.config.location");
        setFromServletContextIfNotNull(CONNECTOR_CONFIG_LOCATION_PROPERTY_NAME, "spring.config.location");

        super.onStartup(servletContext);
    }

    private void setFromSystemPropertyIfNotNull(String name, String setPropertyName) {
        String value = System.getProperty(name);
        LOG.info("Config name from SystemProperty is [{}] value is [{}]", name, value);
        if (value != null) {
            LOG.info("Setting servletInitParam [{}] to value [{}]", setPropertyName, value);
            servletContext.setInitParameter(setPropertyName, value);
        }
    }

    private void setFromServletContextIfNotNull(String name, String setPropertyName) {
        String value = servletContext.getInitParameter(name);
        LOG.info("Config name from servletContext is [{}] value is [{}]", name, value);
        if (value != null) {
            LOG.info("Setting servletInitParam [{}] to value [{}]", setPropertyName, value);
            servletContext.setInitParameter(setPropertyName, value);
        }
    }

    /**
     *  just adds WebRunner.class to the Spring Application sources
     * 
     *  {@inheritDoc }
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.properties("banner.location=classpath:/ascii/ecodex.txt", "spring.output.ansi.enabled=DETECT");
    	return application.sources(WebRunner.class);
    }
    
    public static void main(String[] args) {
        SpringApplication.run(WebRunner.class, args);
    }

}
