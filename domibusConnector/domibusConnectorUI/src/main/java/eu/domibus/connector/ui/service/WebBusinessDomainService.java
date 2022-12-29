package eu.domibus.connector.ui.service;

import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.domain.model.DC5BusinessDomain;
import org.springframework.stereotype.Component;

@UIScope
@Component
public class WebBusinessDomainService {

    public DC5BusinessDomain.BusinessDomainId getCurrentBusinessDomain() {
        //TODO: for IMPL Business Domain Configuration within UI,
        //extend this to retrieve current business Domain
        return DC5BusinessDomain.getDefaultBusinessDomainId();
    }

}
