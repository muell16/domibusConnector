package eu.domibus.connector.ui.view.areas.messages;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.ui.component.WebMessagesGrid;
import eu.domibus.connector.ui.dto.WebMessage;
import eu.domibus.connector.ui.persistence.service.impl.DomibusConnectorWebMessagePersistenceService;
import eu.domibus.connector.ui.service.WebMessageService;
import eu.domibus.connector.ui.view.areas.configuration.TabMetadata;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@UIScope
@Route(value = MessagesList.ROUTE, layout = MessageLayout.class)
@Order(1)
@TabMetadata(title = "All Messages", tabGroup = MessageLayout.TAB_GROUP_NAME)
public class MessagesList extends VerticalLayout implements AfterNavigationObserver {

	public static final String ROUTE = "messagelist";

	private final MessageDetails details;
	private final DomibusConnectorWebMessagePersistenceService messagePersistenceService;

	private Grid<WebMessage> grid;
	
	public MessagesList(WebMessageService messageService, MessageDetails details, DomibusConnectorWebMessagePersistenceService messagePersistenceService) {
		this.details = details;
		this.messagePersistenceService = messagePersistenceService;

		grid = new WebMessagesGrid(details, messagePersistenceService);

		VerticalLayout gridLayout = new VerticalLayout(grid);
		
		gridLayout.setVisible(true);
		gridLayout.setHeight("100vh");
		add(gridLayout);
		setSizeFull();
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		grid.setItems(messagePersistenceService.findAll().stream());
	}
}
