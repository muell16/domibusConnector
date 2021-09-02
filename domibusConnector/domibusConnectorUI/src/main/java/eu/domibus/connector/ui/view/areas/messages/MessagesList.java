package eu.domibus.connector.ui.view.areas.messages;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.ui.component.LumoCheckbox;
import eu.domibus.connector.ui.component.LumoLabel;
import eu.domibus.connector.ui.dto.WebMessage;
import eu.domibus.connector.ui.dto.WebMessageDetail;
import eu.domibus.connector.ui.persistence.service.DomibusConnectorWebMessagePersistenceService;
import eu.domibus.connector.ui.service.WebMessageService;
import eu.domibus.connector.ui.view.areas.configuration.TabMetadata;

import org.springframework.core.annotation.Order;
import org.springframework.data.domain.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@UIScope
@Route(value = MessagesList.ROUTE, layout = MessageLayout.class)
@Order(1)
@TabMetadata(title = "All Messages", tabGroup = MessageLayout.TAB_GROUP_NAME)
public class MessagesList extends VerticalLayout implements AfterNavigationObserver {

	public static final String ROUTE = "messagelist";

	public static final int INITIAL_PAGE_SIZE = 20;

	//TODO: refactor this, both services are almost the same!
	private final WebMessageService messageService;
	private final DomibusConnectorWebMessagePersistenceService dcMessagePersistenceService;

	private final MessageDetails details;

	private WebMessagesGrid grid;
	private LinkedList<WebMessage> fullList = null;

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

	public MessagesList(WebMessageService messageService, DomibusConnectorWebMessagePersistenceService messagePersistenceService, MessageDetails details) {
		this.messageService = messageService;
		this.dcMessagePersistenceService = messagePersistenceService;
		this.details = details;
	}

	@PostConstruct
	void init() {

		grid = new WebMessagesGrid(details);

		grid.setPageSize(pageSize);
		grid.setPaginatorSize(5);

//		HorizontalLayout filtering = createFilterLayout();
//		HorizontalLayout downloadLayout = createDownloadLayout();

		VerticalLayout gridControl = createGridControlLayout();

		VerticalLayout main = new VerticalLayout(gridControl,
//				filtering,
				grid
//				downloadLayout
		);
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

	private VerticalLayout createGridControlLayout() {
		VerticalLayout gridControl = new VerticalLayout();
		
		LumoLabel pageSizeLabel = new LumoLabel("Messages displayed per page:");
		gridControl.add(pageSizeLabel);
		pageSizeField.setTitle("Display Messages");
		pageSizeField.setValue(pageSize);
		pageSizeField.setValueChangeMode(ValueChangeMode.LAZY);
		pageSizeField.addValueChangeListener(this::pageSizeChanged);
		gridControl.add(pageSizeField);
		
		Button hideColsBtn = new Button();
		hideColsBtn.setText("Show/Hide Columns");
		hideColsBtn.addClickListener(e -> {
			Dialog hideableColsDialog = new Dialog();
			
			Div headerContent = new Div();
			Label header = new Label("Select columns you want to see in the list");
			header.getStyle().set("font-weight", "bold");
			header.getStyle().set("font-style", "italic");
			headerContent.getStyle().set("text-align", "center");
			headerContent.getStyle().set("padding", "10px");
			headerContent.add(header);
			hideableColsDialog.add(headerContent);
			
			for(String colName: grid.getHideableColumnNames()) {
				LumoCheckbox hideableCol = new LumoCheckbox(colName);
				hideableCol.setValue(grid.getHideableColumns().get(colName).isVisible());
				hideableCol.addValueChangeListener(e1 -> {
					grid.getHideableColumns().get(colName).setVisible(e1.getValue());
				});
				hideableColsDialog.add(hideableCol);
			}
			
			Button closeBtn = new Button("close");
			closeBtn.addClickListener(e2 -> hideableColsDialog.close());
			
			hideableColsDialog.add(closeBtn);
			
			hideableColsDialog.open();

		});
		
		gridControl.add(hideColsBtn);

		return gridControl;
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

//		List<WebMessage> messages = all.getContent();
//		if(!CollectionUtils.isEmpty(messages)) {
//			for(WebMessage m:messages) {
//				LOGGER.debug(m.toString());
//			}
//		}
		
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

		exampleWebMessage.getMessageInfo().setAction(new WebMessageDetail.Action(getTxt(actionFilterText)));
		exampleWebMessage.getMessageInfo().setFrom(new WebMessageDetail.Party(getTxt(fromPartyIdFilterText)));
		exampleWebMessage.getMessageInfo().setTo(new WebMessageDetail.Party(getTxt(toPartyIdFilterText)));
		exampleWebMessage.getMessageInfo().setService(new WebMessageDetail.Service(getTxt(serviceFilterText)));
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