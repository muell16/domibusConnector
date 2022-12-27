//package eu.domibus.connector.ui.view.areas.messages;
//
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.icon.Icon;
//import com.vaadin.flow.component.icon.VaadinIcon;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.router.Route;
//import com.vaadin.flow.spring.annotation.UIScope;
//import eu.domibus.connector.ui.dto.WebMessage;
//import eu.domibus.connector.ui.persistence.service.impl.DomibusConnectorWebMessagePersistenceService;
//import eu.domibus.connector.ui.service.WebMessageService;
//import eu.domibus.connector.ui.view.areas.configuration.TabMetadata;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.Optional;
//
//@Component
//@UIScope
//@Route(value = Search.ROUTE, layout= MessageLayout.class)
//@Order(2)
//@TabMetadata(title = "Search", tabGroup = MessageLayout.TAB_GROUP_NAME)
//public class Search extends VerticalLayout {
//
//	public static final String ROUTE = "search";
//
//	private static final long serialVersionUID = 1L;
//
//	private WebMessageService messageService;
//
//	private final DomibusConnectorWebMessagePersistenceService dcMessagePersistenceService;
//
//	TextField searchMessageIdText = new TextField();
//	TextField searchEbmsIdText = new TextField();
//	TextField searchBackendMessageIdText = new TextField();
//	TextField searchConversationIdText = new TextField();
//
//	private VerticalLayout main = new VerticalLayout();
//
//	public Search(WebMessageService messageService, DomibusConnectorWebMessagePersistenceService dcMessagePersistenceService) {
//		this.messageService = messageService;
//		this.dcMessagePersistenceService = dcMessagePersistenceService;
//	}
//
//	@PostConstruct
//	void init() {
//
//		HorizontalLayout connectorMessageIdSearch = new HorizontalLayout();
//
//		searchMessageIdText.setPlaceholder("Search by Connector Message ID");
//		searchMessageIdText.setWidth("300px");
//		connectorMessageIdSearch.add(searchMessageIdText);
//
//		Button searchConnectorMessageIdBtn = new Button(new Icon(VaadinIcon.SEARCH));
//		searchConnectorMessageIdBtn.addClickListener(e -> {
//			Optional<WebMessage> messageByConnectorId = messageService.getMessageByConnectorId(searchMessageIdText.getValue());
//			searchMessageIdText.setValue("");
//			messageByConnectorId.ifPresent(msg->searchConnectorMessageIdBtn.getUI()
//					.ifPresent(ui->ui.navigate(MessageDetails.class, msg.getConnectorMessageId())));
//		});
//		connectorMessageIdSearch.add(searchConnectorMessageIdBtn);
//
//		add(connectorMessageIdSearch);
//
//		HorizontalLayout ebmsIdSearch = new HorizontalLayout();
//
//		searchEbmsIdText.setPlaceholder("Search by EBMS Message ID");
//		searchEbmsIdText.setWidth("300px");
//		ebmsIdSearch.add(searchEbmsIdText);
//
//		Button searchEbmsIdBtn = new Button(new Icon(VaadinIcon.SEARCH));
//		searchEbmsIdBtn.addClickListener(e -> {
//			Optional<WebMessage> messageByEbmsId = messageService.getMessageByEbmsId(searchEbmsIdText.getValue());
//			searchEbmsIdText.setValue("");
//			messageByEbmsId.ifPresent(msg -> searchEbmsIdBtn.getUI()
//					.ifPresent(ui -> ui.navigate(MessageDetails.class, msg.getConnectorMessageId())));
//		});
//		ebmsIdSearch.add(searchEbmsIdBtn);
//
//		add(ebmsIdSearch);
//
//		HorizontalLayout backendMessageIdSearch = new HorizontalLayout();
//
//		searchBackendMessageIdText.setPlaceholder("Search by Backend Message ID");
//		searchBackendMessageIdText.setWidth("300px");
//		backendMessageIdSearch.add(searchBackendMessageIdText);
//
//		Button searchBackendMessageIdBtn = new Button(new Icon(VaadinIcon.SEARCH));
//		searchBackendMessageIdBtn.addClickListener(e -> {
//			// on search success navigate to details view
//			Optional<WebMessage> messageByBackendMessageId = messageService.getMessageByBackendMessageId(searchBackendMessageIdText.getValue());
//			searchBackendMessageIdText.setValue("");
//			messageByBackendMessageId.ifPresent(m -> searchBackendMessageIdBtn.getUI().ifPresent(ui ->
//					ui.navigate(MessageDetails.class, m.getConnectorMessageId()))
//			);
//		});
//		backendMessageIdSearch.add(searchBackendMessageIdBtn);
//
//		add(backendMessageIdSearch);
//
//		HorizontalLayout conversationIdSearch = new HorizontalLayout();
//
//		searchConversationIdText.setPlaceholder("Search by Conversation ID");
//		searchConversationIdText.setWidth("300px");
//		conversationIdSearch.add(searchConversationIdText);
//
//		Button searchConversationIdBtn = new Button(new Icon(VaadinIcon.SEARCH));
//		searchConversationIdBtn.addClickListener(e -> {
//			// TODO: fix
//		});
//		conversationIdSearch.add(searchConversationIdBtn);
//
//		add(conversationIdSearch);
//
//		add(main);
//
//		setHeight("100vh");
//		setWidth("100vw");
//	}
//}
