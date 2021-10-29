package eu.domibus.connector.ui.view.areas.testing;

import java.io.ByteArrayInputStream;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.domain.model.DomibusConnectorParty;
import eu.domibus.connector.ui.component.LumoLabel;
import eu.domibus.connector.ui.dto.WebMessage;
import eu.domibus.connector.ui.dto.WebMessageDetail;
import eu.domibus.connector.ui.dto.WebMessageFile;
import eu.domibus.connector.ui.dto.WebMessageFileType;
import eu.domibus.connector.ui.forms.ConnectorTestMessageForm;
import eu.domibus.connector.ui.service.WebConnectorTestService;
import eu.domibus.connector.ui.service.WebPModeService;
import eu.domibus.connector.ui.view.areas.configuration.TabMetadata;

@Component
@UIScope
@Route(value = SendC2CTestMessage.ROUTE, layout = ConnectorTestsLayout.class)
@Order(2)
@TabMetadata(title = "Send Connector Test Message", tabGroup = ConnectorTestsLayout.TAB_GROUP_NAME)
public class SendC2CTestMessage extends VerticalLayout implements AfterNavigationObserver{

	public static final String ROUTE = "sendmessage";

	private ConnectorTestMessageForm messageForm;
	private VerticalLayout messageFilesArea = new VerticalLayout();
	Div resultArea;
	
	Button uploadFileButton;
	Button submitMessageButton;

	private WebPModeService pModeService;
	private WebConnectorTestService webTestService;
	
	boolean filesEnabled = false;

	public SendC2CTestMessage(@Autowired WebPModeService pModeService, @Autowired WebConnectorTestService webTestService) {
		this.messageForm = new ConnectorTestMessageForm();
		this.webTestService = webTestService;
		this.pModeService = pModeService;
		this.messageForm.setParties(pModeService.getPartyList());

		VerticalLayout messageDetailsArea = new VerticalLayout(); 
		messageForm.getStyle().set("margin-top","25px");

		messageDetailsArea.add(messageForm);
		messageForm.setEnabled(true);
		messageDetailsArea.setWidth("500px");
		add(messageDetailsArea);

		add(messageFilesArea);

		uploadFileButton = new Button();
		uploadFileButton.setIcon(new Icon(VaadinIcon.UPLOAD));
		uploadFileButton.setText("Add File to message");
		uploadFileButton.addClickListener(e -> {
			UploadMessageFileDialog uploadFileDialog = new UploadMessageFileDialog();
			Button uploadFile = new Button(new Icon(VaadinIcon.UPLOAD));
			uploadFile.setText("Add File to message");
			uploadFile.addClickListener(e1 -> {
				if(!StringUtils.isEmpty(uploadFileDialog.getFileName()) && 
						uploadFileDialog.getFileType().getValue()!=null && 
						uploadFileDialog.getFileContents()!=null && uploadFileDialog.getFileContents().length > 0) {
					String nok = checkFileValid(uploadFileDialog.getFileName(), uploadFileDialog.getFileType().getValue());
					LumoLabel resultLabel = new LumoLabel();
					if(nok==null) {
						WebMessageFile messageFile = new WebMessageFile(
								uploadFileDialog.getFileName(), uploadFileDialog.getFileType().getValue(), uploadFileDialog.getFileContents());
						messageForm.getMessage().getFiles().add(messageFile);
						resultLabel.setText("File successfully added to message");
						resultLabel.getStyle().set("color", "green");
					}else {
						resultLabel.setText(nok);
						resultLabel.getStyle().set("color", "red");
					}
					uploadFileDialog.close();
					refreshPage(resultLabel);

				}
			});
			uploadFileDialog.add(uploadFile);
			uploadFileDialog.open();
		});

		submitMessageButton = new Button(new Icon(VaadinIcon.CLOUD_UPLOAD_O));
		submitMessageButton.setText("Submit Message");
		submitMessageButton.addClickListener(e -> {
			if(validateMessageForm()) {
				LumoLabel resultLabel = new LumoLabel();
				if(!validateMessageForSumission()) {
					resultLabel.setText("For message submission a BUSINESS_CONTENT and BUSINESS_DOCUMENT must be present!");
					resultLabel.getStyle().set("color", "red");
				}else if(webTestService == null) {
					resultLabel.setText("The service required to submit test messages is not available! Check the configuration!");
					resultLabel.getStyle().set("color", "red");
				} else {
					//				try {
					//					DomibusConnectorClientMessage msg = this.messageService.saveMessage(messageForm.getConnectorClientMessage());
					//					this.messageService.submitStoredMessage(msg);
					webTestService.submitTestMessage(messageForm.getMessage());
					resultLabel.setText("Message successfully submitted!");
					resultLabel.getStyle().set("color", "green");
					//				} catch (ConnectorClientServiceClientException e1) {
					//					resultLabel.setText("Exception thrown at connector client: "+e1.getMessage());
					//					resultLabel.getStyle().set("color", "red");
					//				}

				}
				refreshPage(resultLabel);
			}
		});
		
		HorizontalLayout buttons = new HorizontalLayout(
				uploadFileButton, submitMessageButton
				);
		buttons.setWidth("100vw");
		add(buttons);

		resultArea = new Div();

		add(resultArea);


	}

