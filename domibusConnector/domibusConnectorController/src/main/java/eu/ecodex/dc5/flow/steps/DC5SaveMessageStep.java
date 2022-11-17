package eu.ecodex.dc5.flow.steps;

import eu.ecodex.dc5.message.repo.DC5MessageRepo;
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

    private final DC5MessageRepo messageRepo;


    @Step(name = "SaveMessage")
    @Transactional(propagation = Propagation.REQUIRES_NEW) //run in a new nested TX
    public eu.ecodex.dc5.message.model.DC5Message saveNewMessage(eu.ecodex.dc5.message.model.DC5Message msg) {
        eu.ecodex.dc5.message.model.DC5Message dbMsg = messageRepo.save(msg);
        return dbMsg;
    }

}
