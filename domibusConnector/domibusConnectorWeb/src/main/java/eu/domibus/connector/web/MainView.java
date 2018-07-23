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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;

import eu.domibus.connector.web.viewAreas.messages.Messages;
import eu.domibus.connector.web.viewAreas.messages.MessagesList;

@HtmlImport("styles/shared-styles.html")
@Route
public class MainView extends VerticalLayout {
	
	Div areaMessages = null;
	Tab messagesTab = new Tab("Messages");
	Tabs TopMenu = new Tabs();

    public MainView(@Autowired ExampleTemplate template, @Autowired Messages messages) {
//        // This is just a simple label created via Elements API
//        Button button = new Button("Click me",
//                event -> template.setValue("Clicked!"));
//        // This is a simple template example
//        add(button, template);
//        setClassName("main-layout");
        
        areaMessages = new Div();
		areaMessages.add(messages);
		areaMessages.setVisible(true);
		
		Map<Tab, Component> tabsToPages = new HashMap<>();
		tabsToPages.put(messagesTab, areaMessages);
		
		
		TopMenu.add(messagesTab);
		Div pages = new Div(areaMessages);
		
		Set<Component> pagesShown = Stream.of(areaMessages)
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
