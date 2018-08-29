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
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationProperties;

public class BackendClientInfo extends VerticalLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BackendClientInfo(DomibusConnectorBackendClientInfo domibusConnectorBackendClientInfo, BackendConfiguration backendConfiguration, Dialog newBackendClientDialog) {
		DomibusConnectorBackendClientInfo newBackendClientInfo = domibusConnectorBackendClientInfo;
		
		TextField backendName = new TextField();
		ConfigurationItemTextFieldDiv backendNameDiv = new ConfigurationItemTextFieldDiv(backendName, BackendClientInfoLabels.backendClientInfoNameLabels, "");
		if(domibusConnectorBackendClientInfo.getBackendName()!=null) {
			backendName.setValue(domibusConnectorBackendClientInfo.getBackendName());
			backendName.setReadOnly(true);
		}
		backendName.setWidth("400px");
		ConfigurationProperties.unregisterComponent(backendName);
		add(backendNameDiv);
		
		TextField backendKeyAlias = new TextField();
		ConfigurationItemTextFieldDiv backendKeyAliasDiv = new ConfigurationItemTextFieldDiv(backendKeyAlias, BackendClientInfoLabels.backendClientInfoKeyAliasLabels, "");
		if(domibusConnectorBackendClientInfo.getBackendKeyAlias()!=null)
			backendKeyAlias.setValue(domibusConnectorBackendClientInfo.getBackendKeyAlias());
		backendKeyAlias.addValueChangeListener(e -> newBackendClientInfo.setBackendKeyAlias(e.getValue()));
		backendKeyAlias.setWidth("400px");
		ConfigurationProperties.unregisterComponent(backendKeyAlias);
		add(backendKeyAliasDiv);
		
		Checkbox defaultBackend = new Checkbox();
		ConfigurationItemCheckboxDiv defaultBackendDiv = new ConfigurationItemCheckboxDiv(defaultBackend, BackendClientInfoLabels.backendClientInfoDefaultLabels, domibusConnectorBackendClientInfo.isDefaultBackend());
		defaultBackend.addValueChangeListener(e -> newBackendClientInfo.setDefaultBackend(e.getValue()));
		ConfigurationProperties.unregisterComponent(defaultBackend);
		add(defaultBackendDiv);
		
		Checkbox enabled = new Checkbox();
		ConfigurationItemCheckboxDiv enabledDiv = new ConfigurationItemCheckboxDiv(enabled, BackendClientInfoLabels.backendClientInfoEnabledLabels, domibusConnectorBackendClientInfo.isEnabled());
		enabled.addValueChangeListener(e -> newBackendClientInfo.setEnabled(e.getValue()));
		ConfigurationProperties.unregisterComponent(enabled);
		add(enabledDiv);
		
		TextField pushAddress = new TextField();
		ConfigurationItemTextFieldDiv pushAddressDiv = new ConfigurationItemTextFieldDiv(pushAddress, BackendClientInfoLabels.backendClientInfoPushAddressLabels, "");
		if(domibusConnectorBackendClientInfo.getBackendPushAddress()!=null)
			pushAddress.setValue(domibusConnectorBackendClientInfo.getBackendPushAddress());
		pushAddress.addValueChangeListener(e -> newBackendClientInfo.setBackendPushAddress(e.getValue()));
		pushAddress.setWidth("400px");
		ConfigurationProperties.unregisterComponent(pushAddress);
		add(pushAddressDiv);
		
		TextField description = new TextField();
		ConfigurationItemTextFieldDiv descriptionDiv = new ConfigurationItemTextFieldDiv(description, BackendClientInfoLabels.backendClientInfoDescriptionLabels, "");
		if(domibusConnectorBackendClientInfo.getBackendDescription()!=null)
			description.setValue(domibusConnectorBackendClientInfo.getBackendDescription());
		description.addValueChangeListener(e -> newBackendClientInfo.setBackendDescription(e.getValue()));
		description.setWidth("400px");
		ConfigurationProperties.unregisterComponent(description);
		add(descriptionDiv);
		
		Label servicesLabel = new Label("Services for backend client:");
		ConfigurationItemDiv servicesDiv = new ConfigurationItemDiv(servicesLabel, BackendClientInfoLabels.backendClientInfoServicesLabels, "");
		ConfigurationProperties.unregisterComponent(servicesLabel);
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
		Button saveButton = new Button(new Icon(VaadinIcon.CHECK));
		saveButton.getElement().setAttribute("title", "Save Backend Client Info");
		saveButton.setText("Save");
		saveButton.addClickListener(e -> {backendConfiguration.saveBackendClientInfo(newBackendClientInfo);
				backendConfiguration.reloadBackendList();
				if(newBackendClientDialog!=null)newBackendClientDialog.close();
				});
		saveButtonContent.add(saveButton);

		Button cancelButton = new Button(new Icon(VaadinIcon.CLOSE));
		cancelButton.setText("Cancel");
		cancelButton.addClickListener(e -> {if(newBackendClientDialog!=null)newBackendClientDialog.close();});
		saveButtonContent.add(cancelButton);
		
		saveButtonContent.getStyle().set("padding", "10px");
		add(saveButtonContent);
		
	}
	
	

}
