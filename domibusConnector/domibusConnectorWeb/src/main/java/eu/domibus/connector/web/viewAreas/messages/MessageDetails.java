package eu.domibus.connector.web.viewAreas.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.web.dto.WebMessageDetail;
import eu.domibus.connector.web.dto.WebMessageEvidence;
import eu.domibus.connector.web.forms.ConnectorMessageForm;
import eu.domibus.connector.web.service.WebMessageService;

@HtmlImport("styles/shared-styles.html")
//@StyleSheet("styles/grid.css")
@Component
@UIScope
public class MessageDetails extends VerticalLayout {

	private WebMessageService messageService;
	private ConnectorMessageForm messageForm = new ConnectorMessageForm();
	private VerticalLayout messageEvidencesArea = new VerticalLayout();  

	public MessageDetails(@Autowired WebMessageService messageService) {

		this.messageService = messageService;

		VerticalLayout messageDetailsArea = new VerticalLayout(); 
		messageForm.getStyle().set("margin-top","25px");

		messageDetailsArea.add(messageForm);
		//setAlignItems(Alignment.START);
		messageForm.setEnabled(true);
		setSizeFull();
		messageDetailsArea.setHeight("100vh");
		messageDetailsArea.setWidth("500px");
		add(messageDetailsArea);

		setHeight("100vh");
	}


	public void loadMessageDetails(String connectorMessageId) {
		WebMessageDetail messageByConnectorId = messageService.getMessageByConnectorId(connectorMessageId);
		messageForm.setConnectorMessage(messageByConnectorId, this);

		if(!messageByConnectorId.getEvidences().isEmpty()) {
			messageEvidencesArea.removeAll();

			Div evidences = new Div();
			evidences.setWidth("100vw");
			Label evidencesLabel = new Label();
			evidencesLabel.setText("Evidences:");
			evidencesLabel.getStyle().set("font-size", "20px");
			evidences.add(evidencesLabel);

			messageEvidencesArea.add(evidences);

			Div details = new Div();
			details.setWidth("100vw");

			Grid<WebMessageEvidence> grid = new Grid<>();

			grid.setItems(messageByConnectorId.getEvidences());

			grid.addColumn(WebMessageEvidence::getEvidenceType).setHeader("Evidence Type").setWidth("300px");
			grid.addColumn(WebMessageEvidence::getDeliveredToGatewayString).setHeader("Delivered to Gateway").setWidth("300px");
			grid.addColumn(WebMessageEvidence::getDeliveredToBackendString).setHeader("Delivered to Backend").setWidth("300px");

			grid.setWidth("1000px");
			grid.setHeight("210px");
			grid.setMultiSort(true);

			for(Column<WebMessageEvidence> col : grid.getColumns()) {
				col.setSortable(true);
				col.setResizable(true);
			}

			details.add(grid);



			messageEvidencesArea.add(details);

			messageEvidencesArea.setWidth("100vw");
			add(messageEvidencesArea);
			messageEvidencesArea.setVisible(true);
		}
	}

}
