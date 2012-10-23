package eu.ecodex.evidences;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.etsi.uri._02640.soapbinding.v1.DeliveryConstraints;
import org.etsi.uri._02640.soapbinding.v1.Destinations;
import org.etsi.uri._02640.soapbinding.v1.MsgIdentification;
import org.etsi.uri._02640.soapbinding.v1.MsgMetaData;
import org.etsi.uri._02640.soapbinding.v1.Originators;
import org.etsi.uri._02640.soapbinding.v1.REMDispatchType;
import org.etsi.uri._02640.v2.EntityDetailsType;
import org.etsi.uri._02640.v2.REMEvidenceType;

import eu.ecodex.evidences.exception.ECodexEvidenceBuilderException;
import eu.ecodex.evidences.types.ECodexMessageDetails;
import eu.ecodex.signature.EvidenceUtils;
import eu.ecodex.signature.EvidenceUtilsImpl;
import eu.spocseu.common.SpocsConstants.Evidences;
import eu.spocseu.edeliverygw.REMErrorEvent;
import eu.spocseu.edeliverygw.configuration.EDeliveryDetails;
import eu.spocseu.edeliverygw.evidences.DeliveryNonDeliveryToRecipient;
import eu.spocseu.edeliverygw.evidences.Evidence;
import eu.spocseu.edeliverygw.evidences.RetrievalNonRetrievalByRecipient;
import eu.spocseu.edeliverygw.evidences.SubmissionAcceptanceRejection;
import eu.spocseu.edeliverygw.messageparts.SpocsFragments;

public class ECodexEvidenceBuilder  implements EvidenceBuilder {
	private static Logger LOG = Logger.getLogger(ECodexEvidenceBuilder.class);
	
	private static EvidenceUtils signer = null;
	
	
	
	public ECodexEvidenceBuilder(String javaKeyStorePath, String javaKeyStorePassword, String alias, String keyPassword) {
		signer = new EvidenceUtilsImpl(javaKeyStorePath, javaKeyStorePassword, alias, keyPassword);		
	}
	

	@Override
	public byte[] createSubmissionAcceptanceRejection(boolean isAcceptance,
													  REMErrorEvent eventReason,
													  EDeliveryDetails evidenceIssuerDetails,
													  ECodexMessageDetails messageDetails)
															  throws ECodexEvidenceBuilderException {
		
		
		// This is the message and all related information
		REMDispatchType dispatch = new REMDispatchType();
		
		MsgMetaData msgMetaData = new MsgMetaData();
		EntityDetailsType recipient = new EntityDetailsType();
		EntityDetailsType sender = new EntityDetailsType();
		try {
			recipient.getAttributedElectronicAddressOrElectronicAddress().add(SpocsFragments.createElectoricAddress(messageDetails.getSenderAddress(), "displayName"));
			sender.getAttributedElectronicAddressOrElectronicAddress().add(SpocsFragments.createElectoricAddress(messageDetails.getRecipientAddress(), "displayName"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		Destinations destinations = new Destinations();
		destinations.setRecipient(sender);
		
		Originators originators = new Originators();
		originators.setFrom(recipient);
		originators.setReplyTo(recipient);
		originators.setSender(recipient);
		
		MsgIdentification msgIdentification = new MsgIdentification();
		msgIdentification.setMessageID(messageDetails.getEbmsMessageId());
		
		msgMetaData.setDestinations(destinations);
		msgMetaData.setOriginators(originators);
		msgMetaData.setMsgIdentification(msgIdentification);
		
		GregorianCalendar cal = new GregorianCalendar();
		XMLGregorianCalendar initialSend = null;
		try {
			initialSend = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e1) {
			e1.printStackTrace();
		}
		DeliveryConstraints deliveryConstraints = new DeliveryConstraints();
		deliveryConstraints.setInitialSend(initialSend);
		
		msgMetaData.setDeliveryConstraints(deliveryConstraints);
		
		dispatch.setMsgMetaData(msgMetaData);
		
		SubmissionAcceptanceRejection evidence = new SubmissionAcceptanceRejection(evidenceIssuerDetails, dispatch, isAcceptance);
		//Nur wenn ein Fehler aufgetreten ist ausfüllen.
		if(!isAcceptance) 
			evidence.setEventReason(eventReason);
		
		evidence.setUAMessageId(messageDetails.getNationalMessageId());
		evidence.setHashInformation(messageDetails.getHashValue(), messageDetails.getHashAlgorithm());
		
		
		byte[] signedByteArray = signEvidence(evidence, false);
		
		
		return signedByteArray;
	}
	

	@Override
	public byte[] createDeliveryNonDeliveryToRecipient(boolean isDelivery,
													   REMErrorEvent eventReason, 
												   	   REMEvidenceType previousEvidence) 
												   			   throws ECodexEvidenceBuilderException {
		
		
		DeliveryNonDeliveryToRecipient evidence = new DeliveryNonDeliveryToRecipient(previousEvidence);
		
		if (isDelivery) {
			LOG.debug("Create DeliveryNonDeliveryToRecipient in success case.");
			evidence.getXSDObject().setEventCode(Evidences.DELIVERY_NON_DELIVERY_TO_RECIPIENT
					.getSuccessEventCode());
			
		} else {
			LOG.debug("Create DeliveryNonDeliveryToRecipient in fault case.");
			evidence.getXSDObject().setEventCode(Evidences.DELIVERY_NON_DELIVERY_TO_RECIPIENT
					.getFaultEventCode());
			if(eventReason != null)
				evidence.setEventReason(eventReason);
		}
		
		byte[] signedByteArray = signEvidence(evidence, true);
		
		return signedByteArray;
	}


	@Override
	public byte[] createRetrievalNonRetrievalByRecipient(boolean isRetrieval,
														 REMErrorEvent eventReason, 
														 REMEvidenceType previousEvidence) 
																 throws ECodexEvidenceBuilderException {
		
		
		RetrievalNonRetrievalByRecipient evidence = new RetrievalNonRetrievalByRecipient(previousEvidence);
		
		if (isRetrieval) {
			LOG.debug("Create RetrievalNonRetrievalByRecipient in success case.");
			evidence.getXSDObject().setEventCode(Evidences.RETRIEVAL_NON_RETRIEVAL_BY_RECIPIENT
					.getSuccessEventCode());
			
		} else {
			LOG.debug("Create RetrievalNonRetrievalByRecipient in fault case.");
			evidence.getXSDObject().setEventCode(Evidences.RETRIEVAL_NON_RETRIEVAL_BY_RECIPIENT
					.getFaultEventCode());
			if(eventReason != null)
				evidence.setEventReason(eventReason);
		}
		
		
		byte[] signedByteArray = signEvidence(evidence, true);
		
		return signedByteArray;
	}


	private byte[] signEvidence(Evidence evidenceToBeSigned, boolean removeOldSignature) {
		
		if(removeOldSignature) {
			// delete old signature field
			evidenceToBeSigned.getXSDObject().setSignature(null);
			LOG.debug("Old Signature removed");
		}
		
		
		ByteArrayOutputStream fo = new ByteArrayOutputStream();
		
		
		try {
			
			evidenceToBeSigned.serialize(fo);
		} catch (JAXBException e) {
			e.printStackTrace();
		} 
		
		byte[] bytes = fo.toByteArray();
		
		byte[] signedByteArray = signer.signByteArray(bytes);
		
		return signedByteArray;
	}


	
	
	
	

}
