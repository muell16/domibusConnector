package eu.domibus.connector.web.viewAreas.configuration.evidences;

import java.security.KeyStore;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.web.component.LumoCheckbox;
import eu.domibus.connector.web.dto.WebMessageEvidence;
import eu.domibus.connector.web.forms.FormsUtil;
import eu.domibus.connector.web.service.WebKeystoreService;
import eu.domibus.connector.web.service.WebKeystoreService.CertificateInfo;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationItemChapterDiv;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationUtil;

/**
 * @author riederb
 *
 * This class should handle the following parameters:
 * 
 * 	connector.controller.evidence.timeoutActive
 *	connector.controller.evidence.checkTimeout
	connector.controller.evidence.relayREMMDTimeout
	connector.controller.evidence.deliveryTimeout
	connector.controller.evidence.retrievalTimeout

	gateway.endpoint.address

	gateway.name

	postal.address.street
	postal.address.locality	
	postal.address.postal.code
	postal.address.country

	connector.evidences.keyStore.path
	connector.evidences.keyStore.password
	connector.evidences.privateKey.alias
	connector.evidences.privateKey.password
 * 
 */
@HtmlImport("styles/shared-styles.html")
//@StyleSheet("styles/grid.css")
@Component
@UIScope
public class EvidenceBuilderConfiguration  extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ConfigurationUtil util;
	
	LumoCheckbox useEvidenceTimeout = new LumoCheckbox();
	TextField checkIntervalField = FormsUtil.getFormattedTextField();
	TextField relayTimeoutField = FormsUtil.getFormattedTextField();
	TextField deliveryTimoutField = FormsUtil.getFormattedTextField();
	TextField retrievalTimeoutField = FormsUtil.getFormattedTextField();
	
	TextField gatewayAddressField = FormsUtil.getFormattedTextField();
	TextField gatewayNameField = FormsUtil.getFormattedTextField();
	TextField addressStreetField = FormsUtil.getFormattedTextField();
	TextField addressLocalityField = FormsUtil.getFormattedTextField();
	TextField addressPostalCodeField = FormsUtil.getFormattedTextField();
	TextField addressCountryField = FormsUtil.getFormattedTextField();
	
	TextField keyStorePathField = FormsUtil.getFormattedTextField();
	TextField keyStorePasswordField = FormsUtil.getFormattedTextField();
	TextField keyAliasField = FormsUtil.getFormattedTextField();
	TextField keyPasswordField = FormsUtil.getFormattedTextField();
	
	public EvidenceBuilderConfiguration(@Autowired ConfigurationUtil util, @Autowired WebKeystoreService keystoreService) {
		this.util = util;
		
		add(new ConfigurationItemChapterDiv("Evidence timeout configuration:"));
		
		useEvidenceTimeout.addValueChangeListener(e -> {
			checkIntervalField.setReadOnly(!e.getValue());
			relayTimeoutField.setReadOnly(!e.getValue());
			deliveryTimoutField.setReadOnly(!e.getValue());
			retrievalTimeoutField.setReadOnly(!e.getValue());
		});
		
		add(util.createConfigurationItemCheckboxDiv(EvidenceBuilderConfigurationLabels.evidenceTimoutActiveLabels, useEvidenceTimeout));
		
		add(util.createConfigurationItemTextFieldDiv(EvidenceBuilderConfigurationLabels.checkTimoutIntervalLabels, checkIntervalField));
		
		add(util.createConfigurationItemTextFieldDiv(EvidenceBuilderConfigurationLabels.relayTimoutLabels, relayTimeoutField));
		
		add(util.createConfigurationItemTextFieldDiv(EvidenceBuilderConfigurationLabels.deliveryTimoutLabels, deliveryTimoutField));
		
		add(util.createConfigurationItemTextFieldDiv(EvidenceBuilderConfigurationLabels.retrievalTimoutLabels, retrievalTimeoutField));
		
		add(new ConfigurationItemChapterDiv("Data put into evidences:"));
		
		add(util.createConfigurationItemTextFieldDiv(EvidenceBuilderConfigurationLabels.endpointAddressLabels, gatewayAddressField));
		
		add(util.createConfigurationItemTextFieldDiv(EvidenceBuilderConfigurationLabels.gatewayNameLabels, gatewayNameField));
		
		add(util.createConfigurationItemTextFieldDiv(EvidenceBuilderConfigurationLabels.postalAddressStreetLabels, addressStreetField));
		
		add(util.createConfigurationItemTextFieldDiv(EvidenceBuilderConfigurationLabels.postalAddressLocalityLabels, addressLocalityField));
		
		add(util.createConfigurationItemTextFieldDiv(EvidenceBuilderConfigurationLabels.postalAddressPostalCodeLabels, addressPostalCodeField));
		
		add(util.createConfigurationItemTextFieldDiv(EvidenceBuilderConfigurationLabels.postalAddressCountryLabels, addressCountryField));
		
		add(new ConfigurationItemChapterDiv("Evidences keystore/key:"));
		
		keyStorePathField.setWidth("900px");
		add(util.createConfigurationItemTextFieldDiv(EvidenceBuilderConfigurationLabels.evidencesKeyStorePathLabels, keyStorePathField));
		
		add(util.createConfigurationItemTextFieldDiv(EvidenceBuilderConfigurationLabels.evidencesKeyStorePasswordLabels, keyStorePasswordField));
		
		add(util.createKeystoreInformationGrid(keyStorePathField, keyStorePasswordField));
		
		add(util.createConfigurationItemTextFieldDiv(EvidenceBuilderConfigurationLabels.evidencesKeyAliasLabels, keyAliasField));
		
		add(util.createConfigurationItemTextFieldDiv(EvidenceBuilderConfigurationLabels.evidencesKeyPasswordLabels, keyPasswordField));
	}

}
