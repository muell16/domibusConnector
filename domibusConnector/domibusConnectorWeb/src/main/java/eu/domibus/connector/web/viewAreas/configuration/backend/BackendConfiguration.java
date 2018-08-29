package eu.domibus.connector.web.viewAreas.configuration.backend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.backend.domain.model.DomibusConnectorBackendClientInfo;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.web.forms.FormsUtil;
import eu.domibus.connector.web.service.WebBackendClientService;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationItemChapterDiv;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationUtil;

/**
 * This class should handle the following:
 * adding, deleting,... backends (enter the corresponding DB entries).
 * 
 * Loading and editing the properties for backends (see properties below).
 * 
 * If backends are added or edited, also add or edit the keys in truststore.
 * 
 * 
 * #############################  Properties for backend  #############################
	connector.backend.ws.key.store.path
	connector.backend.ws.key.store.password
	connector.backend.ws.key.key.alias
	connector.backend.ws.key.key.password
	
	connector.backend.ws.trust.store.path
	connector.backend.ws.trust.store.password

 * 
 */
@HtmlImport("styles/shared-styles.html")
//@StyleSheet("styles/grid.css")
@Component
@UIScope
public class BackendConfiguration  extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ConfigurationUtil util;
	WebBackendClientService backendClientService;
	
	private Grid<DomibusConnectorBackendClientInfo> grid = new Grid<>();
	private List<DomibusConnectorBackendClientInfo> fullList = null;
	
	TextField backendKeyStorePathField = FormsUtil.getFormattedTextField();
	TextField backendKeyStorePasswordField = FormsUtil.getFormattedTextField();
	TextField backendKeyAliasField = FormsUtil.getFormattedTextField();
	TextField backendKeyPasswordField = FormsUtil.getFormattedTextField();
	TextField backendTruststorePathField = FormsUtil.getFormattedTextField();
	TextField backendTruststorePasswordField = FormsUtil.getFormattedTextField();
	
	public BackendConfiguration(@Autowired ConfigurationUtil util, @Autowired WebBackendClientService backendClientService) {
		this.util = util;
		this.backendClientService = backendClientService;
		
		add(new ConfigurationItemChapterDiv("Keystore/Truststore configuration:"));
		
		add(util.createConfigurationItemTextFieldDiv(BackendConfigurationLabels.backendKeyStorePathLabels, backendKeyStorePathField));
		
		add(util.createConfigurationItemTextFieldDiv(BackendConfigurationLabels.backendKeyStorePasswordLabels, backendKeyStorePasswordField));
		
		add(util.createConfigurationItemTextFieldDiv(BackendConfigurationLabels.backendKeyAliasLabels, backendKeyAliasField));
		
		add(util.createConfigurationItemTextFieldDiv(BackendConfigurationLabels.backendKeyPasswordLabels, backendKeyPasswordField));
		
		add(util.createConfigurationItemTextFieldDiv(BackendConfigurationLabels.backendTrustStorePathLabels, backendTruststorePathField));
		
		add(util.createConfigurationItemTextFieldDiv(BackendConfigurationLabels.backendTrustStorePasswordLabels, backendTruststorePasswordField));
		
		add(new ConfigurationItemChapterDiv("Configured backend(s):"));
		
		Div addBackendClientInfo = new Div();
		
		Button newBackendClientInfo = new Button(new Icon(VaadinIcon.PLUS));
		newBackendClientInfo.setText("Add new Backend Client Info");
		newBackendClientInfo.addClickListener(e -> openNewBackendClientInfoDialog(null));
		addBackendClientInfo.add(newBackendClientInfo);
		
		add(addBackendClientInfo);
		
		fullList = backendClientService.getAllBackendClients();
		
		grid.setItems(fullList);
		grid.addComponentColumn(domibusConnectorBackendClientInfo -> getDetailsLink(domibusConnectorBackendClientInfo)).setHeader("Details").setWidth("30px");
		grid.addColumn(DomibusConnectorBackendClientInfo::getBackendName).setHeader("Backend Name").setWidth("250px").setSortable(true).setResizable(true);
		grid.addColumn(DomibusConnectorBackendClientInfo::getBackendKeyAlias).setHeader("Key Alias").setWidth("150px").setSortable(true).setResizable(true);
		grid.addComponentColumn(domibusConnectorBackendClientInfo -> getCheckboxForList(domibusConnectorBackendClientInfo.isDefaultBackend())).setHeader("Default").setWidth("100px");
		grid.addComponentColumn(domibusConnectorBackendClientInfo -> getCheckboxForList(domibusConnectorBackendClientInfo.isPushBackend())).setHeader("Push Mode").setWidth("100px");
		grid.addComponentColumn(domibusConnectorBackendClientInfo -> getCheckboxForList(domibusConnectorBackendClientInfo.isEnabled())).setHeader("Enabled").setWidth("100px");
		grid.addColumn(DomibusConnectorBackendClientInfo::getBackendPushAddress).setHeader("Push Address").setWidth("500px").setSortable(true).setResizable(true);
		grid.setWidth("1500px");
		grid.setHeight("300px");
		grid.setMultiSort(true);
		
		Div backendClientInfo = new Div();
		backendClientInfo.add(grid);
		add(backendClientInfo);
	}
	
	private Button getDetailsLink(DomibusConnectorBackendClientInfo domibusConnectorBackendClientInfo) {
		Button getDetails = new Button(new Icon(VaadinIcon.SEARCH));
		getDetails.addClickListener(e -> openNewBackendClientInfoDialog(domibusConnectorBackendClientInfo));
		return getDetails;
	}
	
	public void reloadBackendList() {
		fullList = backendClientService.getAllBackendClients();
		grid.setItems(fullList);
	}
	
	public boolean checkServiceUsedForBackends(DomibusConnectorService service) {
		
		for(DomibusConnectorBackendClientInfo backendInfo:fullList) {
			if(backendInfo.getServices().contains(service))
				return true;
		}
		return false;
	}
	
	private void openNewBackendClientInfoDialog(DomibusConnectorBackendClientInfo backendClientInfo) {
//		DomibusConnectorBackendClientInfo newBackendClientInfo = new DomibusConnectorBackendClientInfo();
		Dialog newBackendClientDialog = new Dialog();
		
		Div headerContent = new Div();
		Label header = new Label("Add a new backend client to configuration");
		if(backendClientInfo!=null) {
			header.setText("Edit a backend client");
		}
		header.getStyle().set("font-weight", "bold");
		header.getStyle().set("font-style", "italic");
		headerContent.getStyle().set("text-align", "center");
		headerContent.getStyle().set("padding", "10px");
		headerContent.add(header);
		newBackendClientDialog.add(headerContent);
		
		BackendClientInfo view = new BackendClientInfo(backendClientInfo!=null?backendClientInfo:new DomibusConnectorBackendClientInfo(), this, newBackendClientDialog, util);
		newBackendClientDialog.add(view);
		
		newBackendClientDialog.open();
	}
	
	public void saveBackendClientInfo(DomibusConnectorBackendClientInfo domibusConnectorBackendClientInfo) {
		backendClientService.saveBackendClientInfo(domibusConnectorBackendClientInfo);
	}
	
	public List<DomibusConnectorService> getServiceList(){
		return backendClientService.getServiceList();
	}
	
	private Checkbox getCheckboxForList(boolean checked) {
		Checkbox isChecked = new Checkbox(checked);
		isChecked.setReadOnly(true);
		
		return isChecked;
	}

}
