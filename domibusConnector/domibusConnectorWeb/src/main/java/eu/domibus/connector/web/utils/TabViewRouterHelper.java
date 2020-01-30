package eu.domibus.connector.web.utils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import eu.domibus.connector.web.configuration.SecurityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for creating a tab menu
 * with support to navigate between this tabs
 * and also only show tabs which the corresponding view
 * can be accessed as enabled
 */
public class TabViewRouterHelper implements BeforeEnterObserver {

    Tabs tabMenu = new Tabs();
    Map<Tab, Class> tabsToPages = new HashMap<>();
    Map<Class, Tab> pagesToTab = new HashMap<>();

    private String tabFontSize = "normal";

    public TabViewRouterHelper() {
        tabMenu.addSelectedChangeListener(this::tabSelectionChanged);
    }

    public Tabs getTabs() {
        return this.tabMenu;
    }

    public String getTabFontSize() {
        return tabFontSize;
    }

    public void setTabFontSize(String tabFontSize) {
        this.tabFontSize = tabFontSize;
    }

    public TabBuilder createTab() {
        return new TabBuilder();
    }


    private void tabSelectionChanged(Tabs.SelectedChangeEvent selectedChangeEvent) {
        if (selectedChangeEvent.isFromClient()) {
            Tab selectedTab = selectedChangeEvent.getSelectedTab();
            Class component = tabsToPages.get(selectedTab);

            UI.getCurrent().navigate(component);

        }
    }


    /**
     * sets the current selected tab dependent
     * on the current view
     */
    private void setSelectedTab(BeforeEnterEvent event) {
        Class<?> navigationTarget = event.getNavigationTarget();
        if (navigationTarget != null) {
            Tab tab = pagesToTab.get(navigationTarget);
            tabMenu.setSelectedTab(tab);
        } else {
            tabMenu.setSelectedTab(null);
        }
    }

    /**
     * set tab enabled if the view is accessable
     * by the current user
     */
    private void setTabEnabledOnUserRole() {
        pagesToTab.entrySet().stream()
                .forEach(entry -> {
                    entry.getValue().setEnabled(SecurityUtils.isUserAllowedToView(entry.getKey()));
                });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        setSelectedTab(event);
        setTabEnabledOnUserRole();
    }

    public class TabBuilder {

        private Icon tabIcon;
        private String tabLabel = "";
        private Component component;
        private Class<? extends Component> clz;

        private TabBuilder() {}

        public TabBuilder withIcon(Icon icon) {
            this.tabIcon = icon;
            return this;
        }

        public TabBuilder withIcon(VaadinIcon icon) {
            this.tabIcon = new Icon(icon);
            return this;
        }

        public TabBuilder withLabel(String label) {
            this.tabLabel = label;
            return this;
        }

        public Tab addForComponent(Component component) {
            clz = component.getClass();
            return addForComponent(clz);
        }

        public Tab addForComponent(Class clz) {
            if (clz == null) {
                throw new IllegalArgumentException("component is not allowed to be null!");
            }


            Span tabText = new Span(tabLabel);
            tabText.getStyle().set("font-size", tabFontSize);

            Tab tab = new Tab(tabText);
            if (tabIcon != null) {
                tabIcon.setSize(tabFontSize);
                HorizontalLayout tabLayout = new HorizontalLayout(tabIcon, tabText);
                tabLayout.setAlignItems(FlexComponent.Alignment.CENTER);
                tab = new Tab(tabLayout);
            }

            tabsToPages.put(tab, clz);
            pagesToTab.put(clz, tab);
            tabMenu.add(tab);
            return tab;
        }


    }



}
