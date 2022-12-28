package eu.ecodex.dc5.flow.flows;

import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.flow.api.StepFailedException;
import eu.ecodex.dc5.flow.events.NewMessageStoredEvent;
import eu.ecodex.dc5.flow.steps.DC5SaveMessageStep;
import eu.ecodex.dc5.flow.steps.DC5TransformMessageStep;
import eu.ecodex.dc5.process.MessageProcessManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ReceiveMessageFlow implements SubmitToConnector {

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
            return ReceiveMessageFlowResult.getSuccess(msg);
        } catch (StepFailedException stepFailedException) {
            return new ReceiveMessageFlowResult(false,null, stepFailedException, null);
        } finally {

        }

    }





}
