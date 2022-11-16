package eu.ecodex.dc5.flow.steps;

import eu.ecodex.dc5.message.model.DC5Message;
import eu.domibus.connector.domain.model.repo.DomibusConnectorMessageRepo;
import eu.ecodex.dc5.flow.api.Step;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service is responsible for starting a Message Process
 */
@Service
@RequiredArgsConstructor
public class DC5SaveMessageStep {

    private final DomibusConnectorMessageRepo messageRepo;


    @Step(name = "SaveMessage")
    @Transactional(propagation = Propagation.REQUIRES_NEW) //run in a new nested TX
    public DC5Message saveNewMessage(DC5Message msg) {
        DC5Message dbMsg = messageRepo.save(msg);
        return dbMsg;
    }

}
