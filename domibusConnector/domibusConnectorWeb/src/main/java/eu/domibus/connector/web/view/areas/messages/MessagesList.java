package eu.domibus.connector.web.view.areas.messages;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.event.SortEvent;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import eu.domibus.connector.web.persistence.service.DomibusConnectorWebMessagePersistenceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.web.dto.WebMessage;
import eu.domibus.connector.web.service.WebMessageService;
import org.vaadin.klaudeta.PaginatedGrid;

@Component
@UIScope
public class MessagesList extends VerticalLayout implements AfterNavigationObserver {

	private static final Logger LOGGER = LogManager.getLogger(MessagesList.class);

	public static final int INITIAL_PAGE_SIZE = 20;

	//TODO: refactor this, both services are almost the same!
	private final WebMessageService messageService;
	private final DomibusConnectorWebMessagePersistenceService dcMessagePersistenceService;

	private PaginatedGrid<WebMessage> grid = new PaginatedGrid<>();
	private LinkedList<WebMessage> fullList = null;
	private Messages messagesView;

	
	TextField fromPartyIdFilterText = new TextField();
	TextField toPartyIdFilterText = new TextField();
	TextField serviceFilterText = new TextField();
	TextField actionFilterText = new TextField();
	TextField backendClientFilterText = new TextField();

	IntegerField pageSizeField = new IntegerField();

	int pageSize = INITIAL_PAGE_SIZE;
	Page<WebMessage> currentPage;
	WebMessage exampleWebMessage = new WebMessage();
	CallbackDataProvider<WebMessage, WebMessage> callbackDataProvider;

	public void setMessagesView(Messages messagesView) {
		this.messagesView = messagesView;
	}

	public MessagesList(WebMessageService messageService,
						DomibusConnectorWebMessagePersistenceService messagePersistenceService) {
		this.messageService = messageService;
		this.dcMessagePersistenceService = messagePersistenceService;


		grid.addComponentColumn(webMessage -> messagesView.geMessageDetailsLink(webMessage)).setHeader("Details").setWidth("30px");
		grid.addColumn(WebMessage::getConnectorMessageId).setHeader("Connector Message ID").setWidth("450px").setKey("connectorMessageId").setSortable(false);
		grid.addColumn(WebMessage::getFromPartyId).setHeader("From Party ID").setWidth("70px").setKey("fromPartyId").setSortable(true);
		grid.addColumn(WebMessage::getToPartyId).setHeader("To Party ID").setWidth("70px").setKey("toPartyId").setSortable(true);
		grid.addColumn(WebMessage::getService).setHeader("Service").setWidth("70px").setKey("service").setSortable(true);
		grid.addColumn(WebMessage::getAction)
				.setHeader("Action").setWidth("70px").setKey("action").setSortable(true);
		grid.addColumn(WebMessage::getCreated).setKey("created").setHeader("Created");
		grid.addColumn(WebMessage::getDeliveredToNationalSystem).setKey("deliveredToNationalSystem").setHeader("Delivered Backend");
		grid.addColumn(WebMessage::getDeliveredToGateway).setKey("deliveredToGateway").setHeader("Delivered Gateway");
		grid.addColumn(WebMessage::getBackendName).setKey("backendName").setHeader("Backend Client").setWidth("100px").setSortable(true);
		grid.setWidth("1800px");
		grid.setHeight("700px");

		grid.setMultiSort(true);
		grid.addSortListener(this::handleSortEvent);

		grid.setPageSize(pageSize);
		grid.setPaginatorSize(5);
		
		for(Column<WebMessage> col : grid.getColumns()) {
			col.setSortable(true);
			col.setResizable(true);
		}
		
		HorizontalLayout filtering = createFilterLayout();

		
		HorizontalLayout downloadLayout = createDownloadLayout();

		pageSizeField.setTitle("Display Messages");
		pageSizeField.setValue(pageSize);
		pageSizeField.setValueChangeMode(ValueChangeMode.LAZY);
		pageSizeField.addValueChangeListener(this::pageSizeChanged);

		VerticalLayout main = new VerticalLayout(pageSizeField, filtering, grid, downloadLayout);
		main.setAlignItems(Alignment.STRETCH);
		main.setHeight("700px");
		add(main);
		setHeight("100vh");
		setWidth("100vw");


		callbackDataProvider
				= new CallbackDataProvider<WebMessage, WebMessage>(this::fetchCallback, this::countCallback);

		grid.setDataProvider(callbackDataProvider);


//		try {
//			//any error here would prevent UI from being created!
//			callbackDataProvider.refreshAll();
//		} catch (Exception e) {
//			LOGGER.error("Exception while load messages from DB in MessagesView", e);
//		}

	}
	
//	private Button geMessageDetailsLink(WebMessage connectorMessage) {
//		return messagesView.geMessageDetailsLink(connectorMessage);
//	}

