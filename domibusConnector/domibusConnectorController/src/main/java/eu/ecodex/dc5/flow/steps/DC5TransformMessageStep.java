package eu.ecodex.dc5.flow.steps;

import ec.ecodex.dc5.process.MessageProcessManager;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.ecodex.dc5.core.model.DC5Msg;
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
    public <T> DomibusConnectorMessage transformMessage(T message, DC5TransformToDomain<T> transformer) {
        DC5MsgProcess currentProcess = messageProcessManager.getCurrentProcess();
        return transformer.transform(message, currentProcess);
    }

}
