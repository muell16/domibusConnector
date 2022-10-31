package eu.ecodex.dc5.flow;

import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.ecodex.dc.core.model.DC5Msg;
import eu.ecodex.dc.core.model.DC5MsgProcess;
import eu.ecodex.dc.core.model.DC5ProcessStep;
import eu.ecodex.dc.core.repository.DC5MessageRepo;
import eu.ecodex.dc.core.repository.DC5MsgProcessRepo;
import eu.ecodex.dc.core.repository.DC5ProcessStepRepo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

/**
 * This service is responsible for starting a Message Process
 */
@Service
public class DC5ReceiveMessageStep {

    private final DomibusConnectorMessageIdGenerator messageIdGenerator;
    private final TransactionTemplate txTemplate;
    private final DC5ProcessStepRepo processStepRepo;
    private final DC5MsgProcessRepo msgProcessRepo;
    private final DC5MessageRepo messageRepo;

    public DC5ReceiveMessageStep(DomibusConnectorMessageIdGenerator messageIdGenerator,
                                 DC5ProcessStepRepo processStepRepo,
                                 PlatformTransactionManager txManager,
                                 DC5MsgProcessRepo msgProcessRepo,
                                 DC5MessageRepo messageRepo) {
        this.messageIdGenerator = messageIdGenerator;

        this.processStepRepo = processStepRepo;
        this.msgProcessRepo = msgProcessRepo;
        this.messageRepo = messageRepo;

        this.txTemplate = new TransactionTemplate(txManager);
        txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }


    private DC5MsgProcess startProcess(String processId) {
        DC5MsgProcess dc5MsgProcess = new DC5MsgProcess();
        dc5MsgProcess.setProcessId(processId);
        dc5MsgProcess.setCreated(LocalDateTime.now());
        return msgProcessRepo.save(dc5MsgProcess);
    }

    @Transactional
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_STEP_PROCESSOR_PROPERTY_NAME, value = "DC5ReceiveMessageStep")
    public <T> DC5ProcessStep receiveMessage(T dtoMessage, Transformer<T, DC5Msg> messageTransformer) {
        String processId = messageIdGenerator.generateDomibusConnectorMessageId().getConnectorMessageId();
        try (org.slf4j.MDC.MDCCloseable closeable = LoggingMDCPropertyNames.putMdc(LoggingMDCPropertyNames.MDC_DC5_MSG_PROCESS_ID, processId)) {
            final DC5MsgProcess msgProcess = startProcess(processId);

            final DC5ProcessStep savedProcessStep = createProcessStep(msgProcess);
            msgProcess.addProcessStep(savedProcessStep);


            try {
                txTemplate.execute(status -> {
                    DC5Msg dbMsg = messageRepo.save(messageTransformer.transform(dtoMessage, msgProcess));
                    savedProcessStep.setMessageResult(dbMsg);
                    return null;
                });
            } catch (DataAccessException persistenceException) {
                savedProcessStep.setError(persistenceException.getMessage());
            } catch (TransformMessageException transformMessageException) {
                savedProcessStep.setError(transformMessageException.getMessage());
                savedProcessStep.setLongErrorText(ExceptionUtils.getStackTrace(transformMessageException));
            }

            return savedProcessStep;
        }
    }

    private DC5ProcessStep createProcessStep(DC5MsgProcess msgProcess) {
        final DC5ProcessStep pStep = new DC5ProcessStep();
        pStep.setStepName(DC5ProcessStep.STEPS.RECEIVE_STEP.name());
        pStep.setCreated(LocalDateTime.now());
        return processStepRepo.save(pStep);
    }


    public static class TransformMessageException extends RuntimeException {
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
