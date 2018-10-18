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
import eu.domibus.connector.web.component.LumoCheckbox;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationItemCheckboxDiv;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationItemDiv;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationItemTextFieldDiv;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationProperties;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationUtil;

public class BackendClientInfo extends VerticalLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BackendClientInfo(DomibusConnectorBackendClientInfo domibusConnectorBackendClientInfo, BackendConfiguration backendConfiguration, Dialog newBackendClientDialog, ConfigurationUtil util) {
		DomibusConnectorBackendClientInfo newBackendClientInfo = domibusConnectorBackendClientInfo;
		
		TextField backendName = new TextField();
		Div backendNameDiv = util.createConfigurationItemTextFieldDivEmpty(BackendClientInfoLabels.backendClientInfoNameLabels, backendName);
		if(domibusConnectorBackendClientInfo.getBackendName()!=null) {
			backendName.setValue(domibusConnectorBackendClientInfo.getBackendName());
			backendName.setReadOnly(true);
		}else {
			backendName.addValueChangeListener(e -> newBackendClientInfo.setBackendName(e.getValue()));
		}
		backendName.setWidth("400px");
		util.unregisterComponent(backendName);
		add(backendNameDiv);
		
		TextField backendKeyAlias = new TextField();
		Div backendKeyAliasDiv = util.createConfigurationItemTextFieldDivEmpty(BackendClientInfoLabels.backendClientInfoKeyAliasLabels, backendKeyAlias);
		if(domibusConnectorBackendClientInfo.getBackendKeyAlias()!=null)
			backendKeyAlias.setValue(domibusConnectorBackendClientInfo.getBackendKeyAlias());
		backendKeyAlias.addValueChangeListener(e -> newBackendClientInfo.setBackendKeyAlias(e.getValue()));
		backendKeyAlias.setWidth("400px");
		util.unregisterComponent(backendKeyAlias);
		add(backendKeyAliasDiv);
		
		LumoCheckbox defaultBackend = new LumoCheckbox();
		Div defaultBackendDiv = util.createConfigurationItemCheckboxDivWithValue(BackendClientInfoLabels.backendClientInfoDefaultLabels, defaultBackend, domibusConnectorBackendClientInfo.isDefaultBackend());
		defaultBackend.addValueChangeListener(e -> newBackendClientInfo.setDefaultBackend(e.getValue()));
		util.unregisterComponent(defaultBackend);
		add(defaultBackendDiv);
		
		LumoCheckbox enabled = new LumoCheckbox();
		Div enabledDiv = util.createConfigurationItemCheckboxDivWithValue(BackendClientInfoLabels.backendClientInfoEnabledLabels, enabled, domibusConnectorBackendClientInfo.isEnabled());
		enabled.addValueChangeListener(e -> newBackendClientInfo.setEnabled(e.getValue()));
		util.unregisterComponent(enabled);
		add(enabledDiv);
		
		TextField pushAddress = new TextField();
		Div pushAddressDiv = util.createConfigurationItemTextFieldDivEmpty(BackendClientInfoLabels.backendClientInfoPushAddressLabels, pushAddress);
		if(domibusConnectorBackendClientInfo.getBackendPushAddress()!=null)
			pushAddress.setValue(domibusConnectorBackendClientInfo.getBackendPushAddress());
		pushAddress.addValueChangeListener(e -> newBackendClientInfo.setBackendPushAddress(e.getValue()));
		pushAddress.setWidth("400px");
		util.unregisterComponent(pushAddress);
		add(pushAddressDiv);
		
		TextField description = new TextField();
		Div descriptionDiv = util.createConfigurationItemTextFieldDivEmpty(BackendClientInfoLabels.backendClientInfoDescriptionLabels, description);
		if(domibusConnectorBackendClientInfo.getBackendDescription()!=null)
			description.setValue(domibusConnectorBackendClientInfo.getBackendDescription());
		description.addValueChangeListener(e -> newBackendClientInfo.setBackendDescription(e.getValue()));
		description.setWidth("400px");
		util.unregisterComponent(description);
		add(descriptionDiv);
		
		Label servicesLabel = new Label("Services for backend client:");
		Div servicesDiv = util.createConfigurationItemDiv(BackendClientInfoLabels.backendClientInfoServicesLabels, servicesLabel, "");
		util.unregisterComponent(servicesLabel);
		add(servicesDiv);
		
		List<DomibusConnectorService> services = backendConfiguration.getServiceList();
		for(DomibusConnectorService service:services) {
			LumoCheckbox serviceBox = new LumoCheckbox(service.getService());
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
