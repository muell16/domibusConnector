package eu.domibus.connector.web.component;

import com.vaadin.flow.component.Component;

public interface WizardStep {
    Component getComponent();
    boolean onForward();
    default boolean onBack() {
        return false;
    }

    default boolean isBackSupported() {
        return false;
    }

    default boolean isNextSupported() {
        return true;
    }

    String getStepTitle();

}
