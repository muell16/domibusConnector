package eu.domibus.connector.ui.component;

import com.vaadin.flow.component.select.Select;
import eu.domibus.connector.common.service.DCBusinessDomainManager;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class DomainSelect extends Select<DomibusConnectorBusinessDomain.BusinessDomainId> {
    private final DCBusinessDomainManager dcBusinessDomainManager;

    public DomainSelect(DCBusinessDomainManager dcBusinessDomainManager) {
        this.dcBusinessDomainManager = dcBusinessDomainManager;
        this.setLabel("Config applies to domain:");
        this.setEmptySelectionAllowed(false);
        final List<DomibusConnectorBusinessDomain.BusinessDomainId> activeBusinessDomainIds = dcBusinessDomainManager.getAllBusinessDomains();
        this.setItems(activeBusinessDomainIds);
        this.setValue(activeBusinessDomainIds.get(0));
    }
}
