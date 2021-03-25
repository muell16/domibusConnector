package eu.domibus.connector.web.view.areas.messages;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.klaudeta.PaginatedGrid;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.event.SortEvent;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.function.ValueProvider;

import eu.domibus.connector.web.dto.WebMessage;

public class WebMessagesGrid extends PaginatedGrid<WebMessage> {

	// collect all hideable columns, to be iterated over later.
	private Map<String,Column> hideableColumns = new HashMap<>();
	
	private Messages messagesView;

	public WebMessagesGrid(Messages messagesView) {
		super();
		this.messagesView = messagesView;
		addAllColumns();
		
		for(Column<WebMessage> col : getColumns()) {
			col.setResizable(true);
		}
		
		setMultiSort(false);
		setWidth("100vh");
		addSortListener(this::handleSortEvent);
	}

	private void addAllColumns(){
		addComponentColumn(webMessage -> geMessageDetailsLink(webMessage)).setHeader("Details").setWidth("30px");
		
		addColumn(webMessage -> webMessage.getMessageInfo().getFrom().getPartyId()).setHeader("From Party ID").setWidth("70px").setKey("fromPartyId").setSortable(true);
		addColumn(webMessage -> webMessage.getMessageInfo().getTo().getPartyId()).setHeader("To Party ID").setWidth("70px").setKey("toPartyId").setSortable(true);

		addHideableColumn(WebMessage::getConnectorMessageId, "Connector Message ID", "450px", "connectorMessageId", false, false);
		addHideableColumn(WebMessage::getEbmsMessageId, "ebMS Message ID", "450px", "ebmsMessageId", false, true);
		addHideableColumn(WebMessage::getBackendMessageId, "Backend Message ID", "450px", "backendMessageId", false, true);
		addHideableColumn(WebMessage::getConversationId, "Conversation ID", "450px", "conversationId", false, false);
		addHideableColumn(webMessage -> webMessage.getMessageInfo().getOriginalSender(), "Original sender", "300px", "originalSender", true, false);
		addHideableColumn(webMessage -> webMessage.getMessageInfo().getFinalRecipient(), "Final recipient", "300px", "finalRecipient", true, false);
		addHideableColumn(webMessage -> webMessage.getMessageInfo().getService().getService(), "Service", "150px", "service", true, true);
		addHideableColumn(webMessage -> webMessage.getMessageInfo().getAction().getAction(), "Action", "150px", "action", true, true);
		addHideableColumn(WebMessage::getBackendName, "backend name", "150px", "backendName", true, false);
		addHideableColumn(WebMessage::getDirection, "direction", "150px", "direction", false, false);
		addHideableColumn(WebMessage::getDeliveredToNationalSystem, "delivered backend", "300px", "deiliveredToNationalSystem", true, false);
		addHideableColumn(WebMessage::getDeliveredToGateway, "delivered gateway", "300px", "deliveredToGateway", true, false);
		addHideableColumn(WebMessage::getCreated, "created", "300px", "created", true, true);
		addHideableColumn(WebMessage::getConfirmed, "confirmed", "300px", "confirmed", true, false);
		addHideableColumn(WebMessage::getRejected, "rejected", "300px", "rejected", true, false);
		
	}

	
	private void addHideableColumn(ValueProvider<WebMessage, ?> valueProvider, String header, String width, String key, boolean sortable, boolean visible){
		Column<WebMessage> column = addColumn(valueProvider).setHeader(header).setKey(key).setWidth(width).setSortable(sortable);
		column.setVisible(visible);
		hideableColumns.put(header, column);
	}
	
	public Map<String, Column> getHideableColumns() {
		return hideableColumns;
	}
	
	public Set<String> getHideableColumnNames() {
		return hideableColumns.keySet();
	}

	public Button geMessageDetailsLink(WebMessage connectorMessage) {
		Button getDetails = new Button(new Icon(VaadinIcon.SEARCH));
		getDetails.addClickListener(e -> messagesView.showMessageDetails(connectorMessage));
		return getDetails;
	}
	
	private void handleSortEvent(SortEvent<Grid<WebMessage>, GridSortOrder<WebMessage>> gridGridSortOrderSortEvent) {
		gridGridSortOrderSortEvent.getSortOrder().stream()
				.map(webMessageGridSortOrder -> {
					SortDirection direction = webMessageGridSortOrder.getDirection();
					return direction;

				});
	}

}
