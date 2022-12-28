package eu.ecodex.dc5.process;

import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.ecodex.dc5.message.model.DC5MessageId;
import eu.ecodex.dc5.process.model.DC5MsgProcess;
import eu.ecodex.dc5.process.model.DC5ProcessStep;
import eu.ecodex.dc5.process.repository.DC5MsgProcessRepo;
import org.springframework.stereotype.Service;

import java.io.Closeable;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageProcessManager {

    private final ThreadLocal<DC5MsgProcess> threadLocalMsgProcess = new ThreadLocal<>();

    private final DC5MsgProcessRepo msgProcessRepo;
    private final DomibusConnectorMessageIdGenerator messageIdGenerator;

    public MessageProcessManager(DC5MsgProcessRepo msgProcessRepo) {
        this.msgProcessRepo = msgProcessRepo;
        this.messageIdGenerator = () -> DC5MessageId.ofString(UUID.randomUUID().toString());
    }

    private CloseableMessageProcess setCurrentProcessId(DC5MsgProcess p) {
        threadLocalMsgProcess.set(p);
        return new CloseableMessageProcess() {
            @Override
            public void close() {
                threadLocalMsgProcess.remove();
            }

            @Override
            public DC5MsgProcess getProcess() {
                return threadLocalMsgProcess.get();
            }
        };
    }


    public CloseableMessageProcess startProcess() {
        MessageProcessId messageProcessId = MessageProcessId.ofString(messageIdGenerator.generateDomibusConnectorMessageId().toString());
        DC5MsgProcess dc5MsgProcess = new DC5MsgProcess();
        dc5MsgProcess.setProcessId(messageProcessId);
        threadLocalMsgProcess.set(dc5MsgProcess);
        dc5MsgProcess.setCreated(LocalDateTime.now());
        DC5MsgProcess p = msgProcessRepo.save(dc5MsgProcess);
        return setCurrentProcessId(p);
    }


    public DC5ProcessStep startStep(String name) {
        DC5ProcessStep processStep = new DC5ProcessStep();
        processStep.setCreated(LocalDateTime.now());
        processStep.setStepName(name);
        DC5MsgProcess dc5MsgProcess = getCurrentProcess();
//        if (dc5MsgProcess == null) {
//            throw new IllegalStateException("No message process is currently active in this thread! You must start one first with MessageProcessManager.startProcess");
//        }
//        processStepRepo.save(processStep);
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
        return getCurrentProcess().getProcessId();
    }

    public CloseableMessageProcess resumeProcess(MessageProcessId messageProcessId) {

        Optional<DC5MsgProcess> byProcessId = msgProcessRepo.findByProcessId(messageProcessId);
        if (byProcessId.isPresent()) {
            return setCurrentProcessId(byProcessId.get());
        } else {
            String error = String.format("Cannot find process with id [%s] in DB", messageProcessId);
            throw new IllegalStateException(error);
        }
    }

    public static interface CloseableMessageProcess extends Closeable {
        public void close();

        public DC5MsgProcess getProcess();
    }

}




