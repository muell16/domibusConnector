package eu.domibus.connector.spring;

import javassist.tools.web.Webserver;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
 *
 * @author spindlest
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final static Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    WebUserAuthenticationProvider authProvider;


    /**
     * creates a Authentication Provider
     * including authProvider
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> authProviders = new ArrayList<>();
        authProviders.add(authProvider);
        return new ProviderManager(authProviders);
    }


    @Configuration
    @Order(1)
    public static class ActuatorWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

        //		@Value("management.endpoints.web.base-path:''")
        private String actuatorBasePath = "actuator";

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            if (StringUtils.isNotEmpty(actuatorBasePath)) {
                http
                        .antMatcher("/" + actuatorBasePath + "/**")
                        .httpBasic()
                        .and()
                        .authorizeRequests()
                        .anyRequest()
                        .hasAnyAuthority("ACTUATOR", "ADMIN");
            }
        }
    }

    @Configuration
    @Order(500)
    public static class DefaultWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //disable csrf so vaadin works!
            http.csrf().disable();
            //allow access to frontend...
           http.antMatcher("/frontend/**").authorizeRequests(req -> req.anyRequest().permitAll());
//            http.authorizeRequests().antMatchers("/frontend/**").permitAll();
            //allow access to all vaadin stuff...TODO: allow only vaadin login page
//            http.authorizeRequests().antMatchers("/**").permitAll();

        }
    }


} 

