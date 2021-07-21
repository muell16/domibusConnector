package eu.domibus.connector.common.spring;

import eu.domibus.connector.common.service.CurrentBusinessDomain;
import eu.domibus.connector.common.service.DCBusinessDomainManager;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.PropertySource;


public class BusinessScopedPropertySource extends PropertySource<DomibusConnectorBusinessDomain.BusinessDomainId> {

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
            value = businessDomainManager.getBusinessDomain(CurrentBusinessDomain.getCurrentBusinessDomain())
                    .map(DomibusConnectorBusinessDomain::getMessageLaneProperties)
                    .map(p -> p.get(name))
                    .orElse(null);
        }
        return value;
    }

}
