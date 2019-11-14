package eu.domibus.connector.security.spring;

import eu.domibus.connector.common.spring.CommonProperties;
import eu.domibus.connector.security.container.DomibusSecurityContainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *  Tests if context is loading..
 * code is loading external ressources (eu trusted lists)
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration( classes={SecurityToolKitContextITCase.TestContextConfiguration.class})
@TestPropertySource(locations={"classpath:test.properties", "classpath:test-auth.properties"},
        properties={"spring.liquibase.enabled=false" }
)
@ActiveProfiles({"ittest", "storage-db"})
public class SecurityToolKitContextITCase {

    @SpringBootApplication(
            scanBasePackages = {"eu.domibus.connector.security", "eu.domibus.connector.persistence"},
            scanBasePackageClasses = {CommonProperties.class},
            exclude = {
                    DataSourceAutoConfiguration.class,
                    DataSourceTransactionManagerAutoConfiguration.class,
                    HibernateJpaAutoConfiguration.class
            }
    )
    public static class TestContextConfiguration {

        @Bean
        public static PropertySourcesPlaceholderConfigurer
                propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }

        @Bean
        public static CommonProperties commonProperties() {
            return new CommonProperties();
        }
                
    }
    
    @Resource
    DomibusSecurityContainer domibusConnectorSecurityContainer;
    
    @Test
    public void testContextLoads() {
        assertThat(domibusConnectorSecurityContainer).isNotNull();
    }
    
}
