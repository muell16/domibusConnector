package eu.domibus.connector.ui.view.areas.messages;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;

import eu.domibus.connector.ui.component.LumoCheckbox;
import eu.domibus.connector.ui.component.LumoLabel;
import eu.domibus.connector.ui.dto.WebMessage;
import eu.domibus.connector.ui.dto.WebMessageDetail;
import eu.domibus.connector.ui.layout.DCVerticalLayoutWithWebMessageGrid;
import eu.domibus.connector.ui.persistence.service.DomibusConnectorWebMessagePersistenceService;
import eu.domibus.connector.ui.service.WebMessageService;
import eu.domibus.connector.ui.view.areas.configuration.TabMetadata;

import org.springframework.core.annotation.Order;
import org.springframework.data.domain.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@UIScope
@Route(value = MessagesList.ROUTE, layout = MessageLayout.class)
@Order(1)
@TabMetadata(title = "All Messages", tabGroup = MessageLayout.TAB_GROUP_NAME)
public class MessagesList extends VerticalLayout implements AfterNavigationObserver {

	public static final String ROUTE = "messagelist";
	
	DCVerticalLayoutWithWebMessageGrid gridLayout;
	private WebMessagesGrid grid;
	
	private final DomibusConnectorWebMessagePersistenceService dcMessagePersistenceService;

	private final MessageDetails details;


	public MessagesList(WebMessageService messageService, DomibusConnectorWebMessagePersistenceService messagePersistenceService, MessageDetails details) {
		this.dcMessagePersistenceService = messagePersistenceService;
		this.details = details;
	}

	@PostConstruct
	void init() {

		grid = new WebMessagesGrid(details, dcMessagePersistenceService);
		
		gridLayout = new DCVerticalLayoutWithWebMessageGrid(grid);
		
		gridLayout.setVisible(true);
		gridLayout.setHeight("100vh");
		add(gridLayout);
		setSizeFull();
	}

	@Nullable
	private String getTxt(TextField field) {
		String txt = null;
		if (!field.getValue().isEmpty()) {
			txt = field.getValue();
		}
		return txt;
	}

	public void reloadList() {
		grid.reloadList();
	}


	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		reloadList();
	}


}
