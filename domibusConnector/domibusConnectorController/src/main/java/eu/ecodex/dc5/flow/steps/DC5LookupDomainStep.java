package eu.ecodex.dc5.flow.steps;

import eu.domibus.connector.common.service.CurrentBusinessDomain;
import eu.domibus.connector.common.service.DCBusinessDomainManager;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.ecodex.dc5.core.model.DC5Domain;
import eu.ecodex.dc5.core.model.DC5Msg;
import eu.ecodex.dc5.core.model.DC5MsgProcess;
import eu.ecodex.dc5.core.repository.DC5DomainRepo;
import eu.ecodex.dc5.flow.api.DC5TransformToDomain;
import eu.ecodex.dc5.flow.api.Step;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DC5LookupDomainStep {

    private final DCBusinessDomainManager domainManager;

    @Step(name = "LookupDomainStep")
    public DomibusConnectorMessage lookupDomain(DomibusConnectorMessage msg) {
        //TODO: implement Business domain matching rules here!

        Optional<DomibusConnectorBusinessDomain> bd = domainManager.getBusinessDomain(DomibusConnectorBusinessDomain.getDefaultMessageLaneId());
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
