package eu.domibus.connector.web.viewAreas.pmodes;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;

@HtmlImport("styles/shared-styles.html")
@StyleSheet("styles/grid.css")
@Component
@UIScope
public class DataTables extends VerticalLayout {

	public DataTables() {
		// TODO Auto-generated constructor stub
	}

}
