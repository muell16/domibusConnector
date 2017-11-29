package eu.domibus.webadmin.runner;

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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;


/**
 * "Main" class / context
 * extends SpringBootServletInitializer to initalize the spring context
 *  
 *  for further doc see {@link SpringBootServletInitializer}
 *  
 * @author spindlest
 *
 */
@EnableAutoConfiguration
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
@Import({JpaContext.class, SecurityConfig.class, DomibusWebAdminContext.class})
@ImportResource({"classpath:/spring/context/webadmin/connectorDaoContext.xml", //load daos from connector
    "classpath:/spring/context/webadmin/dataSource.xml" //create datasource context
})
public class WebRunner extends SpringBootServletInitializer {

    private final static Logger LOG = LoggerFactory.getLogger(WebRunner.class);
	
    /**
     * configures the Spring application by adding this class as source
     * also sets the spring.config.location 
     * to ${catalina.home}/conf/webadmin/webadmin.properties
     * 
     *  
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    	LOG.info("configure: run as war on appserver....");
        Properties props = new Properties();
        //if deployed as war set search location for config to ${CATALINA_HOME}/conf/webadmin/webadmin.properties
        props.put("spring.config.location", "${catalina.home}/conf/webadmin/webadmin.properties");                 
    	application.properties(props);        
    	return application.sources(WebRunner.class);
    }


}
