package eu.domibus.connector.web.configuration;

import eu.domibus.connector.spring.WebUserAuthenticationProvider;
import eu.domibus.connector.web.login.LoginView;
import liquibase.pro.packaged.A;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;

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
    public static class VaadinWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

        private static final String LOGIN_PROCESSING_URL = "/" + LoginView.ROUTE;
        private static final String LOGIN_FAILURE_URL = "/login?error";
        private static final String LOGIN_URL = "/" + LoginView.ROUTE;
        private static final String LOGOUT_SUCCESS_URL = "/" + LoginView.ROUTE;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //disable csrf so vaadin works!
            http.csrf().disable()
            // Register our CustomRequestCache, that saves unauthorized access attempts, so
            // the user is redirected after login.
            .requestCache().requestCache(new CustomRequestCache())

            // Restrict access to our application.
            .and().authorizeRequests()

            // Allow all flow internal requests.
            .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()

            // Allow all requests by logged in users.
            .anyRequest().authenticated()

//             Configure the login page.
            .and().formLogin().loginPage(LOGIN_URL).permitAll().loginProcessingUrl(LOGIN_PROCESSING_URL)
            .failureUrl(LOGIN_FAILURE_URL)

//             Configure logout
            .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);

        }

        /**
         * Allows access to static resources, bypassing Spring security.
         */
        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers(
                    // Vaadin Flow static resources
                    "/VAADIN/**",

                    // the standard favicon URI
                    "/favicon.ico",

                    // the robots exclusion standard
                    "/robots.txt",

                    // web application manifest
                    "/manifest.webmanifest",
                    "/sw.js",
                    "/offline-page.html",

                    // icons and images
                    "/icons/**",
                    "/images/**",

                    // (production mode) static resources
                    "/frontend-es5/**", "/frontend-es6/**",
                    // (development mode) static resources
                    "/frontend/**",

                    // (development mode) webjars
                    "/webjars/**",

                    // (development mode) H2 debugging console
                    "/h2-console/**"

                    );
        }
    }




//    @Configuration
//    @Order(499)
//    @Profile("dev")
    public static class VaadinDevelopmentWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        ConnectorUiConfigurationProperties connectorUiConfigurationProperties;

        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers(


            );
        }
    }

} 

