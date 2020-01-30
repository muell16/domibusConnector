package eu.domibus.connector.web.configuration;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.spring.VaadinConfigurationProperties;
import eu.domibus.connector.web.login.LoginView;
import eu.domibus.connector.web.view.AccessDeniedView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;


@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    private static final Logger LOGGER = LogManager.getLogger(ConfigureUIServiceInitListener.class);

    @Autowired
    ConnectorUiConfigurationProperties connectorUiConfigurationProperties;
    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::beforeEnter);
        });
    }

    /**
     * Reroutes the user if (s)he is not authorized to access the view.
     *
     * @param event
     *            before navigation event with event details
     */
    private void beforeEnter(BeforeEnterEvent event) {
        if (connectorUiConfigurationProperties.isAutoLoginEnabled()) {
            LOGGER.warn("Auto Login is enabled! Only use this in development!");
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(connectorUiConfigurationProperties.getAutoLoginUser(), connectorUiConfigurationProperties.getAutoLoginPassword()));
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        }

        if (!LoginView.class.equals(event.getNavigationTarget())
                && !SecurityUtils.isUserLoggedIn()) {
            event.rerouteTo(LoginView.class);
        }
        Class<?> navigationTarget = event.getNavigationTarget();
        if (!SecurityUtils.isUserAllowedToView(navigationTarget)) {
            event.rerouteTo(AccessDeniedView.class);
        }
    }
}
