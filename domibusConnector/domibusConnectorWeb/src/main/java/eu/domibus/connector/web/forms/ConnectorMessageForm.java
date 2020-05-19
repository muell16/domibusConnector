package eu.domibus.connector.web.forms;

import java.util.Date;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import eu.domibus.connector.web.dto.WebMessage;
import eu.domibus.connector.web.dto.WebMessageDetail;
import eu.domibus.connector.web.dto.WebMessageEvidence;
import eu.domibus.connector.web.view.areas.messages.MessageDetails;

//@HtmlImport("styles/shared-styles.html")
public class ConnectorMessageForm extends FormLayout {

	private TextField connectorMessageId = FormsUtil.getFormattedTextFieldReadOnly();
	private TextField backendMessageId = FormsUtil.getFormattedTextFieldReadOnly();
	private TextField ebmsMessageId = FormsUtil.getFormattedTextFieldReadOnly();
	private TextField conversationId = FormsUtil.getFormattedTextFieldReadOnly();
	private TextField originalSender = FormsUtil.getFormattedTextFieldReadOnly();
	private TextField finalRecipient = FormsUtil.getFormattedTextFieldReadOnly();
	private TextField service = FormsUtil.getFormattedTextFieldReadOnly();
	private TextField action = FormsUtil.getFormattedTextFieldReadOnly();
	private TextField fromPartyId = FormsUtil.getFormattedTextFieldReadOnly();
	private TextField toPartyId = FormsUtil.getFormattedTextFieldReadOnly();
	private TextField backendClient = FormsUtil.getFormattedTextFieldReadOnly();
	private TextField direction = FormsUtil.getFormattedTextFieldReadOnly();
	private TextField deliveredToBackendString = FormsUtil.getFormattedTextFieldReadOnly();
	private TextField deliveredToGatewayString = FormsUtil.getFormattedTextFieldReadOnly();
	private TextField confirmedString = FormsUtil.getFormattedTextFieldReadOnly();
	private TextField rejectedString = FormsUtil.getFormattedTextFieldReadOnly();
	private TextField createdString = FormsUtil.getFormattedTextFieldReadOnly();
	
	private Binder<WebMessageDetail> binder = new Binder<>(WebMessageDetail.class);
	
	public ConnectorMessageForm() {
		fillForm();
//		setResponsiveSteps(new ResponsiveStep("500px", 2, LabelsPosition.TOP));
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

	public void setConnectorMessage(WebMessageDetail message) {
		this.removeAll();
		fillForm();
		binder.setBean(message);
	}
	
	public String getConnectorMessageId() {
		return this.connectorMessageId.getValue();
	}

}
