package eu.ecodex.dc5.flow.aspect;

import eu.ecodex.dc5.process.MessageProcessManager;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.ecodex.dc5.core.model.DC5ProcessStep;
import eu.ecodex.dc5.flow.api.Step;
import eu.ecodex.dc5.flow.api.StepFailedException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Aspect
@Component
@Order(0)
@Log4j2
public class StepAspect {

    private final PlatformTransactionManager txManager;

    private final MessageProcessManager messageProcessManager;

    public StepAspect(PlatformTransactionManager txManager,
                      MessageProcessManager messageProcessManager) {
        this.txManager = txManager;
        this.messageProcessManager = messageProcessManager;
    }


    @Around(value="@annotation(eu.ecodex.dc5.flow.api.Step) && @annotation(stepAnnotation)", argNames="stepAnnotation")
    public Object handleStep(ProceedingJoinPoint pjp, Step stepAnnotation) throws Throwable {
        org.slf4j.MDC.put(LoggingMDCPropertyNames.MDC_DC_STEP_PROCESSOR_PROPERTY_NAME, stepAnnotation.name());
        try {
            return handleStepCreation(pjp, stepAnnotation);
        } finally {
            org.slf4j.MDC.remove(LoggingMDCPropertyNames.MDC_DC_STEP_PROCESSOR_PROPERTY_NAME);
        }
    }

    private Object handleStepCreation(ProceedingJoinPoint pjp, Step stepAnnotation) throws Throwable {
        DC5ProcessStep processStep = messageProcessManager.startStep(stepAnnotation.name());

        try {
            log.trace("executing step: [{}]", stepAnnotation.name());
            Object obj = pjp.proceed();
            processStep.setSuccess(true);
            log.trace("executing step: [{}] :: success", stepAnnotation.name());
            return obj;
        } catch (Throwable e) {
            processStep.setSuccess(false);
            processStep.setLongErrorText(ExceptionUtils.getStackTrace(e));
            processStep.setError(e.getMessage());
            log.trace("executing step: [{}] :: failure", stepAnnotation.name());
            throw new StepFailedException("Failed step: [" + stepAnnotation.name() + "]", e);
        }

    }

}
