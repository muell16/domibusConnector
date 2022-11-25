package eu.ecodex.dc5.flow.steps;

import eu.domibus.connector.common.service.CurrentBusinessDomain;
import eu.domibus.connector.common.service.DCBusinessDomainManager;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.flow.api.Step;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DC5LookupDomainStep {

    private final DCBusinessDomainManager domainManager;

    @Step(name = "LookupDomainStep")
    public DC5Message lookupDomain(DC5Message msg) {
        //TODO: implement Business domain matching rules here!

        Optional<DomibusConnectorBusinessDomain> bd = domainManager.getBusinessDomain(DomibusConnectorBusinessDomain.getDefaultBusinessDomainId());
        if (bd.isPresent()) {
            DomibusConnectorBusinessDomain domibusConnectorBusinessDomain = bd.get();
            CurrentBusinessDomain.setCurrentBusinessDomain(domibusConnectorBusinessDomain.getId());
            msg.setBusinessDomainId(domibusConnectorBusinessDomain.getId());
        } else {
            throw new IllegalStateException("No default business domain found in DB!");
        }
        return msg;
    }


}
