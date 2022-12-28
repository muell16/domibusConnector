package eu.ecodex.dc5.process.model;

import eu.domibus.connector.controller.exception.DCEvidenceProcessingException;
import eu.domibus.connector.controller.exception.ErrorCode;
import eu.ecodex.dc5.message.model.DC5BusinessMessageState;
import eu.ecodex.dc5.process.repository.DC5MsgProcessRepo;
import eu.ecodex.dc5.statemachine.StateTransitionChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import static eu.ecodex.dc5.message.model.DC5BusinessMessageState.BusinessMessageEvents.*;
import static eu.ecodex.dc5.message.model.DC5BusinessMessageState.BusinessMessageEvents.ADMIN_ABORT;
import static eu.ecodex.dc5.message.model.DC5BusinessMessageState.BusinessMessagesStates.*;

@Service
public class DC5ProcessStateMachineFactory {

    private final StateTransitionChecker<DC5ProcessState, DC5ProcessEvents> stateTransitionChecker;

    public DC5ProcessStateMachineFactory() {
        this.stateTransitionChecker = StateTransitionChecker.<DC5ProcessState, DC5ProcessEvents>builder()
                .initialState(DC5ProcessState.NEW)
                .transition(DC5ProcessState.NEW, DC5ProcessState.SAVED, DC5ProcessEvents.SAVED)

                .build();
    }

}
