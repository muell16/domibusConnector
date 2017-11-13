package eu.domibus.webadmin.runner;

import java.util.EnumSet;

import javax.faces.webapp.FacesServlet;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;



@SpringBootApplication
@EnableAutoConfiguration
@Configuration
@ComponentScan
public class WebRunner  extends SpringBootServletInitializer  {

	public WebRunner() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		SpringApplication.run(WebRunner.class, args);
	}
	
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        FacesServlet servlet = new FacesServlet();
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(servlet, "*.xhtml", "*.jsf");
        servletRegistrationBean.setLoadOnStartup(1);
        
		return servletRegistrationBean;
    }

//    @Bean
//    public FilterRegistrationBean rewriteFilter() {
//        FilterRegistrationBean rwFilter = new FilterRegistrationBean(new RewriteFilter());
//        
//        rwFilter.setDispatcherTypes(EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST,
//                DispatcherType.ASYNC, DispatcherType.ERROR));
//        rwFilter.addUrlPatterns("/*");
//        return rwFilter;
//    }
	
//	 implements WebApplicationInitializer 
//	@Override
//	public void onStartup(ServletContext servletContext) throws ServletException {
//		 AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
////		    root.register(SpringCoreConfig.class);
//		 
//		 servletContext.addListener(new ContextLoaderListener(root));
//		
//	}

}
