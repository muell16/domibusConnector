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
    public void DC5ProcessEvent(DC5MessageProcessEvent event) {
        msgProcessRepo.getDC5MsgProcessByProcessId(event.getProcessId());


    }


}
