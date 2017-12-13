package eu.domibus.webadmin.runner;

import eu.domibus.connector.gwc.spring.DomibusConnectorGatewayWebserviceClientContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import eu.domibus.webadmin.blogic.connector.monitoring.impl.ConnectorMonitoringService;
import eu.domibus.webadmin.blogic.connector.pmode.impl.ConnectorPModeSupportImpl;
import eu.domibus.webadmin.blogic.connector.statistics.impl.ConnectorSummaryServiceImpl;
import eu.domibus.webadmin.commons.WebAdminProperties;
import eu.domibus.webadmin.dao.impl.DomibusWebAdminUserDao;
import eu.domibus.webadmin.jsf.LoginBean;
import eu.domibus.webadmin.runner.springsupport.DomibusWebAdminUserAuthenticationProvider;

import java.util.Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;


/**
 * "Main" class / context
 * extends SpringBootServletInitializer to initalize the spring context
 *  
 *  for further doc see {@link SpringBootServletInitializer}
 *  
 * @author spindlest
 *
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude={
//    LiquibaseAutoConfiguration.class
})
@Configuration
@ComponentScan(basePackageClasses= {
		LoginBean.class, 	
		WebAdminProperties.class, 
		DomibusWebAdminUserDao.class, 
		ConnectorPModeSupportImpl.class, 
		ConnectorSummaryServiceImpl.class,
		ConnectorMonitoringService.class,
		DomibusWebAdminUserAuthenticationProvider.class
		})
@Import({
    //DatabaseConnectionContext.class,
    JpaContext.class, 
    SecurityConfig.class, 
    DomibusWebAdminContext.class,
    DomibusConnectorGatewayWebserviceClientContext.class
})
@ImportResource({        
    "classpath:/spring/context/domibusconnector/DomibusConnectorCommonContext.xml",
    "classpath:/spring/context/DomibusConnectorSecurityToolkitContext.xml",
    "classpath:/spring/context/DomibusConnectorEvidencesToolkitContext.xml",
	"classpath:/spring/context/DomibusConnectorContentMapperContext.xml",
//	"classpath:/spring/context/DomibusConnectorGateway${gateway.routing.option}ClientContext.xml", replaced by DomibusConnectorGatewayWebserviceClientContext.class
	"classpath:/spring/context/DomibusConnectorNationalBackendClientContext.xml",
	"classpath:/spring/context/DomibusConnectorMonitoringContext.xml",
	"classpath:/spring/context/quartz-context.xml"
})
public class WebRunner extends SpringBootServletInitializer {

    private final static Logger LOG = LoggerFactory.getLogger(WebRunner.class);
	
    /**
     * configures the Spring application by adding this class as source
     * also sets the spring.config.location if not set
     * to domibuswebapp.config.location if this environment variable is set
     * otherwhise to ${catalina.home}/conf/webadmin/
     * 
     * also sets spring.config.name if not set to 
     * webadmin
     * 
     * this settings results if not anything is configured to 
     * ${catalina.home}/conf/webadmin/domibuswebapp.properties
     * 
     * 
     *  {@inheritDoc }
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    	LOG.info("configure: run as war on appserver....");
        Properties props = new Properties();
        
        props.put("debug", true);
        
        if (System.getProperty("spring.config.name") == null) {
            props.put("spring.config.name", "domibuswebapp");
        }
        
        //if deployed as war and no domibuswebapp.config.location is set 
        //set search location for config to ${CATALINA_HOME}/conf/webadmin/webadmin.properties
        if (System.getProperty("domibuswebapp.config.location") != null ) {
            props.put("spring.config.location", System.getProperty("domibuswebapp.config.location"));
        } else if (System.getProperty("spring.config.location") != null) {
            //do nothing spring.config.location is already in environment!
        } else {         
            //if not already set, set to default:
            props.put("spring.config.location", "${catalina.home}/conf/domibuswebapp/");                 
        }        
    	application.properties(props);  
    	return application.sources(WebRunner.class);
    }

    
    public static void main(String[] args) {
        SpringApplication.run(WebRunner.class, args);
    }

}
