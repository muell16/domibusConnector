package eu.domibus.connector.lib.spring.configuration.validation;

import eu.domibus.connector.common.service.DCKeyStoreService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Import(DCKeyStoreService.class)
public class ValidationTestContext {


}
