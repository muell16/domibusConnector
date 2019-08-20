package eu.domibus.connector.web.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import eu.domibus.connector.web.component.LumoLabel;
import eu.domibus.connector.web.dto.WebUser;
import eu.domibus.connector.web.enums.UserRole;
import eu.domibus.connector.web.viewAreas.configuration.Configuration;
import eu.domibus.connector.web.viewAreas.info.Info;
import eu.domibus.connector.web.viewAreas.messages.Messages;
import eu.domibus.connector.web.viewAreas.pmodes.PModes;
import eu.domibus.connector.web.viewAreas.users.Users;

@HtmlImport("styles/shared-styles.html")
@Route(MainView.MAIN_VIEW_ROUTE)
@PageTitle("domibusConnector - Administrator")
public class MainView extends VerticalLayout 
implements BeforeEnterObserver 
{

	public static final String MAIN_VIEW_ROUTE = "";

	Map<Tab, Component> tabsToPages = new HashMap<>();
	Tabs TopMenu = new Tabs();
	WebUser authenticatedUser;
	UserInfo userInfo;
	
    public MainView(@Autowired DomibusConnectorAdminHeader header, @Autowired UserInfo userInfo, @Autowired Messages messages, @Autowired PModes pmodes, 
    		@Autowired Configuration configuration, @Autowired Users users,
    		@Autowired Info info) {
    	
    	this.userInfo = userInfo;
        
    	Div areaMessages = new Div();
		areaMessages.add(messages);
		areaMessages.setVisible(false);
		
		Div areaPModes = new Div();
		areaPModes.add(pmodes);
		areaPModes.setVisible(false);
		
		Div areaConfiguration = new Div();
		areaConfiguration.add(configuration);
		areaConfiguration.setVisible(false);
		
		Div areaUsers = new Div();
		areaUsers.add(users);
		areaUsers.setVisible(false);
		
		Div areaInfo = new Div();
		areaInfo.add(info);
		areaInfo.setVisible(true);
		
		createTab(areaMessages, "Messages", new Icon(VaadinIcon.LIST), false);
		
		createTab(areaPModes, "PModes", new Icon(VaadinIcon.FILE_CODE), false);
		
		createTab(areaConfiguration, "Configuration", new Icon(VaadinIcon.FILE_PROCESS), false);
		
		createTab(areaUsers, "Users", new Icon(VaadinIcon.USERS), false);
		
		createTab(areaInfo, "Info", new Icon(VaadinIcon.INFO_CIRCLE_O), true);
		
		
		Div pages = new Div(areaMessages, areaPModes, areaConfiguration, areaUsers, areaInfo);
		
		Set<Component> pagesShown = Stream.of(areaMessages, areaPModes, areaConfiguration, areaUsers, areaInfo)
		        .collect(Collectors.toSet());
		
		TopMenu.addSelectedChangeListener(event -> {
		    pagesShown.forEach(page -> page.setVisible(false));
		    pagesShown.clear();
		    Component selectedPage = tabsToPages.get(TopMenu.getSelectedTab());
		    selectedPage.setVisible(true);
		    pagesShown.add(selectedPage);
		});
		
		add(header, userInfo,TopMenu,pages);
	
    }
    
    public void beforeEnter(BeforeEnterEvent event) {
    	boolean authenticated = false;
    	SecurityContext context = SecurityContextHolder.getContext();
    	if(context.getAuthentication().getPrincipal()!=null) {
    		if(context.getAuthentication().getPrincipal() instanceof WebUser) {
    			WebUser authUser = (WebUser) context.getAuthentication().getPrincipal();
    			this.authenticatedUser = authUser;
    			this.userInfo.setUsernameValue(authUser.getUsername());
    			authenticated = true;
    			Tab pmodesTab = (Tab) TopMenu.getComponentAt(1);
    			pmodesTab.setEnabled(authenticatedUser.getRole().equals(UserRole.ADMIN));
    			Tab usersTab = (Tab) TopMenu.getComponentAt(3);
    			usersTab.setEnabled(authenticatedUser.getRole().equals(UserRole.ADMIN));
    		}
    	}
    	
    	if (!authenticated) {
           event.rerouteTo("login/");
        }
    	
     }

	
	private void createTab(Div tabArea, String tabLabel, Icon tabIcon, boolean selected) {
		Span tabText = new Span(tabLabel);
		tabText.getStyle().set("font-size", "20px");
		
		tabIcon.setSize("20px");
		
		HorizontalLayout tabLayout = new HorizontalLayout(tabIcon, tabText);
		tabLayout.setAlignItems(Alignment.CENTER);
		Tab tab = new Tab(tabLayout);
		tab.setSelected(selected);
		
		tabsToPages.put(tab, tabArea);
		TopMenu.add(tab);
		if(selected) {
			TopMenu.setSelectedTab(tab);
		}
		
	}
	
	

}
