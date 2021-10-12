package eu.domibus.connector.ui.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.ui.login.LogoutView;
import eu.domibus.connector.ui.utils.DCTabHandler;
import eu.domibus.connector.ui.view.areas.configuration.ConfigurationOverview;
import eu.domibus.connector.ui.view.areas.info.Info;
import eu.domibus.connector.ui.view.areas.messages.MessageOverview;
import eu.domibus.connector.ui.view.areas.monitoring.JmsMonitoringView;
import eu.domibus.connector.ui.view.areas.pmodes.PmodeOverview;
import eu.domibus.connector.ui.view.areas.users.UserOverview;

@UIScope
@org.springframework.stereotype.Component
@Push
public class MainLayout extends AppLayout implements RouterLayout, BeforeEnterObserver {

    private Tabs tabs;
    private DCTabHandler tabManager = new DCTabHandler();

    public MainLayout() {

        setPrimarySection(Section.DRAWER);

        VerticalLayout topBar = new VerticalLayout();
        topBar.add(new DomibusConnectorAdminHeader());
        addToNavbar(topBar);

        tabManager.setTabFontSize("bigger");
        tabManager
                .createTab()
                .withLabel("Messages")
                .withIcon(new Icon(VaadinIcon.LIST))
                .addForComponent(MessageOverview.class);

        tabManager
                .createTab()
                .withLabel("PModes")
                .withIcon(new Icon(VaadinIcon.FILE_CODE))
                .addForComponent(PmodeOverview.class);

        tabManager
                .createTab()
                .withLabel("Configuration")
                .withIcon(new Icon(VaadinIcon.COG_O))
                .addForComponent(ConfigurationOverview.class);

        tabManager
                .createTab()
                .withLabel("Monitoring")
                .withIcon(new Icon(VaadinIcon.DASHBOARD))
                .addForComponent(JmsMonitoringView.class);


        tabManager
                .createTab()
                .withLabel("Users")
                .withIcon(new Icon(VaadinIcon.USERS))
                .addForComponent(UserOverview.class);

        tabManager
                .createTab()
                .withLabel("Info")
                .withIcon(VaadinIcon.INFO_CIRCLE_O)
                .addForComponent(Info.class);

        tabManager
                .createTab()
                .withLabel("Logout")
                .withIcon(new Icon(VaadinIcon.ARROW_RIGHT))
                .addForComponent(LogoutView.class);

        tabs = tabManager.getTabs();
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);

        topBar.add(tabs);

//        addToDrawer(menuTabs);
    }

    public void beforeEnter(BeforeEnterEvent event) {
//        currentlyLoggedInUser.setText("User: " + SecurityUtils.getUsername());
        tabManager.beforeEnter(event);
    }
}
