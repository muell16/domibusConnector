package eu.domibus.connector.ui.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.event.SortEvent;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.function.ValueProvider;
import eu.domibus.connector.ui.dto.WebMessage;
import eu.domibus.connector.ui.persistence.service.impl.DomibusConnectorWebMessagePersistenceService;
import eu.domibus.connector.ui.view.areas.messages.MessageDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WebMessagesGrid extends Grid<WebMessage> {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebMessagesGrid.class);
	
	private Map<String,Column<WebMessage>> hideableColumns = new HashMap<>();
	
	private final MessageDetails details;
	private final DomibusConnectorWebMessagePersistenceService dcMessagePersistenceService;
	
	WebMessage exampleWebMessage;
	
	Page<WebMessage> currentPage;
	
	CallbackDataProvider<WebMessage, WebMessage> callbackDataProvider;

	public WebMessagesGrid(MessageDetails details, DomibusConnectorWebMessagePersistenceService dcMessagePersistenceService) {
		super();
		this.details = details;
		this.dcMessagePersistenceService = dcMessagePersistenceService;
		addAllColumns();
		
		for(Column<WebMessage> col : getColumns()) {
			col.setResizable(true);
		}
		
		setMultiSort(false);
		setWidth("100%");
		addSortListener(this::handleSortEvent);
	}

	private void addAllColumns(){
		addComponentColumn(webMessage -> geMessageDetailsLink(webMessage)).setHeader("Details").setWidth("30px");
		
		addColumn(
				webMessage -> webMessage.getMessageInfo()!=null && webMessage.getMessageInfo().getFrom()!=null?webMessage.getMessageInfo().getFrom().getPartyString():"")
				.setHeader("From Party").setWidth("70px").setKey("messageInfo.from.partyId").setSortable(true);
		addColumn(
				webMessage -> webMessage.getMessageInfo()!=null && webMessage.getMessageInfo().getTo()!=null?webMessage.getMessageInfo().getTo().getPartyString():"")
				.setHeader("To Party").setWidth("70px").setKey("messageInfo.to.partyId").setSortable(true);

		addHideableColumn(WebMessage::getConnectorMessageId, "Connector Message ID", "450px", "connectorMessageId", false, true);
		addHideableColumn(WebMessage::getEbmsId, "ebMS Message ID", "450px", "ebmsMessageId", false, true);
		addHideableColumn(WebMessage::getBackendMessageId, "Backend Message ID", "450px", "backendMessageId", false, true);
		addHideableColumn(WebMessage::getConversationId, "Conversation ID", "450px", "conversationId", false, false);
		addHideableColumn(
				webMessage -> webMessage.getMessageInfo()!=null?webMessage.getMessageInfo().getOriginalSender():""
				, "Original sender", "300px", "messageInfo.originalSender", true, false);
		addHideableColumn(
				webMessage -> webMessage.getMessageInfo()!=null?webMessage.getMessageInfo().getFinalRecipient():""
				, "Final recipient", "300px", "messageInfo.finalRecipient", true, false);
		addHideableColumn(
				webMessage -> webMessage.getMessageInfo()!=null && webMessage.getMessageInfo().getService()!=null?webMessage.getMessageInfo().getService().getServiceString():""
				, "Service", "150px", "messageInfo.service.service", true, true);
		addHideableColumn(
				webMessage -> webMessage.getMessageInfo()!=null && webMessage.getMessageInfo().getAction()!=null?webMessage.getMessageInfo().getAction().getAction():""
				, "Action", "150px", "messageInfo.action.action", true, true);
		addHideableColumn(WebMessage::getBackendName
				, "backend name", "150px", "backendName", true, true);
		addHideableColumn(msg-> msg.getMsgDirection().toString(), "direction", "150px", "direction", false, false);
		addHideableColumn(WebMessage::getDeliveredToNationalSystem, "delivered backend", "300px", "deiliveredToNationalSystem", true, false);
		addHideableColumn(WebMessage::getDeliveredToGateway, "delivered gateway", "300px", "deliveredToGateway", true, false);
		addHideableColumn(WebMessage::getCreated, "created", "300px", "created", true, false);
		addHideableColumn(WebMessage::getConfirmed, "confirmed", "300px", "confirmed", true, false);
		addHideableColumn(WebMessage::getRejected, "rejected", "300px", "rejected", true, false);
		addHideableColumn(WebMessage::getMsgContentState, "contentState", "300px", "contentState", true, true);

	}

	
	private void addHideableColumn(ValueProvider<WebMessage, ?> valueProvider, String header, String width, String key, boolean sortable, boolean visible){
		Column<WebMessage> column = addColumn(valueProvider).setHeader(header).setKey(key).setWidth(width).setSortable(sortable);
		column.setVisible(visible);
		hideableColumns.put(header, column);
	}
	
	public Map<String, Column<WebMessage>> getHideableColumns() {
		return hideableColumns;
	}
	
	public Set<String> getHideableColumnNames() {
		return hideableColumns.keySet();
	}

	public Button geMessageDetailsLink(WebMessage connectorMessage) {
		Button getDetails = new Button(new Icon(VaadinIcon.SEARCH));
		getDetails.addClickListener(e -> details.show(connectorMessage));
		return getDetails;
	}
	
	private void handleSortEvent(SortEvent<Grid<WebMessage>, GridSortOrder<WebMessage>> gridGridSortOrderSortEvent) {
		gridGridSortOrderSortEvent.getSortOrder().stream()
				.map(webMessageGridSortOrder -> {
					SortDirection direction = webMessageGridSortOrder.getDirection();
					return direction;

				});
	}
