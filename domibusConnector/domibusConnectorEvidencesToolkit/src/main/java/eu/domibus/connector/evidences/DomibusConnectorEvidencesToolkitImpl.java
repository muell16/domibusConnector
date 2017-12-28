package eu.domibus.connector.evidences;

import org.apache.commons.lang.ArrayUtils;
import org.bouncycastle.util.encoders.Hex;
import org.etsi.uri._02640.v2.EventReasonType;

import eu.domibus.connector.common.CommonConnectorProperties;
import eu.domibus.connector.domain.Action;
//import eu.domibus.connector.common.enums.EvidenceType;
import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.MessageConfirmation;
import eu.domibus.connector.domain.enums.EvidenceType;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.evidences.type.RejectionReason;
import eu.ecodex.evidences.EvidenceBuilder;
import eu.ecodex.evidences.exception.ECodexEvidenceBuilderException;
import eu.ecodex.evidences.types.ECodexMessageDetails;
import eu.spocseu.edeliverygw.configuration.EDeliveryDetails;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail;

public class DomibusConnectorEvidencesToolkitImpl implements DomibusConnectorEvidencesToolkit {

    private EvidenceBuilder evidenceBuilder;
    private HashValueBuilder hashValueBuilder;
    private CommonConnectorProperties connectorProperties;

    @Override
    public MessageConfirmation createEvidence(EvidenceType type, Message message, RejectionReason rejectionReason, String details) throws DomibusConnectorEvidencesToolkitException {
    	byte[] evidence = null;
    	switch(type) {
    	case SUBMISSION_ACCEPTANCE:
    		evidence = createSubmissionAcceptance(message);
    		break;
    	case SUBMISSION_REJECTION:
    		evidence = createSubmissionRejection(rejectionReason, message, details);
    		break;
		case DELIVERY:
			evidence = createDeliveryEvidence(message);
			break;
		case NON_DELIVERY:
			evidence = createNonDeliveryEvidence(rejectionReason, message, details);
			break;
		case NON_RETRIEVAL:
			evidence = createNonRetrievalEvidence(rejectionReason, message, details);
			break;
		case RELAY_REMMD_ACCEPTANCE:
			evidence = createRelayREMMDAcceptance(message);
			break;
		case RELAY_REMMD_FAILURE:
			evidence = createRelayREMMDFailure(rejectionReason, message, details);
			break;
		case RELAY_REMMD_REJECTION:
			evidence = createRelayREMMDRejection(rejectionReason, message, details);
			break;
		case RETRIEVAL:
			evidence = createRetrievalEvidence(message);
			break;
		default:
			break;
    	}
    	
    	MessageConfirmation confirmation = buildConfirmation(type, evidence);
    	
    	
    	return confirmation;
    }
    
    private String checkPDFandBuildHashValue(Message message)
            throws DomibusConnectorEvidencesToolkitException {
        String hashValue = null;
    	if (ArrayUtils.isEmpty(message.getMessageContent().getPdfDocument())) {
            Action action = message.getMessageDetails().getAction();
            if (action == null) {
                throw new DomibusConnectorEvidencesToolkitException("Action still null!");
            }
            if (action.isPdfRequired()) {
                throw new DomibusConnectorEvidencesToolkitException(
                        "There is no PDF document in the message though the Action " + action.getAction()
                                + " requires one!");
            }
        } else {
            try {
                hashValue = hashValueBuilder.buildHashValueAsString(message.getMessageContent().getPdfDocument());

            } catch (Exception e) {
                throw new DomibusConnectorEvidencesToolkitException("Could not build hash code though the PDF is not empty!",
                        e);
            }
        }
        return hashValue;
    }
    
    private byte[] createSubmissionAcceptance(Message message)
            throws DomibusConnectorEvidencesToolkitException {
    	String hash = checkPDFandBuildHashValue(message);
        byte[] evidence = createSubmissionAcceptanceRejection(true, null, message, hash);
        
        return evidence;
    }
   
    private byte[] createSubmissionRejection(RejectionReason rejectionReason, Message message,
            String errorDetails) throws DomibusConnectorEvidencesToolkitException {
    	String hash = checkPDFandBuildHashValue(message);
        if (rejectionReason == null) {
            throw new DomibusConnectorEvidencesToolkitException(
                    "in case of a rejection the rejectionReason may not be null!");
        }

        EventReasonType event = new EventReasonType(rejectionReason.toString(), errorDetails);

        byte[] evidence = createSubmissionAcceptanceRejection(false, event, message, hash);

        
        return evidence;
    }

