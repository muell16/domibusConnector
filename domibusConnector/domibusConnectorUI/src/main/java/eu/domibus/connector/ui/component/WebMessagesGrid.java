//package eu.domibus.connector.ui.component;
//
//import com.vaadin.flow.component.Component;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.button.ButtonVariant;
//import com.vaadin.flow.component.contextmenu.ContextMenu;
//import com.vaadin.flow.component.contextmenu.MenuItem;
//import com.vaadin.flow.component.grid.Grid;
//import com.vaadin.flow.component.grid.GridSortOrder;
//import com.vaadin.flow.component.icon.Icon;
//import com.vaadin.flow.component.icon.VaadinIcon;
//import com.vaadin.flow.data.event.SortEvent;
//import com.vaadin.flow.data.provider.SortDirection;
//import eu.domibus.connector.ui.dto.WebMessage;
//import eu.domibus.connector.ui.persistence.service.impl.DomibusConnectorWebMessagePersistenceService;
//import eu.domibus.connector.ui.view.areas.messages.MessageDetails;
//import lombok.Getter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class WebMessagesGrid extends Grid<WebMessage> {
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(WebMessagesGrid.class);
//
//	private final DomibusConnectorWebMessagePersistenceService dcMessagePersistenceService;
//
//	private final MessageFilter filter;
//
//	@Getter
//	private final Button visibleColumnsMenu;
//
//	public WebMessagesGrid(DomibusConnectorWebMessagePersistenceService dcMessagePersistenceService, MessageFilter filter) {
//		super();
//		this.dcMessagePersistenceService = dcMessagePersistenceService;
//		this.filter = filter;
//
//		addComponentColumn(this::geMessageDetailsLink).setHeader("Details").setAutoWidth(true);
//
//		final Column<WebMessage> fromParty = addColumn(
//				webMessage -> webMessage.getMessageInfo() != null && webMessage.getMessageInfo().getFrom() != null
//						? webMessage.getMessageInfo().getFrom().getPartyString()
//						: "")
//				.setHeader("From Party").setAutoWidth(true).setKey("messageInfo.from.partyId").setSortable(true)
//				.setResizable(true);
//
//		final Column<WebMessage> toParty = addColumn(
//				webMessage -> webMessage.getMessageInfo() != null && webMessage.getMessageInfo().getTo() != null
//						? webMessage.getMessageInfo().getTo().getPartyString()
//						: "")
//				.setHeader("To Party").setAutoWidth(true).setKey("messageInfo.to.partyId").setSortable(true)
//				.setResizable(true);
//
//		final String connectorMessageIdHeader = "Connector Message ID";
//		final Column<WebMessage> connectorMsgIdColumn = addColumn(WebMessage::getConnectorMessageId).setHeader(connectorMessageIdHeader).setAutoWidth(true)
//				.setKey("connectorMessageId").setSortable(true).setResizable(true);
//
//		final String ebMSMessageIdHeader = "ebMS Message ID";
//		final Column<WebMessage> ebmsIdColumn = addColumn(WebMessage::getEbmsId).setHeader(ebMSMessageIdHeader).setAutoWidth(true)
//				.setKey("ebmsMessageId").setSortable(true).setResizable(true);
//
//		final String backendMessageIdHeader = "Backend Message ID";
//		final Column<WebMessage> backendMsgIdColumn = addColumn(WebMessage::getBackendMessageId).setHeader(backendMessageIdHeader).setAutoWidth(true)
//				.setKey("backendMessageId").setSortable(true).setResizable(true);
//
//		final String conversationIdHeader = "Conversation ID";
//		final Column<WebMessage> conversationIdColumn = addColumn(WebMessage::getConversationId).setHeader(conversationIdHeader).setAutoWidth(true)
//				.setKey("conversationId").setSortable(true).setResizable(true);
//
//		final String msgDirectionHeader = "Message Direction";
//		final Column<WebMessage> msgDirectionColumn = addColumn(WebMessage::getMsgDirection).setHeader(msgDirectionHeader).setAutoWidth(true)
//				.setKey("messageDirection").setSortable(true).setResizable(true);
//
//		visibleColumnsMenu = new Button("Show/Hide Columns");
//		visibleColumnsMenu.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//		ShowHideColumnContextMenu columnToggleContextMenu = new ShowHideColumnContextMenu(visibleColumnsMenu);
//		columnToggleContextMenu.addColumnToggleItem(connectorMessageIdHeader, connectorMsgIdColumn);
//		columnToggleContextMenu.addColumnToggleItem(ebMSMessageIdHeader, ebmsIdColumn);
//		columnToggleContextMenu.addColumnToggleItem(backendMessageIdHeader, backendMsgIdColumn);
//		columnToggleContextMenu.addColumnToggleItem(conversationIdHeader, conversationIdColumn);
//		columnToggleContextMenu.addColumnToggleItem(msgDirectionHeader, msgDirectionColumn);
//
//		setMultiSort(false);
//		setWidth("100%");
//		addSortListener(this::handleSortEvent);
//	}
//
//	private static class ShowHideColumnContextMenu extends ContextMenu {
//		public ShowHideColumnContextMenu(Component target) {
//			super(target);
//			setOpenOnClick(true);
//		}
//
//		void addColumnToggleItem(String label, Grid.Column<?> column) {
//			MenuItem menuItem = this.addItem(label, e -> {
//				column.setVisible(e.getSource().isChecked());
//			});
//			menuItem.setCheckable(true);
//			menuItem.setChecked(column.isVisible());
//		}
//	}
//
//	public Button geMessageDetailsLink(WebMessage connectorMessage) {
//		Button getDetails = new Button(new Icon(VaadinIcon.SEARCH));
//		getDetails.addClickListener(e -> getDetails.getUI()
//				.ifPresent(ui->ui.navigate(MessageDetails.class, connectorMessage.getConnectorMessageId())));
//		return getDetails;
//	}
//
//	private void handleSortEvent(SortEvent<Grid<WebMessage>, GridSortOrder<WebMessage>> gridGridSortOrderSortEvent) {
//		gridGridSortOrderSortEvent.getSortOrder().stream()
//				.map(webMessageGridSortOrder -> {
//					SortDirection direction = webMessageGridSortOrder.getDirection();
//					return direction;
//				});
//	}
//	public class MessageFilter {
//
//		private final WebMessagesGrid grid;
//		private String connectorMsgId;
//		private String ebmsId;
//		private String backendMsgId;
//		private String conversationId;
//
//		private MessageFilter(WebMessagesGrid grid) {
//			this.grid = grid;
//		}
//
//		public void setConnectorMsgId(String connectorMsgId) {
//			this.connectorMsgId = connectorMsgId;
//			this.grid.applyFilters(this);
//		}
//
//		public void setEbmsId(String ebmsId) {
//			this.ebmsId = ebmsId;
//			this.grid.applyFilters(this);
//		}
//
//		public void setBackendMsgId(String backendMsgId) {
//			this.backendMsgId = backendMsgId;
//			this.grid.applyFilters(this);
//		}
//
//		public void setConversationId(String conversationId) {
//			this.conversationId = conversationId;
//			this.grid.applyFilters(this);
//		}
//
//
//		private boolean matches(String value, String searchTerm) {
//			return searchTerm == null || searchTerm.isEmpty() || value
//					.toLowerCase().contains(searchTerm.toLowerCase());
//		}
//	}
//
//	public void applyFilters(MessageFilter filter) {
//		this.setItems(messagePersistenceService.findAll().stream()
//				.filter(msg -> filter.matches(msg.getConnectorMessageId(), filter.connectorMsgId))
//				.filter(msg -> filter.matches(msg.getEbmsId(), filter.ebmsId))
//				.filter(msg -> filter.matches(msg.getBackendMessageId(), filter.backendMsgId))
//				.filter(msg -> filter.matches(msg.getConversationId(), filter.conversationId)));
//
//	}
//		public void reloadList() {
//		LOGGER.debug("#reloadList");
//		this.setItems(dcMessagePersistenceService.findAll().stream());
//	}
//}
