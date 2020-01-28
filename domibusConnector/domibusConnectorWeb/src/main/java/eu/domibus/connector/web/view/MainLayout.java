package eu.domibus.connector.web.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.web.login.LoginView;
import eu.domibus.connector.web.login.LogoutView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import eu.domibus.connector.web.viewAreas.configuration.Configuration;
import eu.domibus.connector.web.viewAreas.info.Info;
import eu.domibus.connector.web.viewAreas.messages.Messages;
import eu.domibus.connector.web.viewAreas.pmodes.PModes;
import eu.domibus.connector.web.viewAreas.testing.ConnectorTests;
import eu.domibus.connector.web.viewAreas.users.Users;

import javax.annotation.PostConstruct;

@UIScope
@org.springframework.stereotype.Component
public class MainLayout extends AppLayout implements BeforeEnterObserver {

    private Map<Tab, String> tabsToRouteMap = new HashMap<>();
    private Map<String, Tab> routeToTabMap = new HashMap<>();
    private Map<Tab, List<String>> tabToAnyRequiredRole = new HashMap<>();
    private Tabs menuTabs = new Tabs();


    public MainLayout() {

        setPrimarySection(Section.DRAWER);

        addToNavbar(new DrawerToggle(), new DomibusConnectorAdminHeader());

        createTab(Messages.ROUTE, "Messages", new Icon(VaadinIcon.LIST), Stream.of("ROLE_ADMIN", "ROLE_USER").collect(Collectors.toList()));
        createTab(PModes.ROUTE, "PModes", new Icon(VaadinIcon.FILE_CODE), Stream.of("ROLE_ADMIN", "ROLE_USER").collect(Collectors.toList()));
        createTab(Configuration.ROUTE, "Configuration", new Icon(VaadinIcon.COG_O), Stream.of("ROLE_ADMIN").collect(Collectors.toList()));
        createTab(Users.ROUTE, "Users", new Icon(VaadinIcon.USERS), Stream.of("ROLE_ADMIN").collect(Collectors.toList()));
        createTab(ConnectorTests.ROUTE, "Connector Tests", new Icon(VaadinIcon.CONNECT_O), Stream.of("ROLE_ADMIN").collect(Collectors.toList()));
        createTab(Info.ROUTE, "Info", new Icon(VaadinIcon.INFO_CIRCLE_O), Stream.of("ROLE_ADMIN").collect(Collectors.toList()));
        createTab(LogoutView.ROUTE, "Logout", new Icon(VaadinIcon.ARROW_RIGHT), new ArrayList<>());

        menuTabs.addSelectedChangeListener(this::selectedTabChanged);
        menuTabs.setOrientation(Tabs.Orientation.VERTICAL);

//        addToNavbar(domibusConnectorAdminHeader);
//        addToDrawer(topMenu);
        addToDrawer(menuTabs);

    }

    public void beforeEnter(BeforeEnterEvent event) {

        boolean authenticated = false;
        SecurityContext context = SecurityContextHolder.getContext();

        if (context != null && context.getAuthentication() != null) {
            Authentication authentication = context.getAuthentication();
            authenticated = authentication.isAuthenticated();
        }

        if (event == null) {
            throw new IllegalStateException("event in beforeEnter is null!");
        }

        if (!authenticated) {
            String currentPath = event.getLocation().getPath();
            event.rerouteTo(LoginView.ROUTE, currentPath);
        }

        String firstSegment = event.getLocation().getFirstSegment();
        Tab tab = routeToTabMap.get(firstSegment);
        menuTabs.setSelectedTab(tab);

        setRolesOnTab();

    }

    /**
     * iterates over all tabs and only enables the tab
     * if the SecurityContext has any role required by the
     * tab - the possible roles for the tab are in @see this#tabToAnyRequiredRole
     */
    private void setRolesOnTab() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() == null) {
            return;
        }
        List<String> collect = context.getAuthentication().getAuthorities().stream().map(Object::toString).collect(Collectors.toList());
        tabToAnyRequiredRole.keySet().stream().forEach(
                tab -> tab.setEnabled( //only enable tab if any required role is fullfilled by the current security context
                        tabToAnyRequiredRole.isEmpty() ||
                        tabToAnyRequiredRole
                            .getOrDefault(tab, new ArrayList<>())
                            .stream()
                            .filter(role -> collect.contains(role))
                            .findAny().isPresent()
                )
        );
    }


    private Tab createTab(String route, String tabLabel, Icon tabIcon, List<String> requiredRoles) {
        Span tabText = new Span(tabLabel);
        tabText.getStyle().set("font-size", "20px");
        tabIcon.setSize("20px");

        HorizontalLayout tabLayout = new HorizontalLayout(tabIcon, tabText);
        tabLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        Tab tab = new Tab(tabLayout);
        tabsToRouteMap.put(tab, route);
        routeToTabMap.put(route, tab);
        menuTabs.add(tab);
        return tab;
    }

    private void selectedTabChanged(Tabs.SelectedChangeEvent selectedChangeEvent) {
        if (selectedChangeEvent.isFromClient()) { //only start navigation if event is from client...
            Tab selectedTab = selectedChangeEvent.getSelectedTab();
            String route = tabsToRouteMap.get(selectedTab);
            getUI().get().navigate(route);
        }
    }


}
