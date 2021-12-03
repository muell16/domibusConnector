package eu.domibus.connector.ui.view.areas.configuration.evidences;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.evidences.spring.EvidencesToolkitConfigurationProperties;
import eu.domibus.connector.ui.utils.RoleRequired;
import eu.domibus.connector.ui.view.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.ui.view.areas.configuration.ConfigurationPanelFactory;
import eu.domibus.connector.ui.view.areas.configuration.TabMetadata;
import eu.domibus.connector.ui.view.areas.configuration.security.importoldconfig.ImportOldEvidenceConfigDialog;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


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
public class EvidenceBuilderConfiguration  extends VerticalLayout {

	public static final String ROUTE = "evidencebuilder";

	public EvidenceBuilderConfiguration(ConfigurationPanelFactory configurationPanelFactory,
										ObjectProvider<ImportOldEvidenceConfigDialog> importOldEvidenceConfigDialog,
										EvidencesToolkitConfigurationPropertiesForm form) {
		ConfigurationPanelFactory.ConfigurationPanel<EvidencesToolkitConfigurationProperties> configurationPanel
				= configurationPanelFactory.createConfigurationPanel(form, EvidencesToolkitConfigurationProperties.class);

		Button b = new Button("Import old config");
		b.addClickListener(event -> {
			ImportOldEvidenceConfigDialog dialog = importOldEvidenceConfigDialog.getObject();
			dialog.addDialogCloseActionListener((ComponentEventListener<Dialog.DialogCloseActionEvent>) event1 -> configurationPanel.refreshUI());
			dialog.open();
		});
		this.add(b);
		this.add(configurationPanel);
	}

}