	@Override
	public void afterNavigation(AfterNavigationEvent arg0) {
		
		if(webTestService == null) {
			LumoLabel resultLabel = new LumoLabel();
			resultLabel.setText("The service required to submit test messages is not available! Check the configuration!");
			resultLabel.getStyle().set("color", "red");
			
			uploadFileButton.setEnabled(false);
			submitMessageButton.setEnabled(false);
			
			refreshPage(resultLabel);
			return;
		}
		
		WebMessage msg = new WebMessage();

		DomibusConnectorParty pParty = pModeService.getHomeParty();

		WebMessageDetail.Party homeParty = new WebMessageDetail.Party(pParty.getPartyId(), pParty.getPartyIdType());
		msg.getMessageInfo().setFrom(homeParty);

		WebMessageDetail.Action action = webTestService.getTestAction();
		msg.getMessageInfo().setAction(action);

		WebMessageDetail.Service service = webTestService.getTestService();
		msg.getMessageInfo().setService(service);

		messageForm.setMessage(msg);
		
		uploadFileButton.setEnabled(true);
		submitMessageButton.setEnabled(true);

		refreshPage(null);
	}

	private String checkFileValid(String fileName, WebMessageFileType fileType) {
		if(messageForm.getMessage().getFiles()!=null) {
			Iterator<WebMessageFile> fileIterator = messageForm.getMessage().getFiles().iterator();

			while(fileIterator.hasNext()) {
				WebMessageFile file = fileIterator.next();
				if(file.getFileName().equals(fileName)) {
					return "File with that name already part of the message!";
				}
				switch(fileType) {
				case BUSINESS_CONTENT:if(file.getFileType().equals(fileType))return "BUSINESS_CONTENT already part of the message! Must not be more than one!";
				case BUSINESS_DOCUMENT:if(file.getFileType().equals(fileType))return "BUSINESS_DOCUMENT already part of the message! Must not be more than one!";
				case DETACHED_SIGNATURE:if(file.getFileType().equals(fileType))return "DETACHED_SIGNATURE already part of the message! Must not be more than one!";
				default:
				}
			}

		}
		return null;
	}

	private boolean validateMessageForm() {
		BinderValidationStatus<WebMessage> validationStatus = messageForm.getBinder().validate();
		return validationStatus.isOk();
	}

