package eu.domibus.connector.persistence.model;

import javax.persistence.EntityManager;
import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;

/**
 * Just test if persistence model loads in spring context
 * 
 * does not find any mistakes at db create, only if 
 * hibernate can process the entities
 * 
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at>}
 */
public class PersistenceModelTest {

    
    //DBdiff?? https://github.com/vecnatechnologies/dbDiff ??
    
    
    @Configuration
    @EnableAutoConfiguration(exclude={LiquibaseAutoConfiguration.class})
    @EntityScan("eu.domibus.connector.persistence.model")
    static class TestConfiguration {
        
    }

    @Test
    public void contextLoads() {
        SpringApplicationBuilder springAppBuilder = new SpringApplicationBuilder(TestConfiguration.class)
                .properties("spring.jpa.hibernate.ddl-auto=create-drop");                
        ConfigurableApplicationContext appContext = springAppBuilder.run();
        System.out.println("APPCONTEXT IS STARTED...:" + appContext.isRunning());
        
        //test if i get a EntityManager and can execute a query...
        EntityManager bean = appContext.getBean(EntityManager.class);
        bean.find(DomibusConnectorService.class, "service");
                
    }

}
