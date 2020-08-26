package eu.domibus.connector.web.view.areas.testing;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.web.view.MainLayout;

//@HtmlImport("styles/shared-styles.html")
//@StyleSheet("styles/grid.css")
@UIScope
@org.springframework.stereotype.Component
@Route(value = ConnectorTests.ROUTE, layout = MainLayout.class)
public class ConnectorTests extends VerticalLayout {

	public static final String ROUTE = "connectortests";

	public ConnectorTests() {
		// TODO Auto-generated constructor stub
	}

}
