
package eu.domibus.webadmin.spring;

import eu.domibus.webadmin.spring.support.DomibusWebAdminProperties;
import javax.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Configuration
public class DispatcherServletConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(DispatcherServletConfig.class);
    
    @Autowired
    WebApplicationContext webApplicationContext;
    
    @Autowired
    DomibusWebAdminProperties webAdminProperties;
    
    @Bean
    public ServletRegistrationBean registerDispatcherServlet() throws ServletException {
        LOGGER.debug("#registerDispatcherServlet: startup");

        if (webAdminProperties == null) {
            throw new IllegalArgumentException("cannot be null!");
        }

        // Create and register the DispatcherServlet
        String dispatcherServletMapping = "/" + webAdminProperties.getServletPath() + "/*";
        LOGGER.debug("create and register the Dispatcher Servlet on mapping [{}]", dispatcherServletMapping);
        DispatcherServlet dispatcherServlet = new DispatcherServlet(webApplicationContext);

        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(dispatcherServlet, dispatcherServletMapping);

        //ServletRegistration.Dynamic registration = servletRegistrationBean.//servletContext.addServlet(webAdminProperties.getServletPath(), dispatcherServlet);
        servletRegistrationBean.setLoadOnStartup(1);

        return servletRegistrationBean;
    }
    
    
}
