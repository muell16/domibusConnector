package eu.ecodex.dc5.flow;

import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.ecodex.dc.core.model.DC5Msg;
import eu.ecodex.dc.core.model.DC5MsgProcess;
import eu.ecodex.dc.core.model.DC5ProcessStep;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

/**
 * This service is responsible for starting a Message Process
 */
@Service
@RequiredArgsConstructor
public class DC5ReceiveMessageStep {

    private final DomibusConnectorMessageIdGenerator messageIdGenerator;
    private final EntityManager entityManager;
    private final TransactionTemplate txTemplate;

    private final ApplicationEventPublisher events;


    private DC5MsgProcess startProcess(String processId) {
        DC5MsgProcess dc5MsgProcess = new DC5MsgProcess();
        dc5MsgProcess.setProcessId(processId);
        dc5MsgProcess.setCreated(LocalDateTime.now());
        entityManager.persist(dc5MsgProcess);
        return dc5MsgProcess;
    }

    @Transactional
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_STEP_PROCESSOR_PROPERTY_NAME, value = "DC5ReceiveMessageStep")
    public <T> DC5ProcessStep receiveMessage(T dtoMessage, Transformer<T, DC5Msg> messageTransformer) {
        String processId = messageIdGenerator.generateDomibusConnectorMessageId().getConnectorMessageId();
        try (org.slf4j.MDC.MDCCloseable closeable = LoggingMDCPropertyNames.putMdc(LoggingMDCPropertyNames.MDC_DC5_MSG_PROCESS_ID, processId)) {
            final DC5MsgProcess msgProcess = startProcess(processId);

            final DC5ProcessStep procStep = new DC5ProcessStep();
            procStep.setStepName(DC5ProcessStep.STEPS.RECEIVE_STEP.name());
            procStep.setCreated(LocalDateTime.now());
            entityManager.persist(procStep);

            txTemplate.execute(status -> {
                try {
                    DC5Msg dbMsg = messageTransformer.transform(dtoMessage, msgProcess);
                    entityManager.persist(dbMsg);
                    procStep.setMessageResult(dbMsg);
                } catch (PersistenceException persistenceException) {
                    procStep.setError(persistenceException.getMessage());
                } catch (TransformMessageException transformMessageException) {
                    procStep.setError(transformMessageException.getMessage());
                    procStep.setErrorText(ExceptionUtils.getStackTrace(transformMessageException));
                }
                return null;
            });
            return procStep;
        }
    }



    public class TransformMessageException extends RuntimeException {
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


    public interface Transformer<T, DC5Msg>  {
        DC5Msg transform(T msg, DC5MsgProcess msgProcess) throws TransformMessageException;
    }

}
