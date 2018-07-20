package eu.domibus.connector.web.viewAreas;

import java.util.LinkedList;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.web.dto.WebMessage;
import eu.domibus.connector.web.service.WebMessageService;

@HtmlImport("styles/shared-styles.html")
@StyleSheet("styles/grid.css")
@Component
//@Scope(scopeName=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@UIScope
public class Messages extends VerticalLayout implements InitializingBean {
	
	private Grid<WebMessage> grid = new Grid<>();
//	private TextField filterText = new TextField();
	private TextField searchText = new TextField();
	private LinkedList<WebMessage> fullList = null;
	
	private WebMessageService messageService;
	
	@Autowired
	public void setMessageService(WebMessageService messageService) {
		this.messageService = messageService;
	}

	public Messages() {
//		Button clearFilterTextBtn = new Button(
//		        new Icon(VaadinIcon.CLOSE_CIRCLE));
//		clearFilterTextBtn.addClickListener(e -> filterText.clear());
		
//		fullList = messageService.getInitialList();
		
//		filterText.setPlaceholder("Filter by ERV Message");
//		filterText.setValueChangeMode(ValueChangeMode.EAGER);
//		filterText.addValueChangeListener(e -> updateListErvMsg(fullList,filterText.getValue()));
//		
//		searchText.setPlaceholder("Search for Message");
//		searchText.getStyle().set("margin-left", "300px");
//		Button searchBtn = new Button(new Icon(VaadinIcon.SEARCH));
//		searchBtn.addClickListener(e -> searchMessageId(searchText.getValue()));
//		
//		Button resetSearchBtn = new Button(new Icon(VaadinIcon.RECYCLE));
//		resetSearchBtn.addClickListener(e -> reloadList());
		
		
//		grid.setItems(fullList);
////		grid.addComponentColumn(baseMessageERV -> baseMessageERV.getDetailsLink(conf)).setHeader("Details").setWidth("30px");
//		//grid.addColumn(BaseMessageERV::getId).setHeader("ID");
//		grid.addColumn(WebMessage::getConnectorMessageId).setHeader("Connector Message ID").setWidth("500px");
//		//grid.addColumn(BaseMessageERV::getQualitaet).setHeader("Qualität");
//		grid.addColumn(WebMessage::getFromPartyId).setHeader("From Party ID").setWidth("70px");
//		grid.addColumn(WebMessage::getToPartyId).setHeader("To Party ID").setWidth("70px");
//		grid.addColumn(WebMessage::getService).setHeader("Service").setWidth("70px");
//		grid.addColumn(WebMessage::getAction).setHeader("Action").setWidth("70px");
//		grid.addColumn(WebMessage::getCreated).setHeader("Created");
//		grid.addColumn(WebMessage::getDeliveredToBackend).setHeader("Delivered Backend");
//		grid.addColumn(WebMessage::getDeliveredToGateway).setHeader("Delivered Gateway");
//		grid.addColumn(WebMessage::getBackendClient).setHeader("Backend Client").setWidth("70px");
//		grid.setWidth("1800px");
//		grid.setHeight("100%");
//		grid.setMultiSort(true);
//		
//		for(Column<WebMessage> col : grid.getColumns()) {
//			col.setSortable(true);
//			col.setResizable(true);
//		}


		
//		HorizontalLayout filtering = new HorizontalLayout(filterText,
//			    clearFilterTextBtn,searchText,searchBtn,resetSearchBtn);
		
			
//		VerticalLayout main = new VerticalLayout(filtering,grid);
//		main.setAlignItems(Alignment.STRETCH);
//		main.setSizeFull();
//		add(main);
//		setHeight("100vh");
//		setWidth("100vh");
		//updateList();
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		fullList = messageService.getInitialList();
		
		grid.setItems(fullList);
//		grid.addComponentColumn(baseMessageERV -> baseMessageERV.getDetailsLink(conf)).setHeader("Details").setWidth("30px");
		//grid.addColumn(BaseMessageERV::getId).setHeader("ID");
		grid.addColumn(WebMessage::getConnectorMessageId).setHeader("Connector Message ID").setWidth("500px");
		//grid.addColumn(BaseMessageERV::getQualitaet).setHeader("Qualität");
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

	}

//	public void reloadList() {
//		searchText.clear();
//		grid.setItems(config.getERV().getInitList());
//	}
	
//	public void updateListErvMsg(LinkedList<BaseMessageERV> fullList,String searchValue) {
//		LinkedList<BaseMessageERV> target = new LinkedList<BaseMessageERV>();
//		for(BaseMessageERV msg : fullList) {
//			if(msg.getNachrichtid().toUpperCase().contains(searchValue.toUpperCase())) {
//				target.addLast(msg);
//			}
//		}
//		
//		grid.setItems(target);
//	}
	
//	public void searchMessageId(String msgId) {
//		LinkedList<BaseMessageERV> target = config.getERV().search(msgId);
//		grid.setItems(target);
//	}

}
