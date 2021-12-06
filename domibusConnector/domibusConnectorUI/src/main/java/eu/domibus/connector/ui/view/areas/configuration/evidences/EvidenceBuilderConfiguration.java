package eu.domibus.connector.ui.view.areas.configuration.evidences;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.common.service.BusinessDomainConfigurationChange;
import eu.domibus.connector.common.service.ConfigurationPropertyManagerService;
import eu.domibus.connector.common.service.CurrentBusinessDomain;
import eu.domibus.connector.controller.spring.EvidencesTimeoutConfigurationProperties;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.evidences.spring.EvidencesToolkitConfigurationProperties;
import eu.domibus.connector.evidences.spring.PostalAdressConfigurationProperties;
import eu.domibus.connector.security.configuration.DCBusinessDocumentValidationConfigurationProperties;
import eu.domibus.connector.ui.component.LumoCheckbox;
import eu.domibus.connector.ui.forms.FormsUtil;
import eu.domibus.connector.ui.layout.DCVerticalLayoutWithTitleAndHelpButton;
import eu.domibus.connector.ui.service.WebKeystoreService;
import eu.domibus.connector.ui.utils.RoleRequired;
import eu.domibus.connector.ui.view.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.ui.view.areas.configuration.ConfigurationPanelFactory;
import eu.domibus.connector.ui.view.areas.configuration.TabMetadata;
import eu.domibus.connector.ui.view.areas.configuration.security.BusinessDocumentValidationConfigForm;
import eu.domibus.connector.ui.view.areas.configuration.security.importoldconfig.ImportOldEvidenceConfigDialog;
import eu.domibus.connector.ui.view.areas.configuration.util.ConfigurationItemChapterDiv;
import eu.domibus.connector.ui.view.areas.configuration.util.ConfigurationUtil;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author riederb
 *
 * 
 */
@Component
@UIScope
@Route(value = EvidenceBuilderConfiguration.ROUTE, layout = ConfigurationLayout.class)
@RoleRequired(role = "ADMIN")
@TabMetadata(title = "Evidence Builder Configuration", tabGroup = ConfigurationLayout.TAB_GROUP_NAME)
@Order(7)
public class EvidenceBuilderConfiguration  extends DCVerticalLayoutWithTitleAndHelpButton {

	public static final String ROUTE = "evidencebuilder";
	
	public static final String TITLE = "Evidence Builder Configuration";
	public static final String HELP_ID = "ui/configuration/evidence_builder_configuration.html";

	public EvidenceBuilderConfiguration(ConfigurationPanelFactory configurationPanelFactory,
										ObjectProvider<ImportOldEvidenceConfigDialog> importOldEvidenceConfigDialog,
										EvidencesToolkitConfigurationPropertiesForm form) {
		super(HELP_ID, TITLE);
		ConfigurationPanelFactory.ConfigurationPanel<EvidencesToolkitConfigurationProperties> configurationPanel
				= configurationPanelFactory.createConfigurationPanel(form, EvidencesToolkitConfigurationProperties.class);

		Button b = new Button("Import old config");
		b.addClickListener(event -> {
			importOldEvidenceConfigDialog.getIfAvailable().open();
		});
		this.add(b);
		this.add(configurationPanel);
	}

}

