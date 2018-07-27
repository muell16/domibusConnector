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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
	
	Div areaMessages = null;
	Div areaPModes = null;
	
	Tab messagesTab = new Tab("Messages");
	Tab pmodesTab = new Tab("PModes");
	
	Tabs TopMenu = new Tabs();

    public MainView(@Autowired Messages messages, @Autowired PModes pmodes) {
        
        areaMessages = new Div();
		areaMessages.add(messages);
		areaMessages.setVisible(true);
		
		areaPModes = new Div();
		areaPModes.add(pmodes);
		areaPModes.setVisible(false);
		
		Map<Tab, Component> tabsToPages = new HashMap<>();
		tabsToPages.put(messagesTab, areaMessages);
		tabsToPages.put(pmodesTab, areaPModes);
		
		
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

		add(TopMenu,pages);
    }

}
