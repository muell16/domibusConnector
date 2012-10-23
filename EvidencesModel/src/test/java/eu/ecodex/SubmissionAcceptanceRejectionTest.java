package eu.ecodex;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.etsi.uri._01903.v1_3.AnyType;
import org.etsi.uri._02640.soapbinding.v1.DeliveryConstraints;
import org.etsi.uri._02640.soapbinding.v1.Destinations;
import org.etsi.uri._02640.soapbinding.v1.MsgIdentification;
import org.etsi.uri._02640.soapbinding.v1.MsgMetaData;
import org.etsi.uri._02640.soapbinding.v1.OriginalMsgType;
import org.etsi.uri._02640.soapbinding.v1.Originators;
import org.etsi.uri._02640.soapbinding.v1.REMDispatchType;
import org.etsi.uri._02640.v2.EntityDetailsType;
import org.etsi.uri._02640.v2.EntityNameType;
import org.etsi.uri._02640.v2.NamePostalAddressType;
import org.etsi.uri._02640.v2.NamesPostalAddressListType;
import org.etsi.uri._02640.v2.PostalAddressType;
import org.etsi.uri._02640.v2.REMEvidenceType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import eu.ecodex.evidences.ECodexEvidenceBuilder;
import eu.ecodex.evidences.EvidenceBuilder;
import eu.ecodex.evidences.exception.ECodexEvidenceBuilderException;
import eu.ecodex.evidences.types.ECodexMessageDetails;
import eu.ecodex.signature.EvidenceUtils;
import eu.ecodex.signature.EvidenceUtilsImpl;
import eu.spocseu.edeliverygw.REMErrorEvent;
import eu.spocseu.edeliverygw.configuration.EDeliveryDetails;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail.PostalAdress;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail.Server;
import eu.spocseu.edeliverygw.evidences.DeliveryNonDeliveryToRecipient;
import eu.spocseu.edeliverygw.evidences.SubmissionAcceptanceRejection;
import eu.spocseu.edeliverygw.messageparts.SpocsFragments;

public class SubmissionAcceptanceRejectionTest  {

	private static Logger LOG = LoggerFactory.getLogger(SubmissionAcceptanceRejectionTest.class);
	
	private EDeliveryDetails createEntityDetailsObject() {
		
		PostalAdress address = new PostalAdress();
		address.setCountry("country");
		address.setLocality("locality");
		address.setPostalCode("postalcode");
		address.setStreetAddress("streetaddress");
		
		Server server = new Server();
		server.setDefaultCitizenQAAlevel(1);
		server.setGatewayAddress("gatewayaddress");
		server.setGatewayDomain("gatewaydomain");
		server.setGatewayName("gatewayname");
		
		EDeliveryDetail detail = new EDeliveryDetail();
		
		detail.setPostalAdress(address);
		detail.setServer(server);
		
		
		return new EDeliveryDetails(detail);
	}
	
	
	private REMDispatchType createRemDispatchTypeObject() throws MalformedURLException, DatatypeConfigurationException {
		GregorianCalendar cal = new GregorianCalendar();
		XMLGregorianCalendar testDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		
		
		DeliveryConstraints deliveryConstraints = new DeliveryConstraints();
		deliveryConstraints.setAny(new AnyType());
		deliveryConstraints.setInitialSend(testDate);
		deliveryConstraints.setObsoleteAfter(testDate);
		deliveryConstraints.setOrigin(testDate);
		
		
		PostalAddressType postAdressType = new PostalAddressType();
		postAdressType.setCountryName("countryName");
		postAdressType.setLang("lang");
		postAdressType.setLocality("locality");
		postAdressType.setPostalCode("postalcode");
		postAdressType.setStateOrProvince("stateOrProvince");
		
		EntityNameType entityNameType = new EntityNameType();
		entityNameType.setLang("lang");
		
		NamePostalAddressType namesPostal = new NamePostalAddressType();
		namesPostal.setPostalAddress(postAdressType);
		namesPostal.setEntityName(entityNameType);
		
		NamesPostalAddressListType namesPostalList = new NamesPostalAddressListType();
		namesPostalList.getNamePostalAddress().add(namesPostal);
		
		
		EntityDetailsType recipient = new EntityDetailsType();
		recipient.setNamesPostalAddresses(namesPostalList);	
		recipient.getAttributedElectronicAddressOrElectronicAddress().add(SpocsFragments.createElectoricAddress("stefan.mueller@it.nrw.de", "displayName"));
		
		Destinations destinations = new Destinations();
		destinations.setRecipient(recipient);
		
		MsgIdentification msgIdentification = new MsgIdentification();
		msgIdentification.setMessageID("messageID");
		
		Originators originators = new Originators();
		originators.setFrom(recipient);
		originators.setReplyTo(recipient);
		originators.setSender(recipient);
		
		
		MsgMetaData msgMetaData = new MsgMetaData();
		msgMetaData.setDeliveryConstraints(deliveryConstraints);
		msgMetaData.setDestinations(destinations);
		msgMetaData.setMsgIdentification(msgIdentification);
		msgMetaData.setOriginators(originators);
		
		
		byte[] contentValue = {0x000A, 0x000A};
				
		OriginalMsgType orgMsgType = new OriginalMsgType();
		orgMsgType.setContentType("contentType");
		orgMsgType.setSize(BigInteger.valueOf(1000));
		orgMsgType.setValue(contentValue);
		
		
		REMDispatchType dispatchMessage = new REMDispatchType();
		dispatchMessage.setId("id");
		dispatchMessage.setMsgMetaData(msgMetaData);
		dispatchMessage.setOriginalMsg(orgMsgType);
		
		return dispatchMessage;
	}
	
