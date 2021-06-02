package eu.domibus.connector.web.view.areas.messages;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.web.component.LumoLabel;
import eu.domibus.connector.web.dto.WebMessage;
import eu.domibus.connector.web.view.MainLayout;

@UIScope
@ParentLayout(MainLayout.class)
@RoutePrefix(Messages.ROUTE_PREFIX)
@Route(value = Messages.ROUTE, layout = MainLayout.class)
@org.springframework.stereotype.Component
public class Messages extends VerticalLayout implements RouterLayout, BeforeEnterObserver
{
	private static final long serialVersionUID = 1L;

	public static final String ROUTE = "messages";
	public static final String ROUTE_PREFIX = "messages";


	// TODO: use TabRouterHelper from Stephan
	Tabs messagesMenu = new Tabs();

	Tab messagesListTab;
	Tab messageDetailsTab;
	Tab searchTab;
	Tab reportsTab;

	MessagesList messagesListView;
	MessageDetails messageDetailsView;
	Search searchView;
	Reports reportsView;

	public Messages() {

		messagesListTab = new Tab(createRouterLink("Messages List", new Icon(VaadinIcon.ENVELOPES_O), MessagesList.class));

		messageDetailsTab = new Tab(createRouterLink("Message Details", new Icon(VaadinIcon.ENVELOPE_OPEN_O), MessageDetails.class));

		searchTab = new Tab(createRouterLink("Search", new Icon(VaadinIcon.SEARCH), Search.class));

		reportsTab = new Tab(createRouterLink("Reports", new Icon(VaadinIcon.RECORDS), Reports.class));

		messagesMenu.add(messagesListTab, messageDetailsTab, searchTab, reportsTab);

		messagesMenu.setOrientation(Tabs.Orientation.HORIZONTAL);

		messagesMenu.addSelectedChangeListener(e -> {
			e.getPreviousTab().setSelected(false);
			e.getSelectedTab().setSelected(true);
		});

		add(messagesMenu);
	}

	public void showMessageDetails(WebMessage connectorMessage) {
		messagesMenu.setSelectedTab(messageDetailsTab);

		// TODO klasse angeben!
		UI.getCurrent().navigate(Messages.ROUTE_PREFIX +"/"+ MessageDetails.ROUTE +"/"+ connectorMessage.getConnectorMessageId());
//		UI.getCurrent().navigate(MessageDetails.class);
	}

	public void showSearch(WebMessage message) {
		messagesMenu.setSelectedTab(searchTab);

		// TODO klasse angeben!
		UI.getCurrent().navigate(Messages.ROUTE_PREFIX +"/"+ Search.ROUTE +"/");
	}

	public void showReports(WebMessage message) {
		messagesMenu.setSelectedTab(searchTab);

		// TODO klasse angeben!
		UI.getCurrent().navigate(Messages.ROUTE_PREFIX +"/"+ Reports.ROUTE +"/");
	}

	public void showMessagesList() {
		messagesMenu.setSelectedTab(messagesListTab);

		UI.getCurrent().navigate(MessagesList.class);
	}

	private RouterLink createRouterLink(String tabLabel, Icon tabIcon, Class<? extends Component> component) {
		Span tabText = new Span(tabLabel);
		tabText.getStyle().set("font-size", "15px");

		tabIcon.setSize("15px");

		HorizontalLayout tabLayout = new HorizontalLayout(tabIcon, tabText);
		tabLayout.setAlignItems(Alignment.CENTER);

		RouterLink routerLink = new RouterLink(null, component);
		routerLink.add(tabLayout);

		return routerLink;
	}

	public Dialog getErrorDialog(String header, String message) {
		Dialog diag = new Dialog();

		Div headerContent = new Div();
		Label headerLabel = new Label(header);
		headerLabel.getStyle().set("font-weight", "bold");
		headerLabel.getStyle().set("font-style", "italic");
		headerContent.getStyle().set("text-align", "center");
		headerContent.getStyle().set("padding", "10px");
		headerContent.add(headerLabel);
		diag.add(headerContent);

		Div labelContent = new Div();
		LumoLabel label = new LumoLabel(message);

		labelContent.add(label);
		diag.add(labelContent);

		return diag;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent arg0) {
		if(arg0.getNavigationTarget().equals(Messages.class)) {
			if(messagesListTab.isSelected()) {
				arg0.forwardTo(MessagesList.class);
				UI.getCurrent().navigate(MessagesList.class);
			}else if (messageDetailsTab.isSelected()) {
				arg0.forwardTo(MessageDetails.class);
				UI.getCurrent().navigate(MessageDetails.class);
			}else if (searchTab.isSelected()) {
				arg0.forwardTo(Search.class);
				UI.getCurrent().navigate(Search.class);
			}else if (reportsTab.isSelected()) {
				arg0.forwardTo(Reports.class);
				UI.getCurrent().navigate(Reports.class);
			}
		}
	}

	public MessagesList getMessagesListView() {
		return messagesListView;
	}

	public void setMessagesListView(MessagesList messagesListView) {
		this.messagesListView = messagesListView;
	}

	public MessageDetails getMessageDetailsView() {
		return messageDetailsView;
	}

	public void setMessageDetailsView(MessageDetails messageDetailsView) {
		this.messageDetailsView = messageDetailsView;
	}

	public Search getSearchView() {
		return searchView;
	}

	public void setSearchView(Search searchView) {
		this.searchView = searchView;
	}

	public Reports getReportsView() {
		return reportsView;
	}

	public void setReportsView(Reports reportsView) {
		this.reportsView = reportsView;
	}

}
