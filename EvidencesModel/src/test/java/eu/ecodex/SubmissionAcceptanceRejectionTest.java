package eu.ecodex;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
import org.junit.Test;
import org.xml.sax.SAXException;

import eu.ecodex.evidences.ECodexEvidenceBuilder;
import eu.ecodex.evidences.EvidenceBuilder;
import eu.ecodex.evidences.exception.ECodexEvidenceBuilderException;
import eu.ecodex.evidences.types.ECodexMessageDetails;
import eu.ecodex.signature.EvidenceUtils;
import eu.ecodex.signature.EvidenceUtilsXades;
import eu.spocseu.edeliverygw.configuration.EDeliveryDetails;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail.PostalAdress;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail.Server;
import eu.spocseu.edeliverygw.evidences.DeliveryNonDeliveryToRecipient;
import eu.spocseu.edeliverygw.evidences.SubmissionAcceptanceRejection;
import eu.spocseu.edeliverygw.messageparts.SpocsFragments;

public class SubmissionAcceptanceRejectionTest  {
	
	private static EvidenceBuilder builder = new ECodexEvidenceBuilder("D:\\git\\ecodex_evidences\\EvidencesModel\\src\\main\\resources\\evidenceBuilderStore.jks", "123456", "evidenceBuilderKey", "123456");
	private static EvidenceUtils utils = new EvidenceUtilsXades("D:\\git\\ecodex_evidences\\EvidencesModel\\src\\main\\resources\\evidenceBuilderStore.jks", "123456", "evidenceBuilderKey", "123456");
	
	
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
	
	@Test
	public void evidenceChain() throws DatatypeConfigurationException, ECodexEvidenceBuilderException, IOException {
		
		
		EDeliveryDetails details = createEntityDetailsObject();
		
		ECodexMessageDetails msgDetails = new ECodexMessageDetails();
		msgDetails.setEbmsMessageId("ebmsMessageId");
		msgDetails.setHashAlgorithm("hashAlgorithm");
		msgDetails.setHashValue(new byte[]{127, 0, 127});
		msgDetails.setNationalMessageId("nationalMessageId");
		msgDetails.setRecipientAddress("recipientAddress");
		msgDetails.setSenderAddress("senderAddress");
		
		byte[] subm = builder.createSubmissionAcceptanceRejection(true, null, details, msgDetails);
		writeFile(subm, "src/test/resources/submissionAcceptance.xml");
		assertTrue(utils.verifySignature(subm));
		
		byte[] relayrem = builder.createRelayREMMDAcceptanceRejection(false, null, details, subm);
		writeFile(relayrem, "src/test/resources/relayremmdAcceptance.xml");
		assertTrue(utils.verifySignature(relayrem));
		
		byte[] delivery = builder.createDeliveryNonDeliveryToRecipient(true, null, details, relayrem);
		writeFile(delivery, "src/test/resources/deliveryAcceptance.xml");
		assertTrue(utils.verifySignature(delivery));
		
		byte[] retrieval = builder.createRetrievalNonRetrievalByRecipient(true, null, details, delivery);
		writeFile(retrieval, "src/test/resources/retrievalAcceptance.xml");
		assertTrue(utils.verifySignature(retrieval));
		
				
		
	}
	
	private void writeFile(byte[] data, String fileName) throws IOException {
		FileOutputStream fos = new FileOutputStream(new File(fileName));
		fos.write(data);
		fos.flush();
		fos.close();
	}
	
	private SubmissionAcceptanceRejection createSubmissionAcceptance() throws DatatypeConfigurationException, JAXBException, ParserConfigurationException, SAXException, IOException, TransformerException {
		EDeliveryDetails details = createEntityDetailsObject();
		
		REMDispatchType dispatchMessage = createRemDispatchTypeObject();
		
		SubmissionAcceptanceRejection evidence = new SubmissionAcceptanceRejection(details, dispatchMessage, true);
		
//		FileOutputStream fo = new FileOutputStream("src/test/resources/SubmissionAcceptance.xml");
		ByteArrayOutputStream fo = new ByteArrayOutputStream();
		
		evidence.serialize(fo);
		
		byte[] bytes = fo.toByteArray();
		
		EvidenceUtils utils = new EvidenceUtilsXades("D:\\git\\ecodex_evidences\\EvidencesModel\\src\\main\\resources\\evidenceBuilderStore.jks", "123456", "evidenceBuilderKey", "123456");
		
		byte[] signedByteArray = utils.signByteArray(bytes);
		
		System.out.println("Ergebnis Validierung: " + utils.verifySignature(signedByteArray) );
		
		FileOutputStream fos = new FileOutputStream(new File("output_signed.xml"));
		fos.write(signedByteArray);
		fos.flush();
		fos.close();
		
		
		return evidence;
	}
	
}
