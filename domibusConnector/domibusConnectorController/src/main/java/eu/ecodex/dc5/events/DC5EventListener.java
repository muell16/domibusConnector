package eu.ecodex.dc5.events;

import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EventListener(condition = "#root.event.payload.processed == true")
@Transactional(propagation = Propagation.REQUIRES_NEW)
public @interface DC5EventListener {
}
