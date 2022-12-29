package eu.ecodex.dc5.domain;

import eu.domibus.connector.domain.model.DC5BusinessDomain;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class BusinessDomainConfigurationChange extends ApplicationEvent {

    /**
     * The business domain where the configuration has changed
     */
    private final DC5BusinessDomain.BusinessDomainId businessDomainId;

    private final Map<String, String> changedConfiguration;

    /**
     * Create a new {@code ApplicationEvent}.
     *  @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     * @param businessDomainId the business domain, which has been changed
     * @param changedConfiguration the configuration changes itself
     */
    public BusinessDomainConfigurationChange(Object source,
                                             DC5BusinessDomain.BusinessDomainId businessDomainId,
                                             Map<String, String> changedConfiguration) {
        super(source);
        this.businessDomainId = businessDomainId;
        this.changedConfiguration = changedConfiguration;
    }

    public DC5BusinessDomain.BusinessDomainId getBusinessDomainId() {
        return businessDomainId;
    }

    public Map<String, String> getChangedConfiguration() {
        return changedConfiguration;
    }
}
