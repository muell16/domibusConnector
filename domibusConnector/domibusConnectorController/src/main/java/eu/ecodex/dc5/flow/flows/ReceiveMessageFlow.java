package eu.ecodex.dc5.flow.flows;

import eu.domibus.connector.controller.exception.ErrorCode;
import eu.ecodex.dc5.core.model.DC5MsgProcess;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.flow.api.DC5TransformToDomain;
import eu.ecodex.dc5.flow.api.StepFailedException;
import eu.ecodex.dc5.flow.events.NewMessageStoredEvent;
import eu.ecodex.dc5.flow.steps.DC5SaveMessageStep;
import eu.ecodex.dc5.flow.steps.DC5TransformMessageStep;
import eu.ecodex.dc5.process.MessageProcessManager;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.style.ToStringCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ReceiveMessageFlow {

    private final DC5TransformMessageStep transformMessageStep;
    private final DC5SaveMessageStep saveMessageStep;
    private final ApplicationEventPublisher eventPublisher;

    private final MessageProcessManager messageProcessManager;

    @Transactional
    public <T> ReceiveMessageFlowResult receiveMessage(T message, DC5TransformToDomain<T> transform) {
        try (MessageProcessManager.CloseableMessageProcess process = messageProcessManager.startProcess();){
            DC5Message msg = transformMessageStep.transformMessage(message, transform);
            DC5Message dc5Msg = saveMessageStep.saveNewMessage(msg);
            eventPublisher.publishEvent(NewMessageStoredEvent.of(dc5Msg.getId()));
            return ReceiveMessageFlowResult.getSuccess();
        } catch (StepFailedException stepFailedException) {
            return new ReceiveMessageFlowResult(false,Optional.empty(), Optional.of(stepFailedException));
        } finally {

        }

    }

    @Getter
    @RequiredArgsConstructor
    public static class ReceiveMessageFlowResult {
        private final boolean success;
        @NonNull private final Optional<ErrorCode> errorCodeOptional;
        @NonNull private final Optional<Throwable> error;

        public static ReceiveMessageFlowResult getSuccess() {
            return new ReceiveMessageFlowResult(true, Optional.empty(), Optional.empty());
        }

        public boolean isSuccess() {
            return this.success;
        }

        @Override
        public String toString() {
            return new ToStringCreator(this)
                    .append("success", this.success)
                    .append("errorCode", this.errorCodeOptional.orElse(null))
                    .append("error", this.error.orElse(null))
                    .toString();
        }
    }



}
