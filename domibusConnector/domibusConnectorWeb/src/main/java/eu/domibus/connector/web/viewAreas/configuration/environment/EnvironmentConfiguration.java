package eu.domibus.connector.web.viewAreas.configuration.environment;

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
import eu.domibus.connector.web.forms.FormsUtil;
import eu.domibus.connector.web.service.WebPModeService;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationUtil;

/**
 * @author riederb
 *
 * This class should handle the following:
 * 
 * Edit the properties for connection to the GW.
 * 
 * Edit the properties for Test PModes
 * 
 * Edit the Proxy Settings
 * 
 * Edit the datasource settings
 * 
 * ################ Properties for environment from properties file: #######################
 * 
 * 		# The connector is using this webservice address to submit messages to the gateway
		connector.gatewaylink.ws.submissionEndpointAddress=http://127.0.0.1:8080/domibus/services/domibusConnectorSubmissionWebservice

		# Service and action entered here according to the p-modes used.
		# Enabled and used messages received with that combination will not be sent to the backend of the connector, 
		# but will be answered with evidences after processed in the connector.
		connector.test.service=testService1
		connector.test.action=tc2Action

		# If the connector should use a http proxy for loading the trusted lists  you have to configure
		# the proxy values here:
		#
		#HTTP proxy settings
		http.proxy.enabled=true
		http.proxy.host=172.30.9.12
		http.proxy.port=8080
		http.proxy.user=
		http.proxy.password=

		#HTTPS proxy settings
		https.proxy.enabled=true
		https.proxy.host=172.30.9.12
		https.proxy.port=8080
		https.proxy.user=
		https.proxy.password=

		# Application defined datasource:

		# Fully qualified name of the JDBC driver. Auto-detected based on the URL by default.
		spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

		# Login username of the database.
		spring.datasource.username=tbckenddb

		# JDBC URL of the database.
		spring.datasource.url=jdbc:oracle:thin:@sjusee:1521:sjusee

		# Login password of the database.
		spring.datasource.password=tbckenddb

		spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.Oracle10gDialect
  

 */
@HtmlImport("styles/shared-styles.html")
@StyleSheet("styles/grid.css")
@Component
@UIScope
public class EnvironmentConfiguration extends VerticalLayout{
	
	ConfigurationUtil util;
	
	TextField gatewaySubmissionServiceLinkField = FormsUtil.getFormattedTextField();
	
	ComboBox<DomibusConnectorService> serviceBox = new ComboBox<DomibusConnectorService>();
	ComboBox<DomibusConnectorAction> actionBox = new ComboBox<DomibusConnectorAction>();
	
	Checkbox useHttpProxyBox = new Checkbox();
	TextField httpProxyHostField = FormsUtil.getFormattedTextField();
	TextField httpProxyPortField = FormsUtil.getFormattedTextField();
	TextField httpProxyUserField = FormsUtil.getFormattedTextField();
	TextField httpProxyPasswordField = FormsUtil.getFormattedTextField();
	
	Checkbox useHttpsProxyBox = new Checkbox();
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
		
		Div gatewayLink = util.createConfigurationTextFieldWithLabels(EnvironmentConfigurationLabels.gatewaySubmissionLinkLabels, gatewaySubmissionServiceLinkField);
		add(gatewayLink);
		
		List<DomibusConnectorService> serviceList = pmodeService.getServiceList();
		
		serviceBox.setItemLabelGenerator(new ItemLabelGenerator<DomibusConnectorService>() {
			
			@Override
			public String apply(DomibusConnectorService item) {
				return item.getService();
			}
		});
		DomibusConnectorService serviceValue = pmodeService.getService(util.getPropertyValue(EnvironmentConfigurationLabels.connectorTestServiceLabels.PROPERTY_NAME_LABEL));
		Div connectorTestService = util.createConfigurationComboBoxWithItemsAndLabels(EnvironmentConfigurationLabels.connectorTestServiceLabels, serviceBox, serviceList, serviceValue);
		add(connectorTestService);
		
		List<DomibusConnectorAction> actionList = pmodeService.getActionList();
		actionBox.setItemLabelGenerator(new ItemLabelGenerator<DomibusConnectorAction>() {
			@Override
			public String apply(DomibusConnectorAction item) {
				return item.getAction();
			}
		});
		
