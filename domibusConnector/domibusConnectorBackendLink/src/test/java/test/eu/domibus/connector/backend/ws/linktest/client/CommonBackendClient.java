package test.eu.domibus.connector.backend.ws.linktest.client;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@SpringBootApplication(scanBasePackageClasses = CommonBackendClient.class, exclude = {
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class,
})
@Configuration
@ImportResource("classpath:/test/testclient.xml")
public class CommonBackendClient {

    public static ApplicationContext startSpringApplication(String[] profiles, String[] properties) {

        boolean web = false;
        if (ArrayUtils.contains(profiles, "ws-backendclient-server")) {
            web = true;
        }
        
        SpringApplicationBuilder builder = new SpringApplicationBuilder();
        SpringApplication springApp = builder.bannerMode(Banner.Mode.OFF)
                .sources(CommonBackendClient.class)
                .properties(properties)
                .profiles(profiles)
                .web(web)
                .build();

        ConfigurableApplicationContext appContext = springApp.run();
        
        return appContext;
    }
    
  
}
