package ec.ecodex.dc5.process;

import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.ecodex.dc5.core.model.DC5MsgProcess;
import eu.ecodex.dc5.core.model.DC5ProcessStep;
import eu.ecodex.dc5.core.repository.DC5MsgProcessRepo;
import org.springframework.stereotype.Service;

import java.io.Closeable;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MessageProcessManager {

    static ThreadLocal<DC5MsgProcess> threadLocalMsgProcess;

    private final DC5MsgProcessRepo msgProcessRepo;
    private final DomibusConnectorMessageIdGenerator messageIdGenerator;

    public MessageProcessManager(DC5MsgProcessRepo msgProcessRepo, DomibusConnectorMessageIdGenerator messageIdGenerator) {
        this.msgProcessRepo = msgProcessRepo;
        this.messageIdGenerator = messageIdGenerator;
    }

    public static MessageProcessId getCurrentProcessId() {
        return new MessageProcessId(threadLocalMsgProcess.get().getProcessId());
    }

    private CloseableMessageProcess setCurrentProcessId(DC5MsgProcess p) {
        threadLocalMsgProcess.set(p);
        return () -> threadLocalMsgProcess.remove();
    }


    public DC5MsgProcess startProcess() {
        MessageProcessId messageProcessId = new MessageProcessId(messageIdGenerator.generateDomibusConnectorMessageId().toString());
        DC5MsgProcess dc5MsgProcess = new DC5MsgProcess();
        dc5MsgProcess.setProcessId(messageProcessId.getProcessId());
        threadLocalMsgProcess.set(dc5MsgProcess);
        dc5MsgProcess.setCreated(LocalDateTime.now());
        return msgProcessRepo.save(dc5MsgProcess);
    }

//    public DC5ProcessStep startStep(DC5MsgProcess dc5MsgProcess, Class<?> step) {
//        DC5ProcessStep processStep = new DC5ProcessStep();
//        processStep.setCreated(LocalDateTime.now());
//        processStep.setStepName(step.getName());
//        dc5MsgProcess.addProcessStep(processStep);
//        return processStep;
//    }

//    public void finishStep(DC5ProcessStep step) {
//
//    }

    public DC5ProcessStep startStep(String name) {
        DC5ProcessStep processStep = new DC5ProcessStep();
        processStep.setCreated(LocalDateTime.now());
        processStep.setStepName(name);
        DC5MsgProcess dc5MsgProcess = getCurrentProcess();
//        if (dc5MsgProcess == null) {
//            throw new IllegalStateException("No message process is currently active in this thread! You must start one first with MessageProcessManager.startProcess");
//        }
        dc5MsgProcess.addProcessStep(processStep);
        return processStep;
    }

    public DC5MsgProcess getCurrentProcess() {

        DC5MsgProcess dc5MsgProcess = threadLocalMsgProcess.get();
        if (dc5MsgProcess == null) {
            throw new IllegalStateException("No message process is currently active in this thread! You must start one first with MessageProcessManager.startProcess");
        }
        return dc5MsgProcess;
    }

    public MessageProcessId getCurrentMessageProcesssId() {
        return new MessageProcessId(getCurrentProcess().getProcessId());
    }

    public CloseableMessageProcess resumeProcess(MessageProcessId messageProcessId) {

        Optional<DC5MsgProcess> byProcessId = msgProcessRepo.findByProcessId(messageProcessId.getProcessId());
        if (byProcessId.isPresent()) {
            return setCurrentProcessId(byProcessId.get());
        } else {
            String error = String.format("Cannot find process with id [%s] in DB", messageProcessId);
            throw new IllegalStateException(error);
        }
    }

    public static interface CloseableMessageProcess extends Closeable {
        public void close();
    }

}




