package eu.domibus.connector.web.view.areas.pmodes;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import eu.domibus.connector.web.view.areas.configuration.util.ConfigurationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.web.component.LumoLabel;
import eu.domibus.connector.web.service.WebPModeService;



@Component
@UIScope
public class Import extends VerticalLayout {
	
	WebPModeService pmodeService;

	public Import(@Autowired WebPModeService pmodeService, @Autowired ConfigurationUtil util, @Autowired DataTables dataTables) {
		this.pmodeService = pmodeService;
		
		Div areaImporter = new Div();
		
		MemoryBuffer  buffer = new MemoryBuffer ();
		
		Upload upload = new Upload(buffer);
		upload.setMaxFiles(1);
		upload.setId("PModes-Upload");
		upload.setAcceptedFileTypes("application/xml", "text/xml");

		upload.addSucceededListener(event -> {
			boolean result = false;
			byte[] contents = ((ByteArrayOutputStream) buffer.getFileData().getOutputBuffer())
                            .toByteArray();
		    result = pmodeService.importPModes(contents, util);
		    showOutput(contents, result);
		    dataTables.reloadActions();
		    dataTables.reloadParties();
		    dataTables.reloadServices();
		});
		
		
		areaImporter.add(upload);
		
		add(areaImporter);
	}

	private void showOutput(byte[] contents, boolean success) {
		VerticalLayout areaResult = new VerticalLayout();
		
		LumoLabel resultLabel = new LumoLabel();
		if(success) {
			resultLabel.setText("PModes successfully imported!");
			resultLabel.getStyle().set("color", "green");
		}else {
			resultLabel.setText("Import of PModes failed!");
			resultLabel.getStyle().set("color", "red");
		}
		areaResult.add(resultLabel);
		
		TextArea area = new TextArea();
		try {
			area.setValue(new String(contents, "UTF-8"));
			area.setWidth("80vw");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		areaResult.setWidth("100vw");
		areaResult.add(area);
		
		add(areaResult);
	}

}
