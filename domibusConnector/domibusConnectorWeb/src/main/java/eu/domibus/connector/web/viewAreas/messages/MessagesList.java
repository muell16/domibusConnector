package eu.domibus.connector.web.viewAreas.messages;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
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
import eu.domibus.connector.web.dto.WebReport;
import eu.domibus.connector.web.service.WebMessageService;

@HtmlImport("styles/shared-styles.html")
//@StyleSheet("styles/grid.css")
@Component
@UIScope
public class MessagesList extends VerticalLayout {

	private static final Logger LOGGER = LogManager.getLogger(MessagesList.class);

	private Grid<WebMessage> grid = new Grid<>();
	private LinkedList<WebMessage> fullList = null;
	private Messages messagesView;
	private WebMessageService messageService;
	
	TextField fromPartyIdFilterText = new TextField();
	TextField toPartyIdFilterText = new TextField();
	TextField serviceFilterText = new TextField();
	TextField actionFilterText = new TextField();
	TextField backendClientFilterText = new TextField();
	
	public void setMessagesView(Messages messagesView) {
		this.messagesView = messagesView;
	}

	public MessagesList(@Autowired WebMessageService messageService) {
		this.messageService = messageService;

		fullList = new LinkedList<>();

		grid.setItems(fullList);
		grid.addComponentColumn(webMessage -> getDetailsLink(webMessage.getConnectorMessageId())).setHeader("Details").setWidth("30px");
		grid.addColumn(WebMessage::getConnectorMessageId).setHeader("Connector Message ID").setWidth("450px");
		grid.addColumn(WebMessage::getFromPartyId).setHeader("From Party ID").setWidth("70px");
		grid.addColumn(WebMessage::getToPartyId).setHeader("To Party ID").setWidth("70px");
		grid.addColumn(WebMessage::getService).setHeader("Service").setWidth("70px");
		grid.addColumn(WebMessage::getAction).setHeader("Action").setWidth("70px");
		grid.addColumn(WebMessage::getCreated).setHeader("Created");
		grid.addColumn(WebMessage::getDeliveredToBackend).setHeader("Delivered Backend");
		grid.addColumn(WebMessage::getDeliveredToGateway).setHeader("Delivered Gateway");
		grid.addColumn(WebMessage::getBackendClient).setHeader("Backend Client").setWidth("100px");
		grid.setWidth("1800px");
		grid.setHeight("700px");
		grid.setMultiSort(true);
		
		for(Column<WebMessage> col : grid.getColumns()) {
			col.setSortable(true);
			col.setResizable(true);
		}
		
		HorizontalLayout filtering = createFilterLayout();
		
		HorizontalLayout downloadLayout = createDownloadLayout();
			
		VerticalLayout main = new VerticalLayout(filtering, grid, downloadLayout);
		main.setAlignItems(Alignment.STRETCH);
		main.setHeight("700px");
		add(main);
		setHeight("100vh");
		setWidth("100vw");

		try {
			reloadList();
		} catch (Exception e) {
			LOGGER.error("Exception occured during init UI component MessagesList", e);
		}
		
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
		fromPartyIdFilterText.setValueChangeMode(ValueChangeMode.EAGER);
		fromPartyIdFilterText.addValueChangeListener(e -> filter());

		
		toPartyIdFilterText.setPlaceholder("Filter by To Party ID");
		toPartyIdFilterText.setWidth("180px");
		toPartyIdFilterText.setValueChangeMode(ValueChangeMode.EAGER);
		toPartyIdFilterText.addValueChangeListener(e -> filter());
		
		
		serviceFilterText.setPlaceholder("Filter by Service");
		serviceFilterText.setWidth("180px");
		serviceFilterText.setValueChangeMode(ValueChangeMode.EAGER);
		serviceFilterText.addValueChangeListener(e -> filter());
		
		
		actionFilterText.setPlaceholder("Filter by Action");
		actionFilterText.setWidth("180px");
		actionFilterText.setValueChangeMode(ValueChangeMode.EAGER);
		actionFilterText.addValueChangeListener(e -> filter());
		
		
		backendClientFilterText.setPlaceholder("Filter by backend");
		backendClientFilterText.setWidth("180px");
		backendClientFilterText.setValueChangeMode(ValueChangeMode.EAGER);
		backendClientFilterText.addValueChangeListener(e -> filter());
		
		Button clearAllFilterTextBtn = new Button(
				new Icon(VaadinIcon.CLOSE_CIRCLE));
		clearAllFilterTextBtn.setText("Clear Filter");
		clearAllFilterTextBtn.addClickListener(e -> {
			fromPartyIdFilterText.clear();
			toPartyIdFilterText.clear();
			serviceFilterText.clear();
			actionFilterText.clear();
			backendClientFilterText.clear();});
		
		HorizontalLayout filtering = new HorizontalLayout(
				fromPartyIdFilterText,
				toPartyIdFilterText,
				serviceFilterText,
				actionFilterText,
				backendClientFilterText,
				clearAllFilterTextBtn
			    );
		filtering.setWidth("100vw");
		
		return filtering;
	}
	
	private void filter() {
		LinkedList<WebMessage> target = new LinkedList<WebMessage>();
		for(WebMessage msg : fullList) {
			if((fromPartyIdFilterText.getValue().isEmpty() || msg.getFromPartyId()!=null && msg.getFromPartyId().toUpperCase().contains(fromPartyIdFilterText.getValue().toUpperCase()))
				&& (toPartyIdFilterText.getValue().isEmpty() || msg.getToPartyId()!=null && msg.getToPartyId().toUpperCase().contains(toPartyIdFilterText.getValue().toUpperCase()))
				&& (serviceFilterText.getValue().isEmpty() || msg.getService()!=null && msg.getService().toUpperCase().contains(serviceFilterText.getValue().toUpperCase()))
				&& (actionFilterText.getValue().isEmpty() || msg.getAction()!=null && msg.getAction().toUpperCase().contains(actionFilterText.getValue().toUpperCase()))
				&& (backendClientFilterText.getValue().isEmpty() || msg.getBackendClient()!=null && msg.getBackendClient().toUpperCase().contains(backendClientFilterText.getValue().toUpperCase()))) {
				target.addLast(msg);
			}
		}
		
		grid.setItems(target);
	}
	
	private Button getDetailsLink(String connectorMessageId) {
		Button getDetails = new Button(new Icon(VaadinIcon.SEARCH));
		getDetails.addClickListener(e -> showConnectorMessage(connectorMessageId));
		return getDetails;
	}
	
	private void showConnectorMessage(String connectorMessageId) {
		messagesView.showMessageDetails(connectorMessageId);
	}

	public void reloadList() {
		grid.setItems(messageService.getInitialList());
	}
	
	
	
}
