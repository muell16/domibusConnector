package eu.domibus.connector.ui.view.areas.messages;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import eu.domibus.connector.ui.dto.WebMessage;
import eu.domibus.connector.ui.persistence.service.impl.DomibusConnectorWebMessagePersistenceService;
import eu.domibus.connector.ui.service.WebMessageService;
import eu.domibus.connector.ui.view.areas.configuration.TabMetadata;
import eu.ecodex.dc5.message.model.DC5EcxAddress;
import lombok.Getter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@UIScope
@Route(value = MessagesList.ROUTE, layout = MessageLayout.class)
@Order(1)
@TabMetadata(title = "All Messages", tabGroup = MessageLayout.TAB_GROUP_NAME)
public class MessagesList extends Div implements AfterNavigationObserver {

	public static final String ROUTE = "messagelist";

	private final DomibusConnectorWebMessagePersistenceService messagePersistenceService;

	private final Grid<WebMessage> grid;

	private final MessageFilter filter;

	@Getter
	private final Button visibleColumnsMenu;
	
	public MessagesList(WebMessageService messageService, DomibusConnectorWebMessagePersistenceService messagePersistenceService) {
		this.messagePersistenceService = messagePersistenceService;
		this.filter = new MessageFilter();

		grid = new Grid<>(WebMessage.class, false);
		grid.setSizeFull(); // take as much space as is made available by parent.
		grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

		grid.addComponentColumn(this::geMessageDetailsLink)
				.setHeader("Details").setAutoWidth(true);

		// NOTE: the keys must map to DC5Message otherwise sorting will not work
		final Grid.Column<WebMessage> initiator = grid.addColumn(msg -> createEcxAddressRenderer(msg.getInitiator()))
				.setHeader("Initiator")
				.setAutoWidth(true)
				.setKey("ebmsData.initiator")
				.setSortable(true)
				.setResizable(true);

		final Grid.Column<WebMessage> responder = grid.addColumn(msg -> createEcxAddressRenderer(msg.getResponder()))
				.setHeader("Responder")
				.setAutoWidth(true)
				.setKey("ebmsData.responder")
				.setSortable(true)
				.setResizable(true);

		final Grid.Column<WebMessage> domain = grid.addColumn(WebMessage::getDomain)
				.setHeader("Domain")
				.setAutoWidth(true)
				.setKey("businessDomainId")
				.setSortable(true)
				.setResizable(true);

		final String connectorMessageIdHeader = "Connector Message ID";
		final Grid.Column<WebMessage> connectorMsgIdColumn = grid.addColumn(WebMessage::getConnectorMessageId)
				.setHeader(connectorMessageIdHeader)
				.setAutoWidth(true)
				.setKey("connectorMessageId")
				.setSortable(true)
				.setResizable(true);

		final String ebMSMessageIdHeader = "ebMS Message ID";
		final Grid.Column<WebMessage> ebmsIdColumn = grid.addColumn(WebMessage::getEbmsId)
				.setHeader(ebMSMessageIdHeader)
				.setAutoWidth(true)
				.setKey("ebmsData.ebmsMessageId")
				.setSortable(true).setResizable(true);

		final String backendMessageIdHeader = "Backend Message ID";
		final Grid.Column<WebMessage> backendMsgIdColumn = grid.addColumn(WebMessage::getBackendMessageId)
				.setHeader(backendMessageIdHeader)
				.setAutoWidth(true)
				.setKey("backendData.backendMessageId")
				.setSortable(true)
				.setResizable(true);

		final String conversationIdHeader = "Conversation ID";
		final Grid.Column<WebMessage> conversationIdColumn = grid.addColumn(WebMessage::getConversationId)
				.setHeader(conversationIdHeader)
				.setAutoWidth(true)
				.setKey("ebmsData.conversationId").setSortable(true).setResizable(true);

		final String msgDirectionHeader = "Message Direction";
		final Grid.Column<WebMessage> msgDirectionColumn = grid.addColumn(WebMessage::getMessageDirection)
				.setHeader(msgDirectionHeader)
				.setAutoWidth(true)
				.setKey("messageDirection")
				.setSortable(false)
				.setResizable(true);

		final String prvStatesHeader = "Previous States";
		final Grid.Column<WebMessage> prvStatesColumn = grid.addColumn(WebMessage::getPrvStates)
				.setHeader(prvStatesHeader)
				.setAutoWidth(true)
				.setKey("messageContent.messageStates")
				.setSortable(false)
				.setResizable(true);

		final String currentStateHeader = "Current State";
		final Grid.Column<WebMessage> currentStateColumn = grid.addColumn(WebMessage::getMessageContentState)
				.setHeader(currentStateHeader)
				.setAutoWidth(true)
				.setKey("messageContent.currentState")
				.setSortable(false)
				.setResizable(true);

		visibleColumnsMenu = new Button("Show/Hide Columns");
		visibleColumnsMenu.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		ShowHideColumnContextMenu columnToggleContextMenu = new ShowHideColumnContextMenu(visibleColumnsMenu);
		columnToggleContextMenu.addColumnToggleItem(connectorMessageIdHeader, connectorMsgIdColumn);
		columnToggleContextMenu.addColumnToggleItem(ebMSMessageIdHeader, ebmsIdColumn);
		columnToggleContextMenu.addColumnToggleItem(backendMessageIdHeader, backendMsgIdColumn);
		columnToggleContextMenu.addColumnToggleItem(conversationIdHeader, conversationIdColumn);
		columnToggleContextMenu.addColumnToggleItem(msgDirectionHeader, msgDirectionColumn);
		columnToggleContextMenu.addColumnToggleItem(prvStatesHeader, prvStatesColumn);
		columnToggleContextMenu.addColumnToggleItem(currentStateHeader, currentStateColumn);

		grid.setMultiSort(false);
		setWidth("100%");

		Span title = new Span("Messages");
		title.getStyle().set("font-weight", "bold");
		HorizontalLayout headerLayout = new HorizontalLayout(title, visibleColumnsMenu);
		headerLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
		headerLayout.setFlexGrow(1, title);

		add(headerLayout, grid);
		// expand the div to the whole page (this sets how much is available when children try to maximize).
		setSizeFull();


		grid.getHeaderRows().clear();
		HeaderRow headerRow = grid.appendHeaderRow();

		headerRow.getCell(connectorMsgIdColumn).setComponent(
				createFilterHeader("Connector Message ID", filter::setConnectorMsgId));
		headerRow.getCell(ebmsIdColumn).setComponent(
				createFilterHeader("EBMS ID", filter::setEbmsId));
		headerRow.getCell(backendMsgIdColumn).setComponent(
				createFilterHeader("Backend ID", filter::setBackendMsgId));
		headerRow.getCell(conversationIdColumn).setComponent(
				createFilterHeader("Conversation ID", filter::setConversationId));
	}

