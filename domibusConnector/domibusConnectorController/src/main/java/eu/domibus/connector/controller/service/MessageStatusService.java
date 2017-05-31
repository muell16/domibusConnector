package eu.domibus.connector.controller.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import eu.domibus.connector.common.exception.DomibusConnectorMessageException;
import eu.domibus.connector.common.gwc.DomibusConnectorGatewayWebserviceClientException;
import eu.domibus.connector.common.message.Message;
import eu.domibus.connector.common.message.MessageError;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;

public class MessageStatusService extends AbstractMessageService {

    public String requestMessageStatusFromGateway(Message message) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException {
        if (message == null
                || message.getMessageDetails() == null
                || (StringUtils.isEmpty(message.getMessageDetails().getEbmsMessageId()) && StringUtils.isEmpty(message
                        .getMessageDetails().getNationalMessageId()))) {
            throw new DomibusConnectorControllerException(
                    "Backend webservice method getMessageStatusOnGateway cannont be called if no message id (ebms message id of national message id) is set!");
        }

        if (StringUtils.isEmpty(message.getMessageDetails().getEbmsMessageId())
                && !StringUtils.isEmpty(message.getMessageDetails().getNationalMessageId())) {
            message = persistenceService.findMessageByNationalId(message.getMessageDetails().getNationalMessageId());
        }

        try {
            return gatewayWebserviceClient.getMessageStatusOnGateway(message);
        } catch (DomibusConnectorGatewayWebserviceClientException e) {
            throw new DomibusConnectorMessageException(message,
                    "Exception calling backend webservice method getMessageStatusOnGateway for message id "
                            + message.getMessageDetails().getEbmsMessageId(), e, this.getClass());
        }
    }

    public List<MessageError> requestMessageErrors(Message message) throws DomibusConnectorMessageException,
            DomibusConnectorControllerException {

        if (message == null
                || message.getMessageDetails() == null
                || (StringUtils.isEmpty(message.getMessageDetails().getEbmsMessageId()) && StringUtils.isEmpty(message
                        .getMessageDetails().getNationalMessageId()))) {
            throw new DomibusConnectorControllerException(
                    "MessageErrors cannont be loaded if no message id (ebms message id of national message id) is set!");
        }

        if (!StringUtils.isEmpty(message.getMessageDetails().getEbmsMessageId())) {
            message = persistenceService.findMessageByEbmsId(message.getMessageDetails().getEbmsMessageId());
        }

        if (StringUtils.isEmpty(message.getMessageDetails().getEbmsMessageId())
                && !StringUtils.isEmpty(message.getMessageDetails().getNationalMessageId())) {
            message = persistenceService.findMessageByNationalId(message.getMessageDetails().getNationalMessageId());
        }

        try {
            gatewayWebserviceClient.getMessageErrorsFromGateway(message);
        } catch (DomibusConnectorGatewayWebserviceClientException e) {
            throw new DomibusConnectorMessageException(message,
                    "Exception calling backend webservice method getMessageErrorsFromGateway for message id "
                            + message.getMessageDetails().getEbmsMessageId(), e, this.getClass());
        }

        try {
            List<MessageError> msgErrors = persistenceService.getMessageErrors(message);
            return msgErrors;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
