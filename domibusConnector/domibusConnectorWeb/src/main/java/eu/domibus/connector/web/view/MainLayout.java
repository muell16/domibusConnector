package eu.domibus.connector.web.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.web.configuration.SecurityUtils;
import eu.domibus.connector.web.login.LoginView;
import eu.domibus.connector.web.login.LogoutView;
import eu.domibus.connector.web.utils.TabViewRouterHelper;
import eu.domibus.connector.web.areas.configuration.ConfigurationOverviewView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import eu.domibus.connector.web.areas.configuration.ConfigurationLayout;
import eu.domibus.connector.web.areas.info.Info;
import eu.domibus.connector.web.areas.messages.Messages;
import eu.domibus.connector.web.areas.pmodes.PModes;
import eu.domibus.connector.web.areas.testing.ConnectorTests;
import eu.domibus.connector.web.areas.users.Users;

@UIScope
@org.springframework.stereotype.Component
public class MainLayout extends AppLayout implements BeforeEnterObserver {

    private Map<Tab, String> tabsToRouteMap = new HashMap<>();
    private Map<String, Tab> routeToTabMap = new HashMap<>();
    private Map<Tab, List<String>> tabToAnyRequiredRole = new HashMap<>();
    private Tabs menuTabs; // = new Tabs();

    private TabViewRouterHelper tabViewRouterHelper = new TabViewRouterHelper();
//    private Label currentlyLoggedInUser = new Label();

    public MainLayout(
            @Autowired Messages messagesView
    ) {

        setPrimarySection(Section.DRAWER);

        VerticalLayout topBar = new VerticalLayout();
        topBar.add(new DomibusConnectorAdminHeader());
        addToNavbar(topBar);

        tabViewRouterHelper.setTabFontSize("bigger");
        tabViewRouterHelper
                .createTab()
                .withLabel("Messages")
                .withIcon(new Icon(VaadinIcon.LIST))
                .addForComponent(messagesView);

        tabViewRouterHelper
                .createTab()
                .withLabel("PModes")
                .withIcon(new Icon(VaadinIcon.FILE_CODE))
                .addForComponent(PModes.class);

        tabViewRouterHelper
                .createTab()
                .withLabel("Configuration")
                .withIcon(new Icon(VaadinIcon.COG_O))
                .addForComponent(ConfigurationOverviewView.class);

        tabViewRouterHelper
                .createTab()
                .withLabel("Users")
                .withIcon(new Icon(VaadinIcon.USERS))
                .addForComponent(Users.class);

        tabViewRouterHelper
                .createTab()
                .withLabel("Connector Tests")
                .withIcon(VaadinIcon.CONNECT_O)
                .addForComponent(ConnectorTests.class);

        tabViewRouterHelper
                .createTab()
                .withLabel("Info")
                .withIcon(VaadinIcon.INFO_CIRCLE_O)
                .addForComponent(Info.class);

        tabViewRouterHelper
                .createTab()
                .withLabel("Logout")
                .withIcon(new Icon(VaadinIcon.ARROW_RIGHT))
                .addForComponent(LogoutView.class);

        menuTabs = tabViewRouterHelper.getTabs();
        menuTabs.setOrientation(Tabs.Orientation.HORIZONTAL);

        topBar.add(menuTabs);

//        addToDrawer(menuTabs);


    }

    public void beforeEnter(BeforeEnterEvent event) {
//        currentlyLoggedInUser.setText("User: " + SecurityUtils.getUsername());
        tabViewRouterHelper.beforeEnter(event);
    }

}
