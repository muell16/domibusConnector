package eu.domibus.connector.web.forms;

import java.util.Date;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import eu.domibus.connector.web.dto.WebMessage;
import eu.domibus.connector.web.viewAreas.messages.MessageDetails;

@HtmlImport("styles/shared-styles.html")
public class ConnectorMessageForm extends FormLayout {

	private TextField connectorMessageId = getFormattedTextField();
	private TextField backendMessageId = getFormattedTextField();
	private TextField ebmsMessageId = getFormattedTextField();
	private TextField conversationId = getFormattedTextField();
	private TextField originalSender = getFormattedTextField();
	private TextField finalRecipient = getFormattedTextField();
	private TextField service = getFormattedTextField();
	private TextField action = getFormattedTextField();
	private TextField fromPartyId = getFormattedTextField();
	private TextField toPartyId = getFormattedTextField();
	private TextField backendClient = getFormattedTextField();
	private TextField direction = getFormattedTextField();
	private TextField deliveredToBackendString = getFormattedTextField();
	private TextField deliveredToGatewayString = getFormattedTextField();
	private TextField confirmedString = getFormattedTextField();
	private TextField rejectedString = getFormattedTextField();
	private TextField createdString = getFormattedTextField();
	
	private WebMessage message = null;

	private Binder<WebMessage> binder = new Binder<>(WebMessage.class);
	
	public ConnectorMessageForm() {
		fillForm();
	}
	
	private void fillForm() {
		binder.bindInstanceFields(this);
		
		addFormItem(connectorMessageId, "Connector Message ID"); 
		addFormItem(backendMessageId, "Backend Message ID"); 
		addFormItem(ebmsMessageId, "EBMS Message ID"); 
		addFormItem(conversationId, "Conversation ID"); 
		addFormItem(originalSender, "Original Sender"); 
		addFormItem(finalRecipient, "Final Recipient"); 
		addFormItem(service, "Service"); 
		addFormItem(action, "Action"); 
		addFormItem(fromPartyId, "From Party ID"); 
		addFormItem(toPartyId, "To Party ID"); 
		addFormItem(backendClient, "Backend Client Name"); 
		addFormItem(direction, "Direction"); 
		addFormItem(deliveredToBackendString, "Delivered to Backend at"); 
		addFormItem(deliveredToGatewayString, "Delivered to Gateway at"); 
		addFormItem(confirmedString, "Confirmed at"); 
		addFormItem(rejectedString, "Rejected at"); 
		addFormItem(createdString, "Message created at"); 
	}
	
	private TextField getFormattedTextField() {
		TextField loc = new TextField();
//		loc.setEnabled(false);
		loc.setReadOnly(true);
		loc.getStyle().set("fontSize", "13px");
		loc.setWidth("600px");
		return loc;
	}

	private TextArea getFormattedTextArea() {
		TextArea loc = new TextArea();
		loc.getStyle().set("fontSize", "13px");
		loc.setWidth("600px");
		return loc;
	}


	public void setConnectorMessage(WebMessage message, MessageDetails messageDetails) {
		this.removeAll();
		fillForm();
		this.message = message;
		binder.setBean(message);
	}

}
