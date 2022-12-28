package eu.ecodex.dc5.flow.steps;

import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.ecodex.dc5.process.MessageProcessManager;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.core.model.DC5MsgProcess;
import eu.ecodex.dc5.flow.api.Step;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DC5TransformMessageStep {

    @NonNull
    public MessageProcessManager messageProcessManager;

    @Step(name = "ConvertMessageStep")
    public <T> DC5Message transformMessage(T message, SubmitToConnector.DC5TransformToDomain<T> transformer) {
        DC5MsgProcess currentProcess = messageProcessManager.getCurrentProcess();
        return transformer.transform(message, currentProcess);
    }

}
