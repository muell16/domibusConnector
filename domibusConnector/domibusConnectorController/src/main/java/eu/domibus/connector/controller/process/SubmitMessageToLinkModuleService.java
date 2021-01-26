package eu.domibus.connector.controller.process;

import eu.domibus.connector.controller.exception.DomibusConnectorBackendException;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.exception.handling.StoreMessageExceptionIntoDatabase;
import eu.domibus.connector.controller.service.DomibusConnectorBackendDeliveryService;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.MessageTargetSource;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubmitMessageToLinkModuleService {

    private static final Logger LOGGER = LogManager.getLogger(SubmitMessageToLinkModuleService.class);

    public static final class DCSubmitMessageToLinkException extends DomibusConnectorControllerException {
        public DCSubmitMessageToLinkException() {
        }

        public DCSubmitMessageToLinkException(String arg0) {
            super(arg0);
        }

        public DCSubmitMessageToLinkException(Throwable arg0) {
            super(arg0);
        }

        public DCSubmitMessageToLinkException(String arg0, Throwable arg1) {
            super(arg0, arg1);
        }
    }

    @Autowired
    DomibusConnectorGatewaySubmissionService gatewaySubmissionService;

    @Autowired
    private DomibusConnectorBackendDeliveryService backendDeliveryService;

//    @StoreMessageExceptionIntoDatabase(passException = true)
    public void submitMessage(DomibusConnectorMessage message) {

        try {
            MessageTargetSource target = message.getMessageDetails().getDirection().getTarget();
            if (target == MessageTargetSource.GATEWAY) {
                LOGGER.debug("Message target is BACKEND, calling backend delivery service");
                gatewaySubmissionService.submitToGateway(message);
            } else if (target == MessageTargetSource.BACKEND) {
                LOGGER.debug("Message target is BACKEND, calling backend delivery service");
                backendDeliveryService.deliverMessageToBackend(message);
            }
        } catch (DomibusConnectorGatewaySubmissionException e) {
            String error = "Submitting message to gatewaySubmissionService failed!";
           throw new DCSubmitMessageToLinkException(error, e);
        } catch (DomibusConnectorBackendException ex) {
            String error = "Submitting message to backendDeliveryService failed!";
            throw new DCSubmitMessageToLinkException(error, ex);
        }
    }

}
