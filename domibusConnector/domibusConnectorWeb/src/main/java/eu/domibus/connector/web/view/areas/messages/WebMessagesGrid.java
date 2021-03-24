package eu.domibus.connector.web.view.areas.messages;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.vaadin.klaudeta.PaginatedGrid;

import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.function.ValueProvider;

import eu.domibus.connector.web.dto.WebMessage;

public class WebMessagesGrid extends PaginatedGrid<WebMessage> {

	// collect all hideable columns, to be iterated over later.
	private Collection<Column> hideableColumns = new HashSet<>();
	
	private Messages messagesView;

	public WebMessagesGrid() {
		super(WebMessage.class);
		addAllColumns();
		
		for(Column<WebMessage> col : getColumns()) {
			col.setResizable(true);
		}
	}

	private void addAllColumns(){
		addComponentColumn(webMessage -> messagesView.geMessageDetailsLink(webMessage)).setHeader("Details").setWidth("30px");
		
		addColumn(webMessage -> webMessage.getMessageInfo().getFrom().getPartyId()).setHeader("From Party ID").setWidth("70px").setKey("fromPartyId").setSortable(true);
		addColumn(webMessage -> webMessage.getMessageInfo().getTo().getPartyId()).setHeader("To Party ID").setWidth("70px").setKey("toPartyId").setSortable(true);

		addHideableColumn(WebMessage::getConnectorMessageId).setHeader("Connector Message ID").setWidth("450px").setKey("connectorMessageId").setSortable(false);
		addHideableColumn(WebMessage::getEbmsMessageId).setHeader("ebMS Message ID").setWidth("450px").setKey("ebmsMessageId").setSortable(false);
		addHideableColumn(WebMessage::getBackendMessageId).setHeader("Backend Message ID").setWidth("450px").setKey("backendMessageId").setSortable(false);
		addHideableColumn(WebMessage::getConversationId).setHeader("Conversation ID").setWidth("450px").setKey("conversationId").setSortable(false);
		
	}

	// everything below this line is part of the workaround for hideable columns

	private Column<WebMessage> addHideableColumn(String propertyName){
		Column<WebMessage> column = addColumn(propertyName);
		hideableColumns.add(column);
		return column;
	}
	
	private Column<WebMessage> addHideableColumn(ValueProvider<WebMessage, ?> valueProvider){
		Column<WebMessage> column = addColumn(valueProvider);
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
	
	public void setMessagesView(Messages messagesView) {
		this.messagesView = messagesView;
	}
}
