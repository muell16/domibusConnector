package eu.domibus.connector.persistence.spring;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.web.reactive.context.ConfigurableReactiveWebApplicationContext;
import org.springframework.context.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class DatabaseResourceLoaderConfiguration implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        applicationContext.addProtocolResolver(new ProtocolResolver() {
            @Override
            public Resource resolve(String location, ResourceLoader resourceLoader) {
                if (location.startsWith(DatabaseResourceLoader.DB_URL_PREFIX)) {
                    DatabaseResourceLoader r = applicationContext.getBean(DatabaseResourceLoader.class);
                    return r.getResource(location);
                }
                return null;
            }
        });
    }
}