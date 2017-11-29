package eu.domibus.webadmin.runner.springsupport.setup;

import javax.sql.DataSource;
import org.primefaces.component.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * Context provides DataSource 
 *  for uninitialized System to provide all necessary beans to boot application
 *  to make it possibly to start an SetupWizard
 * 
 * @author spindlest
 */
//@Configuration
public class SetupContext {
    
    private final static Logger LOG = LoggerFactory.getLogger(SetupContext.class);
    
    public final static String EMBEDDED_MODE_PROPERTY_NAME = "eu.domibus.webadmin.embedded_mode";
    
    //@ConditionalOnMissingBean(DataSource.class)
    public DataSource h2DataSource() {
        LOG.info("No DataSource bean found, providing embedded DataSource!");
        
        System.setProperty(EMBEDDED_MODE_PROPERTY_NAME, "true");
        
        EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        return db;        
    }
        
}
