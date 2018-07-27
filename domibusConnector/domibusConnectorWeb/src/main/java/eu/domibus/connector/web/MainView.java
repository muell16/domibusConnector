package eu.domibus.connector.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import eu.domibus.connector.web.viewAreas.messages.Messages;
import eu.domibus.connector.web.viewAreas.pmodes.PModes;

@HtmlImport("styles/shared-styles.html")
@Route("domibusConnector/")
@PageTitle("domibusConnector")
public class MainView extends VerticalLayout {
	

    public MainView(@Autowired Messages messages, @Autowired PModes pmodes) {
        
    	HorizontalLayout header = createHeader();
    	
    	
    	Div areaMessages = new Div();
		areaMessages.add(messages);
		areaMessages.setVisible(true);
		
		Div areaPModes = new Div();
		areaPModes.add(pmodes);
		areaPModes.setVisible(false);
		
		Tab messagesTab = new Tab("Messages");
		messagesTab.getStyle().set("font-size", "20px");
		Tab pmodesTab = new Tab("PModes");
		pmodesTab.getStyle().set("font-size", "20px");
		
		Map<Tab, Component> tabsToPages = new HashMap<>();
		tabsToPages.put(messagesTab, areaMessages);
		tabsToPages.put(pmodesTab, areaPModes);
		
		Tabs TopMenu = new Tabs();
		
		TopMenu.add(messagesTab);
		TopMenu.add(pmodesTab);
		
		Div pages = new Div(areaMessages, areaPModes);
		
		Set<Component> pagesShown = Stream.of(areaMessages, areaPModes)
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
		headerLayout.setWidth("100vw");
		headerLayout.getStyle().set("border-bottom", "1px solid #9E9E9E");
		headerLayout.getStyle().set("padding-bottom", "16px");
		
		return headerLayout;
	}

}
