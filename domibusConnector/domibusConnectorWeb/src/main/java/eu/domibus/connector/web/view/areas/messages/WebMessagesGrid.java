package eu.domibus.connector.web.view.areas.messages;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.klaudeta.PaginatedGrid;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.function.ValueProvider;

import eu.domibus.connector.web.dto.WebMessage;

public class WebMessagesGrid extends PaginatedGrid<WebMessage> {

	// collect all hideable columns, to be iterated over later.
	private Collection<Column> hideableColumns = new HashSet<>();
	
	private Messages messagesView;

	public WebMessagesGrid(Messages messagesView) {
		super(WebMessage.class);
		this.messagesView = messagesView;
		addAllColumns();
		
		for(Column<WebMessage> col : getColumns()) {
			col.setResizable(true);
		}
	}

	private void addAllColumns(){
		addComponentColumn(webMessage -> geMessageDetailsLink(webMessage)).setHeader("Details").setWidth("30px");
		
		addColumn(webMessage -> webMessage.getMessageInfo().getFrom().getPartyId()).setHeader("From Party ID").setWidth("70px").setKey("fromPartyId").setSortable(true);
		addColumn(webMessage -> webMessage.getMessageInfo().getTo().getPartyId()).setHeader("To Party ID").setWidth("70px").setKey("toPartyId").setSortable(true);

		addHideableColumn(WebMessage::getConnectorMessageId, "Connector Message ID", "450px", "connectorMsgId", false, false);
		addHideableColumn(WebMessage::getEbmsMessageId, "ebMS Message ID", "450px", "ebmsMsgId", false, true);
		addHideableColumn(WebMessage::getBackendMessageId, "Backend Message ID", "450px", "backendMsgId", false, true);
		addHideableColumn(WebMessage::getConversationId, "Conversation ID", "450px", "conversation", false, false);
		addHideableColumn(webMessage -> webMessage.getMessageInfo().getOriginalSender(), "Original sender", "300px", "originalSender", true, false);
		addHideableColumn(webMessage -> webMessage.getMessageInfo().getFinalRecipient(), "Final recipient", "300px", "finalRecipient", true, false);
		addHideableColumn(webMessage -> webMessage.getMessageInfo().getService().getService(), "Service", "150px", "service", true, true);
		addHideableColumn(webMessage -> webMessage.getMessageInfo().getAction().getAction(), "Action", "150px", "action", true, true);
		addHideableColumn(WebMessage::getBackendName, "backend name", "150px", "backendName", true, false);
		addHideableColumn(WebMessage::getDirectionSource, "Source", "150px", "source", true, false);
		addHideableColumn(WebMessage::getDirectionTarget, "Target", "150px", "target", true, false);
		addHideableColumn(WebMessage::getDeliveredToNationalSystem, "delivered backend", "300px", "deliveredBackend", true, false);
		addHideableColumn(WebMessage::getDeliveredToGateway, "delivered gateway", "300px", "deliveredGateway", true, false);
		addHideableColumn(WebMessage::getCreated, "created", "300px", "created", true, true);
		addHideableColumn(WebMessage::getConfirmed, "confirmed", "300px", "confirmed", true, false);
		addHideableColumn(WebMessage::getRejected, "rejected", "300px", "rejected", true, false);
		
	}

	// everything below this line is part of the workaround for hideable columns

	private Column<WebMessage> addHideableColumn(String propertyName){
		Column<WebMessage> column = addColumn(propertyName);
		hideableColumns.add(column);
		return column;
	}
	
	private Column<WebMessage> addHideableColumn(ValueProvider<WebMessage, ?> valueProvider, String header, String width, String key, boolean sortable, boolean visible){
//		Column<WebMessage> column = addColumn(valueProvider).setHeader(header).setWidth(width).setSortable(sortable);
		Column<WebMessage> column = addColumn(valueProvider).setHeader(header).setWidth(width).setKey(key).setSortable(sortable);
		column.setVisible(visible);
		hideableColumns.add(column);
		return column;
	}

	private Column<WebMessage> addHideableColumn(Renderer<WebMessage> renderer){
		Column<WebMessage> column = addColumn(renderer);
		hideableColumns.add(column);
		return column;
	}

	public Collection<Column> getHideableColumns() {
		return hideableColumns;
	}

	/**
	 * Hides all given columns, and shows all others
	 * @param columns
	 */
	public void hideColumns(Set<Column> columns) {
		for (Column hideableColumn : getHideableColumns()) {
			hideableColumn.setVisible(!columns.contains(hideableColumn));
		}
	}
	
	public Button geMessageDetailsLink(WebMessage connectorMessage) {
		Button getDetails = new Button(new Icon(VaadinIcon.SEARCH));
		getDetails.addClickListener(e -> messagesView.showMessageDetails(connectorMessage));
		return getDetails;
	}
	
//	public void setMessagesView(Messages messagesView) {
//		this.messagesView = messagesView;
//	}
}
