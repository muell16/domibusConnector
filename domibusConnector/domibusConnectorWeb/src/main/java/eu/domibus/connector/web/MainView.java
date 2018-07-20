package eu.domibus.connector.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.web.viewAreas.Messages;

@HtmlImport("styles/shared-styles.html")
@Route("domibusConnector/")
@org.springframework.stereotype.Component
@UIScope
public class MainView extends VerticalLayout {

	Div areaMessages = null;
	Tab messagesTab = new Tab("Messages");
	Tabs TopMenu = new Tabs();
	
	public MainView() {
		
		
		areaMessages = new Div();
		areaMessages.add(new Messages());
		areaMessages.setVisible(true);
		
		
		
//		detailsERV = new Div();
//		//detailsERV.setText("Details not implemented Yet");
//		ervDetails = new ERVDetailsComponent(conf);
//		detailsERV.add(ervDetails);
//		detailsERV.setWidth("100vh");
//		detailsERV.setVisible(false);
		
		
//		logDetails = new Div();
//		
//		logDetails.add(logger);
//		logDetails.setVisible(false);
//		
//
//		
//		
//		demoDiv = new Div();
//		demoDiv.add(new  MainViewComponent());
//		demoDiv.setVisible(false);
		
		
//		close.addClickListener(e -> { 
//			close.getUI().ifPresent(ui -> ui.navigate("logout/"));
//			});
//		
//		
//		Close.add(close);
		
		
//		exitDiv = new Div();
//		exitDiv.add(new LogoutComponent());
//		exitDiv.setVisible(false);
		
		
		Map<Tab, Component> tabsToPages = new HashMap<>();
		tabsToPages.put(messagesTab, areaMessages);
//		tabsToPages.put(ErvOverviewTab, areaERV);
//		tabsToPages.put(ErvDetails, detailsERV);
//		tabsToPages.put(LogTab, logDetails);
//		tabsToPages.put(Demo, demoDiv);
//		tabsToPages.put(Close, exitDiv);
		
		
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
//	
//	public void showERVMessageDetails(String ervMsgID) {
//		ervDetails.loadErvDetails(ervMsgID);
//		ervDetails.setAlignItems(Alignment.START);
//		TopMenu.setSelectedTab(ErvDetails);
//	}

}
