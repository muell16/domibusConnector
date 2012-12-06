package eu.ecodex.connector.evidences;

import eu.ecodex.connector.common.ECodexConnectorProperties;
import eu.ecodex.connector.common.enums.ECodexEvidenceType;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.evidences.exception.EvidencesToolkitException;
import eu.ecodex.connector.evidences.type.RejectionReason;
import eu.ecodex.evidences.EvidenceBuilder;
import eu.ecodex.evidences.exception.ECodexEvidenceBuilderException;
import eu.ecodex.evidences.types.ECodexMessageDetails;
import eu.spocseu.edeliverygw.REMErrorEvent;
import eu.spocseu.edeliverygw.configuration.EDeliveryDetails;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail;

public class ECodexConnectorEvidencesToolkitImpl implements ECodexConnectorEvidencesToolkit {

    private EvidenceBuilder evidenceBuilder;
    private HashValueBuilder hashValueBuilder;
    private ECodexConnectorProperties connectorProperties;

    @Override
    public byte[] createSubmissionAcceptance(Message message, byte[] hash) throws EvidencesToolkitException {

        byte[] evidence = createSubmissionAcceptanceRejection(true, null, message, hash);

        MessageConfirmation confirmation = buildConfirmation(message.getMessageDetails().getNationalMessageId(),
                ECodexEvidenceType.SUBMISSION_ACCEPTANCE, evidence);

        message.addConfirmation(confirmation);

        return evidence;
    }

    @Override
    public byte[] createSubmissionRejection(RejectionReason rejectionReason, Message message, byte[] hash)
            throws EvidencesToolkitException {

        if (rejectionReason == null) {
            throw new EvidencesToolkitException("in case of a rejection the rejectionReason may not be null!");
        }

        REMErrorEvent event = REMErrorEvent.valueOf(rejectionReason.toString());

        byte[] evidence = createSubmissionAcceptanceRejection(false, event, message, hash);

        MessageConfirmation confirmation = buildConfirmation(message.getMessageDetails().getNationalMessageId(),
                ECodexEvidenceType.SUBMISSION_REJECTION, evidence);

        message.addConfirmation(confirmation);

        return evidence;
    }

    @Override
    public void createRelayREMMDAcceptance(Message message) throws EvidencesToolkitException {
        MessageConfirmation prevConfirmation = findConfirmation(ECodexEvidenceType.SUBMISSION_ACCEPTANCE, message);

        byte[] evidence = createRelayREMMDAcceptanceRejection(true, null, prevConfirmation.getEvidence());

        MessageConfirmation confirmation = buildConfirmation(message.getMessageDetails().getNationalMessageId(),
                ECodexEvidenceType.RELAY_REMMD_ACCEPTANCE, evidence);

        message.addConfirmation(confirmation);

    }

    @Override
    public void createRelayREMMDRejection(RejectionReason rejectionReason, Message message)
            throws EvidencesToolkitException {
        if (rejectionReason == null) {
            throw new EvidencesToolkitException("in case of a rejection the rejectionReason may not be null!");
        }

        REMErrorEvent event = REMErrorEvent.valueOf(rejectionReason.toString());

        MessageConfirmation prevConfirmation = findConfirmation(ECodexEvidenceType.SUBMISSION_ACCEPTANCE, message);

        byte[] evidence = createRelayREMMDAcceptanceRejection(false, event, prevConfirmation.getEvidence());

        MessageConfirmation confirmation = buildConfirmation(message.getMessageDetails().getNationalMessageId(),
                ECodexEvidenceType.RELAY_REMMD_REJECTION, evidence);

        message.addConfirmation(confirmation);
    }

    @Override
    public void createDeliveryEvidence(Message message) throws EvidencesToolkitException {
        MessageConfirmation prevConfirmation = findConfirmation(ECodexEvidenceType.RELAY_REMMD_ACCEPTANCE, message);

        byte[] evidence = createDeliveryNonDeliveryEvidence(true, null, prevConfirmation.getEvidence());

        MessageConfirmation confirmation = buildConfirmation(message.getMessageDetails().getNationalMessageId(),
                ECodexEvidenceType.DELIVERY, evidence);

        message.addConfirmation(confirmation);
    }

    @Override
    public void createNonDeliveryEvidence(RejectionReason rejectionReason, Message message)
            throws EvidencesToolkitException {

        if (rejectionReason == null) {
            throw new EvidencesToolkitException("in case of a NonDelivery the rejectionReason may not be null!");
        }

        REMErrorEvent event = REMErrorEvent.valueOf(rejectionReason.toString());

        MessageConfirmation prevConfirmation = findConfirmation(ECodexEvidenceType.RELAY_REMMD_ACCEPTANCE, message);

        byte[] evidence = createDeliveryNonDeliveryEvidence(false, event, prevConfirmation.getEvidence());

        MessageConfirmation confirmation = buildConfirmation(message.getMessageDetails().getNationalMessageId(),
                ECodexEvidenceType.NON_DELIVERY, evidence);

        message.addConfirmation(confirmation);
    }

    @Override
    public void createRetrievalEvidence(Message message) throws EvidencesToolkitException {
        MessageConfirmation prevConfirmation = findConfirmation(ECodexEvidenceType.DELIVERY, message);

        byte[] evidence = createRetrievalNonRetrievalEvidence(true, null, prevConfirmation.getEvidence());

        MessageConfirmation confirmation = buildConfirmation(message.getMessageDetails().getNationalMessageId(),
                ECodexEvidenceType.RETRIEVAL, evidence);

        message.addConfirmation(confirmation);
    }

