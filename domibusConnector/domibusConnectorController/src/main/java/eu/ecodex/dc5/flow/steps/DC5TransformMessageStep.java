package eu.ecodex.dc5.flow.steps;

import eu.ecodex.dc5.process.MessageProcessManager;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.core.model.DC5MsgProcess;
import eu.ecodex.dc5.flow.api.DC5TransformToDomain;
import eu.ecodex.dc5.flow.api.Step;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DC5TransformMessageStep {

    public MessageProcessManager messageProcessManager;

    @Step(name = "ConvertMessageStep")
    public <T> DC5Message transformMessage(T message, DC5TransformToDomain<T> transformer) {
        DC5MsgProcess currentProcess = messageProcessManager.getCurrentProcess();
        return transformer.transform(message, currentProcess);
    }

}