//
//	private Example<WebMessage> createExample() {
//		return Example.of(exampleWebMessage, ExampleMatcher.matchingAny()
//				.withIgnoreCase()
//				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
//	}


//	private Stream<WebMessage> fetchCallback(Query<WebMessage, WebMessage> webMessageWebMessageQuery) {
//		LOGGER.debug("Call fetchCallback");
//		int offset = webMessageWebMessageQuery.getOffset();
//		LOGGER.debug("Offset: "+offset);
//		List<Sort.Order> collect = getSortOrder()
//				.stream()
//				.filter(sortOrder -> sortOrder.getSorted().getKey() != null)
//				.map(sortOrder ->
//						sortOrder.getDirection() == SortDirection.ASCENDING ? Sort.Order.asc(sortOrder.getSorted().getKey()) : Sort.Order.desc(sortOrder.getSorted().getKey()))
//				.collect(Collectors.toList());
//		if (collect.isEmpty()) {
//			collect.add(Sort.Order.desc("created")); //set default sort order if none selected
//		}
//		Sort sort = Sort.by(collect.toArray(new Sort.Order[]{}));
//
//		//creating page request with sort order and offset
//		PageRequest pageRequest = PageRequest.of(offset / getPageSize(), getPageSize(), sort);
//		LOGGER.debug("PageRequest: "+pageRequest.toString());
//		Page<WebMessage> all = dcMessagePersistenceService.findAll(createExample(), pageRequest);
//		LOGGER.debug("Page requested size: "+all.getSize());
//
//		this.currentPage = all;
//
//		return all.stream();
//	}
	
	public void reloadList() {
		LOGGER.debug("#reloadList");
		this.setItems(dcMessagePersistenceService.findAll().stream());
	}
//
//	public void setExampleWebMessage(WebMessage example) {
//		this.exampleWebMessage = example;
//	}
//
//	@Override
//	public void afterNavigation(AfterNavigationEvent event) {
//		LOGGER.debug("#afterNavigation: Create and set callbackDataProvider");
//		callbackDataProvider
//			= getCallbackDataProvider();
//		setDataProvider(callbackDataProvider);
//	}
//
//	private CallbackDataProvider<WebMessage, WebMessage> getCallbackDataProvider() {
//		if (callbackDataProvider == null) {
//			callbackDataProvider = new CallbackDataProvider<WebMessage, WebMessage>(this::fetchCallback, this::countCallback);
//		}
//		return callbackDataProvider;
//	}
}