    private byte[] createRelayREMMDAcceptance(Message message)
            throws DomibusConnectorEvidencesToolkitException {
        MessageConfirmation prevConfirmation = findConfirmation(EvidenceType.SUBMISSION_ACCEPTANCE, message);

        if (prevConfirmation == null) {
            throw new DomibusConnectorEvidencesToolkitException("Message contains no evidence of type "
                    + EvidenceType.SUBMISSION_ACCEPTANCE.name() + "! No evidence of type "
                    + EvidenceType.RELAY_REMMD_ACCEPTANCE + " can be created!");
        }

        byte[] evidence = createRelayREMMDAcceptanceRejection(true, null, prevConfirmation.getEvidence());

     
        return evidence;

    }

    private byte[] createRelayREMMDRejection(RejectionReason rejectionReason, Message message,
            String errorDetails) throws DomibusConnectorEvidencesToolkitException {
        if (rejectionReason == null) {
            throw new DomibusConnectorEvidencesToolkitException(
                    "in case of a rejection the rejectionReason may not be null!");
        }

        EventReasonType event = new EventReasonType(rejectionReason.toString(), errorDetails);

        MessageConfirmation prevConfirmation = findConfirmation(EvidenceType.SUBMISSION_ACCEPTANCE, message);

        if (prevConfirmation == null) {
            throw new DomibusConnectorEvidencesToolkitException("Message contains no evidence of type "
                    + EvidenceType.SUBMISSION_ACCEPTANCE.name() + "! No evidence of type "
                    + EvidenceType.RELAY_REMMD_REJECTION + " can be created!");
        }

        byte[] evidence = createRelayREMMDAcceptanceRejection(false, event, prevConfirmation.getEvidence());

        return evidence;
    }

    private byte[] createRelayREMMDFailure(RejectionReason rejectionReason, Message message,
            String errorDetails) throws DomibusConnectorEvidencesToolkitException {

        if (rejectionReason == null) {
            throw new DomibusConnectorEvidencesToolkitException(
                    "in case of a failure the rejectionReason may not be null!");
        }

        EventReasonType event = new EventReasonType(rejectionReason.toString(), errorDetails);

        MessageConfirmation prevConfirmation = findConfirmation(EvidenceType.SUBMISSION_ACCEPTANCE, message);

        if (prevConfirmation == null) {
            throw new DomibusConnectorEvidencesToolkitException("Message contains no evidence of type "
                    + EvidenceType.SUBMISSION_ACCEPTANCE.name() + "! No evidence of type "
                    + EvidenceType.RELAY_REMMD_FAILURE + " can be created!");
        }

        byte[] evidence = createRelayREMMDFailure(event, prevConfirmation.getEvidence());

        return evidence;
    }

    private byte[] createDeliveryEvidence(Message message) throws DomibusConnectorEvidencesToolkitException {
        MessageConfirmation prevConfirmation = findConfirmation(EvidenceType.RELAY_REMMD_ACCEPTANCE, message);

        if (prevConfirmation == null) {
            throw new DomibusConnectorEvidencesToolkitException("Message contains no evidence of type "
                    + EvidenceType.RELAY_REMMD_ACCEPTANCE.name() + "! No evidence of type "
                    + EvidenceType.DELIVERY + " can be created!");
        }

        byte[] evidence = createDeliveryNonDeliveryEvidence(true, null, prevConfirmation.getEvidence());

      
        return evidence;
    }

    private byte[] createNonDeliveryEvidence(RejectionReason rejectionReason, Message message,
            String errorDetails) throws DomibusConnectorEvidencesToolkitException {

        if (rejectionReason == null) {
            throw new DomibusConnectorEvidencesToolkitException(
                    "in case of a NonDelivery the rejectionReason may not be null!");
        }

        EventReasonType event = new EventReasonType(rejectionReason.toString(), errorDetails);

        MessageConfirmation prevConfirmation = findConfirmation(EvidenceType.RELAY_REMMD_ACCEPTANCE, message);

        if (prevConfirmation == null) {
            throw new DomibusConnectorEvidencesToolkitException("Message contains no evidence of type "
                    + EvidenceType.RELAY_REMMD_ACCEPTANCE.name() + "! No evidence of type "
                    + EvidenceType.NON_DELIVERY + " can be created!");
        }

        byte[] evidence = createDeliveryNonDeliveryEvidence(false, event, prevConfirmation.getEvidence());
       
        return evidence;
    }

