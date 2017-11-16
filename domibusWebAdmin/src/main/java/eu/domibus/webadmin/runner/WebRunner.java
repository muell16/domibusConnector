package eu.domibus.webadmin.runner;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.webapp.FacesServlet;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;

import org.primefaces.component.log.Log;
import org.primefaces.webapp.filter.FileUploadFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.jsf.el.SpringBeanFacesELResolver;

import com.sun.faces.config.ConfigureListener;
import com.sun.faces.config.FacesInitializer;

import eu.domibus.webadmin.blogic.connector.monitoring.impl.ConnectorMonitoringService;
import eu.domibus.webadmin.blogic.connector.pmode.impl.ConnectorPModeSupportImpl;
import eu.domibus.webadmin.blogic.connector.statistics.impl.ConnectorSummaryServiceImpl;
import eu.domibus.webadmin.commons.WebAdminProperties;
import eu.domibus.webadmin.dao.impl.DomibusWebAdminUserDao;
import eu.domibus.webadmin.jsf.AuthFilter;
import eu.domibus.webadmin.jsf.LoginBean;
import eu.domibus.webadmin.runner.springsupport.ViewScope;


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
@Import({JpaContext.class, SecurityConfig.class})
@ImportResource("classpath:/spring/context/connectorDaoContext.xml")
public class WebRunner extends SpringBootServletInitializer implements ServletContextInitializer {

	private final static Logger LOG = LoggerFactory.getLogger(WebRunner.class);
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    	LOG.trace("configure: run as war on appserver....");
    	System.err.print("WebRunner runs as war....");
//    	throw new RuntimeException("STOPPPPPPPP!");
        // is not executed when running in container!!! so whole spring context not working when deployed as war!
    	
    	
    	return application.sources(WebRunner.class);
    }

	public static void main(String[] args) {
		LOG.trace("main: run Spring application as jar....");
		SpringApplication.run(WebRunner.class, args);
	}
	
	//TODO: add auth filter; file-upload-filter; ...
	
	
	
	/*
	 * configure servletRegistration to listen
	 */
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
    	LOG.trace("servletRegistrationBean: called");
        FacesServlet servlet = new FacesServlet();
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(servlet, "*.xhtml", "*.jsf");
        servletRegistrationBean.setLoadOnStartup(1);
        servletRegistrationBean.addUrlMappings("/*");
		return servletRegistrationBean;
    }
    
    /*
     * configure PrimeFaces File Upload filter
     */
    @Bean
    public FilterRegistrationBean uploadFilter() {
    	    	
    	FileUploadFilter filter = new FileUploadFilter();
    	
    	FilterRegistrationBean filterRegistration = new FilterRegistrationBean(filter, servletRegistrationBean());    	
		return filterRegistration;
    	
    }
    
    
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        servletContext.setInitParameter("javax.faces.DEFAULT_SUFFIX", ".xhtml");
        servletContext.setInitParameter("javax.faces.PARTIAL_STATE_SAVING_METHOD", "true");

        servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "Development");
        servletContext.setInitParameter("facelets.DEVELOPMENT", "true");
        servletContext.setInitParameter("javax.faces.FACELETS_REFRESH_PERIOD", "1");

        servletContext.setInitParameter("primefaces.CLIENT_SIDE_VALIDATION", "true");
		servletContext.setInitParameter("primefaces.THEME", "bootstrap");
		servletContext.setInitParameter("primefaces.FONT_AWESOME", "true");
		servletContext.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", "true");
		servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "Development");
//		servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", "true");		
//		servletContext.setInitParameter("com.sun.faces.expressionFactory", "com.sun.el.ExpressionFactoryImpl");
        
        
        Set<Class<?>> clazz = new HashSet<Class<?>>();

        clazz.add(WebRunner.class); // dummy, enables InitFacesContext

        FacesInitializer facesInitializer = new FacesInitializer();
                        
        facesInitializer.onStartup(clazz, servletContext);

    }
    

    @Bean
    public ServletListenerRegistrationBean<JsfApplicationObjectConfigureListener> jsfConfigureListener() {
    	System.out.println("jsfConfigureListener called!");
    	LOG.debug("jsfConfigureListener: called");
        return new ServletListenerRegistrationBean<JsfApplicationObjectConfigureListener>(
                new JsfApplicationObjectConfigureListener());
    }


    static class JsfApplicationObjectConfigureListener extends ConfigureListener {

        @Override
        public void contextInitialized(ServletContextEvent sce) {
            super.contextInitialized(sce);

            ApplicationFactory factory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
            Application app = factory.getApplication();
            
            app.addELResolver(new SpringBeanFacesELResolver());
            
        }
    }
    

    
    /*
     * 
     *  view scope
     */
    @Bean
    public ViewScope viewScope() {
    	return new ViewScope();
    }
    
    /*
     *  configure custom view scope
     */
    @Bean
    public CustomScopeConfigurer customScopeConfigurer() {
    	CustomScopeConfigurer scopeConfig = new CustomScopeConfigurer();
    	scopeConfig.addScope("view", viewScope());
    	return scopeConfig;
    }
    
  

}
