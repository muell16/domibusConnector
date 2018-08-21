package eu.domibus.connector.web.viewAreas.configuration.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.web.forms.FormsUtil;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationUtil;

/**
 * 
 * This class should handle the following:
 * 
 * Edit the token issuer information.
 * 
 * Edit the lotl settings
 * 
 * Edit the keystore/truststore for the security toolkit
 * 
 * ################ Properties for security toolkit from properties file: #######################
 * 
 * #############################  ASIC-S keystore  #############################

		# To be able to sign the secure contents of the message, a keystore with certificate and private key integrated must be used. Here are the
		# credentials to set.
		connector.security.keyStore.path=file:C:/Entwicklung/EXEC-Installation-Workshop/EXECUser1/configuration/keystores/connector-execuser1.jks
		connector.security.keyStore.password=12345
		connector.security.privateKey.alias=execcon1
		connector.security.privateKey.password=12345

		#############################  connector truststore  #############################

		# Holds all public keys of partners to validate the received ASIC-S containers against. Also contains public keys of signatures,
		# if documents to be sent are signed.
		connector.security.truststore.path=file:C:/Entwicklung/EXEC-Installation-Workshop/EXECUser1/configuration/keystores/connector-truststore.jks
		connector.security.truststore.password=12345
		
		# Settings for the security library for generating the Token.
			token.issuer.country=AT
			token.issuer.service.provider=TestProvider
			# This can rather be SIGNATURE_BASED, then the main document needs to be signed, or AUTHENTICATION_BASED, in that case the security interface needs
			# to be implemented (see connector.security.toolkit.implementation.class.name)
			token.issuer.aes.value=AUTHENTICATION_BASED
			
			# The connectors security lib is loading trusted lists over
			# the network:
			
			security.lotl.scheme.uri=https://ec.europa.eu/information_society/policy/esignature/trusted-list/tl.html
			security.lotl.url=https://ec.europa.eu/information_society/policy/esignature/trusted-list/tl-mp.xml
			security.oj.url=http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=uriserv:OJ.C_.2016.233.01.0001.01.ENG


 * 
 */
@HtmlImport("styles/shared-styles.html")
@StyleSheet("styles/grid.css")
@Component
@UIScope
public class SecurityConfiguration extends VerticalLayout{
	
	ConfigurationUtil util;
	
	TextField tokenIssuerCountryField = FormsUtil.getFormattedTextField();
	TextField tokenIssuerServiceProviderField = FormsUtil.getFormattedTextField();
	ComboBox<String> tokenIssuerAESValueBox = new ComboBox<String>();
	
	TextField lotlSchemeURIField = FormsUtil.getFormattedTextField();
	TextField lotlURLField = FormsUtil.getFormattedTextField();
	TextField ojURIField = FormsUtil.getFormattedTextField();
	
	TextField keyStorePathField = FormsUtil.getFormattedTextField();
	TextField keyStorePasswordField = FormsUtil.getFormattedTextField();
	TextField keyAliasField = FormsUtil.getFormattedTextField();
	TextField keyPasswordField = FormsUtil.getFormattedTextField();
	TextField truststorePathField = FormsUtil.getFormattedTextField();
	TextField truststorePasswordField = FormsUtil.getFormattedTextField();

	public SecurityConfiguration(@Autowired ConfigurationUtil util) {
		this.util = util;
		
		add(util.createChapterDiv("Token issuer configuration:"));
		
		add(util.createConfigurationTextFieldWithLabels(SecurityConfigurationLabels.tokenIssuerCountryLabels, tokenIssuerCountryField));
		
		add(util.createConfigurationTextFieldWithLabels(SecurityConfigurationLabels.tokenIssuerServiceProviderLabels, tokenIssuerServiceProviderField));
		
		List<String> items = new ArrayList<String>();
		items.add("SIGNATURE_BASED");
		items.add("AUTHENTICATION_BASED");
		add(util.createConfigurationComboBoxWithItemsAndLabels(SecurityConfigurationLabels.tokenIssuerAESValueLabels, tokenIssuerAESValueBox, items, util.getPropertyValue(SecurityConfigurationLabels.tokenIssuerAESValueLabels.PROPERTY_NAME_LABEL)));
		
		add(util.createChapterDiv("Trusted lists configuration:"));
		
		add(util.createConfigurationTextFieldWithLabels(SecurityConfigurationLabels.lotlSchemeURILabels, lotlSchemeURIField));
		
		add(util.createConfigurationTextFieldWithLabels(SecurityConfigurationLabels.lotlURLLabels, lotlURLField));
		
		add(util.createConfigurationTextFieldWithLabels(SecurityConfigurationLabels.ojURLLabels, ojURIField));
		
		add(util.createChapterDiv("Keystore/Truststore configuration:"));
		
		add(util.createConfigurationTextFieldWithLabels(SecurityConfigurationLabels.securityKeyStorePathLabels, keyStorePathField));
		
		add(util.createConfigurationTextFieldWithLabels(SecurityConfigurationLabels.securityKeyStorePasswordLabels, keyStorePasswordField));
		
		add(util.createConfigurationTextFieldWithLabels(SecurityConfigurationLabels.securityKeyAliasLabels, keyAliasField));
		
		add(util.createConfigurationTextFieldWithLabels(SecurityConfigurationLabels.securityKeyPasswordLabels, keyPasswordField));
		
		add(util.createConfigurationTextFieldWithLabels(SecurityConfigurationLabels.securityTrustStorePathLabels, truststorePathField));
		
		add(util.createConfigurationTextFieldWithLabels(SecurityConfigurationLabels.securityTrustStorePasswordLabels, truststorePasswordField));
	}

}
