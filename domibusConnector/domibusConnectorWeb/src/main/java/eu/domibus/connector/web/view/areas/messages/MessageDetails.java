package eu.domibus.connector.web.view.areas.messages;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.web.component.LumoLabel;
import eu.domibus.connector.web.dto.WebMessage;
import eu.domibus.connector.web.dto.WebMessageEvidence;
import eu.domibus.connector.web.forms.ConnectorMessageForm;
import eu.domibus.connector.web.service.WebMessageService;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

//@HtmlImport("styles/shared-styles.html")
//@StyleSheet("styles/grid.css")
@Component
@UIScope
public class MessageDetails extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger LOGGER = LogManager.getLogger(MessageDetails.class);

	private WebMessageService messageService;
	private ConnectorMessageForm messageForm = new ConnectorMessageForm();
	private VerticalLayout messageEvidencesArea = new VerticalLayout();  

	public MessageDetails(@Autowired WebMessageService messageService) {

		this.messageService = messageService;
		
		Button refreshBtn = new Button(new Icon(VaadinIcon.REFRESH));
		refreshBtn.setText("Refresh");
		refreshBtn.addClickListener(e -> {
			if(messageForm.getBinder()!=null)loadMessageDetails(messageForm.getBinder().getBean());
			});
		
		HorizontalLayout buttons = new HorizontalLayout(
				refreshBtn
			    );
		buttons.setWidth("100vw");
		add(buttons);

		VerticalLayout messageDetailsArea = new VerticalLayout(); 
		messageForm.getStyle().set("margin-top","25px");

		messageDetailsArea.add(messageForm);
		//setAlignItems(Alignment.START);
		messageForm.setEnabled(true);
//		messageDetailsArea.setHeight("100vh");
		messageDetailsArea.setWidth("500px");
		add(messageDetailsArea);
		
		add(messageEvidencesArea);

		setSizeFull();
//		setHeight("100vh");
	}


	public void loadMessageDetails(WebMessage connectorMessage) {
		
		Optional<WebMessage> optionalMessage = null;
				
		if(!StringUtils.isEmpty(connectorMessage.getConnectorMessageId())) {
			LOGGER.debug("MessageDetails loaded with connectorMessageId [{}]", connectorMessage.getConnectorMessageId());
			optionalMessage = messageService.getMessageByConnectorId(connectorMessage.getConnectorMessageId());
		}
		
		if ((optionalMessage == null || !optionalMessage.isPresent()) && !StringUtils.isEmpty(connectorMessage.getBackendMessageId())) {
			LOGGER.debug("MessageDetails loaded with backendMessageId [{}]", connectorMessage.getBackendMessageId());
			optionalMessage = messageService.getMessageByBackendMessageId(connectorMessage.getBackendMessageId());
		}
		
		if ((optionalMessage == null || !optionalMessage.isPresent()) && !StringUtils.isEmpty(connectorMessage.getEbmsMessageId())) {
			LOGGER.debug("MessageDetails loaded with ebmsMessageId [{}]", connectorMessage.getEbmsMessageId());
			optionalMessage = messageService.getMessageByEbmsId(connectorMessage.getEbmsMessageId());
		}
		
		if (optionalMessage == null || !optionalMessage.isPresent()) {
			String errorMessage = String.format("No message found within database with connectorMessageId [%s], ebmsMessageId [%s] or backendMessageId [%s] !", connectorMessage.getConnectorMessageId(), connectorMessage.getEbmsMessageId(), connectorMessage.getBackendMessageId());
			LOGGER.warn(errorMessage);
			Notification.show(errorMessage);
		}

			WebMessage webMessageDetail = optionalMessage.get();
			messageForm.setConnectorMessage(webMessageDetail);

			if (!webMessageDetail.getEvidences().isEmpty()) {
				messageEvidencesArea.removeAll();

				Div evidences = new Div();
				evidences.setWidth("100vw");
				LumoLabel evidencesLabel = new LumoLabel();
				evidencesLabel.setText("Evidences:");
				evidencesLabel.getStyle().set("font-size", "20px");
				evidences.add(evidencesLabel);

				messageEvidencesArea.add(evidences);

				Div details = new Div();
				details.setWidth("100vw");

				Grid<WebMessageEvidence> grid = new Grid<>();

				grid.setItems(webMessageDetail.getEvidences());

				grid.addColumn(WebMessageEvidence::getEvidenceType).setHeader("Evidence Type").setWidth("300px");
				grid.addColumn(WebMessageEvidence::getDeliveredToGatewayString).setHeader("Delivered to Gateway").setWidth("300px");
				grid.addColumn(WebMessageEvidence::getDeliveredToBackendString).setHeader("Delivered to Backend").setWidth("300px");

				grid.setWidth("1000px");
				grid.setHeight("210px");
				grid.setMultiSort(true);

				for (Column<WebMessageEvidence> col : grid.getColumns()) {
					col.setSortable(true);
					col.setResizable(true);
				}

				details.add(grid);


				messageEvidencesArea.add(details);

				messageEvidencesArea.setWidth("100vw");
				//			add(messageEvidencesArea);
				messageEvidencesArea.setVisible(true);
			}
	
	}

}
