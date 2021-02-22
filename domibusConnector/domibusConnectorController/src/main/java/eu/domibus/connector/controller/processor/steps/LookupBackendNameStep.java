package eu.domibus.connector.controller.processor.steps;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * This step looks up the correct backend name
 *
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LookupBackendNameStep implements MessageProcessStep {


    @Override
    public boolean executeStep(DomibusConnectorMessage domibusConnectorMessage) {

        return true;
    }

}
