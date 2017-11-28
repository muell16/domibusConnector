package eu.domibus.webadmin.runner;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * creates a EntityManagerFactory with all Entity classes in 
 * following packages: 
 * 	"eu.domibus.connector.common.db.model", "eu.domibus.webadmin.model.connector"
 * @author spindlest
 *
 */
@Configuration
public class JpaContext {

	@Autowired
	DataSource dataSource;
	
	@Bean("domibus.connector")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan(new String[] { "eu.domibus.connector.common.db.model", "eu.domibus.webadmin.model.connector" });

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

		em.setJpaVendorAdapter(vendorAdapter);                
		return em;
	}
        
}
