package eu.ecodex.connector.evidences;

import org.etsi.uri._02640.v2.REMEvidenceType;

import eu.ecodex.connector.common.ECodexConnectorProperties;
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
    public byte[] createSubmissionAcceptance(String nationalMessageId, byte[] originalMessage, String senderAddress,
            String recipientAddress) throws EvidencesToolkitException {

        return createSubmissionAcceptanceRejection(true, null, nationalMessageId, originalMessage, senderAddress,
                recipientAddress);
    }

    @Override
    public byte[] createSubmissionRejection(RejectionReason rejectionReason, String nationalMessageId,
            byte[] originalMessage, String senderAddress, String recipientAddress) throws EvidencesToolkitException {

        REMErrorEvent event = REMErrorEvent.valueOf(rejectionReason.toString());

        return createSubmissionAcceptanceRejection(false, event, nationalMessageId, originalMessage, senderAddress,
                recipientAddress);
    }

    @Override
    public byte[] createDeliveryEvidence(REMEvidenceType previousEvidence) throws EvidencesToolkitException {
        return createDeliveryNonDeliveryEvidence(true, null, previousEvidence);
    }

    @Override
    public byte[] createNonDeliveryEvidence(RejectionReason rejectionReason, REMEvidenceType previousEvidence)
            throws EvidencesToolkitException {

        REMErrorEvent event = REMErrorEvent.valueOf(rejectionReason.toString());

        return createDeliveryNonDeliveryEvidence(false, event, previousEvidence);
    }

    @Override
    public byte[] createRetrievalEvidence(REMEvidenceType previousEvidence) throws EvidencesToolkitException {
        return createRetrievalNonRetrievalEvidence(true, null, previousEvidence);
    }

    @Override
    public byte[] createNonRetrievalEvidence(RejectionReason rejectionReason, REMEvidenceType previousEvidence)
            throws EvidencesToolkitException {
        REMErrorEvent event = REMErrorEvent.valueOf(rejectionReason.toString());

        return createRetrievalNonRetrievalEvidence(false, event, previousEvidence);
    }

    private byte[] createRetrievalNonRetrievalEvidence(boolean isRetrieval, REMErrorEvent eventReason,
            REMEvidenceType previousEvidence) throws EvidencesToolkitException {
        EDeliveryDetails evidenceIssuerDetails = buildEDeliveryDetails();

        try {
            return evidenceBuilder.createRetrievalNonRetrievalByRecipient(isRetrieval, eventReason,
                    evidenceIssuerDetails, previousEvidence);
        } catch (ECodexEvidenceBuilderException e) {
            throw new EvidencesToolkitException(e);
        }
    }

    private byte[] createDeliveryNonDeliveryEvidence(boolean isDelivery, REMErrorEvent eventReason,
            REMEvidenceType previousEvidence) throws EvidencesToolkitException {
        EDeliveryDetails evidenceIssuerDetails = buildEDeliveryDetails();

        try {
            return evidenceBuilder.createDeliveryNonDeliveryToRecipient(isDelivery, eventReason, evidenceIssuerDetails,
                    previousEvidence);
        } catch (ECodexEvidenceBuilderException e) {
            throw new EvidencesToolkitException(e);
        }
    }

    private byte[] createSubmissionAcceptanceRejection(boolean isAcceptance, REMErrorEvent reason,
            String nationalMessageId, byte[] originalMessage, String senderAddress, String recipientAddress)
            throws EvidencesToolkitException {
        EDeliveryDetails evidenceIssuerDetails = buildEDeliveryDetails();

        ECodexMessageDetails messageDetails = buildMessageDetails(nationalMessageId, originalMessage, senderAddress,
                recipientAddress);

        try {
            return evidenceBuilder.createSubmissionAcceptanceRejection(true, null, evidenceIssuerDetails,
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
            String senderAddress, String recipientAddress) {
        ECodexMessageDetails messageDetails = new ECodexMessageDetails();

        byte[] hashValue = hashValueBuilder.buildHashValue(originalMessage);
        messageDetails.setHashAlgorithm(hashValueBuilder.getAlgorithm().toString());
        messageDetails.setHashValue(hashValue);

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
