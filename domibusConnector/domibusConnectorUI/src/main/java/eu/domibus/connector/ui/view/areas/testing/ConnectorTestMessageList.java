package eu.domibus.connector.ui.view.areas.testing;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.common.DomibusConnectorDefaults;
import eu.domibus.connector.ui.dto.WebMessage;
import eu.domibus.connector.ui.layout.DCVerticalLayoutWithTitleAndHelpButton;
import eu.domibus.connector.ui.persistence.service.DomibusConnectorWebMessagePersistenceService;
import eu.domibus.connector.ui.service.WebConnectorTestService;
import eu.domibus.connector.ui.service.WebMessageService;
import eu.domibus.connector.ui.view.areas.configuration.TabMetadata;
import eu.domibus.connector.ui.view.areas.messages.MessageDetails;
import eu.domibus.connector.ui.view.areas.messages.WebMessagesGrid;

@Component
@UIScope
@Route(value = ConnectorTestMessageList.ROUTE, layout = ConnectorTestsLayout.class)
@Order(1)
@TabMetadata(title = "Connector Test Messages List", tabGroup = ConnectorTestsLayout.TAB_GROUP_NAME)
public class ConnectorTestMessageList extends DCVerticalLayoutWithTitleAndHelpButton implements AfterNavigationObserver {

	public static final String ROUTE = "c2cmessages";
	
	public static final String TITLE = "Connector Test Messages";
	public static final String HELP_ID = "ui/c2ctests/connector_test_messages_list.html";
	
	private final MessageDetails details;
	private final WebMessageService messageService;
	private final WebConnectorTestService testService;
	private final DomibusConnectorWebMessagePersistenceService dcMessagePersistenceService;
	
	private String connectorTestBackendName = DomibusConnectorDefaults.DEFAULT_TEST_BACKEND;
	
	WebMessagesGrid grid;
	
	public ConnectorTestMessageList(WebMessageService messageService,
									MessageDetails details,
									Optional<WebConnectorTestService> testService,
									DomibusConnectorWebMessagePersistenceService dcMessagePersistenceService) {
		super(HELP_ID, TITLE);
		this.messageService = messageService;
		this.details = details;
		this.testService = testService.orElse(null);
		this.dcMessagePersistenceService = dcMessagePersistenceService;
	}

	@PostConstruct
	void init() {
		grid = new WebMessagesGrid(details, dcMessagePersistenceService);
		
		grid.setVisible(true);
		
		add(grid);
	}
	
	@Override
	public void afterNavigation(AfterNavigationEvent arg0) {
		if(testService != null)
			connectorTestBackendName = testService.getConnectorTestBackendName();
		WebMessage example = new WebMessage();
		example.setBackendName(connectorTestBackendName);
		grid.setExampleWebMessage(example);
		grid.reloadList();
//		grid.setItems(messageService.getConnectorTestMessages(connectorTestBackendName));
		
	}
	

}
