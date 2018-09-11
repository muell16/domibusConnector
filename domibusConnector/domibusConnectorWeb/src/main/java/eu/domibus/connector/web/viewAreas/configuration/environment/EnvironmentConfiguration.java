package eu.domibus.connector.web.viewAreas.configuration.environment;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.web.component.LumoCheckbox;
import eu.domibus.connector.web.forms.FormsUtil;
import eu.domibus.connector.web.service.WebPModeService;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationItemChapterDiv;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationProperties;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationUtil;

/**
 * @author riederb
 *
 * This class should handle the following parameters:
 * 
 * 	connector.gatewaylink.ws.submissionEndpointAddress

	connector.test.service
	connector.test.action

	http.proxy.enabled
	http.proxy.host
	http.proxy.port
	http.proxy.user=
	http.proxy.password=

	https.proxy.enabled
	https.proxy.host
	https.proxy.port
	https.proxy.user=
	https.proxy.password=

	spring.datasource.driver-class-name
	spring.datasource.username
	spring.datasource.url
	spring.datasource.password
	spring.jpa.properties.hibernate.dialect

 */
@HtmlImport("styles/shared-styles.html")
//@StyleSheet("styles/grid.css")
@Component
@UIScope
public class EnvironmentConfiguration extends VerticalLayout{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ConfigurationUtil util;
	
	TextField gatewaySubmissionServiceLinkField = FormsUtil.getFormattedTextField();
	
	ComboBox<String> serviceBox = new ComboBox<String>();
	ComboBox<String> actionBox = new ComboBox<String>();
	
	LumoCheckbox useHttpProxyBox = new LumoCheckbox();
	TextField httpProxyHostField = FormsUtil.getFormattedTextField();
	TextField httpProxyPortField = FormsUtil.getFormattedTextField();
	TextField httpProxyUserField = FormsUtil.getFormattedTextField();
	TextField httpProxyPasswordField = FormsUtil.getFormattedTextField();
	
	LumoCheckbox useHttpsProxyBox = new LumoCheckbox();
	TextField httpsProxyHostField = FormsUtil.getFormattedTextField();
	TextField httpsProxyPortField = FormsUtil.getFormattedTextField();
	TextField httpsProxyUserField = FormsUtil.getFormattedTextField();
	TextField httpsProxyPasswordField = FormsUtil.getFormattedTextField();
	
	TextField databaseConnectionStringField = FormsUtil.getFormattedTextField();
	TextField databaseUserField = FormsUtil.getFormattedTextField();
	TextField databasePasswordField = FormsUtil.getFormattedTextField();
	TextField databaseDriverClassField = FormsUtil.getFormattedTextField();
	TextField databaseDialectField = FormsUtil.getFormattedTextField();

