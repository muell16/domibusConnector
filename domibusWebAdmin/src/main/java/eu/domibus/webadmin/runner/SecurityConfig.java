package eu.domibus.webadmin.runner;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import eu.domibus.webadmin.dao.IDomibusWebAdminUserDao;
import eu.domibus.webadmin.runner.springsupport.DomibusWebAdminUserAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final static Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);
	
	@Autowired
	IDomibusWebAdminUserDao userDao;
	
	@Bean
	public AuthenticationEntryPoint loginUrlauthenticationEntryPoint(){
	    return new LoginUrlAuthenticationEntryPoint("/pages/login.xhtml");
	}
		
	@Bean
	public DomibusWebAdminUserAuthenticationProvider domibusWebAdminUserAuthenticationProvider() {
		DomibusWebAdminUserAuthenticationProvider authProvider = new DomibusWebAdminUserAuthenticationProvider(userDao);
		return authProvider;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		

		http.csrf().disable();
		
		http.logout()
			.logoutUrl("/logout")
			.invalidateHttpSession(true)
			.logoutSuccessUrl("/pages/login.xhtml");
			
		http.authorizeRequests()
		
			.antMatchers("/pages/login.xhtml").permitAll()
		
			.antMatchers("/javax.faces.resource/**").permitAll()
			.antMatchers("/css/**").permitAll()
			.antMatchers("/images/**").permitAll()
			.anyRequest().authenticated()
			
			.and()
				.exceptionHandling().defaultAuthenticationEntryPointFor(loginUrlauthenticationEntryPoint(), new AntPathRequestMatcher("/**"));
	} 
	
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		//auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
		auth.authenticationProvider(domibusWebAdminUserAuthenticationProvider());
	}	
} 

