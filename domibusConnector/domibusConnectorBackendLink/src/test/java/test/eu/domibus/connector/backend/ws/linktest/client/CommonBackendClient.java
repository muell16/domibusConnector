package test.eu.domibus.connector.backend.ws.linktest.client;

import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
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

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

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

    public static final String START_WEBSERVICE_PROFILE = "ws-backendclient-server";

    public static final String PROPERTY_BACKENDCLIENT_NAME = "ws.backendclient.name";
    public static final String PROPERTY_BACKENDCLIENT_KEY_ALIAS = "ws.backendclient.cn";
    public static final String PROPERTY_BACKENDCLIENT_KEY_PASSWORD = "ws.backendclient.password";
    public static final String PROPERTY_CONNECTOR_BACKEND_ADDRESS = "connector.backend.ws.address";

    public static ConfigurableApplicationContext startSpringApplication(String[] profiles, String[] properties) {

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

    public static ConfigurableApplicationContext startSpringApplication(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder();
        SpringApplication springApp = builder.bannerMode(Banner.Mode.OFF)
                .sources(CommonBackendClient.class)
                .build();

        ConfigurableApplicationContext appContext = springApp.run(args);

        return appContext;
    }


    public static LinkedBlockingQueue<DomibusConnectorMessageType> getPushedMessagesList(ConfigurableApplicationContext ctx) {
        return (LinkedBlockingQueue<DomibusConnectorMessageType>) ctx.getBean(BackendClientPushWebServiceConfiguration.PUSH_DELIVERED_MESSAGES_LIST_BEAN_NAME);
    }

    public static DomibusConnectorBackendWebService getBackendWebServiceClient(ConfigurableApplicationContext ctx) {
        return ctx.getBean("backendClient", DomibusConnectorBackendWebService.class);
    }

    
  
}
