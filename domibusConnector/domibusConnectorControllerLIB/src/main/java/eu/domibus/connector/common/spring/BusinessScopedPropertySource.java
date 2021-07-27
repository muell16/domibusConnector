package eu.domibus.connector.common.spring;

import eu.domibus.connector.common.service.CurrentBusinessDomain;
import eu.domibus.connector.common.service.DCBusinessDomainManager;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;


public class BusinessScopedPropertySource extends PropertySource<DomibusConnectorBusinessDomain.BusinessDomainId> {

    private final static Logger LOGGER = LogManager.getLogger(BusinessScopedPropertySource.class);

    private final ApplicationContext applicationContext;

    public BusinessScopedPropertySource(ApplicationContext applicationContext) {
        super("BusinessDomain");
        this.applicationContext = applicationContext;
    }

    @Override
    public String getProperty(String name) {
        String value = null;
        if (CurrentBusinessDomain.getCurrentBusinessDomain() != null) {
            DCBusinessDomainManager businessDomainManager = applicationContext.getBean(DCBusinessDomainManager.class);
            Map<ConfigurationPropertyName, String> m = new HashMap<>();

            businessDomainManager.getBusinessDomain(CurrentBusinessDomain.getCurrentBusinessDomain())
                    .map(DomibusConnectorBusinessDomain::getMessageLaneProperties)
                    .orElse(new HashMap<>())
                    .forEach((key, v) -> m.put(ConfigurationPropertyName.of(key), v));

            value = m.get(ConfigurationPropertyName.of(name));

            LOGGER.trace("Resolved property [{}={}]", name, value);
        }
        return value;
    }

}
