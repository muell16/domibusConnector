package eu.ecodex.dc5.statemachine;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import javax.annotation.CheckForNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StateTransitionChecker<S, E> {

    public static class StateTransitionCheckerBuilder<S, E> {
        private List<StateTransition<S, E>> transitions = new ArrayList<>();
        private List<FailStateTransition<S, E>> failTransitions = new ArrayList<>();
        private S initialState;
        private TransitionExceptionSupplier<S, E> transitionExceptionSupplier;

        public StateTransitionCheckerBuilder<S, E> transition(S s1, S s2, E e) {
            transitions.add(new StateTransition<>(s1, s2, e));
            return this;
        }

        public StateTransitionCheckerBuilder<S, E> throwOnTransition(S s1, E e, RuntimeException ecx) {
            failTransitions.add(new FailStateTransition<>(s1, e, ecx));
            return this;
        }

        public StateTransitionCheckerBuilder<S, E> initialState(S s) {
            this.initialState = s;
            return this;
        }

        public StateTransitionChecker<S, E> build() {
            return new StateTransitionChecker<>(this.transitions, this.failTransitions, this.initialState, this.transitionExceptionSupplier);
        }

        public StateTransitionCheckerBuilder<S, E> throwOnNotAllowedTransition(TransitionExceptionSupplier<S, E> exceptionSupplier) {
            this.transitionExceptionSupplier = exceptionSupplier;
            return this;
        }
    }

    public static interface TransitionExceptionSupplier<S, E> {
        public RuntimeException supply(S source, E event);
    }


    public static <S, E> StateTransitionCheckerBuilder<S, E> builder() {
        return new StateTransitionCheckerBuilder<>();
    }

    @Getter
    public static class StateTransition<S, E> {

        private final S source;
        private final E event;
        private final S target;

        public StateTransition(S source, S target, E event) {
            this.source = source;
            this.event = event;
            this.target = target;
        }

        public boolean allowsTransition(S state, E event) {
            return (this.source.equals(state) && this.event.equals(event));
        }
    }

    @Getter
    public static class FailStateTransition<S, E> {
        private final S source;
        private final E event;
        private final RuntimeException exception;

        public FailStateTransition(S source, E event, RuntimeException exception) {
            this.source = source;
            this.event = event;
            this.exception = exception;
        }

        public boolean allowsTransition(S state, E event) {
            return (this.source.equals(state) && this.event.equals(event));
        }
    }

    private final List<StateTransition<S, E>> transitions;
    private final List<FailStateTransition<S, E>> failTransitions;

    @NonNull
    private S initialState;
    @CheckForNull
    private TransitionExceptionSupplier<S, E> transitionExceptionSupplier;

    public Optional<S> doTransition(S currentState, E event) {

        Optional<S> any = transitions.stream()
                .filter(t -> t.allowsTransition(currentState, event))
                .map(s -> s.target)
                .findAny();
        if (any.isPresent()) {
            return any;
        }

        Optional<RuntimeException> ecx = failTransitions.stream()
                .filter(t -> t.allowsTransition(currentState, event))
                .map(s -> s.exception)
                .findAny();
        if (ecx.isPresent()) {
            throw ecx.get();
        }

        if (transitionExceptionSupplier != null) {
            throw transitionExceptionSupplier.supply(currentState, event);
        }
        return Optional.empty();
    }



}
