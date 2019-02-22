package eu.domibus.webadmin.runner;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Iterator;
import java.util.Properties;


/**
 * creates a EntityManagerFactory with all Entity classes in 
 * following packages: 
 * 	"eu.domibus.connector.common.db.model", "eu.domibus.webadmin.model.connector"
 * 
 * also creates a PlatformTransactionManager 
 * 
 * @author spindlest
 *
 */
@Configuration
@EnableTransactionManagement
public class JpaContext {

	private static final Logger LOGGER = LogManager.getLogger(JpaContext.class);

	@Autowired
	DataSource dataSource;

	@Autowired
	private Environment env;

	@Bean("domibus.connector")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan(new String[] { "eu.domibus.connector.common.db.model", "eu.domibus.webadmin.model.connector" });

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

		em.setJpaVendorAdapter(vendorAdapter);

		Properties properties = additionalProperties();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.info("Setting following additional JPA properties:");
			Iterator<Object> it = properties.keySet().iterator();
			while (it.hasNext()) {
				String k = (String) it.next();
				LOGGER.info(String.format("setting prop: [%s=%s]", k, properties.get(k)));
			}
		}

		em.setJpaProperties(properties);
		return em;
	}

	private Properties additionalProperties() {
		Properties properties = new Properties();

		putIfNotNull(properties,"hibernate.ejb.naming_strategy", "org.hibernate.cfg.DefaultNamingStrategy");

		putIfNotNull(properties,"hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
		putIfNotNull(properties,"hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
		putIfNotNull(properties,"hibernate.current_session_context_class", env.getProperty("spring.jpa.properties.hibernate.current_session_context_class"));
		putIfNotNull(properties,"hibernate.jdbc.lob.non_contextual_creation", env.getProperty("spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation"));
		putIfNotNull(properties,"hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
		putIfNotNull(properties,"hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql"));
		putIfNotNull(properties,"hibernate.ejb.naming_strategy", env.getProperty("spring.jpa.properties.hibernate.ejb.naming_strategy"));
		return properties;
	}

	private Object putIfNotNull(Properties props, String key, String value) {
		if (value == null) {
			return null;
		}
		return props.setProperty(key, value);
	}
	
	@Bean("transactionManager")
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}
        
}
