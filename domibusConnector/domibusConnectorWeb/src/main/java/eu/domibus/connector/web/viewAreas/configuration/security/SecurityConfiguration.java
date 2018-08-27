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
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationItemChapterDiv;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationProperties;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationUtil;

/**
 * 
 * This class should handle the following properties:
 * 
 * connector.security.keyStore.path
 * connector.security.keyStore.password
 * connector.security.privateKey.alias
 * connector.security.privateKey.password
 *
 * connector.security.truststore.path
 * connector.security.truststore.password
 *	
 * token.issuer.country
 * token.issuer.service.provider
 * token.issuer.aes.value
 *
 * security.lotl.scheme.uri
 * security.lotl.url
 * security.oj.url
 * 
 */
@HtmlImport("styles/shared-styles.html")
//@StyleSheet("styles/grid.css")
@Component
@UIScope
public class SecurityConfiguration extends VerticalLayout{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
		
		add(new ConfigurationItemChapterDiv("Token issuer configuration:"));
		
		add(util.createConfigurationItemTextFieldDiv(SecurityConfigurationLabels.tokenIssuerCountryLabels, tokenIssuerCountryField));
		
		add(util.createConfigurationItemTextFieldDiv(SecurityConfigurationLabels.tokenIssuerServiceProviderLabels, tokenIssuerServiceProviderField));
		
		List<String> aesValues = new ArrayList<String>();
		aesValues.add("SIGNATURE_BASED");
		aesValues.add("AUTHENTICATION_BASED");
		add(util.createConfigurationItemComboBoxDiv(
				SecurityConfigurationLabels.tokenIssuerAESValueLabels, 
				tokenIssuerAESValueBox, 
				aesValues
				));
		
		add(new ConfigurationItemChapterDiv("Trusted lists configuration:"));
		
		add(util.createConfigurationItemTextFieldDiv(SecurityConfigurationLabels.lotlSchemeURILabels, lotlSchemeURIField));
		
		add(util.createConfigurationItemTextFieldDiv(SecurityConfigurationLabels.lotlURLLabels, lotlURLField));
		
		add(util.createConfigurationItemTextFieldDiv(SecurityConfigurationLabels.ojURLLabels, ojURIField));
		
		add(new ConfigurationItemChapterDiv("Keystore/Truststore configuration:"));
		
		add(util.createConfigurationItemTextFieldDiv(SecurityConfigurationLabels.securityKeyStorePathLabels, keyStorePathField));
		
		add(util.createConfigurationItemTextFieldDiv(SecurityConfigurationLabels.securityKeyStorePasswordLabels, keyStorePasswordField));
		
		add(util.createConfigurationItemTextFieldDiv(SecurityConfigurationLabels.securityKeyAliasLabels, keyAliasField));
		
		add(util.createConfigurationItemTextFieldDiv(SecurityConfigurationLabels.securityKeyPasswordLabels, keyPasswordField));
		
		add(util.createConfigurationItemTextFieldDiv(SecurityConfigurationLabels.securityTrustStorePathLabels, truststorePathField));
		
		add(util.createConfigurationItemTextFieldDiv(SecurityConfigurationLabels.securityTrustStorePasswordLabels, truststorePasswordField));
	}

}
