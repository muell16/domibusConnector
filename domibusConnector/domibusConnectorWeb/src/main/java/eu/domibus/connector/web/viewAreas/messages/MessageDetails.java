package eu.domibus.connector.web.viewAreas.messages;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.web.dto.WebMessage;
import eu.domibus.connector.web.forms.ConnectorMessageForm;
import eu.domibus.connector.web.service.WebMessageService;

@HtmlImport("styles/shared-styles.html")
@StyleSheet("styles/grid.css")
@Component
@UIScope
public class MessageDetails extends VerticalLayout {
	
	private WebMessageService messageService;
	private ConnectorMessageForm form = new ConnectorMessageForm();

	public MessageDetails(@Autowired WebMessageService messageService) {
		
		this.messageService = messageService;

		form.getStyle().set("margin-top","25px");
		
		add(form);
		//setAlignItems(Alignment.START);
		form.setEnabled(true);
		setSizeFull();
		setHeight("100vh");
		setWidth("100vw");
		
	}

	
	public void loadMessageDetails(String connectorMessageId) {
		WebMessage messageByConnectorId = messageService.getMessageByConnectorId(connectorMessageId);
		form.setConnectorMessage(messageByConnectorId, this);
		
	}
	
}
