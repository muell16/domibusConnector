package eu.ecodex.dc5.process.model;

import eu.domibus.connector.controller.exception.DCEvidenceProcessingException;
import eu.domibus.connector.controller.exception.ErrorCode;
import eu.ecodex.dc5.message.model.DC5BusinessMessageState;
import eu.ecodex.dc5.message.model.DC5Confirmation;
import eu.ecodex.dc5.process.repository.DC5MsgProcessRepo;
import eu.ecodex.dc5.statemachine.StateTransitionChecker;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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

    public DC5ProcessStateMachine loadStateMachine(DC5ProcessStateItem state) {
        return new DC5ProcessStateMachine(state);
    }

    public static class DC5ProcessStateItem {

    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class DC5ProcessStateMachine {

        @NonNull
        private final DC5ProcessStateItem currentState;

        public void publishEvent(DC5ProcessEvents event) {
//            Optional<DC5BusinessMessageState.BusinessMessagesStates> state = stateTransitionChecker.doTransition(currentState.getState(), event);
//            if (state.isPresent()) {
//                return DC5BusinessMessageState.builder()
//                        .state(state.get())
//                        .created(LocalDateTime.now())
//                        .event(event)
//                        .confirmation(dc5Confirmation)
//                        .build();
//            }
//            throw new IllegalStateException(String.format("The transition from [%s] to [%s] is not possible!", state, event));
        }

    }

}
