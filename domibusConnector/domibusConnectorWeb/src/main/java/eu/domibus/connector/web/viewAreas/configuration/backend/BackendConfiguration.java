package eu.domibus.connector.web.viewAreas.configuration.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.web.forms.FormsUtil;
import eu.domibus.connector.web.viewAreas.configuration.security.SecurityConfigurationLabels;
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
@StyleSheet("styles/grid.css")
@Component
@UIScope
public class BackendConfiguration  extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ConfigurationUtil util;
	
	TextField backendKeyStorePathField = FormsUtil.getFormattedTextField();
	TextField backendKeyStorePasswordField = FormsUtil.getFormattedTextField();
	TextField backendKeyAliasField = FormsUtil.getFormattedTextField();
	TextField backendKeyPasswordField = FormsUtil.getFormattedTextField();
	TextField backendTruststorePathField = FormsUtil.getFormattedTextField();
	TextField backendTruststorePasswordField = FormsUtil.getFormattedTextField();
	
	public BackendConfiguration(@Autowired ConfigurationUtil util) {
		this.util = util;
		
		add(new ConfigurationItemChapterDiv("Keystore/Truststore configuration:"));
		
		add(util.createConfigurationItemTextFieldDiv(BackendConfigurationLabels.backendKeyStorePathLabels, backendKeyStorePathField));
		
		add(util.createConfigurationItemTextFieldDiv(BackendConfigurationLabels.backendKeyStorePasswordLabels, backendKeyStorePasswordField));
		
		add(util.createConfigurationItemTextFieldDiv(BackendConfigurationLabels.backendKeyAliasLabels, backendKeyAliasField));
		
		add(util.createConfigurationItemTextFieldDiv(BackendConfigurationLabels.backendKeyPasswordLabels, backendKeyPasswordField));
		
		add(util.createConfigurationItemTextFieldDiv(BackendConfigurationLabels.backendTrustStorePathLabels, backendTruststorePathField));
		
		add(util.createConfigurationItemTextFieldDiv(BackendConfigurationLabels.backendTrustStorePasswordLabels, backendTruststorePasswordField));
		
		add(new ConfigurationItemChapterDiv("Configured backend(s):"));
	}

}
