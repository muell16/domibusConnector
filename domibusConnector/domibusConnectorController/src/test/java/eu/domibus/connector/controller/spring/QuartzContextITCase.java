package eu.domibus.connector.controller.spring;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.process.CheckEvidencesTimeoutProcessorImpl;
import eu.domibus.connector.controller.process.CheckEvidencesTimoutProcessor;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;
import javax.sql.DataSource;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Stephan Spindler <stephan.spindler@extern.brz.gv.at>
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration( classes={QuartzContextITCase.TestContextConfiguration.class})
//@TestPropertySource(locations = {"classpath:application-test.properties"}, properties={
//    "connector.check.messages.period.ms=100",
//    "connector.use.evidences.timeout=true"
//})
//@Profile("test")
public class QuartzContextITCase {

    private final static Logger LOGGER = LoggerFactory.getLogger(QuartzContextITCase.class);
    
    private static ConfigurableApplicationContext APPLICATION_CONTEXT;

    private static AtomicInteger JOB_CALL_COUNT = new AtomicInteger(0);
    
    @Import({
        QuartzContext.class       
    })
    @ComponentScan(basePackages="eu.domibus.connector.controller.scheduler.job")    //scan for quartz jobs
    @EnableAutoConfiguration
    public static class TestContextConfiguration {

        @Bean
        public static PropertySourcesPlaceholderConfigurer
                propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }
                
        @Bean
        public CheckEvidencesTimoutProcessor checkEvidencesTimeoutProcessor() {
            return () -> {
                int callNr = JOB_CALL_COUNT.addAndGet(1);
                LOGGER.info("checkEvidencesTimeout called [{}] time on mocked CheckEvidencesTimoutProcessor!", callNr);
            };
        }
                
    }

    @BeforeClass
    public static void beforeClass() {
        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder();
        SpringApplication build = springApplicationBuilder
                .profiles("test")
                .properties("connector.check.messages.period.ms=100", "connector.use.evidences.timeout=true")
                .sources(TestContextConfiguration.class)
                .build();
        ConfigurableApplicationContext applicationContext = build.run("");
        APPLICATION_CONTEXT = applicationContext;
    }
    
    @Before
    public void setUp() {
//        quartzContext = APPLICATION_CONTEXT.getBean(QuartzContext.class);
//        checkEvidencesProcessor = APPLICATION_CONTEXT.getBean(CheckEvidencesTimeoutProcessor.class);
//        dataSource = APPLICATION_CONTEXT.getBean(DataSource.class);
    }
    
    QuartzContext quartzContext;
    
    DataSource dataSource;
    
    @Test(timeout=20000) //10s
    public void testIfQuartzJobsAreExecuted() throws InterruptedException, SQLException {
        
//        assertThat(quartzContext).isNotNull();
        
        Thread.sleep(7000); //1s
        
        //check job counter
        assertThat(JOB_CALL_COUNT.get()).as("job should be executed more than 10 times").isGreaterThan(10);
        
        //TODO: check database if jobs are run
        //Connection connection = dataSource.getConnection();
        //Statement stmt = connection.createStatement();
        //stmt.executeQuery("SELECT * FROM DCON_")
        
        
    }
    
}