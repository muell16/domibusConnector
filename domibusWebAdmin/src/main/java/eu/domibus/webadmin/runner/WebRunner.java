package eu.domibus.webadmin.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
import java.util.Properties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;


@SpringBootApplication
@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackageClasses= {
		LoginBean.class, 	
		WebAdminProperties.class, 
		DomibusWebAdminUserDao.class, 
		ConnectorPModeSupportImpl.class, 
		ConnectorSummaryServiceImpl.class,
		ConnectorMonitoringService.class
		})
@Import({JpaContext.class, SecurityConfig.class, DomibusWebAdminContext.class})
@ImportResource("classpath:/spring/context/connectorDaoContext.xml")
//@PropertySources({
//        @PropertySource(value = "classpath:application.properties"),
//        @PropertySource(value = "file:${CATALINA_HOME}/conf/webadmin/webadmin.properties", ignoreResourceNotFound = true)
//})
public class WebRunner extends SpringBootServletInitializer {

    private final static Logger LOG = LoggerFactory.getLogger(WebRunner.class);
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    	LOG.info("configure: run as war on appserver....");
        Properties props = new Properties();
//        
//        Object profile = System.getProperties().getOrDefault("spring.profiles.active", "default");
//        LOG.info("default profile active is [{}]", profile);
//        
//        String catalinaHome = System.getProperty("CATALINA_HOME");
//        LOG.info("Catalina Home is [{}]", catalinaHome);
//         
//        Object defaultProfileLoc = props.getOrDefault("spring.config.location", "${CATALINA_HOME}/conf/webadmin/");
//        LOG.info("default profile location is [{}]", defaultProfileLoc);
//        
//        //if deployed as war set search location for config to ${CATALINA_HOME}/conf/webadmin/webadmin.properties
          props.put("spring.config.location", "${CATALINA_HOME}/conf/webadmin/webadmin.properties");
//        props.put("spring.profiles.active", profile);
//        //props.put("spring.config.name", "webadmin");
//        //props.put("banner.location", "classpath:/ascii/ecodexlogo.txt");
//                 
    	application.properties(props);
        
    	return application.sources(WebRunner.class, DomibusWebAdminContext.class);
    }

    public static void main(String[] args) {
        LOG.trace("main: run Spring application as jar....");
        SpringApplication.run(WebRunner.class, args);
    }
	
	//TODO: add auth filter; file-upload-filter; ...
	


}