	public EnvironmentConfiguration(@Autowired WebPModeService pmodeService, @Autowired ConfigurationUtil util) {
		this.util = util;
		
		add(util.createConfigurationItemTextFieldDiv(EnvironmentConfigurationLabels.gatewaySubmissionLinkLabels, gatewaySubmissionServiceLinkField));
		
		createAndAddServiceComboBox(pmodeService);
		
		createAndAddActionComboBox(pmodeService);
		
		add(new ConfigurationItemChapterDiv("Proxy Configuration:"));
		
		useHttpProxyBox.addValueChangeListener(e -> {
			httpProxyHostField.setReadOnly(!e.getValue());
			httpProxyPortField.setReadOnly(!e.getValue());
			httpProxyUserField.setReadOnly(!e.getValue());
			httpProxyPasswordField.setReadOnly(!e.getValue());
		});
		Div useHttpProxy = util.createConfigurationItemCheckboxDiv(EnvironmentConfigurationLabels.useHttpProxyLabels, useHttpProxyBox);
		add(useHttpProxy);
		
		Div httpProxyHost = util.createConfigurationItemTextFieldDiv(EnvironmentConfigurationLabels.httpProxyHostLabels, httpProxyHostField);
		add(httpProxyHost);
		
		httpProxyPortField.setWidth("300px");
		Div httpProxyPort = util.createConfigurationItemTextFieldDiv(EnvironmentConfigurationLabels.httpProxyPortLabels, httpProxyPortField);
		add(httpProxyPort);
		
		Div httpProxyUser = util.createConfigurationItemTextFieldDiv(EnvironmentConfigurationLabels.httpProxyUserLabels, httpProxyUserField);
		add(httpProxyUser);
		
		Div httpProxyPassword = util.createConfigurationItemTextFieldDiv(EnvironmentConfigurationLabels.httpProxyPasswordLabels, httpProxyPasswordField);
		add(httpProxyPassword);
		
		useHttpsProxyBox.addValueChangeListener(e -> {
			httpsProxyHostField.setReadOnly(!e.getValue());
			httpsProxyPortField.setReadOnly(!e.getValue());
			httpsProxyUserField.setReadOnly(!e.getValue());
			httpsProxyPasswordField.setReadOnly(!e.getValue());
		});
		Div useHttpsProxy = util.createConfigurationItemCheckboxDiv(EnvironmentConfigurationLabels.useHttpsProxyLabels, useHttpsProxyBox);
		add(useHttpsProxy);
		
		Div httpsProxyHost = util.createConfigurationItemTextFieldDiv(EnvironmentConfigurationLabels.httpsProxyHostLabels, httpsProxyHostField);
		add(httpsProxyHost);
		
		httpsProxyPortField.setWidth("300px");
		Div httpsProxyPort = util.createConfigurationItemTextFieldDiv(EnvironmentConfigurationLabels.httpsProxyPortLabels, httpsProxyPortField);
		add(httpsProxyPort);
		
		Div httpsProxyUser = util.createConfigurationItemTextFieldDiv(EnvironmentConfigurationLabels.httpsProxyUserLabels, httpsProxyUserField);
		add(httpsProxyUser);
		
		Div httpsProxyPassword = util.createConfigurationItemTextFieldDiv(EnvironmentConfigurationLabels.httpsProxyPasswordLabels, httpsProxyPasswordField);
		add(httpsProxyPassword);
		
		add(new ConfigurationItemChapterDiv("Database Connection Configuration:"));
		
		Div databaseUrl = util.createConfigurationItemTextFieldDiv(EnvironmentConfigurationLabels.databaseConnectionStringLabels, databaseConnectionStringField);
		add(databaseUrl);
		
		Div databaseUser = util.createConfigurationItemTextFieldDiv(EnvironmentConfigurationLabels.databaseUserLabels, databaseUserField);
		add(databaseUser);
		
		Div databasePassword = util.createConfigurationItemTextFieldDiv(EnvironmentConfigurationLabels.databasePasswordLabels, databasePasswordField);
		add(databasePassword);
		
		Div databaseDriver = util.createConfigurationItemTextFieldDiv(EnvironmentConfigurationLabels.databaseDriverClassNameLabels, databaseDriverClassField);
		add(databaseDriver);
		
		Div databaseDialect = util.createConfigurationItemTextFieldDiv(EnvironmentConfigurationLabels.databaseDialectLabels, databaseDialectField);
		add(databaseDialect);
		
	}

	private void createAndAddActionComboBox(WebPModeService pmodeService) {
		Collection<String> actionList = pmodeService.getActionListString();
		add(this.util.createConfigurationItemComboBoxDiv(EnvironmentConfigurationLabels.connectorTestActionLabels, actionBox, actionList));
		
	}

	private void createAndAddServiceComboBox(WebPModeService pmodeService) {
		List<String> serviceList = pmodeService.getServiceListString();
		add(util.createConfigurationItemComboBoxDiv(EnvironmentConfigurationLabels.connectorTestServiceLabels, serviceBox, serviceList));
		
	}
	
	

}
