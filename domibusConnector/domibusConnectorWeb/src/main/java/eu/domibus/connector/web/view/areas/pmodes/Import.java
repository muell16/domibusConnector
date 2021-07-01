package eu.domibus.connector.web.view.areas.pmodes;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.web.component.LumoLabel;
import eu.domibus.connector.web.service.WebPModeService;
import eu.domibus.connector.web.view.areas.configuration.TabMetadata;
import eu.domibus.connector.web.view.areas.configuration.util.ConfigurationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;



@Component
@UIScope
@Route(value = Import.ROUTE, layout = PmodeLayout.class)
@TabMetadata(title = "Import PMode-Set", tabGroup = PmodeLayout.TAB_GROUP_NAME)
public class Import extends VerticalLayout {

	public static final String ROUTE = "import";

	WebPModeService pmodeService;
	
	byte[] pmodeFile = null;
	
	Div areaImportResult = new Div();
	
	Div areaPModeFileUploadResult = new Div();
	LumoLabel pModeFileUploadResultLabel = new LumoLabel();
	
	byte[] connectorstore = null;
	
	Div areaConnectorstoreUploadResult = new Div();
	LumoLabel connectorstoreUploadResultLabel = new LumoLabel();
	
	TextArea pModeSetDescription = new TextArea("Description:");
	TextField connectorstorePwd = new TextField("Connectorstore password");

	public Import(@Autowired WebPModeService pmodeService, @Autowired ConfigurationUtil util, @Autowired DataTables dataTables) {
		this.pmodeService = pmodeService;
		
		add(areaImportResult);
		
		Div areaPmodeFileUpload = createPModeImportArea();
		
		add(areaPmodeFileUpload);
		add(areaPModeFileUploadResult);

		Div areaPModeSetDescription = createPModeSetDescriptionArea();
		
		add(areaPModeSetDescription);
		
		Div areaConnectorstoreUpload = createConnectorstoreUploadArea();
		
		add(areaConnectorstoreUpload);
		add(areaConnectorstoreUploadResult);
		
		connectorstorePwd.setHelperText("The password of the truststore.");
		add(connectorstorePwd);
		
		Button importBtn = new Button();
		importBtn.setIcon(new Icon(VaadinIcon.EDIT));
		importBtn.setText("Import PMode-Set");
		importBtn.addClickListener(e -> {
			boolean result = false;
			result = pmodeService.importPModes(pmodeFile, pModeSetDescription.getValue(), connectorstore, connectorstorePwd.getValue());
			showOutput(result, result?"PMode-Set successfully imported!":"Import of PMode-Set failed!");
		});
		importBtn.setEnabled(true);
		
		add(importBtn);
		

		
	}
	
	private Div createPModeImportArea() {
		Div areaPmodeFileUpload = new Div();
		
		areaPmodeFileUpload.add(new LumoLabel("Upload new PMode file:"));
		
		MemoryBuffer  buffer = new MemoryBuffer ();
		
		Upload upload = new Upload(buffer);
		upload.setMaxFiles(1);
		upload.setId("Upload PModes-File");
		upload.setAcceptedFileTypes("application/xml", "text/xml");
		
		upload.addSucceededListener(event -> {
			pmodeFile = ((ByteArrayOutputStream) buffer.getFileData().getOutputBuffer())
                            .toByteArray();
			String fileName = buffer.getFileName();
			pModeFileUploadResultLabel.setText("File "+fileName+" uploaded");
			pModeFileUploadResultLabel.getStyle().set("color", "green");
			areaPModeFileUploadResult.add(pModeFileUploadResultLabel);
		});
		upload.addFailedListener(e -> {
			pModeFileUploadResultLabel.setText("File upload failed!");
			pModeFileUploadResultLabel.getStyle().set("color", "red");
			areaPModeFileUploadResult.add(pModeFileUploadResultLabel);
		});
		
		
		areaPmodeFileUpload.add(upload);
		
		return areaPmodeFileUpload;
	}
	
	private Div createPModeSetDescriptionArea() {
		Div areaPModeSetDescription = new Div();
		
		pModeSetDescription.setHelperText("Describes the contents of the PMode Set like project or use-case name");
		pModeSetDescription.setRequired(true);
		
		areaPModeSetDescription.add(pModeSetDescription);
		
		return areaPModeSetDescription;
	}

	private Div createConnectorstoreUploadArea() {
		Div areaConnectorstoreUpload = new Div();
		
		areaConnectorstoreUpload.add(new LumoLabel("Upload new Connectorstore file:"));
		
		MemoryBuffer  buffer = new MemoryBuffer ();
		
		Upload upload = new Upload(buffer);
		upload.setMaxFiles(1);
		upload.setId("Upload Connectorstore");
//		upload.setAcceptedFileTypes("application/xml", "text/xml");

		upload.addSucceededListener(event -> {
			connectorstore = ((ByteArrayOutputStream) buffer.getFileData().getOutputBuffer())
                            .toByteArray();
			String fileName = buffer.getFileName();
			connectorstoreUploadResultLabel.setText("File "+fileName+" uploaded");
			connectorstoreUploadResultLabel.getStyle().set("color", "green");
			areaConnectorstoreUploadResult.add(connectorstoreUploadResultLabel);
		});
		upload.addFailedListener(e -> {
			connectorstoreUploadResultLabel.setText("File upload failed!");
			connectorstoreUploadResultLabel.getStyle().set("color", "red");
			areaConnectorstoreUploadResult.add(connectorstoreUploadResultLabel);
		});
		
		
		areaConnectorstoreUpload.add(upload);

		
		
		return areaConnectorstoreUpload;
	}
	
	private void showOutput(boolean success, String text) {
		areaImportResult.removeAll();
		
		LumoLabel resultLabel = new LumoLabel();
		resultLabel.setText("PMode-Set successfully imported!");
		if(success) {
			resultLabel.getStyle().set("color", "green");
		}else {
			resultLabel.setText("Import of PMode-Set failed!");
			resultLabel.getStyle().set("color", "red");
		}
		areaImportResult.add(resultLabel);
		
		
	}
}