    private byte[] createRetrievalEvidence(Message message) throws DomibusConnectorEvidencesToolkitException {
        MessageConfirmation prevConfirmation = findConfirmation(EvidenceType.DELIVERY, message);

        if (prevConfirmation == null) {
            throw new DomibusConnectorEvidencesToolkitException("Message contains no evidence of type "
                    + EvidenceType.DELIVERY.name() + "! No evidence of type " + EvidenceType.RETRIEVAL
                    + " can be created!");
        }

        byte[] evidence = createRetrievalNonRetrievalEvidence(true, null, prevConfirmation.getEvidence());

        return evidence;
    }

    private byte[] createNonRetrievalEvidence(RejectionReason rejectionReason, Message message,
            String errorDetails) throws DomibusConnectorEvidencesToolkitException {

        if (rejectionReason == null) {
            throw new DomibusConnectorEvidencesToolkitException(
                    "in case of a NonRetrieval the rejectionReason may not be null!");
        }

        EventReasonType event = new EventReasonType(rejectionReason.toString(), errorDetails);

        MessageConfirmation prevConfirmation = findConfirmation(EvidenceType.DELIVERY, message);

        if (prevConfirmation == null) {
            throw new DomibusConnectorEvidencesToolkitException("Message contains no evidence of type "
                    + EvidenceType.DELIVERY.name() + "! No evidence of type " + EvidenceType.NON_RETRIEVAL
                    + " can be created!");
        }

        byte[] evidence = createRetrievalNonRetrievalEvidence(false, event, prevConfirmation.getEvidence());

        return evidence;
    }

    private MessageConfirmation findConfirmation(EvidenceType evidenctType, Message message) {
        if (message.getConfirmations() != null) {
            for (MessageConfirmation confirmation : message.getConfirmations()) {
                if (confirmation.getEvidenceType().equals(evidenctType)) {
                    return confirmation;
                }
            }
        }
        return null;
    }

    private MessageConfirmation buildConfirmation(EvidenceType evidenceType, byte[] evidence) {
        MessageConfirmation confirmation = new MessageConfirmation();
        confirmation.setEvidenceType(evidenceType);
        confirmation.setEvidence(evidence);
        return confirmation;
    }

    private byte[] createRetrievalNonRetrievalEvidence(boolean isRetrieval, EventReasonType eventReason,
            byte[] previousEvidence) throws DomibusConnectorEvidencesToolkitException {
        EDeliveryDetails evidenceIssuerDetails = buildEDeliveryDetails();

        try {
            return evidenceBuilder.createRetrievalNonRetrievalByRecipient(isRetrieval, eventReason,
                    evidenceIssuerDetails, previousEvidence);
        } catch (ECodexEvidenceBuilderException e) {
            throw new DomibusConnectorEvidencesToolkitException(e);
        }
    }

    private byte[] createDeliveryNonDeliveryEvidence(boolean isDelivery, EventReasonType eventReason,
            byte[] previousEvidence) throws DomibusConnectorEvidencesToolkitException {
        EDeliveryDetails evidenceIssuerDetails = buildEDeliveryDetails();

        try {
            return evidenceBuilder.createDeliveryNonDeliveryToRecipient(isDelivery, eventReason, evidenceIssuerDetails,
                    previousEvidence);
        } catch (ECodexEvidenceBuilderException e) {
            throw new DomibusConnectorEvidencesToolkitException(e);
        }
    }

    private byte[] createRelayREMMDAcceptanceRejection(boolean isAcceptance, EventReasonType eventReason,
            byte[] previousEvidence) throws DomibusConnectorEvidencesToolkitException {
        EDeliveryDetails evidenceIssuerDetails = buildEDeliveryDetails();

        try {
            return evidenceBuilder.createRelayREMMDAcceptanceRejection(isAcceptance, eventReason,
                    evidenceIssuerDetails, previousEvidence);
        } catch (ECodexEvidenceBuilderException e) {
            throw new DomibusConnectorEvidencesToolkitException(e);
        }
    }

