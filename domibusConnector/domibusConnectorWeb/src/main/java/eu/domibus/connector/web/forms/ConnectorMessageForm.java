package eu.domibus.connector.web.forms;

import java.util.Date;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import eu.domibus.connector.web.dto.WebMessage;
import eu.domibus.connector.web.dto.WebMessageDetail;
import eu.domibus.connector.web.dto.WebMessageEvidence;
import eu.domibus.connector.web.viewAreas.messages.MessageDetails;

@HtmlImport("styles/shared-styles.html")
public class ConnectorMessageForm extends FormLayout {

	private TextField connectorMessageId = FormsUtil.getFormattedTextField();
	private TextField backendMessageId = FormsUtil.getFormattedTextField();
	private TextField ebmsMessageId = FormsUtil.getFormattedTextField();
	private TextField conversationId = FormsUtil.getFormattedTextField();
	private TextField originalSender = FormsUtil.getFormattedTextField();
	private TextField finalRecipient = FormsUtil.getFormattedTextField();
	private TextField service = FormsUtil.getFormattedTextField();
	private TextField action = FormsUtil.getFormattedTextField();
	private TextField fromPartyId = FormsUtil.getFormattedTextField();
	private TextField toPartyId = FormsUtil.getFormattedTextField();
	private TextField backendClient = FormsUtil.getFormattedTextField();
	private TextField direction = FormsUtil.getFormattedTextField();
	private TextField deliveredToBackendString = FormsUtil.getFormattedTextField();
	private TextField deliveredToGatewayString = FormsUtil.getFormattedTextField();
	private TextField confirmedString = FormsUtil.getFormattedTextField();
	private TextField rejectedString = FormsUtil.getFormattedTextField();
	private TextField createdString = FormsUtil.getFormattedTextField();
	
	private WebMessageDetail message = null;

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

	public void setConnectorMessage(WebMessageDetail message, MessageDetails messageDetails) {
		this.removeAll();
		fillForm();
		this.message = message;
		binder.setBean(message);
		
		
	}

}