	private static com.vaadin.flow.component.Component createFilterHeader(String labelText,
													 Consumer<String> filterChangeConsumer) {
//		Label label = new Label(labelText);
//		label.getStyle().set("padding-top", "var(--lumo-space-m)")
//				.set("font-size", "var(--lumo-font-size-xs)");
		TextField textField = new TextField();
		textField.setValueChangeMode(ValueChangeMode.LAZY);
		textField.setClearButtonVisible(true);
		textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
		textField.setWidthFull();
		textField.getStyle().set("max-width", "100%");
		textField.addValueChangeListener(
				e -> filterChangeConsumer.accept(e.getValue()));
		VerticalLayout layout = new VerticalLayout(textField);
		layout.getThemeList().clear();
		layout.getThemeList().add("spacing-xs");

		return layout;
	}


	private static class ShowHideColumnContextMenu extends ContextMenu {
		public ShowHideColumnContextMenu(com.vaadin.flow.component.Component target) {
			super(target);
			setOpenOnClick(true);
		}

		void addColumnToggleItem(String label, Grid.Column<?> column) {
			MenuItem menuItem = this.addItem(label, e -> {
				column.setVisible(e.getSource().isChecked());
			});
			menuItem.setCheckable(true);
			menuItem.setChecked(column.isVisible());
		}
	}

	public Button geMessageDetailsLink(WebMessage connectorMessage) {
		Button getDetails = new Button(new Icon(VaadinIcon.SEARCH));
		getDetails.addClickListener(e -> getDetails.getUI()
				.ifPresent(ui->ui.navigate(MessageDetails.class, connectorMessage.getConnectorMessageId())));
		return getDetails;
	}


	private class MessageFilter {

		private String connectorMsgId;
		private String ebmsId;
		private String backendMsgId;
		private String conversationId;

		public void setConnectorMsgId(String connectorMsgId) {
			this.connectorMsgId = connectorMsgId.isEmpty() ? null : connectorMsgId;
			applyFilters(this);
		}

		public void setEbmsId(String ebmsId) {
			this.ebmsId = ebmsId.isEmpty() ? null : ebmsId;
			applyFilters(this);
		}

		public void setBackendMsgId(String backendMsgId) {
			this.backendMsgId = backendMsgId.isEmpty() ? null : backendMsgId;
			applyFilters(this);
		}

		public void setConversationId(String conversationId) {
			this.conversationId = conversationId.isEmpty() ? null : conversationId;
			applyFilters(this);
		}

	}

	public void applyFilters(MessageFilter filter) {

		grid.setItems(query ->
				messagePersistenceService.findByWebFilter(filter.connectorMsgId, filter.ebmsId, filter.backendMsgId, filter.conversationId,
						VaadinSpringDataHelpers.toSpringPageRequest(query))
						.stream());
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		grid.setItems(query ->
				messagePersistenceService
						.findAll(VaadinSpringDataHelpers.toSpringPageRequest(query))
						.stream()
		);
	}

	private static Renderer<WebMessage> createEcxAddressRenderer(String ecxAddress) {
		return LitRenderer.<WebMessage>of(
						"<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
//								+ "  <vaadin-avatar img=\"${item.pictureUrl}\" name=\"${item.fullName}\"></vaadin-avatar>"
								+ "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
								+ "    <span> ${item.ecxAddr} </span>"
								+ "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
								+ "      ${item.partyDetails}" + "    </span>"
								+ "  </vaadin-vertical-layout>"
								+ "</vaadin-horizontal-layout>")
//				.withProperty("pictureUrl", Person::getPictureUrl)
				.withProperty("ecxAddr", msg -> ecxAddress.substring(0, ecxAddress.indexOf(";")))
				.withProperty("partyDetails", msg -> {
					final String[] split = ecxAddress.split(";");
					return split[1] + (split.length > 2 ? split[2] : "");
				});
	}

	private static String formatEcxAddress(DC5EcxAddress address) {
		return address.getEcxAddress() + System.lineSeparator()
				+ address.getParty().getPartyId()
				+ (address.getParty().getPartyIdType() != null
				? "(" + address.getParty().getPartyIdType() + ")"
				: "");
	}

}
