package eu.domibus.connector.evidences;

import eu.domibus.connector.common.annotations.BusinessDomainScoped;
import eu.domibus.connector.evidences.spring.EvidencesToolkitConfigurationProperties;
import eu.domibus.connector.evidences.spring.HomePartyConfigurationProperties;
import eu.domibus.connector.evidences.spring.PostalAdressConfigurationProperties;
import org.bouncycastle.util.encoders.Hex;
import org.etsi.uri._02640.v2.EventReasonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DC5Confirmation;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.ecodex.evidences.EvidenceBuilder;
import eu.ecodex.evidences.exception.ECodexEvidenceBuilderException;
import eu.ecodex.evidences.types.ECodexMessageDetails;
import eu.spocseu.edeliverygw.configuration.EDeliveryDetails;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail;

import javax.jms.Message;
import java.util.Optional;

@BusinessDomainScoped
@Component
public class DomibusConnectorEvidencesToolkitImpl implements DomibusConnectorEvidencesToolkit {

    private final static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorEvidencesToolkitImpl.class);

    @Autowired
    private EvidenceBuilder evidenceBuilder;

    @Autowired
    private HashValueBuilder hashValueBuilder;

    @Autowired
    EvidencesToolkitConfigurationProperties evidencesToolkitConfigurationProperties;

    @Override
    public Evidence createEvidence(DomibusConnectorEvidenceType type, MessageParameters message, DomibusConnectorRejectionReason rejectionReason, String details) throws DomibusConnectorEvidencesToolkitException {
        LOGGER.debug("#createEvidence: [{}] for message [{}]", type, message);
//        LOGGER.trace("#createEvidence: message contains following evidences: [{}]", message.getRelatedMessageConfirmations());
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
        case RETRIEVAL:
            evidence = createRetrievalEvidence(message);
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
		default:
			break;
    	}

    	if (evidence == null) {
    	    //fail if the evidence couldn't be created! So illegal null evidences aren't used in the connector!
            //TODO: make this error more transparent
    	    throw new DomibusConnectorEvidencesToolkitException("Evidence could not be created by evidenceToolkit impl");
        }

        return Evidence.builder().type(type).evidence(evidence).build();
    }
    
    private String checkPDFandBuildHashValue(MessageParameters message)
            throws DomibusConnectorEvidencesToolkitException {
        return message.getBusinessDocumentHash().getHash();
//        throw new RuntimeException("THIS MUS BE IMPLEMENTED!");
//
        //TODO: rethink if the hash should really reference the business doc?!
        //maybe better the hash references the TOKEN? Because the Token also references the business doc?!

//        String hashValue = null;
//    	if (ArrayUtils.isEmpty(message.getMessageContent().getXmlContent())) {
//            DomibusConnectorAction action = message.getMessageDetails().getAction();
//            if (action == null) {
//                throw new DomibusConnectorEvidencesToolkitException("Action still null!");
//            }
////            if (action.isDocumentRequired()) {
////                throw new DomibusConnectorEvidencesToolkitException(
////                        "There is no document in the message though the Action " + action.getAction()
////                                + " requires one!");
////            }
//        } else {
//            try {
//                hashValue = hashValueBuilder.buildHashValueAsString(message.getMessageContent().getXmlContent());
//
//            } catch (Exception e) {
//                throw new DomibusConnectorEvidencesToolkitException("Could not build hash code though the PDF is not empty!",
//                        e);
//            }
//        }
//        return hashValue;
    }
    
    private byte[] createSubmissionAcceptance(MessageParameters message)
            throws DomibusConnectorEvidencesToolkitException {
    	String hash = checkPDFandBuildHashValue(message);
        return createSubmissionAcceptanceRejection(true, null, message, hash);
    }
   
    private byte[] createSubmissionRejection(DomibusConnectorRejectionReason rejectionReason, MessageParameters message,
            String errorDetails) throws DomibusConnectorEvidencesToolkitException {
    	String hash = checkPDFandBuildHashValue(message);
        if (rejectionReason == null) {
            throw new DomibusConnectorEvidencesToolkitException(
                    "in case of a rejection the rejectionReason may not be null!");
        }

        EventReasonType event = new EventReasonType();
        event.setCode(rejectionReason.toString());
        event.setDetails(errorDetails);

        return createSubmissionAcceptanceRejection(false, event, message, hash);
    }

    private byte[] createRelayREMMDAcceptance(MessageParameters message)
            throws DomibusConnectorEvidencesToolkitException {
        Optional<Evidence> prevConfirmation = findConfirmation(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE, message);

        if (!prevConfirmation.isPresent()) {
            throw new DomibusConnectorEvidencesToolkitException("Message contains no evidence of type "
                    + DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE.name() + "! No evidence of type "
                    + DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE + " can be created!");
        }
        return createRelayREMMDAcceptanceRejection(true, null, prevConfirmation.get());

    }

    private byte[] createRelayREMMDRejection(DomibusConnectorRejectionReason rejectionReason, MessageParameters message,
            String errorDetails) throws DomibusConnectorEvidencesToolkitException {
        if (rejectionReason == null) {
            throw new DomibusConnectorEvidencesToolkitException(
                    "in case of a rejection the rejectionReason may not be null!");
        }

        EventReasonType event = new EventReasonType();
        event.setCode(rejectionReason.toString());
        event.setDetails(errorDetails);

        Optional<Evidence> prevConfirmation = findConfirmation(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE, message);

        if (!prevConfirmation.isPresent()) {
            throw new DomibusConnectorEvidencesToolkitException("Message contains no evidence of type "
                    + DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE.name() + "! No evidence of type "
                    + DomibusConnectorEvidenceType.RELAY_REMMD_REJECTION + " can be created!");
        }

        return createRelayREMMDAcceptanceRejection(false, event, prevConfirmation.get());
    }

    private byte[] createRelayREMMDFailure(DomibusConnectorRejectionReason rejectionReason, MessageParameters message,
            String errorDetails) throws DomibusConnectorEvidencesToolkitException {

        if (rejectionReason == null) {
            throw new DomibusConnectorEvidencesToolkitException(
                    "in case of a failure the rejectionReason may not be null!");
        }

        EventReasonType event = new EventReasonType();
        event.setCode(rejectionReason.toString());
        event.setDetails(errorDetails);

        Optional<Evidence> prevConfirmation = findConfirmation(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE, message);

        if (!prevConfirmation.isPresent()) {
            throw new DomibusConnectorEvidencesToolkitException("Message contains no evidence of type "
                    + DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE.name() + "! No evidence of type "
                    + DomibusConnectorEvidenceType.RELAY_REMMD_FAILURE + " can be created!");
        }

        return createRelayREMMDFailure(event, prevConfirmation.get());
    }

    private byte[] createDeliveryEvidence(MessageParameters message) throws DomibusConnectorEvidencesToolkitException {
        Optional<Evidence> prevConfirmation = findConfirmation(DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE, message);

        if (!prevConfirmation.isPresent()) {
            throw new DomibusConnectorEvidencesToolkitException("Message contains no evidence of type "
                    + DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE.name() + "! No evidence of type "
                    + DomibusConnectorEvidenceType.DELIVERY + " can be created!");
        }

        return createDeliveryNonDeliveryEvidence(true, null, prevConfirmation.get());
    }

    private byte[] createNonDeliveryEvidence(DomibusConnectorRejectionReason rejectionReason, MessageParameters message,
            String errorDetails) throws DomibusConnectorEvidencesToolkitException {

        if (rejectionReason == null) {
            throw new DomibusConnectorEvidencesToolkitException(
                    "in case of a NonDelivery the rejectionReason may not be null!");
        }

        EventReasonType event = new EventReasonType();
        event.setCode(rejectionReason.toString());
        event.setDetails(errorDetails);

        Optional<Evidence> prevConfirmation = findConfirmation(DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE, message);

        if (!prevConfirmation.isPresent()) {
            throw new DomibusConnectorEvidencesToolkitException("Message contains no evidence of type "
                    + DomibusConnectorEvidenceType.RELAY_REMMD_ACCEPTANCE.name() + "! No evidence of type "
                    + DomibusConnectorEvidenceType.NON_DELIVERY + " can be created!");
        }

        return createDeliveryNonDeliveryEvidence(false, event, prevConfirmation.get());
    }

    private byte[] createRetrievalEvidence(MessageParameters message) throws DomibusConnectorEvidencesToolkitException {
        Optional<Evidence> prevConfirmation = findConfirmation(DomibusConnectorEvidenceType.DELIVERY, message);

        if (!prevConfirmation.isPresent()) {
            throw new DomibusConnectorEvidencesToolkitException("Message contains no evidence of type "
                    + DomibusConnectorEvidenceType.DELIVERY.name() + "! No evidence of type " + DomibusConnectorEvidenceType.RETRIEVAL
                    + " can be created!");
        }

        return createRetrievalNonRetrievalEvidence(true, null, prevConfirmation.get());
    }

    private byte[] createNonRetrievalEvidence(DomibusConnectorRejectionReason rejectionReason, MessageParameters message,
            String errorDetails) throws DomibusConnectorEvidencesToolkitException {

        if (rejectionReason == null) {
            throw new DomibusConnectorEvidencesToolkitException(
                    "in case of a NonRetrieval the rejectionReason may not be null!");
        }

        EventReasonType event = new EventReasonType();
        event.setCode(rejectionReason.toString());
        event.setDetails(errorDetails);

        Optional<Evidence> prevConfirmation = findConfirmation(DomibusConnectorEvidenceType.DELIVERY, message);

        if (!prevConfirmation.isPresent()) {
            throw new DomibusConnectorEvidencesToolkitException("Message contains no evidence of type "
                    + DomibusConnectorEvidenceType.DELIVERY.name() + "! No evidence of type " + DomibusConnectorEvidenceType.NON_RETRIEVAL
                    + " can be created!");
        }

        return createRetrievalNonRetrievalEvidence(false, event, prevConfirmation.get());

    }

    private Optional<Evidence> findConfirmation(DomibusConnectorEvidenceType evidenceType, MessageParameters message) {
        return message.getRelatedEvidences().stream()
                .filter(c -> c.getType().equals(evidenceType))
                .findFirst();
    }


    private byte[] createRetrievalNonRetrievalEvidence(boolean isRetrieval, EventReasonType eventReason,
                                                       Evidence previousEvidence) throws DomibusConnectorEvidencesToolkitException {
        EDeliveryDetails evidenceIssuerDetails = buildEDeliveryDetails();

        try {
            return evidenceBuilder.createRetrievalNonRetrievalByRecipient(isRetrieval, eventReason,
                    evidenceIssuerDetails, previousEvidence.getEvidence());
        } catch (ECodexEvidenceBuilderException e) {
            throw new DomibusConnectorEvidencesToolkitException(e);
        }
    }

    private byte[] createDeliveryNonDeliveryEvidence(boolean isDelivery, EventReasonType eventReason,
                                                     Evidence previousEvidence) throws DomibusConnectorEvidencesToolkitException {
        EDeliveryDetails evidenceIssuerDetails = buildEDeliveryDetails();

        try {
            return evidenceBuilder.createDeliveryNonDeliveryToRecipient(isDelivery, eventReason, evidenceIssuerDetails,
                    previousEvidence.getEvidence());
        } catch (ECodexEvidenceBuilderException e) {
            throw new DomibusConnectorEvidencesToolkitException(e);
        }
    }

    private byte[] createRelayREMMDAcceptanceRejection(boolean isAcceptance, EventReasonType eventReason,
            Evidence previousEvidence) throws DomibusConnectorEvidencesToolkitException {
        EDeliveryDetails evidenceIssuerDetails = buildEDeliveryDetails();

        try {
            return evidenceBuilder.createRelayREMMDAcceptanceRejection(isAcceptance, eventReason,
                    evidenceIssuerDetails, previousEvidence.getEvidence());
        } catch (ECodexEvidenceBuilderException e) {
            throw new DomibusConnectorEvidencesToolkitException(e);
        }
    }

    private byte[] createRelayREMMDFailure(EventReasonType eventReason, Evidence previousEvidence)
            throws DomibusConnectorEvidencesToolkitException {
        EDeliveryDetails evidenceIssuerDetails = buildEDeliveryDetails();

        try {
            return evidenceBuilder.createRelayREMMDFailure(eventReason, evidenceIssuerDetails, previousEvidence.getEvidence());
        } catch (ECodexEvidenceBuilderException e) {
            throw new DomibusConnectorEvidencesToolkitException(e);
        }
    }

    private byte[] createSubmissionAcceptanceRejection(boolean isAcceptance, EventReasonType eventReason,
                                                       MessageParameters message, String hash) throws DomibusConnectorEvidencesToolkitException {
        EDeliveryDetails evidenceIssuerDetails = buildEDeliveryDetails();


        try {
            return evidenceBuilder.createSubmissionAcceptanceRejection(isAcceptance, eventReason,
                    evidenceIssuerDetails, message.getECodexMessageDetails());
        } catch (ECodexEvidenceBuilderException e) {
            throw new DomibusConnectorEvidencesToolkitException(e);
        }
    }

    private EDeliveryDetails buildEDeliveryDetails() {
        EDeliveryDetail detail = new EDeliveryDetail();
        HomePartyConfigurationProperties homePartyConfigurationProperties = evidencesToolkitConfigurationProperties.getIssuerInfo().getAs4Party();
        PostalAdressConfigurationProperties postalAdressConfigurationProperties = evidencesToolkitConfigurationProperties.getIssuerInfo().getPostalAddress();

        EDeliveryDetail.Server server = new EDeliveryDetail.Server();
        server.setGatewayName(homePartyConfigurationProperties.getName());
        server.setGatewayAddress(homePartyConfigurationProperties.getEndpointAddress());
        detail.setServer(server);

        EDeliveryDetail.PostalAdress postalAddress = new EDeliveryDetail.PostalAdress();
        postalAddress.setStreetAddress(postalAdressConfigurationProperties.getStreet());
        postalAddress.setLocality(postalAdressConfigurationProperties.getLocality());
        postalAddress.setPostalCode(postalAdressConfigurationProperties.getZipCode());
        postalAddress.setCountry(postalAdressConfigurationProperties.getCountry());
        detail.setPostalAdress(postalAddress);

        return new EDeliveryDetails(detail);
    }


}
