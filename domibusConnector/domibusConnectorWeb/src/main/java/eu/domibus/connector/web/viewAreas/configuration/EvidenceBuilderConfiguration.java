package eu.domibus.connector.web.viewAreas.configuration;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;

@HtmlImport("styles/shared-styles.html")
@StyleSheet("styles/grid.css")
@Component
@UIScope
public class EvidenceBuilderConfiguration  extends VerticalLayout {

	/**
	 * 
	 * This class should handle the following:
	 * 
	 * Edit the timeout cron settings for all evidence timeout jobs
	 * 
	 * Edit the information put into evidences (GW, Address)
	 * 
	 * Edit the keystore information for evidence generation
	 * 
	 * 
	 * ################ Properties for evidence builder from properties file: #######################
	 * 
	 * # All timeout values apply the following rules:
		# -- Set to 0, they will be ignored
		# -- After a message is rejected, also a positive received evidence afterwards does not change the status.
		# -- All values can be defined as h hours, m minutes, s seconds, ms milliseconds
		
		# boolean value to turn on/off the check if evidences for outgoing messages have been received yet.
		connector.controller.evidence.timeoutActive=true
		# How often should the timeout of an evidence be checked
		# The default is every hour the messages are checked for timed out evidences
		connector.controller.evidence.checkTimeout=1h
		# After this period, if an outgoing message was sent to the gateway successfully and no RelayREMMD evidence was received,
		# a RelayREMMDRejection will be created for this evidence an sent back to the national system.
		connector.controller.evidence.relayREMMDTimeout=24h
		# After this period, if an outgoing message was sent to the gateway successfully and no Delivery/Non-Delivery evidence was received,
		# a Non-Delivery will be created for this evidence an sent back to the national system.
		connector.controller.evidence.deliveryTimeout=24h
		# After this period, if an outgoing message was sent to the gateway successfully and no Retrieval/Non-Retrieval evidence was received,
		# a Non-Retrieval will be created for this evidence an sent back to the national system.
		connector.controller.evidence.retrievalTimeout=24h
		
		# This property should show the EXTERNAL address where the gateway can be reached. Should match the address published with the pmodes.
		gateway.endpoint.address=http://127.0.0.1:8080/domibus/services/msh
		
		#This is the name of your Gateway. This should match the PartyID within the pmodes.
		gateway.name=Test-GW
		
		# Address data for the generating of the evidences.
		postal.address.street=Teststreet 1
		postal.address.locality=Testcity	
		postal.address.postal.code=12345
		postal.address.country=AT
		
		#############################  evidences keystore  #############################
		# To be able to sign evidences a keystore with certificate and private key integrated must be used. Here are the
		# credentials to set.
		connector.evidences.keyStore.path=file:C:/Entwicklung/EXEC-Installation-Workshop/EXECUser1/configuration/keystores/connector-execuser1.jks
		# if the keystore is password protected enter the password here:
		connector.evidences.keyStore.password=12345
		# if the
		connector.evidences.privateKey.alias=execcon1
		connector.evidences.privateKey.password=12345
	 * 
	 * 
	 */
	public EvidenceBuilderConfiguration() {
		// TODO Auto-generated constructor stub
	}

}