	private boolean validateMessageForSumission() {
		boolean businessDocumentFound = false;
		boolean businessContentFound = false;
		Iterator<WebMessageFile> it = messageForm.getMessage().getFiles().iterator();
		while(it.hasNext()) {
			WebMessageFile file = it.next();
			if(file.getFileType().equals(WebMessageFileType.BUSINESS_CONTENT))
				businessContentFound = true;
			if(file.getFileType().equals(WebMessageFileType.BUSINESS_DOCUMENT))
				businessDocumentFound = true;
		}
		return businessContentFound && businessDocumentFound;
	}

	private void buildMessageFilesArea() {

		messageFilesArea.removeAll();

		Div files = new Div();
		files.setWidth("100vw");
		LumoLabel filesLabel = new LumoLabel();
		filesLabel.setText("Files:");
		filesLabel.getStyle().set("font-size", "20px");
		files.add(filesLabel);

		messageFilesArea.add(files);

		Div details = new Div();
		details.setWidth("100vw");

		if(filesEnabled) {

			Grid<WebMessageFile> grid = new Grid<>();

			grid.setItems(messageForm.getMessage().getFiles());

			grid.addComponentColumn(webMessageFile -> getDeleteFileLink(webMessageFile)).setHeader("Delete").setWidth("50px");
			grid.addComponentColumn(webMessageFile -> createDownloadButton(webMessageFile)).setHeader("Filename").setWidth("500px");
			grid.addColumn(WebMessageFile::getFileType).setHeader("Filetype").setWidth("450px");

			grid.setWidth("1000px");
			grid.setMultiSort(true);

			details.add(grid);

		}
		messageFilesArea.add(details);

		messageFilesArea.setWidth("100vw");
		messageFilesArea.setVisible(filesEnabled);
	}

	public void refreshPage(LumoLabel result) {

		filesEnabled = messageForm.getMessage()!=null && 
				messageForm.getMessage().getFiles()!=null && 
				!messageForm.getMessage().getFiles().isEmpty();

		buildMessageFilesArea();

		if(result !=null) {
			resultArea.removeAll();
			resultArea.add(result);
			resultArea.setVisible(true);
		}else {
			resultArea.removeAll();
			resultArea.setVisible(false);
		}

	}

	private Anchor createDownloadButton(WebMessageFile file) {
		Label button = new Label(file.getFileName());
		final StreamResource resource = new StreamResource(file.getFileName(),
				() -> new ByteArrayInputStream(file.getFileContent()));

		Anchor downloadAnchor = new Anchor();
		downloadAnchor.setHref(resource);
		downloadAnchor.getElement().setAttribute("download", true);
		downloadAnchor.setTarget("_blank");
		downloadAnchor.setTitle(file.getFileName());
		downloadAnchor.add(button);

		return downloadAnchor;
	}

	private Button getDeleteFileLink(WebMessageFile file) {
		Button deleteFileButton = new Button(new Icon(VaadinIcon.ERASER));
		deleteFileButton.addClickListener(e -> {
			Dialog deleteMessageDialog = new Dialog();

			Div headerContent = new Div();
			Label header = new Label("Delete file from message");
			header.getStyle().set("font-weight", "bold");
			header.getStyle().set("font-style", "italic");
			headerContent.getStyle().set("text-align", "center");
			headerContent.getStyle().set("padding", "10px");
			headerContent.add(header);
			deleteMessageDialog.add(headerContent);

			Div labelContent = new Div();
			LumoLabel label = new LumoLabel("Are you sure you want to delete this file from the message? Storage file is deleted as well!");

			labelContent.add(label);
			deleteMessageDialog.add(labelContent);

			Button delButton = new Button("Delete File");
			delButton.addClickListener(e1 -> {
				messageForm.getMessage().getFiles().remove(file);
				LumoLabel resultLabel = new LumoLabel();
				resultLabel.setText("File "+file.getFileName()+" deleted successfully");
				resultLabel.getStyle().set("color", "green");
				deleteMessageDialog.close();
				refreshPage(resultLabel);
			});
			deleteMessageDialog.add(delButton);
			deleteMessageDialog.open();

		});
		return deleteFileButton;
	}

}
