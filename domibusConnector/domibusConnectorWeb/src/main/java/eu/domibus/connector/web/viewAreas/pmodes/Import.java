package eu.domibus.connector.web.viewAreas.pmodes;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.servlet.annotation.MultipartConfig;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.spring.annotation.UIScope;

@HtmlImport("styles/shared-styles.html")
@StyleSheet("styles/grid.css")
@Component
@UIScope
@MultipartConfig
public class Import extends VerticalLayout {

	public Import() {
		Div areaImporter = new Div();
		
		MemoryBuffer buffer = new MemoryBuffer();
		
		Upload upload = new Upload(buffer);
		upload.setAcceptedFileTypes("application/xml", "text/xml");

		upload.addSucceededListener(event -> {
//		    Component component = createComponent(event.getMIMEType(),
//		            event.getFileName(),
//		            buffer.getInputStream(event.getFileName()));
		    
		    showOutput(buffer.getInputStream());
		});
		
		areaImporter.add(upload);
		
		add(areaImporter);
	}

	private void showOutput(InputStream inputStream) {
		Div areaImporter = new Div();
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(inputStream, writer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String theString = writer.toString();
		
		TextArea area = new TextArea();
		area.setValue(theString);
		
		areaImporter.add(area);
		
		add(areaImporter);
	}

}
