package eu.domibus.connector.web.viewAreas.configuration.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.web.forms.FormsUtil;
import eu.domibus.connector.web.viewAreas.configuration.backend.BackendConfigurationLabels;
import eu.domibus.connector.web.viewAreas.configuration.util.ConfigurationUtil;

//@HtmlImport("styles/shared-styles.html")
//@StyleSheet("styles/grid.css")
@Component
@UIScope
public class GatewayConfiguration extends VerticalLayout{
	
	ConfigurationUtil util;
	
	TextField gatewaySubmissionServiceLinkField = FormsUtil.getFormattedTextField();
	
	TextField gatewayKeyStorePathField = FormsUtil.getFormattedTextField();
	TextField gatewayKeyStorePasswordField = FormsUtil.getFormattedTextField();
	TextField gatewayKeyAliasField = FormsUtil.getFormattedTextField();
	TextField gatewayKeyPasswordField = FormsUtil.getFormattedTextField();
	TextField gatewayTruststorePathField = FormsUtil.getFormattedTextField();
	TextField gatewayTruststorePasswordField = FormsUtil.getFormattedTextField();
	TextField gatewayEncryptAliasField = FormsUtil.getFormattedTextField();

	public GatewayConfiguration(@Autowired ConfigurationUtil util) {
		this.util = util;
		
		add(util.createConfigurationItemTextFieldDiv(GatewayConfigurationLabels.gatewaySubmissionLinkLabels, gatewaySubmissionServiceLinkField));
		
		add(util.createConfigurationItemTextFieldDiv(GatewayConfigurationLabels.gatewayKeyStorePathLabels, gatewayKeyStorePathField));
		
		add(util.createConfigurationItemTextFieldDiv(GatewayConfigurationLabels.gatewayKeyStorePasswordLabels, gatewayKeyStorePasswordField));
		
		add(util.createKeystoreInformationGrid(gatewayKeyStorePathField, gatewayKeyStorePasswordField));
		
		add(util.createConfigurationItemTextFieldDiv(GatewayConfigurationLabels.gatewayKeyAliasLabels, gatewayKeyAliasField));
		
		add(util.createConfigurationItemTextFieldDiv(GatewayConfigurationLabels.gatewayKeyPasswordLabels, gatewayKeyPasswordField));
		
		add(util.createConfigurationItemTextFieldDiv(GatewayConfigurationLabels.gatewayTrustStorePathLabels, gatewayTruststorePathField));
		
		add(util.createConfigurationItemTextFieldDiv(GatewayConfigurationLabels.gatewayTrustStorePasswordLabels, gatewayTruststorePasswordField));
		
		add(util.createKeystoreInformationGrid(gatewayTruststorePathField, gatewayTruststorePasswordField));
		
		add(util.createConfigurationItemTextFieldDiv(GatewayConfigurationLabels.gatewayEncryptAliasLabels, gatewayEncryptAliasField));
	}

}
