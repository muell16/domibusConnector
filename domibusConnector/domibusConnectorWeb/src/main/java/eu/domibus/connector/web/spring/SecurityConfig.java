package eu.domibus.connector.web.spring;

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
	
//	@Bean
//	public AuthenticationEntryPoint loginUrlauthenticationEntryPoint(){
//	    return new LoginUrlAuthenticationEntryPoint("/domibusConnector/login");
//	}
		
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		

		http.csrf().disable();
		
//		http.logout()
//			.logoutUrl("/logout")
//			.invalidateHttpSession(true)
//			.logoutSuccessUrl("/domibusConnector/login");
			
		http.authorizeRequests()
            .antMatchers("/services/*").permitAll() //allow access to webservices
//			.antMatchers("/domibusConnector/login").permitAll()
		
			.antMatchers("/VAADIN/**", "/resources/**", "/icons/**", "/images/**",
                    "/frontend/**",
                    "/manifest.json",
                    // (development mode) webjars
                    "/webjars/**").permitAll()
			.antMatchers("/css/**").permitAll()
			.antMatchers("/images/**").permitAll();

//                        .anyRequest().authenticated();
//			.and()
//				.exceptionHandling().defaultAuthenticationEntryPointFor(loginUrlauthenticationEntryPoint(), new AntPathRequestMatcher("/**"));
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

