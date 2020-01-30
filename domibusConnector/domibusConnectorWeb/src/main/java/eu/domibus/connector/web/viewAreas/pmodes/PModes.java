package eu.domibus.connector.web.viewAreas.pmodes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.router.Route;
import eu.domibus.connector.web.utils.RoleRequired;
import eu.domibus.connector.web.view.MainLayout;
import eu.domibus.connector.web.viewAreas.messages.Messages;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.spring.annotation.UIScope;

//@HtmlImport("styles/shared-styles.html")
//@StyleSheet("styles/grid.css")
@UIScope
@org.springframework.stereotype.Component
@Route(value = PModes.ROUTE, layout = MainLayout.class)
@RoleRequired(role = "ADMIN")
public class PModes extends VerticalLayout {

	public static final String ROUTE = "pmodes";

	Div areaImport = null;
	Div areaDataTables = null;
	
	Tab importTab = new Tab("Import PModes");
	Tab dataTablesTab = new Tab("Data Tables");
	
	Tabs pmodesMenu = new Tabs();
	
	public PModes(@Autowired Import importer, @Autowired DataTables dataTables) {
//		messagesList.setMessagesView(this);
		
		areaImport = new Div();
		areaImport.add(importer);
		areaImport.setVisible(true);
		
		areaDataTables = new Div();
		areaDataTables.add(dataTables);
		areaDataTables.setVisible(false);
		
		Map<Tab, Component> tabsToPages = new HashMap<>();
		tabsToPages.put(importTab, areaImport);
		tabsToPages.put(dataTablesTab, areaDataTables);
		
		
		pmodesMenu.add(importTab, dataTablesTab);
		
		
		Div pages = new Div(areaImport, areaDataTables);
		
		Set<Component> pagesShown = Stream.of(areaImport)
		        .collect(Collectors.toSet());
		
	
		pmodesMenu.addSelectedChangeListener(event -> {
		    pagesShown.forEach(page -> page.setVisible(false));
		    pagesShown.clear();
		    Component selectedPage = tabsToPages.get(pmodesMenu.getSelectedTab());
		    selectedPage.setVisible(true);
		    pagesShown.add(selectedPage);
		});

		add(pmodesMenu,pages);
	}

}
