package eu.domibus.connector.ui.service;

import com.tngtech.archunit.base.Optional;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.domain.model.DC5BusinessDomain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.annotation.CheckForNull;

@Component
@UIScope
public class WebCurrentlySelectedDomainHolder {

    private DC5BusinessDomain.BusinessDomainId businessDomainId = null;

    public Optional<DC5BusinessDomain.BusinessDomainId> getBusinessDomainId() {
        return Optional.ofNullable(businessDomainId);
    }

    public void setBusinessDomainId(DC5BusinessDomain.BusinessDomainId businessDomainId) {
        this.businessDomainId = businessDomainId;
    }
}
