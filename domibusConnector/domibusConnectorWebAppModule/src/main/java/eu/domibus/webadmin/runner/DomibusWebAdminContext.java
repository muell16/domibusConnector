package eu.domibus.webadmin.runner;

import com.sun.faces.config.ConfigureListener;
import com.sun.faces.config.FacesInitializer;
import eu.domibus.webadmin.runner.springsupport.ViewScope;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import org.primefaces.webapp.filter.FileUploadFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.jsf.el.SpringBeanFacesELResolver;

/**
 * Configuration/Context for domibusWebAdmin
 *  configures faces Servlet
 *  and PrimeFaces uploadFilter
 * 
 * @author spindlest
 */
@Configuration
public class DomibusWebAdminContext implements ServletContextInitializer {
    
    private final static Logger LOG = LoggerFactory.getLogger(DomibusWebAdminContext.class);
    
    //@Value("${connector.webadmin.contextPath:'/domibuswebadmin'}") //TODO: funktioniert nicht...
    //public String domibusWebAdminContextPath = "/peter";
    
    @Value("${connector.webadmin.debug:false}")
    boolean debugMode;
    
    
    @PostConstruct
    public void postConstruct() {
        //LOG.trace("postConstruct: connector.webadmin.contextPath is : [{}]", domibusWebAdminContextPath);
    }
    
    /*
     * configure servletRegistration to listen
    */
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
    	LOG.trace("servletRegistrationBean: called");
        FacesServlet servlet = new FacesServlet();
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(servlet, "*.jsf", "*.xhtml"); 
        servletRegistrationBean.setLoadOnStartup(1);
        //servletRegistrationBean.addUrlMappings("/*");        
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
    


    @Bean
    public ServletListenerRegistrationBean<JsfApplicationObjectConfigureListener> jsfConfigureListener() {
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
    
  
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        LOG.debug("#onStartup: startup");
        servletContext.setInitParameter("javax.faces.DEFAULT_SUFFIX", ".xhtml");
        servletContext.setInitParameter("javax.faces.PARTIAL_STATE_SAVING_METHOD", "true");
        
        servletContext.setInitParameter("javax.faces.FACELETS_REFRESH_PERIOD", "1");

        servletContext.setInitParameter("primefaces.CLIENT_SIDE_VALIDATION", "true");
        servletContext.setInitParameter("primefaces.THEME", "bootstrap");
        servletContext.setInitParameter("primefaces.FONT_AWESOME", "true");
        servletContext.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", "true");
        
        //servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "Production");
        servletContext.setInitParameter("facelets.DEVELOPMENT", Boolean.toString(debugMode));
        
        Set<Class<?>> clazz = new HashSet<Class<?>>();

        clazz.add(DomibusWebAdminContext.class); // dummy, enables InitFacesContext
        FacesInitializer facesInitializer = new FacesInitializer();        
        facesInitializer.onStartup(clazz, servletContext);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
