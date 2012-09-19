package eu.ecodex;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.spocseu.edeliverygw.configuration.EDeliveryDetails;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail.PostalAdress;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail.Server;
import eu.spocseu.edeliverygw.evidences.SubmissionAcceptanceRejection;
import eu.spocseu.edeliverygw.messageparts.SpocsFragments;

public class SubmissionAcceptanceRejectionTest  {

	private static Logger LOG = LoggerFactory.getLogger(SubmissionAcceptanceRejectionTest.class);
	
	@Test
	public void createSubmissionAcceptanceFromEDeliverDetails() throws DatatypeConfigurationException, JAXBException, MalformedURLException, FileNotFoundException {
		
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
		
		EDeliveryDetails details = new EDeliveryDetails(detail);
		
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
		
		SubmissionAcceptanceRejection evidence = new SubmissionAcceptanceRejection(details, dispatchMessage, true);
		
		FileOutputStream fo = new FileOutputStream("src/test/resources/test.xsd");
		evidence.serialize(fo);
		
		
		
		assertTrue(true);
		
	}
	
}
