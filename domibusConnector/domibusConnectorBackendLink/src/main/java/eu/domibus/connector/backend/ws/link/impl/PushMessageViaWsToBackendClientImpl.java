
package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.backend.persistence.service.BackendClientInfoPersistenceService;
import eu.domibus.connector.backend.service.DomibusConnectorBackendInternalDeliverToController;
import eu.domibus.connector.controller.exception.DomibusConnectorRejectSubmissionException;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformerService;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import eu.domibus.connector.ws.backend.delivery.webservice.DomibusConnectorBackendDeliveryWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static eu.domibus.connector.backend.ws.link.spring.WSBackendLinkContextConfiguration.WS_BACKEND_LINK_PROFILE;


/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Profile(WS_BACKEND_LINK_PROFILE)
@Component
public class PushMessageViaWsToBackendClientImpl implements PushMessageToBackendClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushMessageViaWsToBackendClientImpl.class);
    
    private BackendClientInfoPersistenceService backendClientPersistenceService;
    
    private DCMessagePersistenceService messagePersistenceService;

    private BackendClientWebServiceClientFactory webServiceClientFactory;

    private DomibusConnectorBackendInternalDeliverToController backendSubmissionService;


    private DomibusConnectorDomainMessageTransformerService transformerService;
    
    //SETTER
    @Autowired
    public void setTransformerService(DomibusConnectorDomainMessageTransformerService transformerService) {
        this.transformerService = transformerService;
    }

    @Autowired
    public void setBackendClientPersistenceService(BackendClientInfoPersistenceService backendClientPersistenceService) {
        this.backendClientPersistenceService = backendClientPersistenceService;
    }

    @Autowired
    public void setMessagePersistenceService(DCMessagePersistenceService messagePersistenceService) {
        this.messagePersistenceService = messagePersistenceService;
    }

    @Autowired
    public void setBackendClientWebServiceClientFactory(BackendClientWebServiceClientFactory webServiceClientFactory) {
        this.webServiceClientFactory = webServiceClientFactory;
    }

    @Autowired
    public void setDomibusConnectorBackendInternalDeliverToController(DomibusConnectorBackendInternalDeliverToController backendSubmissionService) {
        this.backendSubmissionService = backendSubmissionService;
    }


    public void push(String connectorMessageId, String backendClientName) {
        DomibusConnectorMessage message = this.messagePersistenceService.findMessageByConnectorMessageId(connectorMessageId);
        DomibusConnectorBackendClientInfo backendClientInfo = this.backendClientPersistenceService.getEnabledBackendClientInfoByName(backendClientName);
        push(new DomibusConnectorBackendMessage(message, backendClientInfo));
    }
    
    @Override
    public void push(DomibusConnectorBackendMessage backendMessage) {
        LOGGER.debug("#push: push message [{}]", backendMessage);

        DomibusConnectorMessage message = backendMessage.getDomibusConnectorMessage();
        DomibusConnectorBackendClientInfo backendClientInfoByName = backendMessage.getBackendClientInfo();

        //transform message to transition
        message = backendSubmissionService.processMessageBeforeDeliverToBackend(message);
        DomibusConnectorMessageType transitionMessage = transformerService.transformDomainToTransition(message);

        //send message
        try {
            DomibsConnectorAcknowledgementType messageResponse = pushMessageToBackendClient(transitionMessage, backendClientInfoByName);

            if (messageResponse.isResult()) {
                String backendMessageId = messageResponse.getMessageId();
                LOGGER.debug("#push: message with id [{}] sucessfully delivered to client [{}], client return id [{}]",
                        message.getConnectorMessageId(), backendClientInfoByName.getBackendName(), backendMessageId);

                backendSubmissionService.processMessageAfterDeliveredToBackend(message);
            } else {
                String error = String.format("Error occured during push: [%s]", messageResponse.getResultMessage());

                throw new RuntimeException(error);

            }

        } catch (Exception ex) {
            LOGGER.warn("Exception occured while submiting message to Backend", ex);

            DomibusConnectorRejectSubmissionException rejectSubmissionException =
                    new DomibusConnectorRejectSubmissionException(backendMessage.getDomibusConnectorMessage(), DomibusConnectorRejectionReason.BACKEND_REJECTION, ex);
            backendSubmissionService.rejectDelivery(backendMessage, rejectSubmissionException);

        }
    }

        
    public DomibsConnectorAcknowledgementType pushMessageToBackendClient(DomibusConnectorMessageType transitionMessage, DomibusConnectorBackendClientInfo backendClientInfoByName) {
        DomibusConnectorBackendDeliveryWebService wsClient = webServiceClientFactory.createWsClient(backendClientInfoByName);
        return wsClient.deliverMessage(transitionMessage);
    }
    

}
