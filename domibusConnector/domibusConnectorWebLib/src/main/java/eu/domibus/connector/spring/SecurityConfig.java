package eu.domibus.connector.spring;

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

import java.util.ArrayList;
import java.util.List;

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
	WebUserAuthenticationProvider authProvider;

		
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//disable csrf so vaadin works!
		http.csrf().disable();
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

