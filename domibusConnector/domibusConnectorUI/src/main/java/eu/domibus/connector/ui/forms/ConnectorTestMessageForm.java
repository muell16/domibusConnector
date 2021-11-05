package eu.domibus.connector.ui.forms;

import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;

import eu.domibus.connector.domain.model.DomibusConnectorParty;
import eu.domibus.connector.domain.model.DomibusConnectorParty.PartyRoleType;
import eu.domibus.connector.ui.dto.WebMessage;
import eu.domibus.connector.ui.dto.WebMessageDetail;
import eu.domibus.connector.ui.dto.WebMessageDetail.Party;
import eu.domibus.connector.ui.dto.WebMessageDetail.Service;

public class ConnectorTestMessageForm extends FormLayout {
	
	private TextField conversationId = FormsUtil.getFormattedTextField();
	private TextField originalSender = FormsUtil.getFormattedRequiredTextField();
	private TextField finalRecipient = FormsUtil.getFormattedRequiredTextField();
	private ComboBox<WebMessageDetail.Party> toParty = (ComboBox<WebMessageDetail.Party>) FormsUtil.getRequiredCombobox();
	private TextField service = FormsUtil.getFormattedTextFieldReadOnly();
	private TextField action = FormsUtil.getFormattedTextFieldReadOnly();
	private TextField fromParty = FormsUtil.getFormattedTextFieldReadOnly();
	
	private Binder<WebMessage> binder = new Binder<>(WebMessage.class);

	public ConnectorTestMessageForm() {
		fillForm();
	}

	private void fillForm() {
		binder.bindInstanceFields(this);
		
		addFormItem(conversationId, "Conversation ID");
		binder.forField(originalSender).withValidator((Validator<String>) (value, context) -> {
			if (value.length() < 1) {
                return ValidationResult
                        .error("Original Sender must not be empty!");
            }
			return ValidationResult.ok();
		}).bind(
				webMessage -> webMessage.getMessageInfo()!=null?webMessage.getMessageInfo().getOriginalSender():"",
				(webMessage,originalSender) -> {
					webMessage.getMessageInfo().setOriginalSender(originalSender);
				});
		addFormItem(originalSender, "Original Sender"); 
		
		binder.forField(finalRecipient).withValidator((Validator<String>) (value, context) -> {
			if (value.length() < 1) {
                return ValidationResult
                        .error("Final Recipient must not be empty!");
            }
			return ValidationResult.ok();
		}).bind(
				webMessage -> webMessage.getMessageInfo()!=null?webMessage.getMessageInfo().getFinalRecipient():"",
				(webMessage,finalRecipient) -> {
					webMessage.getMessageInfo().setFinalRecipient(finalRecipient);
				});
		addFormItem(finalRecipient, "Final Recipient");
		
		
		binder.forField(toParty).withValidator((Validator<WebMessageDetail.Party>) (value, context) -> {
			if (value == null) {
                return ValidationResult
                        .error("ToParty must not be empty!");
            }
			return ValidationResult.ok();
		}).bind(
				webMessage -> webMessage.getMessageInfo()!=null && webMessage.getMessageInfo().getTo()!=null?
						webMessage.getMessageInfo().getTo() :null,
				(webMessage,toParty) -> {
					webMessage.getMessageInfo().setTo(toParty);
				});
		addFormItem(toParty, "ToParty");

		binder.forField(service).bind(
				webMessage -> webMessage.getMessageInfo()!=null?webMessage.getMessageInfo().getService().getServiceString():"",
				(webMessage,service) -> {
					webMessage.getMessageInfo().getService().setService(service);
				});
		addFormItem(service, "Service");

		binder.forField(action).bind(
				webMessage -> webMessage.getMessageInfo()!=null?webMessage.getMessageInfo().getAction().getAction():"",
				(webMessage,action) -> {
					webMessage.getMessageInfo().getAction().setAction(action);
				});
		addFormItem(action, "Action");
		
		binder.forField(fromParty).bind(
				webMessage -> webMessage.getMessageInfo()!=null && webMessage.getMessageInfo().getFrom()!=null?
						webMessage.getMessageInfo().getFrom().toString():"",
				(webMessage,fromParty) -> {
					webMessage.getMessageInfo().getFrom().setPartyString(fromParty);
				});
		addFormItem(fromParty, "From Party");
		
	}

	public ConnectorTestMessageForm(Component... components) {
		super(components);
		// TODO Auto-generated constructor stub
	}
	
	public void setParties(List<DomibusConnectorParty> parties) {
		
		toParty.setItems(parties.stream()
				.filter(p -> p.getRoleType().equals(PartyRoleType.INITIATOR))
								.map(p -> new WebMessageDetail.Party(p.getPartyId(), p.getPartyIdType())));
	}
	
	public void setMessage(WebMessage message) {
		this.removeAll();
		fillForm();
		binder.setBean(message);
	}
	
	public WebMessage getMessage() {
		return binder.getBean();
	}

	public Binder<WebMessage> getBinder() {
		return binder;
	}
}