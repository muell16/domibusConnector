package eu.domibus.connector.web.view.areas.configuration.evidences;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.web.component.LumoCheckbox;
import eu.domibus.connector.web.forms.FormsUtil;
import eu.domibus.connector.web.service.WebKeystoreService;
import eu.domibus.connector.web.utils.RoleRequired;
import eu.domibus.connector.web.view.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.web.view.areas.configuration.TabMetadata;
import eu.domibus.connector.web.view.areas.configuration.util.ConfigurationItemChapterDiv;
import eu.domibus.connector.web.view.areas.configuration.util.ConfigurationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


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
	postal.address.zip-code
	postal.address.country

	connector.evidences.keyStore.path
	connector.evidences.keyStore.password
	connector.evidences.privateKey.alias
	connector.evidences.privateKey.password
 * 
 */
@Component
@UIScope
@Route(value = EvidenceBuilderConfiguration.ROUTE, layout = ConfigurationLayout.class)
@RoleRequired(role = "ADMIN")
@TabMetadata(title = "Evidence Builder Configuration", tabGroup = ConfigurationLayout.TAB_GROUP_NAME)
public class EvidenceBuilderConfiguration  extends VerticalLayout {

	public static final String ROUTE = "evidencebuilder";

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
