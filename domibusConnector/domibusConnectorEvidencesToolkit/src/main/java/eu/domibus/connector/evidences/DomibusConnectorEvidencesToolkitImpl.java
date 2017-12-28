package eu.domibus.connector.evidences;

import org.bouncycastle.util.encoders.Hex;
import org.etsi.uri._02640.v2.EventReasonType;

import eu.domibus.connector.common.CommonConnectorProperties;
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
    public byte[] createSubmissionAcceptance(Message message, String hash)
            throws DomibusConnectorEvidencesToolkitException {

        byte[] evidence = createSubmissionAcceptanceRejection(true, null, message, hash);

        MessageConfirmation confirmation = buildConfirmation(message.getMessageDetails().getNationalMessageId(),
                EvidenceType.SUBMISSION_ACCEPTANCE, evidence);

        message.addConfirmation(confirmation);

        return evidence;
    }

    @Override
    public byte[] createSubmissionRejection(RejectionReason rejectionReason, Message message, String hash)
            throws DomibusConnectorEvidencesToolkitException {

        return createSubmissionRejection(rejectionReason, message, hash, null);
    }

    @Override
    public byte[] createSubmissionRejection(RejectionReason rejectionReason, Message message, String hash,
            String errorDetails) throws DomibusConnectorEvidencesToolkitException {

        if (rejectionReason == null) {
            throw new DomibusConnectorEvidencesToolkitException(
                    "in case of a rejection the rejectionReason may not be null!");
        }

        EventReasonType event = new EventReasonType(rejectionReason.toString(), errorDetails);

        byte[] evidence = createSubmissionAcceptanceRejection(false, event, message, hash);

        MessageConfirmation confirmation = buildConfirmation(message.getMessageDetails().getNationalMessageId(),
                EvidenceType.SUBMISSION_REJECTION, evidence);

        message.addConfirmation(confirmation);

        return evidence;
    }

    @Override
    public MessageConfirmation createRelayREMMDAcceptance(Message message)
            throws DomibusConnectorEvidencesToolkitException {
        MessageConfirmation prevConfirmation = findConfirmation(EvidenceType.SUBMISSION_ACCEPTANCE, message);

        if (prevConfirmation == null) {
            throw new DomibusConnectorEvidencesToolkitException("Message contains no evidence of type "
                    + EvidenceType.SUBMISSION_ACCEPTANCE.name() + "! No evidence of type "
                    + EvidenceType.RELAY_REMMD_ACCEPTANCE + " can be created!");
        }

        byte[] evidence = createRelayREMMDAcceptanceRejection(true, null, prevConfirmation.getEvidence());

        MessageConfirmation confirmation = buildConfirmation(message.getMessageDetails().getNationalMessageId(),
                EvidenceType.RELAY_REMMD_ACCEPTANCE, evidence);

        return confirmation;

    }

    @Override
    public MessageConfirmation createRelayREMMDRejection(RejectionReason rejectionReason, Message message)
            throws DomibusConnectorEvidencesToolkitException {

        return createRelayREMMDRejection(rejectionReason, message, null);
    }

    @Override
    public MessageConfirmation createRelayREMMDRejection(RejectionReason rejectionReason, Message message,
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

        MessageConfirmation confirmation = buildConfirmation(message.getMessageDetails().getNationalMessageId(),
                EvidenceType.RELAY_REMMD_REJECTION, evidence);

        return confirmation;
    }

    @Override
    public MessageConfirmation createRelayREMMDFailure(RejectionReason rejectionReason, Message message)
            throws DomibusConnectorEvidencesToolkitException {

        return createRelayREMMDFailure(rejectionReason, message, null);
    }

    @Override
    public MessageConfirmation createRelayREMMDFailure(RejectionReason rejectionReason, Message message,
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

        MessageConfirmation confirmation = buildConfirmation(message.getMessageDetails().getNationalMessageId(),
                EvidenceType.RELAY_REMMD_FAILURE, evidence);

        return confirmation;
    }

    @Override
    public MessageConfirmation createDeliveryEvidence(Message message) throws DomibusConnectorEvidencesToolkitException {
        MessageConfirmation prevConfirmation = findConfirmation(EvidenceType.RELAY_REMMD_ACCEPTANCE, message);

        if (prevConfirmation == null) {
            throw new DomibusConnectorEvidencesToolkitException("Message contains no evidence of type "
                    + EvidenceType.RELAY_REMMD_ACCEPTANCE.name() + "! No evidence of type "
                    + EvidenceType.DELIVERY + " can be created!");
        }

        byte[] evidence = createDeliveryNonDeliveryEvidence(true, null, prevConfirmation.getEvidence());

        MessageConfirmation confirmation = buildConfirmation(message.getMessageDetails().getNationalMessageId(),
                EvidenceType.DELIVERY, evidence);

        return confirmation;
    }

    @Override
    public MessageConfirmation createNonDeliveryEvidence(RejectionReason rejectionReason, Message message)
            throws DomibusConnectorEvidencesToolkitException {

        return createNonDeliveryEvidence(rejectionReason, message, null);

    }

    @Override
    public MessageConfirmation createNonDeliveryEvidence(RejectionReason rejectionReason, Message message,
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

        MessageConfirmation confirmation = buildConfirmation(message.getMessageDetails().getNationalMessageId(),
                EvidenceType.NON_DELIVERY, evidence);

        return confirmation;
    }

    @Override
    public MessageConfirmation createRetrievalEvidence(Message message) throws DomibusConnectorEvidencesToolkitException {
        MessageConfirmation prevConfirmation = findConfirmation(EvidenceType.DELIVERY, message);

        if (prevConfirmation == null) {
            throw new DomibusConnectorEvidencesToolkitException("Message contains no evidence of type "
                    + EvidenceType.DELIVERY.name() + "! No evidence of type " + EvidenceType.RETRIEVAL
                    + " can be created!");
        }

        byte[] evidence = createRetrievalNonRetrievalEvidence(true, null, prevConfirmation.getEvidence());

        MessageConfirmation confirmation = buildConfirmation(message.getMessageDetails().getNationalMessageId(),
                EvidenceType.RETRIEVAL, evidence);

        return confirmation;
    }

    @Override
    public MessageConfirmation createNonRetrievalEvidence(RejectionReason rejectionReason, Message message)
            throws DomibusConnectorEvidencesToolkitException {
        return createNonRetrievalEvidence(rejectionReason, message, null);

    }

    @Override
    public MessageConfirmation createNonRetrievalEvidence(RejectionReason rejectionReason, Message message,
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

        MessageConfirmation confirmation = buildConfirmation(message.getMessageDetails().getNationalMessageId(),
                EvidenceType.NON_RETRIEVAL, evidence);

        return confirmation;
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

    private MessageConfirmation buildConfirmation(String messageId, EvidenceType evidenceType, byte[] evidence) {
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
