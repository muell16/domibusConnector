package eu.domibus.connector.web.viewAreas;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.web.dto.WebMessage;
import eu.domibus.connector.web.service.WebMessageService;

@HtmlImport("styles/shared-styles.html")
@StyleSheet("styles/grid.css")
@Component
@UIScope
public class Messages extends VerticalLayout {
	
	private Grid<WebMessage> grid = new Grid<>();
	private LinkedList<WebMessage> fullList = null;

	public Messages(@Autowired WebMessageService messageService) {

		fullList = messageService.getInitialList();
		
		grid.setItems(fullList);
		grid.addColumn(WebMessage::getConnectorMessageId).setHeader("Connector Message ID").setWidth("500px");
		grid.addColumn(WebMessage::getFromPartyId).setHeader("From Party ID").setWidth("70px");
		grid.addColumn(WebMessage::getToPartyId).setHeader("To Party ID").setWidth("70px");
		grid.addColumn(WebMessage::getService).setHeader("Service").setWidth("70px");
		grid.addColumn(WebMessage::getAction).setHeader("Action").setWidth("70px");
		grid.addColumn(WebMessage::getCreated).setHeader("Created");
		grid.addColumn(WebMessage::getDeliveredToBackend).setHeader("Delivered Backend");
		grid.addColumn(WebMessage::getDeliveredToGateway).setHeader("Delivered Gateway");
		grid.addColumn(WebMessage::getBackendClient).setHeader("Backend Client").setWidth("70px");
		grid.setWidth("1800px");
		grid.setHeight("100%");
		grid.setMultiSort(true);
		
		for(Column<WebMessage> col : grid.getColumns()) {
			col.setSortable(true);
			col.setResizable(true);
		}
			
		VerticalLayout main = new VerticalLayout(grid);
		main.setAlignItems(Alignment.STRETCH);
		main.setSizeFull();
		add(main);
		setHeight("100vh");
		setWidth("100vh");
		reloadList(messageService);
		
	}



	public void reloadList(WebMessageService messageService) {
		grid.setItems(messageService.getInitialList());
	}
	
}
