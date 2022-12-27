package eu.domibus.connector.controller.service;

import eu.domibus.connector.controller.exception.ErrorCode;
import eu.ecodex.dc5.core.model.DC5MsgProcess;
import eu.ecodex.dc5.message.model.DC5Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.style.ToStringCreator;

import javax.annotation.CheckForNull;
import java.util.Optional;

/**
 * Generic interface to submit a message to the connector
 * from any link modules (gwjmsplugin, ...)
 */
public interface SubmitToConnector {


    @Getter
    public static class ReceiveMessageFlowResult {
        private final boolean success;
        @CheckForNull
        private final ErrorCode errorCode;
        @CheckForNull private final Throwable error;
        @CheckForNull private final DC5Message message;

        @Builder(toBuilder = true)
        public static ReceiveMessageFlowResult getSuccess(DC5Message message) {
            return new ReceiveMessageFlowResult(true, null, null, message);
        }

        @Builder(toBuilder = true)
        public ReceiveMessageFlowResult(boolean success, ErrorCode errorCode, Throwable error, DC5Message message) {
            this.success = success;
            this.errorCode = errorCode;
            this.error = error;
            this.message = message;
            if (success && message == null) {
                throw new IllegalArgumentException("If result is success param message cannot be null!");
            }
        }

        public boolean isSuccess() {
            return this.success;
        }

        public Optional<Throwable> getError() {
            return Optional.ofNullable(error);
        }

        public Optional<ErrorCode> getErrorCode() {
            return Optional.ofNullable(errorCode);
        }

        @Override
        public String toString() {
            return new ToStringCreator(this)
                    .append("success", this.success)
                    .append("errorCode", this.errorCode)
                    .append("error", this.error)
                    .toString();
        }
    }



    public <T> ReceiveMessageFlowResult receiveMessage(T message, DC5TransformToDomain<T> transform);

    interface DC5TransformToDomain<T> {
        DC5Message transform(T msg, DC5MsgProcess msgProcess) throws TransformMessageException;
    }

    class TransformMessageException extends RuntimeException {
        public TransformMessageException() {
        }

        public TransformMessageException(String message) {
            super(message);
        }

        public TransformMessageException(String message, Throwable cause) {
            super(message, cause);
        }

        public TransformMessageException(Throwable cause) {
            super(cause);
        }

    }
}
