package eu.ecodex.dc5.process.model;

import eu.ecodex.dc5.process.repository.DC5MsgProcessRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DC5ProcessStateEventHandler {

    private final DC5ProcessStateMachineFactory dc5ProcessStateMachineFactory;
    private final DC5MsgProcessRepo msgProcessRepo;

    @EventListener
    public void handleDC5ProcessEvent(DC5MessageProcessEvent event) {
        DC5MsgProcess dc5MsgProcessByProcessId = msgProcessRepo.getDC5MsgProcessByProcessId(event.getProcessId());
        //load state machine for process dc5MsgProcessByProcessId and publish state to it
    }


}
