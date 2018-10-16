package eu.domibus.connector.lib.logging.aspects;

import eu.domibus.connector.lib.logging.MDC;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MDCSetterAspect {

    @Around(value="@annotation(eu.domibus.connector.lib.logging.MDC) && @annotation(mdcAnnotation)", argNames="mdcAnnotation")
    public void handleMdc(ProceedingJoinPoint pjp, MDC mdcAnnotation) throws Throwable {
        org.slf4j.MDC.put(mdcAnnotation.name(), mdcAnnotation.value());
        try {
            pjp.proceed();
        } finally {
            org.slf4j.MDC.remove(mdcAnnotation.name());
        }
    }

}