	private void handleSortEvent(SortEvent<Grid<WebMessage>, GridSortOrder<WebMessage>> gridGridSortOrderSortEvent) {
		gridGridSortOrderSortEvent.getSortOrder().stream()
				.map(webMessageGridSortOrder -> {
					SortDirection direction = webMessageGridSortOrder.getDirection();
					return direction;

				});
	}

	private void pageSizeChanged(AbstractField.ComponentValueChangeEvent<IntegerField, Integer> integerFieldIntegerComponentValueChangeEvent) {
		this.pageSize = integerFieldIntegerComponentValueChangeEvent.getValue();
		this.grid.setPageSize(pageSize);
	}


	private int countCallback(Query<WebMessage, WebMessage> webMessageWebMessageQuery) {
		return (int) dcMessagePersistenceService.count(createExample());
	}

	private Stream<WebMessage> fetchCallback(Query<WebMessage, WebMessage> webMessageWebMessageQuery) {
//		WebMessage exampleWebMessage = webMessageWebMessageQuery.getFilter().orElse(new WebMessage());

		int offset = webMessageWebMessageQuery.getOffset();
		int limit = webMessageWebMessageQuery.getLimit();


		//creating sort orders
//		List<Sort.Order> collect = webMessageWebMessageQuery
//				.getSortOrders()
//				.stream().map(querySortOrder ->
//						querySortOrder.getDirection() == SortDirection.ASCENDING ? Sort.Order.asc(querySortOrder.getSorted()) : Sort.Order.desc(querySortOrder.getSorted()))
//				.collect(Collectors.toList());
		List<Sort.Order> collect = grid.getSortOrder()
				.stream()
				.filter(sortOrder -> sortOrder.getSorted().getKey() != null)
				.map(sortOrder ->
						sortOrder.getDirection() == SortDirection.ASCENDING ? Sort.Order.asc(sortOrder.getSorted().getKey()) : Sort.Order.desc(sortOrder.getSorted().getKey()))
				.collect(Collectors.toList());
		Sort sort = Sort.by(collect.toArray(new Sort.Order[]{}));

		//creating page request with sort order and offset
		PageRequest pageRequest = PageRequest.of(offset / grid.getPageSize(), grid.getPageSize(), sort);
		Page<WebMessage> all = dcMessagePersistenceService.findAll(createExample(), pageRequest);

		this.currentPage = all;

		return all.stream();
	}

