package eu.ecodex.dc5.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.ecodex.dc5.process.MessageProcessId;
import eu.ecodex.dc5.process.MessageProcessManager;
import eu.ecodex.dc5.events.model.JPAEventStorageItem;
import eu.ecodex.dc5.events.repo.DC5EventStorageItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
@Log4j2
public class DC5EventManager {

    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final DC5EventStorageItemRepository eventStorage;
    private final MessageProcessManager messageProcessManager;

    private final PlatformTransactionManager platformTransactionManager;

    private final Executor executor = new SimpleAsyncTaskExecutor(); //TODO: create configuration for that!
    private TransactionTemplate txTemplate;

    @PostConstruct
    public void init() {
        this.txTemplate = new TransactionTemplate(platformTransactionManager);
    }

    @EventListener(condition = "#event.processed == false")
    public void publishEvent(DC5Event event) {

        event.setProcessed(false);
        event.setMessageProcessId(messageProcessManager.getCurrentMessageProcesssId());

        try {
            JPAEventStorageItem dbEvent = new JPAEventStorageItem();
            dbEvent.setCreated(LocalDateTime.now());
            dbEvent.setEvent(objectMapper.writeValueAsString(event));
            eventStorage.save(dbEvent);

            eventPublisher.publishEvent(new EventSavedEvent(dbEvent, event));

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Event must be serializable", e);
        }

    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEventAfterCommit(EventSavedEvent event) {
        //TODO: push to JMS (only if HA!)
        //OR JUST RUN IN OTHER EXECUTOR!
        //MAKE CONFIGURABLE BY BEAN!
        executor.execute(() -> {
            this.republishEvent(event);
        });
    }

    private void republishEvent(EventSavedEvent event) {
        //Process event

        txTemplate.executeWithoutResult(txState -> doRepublishEventTransactional(txState, event));

    }

    private void doRepublishEventTransactional(TransactionStatus txState, EventSavedEvent event) {
        DC5Event originalEvent = event.originalEvent;
        MessageProcessId messageProcessId = originalEvent.getMessageProcessId();

        try (MessageProcessManager.CloseableMessageProcess closeableMessageProcess = messageProcessManager.resumeProcess(messageProcessId)) {

            originalEvent.setProcessed(true);
            eventStorage.setConsumed(event.savedEvent.getId());


            try {
                //publish event again to spring....
                eventPublisher.publishEvent(originalEvent);
            } catch (Throwable e) {
                String error = String.format("Error occured cannot continue processing event [%s]", originalEvent);
                log.error(error, e);
                //TODO: add exception handler here!
                //ERROR handling...
                //?? exception behaviour?
                //mark as processed!?

            }
        }
    }

    @RequiredArgsConstructor
    public static class EventSavedEvent {
        private final JPAEventStorageItem savedEvent;
        private final DC5Event originalEvent;
    }



}
