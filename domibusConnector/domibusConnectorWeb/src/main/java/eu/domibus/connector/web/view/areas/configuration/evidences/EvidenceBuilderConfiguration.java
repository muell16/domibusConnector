package eu.domibus.connector.web.view.areas.configuration.evidences;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.common.service.ConfigurationPropertyManagerService;
import eu.domibus.connector.controller.spring.EvidencesTimeoutConfigurationProperties;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.evidences.spring.EvidencesToolkitConfigurationProperties;
import eu.domibus.connector.evidences.spring.PostalAdressConfigurationProperties;
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

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;


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
public class EvidenceBuilderConfiguration  extends VerticalLayout implements BeforeEnterObserver {

	public static final String ROUTE = "evidencebuilder";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final ConfigurationPropertyManagerService configurationPropertyManagerService;
	private final ConfigurationUtil util;
	
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

	private void saveButtonClicked(ClickEvent clickEvent) {
		EvidencesToolkitConfigurationProperties evidenceConfigBean = evidenceConfigBinder.getBean();
		configurationPropertyManagerService.updateConfiguration(getCurrentBusinessDomain(), evidenceConfigBean);

		PostalAdressConfigurationProperties postalAddressConfigBean = postAddrBinder.getBean();
		Set<ConstraintViolation<PostalAdressConfigurationProperties>> constraintViolations = configurationPropertyManagerService.validateConfiguration(getCurrentBusinessDomain(), postalAddressConfigBean);
		if (!constraintViolations.isEmpty()) {
			Notification.show("There are validation errors! " + constraintViolations
					.stream()
					.map(e -> e.getMessage())
					.collect(Collectors.joining(",")), 10000, Notification.Position.MIDDLE);
		}
		configurationPropertyManagerService.updateConfiguration(getCurrentBusinessDomain(), postalAddressConfigBean);

	}

	/**
	 * retrieves the currently edited business domain
	 * @return the currently edited/viewed business domain within this view/form
	 */
	private DomibusConnectorBusinessDomain.BusinessDomainId getCurrentBusinessDomain() {
		DomibusConnectorBusinessDomain.BusinessDomainId businessDomain = DomibusConnectorBusinessDomain.getDefaultMessageLaneId();
		return businessDomain;
	}

	Binder<EvidencesToolkitConfigurationProperties> evidenceConfigBinder;
	Binder<PostalAdressConfigurationProperties> postAddrBinder;

	public EvidenceBuilderConfiguration(ConfigurationUtil util,
										ConfigurationPropertyManagerService configurationPropertyManagerService,
										WebKeystoreService keystoreService) {
		this.configurationPropertyManagerService = configurationPropertyManagerService;
		this.util = util;

		Button saveEvidenceConfigButton = new Button("Save Evidence Config");
		saveEvidenceConfigButton.addClickListener(this::saveButtonClicked);
		add(saveEvidenceConfigButton);

		postAddrBinder = new Binder<>(PostalAdressConfigurationProperties.class);

		postAddrBinder.bind(addressCountryField, "country");
		postAddrBinder.bind(addressStreetField, "street");
		postAddrBinder.bind(addressLocalityField, "locality");
		postAddrBinder.bind(addressPostalCodeField, "zipCode");

		evidenceConfigBinder = new Binder<>(EvidencesToolkitConfigurationProperties.class);
		evidenceConfigBinder.bind(keyStorePathField, "keyStore.path");
		evidenceConfigBinder.bind(keyStorePasswordField, "keyStore.password");

		Button reset = new Button("Reset config");
		reset.addClickListener((e) -> this.reinitBinder());

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

	private void reinitBinder() {
		DomibusConnectorBusinessDomain.BusinessDomainId businessDomain = getCurrentBusinessDomain();
		EvidencesToolkitConfigurationProperties config = configurationPropertyManagerService.loadConfiguration(businessDomain, EvidencesToolkitConfigurationProperties.class);
		evidenceConfigBinder.setBean(config);

		PostalAdressConfigurationProperties postalAdressConfigurationProperties = configurationPropertyManagerService.loadConfiguration(businessDomain, PostalAdressConfigurationProperties.class);
		postAddrBinder.setBean(postalAdressConfigurationProperties);

	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if (event.getNavigationTarget() == this.getClass()) {
			reinitBinder();
		}
	}

}

