package eu.domibus.connector.web.viewAreas.configuration;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.web.forms.FormsUtil;
import eu.domibus.connector.web.viewAreas.configuration.ConfigurationLabels.ConfigurationLabel;

@HtmlImport("styles/shared-styles.html")
@StyleSheet("styles/grid.css")
@Component
@UIScope
public class EnvironmentConfiguration extends VerticalLayout{
	
	TextField gatewaySubmissionServiceLink = FormsUtil.getFormattedTextField();

	/**
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
	 * # The connector is using this webservice address to submit messages to the gateway
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
	 * 
	 */
	public EnvironmentConfiguration() {
		Div gatewayLink = createConfigurationTextFieldWithLabels(ConfigurationLabels.gatewaySubmissionLinkLabels, gatewaySubmissionServiceLink);
		
		add(gatewayLink);
	}

	private Div createConfigurationTextFieldWithLabels(ConfigurationLabel labels, TextField configTextField) {
		Div configDiv = new Div();
		configTextField.setLabel(labels.CONFIGURATION_ELEMENT_LABEL);
		configDiv.add(configTextField);
		Button infoButton = createInfoButton(labels);
		configDiv.add(infoButton);
		return configDiv;
	}

	private Button createInfoButton(ConfigurationLabel labels) {
		Button infoButton = new Button(new Icon(VaadinIcon.INFO_CIRCLE_O));
		Dialog dialog = new Dialog();
		
		Div headerContent = new Div();
		Label header = new Label(labels.CONFIGURATION_ELEMENT_LABEL);
		header.getStyle().set("font-weight", "bold");
		header.getStyle().set("font-style", "italic");
		headerContent.getStyle().set("text-align", "center");
		headerContent.getStyle().set("padding", "10px");
		headerContent.add(header);
		dialog.add(headerContent);
		
		Div infoContent = new Div();
		for(String info:labels.INFO_LABEL) {
			Div infoLine = new Div();
			infoLine.add(new Label(info));
			infoContent.add(infoLine);
		}
		infoContent.getStyle().set("padding", "10px");
		dialog.add(infoContent);
		
		Div propertyContent = new Div();
		Label correspondingProperty = new Label("\n Corresponding property: ");
		correspondingProperty.getStyle().set("font-weight", "bold");
		propertyContent.add(correspondingProperty);
		propertyContent.add(new Label(labels.PROPERTY_NAME_LABEL));
		propertyContent.getStyle().set("padding", "10px");
		dialog.add(propertyContent);
		
		Div closeButtonContent = new Div();
		closeButtonContent.getStyle().set("text-align", "center");
		Button closeButton = new Button("Close", event -> {
		    dialog.close();
		});
		closeButtonContent.add(closeButton);
		closeButtonContent.getStyle().set("padding", "10px");
		dialog.add(closeButtonContent);

		infoButton.addClickListener(event -> dialog.open());
		return infoButton;
	}

}
