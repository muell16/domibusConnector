package eu.domibus.connector.ui.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.server.Command;

import java.util.concurrent.CompletableFuture;

public interface WizardStep {
    Component getComponent();
    void onForward(Command onForwardExecute);
    default void onBack(Command onBackExecute) {}

    default boolean isBackSupported() {
        return false;
    }

    default boolean isNextSupported() {
        return true;
    }

    String getStepTitle();

}
