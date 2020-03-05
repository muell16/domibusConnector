package eu.domibus.connector.web.areas.configuration.gateway;

import com.vaadin.flow.router.Route;
import eu.domibus.connector.web.utils.RoleRequired;
import eu.domibus.connector.web.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.web.areas.configuration.ConfigurationTab;
import eu.domibus.connector.web.areas.configuration.link.GatewayLinkConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.web.forms.FormsUtil;
import eu.domibus.connector.web.areas.configuration.backend.BackendConfigurationLabels;
import eu.domibus.connector.web.areas.configuration.util.ConfigurationUtil;

import java.lang.annotation.Annotation;

import static eu.domibus.connector.backend.ws.link.spring.WSBackendLinkContextConfiguration.WS_BACKEND_LINK_PROFILE;
import static eu.domibus.connector.gateway.link.ws.spring.GatewayLinkWsContext.GW_LINK_WS_PROFILE;

@Profile(GW_LINK_WS_PROFILE) //does not work with vaadin!
@ConfigurationTab(title = "Gateway Configuration")
@Route(value = GatewayConfiguration.ROUTE, layout = ConfigurationLayout.class, registerAtStartup = false)
@RoleRequired(role = "ADMIN")
public class GatewayConfiguration extends VerticalLayout {

	public static final String ROUTE = "gwlinkws";

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
