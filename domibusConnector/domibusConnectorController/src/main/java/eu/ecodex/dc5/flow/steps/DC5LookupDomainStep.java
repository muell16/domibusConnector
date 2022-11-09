package eu.ecodex.dc5.flow.steps;

import eu.domibus.connector.common.service.CurrentBusinessDomain;
import eu.domibus.connector.common.service.DCBusinessDomainManager;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
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

    private final DC5DomainRepo domainRepo;
    private final DCBusinessDomainManager domainManager;

    @Step(name = "LookupDomainStep")
    public DC5Msg lookupDomain(DC5Msg msg) {
        //TODO: implement Business domain matching rules here!
        Optional<DC5Domain> optionalDomain = domainRepo.findByDomainKey(DomibusConnectorBusinessDomain.DEFAULT_LANE_NAME);
        if (optionalDomain.isPresent()) {
            DC5Domain domain = optionalDomain.get();
            CurrentBusinessDomain.setCurrentBusinessDomain(new DomibusConnectorBusinessDomain.BusinessDomainId(domain.getDomainKey()));
            msg.setDomain(optionalDomain.get());
        } else {
            throw new IllegalStateException("No default business domain found in DB!");
        }
        return msg;
    }


}
