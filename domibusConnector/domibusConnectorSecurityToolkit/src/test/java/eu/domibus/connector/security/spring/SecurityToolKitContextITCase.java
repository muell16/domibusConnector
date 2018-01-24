package eu.domibus.connector.security.spring;

import eu.domibus.connector.security.container.DomibusSecurityContainer;
import javax.annotation.Resource;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *  Tests if context is loading..
 * code is loading external ressources (eu trusted lists)
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( classes={SecurityToolKitContextITCase.TestContextConfiguration.class})
@TestPropertySource(locations={"classpath:test.properties"}, 
        properties= {   "liquibase.change-log=classpath:/db/changelog/install/initial-4.0.xml",
                        "spring.jpa.show-sql=true",
                        "spring.datasource.url=jdbc:h2:mem:testdb",
                        "spring.datasource.username=sa",
                        "spring.datasource.driver-class-name=org.h2.Driver",
})
public class SecurityToolKitContextITCase {

    @EnableAutoConfiguration(exclude = {
//        DataSourceAutoConfiguration.class, 
//        DataSourceTransactionManagerAutoConfiguration.class, 
//        HibernateJpaAutoConfiguration.class
    })
    @SpringBootApplication(scanBasePackages = {"eu.domibus.connector.security", "eu.domibus.connector.persistence"})
    public static class TestContextConfiguration {

        @Bean
        public static PropertySourcesPlaceholderConfigurer
                propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }               
    }
    
    @Resource
    DomibusSecurityContainer domibusConnectorSecurityContainer;
    
    @Test
    public void testContextLoads() {
        assertThat(domibusConnectorSecurityContainer).isNotNull();
    }
    
}