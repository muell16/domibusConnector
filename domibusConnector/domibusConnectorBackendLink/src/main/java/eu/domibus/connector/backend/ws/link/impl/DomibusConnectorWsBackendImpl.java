
package eu.domibus.connector.backend.ws.link.impl;

import eu.domibus.connector.backend.persistence.service.BackendClientInfoPersistenceService;
import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.service.DomibusConnectorBackendSubmissionService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformer;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessagesType;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistAllBigDataOfMessageService;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import eu.domibus.connector.ws.backend.webservice.EmptyRequestType;
import java.security.Principal;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Service("connectorBackendImpl")
public class DomibusConnectorWsBackendImpl implements DomibusConnectorBackendWebService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorBackendWebService.class);
    
    @Resource
    private WebServiceContext webServiceContext;
    
    @Autowired
    private BackendClientInfoPersistenceService backendClientInfoPersistenceService;
    
    @Autowired
    private MessageToBackendClientWaitQueue messageToBackendClientWaitQueue;
    
    @Autowired
    private DomibusConnectorMessagePersistenceService messagePersistenceService;
    
    @Autowired
    private DomibusConnectorPersistAllBigDataOfMessageService domibusConnectorPersistAllBigDataOfMessageService;

    @Autowired
    private DomibusConnectorBackendSubmissionService backendSubmissionService;
    
    //setter
    public void setWsContext(WebServiceContext webServiceContext) {
        this.webServiceContext = webServiceContext;
    }

    public void setBackendClientInfoPersistenceService(BackendClientInfoPersistenceService backendClientInfoPersistenceService) {
        this.backendClientInfoPersistenceService = backendClientInfoPersistenceService;
    }

    public void setMessageToBackendClientWaitQueue(MessageToBackendClientWaitQueue messageToBackendClientWaitQueue) {
        this.messageToBackendClientWaitQueue = messageToBackendClientWaitQueue;
    }

    public void setMessagePersistenceService(DomibusConnectorMessagePersistenceService messagePersistenceService) {
        this.messagePersistenceService = messagePersistenceService;
    }

    public void setDomibusConnectorPersistAllBigDataOfMessageService(DomibusConnectorPersistAllBigDataOfMessageService domibusConnectorPersistAllBigDataOfMessageService) {
        this.domibusConnectorPersistAllBigDataOfMessageService = domibusConnectorPersistAllBigDataOfMessageService;
    }

    public void setBackendSubmissionService(DomibusConnectorBackendSubmissionService backendSubmissionService) {
        this.backendSubmissionService = backendSubmissionService;
    }

        
    @Override
    public DomibusConnectorMessagesType requestMessages(EmptyRequestType requestMessagesRequest) {
        DomibusConnectorBackendClientInfo backendClientInfoByName = checkBackendClient();
        DomibusConnectorMessagesType retrieveWaitingMessagesFromQueue = retrieveWaitingMessagesFromQueue(backendClientInfoByName);        
        return retrieveWaitingMessagesFromQueue;
    }
    
    private DomibusConnectorMessagesType retrieveWaitingMessagesFromQueue(DomibusConnectorBackendClientInfo backendInfo) {        
        DomibusConnectorMessagesType messagesType = new DomibusConnectorMessagesType();
        
        List<String> messageIds = messageToBackendClientWaitQueue.getConnectorMessageIdForBackend(backendInfo.getBackendName());
        messageIds.stream().map((msgId) -> messagePersistenceService.findMessageByConnectorMessageId(msgId)).forEach((message) -> {
            messagesType.getMessages().add(transformDomibusConnectorMessageToTransitionMessage(message));
        });
                
        return messagesType;
    }
    
    private DomibusConnectorMessageType transformDomibusConnectorMessageToTransitionMessage(DomibusConnectorMessage message) {
        message = domibusConnectorPersistAllBigDataOfMessageService.loadAllBigFilesFromMessage(message);
        DomibusConnectorMessageType transformDomainToTransition = DomibusConnectorDomainMessageTransformer.transformDomainToTransition(message);
        return transformDomainToTransition;        
    }
    
    
        
    @Override
    public DomibsConnectorAcknowledgementType submitMessage(DomibusConnectorMessageType submitMessageRequest) {        
        DomibsConnectorAcknowledgementType answer = new DomibsConnectorAcknowledgementType();
        try {
            LOGGER.debug("#submitMessage: message: [{}]", submitMessageRequest);
        
            DomibusConnectorBackendClientInfo backendClientInfoByName = checkBackendClient();

            DomibusConnectorMessage transitionMessage = DomibusConnectorDomainMessageTransformer.transformTransitionToDomain(submitMessageRequest);
            transitionMessage.setConnectorBackendClientName(backendClientInfoByName.getBackendName());
        
            backendSubmissionService.submitToController(transitionMessage);
                       
            answer.setResult(true);
            answer.setMessageId(transitionMessage.getConnectorMessageId());
            
            
        } catch (DomibusConnectorControllerException e) {
            LOGGER.error("Exception occured during submitMessage from backend", e);
            answer.setResult(false);            
        }                
        return answer;
    }

    
    private DomibusConnectorBackendClientInfo checkBackendClient() {
        Principal userPrincipal = webServiceContext.getUserPrincipal();
        String backendName = userPrincipal == null ? null : userPrincipal.getName();
        if (userPrincipal == null || backendName == null) {
            String error = String.format("checkBackendClient: Cannot requestMessages because userPrincipal is [%s] the userName is [%s]. Cannot identify backend!", userPrincipal, backendName);
            LOGGER.error("#checkBackendClient: Throwing Exception: {}", error);
            throw new RuntimeException(error);            
        }
        DomibusConnectorBackendClientInfo backendClientInfoByName = backendClientInfoPersistenceService.getBackendClientInfoByName(backendName);
        if (backendClientInfoByName == null) {
            String error = String.format("#checkBackendClient: No backend with name [%s] configured on connector!", backendName);
            LOGGER.error("#checkBackendClient: Throwing Exception: {}", error);
            throw new RuntimeException(error);
        }
        return backendClientInfoByName;
    }


}
