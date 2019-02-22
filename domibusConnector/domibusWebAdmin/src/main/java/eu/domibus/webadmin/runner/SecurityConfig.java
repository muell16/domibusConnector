package eu.domibus.webadmin.runner;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import eu.domibus.webadmin.runner.springsupport.DomibusWebAdminUserAuthenticationProvider;

/**
 * Configures Spring Security
 * @author spindlest
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final static Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);

	@Autowired
	DomibusWebAdminUserAuthenticationProvider authProvider;
	
	@Bean
	public AuthenticationEntryPoint loginUrlauthenticationEntryPoint(){
	    return new LoginUrlAuthenticationEntryPoint("/pages/login.xhtml");
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
		
	
	/**
	 * creates a Authentication Provider
	 *  including authProvider
	 */
	@Bean
	public AuthenticationManager authenticationManager() {		
		List<AuthenticationProvider> authProviders = new ArrayList<>();	
		authProviders.add(authProvider);
		return new ProviderManager(authProviders);		
	}
	
	
	
} 