    @Override
    public void createNonRetrievalEvidence(RejectionReason rejectionReason, Message message)
            throws EvidencesToolkitException {

        if (rejectionReason == null) {
            throw new EvidencesToolkitException("in case of a NonRetrieval the rejectionReason may not be null!");
        }

        REMErrorEvent event = REMErrorEvent.valueOf(rejectionReason.toString());

        MessageConfirmation prevConfirmation = findConfirmation(ECodexEvidenceType.DELIVERY, message);

        byte[] evidence = createRetrievalNonRetrievalEvidence(false, event, prevConfirmation.getEvidence());

        MessageConfirmation confirmation = buildConfirmation(message.getMessageDetails().getNationalMessageId(),
                ECodexEvidenceType.NON_RETRIEVAL, evidence);

        message.addConfirmation(confirmation);
    }

    private MessageConfirmation findConfirmation(ECodexEvidenceType evidenctType, Message message) {
        for (MessageConfirmation confirmation : message.getConfirmations()) {
            if (confirmation.getEvidenceType().equals(evidenctType)) {
                return confirmation;
            }
        }
        return null;
    }

    private MessageConfirmation buildConfirmation(String messageId, ECodexEvidenceType evidenceType, byte[] evidence) {
        MessageConfirmation confirmation = new MessageConfirmation();
        confirmation.setMessageId(messageId);
        confirmation.setEvidenceType(evidenceType);
        confirmation.setEvidence(evidence);
        return confirmation;
    }

    private byte[] createRetrievalNonRetrievalEvidence(boolean isRetrieval, REMErrorEvent eventReason,
            byte[] previousEvidence) throws EvidencesToolkitException {
        EDeliveryDetails evidenceIssuerDetails = buildEDeliveryDetails();

        try {
            return evidenceBuilder.createRetrievalNonRetrievalByRecipient(isRetrieval, eventReason,
                    evidenceIssuerDetails, previousEvidence);
        } catch (ECodexEvidenceBuilderException e) {
            throw new EvidencesToolkitException(e);
        }
    }

    private byte[] createDeliveryNonDeliveryEvidence(boolean isDelivery, REMErrorEvent eventReason,
            byte[] previousEvidence) throws EvidencesToolkitException {
        EDeliveryDetails evidenceIssuerDetails = buildEDeliveryDetails();

        try {
            return evidenceBuilder.createDeliveryNonDeliveryToRecipient(isDelivery, eventReason, evidenceIssuerDetails,
                    previousEvidence);
        } catch (ECodexEvidenceBuilderException e) {
            throw new EvidencesToolkitException(e);
        }
    }

    private byte[] createRelayREMMDAcceptanceRejection(boolean isAcceptance, REMErrorEvent eventReason,
            byte[] previousEvidence) throws EvidencesToolkitException {
        EDeliveryDetails evidenceIssuerDetails = buildEDeliveryDetails();

        try {
            return evidenceBuilder.createRelayREMMDAcceptanceRejection(isAcceptance, eventReason,
                    evidenceIssuerDetails, previousEvidence);
        } catch (ECodexEvidenceBuilderException e) {
            throw new EvidencesToolkitException(e);
        }
    }

    private byte[] createSubmissionAcceptanceRejection(boolean isAcceptance, REMErrorEvent reason, Message message,
            byte[] hash) throws EvidencesToolkitException {
        EDeliveryDetails evidenceIssuerDetails = buildEDeliveryDetails();

        String nationalMessageId = message.getMessageDetails().getNationalMessageId();

        byte[] originalMessage = message.getMessageContent().getXmlContent();

        String senderAddress = message.getMessageDetails().getOriginalSenderAddress();

        String recipientAddress = message.getMessageDetails().getFinalRecipientAddress();

        ECodexMessageDetails messageDetails = null;
        try {
            messageDetails = buildMessageDetails(nationalMessageId, originalMessage, senderAddress, recipientAddress,
                    hash);
        } catch (EvidencesToolkitException e) {
            throw e;
        }

        try {
            return evidenceBuilder.createSubmissionAcceptanceRejection(isAcceptance, reason, evidenceIssuerDetails,
                    messageDetails);
        } catch (ECodexEvidenceBuilderException e) {
            throw new EvidencesToolkitException(e);
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

    private ECodexMessageDetails buildMessageDetails(String nationalMessageId, byte[] originalMessage,
            String senderAddress, String recipientAddress, byte[] hash) throws EvidencesToolkitException {
        ECodexMessageDetails messageDetails = new ECodexMessageDetails();

        if (originalMessage == null || originalMessage.length < 1) {
            throw new EvidencesToolkitException("there is no original message to build an evidence for!");
        }
        messageDetails.setHashAlgorithm(hashValueBuilder.getAlgorithm().toString());
        messageDetails.setHashValue(hash);

        if (nationalMessageId == null || nationalMessageId.isEmpty()) {
            throw new EvidencesToolkitException(
                    "the nationalMessageId may not be null for building a submission evidence!");
        }
        if (recipientAddress == null || recipientAddress.isEmpty()) {
            throw new EvidencesToolkitException(
                    "the recipientAddress may not be null for building a submission evidence!");
        }
        if (senderAddress == null || senderAddress.isEmpty()) {
            throw new EvidencesToolkitException("the senderAddress may not be null for building a submission evidence!");
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

    public void setConnectorProperties(ECodexConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

}
