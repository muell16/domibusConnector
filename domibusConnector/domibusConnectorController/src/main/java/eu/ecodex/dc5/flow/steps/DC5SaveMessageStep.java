package eu.ecodex.dc5.flow.steps;

import eu.ecodex.dc5.core.model.DC5Msg;
import eu.ecodex.dc5.core.repository.DC5MessageRepo;
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
    public DC5Msg receiveMessage(DC5Msg msg) {

        DC5Msg dbMsg = messageRepo.save(msg);
        return dbMsg;

    }

}
