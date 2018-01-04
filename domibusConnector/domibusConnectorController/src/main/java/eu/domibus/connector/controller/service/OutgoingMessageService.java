package eu.domibus.connector.controller.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.domibus.connector.common.exception.ImplementationMissingException;
import eu.domibus.connector.persistence.service.PersistenceException;
import eu.domibus.connector.common.gwc.DomibusConnectorGatewayWebserviceClientException;
import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.exception.DomibusConnectorMessageException;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.nbc.exception.DomibusConnectorNationalBackendClientException;
import eu.domibus.connector.security.exception.DomibusConnectorSecurityException;

public class OutgoingMessageService extends AbstractMessageService implements MessageService {

    static Logger LOGGER = LoggerFactory.getLogger(OutgoingMessageService.class);

    @Override
    public void handleMessage(DomibusConnectorMessage message) throws DomibusConnectorControllerException,
            DomibusConnectorMessageException {

        try {
            persistenceService.persistMessageIntoDatabase(message, DomibusConnectorMessageDirection.NAT_TO_GW);
        } catch (PersistenceException e1) {
            //createSubmissionRejectionAndReturnIt(message, hashValue);
            throw new DomibusConnectorControllerException(e1);
        }

        String hashValue = null;

        
        //should happen in WebClient?
//        if (connectorProperties.isUseContentMapper()) {
//            try {
//                contentMapper.mapNationalToInternational(message);
//            } catch (DomibusConnectorContentMapperException | ImplementationMissingException cme) {
//                createSubmissionRejectionAndReturnIt(message, cme.getMessage());
//                throw new DomibusConnectorMessageException(message, cme.getMessage(), cme, this.getClass());
//            }
//            try {
//                persistenceService.mergeMessageWithDatabase(message);
//            } catch (PersistenceException e1) {
//                //TODO: handle exception    
//                LOGGER.error("Exception occured", e1);
//            }
//        }

        if (connectorProperties.isUseSecurityToolkit()) {
            try {
                securityToolkit.buildContainer(message);
            } catch (DomibusConnectorSecurityException se) {
                createSubmissionRejectionAndReturnIt(message, se.getMessage());
                throw new DomibusConnectorMessageException(message, se.getMessage(), se, this.getClass());
            }
        }

        DomibusConnectorMessageConfirmation confirmation = null;
        if (connectorProperties.isUseEvidencesToolkit()) {
            try {
            	confirmation = evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE, message, null, null);
                // immediately persist new evidence into database
                persistenceService.persistEvidenceForMessageIntoDatabase(message, confirmation.getEvidence(),
                        DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE);

            } catch (DomibusConnectorEvidencesToolkitException ete) {
                createSubmissionRejectionAndReturnIt(message, ete.getMessage());
                throw new DomibusConnectorMessageException(message,
                        "Could not generate evidence submission acceptance! ", ete, this.getClass());
            }

        }

        // replace with new message send service
//        try {
//            gatewayWebserviceClient.sendMessage(message);
//        } catch (DomibusConnectorGatewayWebserviceClientException gwse) {
//            createSubmissionRejectionAndReturnIt(message, gwse.getMessage());
//            throw new DomibusConnectorMessageException(message, "Could not send Message to Gateway! ", gwse,
//                    this.getClass());
//        }

        persistenceService.setMessageDeliveredToGateway(message);
        try {
            persistenceService.setEvidenceDeliveredToGateway(message, confirmation.getEvidenceType());
        } catch (PersistenceException ex) {
                //TODO: handle exception    
            LOGGER.error("Exception occured", ex);
        }

        try {
            DomibusConnectorMessage returnMessage = buildEvidenceMessage(confirmation, message);
            nationalBackendClient.deliverLastEvidenceForMessage(returnMessage);
        } catch (DomibusConnectorNationalBackendClientException | ImplementationMissingException e) {
            throw new DomibusConnectorMessageException(message,
                    "Could not send submission acceptance back to national connector! ", e, this.getClass());
        }

        try {
            persistenceService.setEvidenceDeliveredToNationalSystem(message, confirmation.getEvidenceType());
        } catch (PersistenceException ex) {
            //TODO: handle exception    
            LOGGER.error("Exception occured", ex);
        }

        LOGGER.info("Successfully sent message {} to gateway.", message);

    }


    private void createSubmissionRejectionAndReturnIt(DomibusConnectorMessage message, String errorMessage){

    	DomibusConnectorMessageConfirmation confirmation = null;
        try {
        	confirmation = evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.SUBMISSION_REJECTION, message, DomibusConnectorRejectionReason.OTHER,
                    errorMessage);
        } catch (DomibusConnectorEvidencesToolkitException e) {
            new DomibusConnectorMessageException(message, "Could not even generate submission rejection! ", e,
                    this.getClass());
            LOGGER.error("Could not even generate submission rejection! ", e);
            return;
        }

        try {
            // immediately persist new evidence into database
            persistenceService.persistEvidenceForMessageIntoDatabase(message, confirmation.getEvidence(),
                    DomibusConnectorEvidenceType.SUBMISSION_REJECTION);
        } catch (Exception e) {
            new DomibusConnectorMessageException(message, "Could not persist evidence of type SUBMISSION_REJECTION! ",
                    e, this.getClass());
            LOGGER.error("Could not persist evidence of type SUBMISSION_REJECTION! ", e);
            return;
        }

        try {
            DomibusConnectorMessage returnMessage = buildEvidenceMessage(confirmation, message);
            nationalBackendClient.deliverLastEvidenceForMessage(returnMessage);            
            persistenceService.setEvidenceDeliveredToNationalSystem(message, confirmation.getEvidenceType());

            persistenceService.rejectMessage(message);
        } catch (DomibusConnectorNationalBackendClientException | ImplementationMissingException e) {
            new DomibusConnectorMessageException(message, "Exception while trying to send submission rejection. ", e,
                    this.getClass());
            LOGGER.error("Exception while trying to send submission rejection. ", e);
            return;
        } catch (PersistenceException persistenceException) {
            //TODO: exception
            LOGGER.error("Persistence exceptoin occured while trying to send submission rejection. Rejection is not stored in Storage!", persistenceException);
        }

    }

    private DomibusConnectorMessage buildEvidenceMessage(DomibusConnectorMessageConfirmation confirmation, DomibusConnectorMessage originalMessage) {
        DomibusConnectorMessageDetails details = new DomibusConnectorMessageDetails();
        details.setRefToMessageId(originalMessage.getMessageDetails().getBackendMessageId());
        details.setService(originalMessage.getMessageDetails().getService());

        DomibusConnectorAction action = persistenceService.getAction("SubmissionAcceptanceRejection");
        details.setAction(action);

        DomibusConnectorMessage returnMessage = new DomibusConnectorMessage(details, confirmation);

        return returnMessage;
    }

}
