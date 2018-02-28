
package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendMessage;
import eu.domibus.connector.backend.persistence.service.BackendClientInfoPersistenceService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformer;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistAllBigDataOfMessageService;
import eu.domibus.connector.ws.backend.delivery.webservice.DomibusConnectorBackendDeliveryWebService;
//import org.apache.cxf.ws.policy.v200607.PolicyReference;
//import org.apache.neethi.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component
public class PushMessageViaWsToBackendClientImpl implements PushMessageToBackendClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(PushMessageViaWsToBackendClientImpl.class);
    
    private BackendClientInfoPersistenceService backendClientPersistenceService;
    
    private DomibusConnectorMessagePersistenceService messagePersistenceService;
    
    private DomibusConnectorPersistAllBigDataOfMessageService bigDataMessageService;
    
    private BackendClientWebServiceClientFactory webServiceClientFactory;
    
    //SETTER
    @Autowired
    public void setBackendClientPersistenceService(BackendClientInfoPersistenceService backendClientPersistenceService) {
        this.backendClientPersistenceService = backendClientPersistenceService;
    }

    @Autowired
    public void setMessagePersistenceService(DomibusConnectorMessagePersistenceService messagePersistenceService) {
        this.messagePersistenceService = messagePersistenceService;
    }

    @Autowired
    public void setBigDataMessageService(DomibusConnectorPersistAllBigDataOfMessageService bigDataMessageService) {
        this.bigDataMessageService = bigDataMessageService;
    }
    
    @Autowired
    public void setBackendClientWebServiceClientFactory(BackendClientWebServiceClientFactory webServiceClientFactory) {
        this.webServiceClientFactory = webServiceClientFactory;
    }

    @Override
    public void push(DomibusConnectorBackendMessage backendMessage) {
        LOGGER.debug("#push: push message [{}]", backendMessage);
        //push(backendMessage.getDomibusConnectorMessage().getConnectorMessageId(), backendMessage.getBackendClientInfo().getBackendName());

        DomibusConnectorMessage message = backendMessage.getDomibusConnectorMessage();
        DomibusConnectorBackendClientInfo backendClientInfoByName = backendMessage.getBackendClientInfo();

        //initialize BigData
        message = bigDataMessageService.loadAllBigFilesFromMessage(message);

        //transform message to transition
        DomibusConnectorMessageType transitionMessage = DomibusConnectorDomainMessageTransformer.transformDomainToTransition(message);

        //send message
        try {
            DomibsConnectorAcknowledgementType messageResponse = pushMessageToBackendClient(transitionMessage, backendClientInfoByName);

            if (messageResponse.isResult()) {
                LOGGER.debug("#push: message with id [{}] sucessfully delivered to client [{}]", message.getConnectorMessageId(), backendClientInfoByName.getBackendName());
                String messageId = messageResponse.getMessageId();
                message.getMessageDetails().setBackendMessageId(messageId);

                messagePersistenceService.mergeMessageWithDatabase(message);
                messagePersistenceService.setMessageDeliveredToNationalSystem(message);
            } else {
                //TODO: handle message error
            }

        } catch (Exception ex) {
            //TODO: handle message exception
            LOGGER.error("exception occured throwing it again: [{}]", ex);
            throw new RuntimeException(ex);
        }
    }
    

//    public void push(String connectorMessageId, String backendClientName) {
//
//        //load message from persistence
//        DomibusConnectorMessage message = messagePersistenceService.findMessageByConnectorMessageId(connectorMessageId);
//
//        //load get backend address, cert,
//        DomibusConnectorBackendClientInfo backendClientInfoByName = backendClientPersistenceService.getBackendClientInfoByName(backendClientName);
//
//
//    }
        
    public DomibsConnectorAcknowledgementType pushMessageToBackendClient(DomibusConnectorMessageType transitionMessage, DomibusConnectorBackendClientInfo backendClientInfoByName) {
        DomibusConnectorBackendDeliveryWebService wsClient = webServiceClientFactory.createWsClient(backendClientInfoByName);
        DomibsConnectorAcknowledgementType msgResponse = wsClient.deliverMessage(transitionMessage);
        return msgResponse;
    }
    

}
