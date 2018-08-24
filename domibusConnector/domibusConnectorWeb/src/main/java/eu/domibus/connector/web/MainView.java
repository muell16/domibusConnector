package eu.domibus.connector.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import eu.domibus.connector.web.viewAreas.configuration.Configuration;
import eu.domibus.connector.web.viewAreas.info.Info;
import eu.domibus.connector.web.viewAreas.messages.Messages;
import eu.domibus.connector.web.viewAreas.pmodes.PModes;
import eu.domibus.connector.web.viewAreas.users.Users;

@HtmlImport("styles/shared-styles.html")
@Route("domibusConnector/")
@PageTitle("domibusConnector - Administrator")
public class MainView extends VerticalLayout implements BeforeEnterObserver {
	
	Map<Tab, Component> tabsToPages = new HashMap<>();
	Tabs TopMenu = new Tabs();
	
    public MainView(@Autowired Messages messages, @Autowired PModes pmodes, 
    		@Autowired Configuration configuration, @Autowired Users users,
    		@Autowired Info info) {
        
    	HorizontalLayout header = createHeader();
    	
    	
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
		
		add(header,TopMenu,pages);
	
    }
    
    public void beforeEnter(BeforeEnterEvent event) {
    	SecurityContext context = SecurityContextHolder.getContext();
    	if (context.getAuthentication() instanceof AnonymousAuthenticationToken) {
           event.rerouteTo("domibusConnector/login/");
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

	private HorizontalLayout createHeader() {
		
		Div ecodexLogo = new Div();
		Image ecodex = new Image("frontend/images/logo_ecodex_0.png", "eCodex");
		ecodex.setHeight("70px");
		ecodexLogo.add(ecodex);
		ecodexLogo.setHeight("70px");
		
		
		Div domibusConnector = new Div();
		Label dC = new Label("domibusConnector - Administration");
		dC.getStyle().set("font-size", "30px");
		dC.getStyle().set("font-style", "italic");
		dC.getStyle().set("color", "grey");
		domibusConnector.add(dC);
		domibusConnector.getStyle().set("text-align", "center");
		
		
		Div europaLogo = new Div();
		Image europa = new Image("frontend/images/europa-logo.jpg", "europe");
		europa.setHeight("50px");
		europaLogo.add(europa);
		europaLogo.setHeight("50px");
		europaLogo.getStyle().set("margin-right", "3em");
		
		
		HorizontalLayout headerLayout = new HorizontalLayout(ecodexLogo, domibusConnector, europaLogo);
		headerLayout.setAlignItems(Alignment.CENTER);
		headerLayout.expand(domibusConnector);
		headerLayout.setJustifyContentMode(com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode.CENTER);
		headerLayout.setWidth("100%");
//		headerLayout.getStyle().set("border-bottom", "1px solid #9E9E9E");
		headerLayout.getStyle().set("padding-bottom", "16px");
		
		return headerLayout;
	}

}
