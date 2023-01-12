package eu.domibus.connector.ui.component;

import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.domain.model.DC5BusinessDomain;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@UIScope
public class DomainSelect extends Select<DC5BusinessDomain.BusinessDomainId> {
    private final DCBusinessDomainManager domainManager;

    public DomainSelect(DCBusinessDomainManager domainManager) {
        this.domainManager = domainManager;
        this.getDomains();
        this.setLabel("Config applies to domain:");
        this.setEmptySelectionAllowed(false);
        this.setRequired(true);
    }

    public void getDomains() {
        final List<DC5BusinessDomain.BusinessDomainId> domainIds = this.domainManager.getDomains().stream().map(DC5BusinessDomain::getId).collect(Collectors.toList());
        this.setItems(domainIds);
        this.setValue(domainIds.get(0));
    }
}
