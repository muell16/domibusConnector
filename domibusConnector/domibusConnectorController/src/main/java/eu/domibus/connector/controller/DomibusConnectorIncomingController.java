package eu.domibus.connector.controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.domibus.connector.common.gwc.DomibusConnectorGatewayWebserviceClient;
import eu.domibus.connector.common.gwc.DomibusConnectorGatewayWebserviceClientException;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.controller.service.EvidenceService;
import eu.domibus.connector.controller.service.MessageService;
import eu.domibus.connector.controller.service.ReceiveMessageFromGwService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomibusConnectorIncomingController implements ReceiveMessageFromGwService {

    static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorIncomingController.class);


    private MessageService incomingMessageService;
    private EvidenceService incomingEvidenceService;    
    private DomibusConnectorMessageIdGenerator messageIdGenerator;
    


    /*
    BEGIN SETTER
    */
    @Autowired
    public void setIncomingMessageService(MessageService incomingMessageService) {
        this.incomingMessageService = incomingMessageService;
    }

    @Autowired
    public void setIncomingEvidenceService(EvidenceService incomingEvidenceService) {
        this.incomingEvidenceService = incomingEvidenceService;
    }

    @Autowired
    public void setMessageIdGenerator(DomibusConnectorMessageIdGenerator messageIdGenerator) {
        this.messageIdGenerator = messageIdGenerator;
    }
    /*
        END SETTER
    */
    

//    @Override
//    public void execute() throws DomibusConnectorControllerException {
//        LOGGER.debug("Job for handling incoming messages triggered.");
//        Date start = new Date();
//
//        Collection<DomibusConnectorMessage> messages = null;
////        try {
////            messages = gatewayWebserviceClient.requestPendingMessages();
////        } catch (DomibusConnectorGatewayWebserviceClientException e) {
////            throw new DomibusConnectorControllerException(e);
////        }
//
//        if (!CollectionUtils.isEmpty(messages)) {
//            LOGGER.info("Found {} incoming messages on gateway to handle...", messages.size());
//            for (DomibusConnectorMessage message : messages) {
//                try {
//                    handleMessage(message);
//                } catch (DomibusConnectorControllerException e) {
//                    LOGGER.error("Error handling message with id " + message.getMessageDetails().getEbmsMessageId(), e);
//                }
//            }
//        } else {
//            LOGGER.debug("No pending messages on gateway!");
//        }
//
//        LOGGER.debug("Job for handling incoming messages finished in {} ms.",
//                (System.currentTimeMillis() - start.getTime()));
//    }

    private void handleMessage(DomibusConnectorMessage message) throws DomibusConnectorControllerException {
        

        if (isMessageEvidence(message)) {
            try {
                incomingEvidenceService.handleEvidence(message);
            } catch (DomibusConnectorMessageException | DomibusConnectorControllerException e) {
                LOGGER.error("Error handling message with id " + message.getMessageDetails().getEbmsMessageId(), e);
            }
        } else {
            try {
                incomingMessageService.handleMessage(message);
            } catch (DomibusConnectorControllerException | DomibusConnectorMessageException e) {
                LOGGER.error("Error handling message with id " + message.getMessageDetails().getEbmsMessageId(), e);
            }
        }
    }

    //maybe better in Domain, message itself should know if its an evidence message
    private boolean isMessageEvidence(DomibusConnectorMessage message) {
        return message.getMessageDetails().getAction().getAction().equals("RelayREMMDAcceptanceRejection")
                || message.getMessageDetails().getAction().getAction().equals("DeliveryNonDeliveryToRecipient")
                || message.getMessageDetails().getAction().getAction().equals("RetrievalNonRetrievalToRecipient");
    }

    @Override
    public void receiveMessageFromGwService(DomibusConnectorMessage message) {
        
        if (message.getConnectorMessageId() != null) {
            throw new IllegalArgumentException("Message already received for processing!");
        }
        //get process id for this message
        String msgId = this.messageIdGenerator.generateDomibusConnectorMessageIdGenerator();
        
        MDC.put("messageid", msgId);
        
        
        try {
            this.handleMessage(message);
        } catch (DomibusConnectorControllerException dce) {
            throw new RuntimeException("Error whil processing message");
        }
    }

}