	private Example<WebMessage> createExample() {
		return Example.of(exampleWebMessage, ExampleMatcher.matchingAny()
				.withIgnoreCase()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
	}


	private HorizontalLayout createDownloadLayout() {
		Div downloadExcel = new Div();
		
		Button download = new Button();
		download.setIcon(new Image("frontend/images/xls.png", "XLS"));
		
		download.addClickListener(e -> {
		
			Element file = new Element("object");
			Element dummy = new Element("object");
			
			Input oName = new Input();
			
			String name = "MessagesList.xls";
			
			StreamResource resource = new StreamResource(name,() -> getMessagesListExcel());
			
			resource.setContentType("application/xls");
			
			file.setAttribute("data", resource);
			
			Anchor link = null;
			link = new Anchor(file.getAttribute("data"), "Download Document");
			
			UI.getCurrent().getElement().appendChild(oName.getElement(), file,
					dummy);
			oName.setVisible(false);
			file.setVisible(false);
			this.getUI().get().getPage().executeJavaScript("window.open('"+link.getHref()+"');");
		});
		
		downloadExcel.add(download);
		
		HorizontalLayout downloadLayout = new HorizontalLayout(
				downloadExcel
			    );
		downloadLayout.setWidth("100vw");
		
		return downloadLayout;
	}
	
	private InputStream getMessagesListExcel() {
		return messageService.generateExcel(fullList);
	}
	
	private HorizontalLayout createFilterLayout() {



		fromPartyIdFilterText.setPlaceholder("Filter by From Party ID");
		fromPartyIdFilterText.setWidth("180px");
		fromPartyIdFilterText.setValueChangeMode(ValueChangeMode.LAZY);
		fromPartyIdFilterText.addValueChangeListener(e -> filter());

		
		toPartyIdFilterText.setPlaceholder("Filter by To Party ID");
		toPartyIdFilterText.setWidth("180px");
		toPartyIdFilterText.setValueChangeMode(ValueChangeMode.LAZY);
		toPartyIdFilterText.addValueChangeListener(e -> filter());
		
		
		serviceFilterText.setPlaceholder("Filter by Service");
		serviceFilterText.setWidth("180px");
		serviceFilterText.setValueChangeMode(ValueChangeMode.LAZY);
		serviceFilterText.addValueChangeListener(e -> filter());
		
		
		actionFilterText.setPlaceholder("Filter by Action");
		actionFilterText.setWidth("180px");
		actionFilterText.setValueChangeMode(ValueChangeMode.LAZY);
		actionFilterText.addValueChangeListener(e -> filter());
		
		
		backendClientFilterText.setPlaceholder("Filter by backend");
		backendClientFilterText.setWidth("180px");
		backendClientFilterText.setValueChangeMode(ValueChangeMode.LAZY);
		backendClientFilterText.addValueChangeListener(e -> filter());
		
		Button clearAllFilterTextBtn = new Button(
				new Icon(VaadinIcon.CLOSE_CIRCLE));
		clearAllFilterTextBtn.setText("Clear Filter");
		clearAllFilterTextBtn.addClickListener(e -> {
			fromPartyIdFilterText.clear();
			toPartyIdFilterText.clear();
			serviceFilterText.clear();
			actionFilterText.clear();
			backendClientFilterText.clear();
			filter();
		});
		
		Button refreshListBtn = new Button(new Icon(VaadinIcon.REFRESH));
		refreshListBtn.setText("RefreshList");
		refreshListBtn.addClickListener(e -> {filter();});
		
		HorizontalLayout filtering = new HorizontalLayout(
				fromPartyIdFilterText,
				toPartyIdFilterText,
				serviceFilterText,
//				actionFilterText, //TODO: currently not working!
				backendClientFilterText,
				clearAllFilterTextBtn,
				refreshListBtn
			    );
		filtering.setWidth("100vw");
		
		return filtering;
	}
	
	private void filter() {

		exampleWebMessage.setAction(getTxt(actionFilterText));
		exampleWebMessage.setFromPartyId(getTxt(fromPartyIdFilterText));
		exampleWebMessage.setToPartyId(getTxt(toPartyIdFilterText));
		exampleWebMessage.setService(getTxt(serviceFilterText));
		exampleWebMessage.setBackendName(getTxt(backendClientFilterText));

		callbackDataProvider.refreshAll();

	}

	@Nullable
	private String getTxt(TextField field) {
		String txt = null;
		if (!field.getValue().isEmpty()) {
			txt = field.getValue();
		}
		return txt;
	}

	public void reloadList() {
		callbackDataProvider.refreshAll();
	}


	@Override
	public void afterNavigation(AfterNavigationEvent event) {

	}


}
