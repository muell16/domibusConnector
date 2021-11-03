package eu.domibus.connector.ui.view.areas.configuration.link.wizard;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.Command;
import eu.domibus.connector.link.api.LinkPlugin;
import eu.domibus.connector.ui.component.WizardStep;

public class ChooseImpl extends VerticalLayout implements WizardStep  {

    private ComboBox<LinkPlugin> implChooser = new ComboBox<>();

    public ChooseImpl() {
        this.add(implChooser);

    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public void onForward(Command onForwardExecute) {

    }

    @Override
    public String getStepTitle() {
        return null;
    }

}
