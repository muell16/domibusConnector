package eu.domibus.connector.ui.component;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.domain.model.DC5BusinessDomain;
import eu.domibus.connector.ui.service.WebCurrentlySelectedDomainHolder;
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
    private final WebCurrentlySelectedDomainHolder webCurrentlySelectedDomainHolder;

    public DomainSelect(DCBusinessDomainManager domainManager,
                        WebCurrentlySelectedDomainHolder webCurrentlySelectedDomainHolder) {
        this.webCurrentlySelectedDomainHolder = webCurrentlySelectedDomainHolder;
        this.domainManager = domainManager;
        this.getDomains();
        this.setLabel("Config applies to domain:");
        this.setEmptySelectionAllowed(false);
        this.setRequired(true);
        this.addValueChangeListener(this::selectedDomainChanged);
    }

    private void selectedDomainChanged(ComponentValueChangeEvent<Select<DC5BusinessDomain.BusinessDomainId>, DC5BusinessDomain.BusinessDomainId> event) {
        this.webCurrentlySelectedDomainHolder.setBusinessDomainId(event.getValue());
    }


    public void getDomains() {
        final List<DC5BusinessDomain.BusinessDomainId> domainIds = this.domainManager.getDomains().stream().map(DC5BusinessDomain::getId).collect(Collectors.toList());
        this.setItems(domainIds);
        if (webCurrentlySelectedDomainHolder.getBusinessDomainId().isPresent()) {
            this.setValue(webCurrentlySelectedDomainHolder.getBusinessDomainId().get());
        } else {
            this.setValue(domainIds.get(0));
        }
    }
}
