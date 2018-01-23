package eu.domibus.webadmin.runner;

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
@EnableAutoConfiguration(exclude={ //activates pre defined configurations provided by spring 
//    LiquibaseAutoConfiguration.class
})
//@ImportResource({        
////    "classpath:/spring/context/domibusconnector/DomibusConnectorCommonContext.xml",
//    "classpath:/spring/context/DomibusConnectorSecurityToolkitContext.xml",
//	"classpath:/spring/context/DomibusConnectorContentMapperContext.xml",
//	"classpath:/spring/context/DomibusConnectorNationalBackendClientContext.xml",
//	"classpath:/spring/context/DomibusConnectorMonitoringContext.xml",
//	"classpath:/spring/context/quartz-context.xml"
//})
public class WebRunner extends SpringBootServletInitializer implements WebApplicationInitializer {

    private final static Logger LOG = LoggerFactory.getLogger(WebRunner.class);
	
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
