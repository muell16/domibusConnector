package eu.ecodex.connector.evidences;

import java.util.Date;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;

import org.etsi.uri._02640.v2.AttributedElectronicAddressType;
import org.etsi.uri._02640.v2.EntityDetailsListType;
import org.etsi.uri._02640.v2.EntityDetailsType;
import org.etsi.uri._02640.v2.MessageDetailsType;
import org.etsi.uri._02640.v2.ObjectFactory;
import org.etsi.uri._02640.v2.REMEvidenceType;

import eu.ecodex.connector.evidences.exception.EvidencesToolkitException;
import eu.ecodex.connector.evidences.type.EvidenceInput;
import eu.spocseu.edeliverygw.configuration.EDeliveryDetails;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail.PostalAdress;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail.Server;
import eu.spocseu.edeliverygw.messageparts.SpocsFragments;

public class ECodexConnectorEvidencesToolkitImpl implements ECodexConnectorEvidencesToolkit {

    public final REMEvidenceType createEvidenceMessage(final EvidenceInput input) throws EvidencesToolkitException {
    	
    	try {
    		EDeliveryDetails details = createEntityDetailsObject();
	    
		    REMEvidenceType evidenceMessage = new ObjectFactory().createREMEvidenceType();
	
		    evidenceMessage.setVersion("2.1.1");
		    
		    evidenceMessage.setEventCode("http:uri.etsi.org/02640/Event#Delivery");
		    
			evidenceMessage.setEvidenceIdentifier(UUID.randomUUID().toString());
			
			EntityDetailsType issuerDetails = new ObjectFactory().createEntityDetailsType(); 
			AttributedElectronicAddressType elAddre = new AttributedElectronicAddressType();
			elAddre.setValue(details.getGatewayAddress());
			elAddre.setScheme("mailto");
			issuerDetails.getAttributedElectronicAddressOrElectronicAddress().add(elAddre);
			evidenceMessage.setEvidenceIssuerDetails(issuerDetails);
			
			evidenceMessage.setEventTime(SpocsFragments.createXMLGregorianCalendar(new Date()));
			
			AttributedElectronicAddressType reAddre = new ObjectFactory().createAttributedElectronicAddressType(); 
			reAddre.setValue(details.getGatewayAddress());
			reAddre.setScheme("mailto");
			reAddre.setDisplayName(details.getGatewayName());
			evidenceMessage.setReplyToAddress(reAddre);
			
			EntityDetailsType detailsType = new ObjectFactory().createEntityDetailsType(); 
			AttributedElectronicAddressType seAddre = new ObjectFactory().createAttributedElectronicAddressType(); 
			seAddre.setDisplayName(details.getGatewayName());
			seAddre.setValue(details.getGatewayAddress());
			detailsType.getAttributedElectronicAddressOrElectronicAddress().add(seAddre);
			evidenceMessage.setSenderDetails(detailsType);
	
			EntityDetailsListType detailList = new ObjectFactory().createEntityDetailsListType(); 
			EntityDetailsType recipienttDetailsType = new ObjectFactory().createEntityDetailsType(); 
			AttributedElectronicAddressType recAddre = new ObjectFactory().createAttributedElectronicAddressType(); 
			recAddre.setDisplayName(input.getRecipientName());
			recAddre.setValue(input.getRecipientAddress());
			recipienttDetailsType.getAttributedElectronicAddressOrElectronicAddress().add(recAddre);
			evidenceMessage.setSenderDetails(recipienttDetailsType);
			detailList.getEntityDetails().add(recipienttDetailsType);
			evidenceMessage.setRecipientsDetails(detailList);
			
			MessageDetailsType messageDetailsType = new ObjectFactory().createMessageDetailsType(); 
			messageDetailsType.setIsNotification(true);
			messageDetailsType.setMessageSubject(input.getComment());
			messageDetailsType.setMessageIdentifierByREMMD(input.getMessageId());
			evidenceMessage.setNotificationMessageDetails(messageDetailsType);
	    	
	    	return evidenceMessage;
	    	
    	} catch (DatatypeConfigurationException e) {
    		throw new EvidencesToolkitException(e);
    	}
    }
    
	private EDeliveryDetails createEntityDetailsObject() {
		
		//TODO: to be read from a configuration file. where?
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
}
