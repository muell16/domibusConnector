package eu.domibus.connector.web.view;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.spring.annotation.UIScope;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

//@HtmlImport("styles/shared-styles.html")
@UIScope
@Route(value = DashboardView.ROUTE, layout = MainLayout.class)
@PageTitle("domibusConnector - Administrator")
public class DashboardView extends VerticalLayout
{

	public static final String ROUTE = "";

	Label l = new Label();
	
    public DashboardView() {
    	l.setText("Welcome to Domibus Connector Administration UI");
		add(l);
    }


}
