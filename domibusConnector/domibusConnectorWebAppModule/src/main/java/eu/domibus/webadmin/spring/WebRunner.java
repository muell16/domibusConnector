package eu.domibus.webadmin.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
public class WebRunner extends SpringBootServletInitializer implements WebApplicationInitializer {

    private final static Logger LOG = LoggerFactory.getLogger(WebRunner.class);

    public void onStartup(ServletContext servletContext) throws ServletException {
        String configLocation = servletContext.getInitParameter("config.location");
        if (configLocation != null) {
            servletContext.setInitParameter("spring.config.location", configLocation);
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
