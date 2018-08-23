package eu.domibus.connector.web.viewAreas.configuration.backend;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationItemCheckboxDiv;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationItemDiv;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationItemTextFieldDiv;

public class BackendClientInfo extends VerticalLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BackendClientInfo(DomibusConnectorBackendClientInfo domibusConnectorBackendClientInfo, BackendConfiguration backendConfiguration, Dialog newBackendClientDialog) {
		DomibusConnectorBackendClientInfo newBackendClientInfo = domibusConnectorBackendClientInfo;
		
		
		ConfigurationItemTextFieldDiv backendNameDiv = new ConfigurationItemTextFieldDiv(new TextField(), BackendClientInfoLabels.backendClientInfoNameLabels);
		if(domibusConnectorBackendClientInfo.getBackendName()!=null) {
			backendNameDiv.setConfigurationItemValue(domibusConnectorBackendClientInfo.getBackendName());
			backendNameDiv.getConfigurationItem().setReadOnly(true);
		}
		backendNameDiv.getConfigurationItem().setWidth("400px");
		add(backendNameDiv);
		
		ConfigurationItemTextFieldDiv backendKeyAliasDiv = new ConfigurationItemTextFieldDiv(new TextField(), BackendClientInfoLabels.backendClientInfoKeyAliasLabels);
		if(domibusConnectorBackendClientInfo.getBackendKeyAlias()!=null)
			backendKeyAliasDiv.setConfigurationItemValue(domibusConnectorBackendClientInfo.getBackendKeyAlias());
		backendKeyAliasDiv.getConfigurationItem().addValueChangeListener(e -> newBackendClientInfo.setBackendKeyAlias(e.getValue()));
		backendKeyAliasDiv.getConfigurationItem().setWidth("400px");
		add(backendKeyAliasDiv);
		
		ConfigurationItemCheckboxDiv defaultBackendDiv = new ConfigurationItemCheckboxDiv(new Checkbox(), BackendClientInfoLabels.backendClientInfoDefaultLabels);
		defaultBackendDiv.getConfigurationItemCheckbox().setValue(domibusConnectorBackendClientInfo.isDefaultBackend());
		defaultBackendDiv.getConfigurationItemCheckbox().addValueChangeListener(e -> newBackendClientInfo.setDefaultBackend(e.getValue()));
		add(defaultBackendDiv);
		
		ConfigurationItemCheckboxDiv enabledDiv = new ConfigurationItemCheckboxDiv(new Checkbox(), BackendClientInfoLabels.backendClientInfoEnabledLabels);
		enabledDiv.getConfigurationItemCheckbox().setValue(domibusConnectorBackendClientInfo.isEnabled());
		enabledDiv.getConfigurationItemCheckbox().addValueChangeListener(e -> newBackendClientInfo.setEnabled(e.getValue()));
		add(enabledDiv);
		
		ConfigurationItemTextFieldDiv pushAddressDiv = new ConfigurationItemTextFieldDiv(new TextField(), BackendClientInfoLabels.backendClientInfoPushAddressLabels);
		if(domibusConnectorBackendClientInfo.getBackendPushAddress()!=null)
			pushAddressDiv.setConfigurationItemValue(domibusConnectorBackendClientInfo.getBackendPushAddress());
		pushAddressDiv.getConfigurationItem().addValueChangeListener(e -> newBackendClientInfo.setBackendPushAddress(e.getValue()));
		pushAddressDiv.getConfigurationItem().setWidth("400px");
		add(pushAddressDiv);
		
		ConfigurationItemTextFieldDiv descriptionDiv = new ConfigurationItemTextFieldDiv(new TextField(), BackendClientInfoLabels.backendClientInfoDescriptionLabels);
		if(domibusConnectorBackendClientInfo.getBackendDescription()!=null)
			descriptionDiv.setConfigurationItemValue(domibusConnectorBackendClientInfo.getBackendDescription());
		descriptionDiv.getConfigurationItem().addValueChangeListener(e -> newBackendClientInfo.setBackendDescription(e.getValue()));
		descriptionDiv.getConfigurationItem().setWidth("400px");
		add(descriptionDiv);
		
		ConfigurationItemDiv servicesDiv = new ConfigurationItemDiv(new Label("Services for backend client:"), BackendClientInfoLabels.backendClientInfoServicesLabels);
		add(servicesDiv);
		
		List<DomibusConnectorService> services = backendConfiguration.getServiceList();
		for(DomibusConnectorService service:services) {
			Checkbox serviceBox = new Checkbox(service.getService());
			if(domibusConnectorBackendClientInfo.getServices().contains(service)) {
				newBackendClientInfo.getServices().add(service);
				serviceBox.setValue(true);
			}
			serviceBox.setValue(domibusConnectorBackendClientInfo.getServices().contains(service));
			serviceBox.setReadOnly(backendConfiguration.checkServiceUsedForBackends(service)&&!domibusConnectorBackendClientInfo.getServices().contains(service));
			serviceBox.addValueChangeListener(e -> {
				if(e.getValue()) {
					newBackendClientInfo.getServices().add(service);
				}else {
					newBackendClientInfo.getServices().remove(service);
				}
			});
			add(serviceBox);
		}
		
		Div saveButtonContent = new Div();
		saveButtonContent.getStyle().set("text-align", "center");
		Button saveButton = new Button(new Icon(VaadinIcon.EDIT));
		saveButton.getElement().setAttribute("title", "Save Backend Client Info");
		saveButton.setText("Save");
		saveButton.addClickListener(e -> {backendConfiguration.saveBackendClientInfo(newBackendClientInfo);
				backendConfiguration.reloadBackendList();
				if(newBackendClientDialog!=null)newBackendClientDialog.close();
				});
		saveButtonContent.add(saveButton);
		saveButtonContent.getStyle().set("padding", "10px");
		add(saveButtonContent);
		
	}
	
	

}
