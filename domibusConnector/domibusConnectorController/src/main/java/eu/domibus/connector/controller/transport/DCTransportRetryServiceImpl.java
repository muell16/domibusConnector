package eu.domibus.connector.controller.transport;

import eu.domibus.connector.controller.service.SubmitToLinkService;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.domibus.connector.domain.model.DomibusConnectorTransportStep;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DCTransportRetryServiceImpl implements DCTransportRetryService {

    private final SubmitToLinkService submitToLinkService;
//    private final DCMessagePersistenceService messagePersistenceService;


    @Override
    public void retryTransport(DomibusConnectorTransportStep step) {
        step.getTransportedMessage().ifPresent(submitToLinkService::submitToLink);
    }


    @Override
    public boolean isRetryAble(DomibusConnectorTransportStep step) {
        boolean retryPossible = true;
        if (step.getTransportedMessage().isPresent()) {
            DC5Message msg = step.getTransportedMessage().get();
            if (DomainModelHelper.isBusinessMessage(msg)) {
//                retryPossible = !messagePersistenceService.checkMessageConfirmedOrRejected(msg);
            }
        } else {
            retryPossible = false;
        }
        return retryPossible;
    }

}
