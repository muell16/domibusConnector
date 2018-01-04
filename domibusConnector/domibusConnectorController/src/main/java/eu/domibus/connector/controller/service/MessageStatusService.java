package eu.domibus.connector.controller.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import eu.domibus.connector.common.gwc.DomibusConnectorGatewayWebserviceClientException;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageStatusService extends AbstractMessageService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageStatusService.class);
    
    public String requestMessageStatusFromGateway(DomibusConnectorMessage message) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException {
        if (message == null
                || message.getMessageDetails() == null
                || (StringUtils.isEmpty(message.getMessageDetails().getEbmsMessageId()) && StringUtils.isEmpty(message
                        .getMessageDetails().getBackendMessageId()))) {
            throw new DomibusConnectorControllerException(
                    "Backend webservice method getMessageStatusOnGateway cannont be called if no message id (ebms message id of national message id) is set!");
        }

        if (StringUtils.isEmpty(message.getMessageDetails().getEbmsMessageId())
                && !StringUtils.isEmpty(message.getMessageDetails().getBackendMessageId())) {
            //message = persistenceService.findMessageByNationalId(message.getMessageDetails().getNationalMessageId());
            message = persistenceService.findMessageByNationalId(message.getMessageDetails().getBackendMessageId());
        }

//replace with new gateway handling!        
//        try {
//            return gatewayWebserviceClient.getMessageStatusOnGateway(message);
//        } catch (DomibusConnectorGatewayWebserviceClientException e) {
//            throw new DomibusConnectorMessageException(message,
//                    "Exception calling backend webservice method getMessageStatusOnGateway for message id "
//                            + message.getMessageDetails().getEbmsMessageId(), e, this.getClass());
//        }
        return "emtpy";
    }

    public List<DomibusConnectorMessageError> requestMessageErrors(DomibusConnectorMessage message) throws DomibusConnectorMessageException,
            DomibusConnectorControllerException {

        if (message == null
                || message.getMessageDetails() == null
                || (StringUtils.isEmpty(message.getMessageDetails().getEbmsMessageId()) && StringUtils.isEmpty(message
                        .getMessageDetails().getBackendMessageId()))) {
            throw new DomibusConnectorControllerException(
                    "MessageErrors cannont be loaded if no message id (ebms message id of national message id) is set!");
        }

        if (!StringUtils.isEmpty(message.getMessageDetails().getEbmsMessageId())) {
            message = persistenceService.findMessageByEbmsId(message.getMessageDetails().getEbmsMessageId());
        }

        if (StringUtils.isEmpty(message.getMessageDetails().getEbmsMessageId())
                && !StringUtils.isEmpty(message.getMessageDetails().getBackendMessageId())) {
            message = persistenceService.findMessageByNationalId(message.getMessageDetails().getBackendMessageId());
        }

// TODO: replace with new GW connection        
//        try {
//            gatewayWebserviceClient.getMessageErrorsFromGateway(message);
//        } catch (DomibusConnectorGatewayWebserviceClientException e) {
//            throw new DomibusConnectorMessageException(message,
//                    "Exception calling backend webservice method getMessageErrorsFromGateway for message id "
//                            + message.getMessageDetails().getEbmsMessageId(), e, this.getClass());
//        }

        try {
            List<DomibusConnectorMessageError> msgErrors = persistenceService.getMessageErrors(message);
            return msgErrors;
        } catch (Exception e) {
            LOGGER.error("Exception occured", e);            
            //TODO: logging!
        }

        return null;
    }
}
