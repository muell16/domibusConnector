package eu.ecodex.dc5.message.model;

import eu.domibus.connector.controller.exception.DCEvidenceProcessingException;
import eu.domibus.connector.controller.exception.ErrorCode;
import eu.domibus.connector.controller.spring.ConnectorMessageProcessingProperties;
import eu.domibus.connector.domain.model.DCMessageProcessSettings;
import eu.ecodex.dc5.statemachine.StateTransitionChecker;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static eu.ecodex.dc5.message.model.DC5BusinessMessageState.BusinessMessagesStates.*;
import static eu.ecodex.dc5.message.model.DC5BusinessMessageState.BusinessMessageEvents.*;

@Service
public class DC5BusinessMessageStateMachineFactory {


    private final StateTransitionChecker<DC5BusinessMessageState.BusinessMessagesStates, DC5BusinessMessageState.BusinessMessageEvents> stateTransitionChecker;

    public DC5BusinessMessageStateMachineFactory(ConnectorMessageProcessingProperties properties) {
        this.stateTransitionChecker = StateTransitionChecker.<DC5BusinessMessageState.BusinessMessagesStates, DC5BusinessMessageState.BusinessMessageEvents>builder()
                .initialState(CREATED)
                .transition(CREATED, SUBMITTED, SUBMISSION_ACCEPTANCE_RCV)
                .transition(SUBMITTED, RELAYED, SUBMISSION_ACCEPTANCE_RCV)
                .transition(SUBMITTED, REJECTED, RELAY_REMMD_FAILURE_RCV)
                .transition(SUBMITTED, REJECTED, RELAY_REMMD_REJECTION_RCV)
                .transition(CREATED, REJECTED, SUBMISSION_REJECTION_RCV)
                .transition(RELAYED, REJECTED, NON_DELIVERY_RCV)
                .transition(RELAYED, REJECTED, RELAY_REMMD_REJECTION_RCV)
                .transition(RELAYED, RELAYED, RELAY_REMMD_ACCEPTANCE_RCV)
                .transition(RELAYED, DELIVERED, DELIVERY_RCV)
                .transition(DELIVERED, RETRIEVED, RETRIEVAL_RCV)
//                .throwOnTransition()
                .throwOnNotAllowedTransition((s, e) -> new DCEvidenceProcessingException(ErrorCode.EVIDENCE_IGNORED, String.format("Cannot move with event [%s] from current state [%s]", s, e)))
                .build();
    }

    public DC5BusinessMessageStateMachine loadStateMachine(DC5BusinessMessageState dc5BusinessMessageState) {
        return new DC5BusinessMessageStateMachine(dc5BusinessMessageState);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class DC5BusinessMessageStateMachine {

        private final DC5BusinessMessageState currentState;

        public DC5BusinessMessageState publishEvent(DC5BusinessMessageState.BusinessMessageEvents event, DC5Confirmation dc5Confirmation) {
            Optional<DC5BusinessMessageState.BusinessMessagesStates> state = stateTransitionChecker.doTransition(currentState.getState(), event);
            if (state.isPresent()) {
                return DC5BusinessMessageState.builder()
                        .state(state.get())
                        .created(LocalDateTime.now())
                        .event(event)
                        .confirmation(dc5Confirmation)
                        .build();
            }
            throw new IllegalStateException(String.format("The transition from [%s] to [%s] is not possible!", state, event));
        }

    }
}