		DomibusConnectorAction actionValue = pmodeService.getAction(util.getPropertyValue(EnvironmentConfigurationLabels.connectorTestActionLabels.PROPERTY_NAME_LABEL));
		Div connectorTestAction = util.createConfigurationComboBoxWithItemsAndLabels(EnvironmentConfigurationLabels.connectorTestActionLabels, actionBox, actionList, actionValue);
//		Div connectorTestAction = util.createConfigurationComboBoxWithItemsAndLabels(ConfigurationLabels.connectorTestActionLabels, actionBox, actionList, null);
		add(connectorTestAction);
		
		add(util.createChapterDiv("Proxy Configuration:"));
		
		useHttpProxyBox.addValueChangeListener(e -> {
			httpProxyHostField.setReadOnly(!e.getValue());
			httpProxyPortField.setReadOnly(!e.getValue());
			httpProxyUserField.setReadOnly(!e.getValue());
			httpProxyPasswordField.setReadOnly(!e.getValue());
		});
		Div useHttpProxy = util.createConfigurationCheckboxWithLabels(EnvironmentConfigurationLabels.useHttpProxyLabels, useHttpProxyBox);
		add(useHttpProxy);
		
		Div httpProxyHost = util.createConfigurationTextFieldWithLabels(EnvironmentConfigurationLabels.httpProxyHostLabels, httpProxyHostField);
		add(httpProxyHost);
		
		httpProxyPortField.setWidth("300px");
		Div httpProxyPort = util.createConfigurationTextFieldWithLabels(EnvironmentConfigurationLabels.httpProxyPortLabels, httpProxyPortField);
		add(httpProxyPort);
		
		Div httpProxyUser = util.createConfigurationTextFieldWithLabels(EnvironmentConfigurationLabels.httpProxyUserLabels, httpProxyUserField);
		add(httpProxyUser);
		
		Div httpProxyPassword = util.createConfigurationTextFieldWithLabels(EnvironmentConfigurationLabels.httpProxyPasswordLabels, httpProxyPasswordField);
		add(httpProxyPassword);
		
		useHttpsProxyBox.addValueChangeListener(e -> {
			httpsProxyHostField.setReadOnly(!e.getValue());
			httpsProxyPortField.setReadOnly(!e.getValue());
			httpsProxyUserField.setReadOnly(!e.getValue());
			httpsProxyPasswordField.setReadOnly(!e.getValue());
		});
		Div useHttpsProxy = util.createConfigurationCheckboxWithLabels(EnvironmentConfigurationLabels.useHttpsProxyLabels, useHttpsProxyBox);
		add(useHttpsProxy);
		
		Div httpsProxyHost = util.createConfigurationTextFieldWithLabels(EnvironmentConfigurationLabels.httpsProxyHostLabels, httpsProxyHostField);
		add(httpsProxyHost);
		
		httpsProxyPortField.setWidth("300px");
		Div httpsProxyPort = util.createConfigurationTextFieldWithLabels(EnvironmentConfigurationLabels.httpsProxyPortLabels, httpsProxyPortField);
		add(httpsProxyPort);
		
		Div httpsProxyUser = util.createConfigurationTextFieldWithLabels(EnvironmentConfigurationLabels.httpsProxyUserLabels, httpsProxyUserField);
		add(httpsProxyUser);
		
		Div httpsProxyPassword = util.createConfigurationTextFieldWithLabels(EnvironmentConfigurationLabels.httpsProxyPasswordLabels, httpsProxyPasswordField);
		add(httpsProxyPassword);
		
		add(util.createChapterDiv("Database Connection Configuration:"));
		
		Div databaseUrl = util.createConfigurationTextFieldWithLabels(EnvironmentConfigurationLabels.databaseConnectionStringLabels, databaseConnectionStringField);
		add(databaseUrl);
		
		Div databaseUser = util.createConfigurationTextFieldWithLabels(EnvironmentConfigurationLabels.databaseUserLabels, databaseUserField);
		add(databaseUser);
		
		Div databasePassword = util.createConfigurationTextFieldWithLabels(EnvironmentConfigurationLabels.databasePasswordLabels, databasePasswordField);
		add(databasePassword);
		
		Div databaseDriver = util.createConfigurationTextFieldWithLabels(EnvironmentConfigurationLabels.databaseDriverClassNameLabels, databaseDriverClassField);
		add(databaseDriver);
		
		Div databaseDialect = util.createConfigurationTextFieldWithLabels(EnvironmentConfigurationLabels.databaseDialectLabels, databaseDialectField);
		add(databaseDialect);
		
	}
	
	

}