	@Test
	public void createEvidences() throws DatatypeConfigurationException, JAXBException, ParserConfigurationException, SAXException, IOException, TransformerException {
		
		SubmissionAcceptanceRejection evidence1 = createSubmissionAcceptance();
		
		DeliveryNonDeliveryToRecipient evidence2 = new DeliveryNonDeliveryToRecipient(createEntityDetailsObject(), evidence1);
		
		FileOutputStream fo = new FileOutputStream("src/test/resources/DeliveryNonDelivery.xml");
		
		
		evidence2.serialize(fo);
		
		
		assertTrue(true);
		
	}
	
	private SubmissionAcceptanceRejection createSubmissionAcceptance() throws DatatypeConfigurationException, JAXBException, ParserConfigurationException, SAXException, IOException, TransformerException {
		EDeliveryDetails details = createEntityDetailsObject();
		
		REMDispatchType dispatchMessage = createRemDispatchTypeObject();
		
		SubmissionAcceptanceRejection evidence = new SubmissionAcceptanceRejection(details, dispatchMessage, true);
		
//		FileOutputStream fo = new FileOutputStream("src/test/resources/SubmissionAcceptance.xml");
		ByteArrayOutputStream fo = new ByteArrayOutputStream();
		
		evidence.serialize(fo);
		
		byte[] bytes = fo.toByteArray();
		
		EvidenceUtils utils = new EvidenceUtilsImpl("D:\\git\\ecodex_evidences\\EvidencesModel\\src\\main\\resources\\evidenceBuilderStore.jks", "123456", "evidenceBuilderKey", "123456");
		
		byte[] signedByteArray = utils.signByteArray(bytes);
		
//		FileOutputStream fos = new FileOutputStream(new File("output_signed.xml"));
//		fos.write(signedByteArray);
//		fos.flush();
//		fos.close();

		
		return evidence;
	}
	

	
	@Test
	public void testEcodexEvidence() {
		
		EDeliveryDetails evidenceIssuerDetails = createEntityDetailsObject();
		ECodexMessageDetails messageDetails = new ECodexMessageDetails();
		messageDetails.setEbmsMessageId("ebms3MsgId");
		messageDetails.setHashAlgorithm("sha1");
		messageDetails.setHashValue(new byte[]{0x000A, 0x000A});
		messageDetails.setNationalMessageId("nationalMsgId");
		messageDetails.setRecipientAddress("recipientAddress");
		messageDetails.setSenderAddress("senderAddress");
		
		EvidenceBuilder builder = new ECodexEvidenceBuilder("D:\\git\\ecodex_evidences\\EvidencesModel\\src\\main\\resources\\evidenceBuilderStore.jks", "123456", "evidenceBuilderKey", "123456");
		
		try {
			byte[] evidenceAsByteArray = builder.createSubmissionAcceptanceRejection(true, REMErrorEvent.OTHER, evidenceIssuerDetails, messageDetails);
			
			EvidenceUtils utils = new EvidenceUtilsImpl("", "", "", "");
			
			REMEvidenceType evidenceType = utils.convertIntoEvidenceType(evidenceAsByteArray);
			
			byte[] signedByteArray = builder.createDeliveryNonDeliveryToRecipient(true, null, evidenceType);
			
			
			
//			FileOutputStream fos = new FileOutputStream(new File("output2_signed.xml"));
//			fos.write(signedByteArray);
//			fos.flush();
//			fos.close();
			
			
			evidenceType = utils.convertIntoEvidenceType(signedByteArray);
			
			signedByteArray = builder.createRetrievalNonRetrievalByRecipient(false, REMErrorEvent.OTHER, evidenceType);
			
//			fos = new FileOutputStream(new File("output3_signed.xml"));
//			fos.write(signedByteArray);
//			fos.flush();
//			fos.close();
			
			
		} catch (ECodexEvidenceBuilderException e) {
			e.printStackTrace();
		} 
		
	}
	
	
}
