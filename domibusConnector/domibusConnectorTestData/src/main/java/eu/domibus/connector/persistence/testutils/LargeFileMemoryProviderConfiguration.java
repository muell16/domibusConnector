package eu.domibus.connector.persistence.testutils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class LargeFileMemoryProviderConfiguration {


    @Bean
    @Profile("flow-test")
    public LargeFileProviderMemoryImpl largeFileProviderMemory() {
       return new LargeFileProviderMemoryImpl();
    }


}