    private byte[] createRelayREMMDFailure(EventReasonType eventReason, byte[] previousEvidence)
            throws DomibusConnectorEvidencesToolkitException {
        EDeliveryDetails evidenceIssuerDetails = buildEDeliveryDetails();

        try {
            return evidenceBuilder.createRelayREMMDFailure(eventReason, evidenceIssuerDetails, previousEvidence);
        } catch (ECodexEvidenceBuilderException e) {
            throw new DomibusConnectorEvidencesToolkitException(e);
        }
    }

    private byte[] createSubmissionAcceptanceRejection(boolean isAcceptance, EventReasonType eventReason,
            Message message, String hash) throws DomibusConnectorEvidencesToolkitException {
        EDeliveryDetails evidenceIssuerDetails = buildEDeliveryDetails();

        String nationalMessageId = message.getMessageDetails().getNationalMessageId();

        String senderAddress = message.getMessageDetails().getOriginalSender();

        String recipientAddress = message.getMessageDetails().getFinalRecipient();

        ECodexMessageDetails messageDetails = null;
        try {
            messageDetails = buildMessageDetails(nationalMessageId, senderAddress, recipientAddress, hash);
        } catch (DomibusConnectorEvidencesToolkitException e) {
            throw e;
        }

        try {
            return evidenceBuilder.createSubmissionAcceptanceRejection(isAcceptance, eventReason,
                    evidenceIssuerDetails, messageDetails);
        } catch (ECodexEvidenceBuilderException e) {
            throw new DomibusConnectorEvidencesToolkitException(e);
        }
    }

    private EDeliveryDetails buildEDeliveryDetails() {
        EDeliveryDetail detail = new EDeliveryDetail();

        EDeliveryDetail.Server server = new EDeliveryDetail.Server();
        server.setGatewayName(connectorProperties.getGatewayName());
        server.setGatewayAddress(connectorProperties.getGatewayEndpointAddress());
        detail.setServer(server);

        EDeliveryDetail.PostalAdress postalAddress = new EDeliveryDetail.PostalAdress();
        postalAddress.setStreetAddress(connectorProperties.getPostalAddressStreet());
        postalAddress.setLocality(connectorProperties.getPostalAddressLocality());
        postalAddress.setPostalCode(connectorProperties.getPostalAddressPostalCode());
        postalAddress.setCountry(connectorProperties.getPostalAddressCountry());
        detail.setPostalAdress(postalAddress);

        EDeliveryDetails evidenceIssuerDetails = new EDeliveryDetails(detail);
        return evidenceIssuerDetails;
    }

    private ECodexMessageDetails buildMessageDetails(String nationalMessageId, String senderAddress,
            String recipientAddress, String hash) throws DomibusConnectorEvidencesToolkitException {
        ECodexMessageDetails messageDetails = new ECodexMessageDetails();

        messageDetails.setHashAlgorithm(hashValueBuilder.getAlgorithm().toString());
        if (hash != null)
            messageDetails.setHashValue(Hex.decode(hash));

        if (nationalMessageId == null || nationalMessageId.isEmpty()) {
            throw new DomibusConnectorEvidencesToolkitException(
                    "the nationalMessageId may not be null for building a submission evidence!");
        }
        if (recipientAddress == null || recipientAddress.isEmpty()) {
            throw new DomibusConnectorEvidencesToolkitException(
                    "the recipientAddress may not be null for building a submission evidence!");
        }
        if (senderAddress == null || senderAddress.isEmpty()) {
            throw new DomibusConnectorEvidencesToolkitException(
                    "the senderAddress may not be null for building a submission evidence!");
        }
        messageDetails.setNationalMessageId(nationalMessageId);
        messageDetails.setRecipientAddress(recipientAddress);
        messageDetails.setSenderAddress(senderAddress);
        return messageDetails;
    }

    public void setEvidenceBuilder(EvidenceBuilder evidenceBuilder) {
        this.evidenceBuilder = evidenceBuilder;
    }

    public void setHashValueBuilder(HashValueBuilder hashValueBuilder) {
        this.hashValueBuilder = hashValueBuilder;
    }

    public void setConnectorProperties(CommonConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

}
