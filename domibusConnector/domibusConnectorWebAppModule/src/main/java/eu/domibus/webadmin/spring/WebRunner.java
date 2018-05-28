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

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        //"connector.logging.config"
        String loggingConfigFile = System.getProperty("connector.logging.config");
        if (loggingConfigFile != null) {
            System.setProperty("log4j.configurationFile", loggingConfigFile);
            PropertyConfigurator.configure(loggingConfigFile);
        }
        loggingConfigFile = servletContext.getInitParameter("connector.logging.config");
        if (loggingConfigFile != null) {
            System.setProperty("log4j.configurationFile", loggingConfigFile);
            PropertyConfigurator.configure(loggingConfigFile);
        }


        String envParamConfigLocation = System.getProperty(CONNECTOR_CONFIG_LOCATION_PROPERTY_NAME);
        LOG.info("CONFIG LOCATION by ENV is: " + envParamConfigLocation);
        if (envParamConfigLocation != null) {
            servletContext.setInitParameter("spring.config.location", envParamConfigLocation);
        }

        String servletParamConfigLocation = servletContext.getInitParameter(CONNECTOR_CONFIG_LOCATION_PROPERTY_NAME);
        LOG.info("CONFIG LOCATION by servletContext IS : " + servletParamConfigLocation);
        if (servletParamConfigLocation != null) {
            servletContext.setInitParameter("spring.config.location", servletParamConfigLocation);
        }
        super.onStartup(servletContext);
    }

    /**
     *  just adds WebRunner.class to the Spring Application sources
     * 
     *  {@inheritDoc }
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    	return application.sources(WebRunner.class);
    }
    
    public static void main(String[] args) {
        SpringApplication.run(WebRunner.class, args);
    }

}
