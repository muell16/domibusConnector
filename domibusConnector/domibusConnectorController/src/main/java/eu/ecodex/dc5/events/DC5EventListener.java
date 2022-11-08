package eu.ecodex.dc5.events;

import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@EventListener(condition = "#root.event.processed")
@Transactional(propagation = Propagation.REQUIRES_NEW)
public @interface DC5EventListener {
}
