package eu.ecodex.dc5.pmode;

import eu.domibus.connector.domain.model.DC5BusinessDomain;
import eu.ecodex.dc5.domain.CurrentBusinessDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class DC5PModeServiceImpl implements DC5PmodeService {


    private final BusinessScopedPModeService businessScopedPModeService;



    @Override
    public Optional<DomibusConnectorPModeSet> getCurrentPModeSet(DC5BusinessDomain.BusinessDomainId lane) {
        try (CurrentBusinessDomain.CloseAbleBusinessDomain bd = CurrentBusinessDomain.setCloseAbleCurrentBusinessDomain(lane);) {
            try {
                return Optional.ofNullable(businessScopedPModeService.getCurrentPModeSet());
            } catch (Exception e) {
                log.warn("Failed to get current pModeSet for domain " + lane + " due", e);
                return Optional.empty();
            }
        }
    }


}
