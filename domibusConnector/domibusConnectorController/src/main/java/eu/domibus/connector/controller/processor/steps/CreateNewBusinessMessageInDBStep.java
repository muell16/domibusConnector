package eu.domibus.connector.controller.processor.steps;

import eu.ecodex.dc5.message.model.DC5Message;
import eu.domibus.connector.lib.logging.MDC;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import eu.domibus.connector.tools.logging.LoggingMarker;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateNewBusinessMessageInDBStep implements MessageProcessStep {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateNewBusinessMessageInDBStep.class);

//    private final DCMessagePersistenceService messagePersistenceService;


    @Override
    @MDC(name = LoggingMDCPropertyNames.MDC_DC_STEP_PROCESSOR_PROPERTY_NAME, value = "CreateNewBusinessMessageInDBStep")
    public boolean executeStep(DC5Message DC5Message) {
//        messagePersistenceService.persistBusinessMessageIntoDatabase(DC5Message);
//        LOGGER.debug(LoggingMarker.BUSINESS_LOG, "Successfully created (uncommitted) new business message in database");
        return true;
    }

}
